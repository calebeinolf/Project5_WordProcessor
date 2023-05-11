package com.example.project5_wordprocessor;

import javafx.application.Application;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
    double WINDOW_WIDTH = 1000;
    double WINDOW_HEIGHT = 500;
    TextEdit text;
    int numTimesBoldPressed = 0;

    /**
     * addTopMenu creates the blue and light blue banners at the top of the word processor and adds buttons to it
     * @param stage is the stage
     * @return VBox - the VBox holds the blue and light blue HBox's
     */
    public VBox addTopMenu(Stage stage) {
        VBox vbox = new VBox();

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 15, 15, 15));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #095996;");

        HBox hBoxLower = new HBox();
        hBoxLower.setPadding(new Insets(5, 15, 5, 15));
        hBoxLower.setSpacing(10);
        hBoxLower.setStyle("-fx-background-color: #cfdce6;");
        Text instructions = new Text("Tips:    Ctrl+S = Save,    Ctrl+O = Open (overwrites current file),    Ctrl+Z = Undo");
        instructions.setFont(Font.font (Font.getDefault().getName(), 12));
        hBoxLower.getChildren().add(instructions);

        Button buttonSave = new Button("Save");
        buttonSave.setPrefSize(70, 20);
        buttonSave.setFocusTraversable(false);
        buttonSave.setStyle("-fx-background-color: #cfdce6;");
        buttonSave.setOnAction(event -> saveFile(stage));

        Button buttonOpen = new Button("Open");
        buttonOpen.setPrefSize(70, 20);
        buttonOpen.setFocusTraversable(false);
        buttonOpen.setStyle("-fx-background-color: #cfdce6;");
        buttonOpen.setOnAction(event -> openFile(stage));

        Button buttonUndo = new Button("Undo");
        buttonUndo.setPrefSize(70, 20);
        buttonUndo.setFocusTraversable(false);
        buttonUndo.setStyle("-fx-background-color: #cfdce6;");
        buttonUndo.setOnAction(event -> text.undo());

        Button buttonFont = new Button("Change Font");
        buttonFont.setPrefSize(100,20);
        buttonFont.setFocusTraversable(false);
        buttonFont.setOnAction(event -> text.changeFont());

        Button buttonBold = new Button("Bold");
        buttonBold.setPrefSize(70,20);
        buttonBold.setFocusTraversable(false);
        buttonBold.setOnAction(event -> {
            text.changeBold();
            if (numTimesBoldPressed%2==0) {
                buttonBold.setText("Un-Bold");
            } else {
                buttonBold.setText("Bold");
            }
            numTimesBoldPressed++;
        });

        Button buttonClearAll = new Button(("Clear All"));
        buttonClearAll.setPrefSize(70, 20);
        buttonClearAll.setFocusTraversable(false);
        buttonClearAll.setOnAction(event -> text.clearText());

        hbox.getChildren().addAll(buttonSave, buttonOpen, buttonUndo, buttonFont, buttonBold, buttonClearAll);

        vbox.getChildren().addAll(hbox,hBoxLower);

        return vbox;
    }

    /**
     * saveTextToFile method makes a printwriter copy what the user has typed
     * into a file that can be saved to the computer
     * @param content receives what the user has written so far
     * @param file adds to the file specified by user
     * @throws FileNotFoundException - if file is not found
     */
    private void saveTextToFile(String content, File file) throws FileNotFoundException {
        PrintWriter writer;
        writer = new PrintWriter(file);
        writer.println(content);
        writer.close();
    }

    /**
     * saveFile method allows the user to use the save button found in the word processor.
     * It creates the system "save file" dialogue and calls "saveToFile" to copy the user's input
     * @param stage - the stage
     */
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
     * openFile allows the user to press Open on the word processor and brings up the window's file page.
     * Uses a scanner to scan through all the text and displays it on the word processor.
     * @param stage - the stage
     */
    private void openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            text.clearText();
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

    /**
     * Sets up the starting scene
     * @param primaryStage the primary container for scenes
     */
    // https://docs.oracle.com/javase/8/javafx/api/index.html
    @Override
    public void start(Stage primaryStage) {
        // Add a title to the application window
        primaryStage.setTitle("Untitled.txt");

        BorderPane border = new BorderPane();
        Scene scene = new Scene(border, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);

        //adds the menu at the top that holds the buttons
        VBox topMenu = addTopMenu(primaryStage);
        border.setTop(topMenu);

        text = new TextEdit(border);

        // display the interface
        primaryStage.show();

        //BUTTON PRESSES
        scene.setOnKeyTyped(event -> {
            if (!event.isControlDown() && (int) event.getCharacter().charAt(0) != 13 && (int) event.getCharacter().charAt(0) != 8){
                text.addText(event.getCharacter());
            }
        });

        KeyCodeCombination ctrlZ = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN);
        KeyCodeCombination ctrlS = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
        KeyCodeCombination ctrlO = new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN);

        scene.setOnKeyPressed(event -> {
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