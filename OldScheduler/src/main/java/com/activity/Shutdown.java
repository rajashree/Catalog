package com.activity;

/** 
* ShutdownHooks .java
* To demonstrate registration and working or Shutdown hooks
*/
public class Shutdown implements Runnable
{
    //this is the shutdown hook thread
    public Shutdown(){}
    /*
     * run method of shutdown hook thread, to display message when started
     */
    public void run()
    {
	     System.out.println("*** Shutdown Hook Started ***");
        System.out.println("*** Application Shutting down ***");
    }
            
    /*
     * Main Method
     */
    public static void main(String[] arg) throws Exception
    {
        //Getting the runtime object associated with the current Java application.
        Runtime runTime = Runtime.getRuntime();
        
        //Initiating the shutdown hook
        Shutdown hook = new Shutdown();
        
        //Registering the Shutdown Hook
        runTime.addShutdownHook(new Thread(hook));
        while(true){
        System.out.println("---started---");
        Thread.sleep(10000);
        }
        
    }
}