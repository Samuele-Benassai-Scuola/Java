module it.benassai {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens it.benassai to javafx.fxml;
    exports it.benassai;
}
