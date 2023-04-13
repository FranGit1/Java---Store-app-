module com.example.marinic7 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.marinic7 to javafx.fxml;
    exports com.example.marinic7;
}