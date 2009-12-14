-- MySQL dump 10.10
--
-- Host: 127.0.0.1    Database: rosadb
-- ------------------------------------------------------
-- Server version	5.0.22-community-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` int(11) NOT NULL auto_increment,
  `author` varchar(512) NOT NULL default 'anonymous',
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `title` varchar(512) NOT NULL,
  `content` varchar(10000) NOT NULL,
  `clickcount` int(11) NOT NULL,
  `ip` varchar(128) NOT NULL,
  `verify` varchar(64) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `article`
--


/*!40000 ALTER TABLE `article` DISABLE KEYS */;
LOCK TABLES `article` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `article` ENABLE KEYS */;

--
-- Table structure for table `ftpsiteinfo`
--

DROP TABLE IF EXISTS `ftpsiteinfo`;
CREATE TABLE `ftpsiteinfo` (
  `id` int(11) NOT NULL auto_increment,
  `server` varchar(128) NOT NULL,
  `address` varchar(128) NOT NULL,
  `verify` varchar(128) NOT NULL,
  `access` varchar(128) NOT NULL default 'anybody',
  `port` int(11) NOT NULL default '21',
  `username` varchar(128) NOT NULL default 'anonymous',
  `password` varchar(128) default NULL,
  `encoding` varchar(128) NOT NULL default 'GBK',
  `admin` varchar(128) NOT NULL default 'UNKNOWN',
  `contact` varchar(128) default NULL,
  `description` varchar(5000) default NULL,
  `updateTime` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `lastUpdateTime` timestamp NOT NULL default '0000-00-00 00:00:00',
  `totalFileCount` int(11) default '0',
  `crawlInterval` int(11) default '1',
  `video` int(11) default '0',
  `audio` int(11) default '0',
  `subtitle` int(11) default '0',
  `document` int(11) default '0',
  `text` int(11) default '0',
  `program` int(11) default '0',
  `image` int(11) default '0',
  `compress` int(11) default '0',
  `executable` int(11) default '0',
  `directory` int(11) default '0',
  `unknown` int(11) default '0',
  `speed` int(11) default '0',
  `userslimit` int(11) default '0',
  `recursive` smallint(6) default '1',
  `location` smallint(5) unsigned default '0',
  `hot` int(10) unsigned default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `test` (`server`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ftpsiteinfo`
--


/*!40000 ALTER TABLE `ftpsiteinfo` DISABLE KEYS */;
LOCK TABLES `ftpsiteinfo` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `ftpsiteinfo` ENABLE KEYS */;

--
-- Table structure for table `historyusers`
--

DROP TABLE IF EXISTS `historyusers`;
CREATE TABLE `historyusers` (
  `id` int(11) NOT NULL auto_increment,
  `count` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `historyusers`
--


/*!40000 ALTER TABLE `historyusers` DISABLE KEYS */;
LOCK TABLES `historyusers` WRITE;
INSERT INTO `historyusers` VALUES (1,0),(2,0),(3,0),(4,0),(5,0);
UNLOCK TABLES;
/*!40000 ALTER TABLE `historyusers` ENABLE KEYS */;

--
-- Table structure for table `querystatistics`
--

DROP TABLE IF EXISTS `querystatistics`;
CREATE TABLE `querystatistics` (
  `id` int(11) NOT NULL auto_increment,
  `keyword` varchar(512) NOT NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `hits` int(10) unsigned default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `querystatistics`
--


/*!40000 ALTER TABLE `querystatistics` DISABLE KEYS */;
LOCK TABLES `querystatistics` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `querystatistics` ENABLE KEYS */;

--
-- Table structure for table `resourcepost`
--

DROP TABLE IF EXISTS `resourcepost`;
CREATE TABLE `resourcepost` (
  `id` int(11) NOT NULL auto_increment,
  `nickname` varchar(512) NOT NULL default 'anonymous',
  `resourcename` varchar(1024) NOT NULL,
  `address` varchar(128) NOT NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `port` int(10) unsigned NOT NULL default '21',
  `username` varchar(512) NOT NULL default 'anonymous',
  `passwd` varchar(512) NOT NULL default 'anonymous',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `resourcepost`
--


/*!40000 ALTER TABLE `resourcepost` DISABLE KEYS */;
LOCK TABLES `resourcepost` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `resourcepost` ENABLE KEYS */;

--
-- Table structure for table `resourcerequest`
--

DROP TABLE IF EXISTS `resourcerequest`;
CREATE TABLE `resourcerequest` (
  `id` int(11) NOT NULL auto_increment,
  `nickname` varchar(512) NOT NULL default 'anonymous',
  `resourcename` varchar(1024) NOT NULL,
  `email` varchar(128) NOT NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `state` smallint(5) unsigned default '0',
  `disply` smallint(5) unsigned default '1',
  `deadline` timestamp NOT NULL default '2009-12-31 16:00:00',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `resourcerequest`
--


/*!40000 ALTER TABLE `resourcerequest` DISABLE KEYS */;
LOCK TABLES `resourcerequest` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `resourcerequest` ENABLE KEYS */;

--
-- Table structure for table `responsepost`
--

DROP TABLE IF EXISTS `responsepost`;
CREATE TABLE `responsepost` (
  `postid` int(11) NOT NULL auto_increment,
  `id` int(11) NOT NULL,
  `author` varchar(512) NOT NULL default 'anonymous',
  `content` varchar(10000) NOT NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `ip` varchar(128) NOT NULL,
  `verify` varchar(64) NOT NULL,
  PRIMARY KEY  (`postid`),
  KEY `id` (`id`),
  CONSTRAINT `responsepost_ibfk_1` FOREIGN KEY (`id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `responsepost`
--


/*!40000 ALTER TABLE `responsepost` DISABLE KEYS */;
LOCK TABLES `responsepost` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `responsepost` ENABLE KEYS */;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
CREATE TABLE `sessions` (
  `id` int(11) NOT NULL auto_increment,
  `createTime` bigint(20) NOT NULL,
  `destroyTime` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sessions`
--


/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
LOCK TABLES `sessions` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

