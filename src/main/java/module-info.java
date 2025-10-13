module example.sixdoku {
    requires javafx.controls;
    requires javafx.fxml;


    opens example.sixdoku to javafx.fxml;
    exports example.sixdoku;
    exports example.sixdoku.controllers;
    opens example.sixdoku.controllers to javafx.fxml;
}