/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.controller.admin.devmode;

import com.sourcen.core.App;
import com.sourcen.core.web.controller.BaseController;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: mahalaxmi $
 * @version $Revision: 3196 $, $Date:: 2012-06-15 08:51:23#$
 */
@Controller
public class GroovyShellController extends BaseController {

    private static final Logger logger = LogManager.getLogger(GroovyShellController.class);

    @RequestMapping(value = "/admin/devmode/groovy-shell.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView execute(@RequestParam(required = false) String script) {
        ModelAndView mv = new ModelAndView();
        String result = "";
        String output = "";

        if (script == null) {
            script = getDefaultScript();
        } else {
            // execute the script.
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final PrintWriter printWriter
                    = new PrintWriter(new OutputStreamWriter(out));

            final PrintStream oldOut = System.out;
            final PrintStream oldErr = System.err;

            System.setOut(new PrintStream(out));
            System.setErr(new PrintStream(out));


            try {
                final CompilerConfiguration config = new CompilerConfiguration();
                config.setOutput(printWriter);
                config.setDebug(true);
                config.setVerbose(true);
                final GroovyShell shell = new GroovyShell(new Binding(), config);
                shell.setProperty("applicationContext", App.getContext());
                final Object scriptResult = shell.evaluate(script);
                if (scriptResult == null) {
                    result = "null";
                } else {
                    // build output.
                    Class resultClass = scriptResult.getClass();
                    if (ClassUtils.isPrimitiveOrWrapper(resultClass) || scriptResult instanceof String) {
                        result = String.valueOf(scriptResult);
                    } else if (scriptResult instanceof Iterable) {
                        StringBuilder sb = new StringBuilder();
                        Iterator iterator = ((Iterable) scriptResult).iterator();
                        while (iterator.hasNext()) {
                            sb.append(ToStringBuilder.reflectionToString(iterator.next(), ToStringStyle.MULTI_LINE_STYLE));
                            sb.append("\n");
                        }
                        result = sb.toString();

                    } else {
                        final StringBuilder sb = new StringBuilder(1000);
                        sb.append(resultClass.getCanonicalName());
                        sb.append("@" + Integer.toHexString(scriptResult.hashCode()));
                        sb.append("{\n");

                        sb.append("\tfields : {\n");
                        // get methods.
                        ReflectionUtils.doWithFields(resultClass, new ReflectionUtils.FieldCallback() {
                            @Override
                            public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
                                sb.append("\t\t" + field.getName() + " = " + FieldUtils.readField(scriptResult, field.getName(), true) + "\n");
                            }
                        });
                        sb.append("\t}\n");
                        sb.append("\tmethods : {\n");
                        // get methods.
                        ReflectionUtils.doWithMethods(resultClass, new ReflectionUtils.MethodCallback() {
                            @Override
                            public void doWith(final Method method) throws IllegalArgumentException, IllegalAccessException {
                                if (Modifier.isPublic(method.getModifiers()) && !Modifier.isNative(method.getModifiers())) {
                                    sb.append("\t\t" + method.toString() + "\n");
                                }
                            }
                        });
                        sb.append("\t}");


                        sb.append("\n}");
                        result = sb.toString();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(printWriter);
            } finally {
                System.setOut(oldOut);
                System.setOut(oldErr);
            }
            logger.info("trying to execute groovyScript:"
                    + "\n------------------SCRIPT------------------\n"
                    + script
                    + "\n------------------RESULT------------------\n"
                    + result);
            output = new String(out.toByteArray());
        }
        mv.addObject("script", script);
        mv.addObject("output", output);
        mv.addObject("result", result);
        return mv;
    }

    // initialize once plz :)
    protected static String defaultScript = "import com.sourcen.core.*\n"
            + "import com.sourcen.core.cache.*\n"
            + "import com.sourcen.core.config.*\n"
            + "import com.sourcen.core.db.*\n"
            + "import com.sourcen.core.events.*\n"
            + "import com.sourcen.core.util.*\n"
            + "\n"
            + "import com.sourcen.db.*\n"
            + "import com.sourcen.managers.*\n"
            + "\n\n"
            + "\n\n"
            + "\n\n// clear all variables..."
            + "purge variables\n";

    protected String getDefaultScript() {
        return defaultScript;
    }

}