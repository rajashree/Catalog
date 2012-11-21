package com.structural.decorator.ex1;
//the first concrete decorator which adds vertical scrollbar functionality
class VerticalScrollBarDecorator extends WindowDecorator {
    public VerticalScrollBarDecorator (Window decoratedWindow) {
        super(decoratedWindow);
    }
 
    public void draw() {
        drawVerticalScrollBar();
        super.draw();
    }
 
    private void drawVerticalScrollBar() {
        // draw the vertical scrollbar
    }
 
    public String getDescription() {
        return decoratedWindow.getDescription() + ", including vertical scrollbars";
    }
}
