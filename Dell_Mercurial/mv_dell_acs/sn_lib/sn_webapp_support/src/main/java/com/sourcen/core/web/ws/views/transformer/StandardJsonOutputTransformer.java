/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.ws.views.transformer;

import com.sourcen.core.persistence.domain.Entity;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.util.ArrayUtils;
import com.sourcen.core.util.beans.BeanConverter;
import com.sourcen.core.util.beans.JSONBean;
import com.sourcen.core.util.beans.MappingContext;
import com.sourcen.core.util.beans.ScopeAware;
import com.sourcen.core.util.collections.PropertiesProvider;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.JSONUtils;
import net.sf.json.util.PropertyFilter;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: shawn $
 * @version $Revision: 3176 $, $Date:: 2012-06-14 14:38:37#$
 */
public class StandardJsonOutputTransformer extends OutputTransformerAdapter implements JsonOutputTransformer {

    protected JsonConfig jsonConfig;

    protected static final Logger logger = LoggerFactory.getLogger(StandardJsonOutputTransformer.class);

    protected static final BeanConverter<Entity, Entity> ENTITY_SCOPE_WRAPPER =
            new BeanConverter<Entity, Entity>(Entity.class, Entity.class, null);

    static {
        ENTITY_SCOPE_WRAPPER.autoRegisterEntities(true);
    }

    public StandardJsonOutputTransformer() {
    }

    private static final Collection<JsonConfig> registeredConfig = new ArrayList<JsonConfig>();

