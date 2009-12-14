package database;

/**
  * Autogenerated by Lisptorq 0.1.4 
*/
import java.sql.*;
public class BaseFtpSiteInfo  { 
	public static final String HOT  = "FTPSITEINFO.HOT"; 
	public static final String RECURSIVE  = "FTPSITEINFO.RECURSIVE"; 
	public static final String SPEED  = "FTPSITEINFO.SPEED"; 
	public static final String DIRECTORY  = "FTPSITEINFO.DIRECTORY"; 
	public static final String EXECUTABLE  = "FTPSITEINFO.EXECUTABLE"; 
	public static final String IMAGE  = "FTPSITEINFO.IMAGE"; 
	public static final String TEXT  = "FTPSITEINFO.TEXT"; 
	public static final String SUBTITLE  = "FTPSITEINFO.SUBTITLE"; 
	public static final String VIDEO  = "FTPSITEINFO.VIDEO"; 
	public static final String TOTALFILECOUNT  = "FTPSITEINFO.TOTALFILECOUNT"; 
	public static final String UPDATETIME  = "FTPSITEINFO.UPDATETIME"; 
	public static final String CONTACT  = "FTPSITEINFO.CONTACT"; 
	public static final String ENCODING  = "FTPSITEINFO.ENCODING"; 
	public static final String USERNAME  = "FTPSITEINFO.USERNAME"; 
	public static final String ACCESS  = "FTPSITEINFO.ACCESS"; 
	public static final String ADDRESS  = "FTPSITEINFO.ADDRESS"; 
	public static final String ID  = "FTPSITEINFO.ID"; 
	protected int hot  = 0; 
	protected short recursive  = 1; 
	protected int speed  = 0; 
	protected int directory  = 0; 
	protected int executable  = 0; 
	protected int image  = 0; 
	protected int text  = 0; 
	protected int subtitle  = 0; 
	protected int video  = 0; 
	protected int totalFileCount  = 0; 
	protected Date updateTime; 
	protected String contact  = ""; 
	protected String encoding  = "GBK"; 
	protected String username  = "anonymous"; 
	protected String access  = "anybody"; 
	protected String address; 
	protected int id; 
	protected String server; 
	protected String verify; 
	protected int port  = 21; 
	protected String password  = "anonymous"; 
	protected String admin  = "UNKNOWN"; 
	protected String description  = ""; 
	protected Date lastUpdateTime; 
	protected int crawlInterval  = 2; 
	protected int audio  = 0; 
	protected int document  = 0; 
	protected int program  = 0; 
	protected int compress  = 0; 
	protected int torrent  = 0; 
	protected int unknown  = 0; 
	protected int userslimit  = 0; 
	protected short location  = 0; 
	protected boolean saved  = false; 
	public static final String SERVER  = "FTPSITEINFO.SERVER"; 
	public static final String VERIFY  = "FTPSITEINFO.VERIFY"; 
	public static final String PORT  = "FTPSITEINFO.PORT"; 
	public static final String PASSWORD  = "FTPSITEINFO.PASSWORD"; 
	public static final String ADMIN  = "FTPSITEINFO.ADMIN"; 
	public static final String DESCRIPTION  = "FTPSITEINFO.DESCRIPTION"; 
	public static final String LASTUPDATETIME  = "FTPSITEINFO.LASTUPDATETIME"; 
	public static final String CRAWLINTERVAL  = "FTPSITEINFO.CRAWLINTERVAL"; 
	public static final String AUDIO  = "FTPSITEINFO.AUDIO"; 
	public static final String DOCUMENT  = "FTPSITEINFO.DOCUMENT"; 
	public static final String PROGRAM  = "FTPSITEINFO.PROGRAM"; 
	public static final String COMPRESS  = "FTPSITEINFO.COMPRESS"; 
	public static final String TORRENT  = "FTPSITEINFO.TORRENT"; 
	public static final String UNKNOWN  = "FTPSITEINFO.UNKNOWN"; 
	public static final String USERSLIMIT  = "FTPSITEINFO.USERSLIMIT"; 
	public static final String LOCATION  = "FTPSITEINFO.LOCATION"; 

	public void save()
	throws Exception{ 
		if(saved){
			BaseFtpSiteInfoPeer.doUpdate(this);
		}else{
			BaseFtpSiteInfoPeer.doInsert(this);
		}
	} 

