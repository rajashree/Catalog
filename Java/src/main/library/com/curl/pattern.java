package com.curl;

import org.apache.oro.text.*;
import org.apache.oro.text.regex.*;

public class pattern {
   
   public void parseLog() throws Exception {
	 String link="0 src=\"http://photos3.socializr.com/30/78/57/30785dd7163m11.jpg\">0 src=\"http://photos3.socializr.com/30/78/57/307857163m22.jpg\"> 0 src=\"http://photos3.socializr.com/30/78/57/3078571d63m33.jpg\"></a";
	   //  String link="src=\"http://photos7.socializr.com/71/52/02/715202998m.jpg\"";
       String regexpForLink="\\s*src=\"([^\"]+)\"";

        PatternCompiler compiler=new Perl5Compiler();
        Pattern pattern=compiler.compile(regexpForLink);

        PatternMatcher matcher=new Perl5Matcher();
        if (matcher.contains(link,pattern)) {
            MatchResult result=matcher.getMatch();
            System.out.println(result.group(1));
            System.out.println(result.group(2));
            
        }
        
    }
   /* public void parseHTML() throws Exception {
        String html="<font face=\"Arial, Serif\" size=\"+1\"color=\"red\">";

        String regexpForFontTag="<\\s*font\\s+([^>]*)\\s*>";
        String regexpForFontAttrib="([a-z]+)\\s*=\\s*\"([^\"]+)\"";

        PatternCompiler compiler=new Perl5Compiler();
        Pattern patternForFontTag=compiler.compile(regexpForFontTag,Perl5Compiler.CASE_INSENSITIVE_MASK);
        Pattern patternForFontAttrib=compiler.compile(regexpForFontAttrib,Perl5Compiler.CASE_INSENSITIVE_MASK);

        PatternMatcher matcher=new Perl5Matcher();
        if (matcher.contains(html,patternForFontTag)) {
            MatchResult result=matcher.getMatch();
            String attrib=result.group(1);

            PatternMatcherInput input=new PatternMatcherInput(attrib);
            while (matcher.contains(input,patternForFontAttrib)) {
                result=matcher.getMatch();
                System.out.println(result.group(1)+": "+result.group(2));
            }
        }
    }*/
    public void substitutelink() throws Exception {
    	String link="0 src=\"http://photos3.socializr.com/30/78/57/30785dd7163m11.jpg\">0 src=\"http://photos3.socializr.com/30/78/57/307857163m22.jpg\"> 0 src=\"http://photos3.socializr.com/30/78/57/3078571d63m33.jpg\"></a";
    	String regexpForLink="\\s*src=\"([^\"]+)\"";

        PatternCompiler compiler;
        Pattern patternForLink;
        PatternMatcher matcher;
        MatchResult result;
        PatternMatcherInput input;
        
        compiler=new Perl5Compiler();
        patternForLink=compiler.compile(regexpForLink,Perl5Compiler.CASE_INSENSITIVE_MASK);
        matcher=new Perl5Matcher();
        input   = new PatternMatcherInput(link);

        while(matcher.contains(input, patternForLink)) {
   	     result = matcher.getMatch();
   	     // Perform whatever processing on the result you want.
   	     System.out.println("Result"+result.toString());
   	   }
    }
    public static void main(String[] args) throws Exception {
    	pattern test=new pattern();
        System.out.println("\n\nLog Parsing Example");
        test.parseLog();
       // System.out.println("\n\nHtml Example 1");
       // test.parseHTML();
        System.out.println("\n\nHtml Example 2");
        test.substitutelink();
	}
}
