package com.example.project5_wordprocessor;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Scanner;


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
        content.setFont(Font.font ("Monospace", 18));
        content.setTextAlignment(TextAlignment.LEFT);
        content.setWrappingWidth(WINDOW_WIDTH - 100);
        // add this text field to the layout
        layout.setLeft(content);
    }

    public String getText(){
        return beforeCursor.toString() + afterCursor.toString();
    }

    public void displayText() {
        content.setText(beforeCursor.toString() + "|" + afterCursor.toString());

//        content.setText(beforeCursor.toString() + afterCursor.toString());
//        addCursor(pass thru the coordinates at which the beforeCursor string ends, put cursor there)
    }

    public void addText(String s){
        if (!s.equals("\b")) {
            beforeCursor.append(s);
            displayText();
        }
//        System.out.println(beforeCursor.toString() + "|" + afterCursor.toString());
    }

    public void backspace(){
        if (!beforeCursor.isEmpty()) {
            beforeCursor.deleteCharAt(beforeCursor.length() - 1);
            content.setText(beforeCursor.toString());
        }
    }

    public void moveCursorLeft(){
        if (!beforeCursor.isEmpty()) {
            afterCursor.insert(0, beforeCursor.charAt(beforeCursor.length() - 1));
            beforeCursor.deleteCharAt(beforeCursor.length() - 1);
        }
        displayText();
    }

    public void moveCursorRight(){
        if (!afterCursor.isEmpty()) {
            beforeCursor.append(afterCursor.charAt(0));
            afterCursor.deleteCharAt(0);
        }
        displayText();
    }

    static int nthLastIndexOf(int nth, String ch, String string) {
        if (nth <= 0) return string.length();
        return nthLastIndexOf(--nth, ch, string.substring(0, string.lastIndexOf(ch)));
    }

    public void moveCursorUp(){ // NOT WORKING RIGHT

//        System.out.println(beforeCursor.toString());

        if (beforeCursor.toString().contains("\n")){
            int lastLineLength = beforeCursor.toString().length()-beforeCursor.toString().lastIndexOf("\n")-1;
            int secondToLastLineLength = -1+beforeCursor.length()-(beforeCursor.substring(0, nthLastIndexOf(1,"\n",beforeCursor.toString())).lastIndexOf("\n"))-lastLineLength;
            int amtToRemove;
            if (secondToLastLineLength>=lastLineLength){
                amtToRemove = secondToLastLineLength;
            } else {
                amtToRemove = lastLineLength + 1;
            }

            //move cursor left 'amtToRemove' times
            for (int i=0; i < amtToRemove; i++){
                moveCursorLeft();
            }


        } else {
            System.out.println("oh no");
        }
        //if the beforeCursor has a line break:
            //if last line length < 2nd to last line length:
                //last line length + (2nd to last line length - last line length)
            //else
                //remove last line from beforeCursor and add it to afterCursor

        // else:
            //move cursor to beginning of beforeCursor
    }

    public void moveCursorDown(){
        //if the beforeCursor has a line break:
            //get num characters in afterCursor before the first line break
            //move cursor up that many characters in the below line
        // else:
            //move cursor to end of afterCursor
    }
}
