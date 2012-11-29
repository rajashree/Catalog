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
 (5,'banner','banner for default theme','<div class=\"banner\">  <div class=\"editable\"><span style=\"color: rgb(150, 212, 17);\">Turn Your<span style=\"color: rgb(139, 139, 139);\">Sales Staff</span></span><br/>\r\n    <br/>\r\n    <span style=\"color: rgb(150, 212, 17); padding-left: 50px;\">Into <span style=\"color: rgb(139, 139, 139);\">Great Producers</span></span> </div></div>',0,'2008-09-01 18:26:55','2008-09-01 18:26:55'),
 (6,'content','AB Content for default theme','<div class=\"options\">\r\n    <div class=\"option_sub\">\r\n      <h1><a title=\"your sales team can do more than just wait for good leads to come to them...\" href=\"index.php?p=3\"> <div class=\"editable\">your sales team can do more than just wait for good leads to come to them...</div></a></h1>\r\n      <div class=\"option_text\">  <div class=\"editable\">A company\'s sales team should provide a return-on-investment like any corporate resource. Our expertise is in building sales teams that turn leads into deals.</div></div>\r\n      <a title=\"Increase Sales\" class=\"option_link\" href=\"index.php?p=3\">Increase Sales</a> </div>\r\n    <div class=\"option_sub\">\r\n      <h1><a title=\"shouldnÃ??Ã?Â¢??t your sales team close more deals?\" href=\"index.php?p=4\">shouldnÃ??Ã?Â¢??t your sales team close more deals?</a></h1>\r\n      <div class=\"option_text\">  <div class=\"editable\">If youÃ??Ã?Â¢??re spending more than 20% of your gross margin per month on supplying your team with leads, our advanced lead generation strategies can significantly improve your return on investment.</div></div>\r\n      <a title=\"Maximize Lead Conversion\" class=\"option_link\" href=\"index.php?p=4\">Maximize Lead Conversion</a></div>\r\n  </div>',0,'2008-09-01 18:27:32','2008-09-01 18:27:32'),
 (7,'htmlheader','html header for default theme','<html>\r\n<head >\r\n<title>Source(N)</title>\r\n<style type=\"text/css\">\r\n/*margin and padding on body element\r\n  can introduce errors in determining\r\n  element position and are not recommended;\r\n  we turn them off as a foundation for YUI\r\n  CSS treatments. */\r\nbody {\r\n	margin:0;\r\n	padding:0;\r\n}\r\n</style>\r\n\r\n<link rel=\"stylesheet\" type=\"text/css\" href=\"${base}/build/fonts/fonts-min.css\" />\r\n<link rel=\"stylesheet\" type=\"text/css\" href=\"${base}/build/container/assets/skins/sam/container.css\" />\r\n<link rel=\"stylesheet\" type=\"text/css\" href=\"${base}/build/menu/assets/skins/sam/menu.css\" />\r\n<link rel=\"stylesheet\" type=\"text/css\" href=\"${base}/build/button/assets/skins/sam/button.css\" />\r\n<link rel=\"stylesheet\" type=\"text/css\" href=\"${base}/build/editor/assets/skins/sam/editor.css\" />\r\n<script type=\"text/javascript\" src=\"${base}/build/utilities/utilities.js\"></script>\r\n<script type=\"text/javascript\" src=\"${base}/build/container/container_core-min.js\"></script>\r\n<script type=\"text/javascript\" src=\"${base}/build/menu/menu-min.js\"></script>\r\n<script type=\"text/javascript\" src=\"${base}/build/button/button-min.js\"></script>\r\n<script type=\"text/javascript\" src=\"${base}/build/editor/editor-beta-min.js\"></script>\r\n<style>\r\n    .yui-editor-container {\r\n        position: absolute;\r\n        top: -9999px;\r\n        left: -9999px;\r\n        z-index: 999;\r\n    }\r\n    #editor {\r\n        visibility: hidden;\r\n        position: absolute;\r\n    }\r\n    .editable {\r\n       border:1px dashed blue;\r\n        margin: .25em;\r\n        float: left;\r\n        width: 350px;\r\n        height: 100px;\r\n        overflow: auto;\r\n    }\r\n</style>\r\n\r\n<style media=\"screen\" type=\"text/css\">\r\n@import url(${base}/sites/${site_id}/css/style.css );\r\n</style>\r\n\r\n</head>\r\n<body  class=\" yui-skin-sam\">\r\n<textarea id=\"editor\"></textarea> \r\n<div class=\"wrapper\" id=\"editable_cont\" >',1,'2008-09-02 16:15:53','2008-09-02 16:15:53'),
 (8,'htmlfooter','html footer for default theme','</div>\r\n\r\n<script>\r\n\r\n(function() {\r\n\r\nvar Dom = YAHOO.util.Dom,\r\n        Event = YAHOO.util.Event,\r\n        editing = null;\r\n    \r\n    YAHOO.log(\'Setup a stripped down config for the editor\', \'info\', \'example\');\r\n    var myConfig = {\r\n        height: \'150px\',\r\n        width: \'380px\',\r\n        animate: true,\r\n        toolbar: {\r\n            titlebar: \'My Editor\',\r\n            limitCommands: true,\r\n            collapse: true,\r\n            buttons: [\r\n                { group: \'textstyle\', label: \'Font Style\',\r\n                    buttons: [\r\n                        { type: \'push\', label: \'Bold\', value: \'bold\' },\r\n                        { type: \'push\', label: \'Italic\', value: \'italic\' },\r\n                        { type: \'push\', label: \'Underline\', value: \'underline\' },\r\n                        { type: \'separator\' },\r\n                        { type: \'select\', label: \'Arial\', value: \'fontname\', disabled: true,\r\n                            menu: [\r\n                                { text: \'Arial\', checked: true },\r\n                                { text: \'Arial Black\' },\r\n                                { text: \'Comic Sans MS\' },\r\n                                { text: \'Courier New\' },\r\n                                { text: \'Lucida Console\' },\r\n                                { text: \'Tahoma\' },\r\n                                { text: \'Times New Roman\' },\r\n                                { text: \'Trebuchet MS\' },\r\n                                { text: \'Verdana\' }\r\n                            ]\r\n                        },\r\n                        { type: \'spin\', label: \'13\', value: \'fontsize\', range: [ 9, 75 ], disabled: true },\r\n                        { type: \'separator\' },\r\n                        { type: \'color\', label: \'Font Color\', value: \'forecolor\', disabled: true },\r\n                        { type: \'color\', label: \'Background Color\', value: \'backcolor\', disabled: true }\r\n                    ]\r\n                }\r\n            ]\r\n        }\r\n    };\r\n\r\n    YAHOO.log(\'Override the prototype of the toolbar to use a different string for the collapse button\', \'info\', \'example\');\r\n    YAHOO.widget.Toolbar.prototype.STR_COLLAPSE = \'Click to close the editor.\';\r\n    YAHOO.log(\'Create the Editor..\', \'info\', \'example\');\r\n    myEditor = new YAHOO.widget.Editor(\'editor\', myConfig);\r\n    myEditor.on(\'toolbarLoaded\', function() {\r\n        YAHOO.log(\'Toolbar is loaded, add a listener for the toolbarCollapsed event\', \'info\', \'example\');\r\n        this.toolbar.on(\'toolbarCollapsed\', function() {\r\n            YAHOO.log(\'Toolbar was collapsed, reposition and save the editors data\', \'info\', \'example\');\r\n            Dom.setXY(this.get(\'element_cont\').get(\'element\'), [-99999, -99999]);\r\n            Dom.removeClass(this.toolbar.get(\'cont\').parentNode, \'yui-toolbar-container-collapsed\');\r\n            myEditor.saveHTML();\r\n            editing.innerHTML = myEditor.get(\'element\').value;\r\n            editing = null;\r\n        }, myEditor, true);\r\n    }, myEditor, true);\r\n    myEditor.render();\r\n\r\n    Event.on(\'editable_cont\', \'dblclick\', function(ev) {\r\n        var tar = Event.getTarget(ev);\r\n    \r\n  if (Dom.hasClass(tar, \'editable\')) {\r\n            YAHOO.log(\'An element with the classname of editable was double clicked on.\', \'info\', \'example\');\r\n            if (editing !== null) {\r\n                YAHOO.log(\'There is an editor open, save its data before continuing..\', \'info\', \'example\');\r\n                myEditor.saveHTML();\r\n                editing.innerHTML = myEditor.get(\'element\').value;\r\n            }\r\n            YAHOO.log(\'Get the XY position of the Element that was clicked\', \'info\', \'example\');\r\n            var xy = Dom.getXY(tar);\r\n            YAHOO.log(\'Set the Editors HTML with the elements innerHTML\', \'info\', \'example\');\r\n            myEditor.setEditorHTML(tar.innerHTML);\r\n            YAHOO.log(\'Reposition the editor with the elements xy\', \'info\', \'example\');\r\n            Dom.setXY(myEditor.get(\'element_cont\').get(\'element\'), xy);\r\n            editing = tar;\r\n        }\r\n    });\r\n\r\n})();\r\n</script>\r\n</body>\r\n</html>',0,'2008-09-02 16:16:27','2008-09-02 16:16:27'),
 (9,'about body','','<div class=\"inner_body\">\r\n<div class=\"body_head\"/>\r\n<div class=\"body_cont\">\r\n    <div class=\"left\">\r\n  <div class=\"sbar_top\"><img alt=\"Testimonial\" src=\"http://www.revenueresultsinc.com/wp-content/themes/micro/images/sbarimg/5.jpg\"/></div>\r\n  <div class=\"sbar_testi\">    <div class=\"act_testi\"> \"Revenue Results has a unique ability to dramatically increase conversion on inbound leads to deals. Their ability to simplify the sales process and guide our team to the desired outcome is hard to duplicate. We have found their services to be absolutely essential to a strong sales ROI.\" </div>\r\n    <div class=\"test_name\"><strong>Ken Gootnick </strong><br/>\r\n      Sounding Board, LLC<br/>\r\n      <a title=\"\" href=\"index.php?p=8\">More Testimonials</a></div>\r\n    <div class=\"test_link\"/>\r\n  </div>\r\n  <h3><a title=\"\" href=\"index.php?p=6\" class=\"sbar_down\">Download</a></h3>\r\n  <h3><a title=\"\" href=\"index.php?p=9\" class=\"sbar_bott\">Call Us</a></h3>\r\n</div>\r\n  <div class=\"right\">\r\n    <div class=\"main_cont\">\r\n            <div id=\"post-7\" class=\"post\">\r\n        <div class=\"posttop\">\r\n                    <h1 class=\"posttitle\">\r\n            <!--<a href=\"http://www.revenueresultsinc.com/index.php/about-us/\" rel=\"bookmark\">-->\r\n            ABOUT US            <!--</a>-->\r\n          </h1>\r\n          <div>\r\n            <p>Revenue Results is a sales consulting firm that develops high conversion sales programs for its clients. Project services typically bundle together sales strategy planning, lead generation, sales compensation, CRM implementation and lead websites. All delivered by a team of marketing, design, and sales experts.</p>\r\n<p>Typical clients have included software development, technology companies, training and consulting firms, travel providers, telecommunications firms, and executive recruitment companies.</p>\r\n<p>Web 2.0 sales experts and authors Jim Weldon and Tom Nevins are the co-founders of Silicon Valley based Revenue Results. Their work has been recognized by a number of industry organizations such as YEO, SV Startups and National Sales Trainers. They are regular contributors to “CEO Sales Talk” as well as a number of other leading e-zines and blogs that focus on selling over the web and managing your sales supply chain. The team at Revenue Results has started or advised a dozen startups during their career as well as worked for a number of Fortune 500 firms in the technology marketplace. This combined expertise has given the founders a great foundation to see what does and doesn’t work for companies in need of sales expertise.</p>\r\n<p><a title=\"free sales analysis\" class=\"option_link\" href=\"index.php/free-sales-analysis/\">FREE SALES ANALYSIS – SEE IF YOU QUALIFY</a></p>\r\n          </div>\r\n        </div>\r\n      </div>\r\n          </div>\r\n  </div>\r\n  </div>\r\n\r\n\r\n<div class=\"footer\">\r\n  <div style=\"float: left;\"> <a title=\"\" href=\"http://www.revenueresultsinc.com\">Home</a> <a title=\"\" href=\"index.php?p=12\">FAQ</a> <a title=\"\" href=\"index.php?p=13\">Privacy Policy</a><!--<a href=\"index.php?p=11\" title=\"\">Site Map</a>--> <a title=\"\" href=\"index.php?p=9\">Request More Info</a> <a title=\"\">Call Us: 877 - 300 - 8243</a> </div>\r\n  <div style=\"float: right;\"> <a title=\"\">copyright © 2007 Revenue Results. - All rights reserved</a> </div>\r\n</div>\r\n</div>',0,'2008-09-10 13:15:40','2008-09-10 13:15:40');
