module matias.jorge.actvpersonas {
    requires javafx.controls;
    requires javafx.fxml;

    opens matias.jorge.actvpersonas to javafx.fxml;
    opens matias.jorge.actvpersonas.controllers to javafx.fxml;
    opens matias.jorge.actvpersonas.models to javafx.fxml, javafx.base;
    exports matias.jorge.actvpersonas;
}
