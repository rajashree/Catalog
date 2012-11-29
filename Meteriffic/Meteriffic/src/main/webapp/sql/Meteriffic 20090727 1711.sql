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
-- Create schema meteriffic
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ meteriffic;
USE meteriffic;

--
-- Table structure for table `meteriffic`.`features`
--

DROP TABLE IF EXISTS `features`;
CREATE TABLE `features` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL default '',
  `pid` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `meteriffic`.`features`
--

/*!40000 ALTER TABLE `features` DISABLE KEYS */;
INSERT INTO `features` (`id`,`name`,`pid`) VALUES 
 (643,'All Features',14),
 (644,'Current Product',643),
 (645,'Unknown Feature',643),
 (646,'Body',643),
 (647,'Body-General',646),
 (648,'Function Buttons',646),
 (649,'LCD-Screen',646),
 (650,'Weight',646),
 (651,'Size',646),
 (652,'Material',646),
 (653,'Paper-Trays',646),
 (654,'Paper-Format',646),
 (655,'Pickup-Rollers',646),
 (656,'Cartridges',643),
 (657,'Interface',643),
 (658,'Connectivity',657),
 (659,'Printing-System',643),
 (660,'Print-Speed',659),
 (661,'Print-Quality',659),
 (662,'Processor',659),
 (663,'Warm-Up-Time',659),
 (664,'Resolution',659),
 (665,'Noise',659),
 (666,'Bi-directional',659),
 (667,'Standard-Printer-Languages',659),
 (668,'Operating-Conditions',659),
 (669,'Memory',643),
 (670,'Storage-Type',669),
 (671,'Memory-Cards',669),
 (672,'Power-Requirement',643),
 (673,'Power-Consumption',672),
 (674,'Operating System',643),
 (675,'Software',643),
 (676,'Additional-Features',643),
 (677,'Fax',676),
 (678,'Scanner',676),
 (679,'Photo-Printer',676);
INSERT INTO `features` (`id`,`name`,`pid`) VALUES 
 (680,'Photocopier',676),
 (681,'Installation',643),
 (682,'Software-Installation',681),
 (683,'Control',643),
 (684,'Accessories',643),
 (685,'Manual',643),
 (686,'Price',643),
 (687,'Customer-Service',643),
 (688,'After-Sales-Service',687),
 (689,'Delivery',687),
 (690,'Replacement-Cost',643),
 (691,'Job-Queue',643),
 (692,'Design',643),
 (693,'Toner-Level-Indicator',643),
 (694,'Durable',643),
 (695,'Indicator',643),
 (696,'Color-Printer',643),
 (697,'Paper-Level-Indicator',643),
 (698,'Sharing',643),
 (857,'Operation Cost',643),
 (858,'Self-Calibration',643);
/*!40000 ALTER TABLE `features` ENABLE KEYS */;


--
-- Table structure for table `meteriffic`.`products`
--

DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL,
  `pid` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `meteriffic`.`products`
--

