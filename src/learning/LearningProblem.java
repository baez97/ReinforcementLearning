package learning;

import learning.State;
import utils.Utils;
import learning.Action;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This abstract class must be extended by all classes implementing reinforcement learning problems.
 */
public abstract class LearningProblem{
	
	/* Gamma value for reward discounting. (Can be changed). */
	public double gamma = 1;
	
	/* In some cases, there can be an initial state. */
	protected State initialState;	
	
	// Methods related with the problem definition
	
	/** Returns the initial state. */
	public State initialState() { return initialState; }
	
	/** Returns true if the state is final. */
	public abstract boolean isFinal(State state);	
	
	/** Returns the set of actions that can be applied to an state */
	public abstract ArrayList<Action> getPossibleActions(State state);
	
	/** Returns the state resulting of applying an action to a certain state. */ 
	public abstract State applyAction(State state, Action action);
	
	/** Returns the reward associated to an state. */
	public abstract double getReward(State state);
	
	/**  Returns the reward resulting of applying an action to an state.	 */
	public abstract double getTransitionReward(State fromState, Action action, State toState);

	// Utility methods
	
	/** Allows setting the initial state. Sometimes this function must be overloaded. */
	public void setInitialState(State state){ initialState = state; }
	
	/** Sets the value for gamma. */
	public void setGamma(double gamma){ this.gamma = gamma; }	
	
	/** Returns a random action. */
	public Action randomAction(State state) {
		ArrayList<Action> pActions = getPossibleActions(state);
		return pActions.get(Utils.random.nextInt(pActions.size()));
	}
	
	/** Generates and returns a random state */
	public abstract State getRandomState();	
	
	/** Sets parameters of the problem if necessary */
	public abstract void setParams(String[] params);
	
	/** Creates an instance of the problem given its name and parameters.*/
	public static LearningProblem generateProblem(String problemName, String[] params){
		try{
			@SuppressWarnings("unchecked")
			Class<LearningProblem> problemClass = (Class<LearningProblem>) Class.forName("problems."+problemName);
			LearningProblem problem = problemClass.newInstance();
			problem.setParams(params);
			return problem;
		}
		catch (Exception E){
			System.out.println("The problem "+problemName+" can't be built.");
			System.exit(-1);
		}
		return null;
	}
}

