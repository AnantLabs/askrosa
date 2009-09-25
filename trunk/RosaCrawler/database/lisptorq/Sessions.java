package FtpSiteInfo id REQUIRED PRIMARY AUTO INTEGER server REQUIRED VARCHAR 128 address REQUIRED VARCHAR 128 verify REQUIRED VARCHAR 128 access REQUIRED VARCHAR 128 DEFAULT anybody port REQUIRED INTEGER DEFAULT 21 username REQUIRED VARCHAR 128 DEFAULT anonymous password VARCHAR 128 DEFAULT anonymous encoding REQUIRED VARCHAR 128 DEFAULT GBK admin REQUIRED VARCHAR 128 DEFAULT UNKNOWN contact VARCHAR 128 DEFAULT " description VARCHAR 5000 DEFAULT " updateTime TIMESTAMP lastUpdateTime TIMESTAMP totalFileCount INTEGER DEFAULT 0 crawlInterval INTEGER DEFAULT 2 video INTEGER DEFAULT 0 audio INTEGER DEFAULT 0 subtitle INTEGER DEFAULT 0 document INTEGER DEFAULT 0 text INTEGER DEFAULT 0 program INTEGER DEFAULT 0 image INTEGER DEFAULT 0 compress INTEGER DEFAULT 0 executable INTEGER DEFAULT 0 directory INTEGER DEFAULT 0 unknown INTEGER DEFAULT 0 speed INTEGER DEFAULT 0 userslimit INTEGER DEFAULT 0 recursive SMALLINT DEFAULT 1 HistoryUsers id REQUIRED PRIMARY AUTO INTEGER count REQUIRED BIGINTEGER Sessions id REQUIRED PRIMARY AUTO INTEGER createTime REQUIRED BIGINTEGER destroyTime BIGINTEGER QueryStatistics id REQUIRED PRIMARY AUTO INTEGER keyword REQUIRED VARCHAR 512 time REQUIRED TIMESTAMP QueryStatisticsResult keyword REQUIRED VARCHAR 512 frequency REQUIRED INTEGER ResourceRequest nickname REQUIRED VARCHAR 512 DEFAULT anonymous resourcename REQUIRED VARCHAR 1024 email REQUIRED VARCHAR 128 time REQUIRED TIMESTAMP Article id REQUIRED PRIMARY AUTO INTEGER author REQUIRED VARCHAR 512 DEFAULT anonymous time REQUIRED TIMESTAMP title REQUIRED VARCHAR 512 content REQUIRED VARCHAR 10000 clickcount REQUIRED INTEGER ip REQUIRED VARCHAR 128 verify REQUIRED VARCHAR 64 DEFAULT secret ResponsePost id REQUIRED INTEGER FOREIGN Articles author REQUIRED VARCHAR 512 DEFAULT anonymous content REQUIRED VARCHAR 10000 time REQUIRED TIMESTAMP ip REQUIRED VARCHAR 128 verify REQUIRED VARCHAR 64 DEFAULT secret;
/**
  * Autogenerated by Lisptorq 0.1.4 
*/
import java.sql.*;
import java.util.Iterator;
public class Sessions extends BaseSessions { 
}