/*!40000 ALTER TABLE `blocks` ENABLE KEYS */;


--
-- Definition of table `page_blocks`
--

DROP TABLE IF EXISTS `page_blocks`;
CREATE TABLE `page_blocks` (
  `pid` int(11) NOT NULL,
  `bid` varchar(64) NOT NULL,
  `pos` smallint(5) unsigned NOT NULL default '0',
  PRIMARY KEY  (`pid`,`bid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `page_blocks`
--

/*!40000 ALTER TABLE `page_blocks` DISABLE KEYS */;
INSERT INTO `page_blocks` (`pid`,`bid`,`pos`) VALUES 
 (5,'3',1),
 (5,'4',5),
 (5,'5',2),
 (5,'6',3),
 (5,'7',0),
 (5,'8',6),
 (11,'3',1),
 (11,'7',0);
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
 (11,'maximize-lead-strategies','maximize-lead-strategies','maximize-lead-strategies',1,'2008-09-05 13:05:47','2008-09-05 13:05:47'),
 (12,'about-us','about-us','',1,'2008-09-10 13:12:32','2008-09-10 13:12:32');
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
 (1,'default','default','2008-09-02 18:50:39','2008-09-02 18:50:39'),
 (2,'weeeeeee','eeeeeeeeeeeeee','2008-09-10 15:36:41','2008-09-10 15:36:41');
/*!40000 ALTER TABLE `themes` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
