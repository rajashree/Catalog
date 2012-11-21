package com.behavioral.command.ex1;


/* The Command for turning the light off in North America, or turning the light on in most other places */
public class FlipDownCommand implements Command {
 
   private Light theLight;
 
   public FlipDownCommand(Light light) {
        this.theLight=light;
   }
 
   public void execute() {
      theLight.turnOff();
   }
}
