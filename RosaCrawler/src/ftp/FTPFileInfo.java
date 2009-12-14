/**
 * ftp信息相关的包
 */
package ftp;

import java.io.Serializable;

import org.apache.commons.net.ftp.FTPFile;

import resource.CategoryConfigure;
import resource.CrawlerSetting;
import utils.Tools;

/**
 * 负责存放FTP文件信息的类
 * 
 * @author elegate
 */
public class FTPFileInfo implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -3109726898645332864L;


    public static final String TO_STRING_FORMAT_STRING = "%10s%15s%20s\t%s";


//    public static final String DATE_FORMAT = ;

    /**
     * file name
     */
    protected String name = null;

    /**
     * file size
     */
    protected long size;

    /**
     * file date
     */
    protected String date;

    /**
     * absolute path
     */
    protected String path = null;
    /**
     * parent path
     */
    protected String parentPath = null;
    /**
     * file extension
     */
    protected String extension = null;

    /**
     * file classification category,see config/categories.xml
     */
    protected String category = null;

    public FTPFileInfo()
    {
    }

    /**
     * constructor
     * 
     * @param file
     *                FTPFile object
     */
    public FTPFileInfo(FTPFile file)
    {
	parseName(file.getName());
	size = file.getSize();
	date =CrawlerSetting.FTP_FILE_INFO_DATE_FORMAT.format(file.getTimestamp().getTime());
	if (file.getType() == FTPFile.DIRECTORY_TYPE)
	    category = CategoryConfigure.getCategory("directory");
	this.extension = Tools.getFileType(this.name);
	category = classify(this.extension);
	if (this.extension == null)
	    this.extension = "";
    }
    /**
     * analyze the file absolute path and get the parent path and file name
     * @param absolutePath absolute file path
     */
    private void parseName(String absolutePath)
    {
	int index = absolutePath.lastIndexOf("/");
	if (index == -1)
	{
	    this.name = absolutePath;
	    this.parentPath = "/";
	}
	else if (index + 1 < absolutePath.length())
	{
	    this.name = absolutePath.substring(index + 1);
	    this.parentPath = absolutePath.substring(0, index + 1);
	}
	else
	{
	    name = "";
	    parentPath = absolutePath;
	}
	path = absolutePath;
    }
    /**
     * get the file category,see config/categories.xml
     * @return file category
     */
    public String getCategory()
    {
	return category;
    }
    /**
     * set file category
     * @param category file category
     */
    public void setCategory(String category)
    {
	this.category = category;
    }

    // /**
    // * get the abstract file path
    // *
    // * @return the abstract file path
    // */
    // public String getPath()
    // {
    // return parentPath + "/" + name;
    // }

    /**
     * get file name
     * 
     * @return file name
     */
    public String getName()
    {
	return name;
    }

    public void setName(String name)
    {
	parseName(name);
    }

    /**
     * get file size
     * 
     * @return file size
     */
    public long getSize()
    {
	return size;
    }

    public void setSize(long size)
    {
	this.size = size;
    }

    /**
     * get file date
     * 
     * @return file date
     */
    public String getDate()
    {
	return date;
    }

    public void setDate(String date)
    {
	this.date = date;
    }

    // /**
    // * get access attribute
    // *
    // * @return access attribute
    // */
    // public String getAttr()
    // {
    // return attr;
    // }
    //
    // public void setAttr(String attr)
    // {
    // this.attr = attr;
    // }

    // /**
    // * get file type,directory or file
    // *
    // * @return file type
    // * @see org.apache.commons.net.ftp.FTPFile#DIRECTORY_TYPE
    // * @see org.apache.commons.net.ftp.FTPFile#FILE_TYPE
    // */
    // public int getType()
    // {
    // return type;
    // }
    //
    // public void setType(int type)
    // {
    // this.type = type;
    // }
    /**
     * get file absolute path
     */
    public String getPath()
    {
	return path;
    }
    /**
     * set file abosulte path
     * @param abosultePath file absolute path
     */
    public void setPath(String abosultePath)
    {
	parseName(abosultePath);
    }

    public String getParentPath()
    {
	return this.parentPath;
    }
    /**
     * set parent file path
     * @param parentPath parent file path
     */
    public void setParentPath(String parentPath)
    {
	this.parentPath = parentPath;
    }

     /**
     * get hash code
     */
     public int hashCode()
     {
	 return path.hashCode();
     }
    
    public boolean equals(Object obj)
    {
	if (obj == null)
	    return false;
	if (!(obj instanceof FTPFileInfo))
	    return false;
	FTPFileInfo info = (FTPFileInfo) obj;
	return info.getName().equals(name) && info.date == date
		&& info.size == size;
    }

    public String toString()
    {
	return String.format(TO_STRING_FORMAT_STRING, Tools
		.getSizeInString(size), date, category, path);
    }
    /**
     * classify the file
     * @param extension file extension
     * @return file category
     */
    protected String classify(String extension)
    {
	if (category != null)
	{
	    return category;
	}

	if (extension == null)
	{
	    return CategoryConfigure.getCategory("unknown");
	}
	extension = ";" + extension + ";";
	for (Object obj : CategoryConfigure.getAllCategories())
	{
	    String key = (String) obj;
	    String value = CategoryConfigure.getCategory(key).toLowerCase();
	    if (value.contains(extension))
		return key;
	}
	return CategoryConfigure.getCategory("unknown");
    }

    public static void main(String args[])
    {
	FTPFileInfo info = new FTPFileInfo();
	info.setName("/movie");
    }

    /**
     * @return the extension
     */
    public String getExtension()
    {
	return extension;
    }

    /**
     * @param extension
     *                the extension to set
     */
    public void setExtension(String extension)
    {
	this.extension = extension;
    }

}
