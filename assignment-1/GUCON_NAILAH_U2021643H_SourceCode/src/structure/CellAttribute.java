package structure;

public enum CellAttribute {
    WHITE, GREEN, BROWN, WALL;

    public float getReward() {
        // Rewards for the walls: 0, white squares: -0.04, green squares: +1, and for the brown squares: -1
        // Obtain values from Const class
        switch (this) {
            case WALL:
                return Const.REWARD_WALL;
            case WHITE:
                return Const.REWARD_WHITESQUARE;
            case GREEN:
                return Const.REWARD_GREENSQUARE;
            case BROWN:
                return Const.REWARD_BROWNSQUARE;
            default:
                return 0f;
        }
    }

    public String getSymbol() {
        switch (this) {
            case WHITE:
                return "W";
            case GREEN:
                return "G";
            case BROWN:
                return "B";
            case WALL:
                return "X";
            default:
                return "";
        }
    }
}
