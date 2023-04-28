package main;

import structure.Maze;
import structure.Coordinates;
import structure.Const;
import structure.CellAttribute;
import structure.Cell;
import output.createOutput;
public class PolicyIteration {
    private final static int K = 75;

    public static void main(String[] args) {
        /** CHANGE THIS to the complicated mazes txt files e.g. "complicated-maze-48x48-4.txt" */
        Maze maze = new Maze("maze.txt");
        runPolicyIteration(maze);
    }

    /**
     Policy Iteration:
     It iteratively evaluates and improves the policy until it converges to an optimal policy.
     The algorithm consists of two main steps: Policy Evaluation (1) and Policy Improvement (2).
     (1) In Policy Evaluation, the utility of each state in the maze is estimated using the Bellman Equation,
         which takes into account the expected utility of the successor states.
     (2) In Policy Improvement, the policy of each state is updated to the action that maximizes the expected
         utility of the successor states. The algorithm iterates through each state in the maze (excluding walls)
         and checks if changing the policy of that state would improve the overall policy. If yes, the policy of
         that state is updated, and the algorithm continues to the next state.
     The algorithm terminates when the policy no longer changes after (2), indicating that
     it has converged to an optimal policy.
     */
    private static void runPolicyIteration(Maze maze) {
        // track whether any policy changes were made during current iteration
        boolean changeOrNot;
        // Initializes the iteration counter to 1
        int iteration = 1;
        // add the intermediate maze states during the algorithm execution to output
        createOutput output = new createOutput("PolicyIteration", maze);

        System.out.println("Running Policy Iteration...");
        System.out.println();
        System.out.println("Original Maze:");
        maze.print();
        output.add(maze);

        do {
            System.out.println("======================================================================================");
            System.out.printf("Iteration %d:\n", iteration);
            changeOrNot = false;

            // Calls the policyEvaluation with a fixed number of iterations K
            policyEvaluation(maze, K);

            // Loops over all cells in the maze and performs policy improvement for each non-wall cell
            // At the end of the loop (based on while condition), if changeOrNot is still true, the policy iteration loop continues.
            // If changeOrNot is false, the loop terminates and the final policy has been determined.
            for (int c = 0; c < Const.NUM_COL; c++) {
                for (int r = 0; r < Const.NUM_ROW; r++) {
                    Cell currCell = maze.getCell(new Coordinates(c, r));

                    if (currCell.getCellAttribute() == CellAttribute.WALL) {
                        continue;
                    }

                    // evaluates the optimal action for the cell and updates the cell's action accordingly
                    // If the policyImprovement method returns true, indicates that the optimal action for the cell has changed
                    boolean changed = policyImprovement(currCell, maze);

                    // If any policy changes were made during this iteration, sets changeOrNot to true
                    if (changed) {
                        changeOrNot = true;
                    }
                }
            }

            System.out.println();
            System.out.printf("Any policy changes (true/false)? : %s\n", changeOrNot);
            System.out.println();
            // print out the maze for the current iteration
            System.out.printf("Updated Maze for Iteration %d:\n", iteration);
            maze.print();
            output.add(maze);
            if(changeOrNot == false){
                System.out.println("Optimal policy has been obtained!");
            }
            // increments the iteration counter
            iteration++;
        } while (changeOrNot); // runs until no policy changes were made during the previous iteration

        output.finalise();
    }

    /**
     The goal of policy evaluation is to update the utilities of each state in the environment given the current policy.

     The Bellman equation for the utility of a state s is: U(s) = R(s) + gamma * sum(P(s, a, s') * U(s')), where:
        U(s) is the utility of state s
        R(s) is the reward for being in state s
        gamma is the discount factor 0.99
        P(s, a, s') is the probability of transitioning from state s to state s' when taking action a
        sum(P(s, a, s') * U(s')) is the expected utility of the next state s' given the current state s and action a.
     */
    private static void policyEvaluation(Maze maze, int k) {
        // iterates k times over all states and updates the utility of each state based on the current policy
        for (int i = 0; i < k; i++) {
            for (int column = 0; column < Const.NUM_COL; column++) {
                for (int row = 0; row < Const.NUM_ROW; row++) {
                    // get current reward & policy
                    Cell curCell = maze.getCell(new Coordinates(column, row));

                    // If the current state is a wall, utility is not updated
                    if (curCell.getCellAttribute() == CellAttribute.WALL)
                        continue;

                    // For each non-wall state, the algorithm sums up the expected utilities of its
                    // neighbours (UP, LEFT, RIGHT) based on the current policy
                    Cell[] neighbours = maze.getNeighboursOfCell(curCell);
                    double up = Const.PROBABILITY_UP * neighbours[0].getUtility();
                    double left = Const.PROBABILITY_LEFT * neighbours[1].getUtility();
                    double right = Const.PROBABILITY_RIGHT * neighbours[2].getUtility();

                    // updates the utility of the current state using the Bellman equation.
                    float reward = curCell.getCellAttribute().getReward();
                    curCell.setUtility(reward + Const.DISCOUNT_FACTOR * (up + left + right));
                }
            }
        }
    }

    /**
     The goal of policy improvement is to improve the policy until we reach an optimal policy that maximizes
     the expected total reward for the agent.
     In this implementation, we update the policy of each non-wall cell in the maze based on the results of
     policy evaluation. This process continues until the policy is converged and no further changes are required.
     */
    private static boolean policyImprovement(Cell curCell, Maze maze) {
        // holds the maximum possible sub-utility for each of the directions (i.e. up, down, left, right)
        double[] maxSubUtility = new double[Coordinates.ALL_DIRECTIONS];

        // go through each direction and calculates the sub-utility for that direction based on the current policy
        for (int direction = 0; direction < Coordinates.ALL_DIRECTIONS; direction++) {
            Cell[] neighbouringCells = maze.getNeighboursOfCell(curCell, direction);
            double up = Const.PROBABILITY_UP * neighbouringCells[0].getUtility();
            double left = Const.PROBABILITY_LEFT * neighbouringCells[1].getUtility();
            double right = Const.PROBABILITY_RIGHT * neighbouringCells[2].getUtility();

            maxSubUtility[direction] = up + left + right;
        }

        // holds the neighbouring cells of curCell
        Cell[] neighbours = maze.getNeighboursOfCell(curCell);
        // sub-utility for the current policy is calculated based on the neighbouring cells and their utilities
        double up = Const.PROBABILITY_UP * neighbours[0].getUtility();
        double left = Const.PROBABILITY_LEFT * neighbours[1].getUtility();
        double right = Const.PROBABILITY_RIGHT * neighbours[2].getUtility();

        // holds the direction with the highest sub-utility value
        int maxSubUtilityDirection = 0;
        for (int m = 1; m < maxSubUtility.length; m++) {
            if (maxSubUtility[m] > maxSubUtility[maxSubUtilityDirection])
                maxSubUtilityDirection = m;
        }

        // holds the sub-utility value for the current policy
        double curSubUtility = up + left + right;

        // if the sub-utility value for the new policy is greater than the sub-utility value for the current policy
        if (maxSubUtility[maxSubUtilityDirection] > curSubUtility) {
            // the action for curCell is updated to the direction with the highest sub-utility
            curCell.setAction(maxSubUtilityDirection);
            return true;
        } else {
            return false;
        }
    }
}
