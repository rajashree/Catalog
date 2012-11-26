package com.curl;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class PatternMatchUtil {
	 public String[] srcPattern(String link) throws Exception {
	    	String regexpForLink="\\s*src=\"([^\"]+[m.jpg])+\"";
	    	String images[];
	    	int size=0;
	    	int i=0;
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
	   	     size++;
	   	     
	   	   }
	       images=new String[size];
	       input   = new PatternMatcherInput(link);
	       while(matcher.contains(input, patternForLink)) {
		   	   result = matcher.getMatch();
		   	   String image=result.group(1).toString();
		   	   images[i++]=image;
		   	     
		   }
	       return images;
	       
	}
	 public String[] friendsrcPattern(String link) throws Exception {
	    	String regexpForLink="\\s*src=\"([^\"]+(m.jpg))+\"";
	    	String images[];
	    	int size=0;
	    	int i=0;
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
	   	     size++;
	   	     
	   	   }
	       images=new String[size];
	       input   = new PatternMatcherInput(link);
	       while(matcher.contains(input, patternForLink)) {
		   	   result = matcher.getMatch();
		   	   String image=result.group(1).toString();
		   	   images[i++]=image;
		   	     
		   }
	       return images;
	       
	}
	 
	 
	 public String beboMemberID(String link) throws Exception {
	    	String regexpForLink="MemberId=([^&]+)&";
	    	int size=0;String image = "";
	    	int i=0;
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
	   	     size++;
	   	     
	   	   }
	      
	       input   = new PatternMatcherInput(link);
	       while(matcher.contains(input, patternForLink)) {
		   	   result = matcher.getMatch();
		   	   image=result.group(1).toString();
		   	   
		   	     
		   }
	       
		return image;
	       
	}
	 public String[] beboAlbumIds(String link) throws Exception {
		 	String regexpForLink="RedirectCode=moo&PhotoAlbumId=([^>Print]+)>Print";
	    	String images[];
	    	int size=0;
	    	int i=0;
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
	   	     size++;
	   	     
	   	   }
	       images=new String[size];
	       input   = new PatternMatcherInput(link);
	       while(matcher.contains(input, patternForLink)) {
		   	   result = matcher.getMatch();
		   	   String image=result.group(1).toString();
		   	   images[i++]=image;
		   	     
		   }
	       return images;
	       
	}
	 public String beboAlbumName(String link) throws Exception {
		 	String regexpForLink="&PhotoAlbumName=([^>]+)>";
	    	String image="";
	    	int size=0;
	    	int i=0;
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
	   	     size++;
	   	     
	   	   }
	       
	       input   = new PatternMatcherInput(link);
	       while(matcher.contains(input, patternForLink)) {
		   	   result = matcher.getMatch();
		   	   image=result.group(1).toString();
		   	    
		   }
	       return image;
	       
	}
	 public String[] beboImages(String link) throws Exception {
		 
		 	String regexpForLink="img\\s+src=http://i([^h]+)h";
	    	String images[];
	    	int size=0;
	    	int i=0;
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
	   	     size++;
	   	     
	   	   }
	       images=new String[size];
	       input   = new PatternMatcherInput(link);
	       while(matcher.contains(input, patternForLink)) {
		   	   result = matcher.getMatch();
		   	   String image=result.group(1).toString();
		   	   images[i++]=image;
		   	     
		   }
	       return images;
	       
	}
}
