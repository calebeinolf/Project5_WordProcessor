package com.example.project5_wordprocessor;

import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class TextEdit {
    double WINDOW_WIDTH = 1000;
    double WINDOW_HEIGHT = 500;

    private StringBuilder beforeCursor = new StringBuilder();
    private StringBuilder afterCursor = new StringBuilder();

    private Text content;

    // create a new text node to display text on the interface
    // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/text/Text.html
    public TextEdit(BorderPane layout) {
        content = new Text();
        content.setTextAlignment(TextAlignment.LEFT);
        content.setWrappingWidth(WINDOW_WIDTH - 100);
        // add this text field to the layout
        layout.setCenter(content);
    }

    public void addText(String s){
        if (!s.equals("\b")) {
            beforeCursor.append(s);
            content.setText(beforeCursor.toString());
            System.out.println(s);
        }
    }

    public void backspace(){
        if (!beforeCursor.isEmpty()) {
            beforeCursor.deleteCharAt(beforeCursor.length() - 1);
            content.setText(beforeCursor.toString());
        }
    }
}