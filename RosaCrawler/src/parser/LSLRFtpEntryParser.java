package parser;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.parser.UnixFTPEntryParser;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * 由于ls -lR命令返回的每一行不是绝对路径，而是
 * <pre>
 * 父目录：
 * 父目录下面的一级文件列表
 * </pre>
 * 这样的形式，所以我们修改了原有的包中的UnixFTPEntryParser这个类，来提取完整路径信息。
 * @author elegate
 *
 */
public class LSLRFtpEntryParser extends UnixFTPEntryParser
{
    /**
     * this is the regular expression used to determine the parent path of eache
     * file
     */
    private static final String PARENT_PATH_REGEX = "(\\.?/?)((\\S*)(\\s*.*)/)*((\\S*)(\\s*.*)):";

    /**
     * internal pattern the matcher tries to match, representing a file entry
     */
    private Pattern pattern = null;

    /**
     * internal match result used by the parser
     */
    private MatchResult result = null;

    /**
     * Internal PatternMatcher object used by the parser. It has protected scope
     * in case subclasses want to make use of it for their own purposes.
     */
    protected PatternMatcher matcher = null;

    private String parentPath = "";
    

    public LSLRFtpEntryParser()
    {
	parentPath = "";
	try
	{
	    matcher = new Perl5Matcher();
	    pattern = new Perl5Compiler().compile(PARENT_PATH_REGEX);
	}
	catch (MalformedPatternException e)
	{
	    throw new IllegalArgumentException("Unparseable regex supplied:  "
		    + PARENT_PATH_REGEX);
	}
    }

    public boolean parentPathMatchs(String str)
    {
	this.result = null;
	if (matcher.matches(str.trim(), this.pattern))
	{
	    this.result = matcher.getMatch();
	}
	return null != this.result;
    }

    /**
     * This method is a hook for those implementors (such as
     * VMSVersioningFTPEntryParser, and possibly others) which need to
     * perform some action upon the FTPFileList after it has been created
     * from the server stream, but before any clients see the list.
     *
     * This default implementation removes entries that do not parse as files.
     *
     * @param original Original list after it has been created from the server stream
     *
     * @return <code>original</code> unmodified.
     */
     public List<String> preParse(List<String> original) {
         return original;
     }
    /**
     * Parses a line of a unix (standard) FTP server file listing and converts
     * it into a usable format in the form of an <code> FTPFile </code>
     * instance. If the file listing line doesn't describe a file,
     * <code> null </code> is returned, otherwise a <code> FTPFile </code>
     * instance representing the files in the directory is returned.
     * <p>
     * 
     * @param entry
     *                A line of text from the file listing
     * @return An FTPFile instance corresponding to the supplied entry
     */
    public FTPFile parseFTPEntry(String entry)
    {
//	System.out.println("Entry:"+entry);
	FTPFile file = new FTPFile();
	file.setRawListing(entry);
	int type;
	boolean isDevice = false;
	if (parentPathMatchs(entry))//提取父目录
	{
	    parentPath = entry.substring(0, entry.length() - 1).trim();
	    if (parentPath.length() > 0 && parentPath.charAt(0) == '.')
		parentPath = parentPath.substring(1);
	    if (parentPath.length() > 1 && parentPath.charAt(0) != '/')
		parentPath = "/" + parentPath;
	    if (parentPath.length() > 0 && parentPath.endsWith("/"))
		parentPath = parentPath.substring(0, parentPath.length() - 1);
	}
	else if (matches(entry))
	{
	   
	    String typeStr = group(1);
	    String hardLinkCount = group(15);
	    String usr = group(16);
	    String grp = group(17);
	    String filesize = group(18);
	    String datestr = group(19) + " " + group(20);
	    String name = group(21).trim();
	    if (name.length() == 0)
		name = parentPath;
	    else
		name = parentPath + "/" + name;  //将原有的文件名替换成完整的绝对路径
	    String endtoken = group(22);
	   
	    try
	    {
		file.setTimestamp(super.parseTimestamp(datestr));
	    }
	    catch (ParseException e)
	    {
		return null; // this is a parsing failure too.
	    }

	    // bcdlfmpSs-
	    switch (typeStr.charAt(0))
	    {
	    case 'd':
		type = FTPFile.DIRECTORY_TYPE;
		break;
	    case 'l':
		type = FTPFile.SYMBOLIC_LINK_TYPE;
		break;
	    case 'b':
	    case 'c':
		isDevice = true;
		// break; - fall through
	    case 'f':
	    case '-':
		type = FTPFile.FILE_TYPE;
		break;
	    default:
		type = FTPFile.UNKNOWN_TYPE;
	    }

	    file.setType(type);

	    int g = 4;
	    for (int access = 0; access < 3; access++, g += 4)
	    {
		// Use != '-' to avoid having to check for suid and sticky bits
		file.setPermission(access, FTPFile.READ_PERMISSION, (!group(g)
			.equals("-")));
		file.setPermission(access, FTPFile.WRITE_PERMISSION, (!group(
			g + 1).equals("-")));

		String execPerm = group(g + 2);
		if (!execPerm.equals("-")
			&& !Character.isUpperCase(execPerm.charAt(0)))
		{
		    file.setPermission(access, FTPFile.EXECUTE_PERMISSION,
				    true);
		}
		else
		{
		    file.setPermission(access, FTPFile.EXECUTE_PERMISSION,
			    false);
		}
	    }

	    if (!isDevice)
	    {
		try
		{
		    file.setHardLinkCount(Integer.parseInt(hardLinkCount));
		}
		catch (NumberFormatException e)
		{
		    // intentionally do nothing
		}
	    }

	    file.setUser(usr);
	    file.setGroup(grp);

	    try
	    {
		file.setSize(Long.parseLong(filesize));
	    }
	    catch (NumberFormatException e)
	    {
		// intentionally do nothing
	    }
	    if (null == endtoken)
	    {
		file.setName(name);
	    }
	    else
	    {
		// oddball cases like symbolic links, file names
		// with spaces in them.
		name += endtoken;
		if (type == FTPFile.SYMBOLIC_LINK_TYPE)
		{

		    int end = name.indexOf(" -> ");
		    // Give up if no link indicator is present
		    if (end == -1)
		    {
			file.setName(name);
		    }
		    else
		    {
			file.setName(name.substring(0, end));
			file.setLink(name.substring(end + 4));
		    }

		}
		else
		{
		    file.setName(name);
		}
	    }
	    return file;
	}
	return null;
    }
}
