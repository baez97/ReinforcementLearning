package learning;

import java.util.Collection;
import java.util.HashMap;

/** 
 * This class extends the learning problem to provide access to the underlying 
 * transition model so that it can be solved as a Markov Decision Process. 
 */
public abstract class MDPLearningProblem extends LearningProblem {
	
	// Abstract function that must be implemented in the problems. 
	
	/** 
	 * Returns a collection with all the states of the problem. In cases where 
	 * it is not possible, returns null.
	 */
	public abstract Collection<State> getAllStates();		
	
	/** Returns the entry of the transition model for a pair state/action */ 
	public abstract StateActionTransModel getTransitionModel(State state, Action action);
	
	// Other methods already implemented. 
	
	/** 
	 * Applies an action to an state (returns a random state given the transition model
	 * for the pair state-action). This function is common for all MDP problems, therefore, 
	 * can be implemented here. 
	 */
	@Override
	public State applyAction(State state, Action action) {
		StateActionTransModel model = getTransitionModel(state, action);
		return model.genNextState();
	}		
	
	// Utility methods
	
	/** 
	 * Calculates the expected utility for an state-action given the utilities of all states in the problem.  
	 */
	public double getExpectedUtility(State state, Action action, HashMap<State, Double> utilities, double gamma){
		// Extracts the transition model for the state-action (reachable states and probability of reaching them).
		StateActionTransModel transModel = getTransitionModel(state, action);
		State[] reachableStates = transModel.getReachableStates();
		double[] probs = transModel.getProbs();
		
		// Calculates the expected utility. The reward not only depends on the state, but also on the action.
		
		double utility = getReward(state); //	U<s> = R<s> 	
		// Calculates the expected utility
		for (int stateIdx=0;stateIdx<reachableStates.length;stateIdx++){  
			// U<s> +=   gamma *    T<s,a,s'>    * (  R <s,a,s'> + U<s'> )
			utility +=   gamma * probs[stateIdx] *(getTransitionReward(state, action,reachableStates[stateIdx]) + utilities.get(reachableStates[stateIdx]));	
		}	 
		return utility;
	}
}
