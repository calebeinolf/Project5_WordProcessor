package com.example.project5_wordprocessor;

import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main extends Application {

    TextEdit text;

    public HBox addTopMenu(Stage stage) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 15, 15, 15));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #095996;");

        FileChooser fileChooser = new FileChooser();
        Button buttonSave = new Button("Save");
        buttonSave.setPrefSize(70, 20);
        buttonSave.setFocusTraversable(false);
        buttonSave.setOnAction(event -> {
            //Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            //Show save file dialog
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    saveTextToFile(text.getText(), file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Button buttonOpen = new Button("Open");
        buttonOpen.setPrefSize(70, 20);
        buttonOpen.setFocusTraversable(false);
        buttonOpen.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                openFile(file);
            }
        });

        Button buttonUndo = new Button("Undo");
        buttonUndo.setPrefSize(70, 20);
        buttonUndo.setFocusTraversable(false);
        buttonUndo.setOnAction(event -> {
            //TODO: Make this undo
        });

        hbox.getChildren().addAll(buttonSave, buttonOpen, buttonUndo);

        return hbox;
    }

    private void saveTextToFile(String content, File file) throws FileNotFoundException {
        PrintWriter writer;
        writer = new PrintWriter(file);
        writer.println(content);
        writer.close();
    }

    private void openFile(File file) {
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                text.addText(sc.nextLine());
                text.addText("\n");
            }
        } catch (IOException e){
            System.err.println("No such file");
        }
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
        HBox topMenu = addTopMenu(primaryStage);
        border.setTop(topMenu);

        text = new TextEdit(border);

        // display the interface
        primaryStage.show();


        //BUTTON PRESSES
        exampleScene.setOnKeyTyped(event -> {
            text.addText(event.getCharacter());
        });

        exampleScene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                System.exit(0);
            } else if (event.getCode().equals(KeyCode.BACK_SPACE)){
                text.backspace();
            } else if (event.getCode().equals(KeyCode.LEFT)){
                text.moveCursorLeft();
            } else if (event.getCode().equals(KeyCode.RIGHT)){
                text.moveCursorRight();
            } else if (event.getCode().equals(KeyCode.UP)){
                text.moveCursorUp();
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                text.moveCursorDown();
            } //else if (event.getCode().equals(KeyCode.ENTER)){
//                text.backspace();
//                text.addText("\n");
//                System.out.println("!!!");
//            }
        });


    }

    public static void main(String[] args) {
        launch();
    }
}