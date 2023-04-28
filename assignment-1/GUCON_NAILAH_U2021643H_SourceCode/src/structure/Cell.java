package structure;

/**
 Cell class represents a cell in a grid world and contains information such as cell attribute, utility and action.
 */
public class Cell extends Coordinates {
    // to store the cell type of this cell
    private CellAttribute cellAttribute;
    // to store the utility of this cell
    private double utility;
    // store the action of this cell
    private Action action;

    // takes a Coordinate object as a parameter to initialise the cell with a specified coordinate
    public Cell(Coordinates coordinate) {
        // Calls the constructor of the Coordinate class to initialise the col and row values of the cell
        super(coordinate.getCol(), coordinate.getRow());
        // initial cell attribute is white, initial utility is 0, initial action is UP
        this.cellAttribute = CellAttribute.WHITE;
        this.utility = 0;
        this.action = Action.UP;
    }

    public CellAttribute getCellAttribute() {
        return cellAttribute;
    }

    public void setCellAttribute(char type) {
        switch (type) {
            // WALL
            case 'X':
                this.cellAttribute = CellAttribute.WALL;
                this.setUtility(Const.REWARD_WALL);
                break;
            // WHITE
            case 'W':
                this.cellAttribute = CellAttribute.WHITE;
                this.setUtility(Const.REWARD_WHITESQUARE);
                break;
            // GREEN
            case 'G':
                this.cellAttribute = CellAttribute.GREEN;
                this.setUtility(Const.REWARD_GREENSQUARE);
                break;
            // BROWN
            case 'B':
                this.cellAttribute = CellAttribute.BROWN;
                this.setUtility(Const.REWARD_BROWNSQUARE);
                break;
        }
    }
    public double getUtility() {
        return utility;
    }

    public void setUtility(double utility) {
        this.utility = utility;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(int val) {
        switch (val) {
            case Coordinates.UP:
                this.action = Action.UP;
                break;
            case Coordinates.DOWN:
                this.action = Action.DOWN;
                break;
            case Coordinates.LEFT:
                this.action = Action.LEFT;
                break;
            case Coordinates.RIGHT:
                this.action = Action.RIGHT;
                break;
        }
    }
}
