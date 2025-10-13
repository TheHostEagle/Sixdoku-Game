package example.sixdoku.models;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class AlertBox implements IAlertBox
{
    @Override
    public void showAlertBox(String title, String message, String header)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        //alert.getDialogPane().setPrefSize(400, 450);
        alert.showAndWait();
    }
}
