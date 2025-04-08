module it.benassai {
    requires javafx.controls;
    requires javafx.fxml;

    opens it.benassai to javafx.fxml;
    exports it.benassai;
}
