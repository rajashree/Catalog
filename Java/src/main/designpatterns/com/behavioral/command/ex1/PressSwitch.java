package com.behavioral.command.ex1;


/* The test class or client */
public class PressSwitch {
 
   public static void main(String[] args) {
       Light lamp = new Light();
       Command switchUp = new FlipUpCommand(lamp);
       Command switchDown = new FlipDownCommand(lamp);
 
       // See criticism of this model above:
       // The switch itself should not be aware of lamp details (switchUp, switchDown) 
       // either directly or indirectly
       Switch s = new Switch(switchUp,switchDown);
 
       try {
           if (args[0].equalsIgnoreCase("ON")) {
                s.flipUp();
           } else if (args[0].equalsIgnoreCase("OFF")) {
               s.flipDown();
           } else {
               System.out.println("Argument \"ON\" or \"OFF\" is required.");
           }
       } catch (Exception e){
           System.out.println("Arguments required.");
       }
   }
}
