/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.ws;

import com.sourcen.core.web.controller.BaseController;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.servlet.ModelAndView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3553 $, $Date:: 2012-06-22 15:45:27#$
 */
@Controller
public abstract class APIList extends BaseController {

    private Collection<APIService> apiServices = new CopyOnWriteArraySet<APIService>();

    protected APIList(Collection<Class<? extends WebService>> classes) {
        reloadAPIList(classes);
        // scan through all API methods.
    }

    protected APIList() {
    }

    public ModelAndView get_list() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("apiServices", apiServices);
        return mv;
    }

    protected void reloadAPIList(Collection<Class<? extends WebService>> classes) {
        apiServices.clear();
        for (Class clazz : classes) {
            RequestMapping apiRequestMapping = AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
            Assert.notNull(apiRequestMapping,
                    clazz + " must have  @RequestMapping annotation to be exposed as a WebService endpoint");

            Assert.isTrue(apiRequestMapping.value().length == 1,
                    clazz + " must have  @RequestMapping with atMost 1 endpoint, but we found" + apiRequestMapping.value().length);

            final String apiEndPoint = apiRequestMapping.value()[0];
            Assert.isTrue(!apiEndPoint.contains("*"),
                    "APIList does not handle endpoints that contain *, we found " + apiEndPoint);

            final Collection<APIMethod> methods = new ArrayList<APIMethod>();
            final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();


            ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
                @Override
                public void doWith(final Method method) throws IllegalArgumentException, IllegalAccessException {
                    RequestMapping methodRequestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
                    if (methodRequestMapping != null) {

                        Assert.isTrue(methodRequestMapping.value().length == 1,
                                method + " must have  @RequestMapping with atMost 1 endpoint, but we found" + methodRequestMapping
                                        .value().length);

                        String methodEndPoint = apiEndPoint + "/" + methodRequestMapping.value()[0];

                        Assert.isTrue(!methodEndPoint.contains("*"),
                                "APIList does not handle endpoints that contain *, we found " + methodEndPoint);

                        Collection<APIParameter> parameters = new ArrayList<APIParameter>();

                        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

                        for (int i = 0; i < parameterAnnotations.length; i++) {
                            Annotation[] annotations = parameterAnnotations[i];
                            String paramName = parameterNames[i];
                            String paramType = null;
                            if (ClassUtils.isPrimitiveOrWrapper(parameterTypes[i]) || parameterTypes[i].equals(String.class)) {
                                paramType = parameterTypes[i].getSimpleName();
                            } else {
                                paramType = parameterTypes[i].getName();
                            }
                            String defaultValue = "";
                            Boolean required = false;


                            if (annotations.length > 0) {
                                for (Annotation annotation : annotations) {
                                    if (annotation instanceof RequestParam) {
                                        RequestParam requestParamAnnotation = (RequestParam) annotation;
                                        if (requestParamAnnotation.value() != null && !requestParamAnnotation.value()
                                                .isEmpty()) {
                                            paramName = requestParamAnnotation.value();
                                        }
                                        if (!requestParamAnnotation.defaultValue()
                                                .equals(ValueConstants.DEFAULT_NONE)) {
                                            defaultValue = requestParamAnnotation.defaultValue();
                                        }
                                        required = requestParamAnnotation.required();
                                    }
                                }
                            }

                            parameters.add(new APIParameter(paramName, paramType, required, defaultValue));

                        }
                        methods.add(new APIMethod(method.getName(), methodEndPoint, parameters));
                    }
                }
            });
            javax.jws.WebService wsAnnotation = AnnotationUtils.findAnnotation(clazz, javax.jws.WebService.class);
            String serviceName = null;
            if (wsAnnotation != null && !wsAnnotation.name().isEmpty()) {
                serviceName = wsAnnotation.name();
            } else {
                serviceName = clazz.getSimpleName();
                int implIndex = serviceName.indexOf("Impl");
                if (implIndex > -1) {
                    serviceName = serviceName.substring(0, implIndex);
                }
            }
            APIService service = new APIService(serviceName, apiEndPoint, methods);
            apiServices.add(service);
        }
    }

    public static class APIService {

        private String serviceName;

        private String endpointUrl;

        private final Collection<APIMethod> methods = new TreeSet<APIMethod>(new Comparator<APIMethod>() {
            @Override
            public int compare(final APIMethod o1, final APIMethod o2) {
                return o1.getMethodName().compareTo(o2.getMethodName());
            }
        });

        public APIService(final String serviceName, final String endpointUrl, final Collection<APIMethod> methods) {
            this.serviceName = serviceName;
            this.endpointUrl = endpointUrl;
            this.methods.addAll(methods);
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getEndpointUrl() {
            return endpointUrl;
        }

        public Collection<APIMethod> getMethods() {
            return methods;
        }
    }

    public static class APIMethod {

        private String methodName;

        private String methodEndPoint;

        private final Collection<APIParameter> parameters; // does not need sorting.

        public APIMethod(final String methodName, final String methodEndPoint,
                         final Collection<APIParameter> parameters) {
            this.methodName = methodName;
            this.methodEndPoint = methodEndPoint;
            this.parameters = parameters;
        }

        public String getMethodName() {
            return methodName;
        }

        public String getMethodEndPoint() {
            return methodEndPoint;
        }

        public Collection<APIParameter> getParameters() {
            return parameters;
        }
    }

    public static class APIParameter {

        private String paramName;

        private String paramType;

        private Boolean required = false;

        private String defaultValue = "";

        public APIParameter() {
        }

        public APIParameter(final String paramName, final String paramType, final Boolean required,
                            final String defaultValue) {
            this.paramName = paramName;
            this.paramType = paramType;
            this.required = required;
            this.defaultValue = defaultValue;
        }

        public String getParamName() {
            return paramName;
        }

        public String getParamType() {
            return paramType;
        }

        public Boolean getRequired() {
            return required;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

    }

}
