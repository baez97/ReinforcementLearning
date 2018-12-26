package problems.maze;

import java.util.ArrayList;
import java.util.Collection;

import learning.*;
import visualization.*;
import utils.*;

/**
 * Implements the maze problem as a model free problem.
 */
public class MazeProblemMF extends MFLearningProblem implements MazeProblem, ProblemVisualizable {

    /**
     * Size of the problem. Default value is 10.
     */
    protected int size = 10;

    /**
     * Maze
     */
    protected Maze maze;

    /**
     * Constructors
     */
    public MazeProblemMF() {
        this.maze = new Maze(size, 0);
        initialState = new MazeState(maze.posHamster.x, maze.posHamster.y);
    }

    public MazeProblemMF(int size) {
        this.size = size;
        this.maze = new Maze(size, 0);
    }

    public MazeProblemMF(int size, int seed) {
        this.size = size;
        this.maze = new Maze(size, seed);
        initialState = new MazeState(maze.posHamster.x, maze.posHamster.y);
    }

    public MazeProblemMF(Maze maze) {
        this.size = maze.size;
        this.maze = maze;
        initialState = new MazeState(maze.posHamster.x, maze.posHamster.y);
    }

    /**
     * Generates a random instance of the problem given the seed.
     */
    private void generateInstance(int size, int seed) {
        this.size = size;
        this.maze = new Maze(size, seed);
        initialState = new MazeState(maze.posHamster.x, maze.posHamster.y);
    }

    /**
     * Returns a reference to the maze
     */
    public Maze getMaze() {
        return this.maze;
    }

    // Inherited from Learning Problem
    /**
     * Whether the state corresponds to a final state (CAT or CHEESE).
     */
    @Override
    public boolean isFinal(State state) {
        //
        // COMPLETAR
        // 
        MazeState mazeState = (MazeState) state;
        boolean cheese = mazeState.position.equals(this.maze.posCheese);
        boolean cat = this.maze.posCats.contains(mazeState.position);

        return cheese || cat;
    }

    /**
     * Returns the set of actions that can be done at each step.
     */
    @Override
    public ArrayList<Action> getPossibleActions(State state) {
        MazeState mazeState = (MazeState) state;
        ArrayList<Action> possibleActions = new ArrayList<Action>();
        //
        // COMPLETAR
        // 

        possibleActions.add(MazeAction.UP);
        possibleActions.add(MazeAction.RIGHT);
        possibleActions.add(MazeAction.DOWN);
        possibleActions.add(MazeAction.LEFT);
        
        if ( this.maze.holeList.contains(mazeState.position) ) {
            possibleActions.add(MazeAction.DIVE);
        }

        // Returns the actions.
        return possibleActions;
    }

    /**
     * Returns the reward of an state.
     */
    @Override
    public double getReward(State state) {
        double reward = 0;

        //
        // COMPLETAR
        // 
        
        MazeState mazeState = (MazeState) state;
        boolean cheese = mazeState.position.equals(this.maze.posCheese);
        boolean cat    = this.maze.posCats.contains(mazeState.position);
        
        if ( cheese ) {
            return  100.0;
        } else if ( cat ) {
            return -100.0;
        }
        
        // Otherwise returns 0
        return 0;
    }

    /**
     * In this case, the transition reward penalizes distance.
     */
    @Override
    public double getTransitionReward(State fromState, Action action, State toState) {
        //
        // COMPLETAR
        // 
        
        int toX,toY;
        toX = ((MazeState) toState).X();
        toY = ((MazeState) toState).Y();	

        // Si no hay transición, devuelve 0
        if ((fromState==null) || (toState==null))
            return 0;

        // Primero penaliza la distancia
        int fromX,fromY;
        fromX = ((MazeState) fromState).X();
        fromY = ((MazeState) fromState).Y();

        // Calcula la distancia euclediana (Teorema de Pitágoras)
        double distancia = Math.sqrt(Math.pow(fromX-toX, 2) + Math.pow(fromY-toY, 2));

        // Calcula la recompensa.
        double recompensa = -1*distancia;

        // Si el estado actual es agua, la recompensa se duplica
        if (maze.cells[fromX][fromY]==Maze.WATER)
                recompensa = recompensa*2;

        // Si es un tunel y la acción es DIVE, la recompensa es la mitad
        if (action==MazeAction.DIVE)
                recompensa = recompensa*0.5;

        // Devuelve la recompensa
        return recompensa;
    }

