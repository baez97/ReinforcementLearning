package algorithms.qlearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import learning.Action;
import learning.LearningProblem;
import learning.MDPLearningProblem;
import learning.Policy;
import learning.State;
import utils.Utils;

/** 
 * This class allows storing and managing the values for Q(s,a)
 */
public class QTable {
	
	/** Reference to the problem. */
	LearningProblem problem;
	
	/** Contains the main table. For each state, there is a map
	 *  with the qvalues corresponding to each action. */
	private HashMap<State, HashMap<Action, Double>> table;
	
	/** Constructor. Constructs an empty table.*/
	public QTable(){
		table = new HashMap<State, HashMap<Action, Double>>();
	}
	
	/** This constructor builds a table, assuming that all states of the problem are known. */
	public QTable(LearningProblem problem){
		this.problem = problem;
		
		// Creates the qtable.		
		table = new HashMap<State, HashMap<Action, Double>>();
		
		// For MDPLearningProblem problems, the states are known a priori, and can be initialized.
		if (problem instanceof MDPLearningProblem)
			// Adds an empty entry for each state.
			for (State state: ((MDPLearningProblem)problem).getAllStates()){
				table.put(state,new HashMap<Action, Double>());
				// Creates the entry for each action and initializes the q(s,a) to 0
				ArrayList<Action> possibleActions = problem.getPossibleActions(state);
				for (Action action: possibleActions)
					table.get(state).put(action, 0.0);
			}
	}
	
	/** Test whether the table contains the entries corresponding to a given state. */
	public boolean contains(State state){
		return table.containsKey(state);
	}
	
	/** Test whether the table contains an entry for a given pair state-action. */
	public boolean contains(State state, Action action){
		return table.containsKey(state) && table.get(state).containsKey(action);
	}
	
	/** Sets a value in an entry of the table: Q(state,action)=value. */
	public void setQValue(State state, Action action, double value){
		// If the table contains the state, assigns the value.
		if (table.containsKey(state))
			table.get(state).put(action, value);
		// Otherwise creates the entry for each action and puts the value.
		else{
			 table.put(state,new HashMap<Action, Double>());
			 ArrayList<Action> possibleActions = problem.getPossibleActions(state);
			 for (Action otheraction: possibleActions)
				 if (otheraction!=action)
					table.get(state).put(otheraction, 0.0);			 
			 table.get(state).put(action, value);
		}
	}	
	
	/** Gets the value in an entry of the table, Q(state,action).
	 *  If the entry does not exist, return 0.*/
	public double getQValue(State state, Action action){
		if (this.contains(state,action))
			return table.get(state).get(action);
		else 
			return 0;
	}	
	
	/** Returns the action that maximizes Q(state,action) given the state.*/
	public Action getActionMaxValue(State state){
		// If there is no entry for the state, returns null.
		if (!table.containsKey(state) || table.get(state).isEmpty())
			return null;
		// If there is, looks for the best action.
		Action bestAction=null;
		double bestValue=Double.NEGATIVE_INFINITY;
		for (Entry<Action,Double> actionValue: table.get(state).entrySet()){
			// If the action is the best, updates
			if (actionValue.getValue()>bestValue){
				bestAction = actionValue.getKey();
				bestValue = actionValue.getValue();
			}
			// If it is equal to the best, updates randomly.
			else if (actionValue.getValue()==bestValue && Utils.random.nextFloat()<0.5) {
				bestAction = actionValue.getKey();
				bestValue = actionValue.getValue();				
			}
		}
		// Returns the corresponding action.
		return bestAction;
	}
	
	/** Returns the maximum value q(state,action) for a state. */
	public double getMaxQValue(State state){
		Action bestAction = getActionMaxValue(state);
		if ((bestAction==null) || !(contains(state, bestAction)))
			return 0;
		else
			return table.get(state).get(bestAction);
	}
	
	/** Generates policy from the values in the Qtable */
	public Policy generatePolicy(){
		Policy policy = new Policy();
		// For each state selects the action with the maximum Q(s,a) value.
		for (State state: table.keySet()){
			Action action = getActionMaxValue(state);
			policy.setAction(state, action);
		}
		return policy;
	}
	
	/** Allows printing the table. */
	public String toString(){
		String output = "";
		// Gets the states in the table.
		Set<State> states = table.keySet();
		// Prints each one. 
		for (State state: states){
			output += state.toString()+" --> ";
			for (Entry<Action,Double> actionValue: table.get(state).entrySet()){
				output+= "\t "+actionValue.getKey().getId()+" ("+actionValue.getValue()+")";
			}
			output += "\n";
		}
		return output;
	}
	
	/** Main function. Allows testing the class. */	
	public static void main(String[] args) {
		QTable qtable = new QTable();
		
		State state = new problems.maze.MazeState(1,3);
		qtable.setQValue(state, problems.maze.MazeAction.DOWN, 1.2);
		qtable.setQValue(state, problems.maze.MazeAction.UP, 2);
		
	    state = new problems.maze.MazeState(1,2);
		qtable.setQValue(state, problems.maze.MazeAction.LEFT, 3);
		qtable.setQValue(state, problems.maze.MazeAction.RIGHT, 2.1);

		System.out.println(qtable);
				
	    System.out.println(state +" --->  MaxQ: "+qtable.getMaxQValue(state)+". Action: "+qtable.getActionMaxValue(state));		
	}		

}
