package com.example.project5_wordprocessor;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import javafx.scene.input.KeyCode;

public class Main extends Application {
    private final StringBuilder text = new StringBuilder();

    /**
     * Set up the starting scene of your application given the primaryStage (basically the window)
     * https://docs.oracle.com/javase/8/javafx/api/index.html
     *
     * @param primaryStage the primary container for scenes
     */
    @Override
    public void start(Stage primaryStage) {
        // Add a title to the application window
        primaryStage.setTitle("Hello World!");

        // prepare the scene layout to use a BorderPane -- a top, bottom, left, right, center style pane layout
        // https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
        BorderPane layout = new BorderPane();

        // Create a new scene using this layout
        // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html
        // define the size of this scene
        double WINDOW_WIDTH = 1000;
        double WINDOW_HEIGHT = 500;
        Scene exampleScene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);

        // make this scene the initial (and for now only) scene in your application
        primaryStage.setScene(exampleScene);

        // create a new text node to display text on the interface
        // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/text/Text.html
        Text content = new Text();
        content.setTextAlignment(TextAlignment.LEFT);
        content.setWrappingWidth(WINDOW_WIDTH-100);
        // add this text field to the layout
        layout.setCenter(content);

        // create a new button object and set its text
        Button btn = new Button("Say 'Hello World'");
        // define the code that should run when the button is clicked
        btn.setOnAction(event -> {
            text.append("Hello World! ");
            content.setText(text.toString());
        });
        // add this button to the layout centered at the bottom with some spacing from other elements
        BorderPane.setAlignment(btn, Pos.CENTER);
        BorderPane.setMargin(btn, new Insets(16, 16, 16, 16));
        layout.setBottom(btn);

        // define code to run every time a KeyPressed event is detected on this window to check for ESC to close
        // NOTE: there even is of type javafx.scene.input.KeyEvent
        // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/input/KeyEvent.html
        exampleScene.setOnKeyPressed(event -> {
            // check if the key that was pressed is the ESC key
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                // exit the program
                System.exit(0);
            } else if (event.getCode().equals(KeyCode.BACK_SPACE)){
                text.deleteCharAt(text.length()-1);
                //content.setText(text.toString());
            }
        });

        // define code to run every time a KeyTyped event is detected on this window to check for ESC to close
        // NOTE: there even is of type javafx.scene.input.KeyEvent
        // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/input/KeyEvent.html
        exampleScene.setOnKeyTyped(event -> {
            // TODO: add whatever the typed character is to the text on this page
            // NOTE: the typed String can be retrieved with event.getCharacter()
            text.append(event.getCharacter());
            content.setText(text.toString());
        });

        // display the interface
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}