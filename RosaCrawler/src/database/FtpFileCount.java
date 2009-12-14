package database;

public class FtpFileCount
{
    private String locationString = "unknow";

    private short location = 0;

    private int ftpCount = 0;

    private long fileCount = 0;

    public short getLocation()
    {
	return location;
    }

    public void setLocation(short location)
    {
	this.location = location;
    }

    public long getFileCount()
    {
	return fileCount;
    }

    public void setFileCount(long fileCount)
    {
	this.fileCount = fileCount;
    }

    public String getLocationString()
    {
	return locationString;
    }

    public void setLocationString(String locationString)
    {
	this.locationString = locationString;
    }

    public int getFtpCount()
    {
        return ftpCount;
    }

    public void setFtpCount(int ftpCount)
    {
        this.ftpCount = ftpCount;
    }
}