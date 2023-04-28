package output;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

// import structure classes needed
import structure.Maze;
import structure.Const;
import structure.Coordinates;
import structure.CellAttribute;
public class createOutput {
    private String fileName;
    private String timestamp;
    private List<String> headers;
    private List<List<Double>> data;

    public createOutput(String fileName, Maze maze) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_hh-mm-aa");
        Date date = new Date();

        // initializes the fileName and timestamp fields based on the current date and time
        this.fileName = fileName;
        this.timestamp = dateFormat.format(date);

        // initializes an empty ArrayList of headers and an empty ArrayList of data
        this.headers = new ArrayList<String>();
        this.data = new ArrayList<List<Double>>();

        // loops through all the cells in the maze, and for each cell, adds a header to the headers list
        for (int col = 0; col < Const.NUM_COL; col++) {
            for (int row = 0; row < Const.NUM_ROW; row++) {
                if (maze.getCell(new Coordinates(col, row)).getCellAttribute() == CellAttribute.WALL) {
                    // If it's a wall, add header = "Wall: (c, r)" where c and r are the column and row coordinates of the wall cell
                    headers.add("\"Wall: (" + col + ", " + row + ")\"");
                } else {
                    // If it's NOT a wall, add header = "State: (c, r)" where c and r are the column and row coordinates of the non-wall cell.
                    headers.add("\"State: (" + col + ", " + row + ")\"");
                }
            }
        }
    }

    public void add(Maze maze) {
        List<Double> nextCell = new ArrayList<Double>();

        // loops through all cells in the maze by iterating over all the columns and rows
        for (int col = 0; col < Const.NUM_COL; col++) {
            for (int row = 0; row < Const.NUM_ROW; row++) {
                // retrieves the utility value and add to nextCell list
                nextCell.add(maze.getCell(new Coordinates(col, row)).getUtility());
            }
        }

        // adds the nextCell list to the data list, effectively adding a new row to the output.
        this.data.add(nextCell);
    }

    private double findMaxPositiveUtility(int iteration) {
        // iteration = index of a list of doubles within data;
        // returns the maximum value in that list
        return Collections.max(this.data.get(iteration));
    }

    private double findMaxNegativeUtility(int iteration) {
        // iteration = index of a list of doubles within data;
        // returns the minimum value in that list
        return Collections.min(this.data.get(iteration));
    }

    public void finalise() {
        // write the data to a CSV file
        // concatenates the file name with the timestamp and passes the headers and data
        createFile.writeToFile(this.fileName + "_" + this.timestamp, this.headers, this.data);
    }
}
