package learning;

/** 
 * This class extends Learning Problem to implement model free problems, where
 * the transition model is not known, the environment can change, and the new 
 * state is read. 
 * 
 * For instance, in the well known PacMan game, the agent applies an action (moves)
 * and while it moves, so do the ghosts. The result of its action depends on the
 * environment, because it is possible that it moves to an empty position and, at the
 * same time, the ghost reaches such position. 
 */
public abstract class MFLearningProblem extends LearningProblem {

	/** 
	 *  Returns the state resulting of applying an action to a certain state. 
	 *  
	 *  Problems of this class update the environment (both things that depend
	 *  and do not depend on the agent) and read the new state.
     */ 
	public State applyAction(State state, Action action){
		// Updates the environment. 
		updateEnvironment(state, action);
		// Reads the new state.
		return readNewState(state, action);
	}

	/** Updates the environment. Sometimes it depends on the state and the action. */
	public abstract void updateEnvironment(State state, Action action);
	
	/** Reads the new state*/
	public abstract State readNewState(State state, Action action);
	
}
	

