package com.example.project5_wordprocessor;

import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;

public class Main extends Application {

    TextEdit text;

    public HBox addTopMenu() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 15, 15, 15));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #095996;");

        Button buttonSave = new Button("Save");
        buttonSave.setPrefSize(70, 20);
        buttonSave.setFocusTraversable(false);
        buttonSave.setOnAction(event -> {
            //TODO: Make this save the doc
        });
        Button buttonUndo = new Button("Undo");
        buttonUndo.setPrefSize(70, 20);
        buttonUndo.setFocusTraversable(false);
        buttonUndo.setOnAction(event -> {
            //TODO: Make this undo
        });

        hbox.getChildren().addAll(buttonSave, buttonUndo);

        return hbox;
    }

    /**
     * Set up the starting scene of your application given the primaryStage (basically the window)
     *
     * @param primaryStage the primary container for scenes
     */
    // https://docs.oracle.com/javase/8/javafx/api/index.html
    @Override
    public void start(Stage primaryStage) {
        // Add a title to the application window
        primaryStage.setTitle("Word Processor");


        BorderPane border = new BorderPane();
        double WINDOW_WIDTH = 1000;
        double WINDOW_HEIGHT = 500;
        Scene exampleScene = new Scene(border, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(exampleScene);

        //adds the menu at the top that holds the buttons
        HBox topMenu = addTopMenu();
        border.setTop(topMenu);

        text = new TextEdit(border);

        // display the interface
        primaryStage.show();


        //BUTTON PRESSES
        exampleScene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                System.exit(0);
            } else if (event.getCode().equals(KeyCode.BACK_SPACE)){
                text.backspace();
            } else if (event.getCode().equals(KeyCode.LEFT)){
                text.moveCursorLeft();
            } else if (event.getCode().equals(KeyCode.RIGHT)){
                text.moveCursorRight();
            }
        });

        exampleScene.setOnKeyTyped(event -> {
            text.addText(event.getCharacter());
        });

    }

    public static void main(String[] args) {
        launch();
    }
}