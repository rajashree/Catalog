/***********************************************************************
* Raining Data Corp.
*
* Copyright (c) Raining Data Corp. All Rights Reserved.
*
* This software is confidential and proprietary information belonging to
* Raining Data Corp. It is the property of Raining Data Corp. and is protected
* under the Copyright Laws of the United States of America. No part of this
* software may be copied or used in any form without the prior
* written permission of Raining Data Corp.
*
************************************************************************/
package com.job;

import com.job.JobEntryConfig.TaskInfo;


/**
 * JobFactory is a helper class for creating job objects.
 */
public class JobFactory {

   /**
    * Class constructor
    */
    private JobFactory() {
    }

    /**
     * Gets the job that can handle the given job instance
     * @param   taskInfo    task entry object in config file
     * @return  <code>Job</code> that can handle the given job instance
     */
    public static Job getJob(TaskInfo taskInfo) {

        Job job = null;

        if (taskInfo == null) {
            return null;
        }

		if (taskInfo != null && taskInfo.hasClassName()) {
            // get the implementation class using reflection
            try {
                Class jobClass = Class.forName(taskInfo.getClassName());
                job = (Job) jobClass.newInstance();

            } catch (Exception e) {
				e.printStackTrace();
            }
        }

        return job;
    }
}