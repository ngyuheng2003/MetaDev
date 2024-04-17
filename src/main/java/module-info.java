module com.metadev.connect {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires spring.security.crypto;
    requires com.zaxxer.hikari;

    opens com.metadev.connect to javafx.fxml;
    exports com.metadev.connect;
}