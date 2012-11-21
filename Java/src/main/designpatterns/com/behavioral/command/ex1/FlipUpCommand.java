package com.behavioral.command.ex1;


/* The Command for turning the light on in North America, or turning the light off in most other places */
public class FlipUpCommand implements Command {
 
   private Light theLight;
 
   public FlipUpCommand(Light light) {
        this.theLight=light;
   }
 
   public void execute(){
      theLight.turnOn();
   }
}
