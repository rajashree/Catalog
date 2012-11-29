/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory method to supplant dozer mapping. Extending entities 
 * must define ObjectMap methods using the ObjectMap annotation.
 * Named mappings will only be utilized if referenced by name!
 * Requires explicit mappings.
 *
 */
public abstract class Mapper 
{

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Map<Class<?>, Map<Class<?>, Method>> mappings;
	protected Map<String, Method> namedMappings;

	protected Mapper() {
		init();
	}
	
	private void init()
	{
		Class<?> clazz = this.getClass();
		mappings = new HashMap<Class<?>, Map<Class<?>, Method>>();
		namedMappings = new HashMap<String, Method>();

		for(Method m : clazz.getDeclaredMethods()) {
			ObjectMap om = m.getAnnotation(ObjectMap.class);
			if(om == null)
				continue;
			//named mappings are a direct reference,
			//we do not vet the inputs and return types.
			if(om.name().trim().length() > 0)
				namedMappings.put(om.name(), m);
			else
			{
				for(Class<?> parm : m.getParameterTypes()) {
					Map<Class<?>, Method> returnTypes = mappings.get(parm);
					if(returnTypes == null)
						returnTypes = new HashMap<Class<?>, Method>(3);
					returnTypes.put(m.getReturnType(), m);
					mappings.put(parm, returnTypes);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T doMapping(Object source, Class<T> target, String name)
	{
		//factory method, add your mapping here.
		if(source == null)
			return null;
		if(name == null || name.trim().length() == 0)
			return doMapping(source, target);

		T result = null;
		try
		{
			Method m = namedMappings.get(name);
			if(m != null)
				result = (T) m.invoke(this, source);

		} catch (Exception e) {
            logger.error("Error during mapping", e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> T doMapping(Object source, Class<T> target)
	{
		//factory method, add your mapping here.
		if(source == null)
			return null;
		if(target == null)
			return null;

		T result = null;
		try
		{
			boolean hasMapping = false;

			Map<Class<?>, Method> returnTypes = mappings.get(source.getClass());
			if(returnTypes != null)
			{
				Method m = returnTypes.get(target);
				if(m != null)
				{
					hasMapping = true;
					result = (T) m.invoke(this, source);
				}
			}
			if(!hasMapping)
			{
                logger.warn("NO MAPPING FOUND, USING REFLECTION FOR: source["+source+"]; target["+target+"]");
				result = target.newInstance();
				PropertyUtils.copyProperties(result, source);
			}
		} catch (Exception e) {
            logger.error("Error during mapping", e);
		}
		return result;
	}

}
