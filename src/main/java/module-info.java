module com.metadev.connect {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.metadev.connect to javafx.fxml;
    exports com.metadev.connect;
}