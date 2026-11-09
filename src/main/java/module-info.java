module com.mycompany.mavenproject1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.mavenproject1 to javafx.fxml;
    opens com.mycompany.mavenproject1.controller to javafx.fxml;
    opens com.mycompany.mavenproject1.model to javafx.base;

    exports com.mycompany.mavenproject1;
    exports com.mycompany.mavenproject1.controller;
    exports com.mycompany.mavenproject1.model;
    exports com.mycompany.mavenproject1.dao;
    exports com.mycompany.mavenproject1.database;
    exports com.mycompany.mavenproject1.util;
}
