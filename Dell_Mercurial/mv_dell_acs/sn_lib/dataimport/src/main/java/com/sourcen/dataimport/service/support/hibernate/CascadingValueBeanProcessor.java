/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.hibernate;

import com.sourcen.core.persistence.domain.Entity;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * allows you to have "image.name" in the destination of mapping xml files.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class CascadingValueBeanProcessor extends BeanProcessorAdapter {

    @Override
    public boolean supportsBean(final Class clazz) {
        return Entity.class.isAssignableFrom(clazz);
    }

    @Override
    public Object preProcessBeforePersist(final Object bean, final Map<String, Object> row) {
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            int dotIndex = entry.getKey().indexOf(".");
            String mapping = entry.getKey();
            if (dotIndex > -1) {
                // example .. source="Util_ID" destination="user.account.id"
                String[] depth = mapping.split("\\."); // the user and account are 2 different objects., and id is the field to set the value for, hence ignore the id.
                try {
                    Object depthObject = bean;
                    for (int i = 0; i < depth.length - 1; i++) {
                        String depthNode = depth[i];
                        Assert.isTrue(depthNode != null && depthNode.trim().length() > 0,
                                "invalid value for mapping:=" + mapping + " on " + bean);

                        if (depthObject == null) {

                            logger.info("while mapping fields, we found a null value for mapping:="
                                    + mapping + " on :=" + entry.getValue() + " for bean:=" + bean);
                            break;
                        }


                        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(depthObject.getClass(), depthNode);
                        if (pd == null) {
                            logger.warn(" unable to find PD in class:=" + bean.getClass() + ", depthNode:=" + depthNode);
                        }
                        else if (pd.getReadMethod() != null) {
                            if (depthObject == null) {
                                logger.info("Unable to find value for " + mapping + " in " + bean);
                                break;
                            }
                            depthObject = pd.getReadMethod().invoke(depthObject);
                            if (depthObject == null) {
                                // create the object.
                                logger.debug("creating a new instance of " + pd.getReadMethod().getReturnType() + " to set the depth values.");
                                try {
                                    depthObject = pd.getReadMethod().getReturnType().newInstance();
                                } catch (Exception e) {
                                    logger.error("Unable to create a new instance of " + pd.getReadMethod().getReturnType() + " to set the depth values.", e);
                                }
                            }
                        }
                    }
                    if (depthObject != null) {
                        // this is the final object to set the value.

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.preProcessBeforePersist(bean, row);

    }
}
