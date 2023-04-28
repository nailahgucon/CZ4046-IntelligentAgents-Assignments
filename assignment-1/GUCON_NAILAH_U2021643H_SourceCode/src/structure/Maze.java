package structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Maze {
    Cell[][] cells;
    int numCol, numRow;

    public Maze() {
        // column and row value must be a positive integer
        if (numCol < 0 || numRow < 0)
            throw new IllegalArgumentException("Col and Row must be a positive integer.");

        this.numCol = Const.NUM_COL;
        this.numRow = Const.NUM_ROW;
        this.cells = new Cell[this.numCol][this.numRow];

        // Initializes each Cell object by looping over each element of cells and creating
        // a new Cell object with the appropriate Coordinates object
        for (int c = 0; c < numCol; c++) {
            for (int r = 0; r < numRow; r++) {
                // The Coordinates object is created using the current c and r values of the loops
                Coordinates coord = new Coordinates(c, r);
                cells[c][r] = new Cell(coord);
            }
        }
    }

    /**
     Maze(String fileName): Create a new instance of the Maze class by loading a maze from a file.
     The constructor takes a String argument fileName, which specifies the name of the file from which to load the maze.
     */
    public Maze(String fileName) {
        // calls the default Maze() constructor using this(), which initializes instance variables
        // numCol, numRow, and cells with default values
        this();
        // read the contents of the file and initialize the cells 2D array with the appropriate Cell objects
        this.importMapFromFile(fileName);
    }

    /**
     getCell(Coordinate coordinate) :
     1. extracts the column and row values from the Coordinate object
     2. returns the Cell object at the specified coordinates in the cells array
     */
    public Cell getCell(Coordinates coordinate) {
        int c = coordinate.getCol();
        int r = coordinate.getRow();

        return cells[c][r];
    }

    public Cell[] getNeighboursOfCell(Cell currCell) {
        // gets the action of the currCell
        // returns the direction in which the action (aka policy) recommends moving from the current cell
        Action currAction = currCell.getAction();
        // obtain an array of Coordinate objects representing the coordinates of neighboring cells in that direction
        Coordinates[] neighbourCoordinates = currCell.getNeighbours(currAction.getDirection());

        // checks each neighbor coordinate to see if it is a wall
        // create an array of Cell objects of the same length as the neighbourCoordinates array.
        Cell[] neighbourCells = new Cell[neighbourCoordinates.length];

        for (int n = 0; n < neighbourCoordinates.length; n++) {
            // // For each neighbor coordinate, get the corresponding Cell object
            Cell neighbourCell = this.getCell(neighbourCoordinates[n]);

            // If the CellAttribute of the neighbourCell is WALL, set the neighbourCoordinates coordinate to the current
            if (neighbourCell.getCellAttribute() == CellAttribute.WALL)
                neighbourCoordinates[n] = (Coordinates) currCell;

            // Otherwise, add the neighbourCoordinates to the neighbourCells array.
            neighbourCells[n] = this.getCell(neighbourCoordinates[n]);
        }

        // returns the neighbourCells array containing the neighboring cells of currCell
        return neighbourCells;
    }

    public Cell[] getNeighboursOfCell(Cell currCell, int direction) {
        // gets the neighbouring coordinates of the current cell in the given direction
        Coordinates[] neighbourCoordinates = currCell.getNeighbours(direction);

        // create an array of Cell objects for the neighbouring cells,
        // with the same length as the array of neighbouring coordinates
        Cell[] neighbourCells = new Cell[neighbourCoordinates.length];

        // for loop iterates over the neighbouring coordinates and gets the corresponding Cell object from the Maze
        for (int n = 0; n < neighbourCoordinates.length; n++) {
            Cell neighbourCell = this.getCell(neighbourCoordinates[n]);

            // If the neighbouring cell is a wall, replace the neighbouring coordinate with the current
            // cell's coordinate, effectively treating the wall as a barrier
            if (neighbourCell.getCellAttribute() == CellAttribute.WALL)
                neighbourCoordinates[n] = (Coordinates) currCell;

            // adds the Cell object to the neighbourCells array
            neighbourCells[n] = this.getCell(neighbourCoordinates[n]);
        }

        // returns the array of neighbouring cells.
        return neighbourCells;
    }

    /**
     print(): prints the maze to the console.
     */
    public void print() {
        // loops through each row and column of the maze
        for (int r = 0; r < this.numRow; r++) {
            for (int c = 0; c < this.numCol; c++) {
                Cell currCell = cells[c][r];

                // checks if the current cell is not a wall
                if (currCell.getCellAttribute() != CellAttribute.WALL) {
                    // If not wall, retrieve the utility and action of the current cell
                    double utility = currCell.getUtility();
                    String CellAttribute = currCell.getCellAttribute().getSymbol();
                    String action = currCell.getAction().getSymbol();

                    System.out.printf("| " + CellAttribute + " %7.3f " + action, utility);
                } else {
                    // If the current cell is a wall, it print as ------------
                    System.out.print("|------------");
                }
            }
            System.out.println("|");
        }
        System.out.println();
    }

    public void importMapFromFile(String fileName) {
        try {
            // retrieves the absolute path of the current working directory
            String filePath = new File("").getAbsolutePath();
            // creates a Scanner object to read from the specified file path
            Scanner s = new Scanner(new BufferedReader(new FileReader(filePath.concat("/mazeEnvironments/" + fileName))));

            // iterates over each line of the file until there are no more lines
            while (s.hasNext()) {
                // iterate over each cell in the cells 2D array and set the corresponding cell type to the next character
                for (int r = 0; r < this.numRow; r++) {
                    for (int c = 0; c < this.numCol; c++) {
                        char type = s.next().charAt(0);
                        cells[c][r].setCellAttribute(type);
                    }
                }
            }
            // closes the Scanner object
            s.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
