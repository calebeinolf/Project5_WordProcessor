module com.example.project5_wordprocessor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.project5_wordprocessor to javafx.fxml;
    exports com.example.project5_wordprocessor;
}