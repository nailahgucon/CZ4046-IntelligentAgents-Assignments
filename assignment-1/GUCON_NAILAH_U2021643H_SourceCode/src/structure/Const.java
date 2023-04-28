package structure;

public class Const {
    // cols and rows for Grid World
    public final static int NUM_COL = 6;
    public final static int NUM_ROW = 6;

    // Rewards for the walls: 0, white squares: -0.04, green squares: +1, and for the brown squares: -1
    public final static float REWARD_WALL = 0f;
    public final static float REWARD_WHITESQUARE = -0.04f;
    public final static float REWARD_GREENSQUARE = 1f;
    public final static float REWARD_BROWNSQUARE = -1f;

    // Based on diagram, intended outcome (UP): 0.8, left or right: 0.1
    public final static float PROBABILITY_UP = 0.8f;
    public final static float PROBABILITY_LEFT = 0.1f;
    public final static float PROBABILITY_RIGHT = 0.1f;

    // use a discount factor of 0.99
    public final static float DISCOUNT_FACTOR = 0.99f;
}
