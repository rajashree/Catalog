package com.structural.decorator.ex1;

//the second concrete decorator which adds horizontal scrollbar functionality
class HorizontalScrollBarDecorator extends WindowDecorator {
    public HorizontalScrollBarDecorator (Window decoratedWindow) {
        super(decoratedWindow);
    }
 
    public void draw() {
        drawHorizontalScrollBar();
        super.draw();
    }
 
    private void drawHorizontalScrollBar() {
        // draw the horizontal scrollbar
    }
 
    public String getDescription() {
        return decoratedWindow.getDescription() + ", including horizontal scrollbars";
    }
}
