-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.27-community-nt


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema microsite
--

CREATE DATABASE IF NOT EXISTS microsite;
USE microsite;

--
-- Definition of table `blocks`
--

DROP TABLE IF EXISTS `blocks`;
CREATE TABLE `blocks` (
  `bid` int(11) NOT NULL auto_increment,
  `name` varchar(64) NOT NULL default '',
  `description` varchar(32) NOT NULL default '',
  `content` text NOT NULL,
  `dynamic` tinyint(1) default '0',
  `created` varchar(32) NOT NULL default '0',
  `modified` varchar(32) NOT NULL default '0',
  PRIMARY KEY  (`bid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `blocks`
--

/*!40000 ALTER TABLE `blocks` DISABLE KEYS */;
INSERT INTO `blocks` (`bid`,`name`,`description`,`content`,`dynamic`,`created`,`modified`) VALUES 
 (3,'header','header for default theme','<div class=\"header\">\r\n    	<div class=\"header_top\">\r\n        	<ul>\r\n            	<li><a title=\"increase sales\" href=\"index.php?p=3\">increase sales</a></li>\r\n                <li><a title=\" maximize lead conversion\" href=\"index.php?p=4\"> maximize lead conversion</a></li>\r\n                <li><a title=\"case studies\" href=\"index.php?p=8\">case studies</a></li>\r\n                <li><a title=\" free web downloads\" href=\"index.php?p=6\"> free web downloads</a></li>\r\n                <li><a title=\"free sales analysis\" href=\"index.php?p=15\">free sales analysis</a></li> \r\n                <li><a title=\"about us\" href=\"index.php?p=7\">about us</a></li>\r\n            </ul>\r\n        </div>\r\n        <div class=\"header_body\">\r\n        	<h1><a title=\"Home\" href=\"http://www.revenueresultsinc.com/\"><img alt=\"Home\" src=\"${base}/sites/${site_id}/css/images/logo.jpg\"/></a></h1>\r\n        </div>\r\n        <div class=\"header_foot\"></div>\r\n    </div>',1,'2008-09-01 18:25:46','2008-09-01 18:25:46'),
 (4,'footer','footer for default theme','<div class=\"footer\">\r\n  <div style=\"float: left;\"> <a title=\"\" href=\"http://www.revenueresultsinc.com\">Home</a> <a title=\"\" href=\"index.php?p=12\">FAQ</a> <a title=\"\" href=\"index.php?p=13\">Privacy Policy</a><!--<a href=\"index.php?p=11\" title=\"\">Site Map</a>--> <a title=\"\" href=\"index.php?p=9\">Request More Info</a> <a title=\"\">Call Us: 877 - 300 - 8243</a> </div>\r\n  <div style=\"float: right;\"> <a title=\"\">copyright Â© 2007 Revenue Results. - All rights reserved</a> </div>\r\n</div>',0,'2008-09-01 18:26:11','2008-09-01 18:26:11'),
 (5,'banner','banner for default theme','<div class=\"banner\"> <span style=\"color: rgb(150, 212, 17);\">Turn Your<span style=\"color: rgb(139, 139, 139);\">Sales Staff</span></span><br/>\r\n    <br/>\r\n    <span style=\"color: rgb(150, 212, 17); padding-left: 50px;\">Into <span style=\"color: rgb(139, 139, 139);\">Great Producers</span></span> </div>',0,'2008-09-01 18:26:55','2008-09-01 18:26:55'),
 (6,'content','AB Content for default theme','<div class=\"options\">\r\n    <div class=\"option_sub\">\r\n      <h1><a title=\"your sales team can do more than just wait for good leads to come to them...\" href=\"index.php?p=3\">your sales team can do more than just wait for good leads to come to them...</a></h1>\r\n      <div class=\"option_text\">A company\'s sales team should provide a return-on-investment like any corporate resource. Our expertise is in building sales teams that turn leads into deals.</div>\r\n      <a title=\"Increase Sales\" class=\"option_link\" href=\"index.php?p=3\">Increase Sales</a> </div>\r\n    <div class=\"option_sub\">\r\n      <h1><a title=\"shouldnâ??t your sales team close more deals?\" href=\"index.php?p=4\">shouldnâ??t your sales team close more deals?</a></h1>\r\n      <div class=\"option_text\">If youâ??re spending more than 20% of your gross margin per month on supplying your team with leads, our advanced lead generation strategies can significantly improve your return on investment.</div>\r\n      <a title=\"Maximize Lead Conversion\" class=\"option_link\" href=\"index.php?p=4\">Maximize Lead Conversion</a></div>\r\n  </div>',0,'2008-09-01 18:27:32','2008-09-01 18:27:32'),
 (7,'htmlheader','html header for default theme','<html>\r\n<head >\r\n<title>Source(N)</title>\r\n<style media=\"screen\" type=\"text/css\">\r\n@import url(${base}/sites/${site_id}/css/style.css );\r\n</style>\r\n\r\n</head>\r\n<body>\r\n\r\n<div class=\"wrapper\" >',1,'2008-09-02 16:15:53','2008-09-02 16:15:53'),
 (8,'htmlfooter','html footer for default theme','</div>\r\n</body>\r\n</html>',0,'2008-09-02 16:16:27','2008-09-02 16:16:27'),
 (20,'aaaaaaaaaaaa','aaaaaaaaa','aaaaaaaaaaaaa',1,'2008-09-09 04:57:37','2008-09-09 04:57:37');
/*!40000 ALTER TABLE `blocks` ENABLE KEYS */;


--
-- Definition of table `page_blocks`
--

DROP TABLE IF EXISTS `page_blocks`;
CREATE TABLE `page_blocks` (
  `pid` int(11) NOT NULL,
  `bid` varchar(64) NOT NULL,
  PRIMARY KEY  (`pid`,`bid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `page_blocks`
--

/*!40000 ALTER TABLE `page_blocks` DISABLE KEYS */;
INSERT INTO `page_blocks` (`pid`,`bid`) VALUES 
 (5,'3'),
 (5,'4'),
 (5,'5'),
 (5,'6'),
 (5,'7'),
 (5,'8'),
 (8,'3'),
 (8,'4'),
 (8,'5'),
 (8,'7');
/*!40000 ALTER TABLE `page_blocks` ENABLE KEYS */;


--
-- Definition of table `pages`
--

DROP TABLE IF EXISTS `pages`;
CREATE TABLE `pages` (
  `pid` int(11) NOT NULL auto_increment,
  `name` varchar(64) NOT NULL default '',
  `title` varchar(128) NOT NULL,
  `description` text NOT NULL,
  `tid` int(11) default '0',
  `created` varchar(32) NOT NULL default '0',
  `modified` varchar(32) NOT NULL default '0',
  PRIMARY KEY  (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `pages`
--

/*!40000 ALTER TABLE `pages` DISABLE KEYS */;
INSERT INTO `pages` (`pid`,`name`,`title`,`description`,`tid`,`created`,`modified`) VALUES 
 (5,'index','dashboard','dashboard',1,'2008-09-05 12:59:56','2008-09-05 12:59:56'),
 (8,'increase-sales','increase-sales','increase-sales',1,'2008-09-05 13:03:17','2008-09-05 13:03:17'),
 (9,'the-seven-elements-of-effective-lead-generation','','',1,'2008-09-05 13:04:38','2008-09-05 13:04:38'),
 (10,'contact-us','contact-us','contact-us',1,'2008-09-05 13:05:19','2008-09-05 13:05:19'),
 (11,'maximize-lead-strategies','maximize-lead-strategies','maximize-lead-strategies',1,'2008-09-05 13:05:47','2008-09-05 13:05:47');
/*!40000 ALTER TABLE `pages` ENABLE KEYS */;


--
-- Definition of table `sites`
--

DROP TABLE IF EXISTS `sites`;
CREATE TABLE `sites` (
  `sid` int(11) NOT NULL auto_increment,
  `name` varchar(64) NOT NULL default '',
  `description` varchar(32) NOT NULL default '',
  `title` varchar(32) NOT NULL default '',
  `theme` text NOT NULL,
  `username` varchar(32) NOT NULL default '',
  `created` varchar(32) NOT NULL default '0',
  `modified` varchar(32) NOT NULL default '0',
  PRIMARY KEY  (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sites`
--

/*!40000 ALTER TABLE `sites` DISABLE KEYS */;
INSERT INTO `sites` (`sid`,`name`,`description`,`title`,`theme`,`username`,`created`,`modified`) VALUES 
 (14,'aaaaaaaaaaaaaa','aaaaaaaaaaaaaaaaaaaaaaaa','aaaaaaaaaaaaaaaaaaa','default','admin','2008-09-09 09:09:30','2008-09-09 09:09:30'),
 (15,'ddddddddddd','dddddddddddddd','dddddddddd','default','admin','2008-09-09 09:41:29','2008-09-09 09:41:29');
/*!40000 ALTER TABLE `sites` ENABLE KEYS */;


--
-- Definition of table `themes`
--

DROP TABLE IF EXISTS `themes`;
CREATE TABLE `themes` (
  `tid` int(11) NOT NULL auto_increment,
  `name` varchar(64) NOT NULL default '',
  `description` varchar(32) NOT NULL default '',
  `created` varchar(32) NOT NULL default '0',
  `modified` varchar(32) NOT NULL default '0',
  PRIMARY KEY  (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `themes`
--

/*!40000 ALTER TABLE `themes` DISABLE KEYS */;
INSERT INTO `themes` (`tid`,`name`,`description`,`created`,`modified`) VALUES 
 (1,'default','default','2008-09-02 18:50:39','2008-09-02 18:50:39');
/*!40000 ALTER TABLE `themes` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
