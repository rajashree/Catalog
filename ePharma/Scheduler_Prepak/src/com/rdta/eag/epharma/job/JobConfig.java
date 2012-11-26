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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;



/**
 * JobConfig represents configuration information for jobs in the system.
 *
 */
public class JobConfig {

	
    public static final String JOB_NODE = "job";
	  
    // jobs
    private Map jobs = null;
    private List ljobs = null;
     
    /**
     * Class Constructor that creates a JobConfig object from a DOM Document
     * @param    doc DOM Document
     */
    public JobConfig(Document doc) {

        Element root = doc.getDocumentElement();

        // load jobs
        List tmpJobs = getJobs(root);
        // add jobs
        if (tmpJobs != null && !tmpJobs.isEmpty()) {
            for (int i = 0; i < tmpJobs.size(); i++) {
                JobEntryConfig job = (JobEntryConfig) tmpJobs.get(i);
                addJob(job);
            }
        }
    }

    /**
     * Gets a list of JobEntry objects.
     * @return  List of JobEntryConfig objects
     */
    public List getJobsList() {
        return ljobs;
    }

    /**
     * Gets a job entry for the given type.
     * @param   type  of job
     * @return  <code>JobEntryConfig</code> object for the given type.
     */
    public JobEntryConfig getJob(String type) {
        return (jobs != null ? (JobEntryConfig) jobs.get(type) : null);
    }

    /**
     * Checks if there are any job entries.
     * @return  <code>true</code> if there are job entries, else <code>false</code>
     */
    public boolean hasJobs() {
        return (jobs != null && !jobs.isEmpty());
    }

    /**
     * Adds a job entry.
     * @param  job <code>JobEntryConfig</code>.
     */
    private void addJob(JobEntryConfig job) {
        if (job == null) {
            return;
        }

        if (jobs == null) {
            jobs = new HashMap(11);
            ljobs = new ArrayList(11);
        }

        jobs.put(job.getType(),job);
        ljobs.add(job);
    }

   
    /**
     * Checks if the configuration is valid.
     * @return   <code>true</code> if valid, else <code>false</code>
     */
    public boolean isValid() {
        // must have jobs
        if (!hasJobs()) {
            return false;
        }
        
        return true;
    }
	
	
	/**
     * Gets a <code>List</code> of <code>JobEntryConfig</code> objects from a jobs node.
     * @param    node    jobs node
     * @return   <code>List</code> of <code>JobEntryConfig</code> objects
     */
    private List getJobs(Node node) {
     
		List jobs = new ArrayList(5);
		      
		NodeList nodeList = node.getChildNodes();
		int length = nodeList.getLength();
		String nodeName = null;
		Node childNode = null;
        for (int i = 0; i < length; i++) {
            childNode = nodeList.item(i);
			nodeName = childNode.getNodeName();
			if (childNode.getNodeType() == Node.ELEMENT_NODE && nodeName.equals(JOB_NODE) ) {
                   jobs.add(new JobEntryConfig(childNode));
             }
        }
        return jobs;
    }
 
	
	
}