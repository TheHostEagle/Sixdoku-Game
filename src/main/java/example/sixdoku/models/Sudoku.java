package example.sixdoku.models;

import java.util.ArrayList;
import java.util.Random;

/**
 * Model that represents the 6x6 Sudoku game
 * Contains all game logic
 */
public class Sudoku
{
    private ArrayList<Integer> board;
    private ArrayList<Integer> initialBoard;
    private ArrayList<Integer> solution;
    private final int SIZE = 6;
    private final int TOTAL_CELLS = SIZE * SIZE;

    public Sudoku()
    {
        this.board = new ArrayList<>();
        this.initialBoard = new ArrayList<>();
        initializeEmptyBoard();
    }

    /**
     * Initializes the board with zeros
     */
    public void initializeEmptyBoard()
    {
        board.clear();
        for (int i = 0; i < TOTAL_CELLS; i++)
        {
            board.add(0);
        }
        initialBoard = new ArrayList<>(board);
    }

    /**
     * Generates a valid Sudoku puzzle
     */
    public void generateSudoku()
    {
        ArrayList<Integer> predefinedSolution = new ArrayList<>();
        int[] solutionArray =
                {
                1, 2, 3, 4, 5, 6,
                4, 5, 6, 1, 2, 3,
                2, 3, 4, 5, 6, 1,
                5, 6, 1, 2, 3, 4,
                3, 4, 5, 6, 1, 2,
                6, 1, 2, 3, 4, 5
        };

        for (int i = 0; i < solutionArray.length; i++)
        {
            int value = solutionArray[i];
            predefinedSolution.add(value);
        }

        this.solution = new ArrayList<>(predefinedSolution);
        this.board = new ArrayList<>(this.solution);
        createPartialVersion();
        this.initialBoard = new ArrayList<>(this.board);
    }

    /**
     * Creates a playable version with some empty cells
     */
    private void createPartialVersion()
    {
        Random random = new Random();
        int cellsToRemove = TOTAL_CELLS / 2;

        for (int i = 0; i < cellsToRemove; i++)
        {
            int position = random.nextInt(TOTAL_CELLS);
            board.set(position, 0);
        }
    }

    /**
     * Places a number in a specific position
     */
    public boolean placeNumber(int row, int col, int number)
    {
        if (isValidMove(row, col, number))
        {
            int position = convertCoordinates(row, col);
            board.set(position, number);
            return true;
        }
        return false;
    }

    /**
     * Checks if a move is valid according to Sudoku rules
     */
    public boolean isValidMove(int row, int col, int number)
    {

        for (int c = 0; c < SIZE; c++)
        {
            if (c != col && getValue(row, c) == number)
            {
                return false;
            }
        }


        for (int r = 0; r < SIZE; r++)
        {
            if (r != row && getValue(r, col) == number)
            {
                return false;
            }
        }

        int startRow = (row / 2) * 2;
        int startCol = (col / 3) * 3;

        for (int r = startRow; r < startRow + 2; r++)
        {
            for (int c = startCol; c < startCol + 3; c++)
            {
                if ((r != row || c != col) && getValue(r, c) == number)
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if the Sudoku is completely solved
     */
    public boolean isSolved()
    {
        for (int i = 0; i < TOTAL_CELLS; i++)
        {
            if (!board.get(i).equals(solution.get(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Resets the board to initial state
     */
    public void reset()
    {
        this.board = new ArrayList<>(this.initialBoard);
    }

    /**
     * Converts (row, column) coordinates to ArrayList position
     */
    public int convertCoordinates(int row, int col)
    {
        return row * SIZE + col;
    }

    /**
     * Gets the value at a specific position
     */
    public int getValue(int row, int col)
    {
        return board.get(convertCoordinates(row, col));
    }

    /**
     * Gets the initial value at a position (to know if it was a hint)
     */
    public int getInitialValue(int row, int col)
    {
        return initialBoard.get(convertCoordinates(row, col));
    }

    /**
     * Gets a random hint (correct value for an empty cell)
     */
    public int getHint(int row, int col)
    {
        return solution.get(convertCoordinates(row, col));
    }

    /**
     * Checks if a cell was originally empty (editable)
     */
    public boolean isCellEditable(int row, int col)
    {
        return getInitialValue(row, col) == 0;
    }

    // Getters
    public ArrayList<Integer> getBoard() { return new ArrayList<>(board); }
    public ArrayList<Integer> getSolution() { return new ArrayList<>(solution); }
    public int getSIZE() { return SIZE; }
    public int getTotalCells() { return TOTAL_CELLS; }
}