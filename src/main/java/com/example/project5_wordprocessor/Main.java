package com.example.project5_wordprocessor;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
import javax.swing.*;

public class Main extends Application {

    TextEdit text;
    double WINDOW_WIDTH = 1000;
    double WINDOW_HEIGHT = 500;

    public HBox addTopMenu(Stage stage) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 15, 15, 15));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #095996;");

        Button buttonSave = new Button("Save");
        buttonSave.setPrefSize(70, 20);
        buttonSave.setFocusTraversable(false);
        buttonSave.setOnAction(event -> {
            saveFile(stage);
        });

        Button buttonOpen = new Button("Open");
        buttonOpen.setPrefSize(70, 20);
        buttonOpen.setFocusTraversable(false);
        buttonOpen.setOnAction(event -> {
            openFile(stage);
        });

        Button buttonUndo = new Button("Undo");
        buttonUndo.setPrefSize(70, 20);
        buttonUndo.setFocusTraversable(false);
        buttonUndo.setOnAction(event -> {
            //TODO: Make this undo
            text.undo();
        });

        Button buttonFont = new Button("Font");
        buttonFont.setPrefSize(70,20);
        buttonFont.setFocusTraversable(false);

        buttonFont.setOnAction(actionEvent -> {
            JOptionPane.showMessageDialog(null,"Coming Soon!");
        });

        Button buttonBold = new Button("Bold");
        buttonBold.setPrefSize(70,20);
        buttonBold.setFocusTraversable(false);
        buttonBold.setOnAction(actionEvent -> {

            JOptionPane.showMessageDialog(null,"Coming Soon!");
        });

        hbox.getChildren().addAll(buttonSave, buttonOpen, buttonUndo, buttonFont, buttonBold);

        return hbox;
    }

    private void saveTextToFile(String content, File file) throws FileNotFoundException {
        PrintWriter writer;
        writer = new PrintWriter(file);
        writer.println(content);
        writer.close();
    }

    private void openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Scanner sc = new Scanner(file);
                StringBuilder sb = new StringBuilder();
                while (sc.hasNextLine()) {
                    sb.append(sc.nextLine());
                    sb.append("\n");
                }
                text.addText(sb.toString());
            } catch (IOException e) {
                System.err.println("No such file");
            }
            text.clearStacks();
            stage.setTitle(file.getName());
        }
    }

    public void saveFile(Stage stage){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                saveTextToFile(text.getText(), file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
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
        primaryStage.setTitle("Untitled.txt");


        BorderPane border = new BorderPane();
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
            if (!event.isControlDown() && (int) event.getCharacter().charAt(0) != 13 && (int) event.getCharacter().charAt(0) != 8){
                text.addText(event.getCharacter());
                int a = (int) event.getCharacter().charAt(0);
//                System.out.println(a);
            }
        });

        KeyCodeCombination ctrlZ = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN);
        KeyCodeCombination ctrlS = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
        KeyCodeCombination ctrlO = new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN);

        KeyCodeCombination ctrlLeft = new KeyCodeCombination(KeyCode.LEFT, KeyCodeCombination.CONTROL_DOWN);
        KeyCodeCombination ctrlRight = new KeyCodeCombination(KeyCode.RIGHT, KeyCodeCombination.CONTROL_DOWN);
        KeyCodeCombination ctrlUp = new KeyCodeCombination(KeyCode.UP, KeyCodeCombination.CONTROL_DOWN);
        KeyCodeCombination ctrlDown = new KeyCodeCombination(KeyCode.DOWN, KeyCodeCombination.CONTROL_DOWN);

        exampleScene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                System.exit(0);
            } else if (event.getCode().equals(KeyCode.BACK_SPACE)){
                text.backspace("\b");
            } else if (event.getCode().equals(KeyCode.LEFT)){
                text.moveCursorLeft();
            } else if (event.getCode().equals(KeyCode.RIGHT)){
                text.moveCursorRight();
            } else if (event.getCode().equals(KeyCode.UP)){
                text.moveCursorUp();
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                text.moveCursorDown();
            } else if (ctrlZ.match(event)){
                text.undo();
            } else if (ctrlS.match(event)){
                saveFile(primaryStage);
            } else if (ctrlO.match(event)){
                openFile(primaryStage);
            } else if (event.getCode().equals(KeyCode.ENTER)){
                text.addText("\n");
            }
        });


    }

    public static void main(String[] args) {
        launch();
    }
}