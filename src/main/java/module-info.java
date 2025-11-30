module org.example.autovermietung {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens org.example.autovermietung to javafx.fxml, org.hibernate.orm.core;

    exports org.example.autovermietung;
    exports org.example.autovermietung.Controller;
    opens org.example.autovermietung.Controller to javafx.fxml, org.hibernate.orm.core;
    exports org.example.autovermietung.Model;
    opens org.example.autovermietung.Model to javafx.fxml, org.hibernate.orm.core;
    exports org.example.autovermietung.Repository;
    opens org.example.autovermietung.Repository to javafx.fxml, org.hibernate.orm.core;
}