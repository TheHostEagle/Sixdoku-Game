package example.sixdoku.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import example.sixdoku.models.Sudoku;
import example.sixdoku.models.AlertBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import static javafx.scene.input.KeyCode.ENTER;

public class gameController
{

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView imageIcon2;

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
        startGIF();
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
     * Creates an individual cell with change listener
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
            }
            else
            {
                cell.setEditable(true);
                cell.setStyle(getCellStyle(row, col, false));
            }
        }
        else
        {
            cell.setText("");
            cell.setEditable(true);
            cell.setStyle(getCellStyle(row, col, false));
        }

        cell.textProperty().addListener((observable, oldValue, newValue) ->
        {
            try {
                if (newValue == null || newValue.trim().isEmpty())
                {
                    sudoku.setValueDirectly(row, col, 0);
                }
                else
                {
                    int number = Integer.parseInt(newValue);
                    if (number >= 1 && number <= 6)
                    {
                        sudoku.setValueDirectly(row, col, number);
                    }
                    else
                    {
                        AlertBox alertBox = new AlertBox();
                        alertBox.showAlertBox("Numero Invalido",
                                "Solo se permiten numeros del 1 al 6",
                                "");
                        cell.setText("");
                        sudoku.setValueDirectly(row, col, 0);
                    }
                }
            }
            catch (NumberFormatException e)
            {
                AlertBox alertBox = new AlertBox();
                alertBox.showAlertBox("Entrada Invalida",
                        "Solo se permiten numeros enteros del 1 al 6",
                        "");

                cell.setText("");
                sudoku.setValueDirectly(row, col, 0);
            }

            cell.setOnKeyPressed(event ->
            {
                if (event.getCode() == ENTER)
                {
                    validate();
                }
            });
        });
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

    public void startGIF()
    {
        try
        {
            Image gif = new Image(getClass().getResourceAsStream("/example/sixdoku/images/disco.gif"));
            imageIcon2.setImage(gif);

            imageIcon2.setFitWidth(31);
            imageIcon2.setFitHeight(28);
            imageIcon2.setPreserveRatio(true);

        } catch (Exception e)
        {
            System.err.println("Error al cargar el GIF: " + e.getMessage());
        }
    }

    @FXML
    private void handleImageClick(MouseEvent event)
    {
        if (event.getSource() == imageIcon2)
        {
            System.out.println("Boton pista clickeado - Buscando celda vacia...");
            giveHint();
        }
    }



    private void giveHint()
    {
        // Buscar celdas vacías y guardar la cuenta en este contador
        int emptyCount = 0;
        for (int row = 0; row < 6; row++)
        {
            for (int col = 0; col < 6; col++)
            {
                if (sudoku.getValue(row, col) == 0)
                {
                    emptyCount++;
                }
            }
        }

        if (emptyCount == 0)
        {
            new AlertBox().showAlertBox("Sin pistas", "No hay celdas vacias", "");
            return;
        }

        int randomIndex = (int) (Math.random() * emptyCount);
        int currentIndex = 0;

        for (int row = 0; row < 6; row++)
        {
            for (int col = 0; col < 6; col++)
            {
                if (sudoku.getValue(row, col) == 0)
                {
                    if (currentIndex == randomIndex)
                    {
                        int correctNumber = sudoku.getSolution().get(row * 6 + col);

                        sudoku.setValueDirectly(row, col, correctNumber);

                        displayBoard();
                        new AlertBox().showAlertBox("Pista", "Numero " + correctNumber + " agregado", "");
                        return;
                    }
                    currentIndex++;
                }
            }
        }
    }
}
