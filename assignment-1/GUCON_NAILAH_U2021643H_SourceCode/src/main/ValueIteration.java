package main;

import structure.Maze;
import structure.Coordinates;
import structure.Const;
import structure.CellAttribute;
import structure.Cell;
import output.createOutput;

/**
 Value Iteration:
 At each iteration, the algorithm updates the value function for each state by taking the maximum over all possible
 actions and their expected returns. This process is repeated until the value function converges to the optimal
 value function. Once the optimal value function is obtained, the optimal policy can be derived by taking the
 action with the maximum expected return for each state.
 */
public class ValueIteration {
    private final static double C = 0.1;
    // maximum reward in the given environment
    private final static float R_MAX = 1;
    // Epsilon is a constant that indicates the maximum error allowable
    // for a given state, before accounting for the discount factor
    private final static double EPSILON = C * R_MAX;

    public static void main(String[] args) {
        /** CHANGE THIS to the complicated mazes txt files e.g. "complicated-maze-48x48-4.txt" */
        Maze maze = new Maze("maze.txt");
        runValueIteration(maze);
    }

    private static void runValueIteration(Maze maze) {
        // initializes the iteration counter to 1
        int iteration = 1;
        // initializes the maximum change in utility to 0
        double maxChangeInUtility = 0;
        // calculates the threshold for stopping the algorithm
        double threshold = EPSILON * ((1 - Const.DISCOUNT_FACTOR) / Const.DISCOUNT_FACTOR);
        // output the progress of the algorithm
        createOutput output = new createOutput("ValueIteration", maze);

        System.out.println("Running Value Iteration...");
        System.out.println();
        System.out.println("Original Maze:");
        // prints out the original maze
        maze.print();
        // adds original maze to the output
        output.add(maze);

        do {
            System.out.println("======================================================================================");
            System.out.printf("Iteration: %d\n", iteration);
            // resets the maximum change in utility to 0 at the beginning of each iteration
            maxChangeInUtility = 0;

            // loop over the columns of the maze
            for (int c = 0; c < Const.NUM_COL; c++) {
                // loop over the rows of the maze
                for (int r = 0; r < Const.NUM_ROW; r++) {
                    // gets the current cell based on the current column and row
                    Cell curCell = maze.getCell(new Coordinates(c, r));

                    // if the current cell is a wall, skip it
                    if (curCell.getCellAttribute() == CellAttribute.WALL)
                        continue;

                    // calculates the change in utility for the current cell based on its neighbors and
                    // updates the current cell's utility.
                    double changeInUtility = calculateUtility(curCell, maze);

                    // if the current change in utility is greater than the current maximum change in utility,
                    // update the maximum change in utility to the current change in utility
                    if (changeInUtility > maxChangeInUtility)
                        maxChangeInUtility = changeInUtility;
                }
            }

            System.out.println();
            System.out.printf("Threshold: %5.5f\n", threshold);
            System.out.printf("Maximum change in utility: %5.5f\n", maxChangeInUtility);
            System.out.println();
            // print out the maze for the current iteration
            System.out.printf("Updated Maze for Iteration %d:\n", iteration);
            maze.print();
            // add the maze for the current iteration to the output
            output.add(maze);
            if (maxChangeInUtility <= threshold){
                System.out.println("Optimal value function has been obtained!");
            }
            // increment the iteration counter
            iteration++;
        } while (maxChangeInUtility > threshold); // run until the maximum change in utility is less than the threshold

        output.finalise();
    }

    /**
     calculateUtility(Cell currCell, Maze maze):
     calculates the new utility value for a given cell in the maze using the Bellman equation;
     Bellman equation: reward + discount_factor * max(sub-utilities)
     */
    private static double calculateUtility(Cell curCell, Maze maze) {
        // Create an array to store the calculated sub-utilities for each direction.
        double[] subUtilities = new double[Coordinates.ALL_DIRECTIONS];

        // Loop through all possible directions for the cell and calculate the sub-utility value for each direction.
        // This is done by summing up the weighted utility values of the three neighbors (i.e. UP, LEFT, RIGHT)
        // in the given direction.
        for (int direction = 0; direction < Coordinates.ALL_DIRECTIONS; direction++) {
            Cell[] neighbours = maze.getNeighboursOfCell(curCell, direction);
            double up = Const.PROBABILITY_UP * neighbours[0].getUtility();
            double left = Const.PROBABILITY_LEFT * neighbours[1].getUtility();
            double right = Const.PROBABILITY_RIGHT * neighbours[2].getUtility();

            subUtilities[direction] = up + left + right;
        }

        // Determine which direction with the maximum sub-utility and sets the utility and
        // action of the current cell to the corresponding values.
        int maximumUtility = 0;
        for (int utility = 1; utility < subUtilities.length; utility++) {
            if (subUtilities[utility] > subUtilities[maximumUtility]) {
                maximumUtility = utility;
            }
        }

        // Retrieve the current reward of the cell
        float curReward = curCell.getCellAttribute().getReward();
        // Retrieve the current utility value of the cell
        double prevUtility = curCell.getUtility();
        // Calculate the new utility value for the cell using the Bellman equation:
        // reward + discount_factor (0.99) * max(sub-utilities)
        double newUtility = curReward + Const.DISCOUNT_FACTOR * subUtilities[maximumUtility];
        // Set the new utility value and the corresponding action (aka policy) for the cell
        curCell.setUtility(newUtility);
        curCell.setAction(maximumUtility);

        // Return the absolute difference between the old and new utility values of the cell
        return (Math.abs(prevUtility - newUtility));
    }
}
