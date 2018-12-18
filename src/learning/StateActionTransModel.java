package learning;

import utils.Utils;

/** 
 * This class represents an entry of the transition model, i.e., the values of 
 * the transition model for a pair state-action. For instance:
 * 
 *     T(S1,a,S2)=0.2
 *     T(S1,a,S3)=0.8
 * 
 * An object StateActionTransModel would represent this information, corresponding 
 * to the transition model for S1 when applying a. Therefore:
 * 
 *     reachableStates = [S2, S3]
 *     probs = [0.2, 0.8]
 * 
 * Notice that the whole transition model is composed by a set of instances of
 * this class, one for each pair state-action. In this example case, we could 
 * have something like:
 * 
 * StateActionTransModel S1_a = new StateActionTransModel(reachableStates, probs);
 * 
 */
public class StateActionTransModel{
	
	/* Reachable states from a certain state when applying a certain action. */
	private State[] reachableStates;
	
	/* Probability of reaching each state. */
	private double[] probs;
	
	// Utility methods
	
	/** 
	 * Randomly generates the next state considering the entry for
	 * the transition model, corresponding to a pair state-action, 
	 * represented by the object. 
	 */
	public State genNextState(){		
		// Generates a random position given this transition model. 
		double randUniform = Utils.random.nextDouble();
		int selected = 0;
		double cumProbabilities = probs[selected];
		while (randUniform>cumProbabilities){
			cumProbabilities += probs[++selected];
		}		
		// Returns the corresponding state.
		return reachableStates[selected];
	}

	/** 
	 * Creates the table with the reachable states and the probabilities.
	 */
	public StateActionTransModel(State[] reachableStates, double[] probs){		
		// The sizes of both vectors must be similar.
		assert reachableStates.length != probs.length: 
			"The number of reachable states does not correspond with the vector of probabilities";		
		// The sum of the probabilities must be one.
		double cumProbabilities = 0;
		for (int pInd=0;pInd<probs.length;pInd++)
			cumProbabilities += probs[pInd];	
		assert cumProbabilities != 1.0: 
			"The sum of the probability vector must be equal to 1.";		
		// Saves the states and probabilities.
		this.reachableStates = reachableStates;
		this.probs = probs;		
	}
	
	/** Returns the probabilities. */
	public double[] getProbs(){ return probs; }
	
	/** Returns the reachable states. */
	public State[] getReachableStates(){ return reachableStates; }

	/**
	 * Prints the entry of the transition model represented by the object.
	 */
	public String toString(){
		String output = "";
		int numStates = reachableStates.length;
		for (int i=0;i<numStates;i++){
			output = output+reachableStates[i]+" -> "+probs[i]+"\n";
		}
		return output;
	}
}