    public void setJsonConfig(final JsonConfig jsonConfig) {
        if (!registeredConfig.contains(jsonConfig)) {
            jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateProcessor());
            jsonConfig.registerJsonValueProcessor(Timestamp.class, new JsonDateProcessor());
            registeredConfig.add(jsonConfig);
        }
        this.jsonConfig = jsonConfig;
    }

    @Override
    protected Object transformObject(final Object bean) {
        if (bean instanceof JSONObject || bean instanceof JSONArray) {
            return bean;
        }
        if (bean == null) {
            return new JSONObject();
        }
        try {
            if (ClassUtils.isPrimitiveOrWrapper(bean.getClass()) || bean.getClass().equals(String.class)) {
                JSONArray array = new JSONArray();
                array.add(bean);
                return array;
            } else {

                boolean applyScope = false;
                boolean isJsonAware = false;
                // check if we have to apply scope or not.
                if(bean instanceof Iterable){
                    Iterator<?> iterator = ((Iterable) bean).iterator();
                    if(iterator.hasNext()){
                        //CS-544 - We were moving the reference by 2 elements, which throws NoSuchElementException.
                        //Therefore, casting it an Object first and then performing checks.
                        Object object = iterator.next();
                        applyScope = object instanceof ScopeAware;
                        isJsonAware = object instanceof JSONBean;
                    }
                }else if(bean instanceof Map){
                     applyScope = (!((Map) bean).isEmpty() && ((Map) bean).values().iterator() instanceof ScopeAware);
                     isJsonAware = (!((Map) bean).isEmpty() && ((Map) bean).values().iterator() instanceof JSONBean);
                }else {
                    applyScope = (bean instanceof ScopeAware);
                    isJsonAware = (bean instanceof JSONBean);
                }

                if(!applyScope){
                    return JSONSerializer.toJSON(bean, jsonConfig);
                }

                // check if bean has any scopes.
                // fix for https://jira.marketvine.com/browse/EXTERNALINTERACTIVEADS-351
                final String requestScope = request.getParameter("scope");
                final String scope;
                if (requestScope == null || requestScope.isEmpty()) {
                    scope = "default";
                } else {
                    scope = requestScope;
                }

                if(isJsonAware){
                    if(applyScope){
                        if(bean instanceof Iterable){
                            StringBuilder sb = new StringBuilder();
                            Iterator<?> iterator = ((Iterable) bean).iterator();
                            sb.append("[");
                            while(iterator.hasNext()){
                                Object obj = iterator.next();
                                sb.append(((JSONBean) obj).toJSON(scope));
                                if(iterator.hasNext()){
                                    sb.append(",");
                                }
                            }
                            sb.append("]");
                            return sb.toString();
                        }else if(bean instanceof Map){
                            StringBuilder sb = new StringBuilder();
                            sb.append("{");
                            Iterator<?> iterator = ((Map) bean).entrySet().iterator();
                            while(iterator.hasNext()){
                                Map.Entry obj = (Map.Entry) iterator.next();
                                sb.append(JSONUtils.valueToString(obj.getKey()));
                                sb.append(":");
                                sb.append(((JSONBean)obj.getValue()).toJSON(scope));
                                if(iterator.hasNext()){
                                    sb.append(",");
                                }
                            }
                            sb.append("}");
                            return sb.toString();
                        }else {
                            return ((JSONBean) bean).toJSON(scope);
                        }
                    }else{
                        if(bean instanceof Iterable){
                            StringBuilder sb = new StringBuilder();
                            Iterator<?> iterator = ((Iterable) bean).iterator();
                            sb.append("[");
                            while(iterator.hasNext()){
                                Object obj = iterator.next();
                                sb.append(((JSONBean) obj).toJSON());
                                if(iterator.hasNext()){
                                    sb.append(",");
                                }
                            }
                            sb.append("]");
                            return sb.toString();
                        }else if(bean instanceof Map){
                            StringBuilder sb = new StringBuilder();
                            sb.append("{");
                            Iterator<?> iterator = ((Map) bean).entrySet().iterator();
                            while(iterator.hasNext()){
                                Map.Entry obj = (Map.Entry) iterator.next();
                                sb.append(JSONUtils.valueToString(obj.getKey()));
                                sb.append(":");
                                sb.append(((JSONBean)obj.getValue()).toJSON());
                                if(iterator.hasNext()){
                                    sb.append(",");
                                }
                            }
                            sb.append("}");
                            return sb.toString();
                        }else {
                            return ((JSONBean) bean).toJSON();
                        }
                    }
                }

                // check if we are returning entities directly?
                // then wrap then with BeanConverter to skip cyclic references.
                Class<? extends Entity> entityClass;
                final Object transformedBean;
                final MappingContext mappingContext = new MappingContext(scope);
                if (bean instanceof Entity) {
                    entityClass = (Class<? extends Entity>) bean.getClass();
                    transformedBean = ENTITY_SCOPE_WRAPPER.convert((Entity) bean, entityClass.newInstance(), mappingContext);
                } else if (bean instanceof Collection) {
                    // dont need to check if it has elements here.
                    Class<?> itemClass = ((Collection) bean).iterator().next().getClass();
                    if(Entity.class.isAssignableFrom(itemClass)){
                        transformedBean = ENTITY_SCOPE_WRAPPER.convert((Collection<Entity>) bean, itemClass, mappingContext);
                    }  else{
                        logger.info("Unable to transform bean with scope as its not a Entity or Collection of Entities.:="+bean);
                        transformedBean = bean;
                    }
                }else{
                    logger.info("Unable to transform bean with scope as its not a Entity or Collection of Entities.:="+bean);
                    transformedBean = bean;
                }
                if(transformedBean != bean ){
                    JsonHierarchicalStreamDriver driver = new JsonHierarchicalStreamDriver() {
                        @Override
                        public HierarchicalStreamWriter createWriter(final Writer writer) {
                            return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
                        }
                    };
                    XStream xstream = new XStream(driver);
                    xstream.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);


                    xstream.aliasSystemAttribute(null, "class");

                    xstream.registerConverter(new Converter() {
                        @Override
                        public void marshal(final Object source, final HierarchicalStreamWriter writer,
                                final MarshallingContext context) {
                            PropertiesProvider properties = ((PropertiesAware) source).getProperties();
                            for (String key : properties.keySet()) {
                                writer.startNode(key);
                                writer.setValue("'"+ StringEscapeUtils.escapeJavaScript(properties.getProperty(key))+"'");
                                writer.endNode();

                            }
                        }

                        @Override
                        public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext context) {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public boolean canConvert(final Class type) {
                            return type.equals(PropertiesAware.class);
                        }
                    });
                    return xstream.toXML(transformedBean);
                }






                // If there is a scope then always use, there might be a child object in the hierarchy
                // that knows about the scope.  We have change to the make sure getFieldsForScope is not
                // null anyway because of parent object containing a collection of objects that need to
                // be filtered.  The parent and collection object does not have configurations for the
                // scope.
                JsonConfig newConfig = jsonConfig.copy();
                newConfig.setJsonPropertyFilter(new PropertyFilter() {
                    @Override
                    public boolean apply(final Object obj, final String name, final Object value) {
                        String[] objFields = BeanConverter.getFieldsForScope(obj.getClass(), scope);
                        if (objFields == null) {
                            logger.warn("Unable to find scope :=" + scope + " for object:=" + obj.getClass() +
                                    " using scope='default'");
                            objFields = BeanConverter.getFieldsForScope(obj.getClass(), "default");
                        }
                        return (objFields != null) ? ArrayUtils.indexOf(objFields, name) == -1 : false;
                    }
                });
                return JSONSerializer.toJSON(bean, newConfig);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "{error:'Unable to convert to JSON value := " + e.getMessage() + "'}";
        }
    }

    @Override
    protected Object transformResultCollection(final Collection<Object> collection) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(collection);
        return jsonArray;
    }

    public static class JsonDateProcessor extends AbstractValueProcessor {

        @Override
        public Object processObjectValue(String s, Object o, JsonConfig jsonConfig) {
            if (o instanceof Date) {
                return ((Date) o).getTime();
            }
            return null;
        }
    }

    @Deprecated
    public static class JsonHtmlEscapeProcessor extends AbstractValueProcessor {

        @Override
        public Object processObjectValue(String s, Object o, JsonConfig jsonConfig) {
            if (o instanceof String) {

                String result = (String) o;

                char[] orgChars = result.toCharArray();
                int writeIndex = 0;
                StringBuilder sb = new StringBuilder(orgChars.length * 2);
                int charLen = orgChars.length;
                char ch;
                for (int i = 0; i < charLen; i++) {
                    ch = orgChars[i];
                    if (ch == '\'') {
                        sb.append('\\');
                    }
                    sb.append(ch);
                }
                result = sb.toString();
                return result;
            }
            return null;
        }
    }

    public abstract static class AbstractValueProcessor implements JsonValueProcessor {

        @Override
        public Object processArrayValue(Object o, JsonConfig jsonConfig) {
            if (JSONUtils.isArray(o)) {
                Object[] arr = (Object[]) o;
                if (arr.length > 0) {
                    Object[] result = new Object[arr.length];
                    for (int i = 0; i < arr.length; i++) {
                        result[i] = processObjectValue(null, arr[i], jsonConfig);
                    }
                    return result;
                }
            }
            return processObjectValue(null, o, jsonConfig);
        }
    }

}
