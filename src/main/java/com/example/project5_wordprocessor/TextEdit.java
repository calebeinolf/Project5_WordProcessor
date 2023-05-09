package com.example.project5_wordprocessor;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Deque;

public class TextEdit {
    double WINDOW_WIDTH = 1000;
    StackPane contentPane;
    private final StringBuilder beforeCursor = new StringBuilder();
    private final StringBuilder afterCursor = new StringBuilder();
    private final Text startText;
    private final Text content;
    private Deque<Event> history;
    private Deque<Character> deletedChars;
    private Deque<Integer> eventIndex;

    // create a new text node to display text on the interface
    // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/text/Text.html
    public TextEdit(BorderPane layout) {
        contentPane = new StackPane();
        contentPane.setPadding(new Insets(15, 15, 15, 15));

        startText = new Text();
        startText.setText(" Start typing...");
        startText.setFont(Font.font ("Consolas", 18));
        startText.setFill(Color.LIGHTGRAY);
        startText.setTextAlignment(TextAlignment.LEFT);
        StackPane.setAlignment(startText, Pos.TOP_LEFT);

        content = new Text();
        content.setText("|");
        content.setFont(Font.font ("Consolas", 18));
        content.setTextAlignment(TextAlignment.LEFT);
        content.setWrappingWidth(WINDOW_WIDTH - 100);
        StackPane.setAlignment(content, Pos.TOP_LEFT);

        // add this text field to the layout
        contentPane.getChildren().addAll(content, startText);
        layout.setLeft(contentPane);
    }

    public String getText(){
        return beforeCursor + afterCursor.toString();
    }

    public void displayText() {
        if (beforeCursor.isEmpty() && afterCursor.isEmpty()){
            contentPane.getChildren().add(startText);
        } else {
            contentPane.getChildren().remove(startText);
        }
        content.setText(beforeCursor + "|" + afterCursor);

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
//            content.setText(beforeCursor.toString());
            displayText();
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

    public void undo(){

        // TODO: these things need to be added elsewhere:
        // make typing add the key event to the History stack (deque) AND
        // make it store the location of that character in the eventIndex stack
        // make backspace add that key event to the History stack AND:
        // make it store the character that was deleted in the deletedChars stack
        // make it store the location of that character in the eventIndex stack

        // TODO: make the undo method work:
        // if the last event in the History stack was "\b" (backspace):
        //      get the last deleted char from deletedChars,
        //      put it back at its index that was stored in the eventIndex stack
        // else:
        //      get the event's character (probably using event.getCharacter()) from the top of the History stack
        //      also get the index it was deleted from from the eventIndex stack
        //      find that character (in the combined String of beforeCursor and afterCursor) and remove it

    }
}