/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` (`id`,`name`,`pid`) VALUES 
 (1162,'Brother',14),
 (1163,'DCP',1162),
 (1164,'Brother-7030',1163),
 (1165,'Brother-7040',1163),
 (1166,'Brother-8060',1163),
 (1167,'Brother-9040CN',1163),
 (1168,'Brother-9045CDN',1163),
 (1169,'HL',1162),
 (1170,'Brother-5240',1169),
 (1171,'Brother-5250DN',1169),
 (1172,'Brother-5250DNT',1169),
 (1173,'Brother-5280DW',1169),
 (1174,'Brother-6050D',1169),
 (1175,'Brother-6050DN',1169),
 (1176,'Brother-2170W',1169),
 (1177,'Brother-4040CN',1169),
 (1178,'Brother-4070CDW',1169),
 (1179,'MFC',1162),
 (1180,'Brother-8220',1179),
 (1181,'Brother-7440N',1179),
 (1182,'7840W',1179),
 (1183,'Brother-8660 DN',1179),
 (1184,'Brother-9440CN',1179),
 (1185,'Brother-9840CWD',1179),
 (1186,'HP',14),
 (1187,'Color LaserJet',1186),
 (1188,'HP-3600dn',1187),
 (1189,'HP-3600n',1187),
 (1190,'HP-4700dn',1187),
 (1191,'HP-CM1312nfi MFP',1187),
 (1192,'HP-CM2320nf MFP',1187),
 (1193,'HP-CP1215',1187),
 (1194,'HP-CP1518ni',1187),
 (1195,'HP-CP2025dn',1187),
 (1196,'HP-CP2025n',1187);
INSERT INTO `products` (`id`,`name`,`pid`) VALUES 
 (1197,'HP-CP3525n',1187),
 (1198,'HP-CP3505dn',1187),
 (1199,'HP-5200',1187),
 (1200,'HP-5200tn',1187),
 (1201,'HP-5550n',1187),
 (1202,'HP-5550dn',1187),
 (1203,'HP-M1319f MFP',1187),
 (1204,'HP-M1522nf MFP',1187),
 (1205,'HP-M2727nf MFP',1187),
 (1206,'HP-M3035xs MFP',1187),
 (1207,'HP-P1005',1187),
 (1208,'HP-P1006',1187),
 (1209,'HP-P1505',1187),
 (1210,'HP-P1505n',1187),
 (1211,'HP-P2015',1187),
 (1212,'HP-P2015d',1187),
 (1213,'HP-P3005',1187),
 (1214,'HP-P3005d',1187),
 (1215,'HP-P3005dn',1187),
 (1216,'HP-P3005n',1187),
 (1217,'HP-P3005x',1187),
 (1218,'Canon',14),
 (1219,'Color ImageRunner',1218),
 (1220,'Canon-LBP5360',1219),
 (1221,'ImageCLASS',1218),
 (1222,'Canon-D340',1221),
 (1223,'Canon-D320',1221),
 (1224,'Canon-MF3240',1221),
 (1225,'Canon-MF4150',1221),
 (1226,'Canon-MF4270',1221),
 (1227,'Canon-MF4350d',1221),
 (1228,'Canon-MF4370dn',1221),
 (1229,'Canon-MF4690',1221),
 (1230,'Canon-MF7460',1221),
 (1231,'Dell',14),
 (1232,'Laser',1231);
INSERT INTO `products` (`id`,`name`,`pid`) VALUES 
 (1233,'Dell-1110',1232),
 (1234,'Dell-1320c',1232),
 (1235,'Dell-1125',1232),
 (1236,'Dell-3130cn',1232),
 (1237,'Dell-3115cn',1232),
 (1238,'Dell-1720',1232),
 (1239,'Dell-2330dn',1232),
 (1240,'Dell-2135cn',1232),
 (1241,'Dell-1815dn',1232),
 (1242,'Dell-5110cn',1232),
 (1243,'Epson',14),
 (1244,'Action Laser',1243),
 (1245,'Epson-1000',1244),
 (1246,'Epson-1100',1244),
 (1247,'Epson-1400',1244),
 (1248,'Epson-1500',1244),
 (1249,'Epson-II',1244),
 (1250,'Epson-Plus',1244),
 (1251,'AcuLaser',1243),
 (1252,'Epson-CX11N',1251),
 (1253,'CX11NF',1251),
 (1254,'ColorPage',1243),
 (1255,'Epson-8000',1254),
 (1256,'EPL',1243),
 (1257,'Epson-5700i',1256),
 (1258,'Lexmark',14),
 (1259,'Other',1258),
 (1260,'Lexmark-C500N',1259),
 (1261,'Lexmark-C530DN',1259),
 (1262,'Lexmark-C532N',1259),
 (1263,'Lexmark-C534N',1259),
 (1264,'Lexmark-C534DN',1259),
 (1265,'Lexmark-C935DN',1259),
 (1266,'Lexmark-E120N',1259),
 (1267,'Lexmark-E250D',1259),
 (1268,'Lexmark-E250DN',1259),
 (1269,'Lexmark-E450DN',1259);
INSERT INTO `products` (`id`,`name`,`pid`) VALUES 
 (1270,'Lexmark-T640',1259),
 (1271,'Lexmark-X342N',1259),
 (1272,'Lexmark-X500N',1259),
 (1273,'Lexmark-X502N',1259),
 (1274,'Lexmark-X642E',1259),
 (1275,'Okidata',14),
 (1276,'Other',1275),
 (1277,'Okidata-B2520',1276),
 (1278,'Okidata-C3400n',1276),
 (1279,'Okidata-C710dn',1276),
 (1280,'Okidata-C5800Ldn',1276),
 (1281,'Color Signage Printer',1275),
 (1282,'Okidata-C9650hn',1281),
 (1283,'Panasonic',14),
 (1284,'KX',1283),
 (1285,'Panasonic-MB781',1284),
 (1286,'Panasonic-MB271',1284),
 (1287,'Panasonic-FLB881',1284),
 (1288,'Panasonic-FLB851',1284),
 (1289,'Panasonic-FLB801',1284),
 (1290,'Panasonic-FLB811',1284),
 (1291,'Panasonic-FLM651',1284),
 (1292,'Samsung',14),
 (1293,'CLP',1292),
 (1294,'Samsung-315W',1293),
 (1295,'Samsung-315',1293),
 (1296,'Samsung-300',1293),
 (1297,'Samsung-300N',1293),
 (1298,'Samsung-610ND',1293),
 (1299,'Samsung-660ND',1293),
 (1300,'CLX',1292),
 (1301,'Samsung-3175FN',1300),
 (1302,'Samsung-3160FN',1300),
 (1303,'Samsung-8380ND',1300);
INSERT INTO `products` (`id`,`name`,`pid`) VALUES 
 (1304,'Samsung-2160N',1300),
 (1305,'Samsung-6200FX',1300),
 (1306,'ML',1292),
 (1307,'Samsung-1630',1306),
 (1308,'Samsung-1630W',1306),
 (1309,'Samsung-2510',1306),
 (1310,'Samsung-2571N',1306),
 (1311,'Samsung-2851ND',1306),
 (1312,'Samsung-3051N',1306),
 (1313,'Samsung-3051ND',1306),
 (1314,'Samsung-3471ND',1306),
 (1315,'SF',1292),
 (1316,'SCX',1292),
 (1317,'Samsung-4200',1316),
 (1318,'Samsung-4500',1316),
 (1319,'Samsung-4521FG',1316),
 (1320,'Samsung-4720FN',1316),
 (1321,'Samsung-4725FN',1316),
 (1322,'Samsung-6322DN',1316),
 (1323,'Xerox',14),
 (1324,'Phaser',1323),
 (1325,'Xerox-6130',1324),
 (1326,'Xerox-7760',1324);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;


--
-- Table structure for table `meteriffic`.`property`
--

DROP TABLE IF EXISTS `property`;
CREATE TABLE `property` (
  `pkey` varchar(45) NOT NULL,
  `pvalue` varchar(45) NOT NULL,
  PRIMARY KEY  (`pkey`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `meteriffic`.`property`
--

/*!40000 ALTER TABLE `property` DISABLE KEYS */;
INSERT INTO `property` (`pkey`,`pvalue`) VALUES 
 ('admin.email','rajashree@sourcen.com'),
 ('application.timezone','WET'),
 ('category_id','14'),
 ('database.driver','com.mysql.jdbc.Driver'),
 ('database.password',''),
 ('database.url','jdbc:mysql://localhost:3306'),
 ('defaultadminsearch','2'),
 ('defaultapplicationsearch','1'),
 ('enableEmailValidation','true'),
 ('enablePasswordReset','true'),
 ('enableRegistration','true'),
 ('feedback.mail.fromaddress','rajashree@sourcen.com'),
 ('feedback.mail.subject','AcceptSearch Feedback'),
 ('feedback.mail.toaddress','rajashree@sourcen.com'),
 ('mail.smtp.host','smtp.1and1.com'),
 ('mail.smtp.password','sniplpass'),
 ('mail.smtp.port','25'),
 ('mail.smtp.user','test@snipl.com');
/*!40000 ALTER TABLE `property` ENABLE KEYS */;


--
-- Table structure for table `meteriffic`.`role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `role` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `meteriffic`.`role`
--

