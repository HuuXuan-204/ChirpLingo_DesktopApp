module com.chirplingo {
    requires transitive javafx.controls;
    requires transitive javafx.base;
    requires java.sql;
    requires javafx.fxml;
    requires transitive java.net.http;
    requires transitive com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.annotation;
    requires io.github.cdimascio.dotenv.java;

    opens com.chirplingo to javafx.fxml;
    opens com.chirplingo.domain to javafx.fxml;
    exports com.chirplingo;
    exports com.chirplingo.domain.newspaper;
    exports com.chirplingo.domain;
    exports com.chirplingo.client.interfaces;
    exports com.chirplingo.client.base;
    exports com.chirplingo.practice.entities;
    exports com.chirplingo.utils;
}
