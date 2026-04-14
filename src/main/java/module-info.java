module com.chirplingo {
    requires transitive javafx.controls;
    requires transitive javafx.base;
    requires java.sql;
    requires javafx.fxml;

    opens com.chirplingo to javafx.fxml;
    opens com.chirplingo.domain to javafx.fxml;
    exports com.chirplingo;
    exports com.chirplingo.domain;
    exports com.chirplingo.practice.entities;
    exports com.chirplingo.utils;
}
