package com.java.usorted;

import java.util.regex.*;


public class PatternMatching {
    public static void main(String a[]){
        Pattern pp = Pattern.compile("[[a-z A-Z 0-9]/w,;@$*_.(){}/]+");
        Matcher mm = pp.matcher("/");//'-fsdf�����
        boolean bb  = mm.matches();
        if(bb)
            System.out.println("correct "+bb);
        else
            System.out.println("mistake "+bb);



        //Diff Ex
        String s = "uk-india-ireland-srilanka";
        String[] arr = s.split("-");
        System.out.println(arr.length);
    }
}
