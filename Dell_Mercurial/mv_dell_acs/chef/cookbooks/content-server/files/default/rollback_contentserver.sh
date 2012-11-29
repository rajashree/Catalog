#!/bin/bash

#Marketvine Content Server Production Rollback Script

exit 0

#Date
NOW=`date +"%b-%d-%Y"`

# Where are the Tomcat binaries
CATALINA_HOME=

# Prod Backup Directory
PROD_BACKUP_DIR=

# MySQL PROD Host
MYSQL_PROD_HOST=

#MySQL PROD User Name
MYSQL_PROD_USER=

#MySQL PROD Password
MYSQL_PROD_PASS=

#MySQL PROD DB Name
MYSQL_PROD_DB_NAME=

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

#DB Restore
db_restore () {
/usr/bin/mysql -u $MYSQL_PROD_USER -p$MYSQL_PROD_PASS $MYSQL_PROD_DB_NAME -h $MYSQL_PROD_HOST < $PROD_BACKUP_DIR/$MYSQL_PROD_DB_NAME_$NOW.sql
}

#App Restore
app_restore () {
unlink $CATALINA_HOME/webapps/ROOT
ln -s $PROD_BACKUP_DIR/Prod_Old $CATALINA_HOME/webapps/ROOT
echo "Deleting WORK directory"
rm -rf $CATALINA_HOME/work/Catalina
rm -rf $PROD_BACKUP_DIR/Prod_Current
}

#Start Tomcat 
start_tomcat () {
$CATALINA_HOME/bin/startup.sh 2>&1 > /dev/null
}

echo "Stopping Tomcat"
stop_tomcat
echo "DB Restore"
db_restore
echo "Application Restore"
app_restore
echo "Starting Tomcat"
start_tomcat
echo "Restore Successfull"

exit 0


#echo 'Running Rollbach.sh'
#exit 1
