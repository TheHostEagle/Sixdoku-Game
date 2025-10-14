package example.sixdoku.models;

import java.util.ArrayList;
import java.util.Collections;
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
        initializeEmptyBoard();
        fillBoard(0);
        solution = new ArrayList<>(board);
        createPartialVersion();
        initialBoard =  new ArrayList<>(board);
    }

    /**
     * Fill the Sudoku puzzle with a valid solution
     * @param index actual position in the array
     * @return true if sudoku puzzle was completed and false if a cell could not be filled
     */

    private boolean fillBoard(int index)
    {
        if(index >= TOTAL_CELLS) { return true; }

        int row = index / SIZE;
        int col = index % SIZE;

        ArrayList<Integer> numbers = new ArrayList<>();
        for(int i = 1; i <= SIZE; i++)
        {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        for (int i = 0; i < numbers.size(); i++)
        {
            int num = numbers.get(i);
            if (isValidMove(row, col, num) == true)
            {
                board.set(index, num);
                if (fillBoard(index + 1) == true)
                {
                    return true;
                }
                board.set(index, 0);
            }
        }
        return false;
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
    public boolean isValidMove(int row, int col, int num)
    {
        for(int j = 0; j < SIZE; j++)
        {
            int index = (row * SIZE) + j;
            if(board.get(index) == num)
            {
                return false;
            }
        }

        for(int z = 0; z < SIZE; z++)
        {
             int index = (z * SIZE) + col;
             if(board.get(index) == num)
             {
                 return false;
             }
        }

        int startRow = (row / 2) * 2;
        int startCol = (col / 3 ) * 3;
        for(int k = startRow; k < startRow + 2; k++)
        {
            for(int l = startCol; l < startCol + 3; l++)
            {
                int index = (k * SIZE) + l;
                if(board.get(index) == num)
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