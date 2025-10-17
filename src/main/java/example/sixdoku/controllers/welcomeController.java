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
        alertBox.showAlertBox("Instrucciones del juego",
                "El objetivo del juego es llenar toda la cuadrícula de 6 filas por 6 columnas con números del 1 al 6, siguiendo estas reglas básicas:\n" +
                "1.) Cada fila debe contener los números del 1 al 6, sin repetir ninguno.\n" +
                "2.) Cada columna también debe contener los números del 1 al 6, sin repeticiones.\n" + "3.) El tablero está dividido en seis bloques de 2x3 celdas, y en cada bloque también deben aparecer los números del 1 al 6 una sola vez.\n",
                "INSTRUCCIONES DE JUEGO - SUDOKU 6X6:");
    }

    @FXML
    public void nextStage()
    {
        try
        {
            Stage stage = (Stage) rootPane.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(App.class.getResource("views/gameView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 700, 550);

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
