[
    Copyright (C) 2005  Arnold Doray

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
]


(import lisptorq.lisp)
define database 
(
    (FtpSiteInfo
		(id (REQUIRED PRIMARY AUTO INTEGER))
        (server (REQUIRED (VARCHAR 128)))
        (address (REQUIRED (VARCHAR 128)))
        (verify  (REQUIRED (VARCHAR 128)))
		(access (REQUIRED (VARCHAR 128)(DEFAULT "anybody")))
        (port   (REQUIRED INTEGER (DEFAULT 21)))
        (username   (REQUIRED (VARCHAR 128)(DEFAULT "anonymous")))
        (password   ((VARCHAR 128)(DEFAULT "anonymous")))
        (encoding   (REQUIRED (VARCHAR 128)(DEFAULT "GBK")))
        (admin      (REQUIRED (VARCHAR 128)(DEFAULT "UNKNOWN")))
        (contact    ((VARCHAR 128)(DEFAULT "")))
        (description ((VARCHAR 5000)(DEFAULT "")))
        (updateTime  (TIMESTAMP))
        (lastUpdateTime (TIMESTAMP))
        (totalFileCount (INTEGER (DEFAULT 0)))
        (crawlInterval  (INTEGER (DEFAULT 2)))
        (video          (INTEGER (DEFAULT 0)))
        (audio (INTEGER (DEFAULT 0)))
        (subtitle (INTEGER (DEFAULT 0)))
        (document (INTEGER (DEFAULT 0)))
        (text (INTEGER (DEFAULT 0)))
        (program (INTEGER (DEFAULT 0)))
        (image (INTEGER (DEFAULT 0)))
        (compress (INTEGER (DEFAULT 0)))
        (executable (INTEGER (DEFAULT 0)))
        (torrent    (INTEGER (DEFAULT 0)))
        (directory  (INTEGER (DEFAULT 0)))
        (unknown (INTEGER (DEFAULT 0)))
        (speed   (INTEGER (DEFAULT 0)))
        (userslimit (INTEGER (DEFAULT 0)))
        (recursive  (SMALLINT (DEFAULT 1)))
		(location   (SMALLINT (DEFAULT 0)))
		(hot        (INTEGER (DEFAULT 0)))
    )

    (HistoryUsers
          (id (REQUIRED PRIMARY AUTO INTEGER))
          (count (REQUIRED BIGINTEGER))
	)
    (Sessions
	 (id (REQUIRED PRIMARY AUTO INTEGER))
	 (createTime (REQUIRED BIGINTEGER))
        (destroyTime (BIGINTEGER))
     )	          
    (QueryStatistics 
        (id (REQUIRED PRIMARY AUTO INTEGER))
	(keyword (REQUIRED (VARCHAR 512)))
 	(time  (REQUIRED TIMESTAMP))
	)
    (QueryStatisticsResult 
     	(keyword (REQUIRED (VARCHAR 512)))
 	(frequency  (REQUIRED INTEGER))
	)
    (ResourceRequest
      (id (REQUIRED PRIMARY AUTO INTEGER))
      (nickname  (REQUIRED (VARCHAR 512)(DEFAULT "anonymous")))
      (resourcename (REQUIRED (VARCHAR 1024)))
      (email (REQUIRED (VARCHAR 128)))
      (time (REQUIRED TIMESTAMP))
	  (deadline (REQUIRED TIMESTAMP))
	  (disply (SMALLINT (DEFAULT 1)))
	  (state (SMALLINT (DEFAULT 0)))
    )
    (ResourcePost
      (id (REQUIRED PRIMARY AUTO INTEGER))
      (nickname  (REQUIRED (VARCHAR 512)(DEFAULT "anonymous")))
      (resourcename (REQUIRED (VARCHAR 1024)))
      (address (REQUIRED (VARCHAR 128)))
      (time (REQUIRED TIMESTAMP))
	  (port (REQUIRED INTEGER (DEFAULT 21)))
	  (username (REQUIRED (VARCHAR 512)(DEFAULT "anonymous")))
	  (passwd (REQUIRED (VARCHAR 512)(DEFAULT "anonymous")))
    )
   (Article
     (id (REQUIRED PRIMARY AUTO INTEGER))
     (author (REQUIRED (VARCHAR 512)(DEFAULT "anonymous")))
     (time  (REQUIRED TIMESTAMP))
     (title (REQUIRED (VARCHAR 512)))
     (content (REQUIRED (VARCHAR 10000)))
     (clickcount (REQUIRED INTEGER)(DEFAULT 0))
     (ip    (REQUIRED (VARCHAR 128)))
     (verify (REQUIRED (VARCHAR 64)))
   )
   (ResponsePost
   (postid (REQUIRED PRIMARY AUTO INTEGER))
    (id (REQUIRED INTEGER (FOREIGN Article)))
    (author (REQUIRED (VARCHAR 512)(DEFAULT "anonymous")))
    (content (REQUIRED (VARCHAR 10000)))
    (time  (REQUIRED TIMESTAMP))
    (ip    (REQUIRED (VARCHAR 128)))
	(verify (REQUIRED (VARCHAR 64)))
   )
)

(lisptorq:set database-type   derby)
(lisptorq:set database-name   FtpSearch)
(lisptorq:set derby-home    ./FtpSearchDB)
(lisptorq:set package       database)


(lisptorq:generate database)
