	public short getLocation(){ 
		return location;
	} 

	public int getUserslimit(){ 
		return userslimit;
	} 

	public int getUnknown(){ 
		return unknown;
	} 

	public int getTorrent(){ 
		return torrent;
	} 

	public int getCompress(){ 
		return compress;
	} 

	public int getProgram(){ 
		return program;
	} 

	public int getDocument(){ 
		return document;
	} 

	public int getAudio(){ 
		return audio;
	} 

	public int getCrawlInterval(){ 
		return crawlInterval;
	} 

	public Date getLastUpdateTime(){ 
		return lastUpdateTime;
	} 

	public String getDescription(){ 
		return description;
	} 

	public String getAdmin(){ 
		return admin;
	} 

	public String getPassword(){ 
		return password;
	} 

	public int getPort(){ 
		return port;
	} 

	public String getVerify(){ 
		return verify;
	} 

	public String getServer(){ 
		return server;
	} 

	public void setAddress(String args0){ 
		address =args0;
	} 

	public void setAccess(String args0){ 
		access =args0;
	} 

	public void setUsername(String args0){ 
		username =args0;
	} 

	public void setEncoding(String args0){ 
		encoding =args0;
	} 

	public void setContact(String args0){ 
		contact =args0;
	} 

	public void setUpdateTime(Date args0){ 
		updateTime =args0;
	} 

	public void setTotalFileCount(int args0){ 
		totalFileCount =args0;
	} 

	public void setVideo(int args0){ 
		video =args0;
	} 

	public void setSubtitle(int args0){ 
		subtitle =args0;
	} 

	public void setText(int args0){ 
		text =args0;
	} 

	public void setImage(int args0){ 
		image =args0;
	} 

	public void setExecutable(int args0){ 
		executable =args0;
	} 

	public void setDirectory(int args0){ 
		directory =args0;
	} 

	public void setSpeed(int args0){ 
		speed =args0;
	} 

	public void setRecursive(short args0){ 
		recursive =args0;
	} 

	public void setHot(int args0){ 
		hot =args0;
	} 

	public int getHot(){ 
		return hot;
	} 

	public short getRecursive(){ 
		return recursive;
	} 

	public int getSpeed(){ 
		return speed;
	} 

	public int getDirectory(){ 
		return directory;
	} 

	public int getExecutable(){ 
		return executable;
	} 

	public int getImage(){ 
		return image;
	} 

	public int getText(){ 
		return text;
	} 

	public int getSubtitle(){ 
		return subtitle;
	} 

	public int getVideo(){ 
		return video;
	} 

	public int getTotalFileCount(){ 
		return totalFileCount;
	} 

	public Date getUpdateTime(){ 
		return updateTime;
	} 

	public String getContact(){ 
		return contact;
	} 

	public String getEncoding(){ 
		return encoding;
	} 

	public String getUsername(){ 
		return username;
	} 

	public String getAccess(){ 
		return access;
	} 

	public String getAddress(){ 
		return address;
	} 

	/**
	  * id is autogenerated 
	*/
	public int getId(){ 
		return id;
	} 

	public void setServer(String args0){ 
		server =args0;
	} 

	public void setVerify(String args0){ 
		verify =args0;
	} 

	public void setPort(int args0){ 
		port =args0;
	} 

	public void setPassword(String args0){ 
		password =args0;
	} 

	public void setAdmin(String args0){ 
		admin =args0;
	} 

	public void setDescription(String args0){ 
		description =args0;
	} 

	public void setLastUpdateTime(Date args0){ 
		lastUpdateTime =args0;
	} 

	public void setCrawlInterval(int args0){ 
		crawlInterval =args0;
	} 

	public void setAudio(int args0){ 
		audio =args0;
	} 

	public void setDocument(int args0){ 
		document =args0;
	} 

	public void setProgram(int args0){ 
		program =args0;
	} 

	public void setCompress(int args0){ 
		compress =args0;
	} 

	public void setTorrent(int args0){ 
		torrent =args0;
	} 

	public void setUnknown(int args0){ 
		unknown =args0;
	} 

	public void setUserslimit(int args0){ 
		userslimit =args0;
	} 

	public void setLocation(short args0){ 
		location =args0;
	} 
}