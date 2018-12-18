package algorithms.mdp;

import java.util.HashMap;

import learning.*;

public class PolicyIteration extends LearningAlgorithm {
	/** Max delta. Controls convergence.*/
	private double maxDelta = 0.01;
	
	/** 
	 * Learns the policy (notice that this method is protected, and called from the 
	 * public method learnPolicy(LearningProblem problem, double gamma) in LearningAlgorithm.
	 */	
	@Override
	protected void learnPolicy() {
		if (!(problem instanceof MDPLearningProblem)){
			System.out.println("The algorithm PolicyIteration can not be applied to this problem (model is not visible).");
			System.exit(0);
		}
		
		// Initializes the policy randomly
	    solution = new Policy();
		Policy policyAux=null;
		 //****************************/
		 //
		 // TO DO
		 // 
		 // 
		 //***************************/
		
		// Main loop of the policy iteration.
		 //****************************/
		 //
		 // TO DO
		 // 
		 // 
		 //***************************/
	}
		
	
	/** 
	 * Policy evaluation. Calculates the utility given the policy 
	 */
	private HashMap<State,Double> policyEvaluation(Policy policy){
		
		// Initializes utilities. In case of terminal states, the utility corresponds to
		// the reward. In the remaining (most) states, utilities are zero.		
		HashMap<State,Double> utilities = new HashMap<State,Double>();
		 //****************************/
		 //
		 // TO DO
		 // 
		 // 
		 //***************************/
		return utilities;
	}

	/** 
	 * Improves the policy given the utility 
	 */
	private Policy policyImprovement(HashMap<State,Double> utilities){
		// Creates the new policy
		Policy newPolicy = new Policy();
		 //****************************/
		 //
		 // TO DO
		 // 
		 // 
		 //***************************/
		return newPolicy;
	}
	
	/** 
	 * Sets the parameters of the algorithm. 
	 */
	@Override
	public void setParams(String[] args) {
		// In this case, there is only one parameter (maxDelta).
		if (args.length>0){
			try{
				maxDelta = Double.parseDouble(args[0]);
			} 
			catch(Exception e){
				System.out.println("The value for maxDelta is not correct. Using 0.01.");
			}	
		}	
	}
	
	/** Prints the results */
	public void printResults(){
		System.out.println("Policy Iteration");
		// Prints the policy
		System.out.println("\nOptimal policy");
		System.out.println(solution);
	}	
	
	/** Main function. Allows testing the algorithm with MDPExProblem */
	public static void main(String[] args){
		LearningProblem mdp = new problems.mdpexample2.MDPExProblem();
		mdp.setParams(null);
		PolicyIteration pi = new PolicyIteration();
		pi.setProblem(mdp);
		pi.learnPolicy(mdp);
		pi.printResults();
	}	
	
}
