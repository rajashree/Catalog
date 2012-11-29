-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.51b-community-nt


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

DROP TABLE IF EXISTS "microsite"."blocks";
CREATE TABLE "blocks" (
  "bid" int(11) NOT NULL auto_increment,
  "name" varchar(64) NOT NULL default '',
  "description" varchar(32) NOT NULL default '',
  "content" text NOT NULL,
  "sid" int(11) NOT NULL ,
  "created" varchar(32) NOT NULL default '0',
  "modified" varchar(32) NOT NULL default '0',
  PRIMARY KEY  ("bid")
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `microsite`.`sites`;
CREATE TABLE  `microsite`.`sites` (
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

DROP TABLE IF EXISTS "microsite"."themes";
CREATE TABLE "themes" (
  "tid" int(11) NOT NULL auto_increment,
  "name" varchar(64) NOT NULL default '',
  "description" varchar(32) NOT NULL default '',
  "created" varchar(32) NOT NULL default '0',
  "modified" varchar(32) NOT NULL default '0',
  PRIMARY KEY  ("tid")
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS "microsite"."pages";
CREATE TABLE "pages" (
  "pid" int(11) NOT NULL auto_increment,
  "name" varchar(64) NOT NULL default '',
  "title" varchar(32) NOT NULL default '',
  "description" varchar(32) NOT NULL default '',
  "tid" int(11) NOT NULL default '0',
  "created" varchar(32) NOT NULL default '0',
  "modified" varchar(32) NOT NULL default '0',
  PRIMARY KEY  ("pid")
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