    /**
     * Generates the transition model for a certain state in this particular
     * problem. This method is PRIVATE.
     */
    private StateActionTransModel mazeTransitionModel(State state, Action action) {
        // Structures contained by the transition model.
        State[] reachable;
        double[] probs;

        // Coordinates of the current state
        int fromX, fromY;
        fromX = ((MazeState) state).X();
        fromY = ((MazeState) state).Y();

        /* First considers diving. */
        if (action == MazeAction.DIVE) {
            // It must be a hole. Gets the outputs.
            Position inputHolePos = new Position(fromX, fromY);
            // It considers all holes but one
            reachable = new State[maze.numHoles - 1];
            probs = new double[maze.numHoles - 1];
            int holeNum = 0;
            for (Position holePos : maze.holeList) {
                if (holePos.equals(inputHolePos)) {
                    continue;
                }
                reachable[holeNum] = new MazeState(holePos);
                probs[holeNum++] = 1.0 / (maze.numHoles - 1);
            }
            // Returns 
            return new StateActionTransModel(reachable, probs);
        }

        // Creates the transition model.
        reachable = new State[4];
        probs = new double[4];

        // Probability of error 0.1 times each position.
        double probError = 0.1;
        double probSuccess = 1.0 - probError * (4 - 1);

        int ind = 0;
        //
        // Cell X,Y-1
        //
        // Probability
        if (action == MazeAction.UP) {
            probs[ind] = probSuccess;
        } else {
            probs[ind] = probError;
        }
        // Reached state
        if ((fromY > 0) && (maze.cells[fromX][fromY - 1] != Maze.WALL)) {
            reachable[ind] = new MazeState(fromX, fromY - 1); // Can move
        } else {
            reachable[ind] = new MazeState(fromX, fromY); // Can't move
        }

        ind++;
        //
        // Cell X,Y+1
        //
        // Probability
        if (action == MazeAction.DOWN) {
            probs[ind] = probSuccess;
        } else {
            probs[ind] = probError;
        }
        // Reached state
        if ((fromY < maze.size - 1) && (maze.cells[fromX][fromY + 1] != Maze.WALL)) {
            reachable[ind] = new MazeState(fromX, fromY + 1); // Can move
        } else {
            reachable[ind] = new MazeState(fromX, fromY); // Can't move
        }

        ind++;
        //
        // Cell X-1,Y
        //
        // Probability
        if (action == MazeAction.LEFT) {
            probs[ind] = probSuccess;
        } else {
            probs[ind] = probError;
        }
        // Reached state
        if ((fromX > 0) && (maze.cells[fromX - 1][fromY] != Maze.WALL)) {
            reachable[ind] = new MazeState(fromX - 1, fromY); // Can move
        } else {
            reachable[ind] = new MazeState(fromX, fromY); // Can't move
        }

        ind++;
        //
        // Cell X,Y+1
        //
        // Probability
        if (action == MazeAction.RIGHT) {
            probs[ind] = probSuccess;
        } else {
            probs[ind] = probError;
        }
        // Reached state
        if ((fromX < maze.size - 1) && (maze.cells[fromX + 1][fromY] != Maze.WALL)) {
            reachable[ind] = new MazeState(fromX + 1, fromY); // Can move
        } else {
            reachable[ind] = new MazeState(fromX, fromY); // Can't move
        }

        // Returns 
        return new StateActionTransModel(reachable, probs);
    }

    // From MFLearningModel
    /**
     * Updates the environment.
     */
    @Override
    public void updateEnvironment(State state, Action action) {
        //
        // COMPLETAR
        //
        
        // No tiene que hacer nada puesto que en este caso el agente
        // no modifica el entorno
    }

    /**
     * Generates the new state.
     */
    @Override
    public State readNewState(State state, Action action) {
        //
        // COMPLETAR
        // 
        
        StateActionTransModel model = mazeTransitionModel(state, action);
        return model.genNextState();
    }

    // Utilities
    /**
     * Returns a random state.
     */
    @Override
    public State getRandomState() {
        // Returns only positions corresponding to empty cells.
        int posX, posY;
        boolean validCell = false;
        do {
            posX = Utils.random.nextInt(size);
            posY = Utils.random.nextInt(size);
            // Walls are not valid states. 
            if (maze.cells[posX][posY] == Maze.WALL) {
                continue;
            }
            // Sometimes (not very often) there are empty cells surrounded
            // by walls or by the limit of the maze.  Test that there is at least
            // an adjacent position to move.
            if (posX > 0 && maze.cells[posX - 1][posY] == Maze.EMPTY) {
                validCell = true;
            }
            if (posX < maze.size - 1 && maze.cells[posX + 1][posY] == Maze.EMPTY) {
                validCell = true;
            }
            if (posY > 0 && maze.cells[posX][posY - 1] == Maze.EMPTY) {
                validCell = true;
            }
            if (posY > maze.size - 1 && maze.cells[posX][posY + 1] == Maze.EMPTY) {
                validCell = true;
            }
        } while (!validCell);
        return new MazeState(posX, posY);
    }

    /**
     * Generates and instance of the problem.
     */
    @Override
    public void setParams(String[] params) {
        try {
            if (params.length == 1) {
                generateInstance(Integer.parseInt(params[0]), 0);
            } else {
                generateInstance(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
            }
        } catch (Exception E) {
            System.out.println("There has been an error while generating the na new instance of MazeProblem.");
        }
    }

    /* Visualization */
    /**
     * Returns a panel with the view of the problem.
     */
    public ProblemView getView(int sizePx) {
        MazeView mazeView = new MazeView(this, sizePx);
        return mazeView;
    }

    /* Test */
    public static void main(String[] args) {
        MazeProblemMF mazeProblem = new MazeProblemMF(15, 5);
//		ProblemVisualization mainWindow = new ProblemVisualization(mazeProblem,600);		// Main window

        State currentState = mazeProblem.initialState();
        System.out.println("Current state: " + currentState);
        System.out.println("Reward: " + mazeProblem.getReward(currentState));
        System.out.println("Is final: " + mazeProblem.isFinal(currentState));

        System.out.println("Possible actions: ");
        ArrayList<Action> actions = mazeProblem.getPossibleActions(currentState);
        for (Action action : actions) {
            System.out.println("\t" + action);
        }

        System.out.println("Apply action UP.\n");
        State newState = mazeProblem.applyAction(currentState, MazeAction.UP);
        System.out.println("New state: " + newState);
        System.out.println("Transition reward:" + mazeProblem.getTransitionReward(currentState, MazeAction.UP, newState));
    }

}
