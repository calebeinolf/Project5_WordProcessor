package com.example.project5_wordprocessor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.util.*;

public class TextEdit {
    double WINDOW_WIDTH = 1000;
    double WINDOW_HEIGHT = 500;
    Pane contentPane;
    private final StringBuilder beforeCursor = new StringBuilder();
    private final StringBuilder blankBeforeCursor = new StringBuilder();
    private final StringBuilder afterCursor = new StringBuilder();
    private final StringBuilder blankAfterCursor = new StringBuilder();
    private final Text startText;
    private final Text content;
    private final Text cursorText;
    private final Deque<String> history;
    private final Deque<Character> deletedChars;
    private final Deque<Integer> eventIndex;
    private final List<String> fonts;
    private String currentFont;
    private boolean fontIsBold;

    /**
     * the TextEdit constructor creates the several text nodes and puts them into a ScrollPane,
     * which is added to the layout.
     * It also creates the stacks necessary for undo-ing.
     *
     * @param layout - the layout that the text's container will be added to
     */
    public TextEdit(BorderPane layout) {
        history = new LinkedList<>();
        deletedChars = new LinkedList<>();
        eventIndex = new LinkedList<>();
        fonts = new ArrayList<>(Arrays.asList("Courier New", "Consolas"));

        contentPane = new Pane();
        contentPane.setPadding(new Insets(15, 15, 15, 15));
        EventHandler<MouseEvent> handler = MouseEvent::consume;
        contentPane.addEventFilter(MouseEvent.ANY, handler);

        currentFont = "Consolas";
        fontIsBold = false;

        cursorText = new Text("|");
        cursorText.setFont(Font.font (currentFont, 18));
        cursorText.setTextAlignment(TextAlignment.LEFT);
        cursorText.setWrappingWidth(WINDOW_WIDTH - 100);
        cursorText.setY(25);
        cursorText.setX(15);

        double BLINK_INTERVAL = 500;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(BLINK_INTERVAL), event -> {
            if (contentPane.getChildren().contains(cursorText)) {
                contentPane.getChildren().remove(cursorText);
            } else {
                contentPane.getChildren().add(cursorText);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        startText = new Text();
        startText.setText(" Start typing...");
        startText.setFont(Font.font (currentFont, 18));
        startText.setFill(Color.LIGHTGRAY);
        startText.setTextAlignment(TextAlignment.LEFT);
        startText.setY(25);
        startText.setX(15);

        content = new Text();
        content.setFont(Font.font (currentFont, 18));
        content.setTextAlignment(TextAlignment.LEFT);
        content.setWrappingWidth(WINDOW_WIDTH - 100);
        content.setY(25);
        content.setX(15);

        //adds both Text nodes to the contentPane
        contentPane.getChildren().addAll(content, startText, cursorText);

        //adds the contentPane to a scrollable pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        scrollPane.setContent(contentPane);
        scrollPane.setFocusTraversable(false);

        //adds the scrollPane to the overall layout
        layout.setLeft(scrollPane);
    }

    /**
     * Used when saving the document, gets the text the user has typed
     * @return a string of what the user has typed
     */
    public String getText(){
        return beforeCursor + afterCursor.toString();
    }

    /**
     * displays the text
     */
    public void displayText() {
        if (beforeCursor.isEmpty() && afterCursor.isEmpty()){
            contentPane.getChildren().add(startText);
        } else {
            contentPane.getChildren().remove(startText);
        }
        content.setText(beforeCursor + " " + afterCursor);
        cursorText.setText(blankBeforeCursor + "|" + blankAfterCursor);

    }

    /**
     * adds the text to the StringBuilders
     * @param s - the text that is being added
     */
    public void addText(String s){
        if (!s.equals("\b")) {
            beforeCursor.append(s);
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i)==('\n')) {
                    blankBeforeCursor.append("\n");
                } else {
                    blankBeforeCursor.append(" ");
                }
                displayText();
            }
        }
        history.push(s);
        eventIndex.push(beforeCursor.length()-1);
    }

    /**
     * deletes the character right before the cursor
     * @param s - the backspace character
     */
    public void backspace(String s){
        if (!beforeCursor.isEmpty()) {
            deletedChars.push(beforeCursor.charAt(beforeCursor.length()-1));
            eventIndex.push(beforeCursor.length()-1);
            history.push(s);

            beforeCursor.deleteCharAt(beforeCursor.length() - 1);
            blankBeforeCursor.deleteCharAt(blankBeforeCursor.length() - 1);

            displayText();
        }
    }

    /**
     * moves the cursor one to the left
     */
    public void moveCursorLeft(){
        if (!beforeCursor.isEmpty()) {
            afterCursor.insert(0, beforeCursor.charAt(beforeCursor.length() - 1));
            blankAfterCursor.insert(0, blankBeforeCursor.charAt(blankBeforeCursor.length() - 1));
            beforeCursor.deleteCharAt(beforeCursor.length() - 1);
            blankBeforeCursor.deleteCharAt(blankBeforeCursor.length() - 1);
        }
        displayText();
    }

    /**
     * moves the cursor one to the right
     */
    public void moveCursorRight(){
        if (!afterCursor.isEmpty()) {
            beforeCursor.append(afterCursor.charAt(0));
            blankBeforeCursor.append(blankAfterCursor.charAt(0));
            afterCursor.deleteCharAt(0);
            blankAfterCursor.deleteCharAt(0);
        }
        displayText();
    }

    /**
     * this aids in finding the lengths of different parts of what the user has typed.
     * necessary for moveCursorUp and moveCursorDown.
     */
    static int nthLastIndexOf(int nth, String ch, String string) {
        if (nth <= 0) return string.length();
        return nthLastIndexOf(--nth, ch, string.substring(0, string.lastIndexOf(ch)));
    }

    /**
     * moves the cursor up, putting it in the same position but on the line above.
     * (unless the line above is shorter, then it goes to the end of the above line)
     */
    public void moveCursorUp(){
//        System.out.println(beforeCursor.toString());
        if (beforeCursor.toString().contains("\n")){
            int lastLineLength = beforeCursor.toString().length()-beforeCursor.toString().lastIndexOf("\n")-1;
            int secondToLastLineLength = -1+beforeCursor.length()-(beforeCursor.substring(0, nthLastIndexOf(1,"\n",beforeCursor.toString())).lastIndexOf("\n"))-lastLineLength;
            int amtToRemove;
            if (secondToLastLineLength>lastLineLength){
                amtToRemove = secondToLastLineLength;
            } else if (secondToLastLineLength==lastLineLength) {
                amtToRemove = secondToLastLineLength + 1;
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

    /**
     * moves the cursor down, putting it in the same position but on the line below.
     * (unless the line above is shorter, then it goes to the end of the below line)
     */
    public void moveCursorDown(){
        if (afterCursor.toString().contains("\n")){
            int nextLineLength = afterCursor.indexOf("\n", afterCursor.indexOf("\n") + 1) - (afterCursor.indexOf("\n")) - 1;
            if (nextLineLength<0) {
                nextLineLength = afterCursor.length() - (afterCursor.indexOf("\n")) - 1;
            }

            int beforeCursorLastLineLength = beforeCursor.toString().length() - beforeCursor.toString().lastIndexOf("\n")-1;
            int currentLineLength = beforeCursorLastLineLength + (afterCursor.indexOf("\n"));
            int amtToMove;

            if (beforeCursorLastLineLength<nextLineLength || nextLineLength>currentLineLength){
                amtToMove = currentLineLength + 1;
            } else {
                amtToMove = afterCursor.indexOf("\n") + nextLineLength + 1;
            }

            for (int i = 0; i < amtToMove; i++){
                moveCursorRight();
            }

        } else {
            int length = afterCursor.length();
            for (int i = 0; i < length; i++){
                moveCursorRight();
            }
        }
    }

    /**
     * undo's the last action.
     * if a character was just deleted, it puts it back.
     * if a character was just added, it removes it.
     */
    public void undo(){
        if (!history.isEmpty() && !eventIndex.isEmpty()){
                if (history.peek().equals("\b")) {
                    beforeCursor.insert((int) eventIndex.peek(), deletedChars.peek());
                    if (deletedChars.peek()=='\n'){
                        blankBeforeCursor.insert(eventIndex.peek(), "\n");
                    } else{
                        blankBeforeCursor.insert(eventIndex.peek(), " ");
                    }
                    eventIndex.pop();
                    deletedChars.pop();
                    history.pop();
                } else {
                    if (eventIndex.peek() < beforeCursor.length()) {
                        beforeCursor.deleteCharAt(eventIndex.peek());
                        blankBeforeCursor.deleteCharAt(eventIndex.peek());
                    } else {
                        afterCursor.deleteCharAt(eventIndex.peek() - beforeCursor.length());
                        blankAfterCursor.deleteCharAt(eventIndex.peek() - blankBeforeCursor.length());
                    }
                    eventIndex.pop();
                    history.pop();
                }
                displayText();
        }
    }

    /**
     * clears the stacks for when the file is reset (either cleared or a new file opened)
     */
    public void clearStacks(){
        history.clear();
        deletedChars.clear();
        eventIndex.clear();
    }

    /**
     * removes all text from the StringBuilders.
     * All the user's hard work... gone in an instant!
     * Useful when opening a new file
     */
    public void clearText(){
        if (!beforeCursor.isEmpty()) {
            beforeCursor.delete(0, beforeCursor.length());
            blankBeforeCursor.delete(0, blankBeforeCursor.length());
            afterCursor.delete(0, afterCursor.length());
            blankAfterCursor.delete(0, blankAfterCursor.length());
            displayText();
        }
    }

    /**
     *changes the font weight to bold
     */
    public void changeBold(){
        if (!fontIsBold) {
            content.setFont(Font.font(currentFont, FontWeight.BOLD, 18));
            cursorText.setFont(Font.font(currentFont, FontWeight.BOLD, 18));
            fontIsBold = true;
        } else {
            content.setFont(Font.font(currentFont, FontWeight.NORMAL, 18));
            fontIsBold = false;
        }
    }

    /**
     * changes the font to another font in the list
     */
    public void changeFont(){
        System.out.println(currentFont + "   " + fonts.get(0));
        System.out.println(currentFont.equals(fonts.get(0)));
        if (currentFont.equals("Consolas")){
            currentFont = fonts.get(0);
        } else {
            currentFont = fonts.get(1);
        }
        if (fontIsBold) {
            content.setFont(Font.font(currentFont, FontWeight.BOLD, 18));
            cursorText.setFont(Font.font(currentFont, FontWeight.BOLD, 18));
        } else {
            content.setFont(Font.font(currentFont, FontWeight.NORMAL, 18));
            cursorText.setFont(Font.font(currentFont, FontWeight.NORMAL, 18));
        }

    }
}
