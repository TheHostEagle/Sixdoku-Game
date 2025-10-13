package example.sixdoku.controllers;

import example.sixdoku.App;
import example.sixdoku.models.AlertBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class welcomeController
{

    @FXML
    private Pane rootPane;

    @FXML
    public void instructions()
    {
        AlertBox alertBox = new AlertBox();
        alertBox.showAlertBox("INSTRUCCIONES","{añadir}","{añadir luego}");
    }

    @FXML
    public void nextStage()
    {
        try
        {
            Stage stage = (Stage) rootPane.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(App.class.getResource("views/gameView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 600);

            stage.setScene(scene);

        } catch (IOException e)
        {
            System.err.println("Error al cargar la vista del juego: " + e.getMessage());
            e.printStackTrace();

            AlertBox alert = new AlertBox();
            alert.showAlertBox("Error", "No se pudo cargar la pantalla del juego.", "Error de Carga");
        }
    }
}
