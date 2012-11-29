package com.java.usorted;

public class Looping
{
	 public static int loopingCounter = 0;
	 static int initialValue = 0;
	 static int finalValue = 0;
    public Looping()
    {
    }

    public String incrementCounter(String initialValue, String finalValue1)
    {
        loopingCounter++;
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("@@@@@@COUNTER IS " + loopingCounter + "  @@@@@@@@@@@@@");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        Looping _tmp = this;
        finalValue = Integer.parseInt(finalValue1);
      
        String status = getStatus();
        return status;
    }

    public int getLoopCounter()
    {
        return loopingCounter;
    }

    public String getStatus()
    {
        if(loopingCounter < finalValue && loopingCounter != finalValue)
        {
           // return "<loop><initialValue>" + loopingCounter + "</initialValue><finalValue>" + finalValue + "</finalValue><status>continue</status></loop>";
        	return "<loop><status>Continue</loop></status>";
        } else
        {
            int lpCouinter = loopingCounter;
            loopingCounter = 0;
            //return "<loop><initialValue>" + lpCouinter + "</initialValue><finalValue>" + finalValue + "</finalValue><status>stop</status></loop>";
            return "<loop><status>stop</status></loop>";
        }
    }

    public int getFinalValue()
    {
        return finalValue;
    }

    public static void main(String arg[])
    {
        Looping lp = new Looping();
      //  Looping lp2 = new Looping();
        System.out.println("looping counter" + lp.incrementCounter("0", "5"));
        System.out.println("looping counter" + lp.incrementCounter("0", "5"));
        System.out.println("looping counter" + lp.incrementCounter("0", "5"));
        System.out.println("looping counter" + lp.incrementCounter("0", "5"));
        System.out.println("looping counter" + lp.incrementCounter("0", "5"));
     
    }

   
}
