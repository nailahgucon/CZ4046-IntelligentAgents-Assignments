package structure;

public class Coordinates {
    public static final int ALL_DIRECTIONS = 4;
    public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
    private int col, row;

    /**
     coordinates are in (col,row) format with the top left corner being (0,0).

     Coordinates(int col, int row): It checks if the column and row values are valid,
     i.e. if they are both non-negative and if they are not greater than the maximum column
     and row indices (which are defined in the Const class). If any of these checks fail, it
     throws an IllegalArgumentException with an appropriate error message.

     If all the checks pass, it sets the col and row instance variables of the Coordinate object
     to the values of the arguments.
     */
    public Coordinates(int col, int row) {
        if (col < 0 || row < 0)
            throw new IllegalArgumentException("Column and row values must be a positive integer.");
        else if (col > Const.NUM_COL - 1)
            throw new IllegalArgumentException("Column value is out of range.");
        else if (row > Const.NUM_ROW - 1)
            throw new IllegalArgumentException("Row value is out of range.");
        else {
            this.col = col;
            this.row = row;
        }
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }

    public Coordinates[] getNeighbours(int direction) {
        // Creates a new array of Coordinate objects with a length of 3.
        Coordinates[] coordinates = new Coordinates[3];

        // Defines a 3D array of integers called offset that represents the offset of the neighboring cells in each direction.
        // first dimension: four cardinal directions (up, down, left, right),
        // second dimension: three types of neighboring cells in that direction (forward, right angle left, right angle right),
        // third dimension: col and row offsets of the neighboring cell
        int[][][] offset = { { { 0, -1 }, { -1, 0 }, { +1, 0 } }, { { 0, +1 }, { +1, 0 }, { -1, 0 } },
                { { -1, 0 }, { 0, +1 }, { 0, -1 } }, { { +1, 0 }, { 0, -1 }, { 0, +1 } } };

        // Attempt to create a new Coordinate object at the specified column and row offsets in the UP direction
        // using the offset array.
        // If the Coordinate object cannot be created (because the resulting col or row is out of range),
        // the catch block sets coordinates[0] to the current Coordinate object (i.e., this).
        try {
            coordinates[0] = new Coordinates(this.col + offset[direction][0][0], this.row + offset[direction][0][1]);
        } catch (IllegalArgumentException e) {
            coordinates[0] = this;
        }

        // Attempt to create a new Coordinate object at the specified column and row offsets in the LEFT direction
        // using the offset array.
        // If the Coordinate object cannot be created (because the resulting col or row is out of range),
        // the catch block sets coordinates[0] to the current Coordinate object (i.e., this).
        try {
            coordinates[1] = new Coordinates(this.col + offset[direction][1][0], this.row + offset[direction][1][1]);
        } catch (IllegalArgumentException e) {
            coordinates[1] = this;
        }

        // Attempt to create a new Coordinate object at the specified column and row offsets in the RIGHT direction
        // using the offset array.
        // If the Coordinate object cannot be created (because the resulting col or row is out of range),
        // the catch block sets coordinates[0] to the current Coordinate object (i.e., this).
        try {
            coordinates[2] = new Coordinates(this.col + offset[direction][2][0], this.row + offset[direction][2][1]);
        } catch (IllegalArgumentException e) {
            coordinates[2] = this;
        }

        // returns the coordinates array of neighboring Coordinate objects
        return coordinates;
    }
}
