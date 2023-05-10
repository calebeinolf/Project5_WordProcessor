package com.example.project5_wordprocessor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Deque;
import java.util.LinkedList;

public class TextEdit {
    double WINDOW_WIDTH = 1000;
    double WINDOW_HEIGHT = 500;
    StackPane contentPane;
    private final StringBuilder beforeCursor = new StringBuilder();
    private final StringBuilder afterCursor = new StringBuilder();
    private final Text startText;
    private final Text content;
    private Deque<String> history;
    private Deque<Character> deletedChars;
    private Deque<Integer> eventIndex;

    // create a new text node to display text on the interface
    // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/text/Text.html
    public TextEdit(BorderPane layout) {
        history = new LinkedList<>();
        deletedChars = new LinkedList<>();
        eventIndex = new LinkedList<>();

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

        //adds both Text nodes to the contentPane
        contentPane.getChildren().addAll(content, startText);

        //adds the contentPane to a scrollable pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        scrollPane.setContent(contentPane);
        scrollPane.setFocusTraversable(false);

        //adds the scrollPane to the overall layout
        layout.setLeft(scrollPane);
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
        history.push(s);
        eventIndex.push(beforeCursor.length()-1);

//        System.out.println(history.toString());
//        System.out.println(eventIndex.toString());
    }

    public void backspace(String s){
        if (!beforeCursor.isEmpty()) {
            deletedChars.push(beforeCursor.charAt(beforeCursor.length()-1));
            eventIndex.push(beforeCursor.length()-1);
            history.push(s);

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

    public void moveCursorUp(){
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
            int length = beforeCursor.length();
            for (int i = 0; i < length; i++){
                moveCursorLeft();
            }
        }
    }

    public void moveCursorDown(){
        //if the beforeCursor has a line break:
            //get num characters in afterCursor before the first line break
            //move cursor up that many characters in the below line
        // else:
            //move cursor to end of afterCursor
//        if (afterCursor.toString().contains("\n")){
//            int beforeCursorLastLineLength = beforeCursor.toString().length()-beforeCursor.toString().lastIndexOf("\n")-1;
//            int currentLineLength = beforeCursorLastLineLength+(afterCursor.indexOf("\n"));
//
//            int amtToMove = currentLineLength + 1;
//
//            //move cursor right 'currentLineLength' times
//            for (int i=0; i < amtToMove; i++){
//                moveCursorRight();
//            }
//        } else {
//            int length = afterCursor.length();
//            for (int i = 0; i < length; i++){
//                moveCursorRight();
//            }
//        }
    }

    public void undo(){
        if (!history.isEmpty() && !eventIndex.isEmpty()){
//            System.out.println(eventIndex.peek());
//            System.out.println(beforeCursor.length()-1);
                if (history.peek().equals("\b")) {
                    beforeCursor.insert((int) eventIndex.poll(), deletedChars.poll());
                    history.pop();
                } else {
                    if (eventIndex.peek() < beforeCursor.length()) {
                        beforeCursor.deleteCharAt(eventIndex.poll());
                        history.pop();
                    } else {
                        afterCursor.deleteCharAt(eventIndex.poll() - beforeCursor.length());
                        history.pop();
                    }
                }
                displayText();
        }
    }

    public void clearStacks(){
        history.clear();
        deletedChars.clear();
        eventIndex.clear();
    }
}
