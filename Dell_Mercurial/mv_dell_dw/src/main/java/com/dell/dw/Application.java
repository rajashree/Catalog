package com.dell.dw;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/3/12
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Application {
    public static void main(String []args) {
        String configFile = args.length > 0?args[0]:"spring/applicationContext.xml";
        new ClassPathXmlApplicationContext(configFile);
    }
}
