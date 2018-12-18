package algorithms.mdp;

import java.util.HashMap;
import java.util.Map.Entry;

import learning.*;

/** 
 * Implements the value iteration algorithm for Markov Decision Processes 
 */
public class ValueIteration extends LearningAlgorithm {
	
	/** Stores the utilities for each state */;
	private HashMap<State, Double> utilities;
	
	/** Max delta. Controls convergence.*/
	private double maxDelta = 0.01;
	
	/** 
	 * Learns the policy (notice that this method is protected, and called from the 
	 * public method learnPolicy(LearningProblem problem, double gamma) in LearningAlgorithm.
	 */
	@Override
	protected void learnPolicy() {
		// This algorithm only works for MDPs
		if (!(problem instanceof MDPLearningProblem)){
			System.out.println("The algorithm ValueIteration can not be applied to this problem (model is not visible).");
			System.exit(0);
		}
		
		 //****************************/
		 //
		 // TO DO
		 // 
		 // 
		 //***************************/
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
		// Prints the utilities.
		System.out.println("Value Iteration\n");
		System.out.println("Utilities");
		for (Entry<State,Double> entry: utilities.entrySet()){
			State state = entry.getKey();
			double utility = entry.getValue();
			System.out.println("\t"+state +"  ---> "+utility);
		}
		// Prints the policy
		System.out.println("\nOptimal policy");
		System.out.println(solution);
	}
	
	
	/** Main function. Allows testing the algorithm with MDPExProblem */
	public static void main(String[] args){
		LearningProblem mdp = new problems.mdpexample2.MDPExProblem();
		mdp.setParams(null);
		ValueIteration vi = new ValueIteration();
		vi.setProblem(mdp);
		vi.learnPolicy(mdp);
		vi.printResults();
	
	}

}
