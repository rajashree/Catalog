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
package com.rdta.eag.epharma.job;


import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * JobEntryConfig represents configuration information for a job in the system.
 *
 */
public class JobEntryConfig {
	
	public static final String TASK_NODE = "task";

     // type
    private String type = null;
	
	private List taskList = new ArrayList(5);
		
   
    /**
     * Class constructor
     */
    public JobEntryConfig() {
    }

    /**
     * Class constructor that creates a JobEntryConfig object from a DOM Node
     * @param    node    DOM <code>Node</code>
     */
    public JobEntryConfig(Node node) {
        // type
        this.type = ((Element)node).getAttribute("type");
		
		 //read all tasks node and add them to job
		 NodeList nodeList = node.getChildNodes();
		 int length = nodeList.getLength();
	     String nodeName = null;
		 Node childNode = null;
		 for (int i = 0; i < length; i++) {
		    childNode = nodeList.item(i);
			nodeName = childNode.getNodeName();
			//if child node is element node and name should be task
            if (childNode.getNodeType() == Node.ELEMENT_NODE && nodeName.equals(TASK_NODE) ) {
			      TaskInfo taskInfo = new TaskInfo(childNode);
				  //add task to task list
				  addTask(taskInfo);
	        }//end of if loop
         }//end of for loop
	}

    /**
     * Gets the job type
     * @return   job type
     */
    public String getType() {
        return type;
    }

    /**
     * Checks if job has a type.
     * @return   true if there is a type
     *           else false
     */
    public boolean hasType() {
        return (type != null && type.trim().length() > 0);
    }

    /**
     * Registers the job type.
     * @param    type  job type
     */
    public void setType(String type) {
        this.type = type;
    }

	/**
	 * Returns Taks List.
	 * 
	 * @return list
	 */
	public List getTaskList() {
		return taskList;
	}
	
	/**
	 * add TaskInfo object to Task List.
	 * 
	 * @return list
	 */
	public void addTask(TaskInfo taskInfo) {
		
		if(taskInfo.isValid()) {
			taskList.add(taskInfo);
		} else {
			System.out.println(" ERROR: Configured Task:  " + taskInfo.getName()  + " Not Valid");
		}
	}
	
	/**
	 * Return it has Tasks or not
	 * 
	 * @return boolean
	 */
	public boolean hasTasks() {
		
		return (taskList != null && !taskList.isEmpty());
	}
	
	
  
	    /**
	    * Tasks Info maintains the Tak Information. 
	    * 
	    * @author rsrinivasa
	    *
	    */	
	   public static class TaskInfo  {   
		
		   //task name
			private String name = null;
		    // job class
		    private String className = null;
			
		    // delay duration
		    private int delayDuration = 0;
		    // sleep duration
		    private int sleepDuration = 0;
							
			
			public TaskInfo(){
				
			}
			
			/**
			 * Populate the task from the Taks Node
			 * 
			 * @param taskNode
			 */
			public TaskInfo(Node taskNode) {
				
				//type
		        this.name = ((Element)taskNode).getAttribute("name");

		        // job class
		        this.className = ((Element)taskNode).getAttribute("class");

		        // delayDuration
		        String tmpDuration = ((Element)taskNode).getAttribute("delayDuration");
		        if (tmpDuration != null) {
		            try {
		                this.delayDuration = Integer.parseInt(tmpDuration);
		            } catch (NumberFormatException nfe) {
		            }
		        }

		        // sleepDuration
		        tmpDuration = ((Element)taskNode).getAttribute("sleepDuration");
		        if (tmpDuration != null) {
		            try {
		                this.sleepDuration = Integer.parseInt(tmpDuration);
		            } catch (NumberFormatException nfe) {
		            }
		        }
			}
			
					
		    /**
		     * Returns name of the Task.
		     * @return   full class name of the job implementation class
		     */
		    public String getName() {
		        return name;
		    }

		    /**
		     * Checks if job has a name.
		     * @return   <code>true</code> if there is a class name, else <code>false</code>
		     */
		    public boolean hasName() {
		        return (name != null && name.trim().length() > 0);
		    }

		    /**
		     * Set the name of the task
		     * @param    className  full class name of the job
		     */
		    public void setName(String name) {
		        this.name = name;
		    }
			
			
			
			  /**
		     * Gets the job implementation clas.
		     * @return   full class name of the job implementation class
		     */
		    public String getClassName() {
		        return className;
		    }

		    /**
		     * Checks if job has a class name.
		     * @return   <code>true</code> if there is a class name, else <code>false</code>
		     */
		    public boolean hasClassName() {
		        return (className != null && className.trim().length() > 0);
		    }

		    /**
		     * Registers the class name of the jon.
		     * @param    className  full class name of the job
		     */
		    public void setClassName(String className) {
		        this.className = className;
		    }

		   
		    /**
		     * Gets the delay duration for the job.
		     * @return   delay duration for the job.
		     */
		    public int getDelayDuration() {
		        return delayDuration;
		    }

		    /**
		     * Checks if job has a delay duration.
		     * @return   true if there is a delay duration
		     *           else false
		     */
		    public boolean hasDelayDuration() {
		        return (delayDuration > 0);
		    }

		    /**
		     * Registers the delay duration for the job.
		     * @param    delayDuration  delay duration in minutes for the job
		     */
		    public void setDelayDuration(int delayDuration) {
		        this.delayDuration = delayDuration;
		    }

		    /**
		     * Gets the sleep duration for the job.
		     * @return   sleep duration for the job.
		     */
		    public int getSleepDuration() {
		        return sleepDuration;
		    }

		    /**
		     * Checks if job has a sleep duration.
		     * @return   true if there is a sleep duration
		     *           else false
		     */
		    public boolean hasSleepDuration() {
		        return (sleepDuration > 0);
		    }

		    /**
		     * Registers the sleep duration for the job.
		     * @param    sleepDuration  sleep duration in minutes for the job
		     */
		    public void setSleepDuration(int sleepDuration) {
		        this.sleepDuration = sleepDuration;
		    }

		  
		    /**
		     * Checks if the job is valid.
		     * @return   true if valid
		     *           else false
		     */
		    public boolean isValid() {
		        // must have a type, class, source and queue
		        if (!hasClassName() ) {
		            return false;
		        }

		        // job source class must implement Job interface
		        try {
		            // job source impl class
		            Class sourceImpl = Class.forName(className);
				 } catch (ClassNotFoundException cnfe) {
		            return false;
		        }

		        return true;
		    }
	   }


}