package example.sixdoku.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import example.sixdoku.models.Sudoku;
import example.sixdoku.models.AlertBox;

public class gameController
{

    @FXML
    private GridPane gridPane;

    @FXML
    private Button startButton;

    @FXML
    private Button validateButton;

    private Sudoku sudoku;

    /**
     * Automatically executed when view loads
     */
    @FXML
    public void initialize()
    {
        this.sudoku = new Sudoku();
        showEmptyBoard();
    }

    /**
     * Displays the board on the interface
     */
    private void displayBoard()
    {
        gridPane.getChildren().clear();

        for (int row = 0; row < sudoku.getSIZE(); row++)
        {
            for (int col = 0; col < sudoku.getSIZE(); col++)
            {
                TextField cell = createCell(row, col);
                gridPane.add(cell, col, row);
            }
        }
    }

    /**
     * Shows empty board
     */
    private void showEmptyBoard()
    {
        sudoku.initializeEmptyBoard();
        displayBoard();
    }

    /**
     * Creates an individual cell
     */
    private TextField createCell(int row, int col)
    {
        TextField cell = new TextField();
        cell.setPrefSize(50, 50);

        int value = sudoku.getValue(row, col);
        int initialValue = sudoku.getInitialValue(row, col);


        if (value != 0)
        {
            cell.setText(String.valueOf(value));
            if (initialValue != 0)
            {
                cell.setEditable(false);
                cell.setStyle(getCellStyle(row, col, true));
            } else
            {
                cell.setEditable(true);
                cell.setStyle(getCellStyle(row, col, false));
            }
        } else
        {
            cell.setText("");
            cell.setEditable(true);
            cell.setStyle(getCellStyle(row, col, false));
        }

        return cell;
    }

    /**
     * Gets CSS style for a cell
     */
    private String getCellStyle(int row, int col, boolean isHint)
    {
        String style = "-fx-font-size: 18px; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 1;";

        // Thick borders between blocks
        if (col == 2) style += " -fx-border-right-width: 3;";
        if (row == 1 || row == 3) style += " -fx-border-bottom-width: 3;";

        // Background color for hints
        if (isHint) style += " -fx-background-color: #f0f0f0; -fx-font-weight: bold;";

        return style;
    }

    /**
     * START button - Creates new game
     */
    @FXML
    private void startGame()
    {
        AlertBox alertBox = new AlertBox();
        sudoku.generateSudoku();
        displayBoard();
        alertBox.showAlertBox("Juego Iniciado", "Un nuevo Sixdoku fue generado!","");
    }

    /**
     * VALIDATE button - Checks if Sudoku is correct
     */
    @FXML
    private void validate()
    {
        AlertBox alertBox = new AlertBox();
        if (sudoku.isSolved())
        {
            alertBox.showAlertBox("Felicidades!", "Has resuelto correctamente el Sixdoku!","");
        } else
        {
            alertBox.showAlertBox("Sigue Intentando", "El Sixdoku aun no esta resuelto.","");
        }
    }

    /**
     * RESET button - Returns to initial state
     */
    @FXML
    private void onActionReset()
    {
        AlertBox alertBox = new AlertBox();
        System.out.println("Boton de reinicio presionado");
        sudoku.reset();
        displayBoard();
        alertBox.showAlertBox("Reiniciar Juego", "Tablero devuelto al estado inicial","");
    }

}
