module org.example.autovermietung {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    // JDBC / SQLite
    requires java.sql;

    // JPA / Hibernate
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    // Optional UI libs (keep only if you actually use them)
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires jdk.compiler;
    requires java.desktop;

    // Allow FXMLLoader to access controllers
    opens org.example.autovermietung.Controller to javafx.fxml;
    opens org.example.autovermietung.View to javafx.fxml;

    // Allow Hibernate/JPA reflection access to entities
    opens org.example.autovermietung.Model to org.hibernate.orm.core, jakarta.persistence;

    // Export packages (only needed if other modules depend on them)
    exports org.example.autovermietung;
    exports org.example.autovermietung.Controller;
    exports org.example.autovermietung.Model;
}