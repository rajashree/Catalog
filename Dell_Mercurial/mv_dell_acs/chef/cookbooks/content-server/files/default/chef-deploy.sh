#!/bin/bash

echo "Executing deploy.sh"
sh /etc/marketvine/deploy.sh
if [ $? -ne 0 ]
then
	echo "Rolling back"
	/etc/marketvine/rollback.sh
	if [ $? -ne 0 ] ; then
		echo "Rollback failed"
		#echo 'Two errors'
		#Two errors.  Deploy failed and rollback failed
		exit 2
	else
		echo "Rollback successful"
		#echo 'One error'
		#One error.  Deploy failed but rollback succeeded
		exit 1
	fi
fi