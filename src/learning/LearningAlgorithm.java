package learning;

import learning.Policy;
import learning.LearningProblem;

/** 
 * All learning algorithms must extend this class.
 */
public abstract class LearningAlgorithm {

	/* Learning problem to be solved. */
	protected LearningProblem problem=null;
	
	/* Policy that will be returned as solution. */
	protected Policy solution = new Policy();
		
	// Methods related with search. 	
	
	/** 
	 * Learns and returns the policy. Calls the method learnPolicy(),
	 * that is specific for each algorithm. 
	 */
	public Policy learnPolicy(LearningProblem problem){
		// Resets the policy (in case the algorithm is executed several times).
		solution.reset();
		// Fixes the values for both problem and gamma
		setProblem(problem);
		// This is the main method that must be implemented.
		learnPolicy();
		// Returns the solution
		return solution;
	}		
	
	/** 
	 * This method must be implemented by each learning algorithm. Learns the 
	 * policy and stores it in the variable policy.  
	 */
	protected abstract void learnPolicy();	
	
	// Utility methods
	
	/** Sets the problem. */
	public void setProblem(LearningProblem problem){ this.problem = problem; }
	
	/** Fixes the parameters of the algorithm*/
	public abstract void setParams(String[] args);
	
	/** Creates an instance of the algorithm with the name and parameters passed as arguments*/
	public static LearningAlgorithm generateAlgorithm(String algorithmName, String[] params){
		try{
			@SuppressWarnings("unchecked")
			Class<LearningAlgorithm> algorithmClass = (Class<LearningAlgorithm>) Class.forName("algorithms."+algorithmName);
			LearningAlgorithm algorithm = algorithmClass.newInstance();
			algorithm.setParams(params);
			return algorithm;
		}
		catch (Exception E){
			System.out.println("The algorithm "+algorithmName+" can't be built.");
			System.exit(-1);
		}
		return null;
	}
}
