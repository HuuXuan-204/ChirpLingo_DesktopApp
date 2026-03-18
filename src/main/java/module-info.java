module com.chirplingo {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.chirplingo to javafx.fxml;
    exports com.chirplingo;
}
