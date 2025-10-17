package example.sixdoku.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import example.sixdoku.models.Sudoku;
import example.sixdoku.models.AlertBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

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
    private int count = 0;
    private boolean gameStarted = false;

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
     * Show the instructions of the game.
     * @param event is the action that happens when the user press the button.
     */
    @FXML
    void instructionsGame(ActionEvent event) {
        AlertBox alertBox = new AlertBox();
        alertBox.showAlertBox("Instrucciones del juego",
                "El objetivo del juego es llenar toda la cuadrícula de 6 filas por 6 columnas con números del 1 al 6, siguiendo estas reglas básicas:\n" +
                        "1.) Cada fila debe contener los números del 1 al 6, sin repetir ninguno.\n" +
                        "2.) Cada columna también debe contener los números del 1 al 6, sin repeticiones.\n" + "3.) El tablero está dividido en seis bloques de 2x3 celdas, y en cada bloque también deben aparecer los números del 1 al 6 una sola vez.\n",
                "INSTRUCCIONES DE JUEGO - SUDOKU 6X6:");
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
        cell.setPrefSize(60, 60);

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
                cell.setEditable(gameStarted);
                cell.setStyle(getCellStyle(row, col, false));
            }
        }
        else
        {
            cell.setText("");
            cell.setEditable(gameStarted);
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
        StringBuilder style = new StringBuilder();

        // Estilo base
        style.append("-fx-font-size: 18px; -fx-alignment: center; ");

        if (isHint)
        {
            style.append("-fx-background-color: #f0f0f0; ");
        }
        else
        {
            style.append("-fx-background-color: white; ");
        }

        style.append("-fx-border-color: black; ");
        style.append("-fx-border-style: solid inside; ");

        // Grosor de bordes para cada lado
        double top = 0.2, right = 0.2, bottom = 0.2, left = 0.2;

        // Bordes gruesos entre bloques 2x3
        if (row == 1 || row == 3) bottom = 2;  // Linea gruesa abajo
        if (col == 2) right = 2;               // Linea gruesa derecha

        // Bordes exteriores gruesos
        if (row == 0) top = 2;
        if (row == 5) bottom = 2;
        if (col == 0) left = 2;
        if (col == 5) right = 2;

        style.append("-fx-border-width: ")
                .append(top).append(" ")
                .append(right).append(" ")
                .append(bottom).append(" ")
                .append(left).append(";");

        return style.toString();
    }

    /**
     * START button - Creates new game
     */
    @FXML
    private void startGame()
    {
        AlertBox alertBox = new AlertBox();
        sudoku.generateSudoku();
        count = 0;
        gameStarted = true;
        imageIcon2.setVisible(true);
        displayBoard();
        alertBox.showAlertBox("Juego Iniciado", "Un nuevo Sixdoku fue generado!","");
    }

    /**
     * VALIDATE button - Checks if Sudoku is correct
     */
    @FXML
    private void validate()
    {
        boolean correct = true;

        for(Node node : gridPane.getChildren())
        {
            if(node instanceof TextField cell)
            {
                int row = GridPane.getRowIndex(cell);
                int col = GridPane.getColumnIndex(cell);
                int userValue = sudoku.getValue(row, col);
                int correctValue = sudoku.getSolution().get(row * 6 + col );
                if (userValue == 0)
                {
                    cell.setStyle(getCellStyle(row, col, false));
                    correct = false;
                }
                else if (userValue != correctValue)
                {
                    cell.setStyle("-fx-background-color: #ffb3b3; -fx-font-weight: bold; -fx-font-size: 18px; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 1;");
                    correct = false;
                }
                else
                {
                    cell.setStyle(getCellStyle(row, col, true));
                }
            }
        }
        if (correct == true)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Felicidades");
            alert.setHeaderText("Has completado el sudoku");
            alert.setContentText("¿Que deseas hacer?");
            ButtonType newGame = new  ButtonType("Nuevo sudoku");
            ButtonType exit = new  ButtonType("Salir");
            alert.getButtonTypes().setAll(newGame, exit);
            //Esperar la respuesta del jugador
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent())
            {
                if(result.get() == newGame)
                {
                    startGame();
                } else if(result.get() == exit)
                {
                    Platform.exit();
                }
            }
        } else
        {
            AlertBox alertBox = new AlertBox();
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
        if(count >= 3) {
            AlertBox alertBox = new AlertBox();
            alertBox.showAlertBox("Limite de pistas", "Ya se usaron todas las pistas permitidas", "");
            imageIcon2.setVisible(false);
            return;
        }
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

        if(emptyCount <= 2)
        {
            AlertBox alertBox = new AlertBox();
            alertBox.showAlertBox("Sin pistas", "Muy pocas celdas, no es posible usar la pista", "");
            return;
        }

        if (emptyCount == 0)
        {
            AlertBox alertBox = new AlertBox();
            alertBox.showAlertBox("Sin pistas", "No hay celdas vacias", "");
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
                        count++;
                        AlertBox alertBox = new AlertBox();
                        alertBox.showAlertBox("Pista", "Numero " + correctNumber + " agregado", "");
                        return;
                    }
                    currentIndex++;
                }
            }
        }
    }
}
