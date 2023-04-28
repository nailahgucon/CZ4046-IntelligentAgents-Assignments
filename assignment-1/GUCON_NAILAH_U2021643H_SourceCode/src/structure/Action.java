package structure;

/**
 Defines four possible actions for agent to take: UP (0), DOWN (1), LEFT (2), and RIGHT (3).
 */
public enum Action {
    UP(0), DOWN(1), LEFT(2), RIGHT(3);

    private int direction; // UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3
    private String symbol;

    private Action(int direction) {
        this.direction = direction;

        switch (direction) {
            case 0:
                // UP
                this.symbol = "\uD83E\uDC45";
                break;
            case 1:
                // DOWN
                this.symbol = "\uD83E\uDC47";
                break;
            case 2:
                // LEFT
                this.symbol = "\uD83E\uDC44";
                break;
            case 3:
                // RIGHT
                this.symbol = "\uD83E\uDC46";
                break;
        }
    }

    // returns the Unicode symbol for the enumerated value
    public String getSymbol() {
        return this.symbol;
    }

    // returns the integer direction for the enumerated value
    public int getDirection() {
        return this.direction;
    }
}
