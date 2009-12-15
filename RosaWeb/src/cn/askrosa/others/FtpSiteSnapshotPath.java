package cn.askrosa.others;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FtpSiteSnapshotPath
{
    private String relativePath;
    private String absolutePath;
    public String getRelativePath()
    {
	return this.relativePath;
    }
    public String getAbsolutePath()
    {
	return this.absolutePath;
    }
    public String getEncodedAbsolutePath()throws UnsupportedEncodingException
    {
	return URLEncoder.encode(this.absolutePath,"utf-8");
    }
    public void setRelativePath(String relativePath)
    {
	this.relativePath=relativePath;
    }
    public void setAbsolutePath(String absolutePath)
    {
	this.absolutePath=absolutePath;
    }    
}