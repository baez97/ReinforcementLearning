package algorithms;

import java.util.ArrayList;
import java.util.Collection;

import utils.Utils;
import learning.Action;
import learning.LearningAlgorithm;
import learning.State;

/** 
*  This algorithm is a dummy/demo one. The returned
*  policy consists of choosing actions randomly.
*/
public class RandomAlgorithm extends LearningAlgorithm{
	
    /** Assigns the actions randomly */
	@Override
	protected void learnPolicy() {
		// Gets all the states.
		Collection<State> states = problem.getAllStates();	
		// Assigns the action to each state.
		for (State state: states){	
			// Extracts the possible actions that can be applied to the state.
			ArrayList<Action> possibleActions = problem.getPossibleActions(state);
			// Randomly selects the index of one of such actions
			int selActionIdx = Utils.random.nextInt(possibleActions.size());
			// Assigns the corresponding action to the state.
			solution.setAction(state, possibleActions.get(selActionIdx));
		}
	}
    
	/** Assigns parameters for the algorithm */
	@Override
	public void setParams(String[] args) {
		// This algorithm does not need parameters.
	}
}
