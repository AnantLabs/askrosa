package searcher;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import database.FtpSiteInfo;

import ftp.FTPFileInfo;
import utils.Tools;

/**
 * 用于保存每一条搜索结果信息的类
 * @author elegate
 *
 */
public class SearchResultElement extends FTPFileInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = 5770186142246931190L;
    // private String fileSize = null;// 文件大小
    /**
     * 服务器名
     */
    private String server = "";
    /**
     * 用户名
     */
    private String username = "anonymous";
    /**
     * 密码
     */
    private String password = "";
    /**
     * 端口号
     */
    private int port = 21;
    /**
     * 高亮路径
     */
    private String highlightedPath;
    /**
     * 高亮文件名
     */
    private String highlightedName;
    
    private String updateTime;
    
    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    private int location = FtpSiteInfo.CAMPUS_SITE;
    
    public int getLocation()
    {
        return location;
    }

    public void setLocation(int location)
    {
        this.location = location;
    }

    public SearchResultElement()
    {
	super();
    }

    public String getAbsolutePath()
    {
	return getFtpAddress() + this.path;
    }
    public String getEncodedAbsolutePath() throws UnsupportedEncodingException
    {
	return URLEncoder.encode(getFtpAddress()+this.path,"utf-8");
    }
    
    public String getEncodedPath() throws UnsupportedEncodingException
    {
	return URLEncoder.encode(this.path,"utf-8");
    }
    
    public String getEncodedParentPath() throws UnsupportedEncodingException
    {
	return URLEncoder.encode(this.parentPath,"utf-8");
    }

    public String getHighlightedParentPath()
    {
	int endIndex=this.getHighlightedPath().length()-this.getHighlightedName().length();
	return this.getHighlightedPath().substring(0, endIndex);
    } 
    public String getAbsoluteParentPath()
    {
	return getFtpAddress() + this.parentPath;
    }
    
    public String getFtpAddress()
    {
	String portString = port == 21 ? "" : (":" + port);
	if (password != null && password.length() > 0)
	    return "ftp://" + username + ":" + password + "@" + server
		    + portString;
	else
	    return "ftp://" + username + "@" + server + portString;
    }

    public void setServer(String server)
    {
	this.server = server;
    }

    public String getServer()
    {
	return this.server;
    }

    public void setUsername(String username)
    {
	this.username = username;
    }

    public String getUsername()
    {
	return this.username;
    }

    public void setPassword(String password)
    {
	this.password = password;
    }

    public String getPassword()
    {
	return this.password;
    }

    public void setPort(int port)
    {
	this.port = port;
    }

    public int getPort()
    {
	return port;
    }

    public String getFileSize()
    {
	return Tools.getSizeInString(this.size);
    }

    public String getModifiedTime()
    {
	return date;
    }

    public void setHighlightedPath(String path)
    {
	this.highlightedPath = path;
    }

    public String getHighlightedPath()
    {
	return this.highlightedPath;
    }

    public void setHighlightedName(String name)
    {
	this.highlightedName = name;
    }

    public String getHighlightedName()
    {
	return this.highlightedName;
    }

    public String toString()
    {
	return String.format("%-50s", getFtpAddress())+" "+updateTime+" "+ super.toString();
    }
}