/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`role`) VALUES 
 ('ROLE_ADMIN'),
 ('ROLE_USER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;


--
-- Table structure for table `meteriffic`.`saved_search`
--

DROP TABLE IF EXISTS `saved_search`;
CREATE TABLE `saved_search` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `title` varchar(45) NOT NULL,
  `description` varchar(300) NOT NULL,
  `user` varchar(75) NOT NULL,
  `isbuzz` tinyint(1) NOT NULL,
  `isvolume` tinyint(1) NOT NULL,
  `productsIds` varchar(445) NOT NULL,
  `featureIds` varchar(445) NOT NULL,
  `sentimentIds` varchar(45) NOT NULL,
  `sourceTypes` varchar(45) NOT NULL,
  `orientation` varchar(45) NOT NULL,
  `startdate` varchar(45) default NULL,
  `enddate` varchar(45) default NULL,
  `created` varchar(45) NOT NULL,
  `modified` varchar(45) NOT NULL,
  `isPrivate` tinyint(1) NOT NULL,
  PRIMARY KEY  USING BTREE (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `meteriffic`.`saved_search`
--

/*!40000 ALTER TABLE `saved_search` DISABLE KEYS */;
INSERT INTO `saved_search` (`id`,`title`,`description`,`user`,`isbuzz`,`isvolume`,`productsIds`,`featureIds`,`sentimentIds`,`sourceTypes`,`orientation`,`startdate`,`enddate`,`created`,`modified`,`isPrivate`) VALUES 
 (1,'Application Default Search','Application Default Search Description','admin',0,0,'1162,1186','643','1,2,3,4','review,blog,forum,microblog,news,other','Products','2000-06-08','2009-06-10','Jun 23, 2009 1:11:02 PM WEST','Jun 23, 2009 1:11:02 PM WEST',1),
 (2,' Default Search','Default Search Description','admin',0,0,'1162,1186,1218,1231,1243,1258,1275,1283,1292,1323','643,644,645,646,656,657,659,669,672,674,675,676','1,2,3,4','review,blog,forum,microblog,news,other','Products','','','Jul 27, 2009 10:12:57 AM WEST','Jul 27, 2009 10:22:39 AM WEST',1);
/*!40000 ALTER TABLE `saved_search` ENABLE KEYS */;


--
-- Table structure for table `meteriffic`.`user_role`
--

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `username` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  PRIMARY KEY  USING BTREE (`username`,`role`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `meteriffic`.`user_role`
--

/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`username`,`role`) VALUES 
 ('admin','ROLE_ADMIN');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;


--
-- Table structure for table `meteriffic`.`users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `username` varchar(45) NOT NULL,
  `firstname` varchar(45) NOT NULL,
  `lastname` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `created` varchar(45) NOT NULL,
  `modified` varchar(45) NOT NULL,
  `password` varchar(65) NOT NULL,
  `enabled` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `meteriffic`.`users`
--

/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`,`username`,`firstname`,`lastname`,`email`,`created`,`modified`,`password`,`enabled`) VALUES 
 (2,'admin','Administrator','Administrator','rajashree@sourcen.com','Jun 23, 2009 1:03:53 PM WEST','Jun 23, 2009 1:07:31 PM WEST','b5576fe350c16c7ecbcc8443b21abae7f47ac27a75b5107096cb9efe9330496d',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
