package com.java.usorted;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringReplace {
	static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
    
        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e+pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

public static void main(String s[]){
	String str= "/portal/site/salesreps/template.PAGE/forums/resolver.vcarp/?javax.portlet.ctx_vca=topicparam%3Dture%26apptypes%3Dforum%26appmodes%3Dtopic%26forum%3D1.21.901%26post%3D1.24.1541&javax.portlet.tpst=3d6657a5539afca7e835e51069008a0c&javax.portlet.begCacheTok=com.vignette.cachetoken&javax.portlet.endCacheTok=com.vignette.cachetoken ";
	//System.out.println(str);
	 
     System.out.println(replace(str,"/site/*/","/site/xxx/"));
     // p s r p s r
 }
}
