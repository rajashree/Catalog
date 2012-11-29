#!/bin/bash

#MArketvine Content Server PROD Deployment Script



#Date
NOW=`date +"%b-%d-%Y"`
#NOW=`date +"%b-%d-%Y-%H:%M"`

# Where are the Tomcat binaries
CATALINA_HOME=/opt/dell_acs/tomcat-7.0.27/

# Prod Backup Directory
PROD_BACKUP_DIR=/opt/dell_acs/tomcat-7.0.27/webapps/backups

#Prod Webapps Directory
PROD_WEBAPPS_DIR=/opt/dell_acs/tomcat-7.0.27/webapps

# MySQL PROD Host
MYSQL_PROD_HOST=Ena-Iads-Stage-LinSql01.dmz.marketvine

#MySQL PROD User Name
MYSQL_PROD_USER=deployer

#MySQL PROD Password
MYSQL_PROD_PASS=w1lmpj2hzQhL

#MySQL PROD DB Name
MYSQL_PROD_DB_NAME=dell_acs_stage

DEPLOYMENT_FLAG_FILE=/opt/dell_acs/tomcat-7.0.27/webapps/newdeploymentready.txt


FILE_NAME=`ls -tr  <%= @updates_path %>/*.war | tail -1 `
BASE_NAME=`basename "$FILE_NAME" .war`



#Copy WAR file form UAT 
copy_war_from_uat () {
  #Create Directory for New Version
  mkdir -p $PROD_WEBAPPS_DIR/$BASE_NAME
  cp $FILE_NAME $PROD_WEBAPPS_DIR/$BASE_NAME
 }

#Extract WAR file
extract_war () {
  /usr/bin/unzip $PROD_WEBAPPS_DIR/$BASE_NAME/$BASE_NAME.war -d $PROD_WEBAPPS_DIR/$BASE_NAME 2>&1 > /dev/null
  #cp -f $PROD_BACKUP_DIR/application.properties $PROD_BACKUP_DIR/Prod_Current/WEB-INF/classes
  rm -rf $PROD_WEBAPPS_DIR/$BASE_NAME/$BASE_NAME.war
  
sed 's/app\.profile=/app\.profile=<%= @dell_acs_profile %>\n#app\.profile=/' $PROD_WEBAPPS_DIR/$BASE_NAME/WEB-INF/classes/application.properties > $PROD_WEBAPPS_DIR/$BASE_NAME/WEB-INF/classes/application_temp.properties

rm -f $PROD_WEBAPPS_DIR/$BASE_NAME/WEB-INF/clases/application.properties
mv $PROD_WEBAPPS_DIR/$BASE_NAME/WEB-INF/classes/application_temp.properties $PROD_WEBAPPS_DIR/$BASE_NAME/WEB-INF/classes/application.properties
  
  
}

#Stop Tomcat
stop_tomcat () {
  $CATALINA_HOME/bin/shutdown.sh 2>&1 > /dev/null
  if [ `ps auxwwww|grep java|grep -v grep|wc -l` -gt 0 ]
  then
    for pid in `ps aux | pgrep java`
    do
      kill -9 $pid 2>&1 > /dev/null
    done
  fi
}

#MySQL PROD DB Backup
mysql_db_backup () {
  /usr/bin/mysqldump -u $MYSQL_PROD_USER -p$MYSQL_PROD_PASS $MYSQL_PROD_DB_NAME -h $MYSQL_PROD_HOST > $PROD_BACKUP_DIR/$MYSQL_PROD_DB_NAME_$NOW.sql
}

#App Deploy
app_deploy () {
#echo "1"
  if ! [ $PROD_WEBAPPS_DIR/ROOT -ef $WEB_WEBAPPS_DIR/ROOT_BACKUP1 ];
  then
#echo "2"  
    #echo "it works"
     # Previous deployment was clean.
     #Remove 2nd backup.
     if [ -e $PROD_WEBAPPS_DIR/ROOT_BACKUP2 ]; then
#echo "3"	 
       # if the symbolic link exists remove it
        DELETE_PATH=`readlink $PROD_WEBAPPS_DIR/ROOT_BACKUP2`
        unlink $PROD_WEBAPPS_DIR/ROOT_BACKUP2
#echo "rm -rf $DELETE_PATH"		
        rm -rf $DELETE_PATH
     fi
     #Point 2 to 1.
     if [ -e $PROD_WEBAPPS_DIR/ROOT_BACKUP1 ]; then
#echo "4"	 
        NEW_PATH=`readlink $PROD_WEBAPPS_DIR/ROOT_BACKUP1`
        if [ 1 -eq 1 ] & [ -e $PROD_WEBAPPS_DIR/ROOT_BACKUP2 ]; then
           unlink $PROD_WEBAPPS_DIR/ROOT_BACKUP2
        fi
        ln -sf $NEW_PATH $PROD_WEBAPPS_DIR/ROOT_BACKUP2
     fi
     #Point 1 to ROOT
     if [ -e $PROD_WEBAPPS_DIR/ROOT ]; then
#echo "5"	 
        #echo "readlink $PROD_WEBAPPS_DIR/ROOT"
        NEW_PATH=`readlink $PROD_WEBAPPS_DIR/ROOT`
        #echo $NEW_PATH
        if [ 1 -eq 1 ] & [ -e $PROD_WEBAPPS_DIR/ROOT_BACKUP1 ]; then
          #echo "New 4"
          unlink $PROD_WEBAPPS_DIR/ROOT_BACKUP1
        fi
        ln -sf $NEW_PATH $PROD_WEBAPPS_DIR/ROOT_BACKUP1
     else
#echo "6"	 
        echo "ROOT does not exsit.  Configuration error."
        exit 1
     fi
  fi

  echo "Deploying new WAR File"
  if [ 1 -eq 1 ] & [ -e $PROD_WEBAPPS_DIR/ROOT ]; then
     unlink $PROD_WEBAPPS_DIR/ROOT
     #echo "Removed ROOT link"
  fi
  #echo "ln -sf $PROD_WEBAPPS_DIR/$BASE_NAME $PROD_WEBAPPS_DIR/ROOT"
  #ls -l $PROD_WEBAPPS_DIR
  ln -sf $PROD_WEBAPPS_DIR/$BASE_NAME $PROD_WEBAPPS_DIR/ROOT
  #ls -l $PROD_WEBAPPS_DIR

  echo "Deleting WORK directory"
  rm -rf $CATALINA_HOME/work/Catalina
}

#Start Tomcat 
start_tomcat () {
  $CATALINA_HOME/bin/startup.sh 2>&1 > /dev/null
}


if [ -d $PROD_WEBAPPS_DIR/$BASE_NAME ];
then
  echo 'Most recent version already exists.  Early Out.  Nothing to deploy.'
  exit 0
fi

echo "Stopping Tomcat"
stop_tomcat
echo "Backing up PROD DB"
mysql_db_backup
echo "Coping WAR file from UAT"
copy_war_from_uat
echo "Uncompressing WAR"
extract_war
echo "Deploying Application"
app_deploy
echo "Starting Tomcat"
start_tomcat 
echo "Deployment Successful"

exit 0


