package utils;

import java.io.*;
import java.util.Hashtable;

public class IOFileFilter implements FileFilter
{
    private Hashtable<String, FileFilter> filters = null;


    /**
     * Creates a file filter. If no filters are added, then all files are
     * accepted.
     * 
     * @see #addExtension
     */
    public IOFileFilter()
    {
	this.filters = new Hashtable<String, FileFilter>();
    }

   /**
     * Creates a file filter from the given string array and description.
     * Example: new ExampleFileFilter(String {"gif", "jpg"}, "Gif and JPG
     * Images"); Note that the "." before the extension is not needed and will
     * be ignored.
     * 
     * @see #addExtension
     */
    public IOFileFilter(String[] filters)
    {
	this();
	for (int i = 0; i < filters.length; i++)
	{
	    // add filters one by one
	    addExtension(filters[i]);
	}
    }

    /**
     * Return true if this file should be shown in the directory pane, false if
     * it shouldn't. Files that begin with "." are ignored.
     */
    public boolean accept(File f)
    {
	if (f != null)
	{

	    if (f.isDirectory())
	    {
		return true;
	    }
	    String extension = getExtension(f);
	    if (extension != null && filters.get(extension) != null)
	    {
		return true;
	    }
	}
	return false;
    }

    /**
     * Return the extension portion of the file's name .
     * 
     * @see #getExtension
     * @see FileFilter#accept
     */
    public String getExtension(File f)
    {
	if (f != null)
	{
	    String filename = f.getName();
	    int i = filename.lastIndexOf('.');
	    if (i > 0 && i < filename.length() - 1)
	    {
		return filename.substring(i + 1).toLowerCase();
	    }
	}
	return null;
    }

    /**
     * Adds a filetype "dot" extension to filter against. For example: the
     * following code will create a filter that filters out all files except
     * those that end in ".jpg" and ".tif": ExampleFileFilter filter = new
     * ExampleFileFilter(); filter.addExtension("jpg");
     * filter.addExtension("tif"); Note that the "." before the extension is not
     * needed and will be ignored.
     */
    public void addExtension(String extension)
    {
	if (filters == null)
	{
	    filters = new Hashtable<String, FileFilter>(5);
	}
	filters.put(extension.toLowerCase(), this);
    }
}
