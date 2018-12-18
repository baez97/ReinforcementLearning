import java.util.Arrays;

import learning.*;
import visualization.*;

/** Utility class. Solves a search problem with a learning algorithm and shows the results. */
public class Solver {
	
	/* Maximum number of actions allowed. */ 
	public static int maxSteps=500;
	
	/* Number of evaluations required to evaluate a policy. */
	public static int numEvaluations = 10000;
	
	/** 
	 * Applies the policy to the problem and returns the utility.
	 */
	public static double applyPolicy(LearningProblem problem, State initialState, Policy policy, double gamma){
		State currentState, newState;
		Action nextAction;
		double utility; // Total utility
		double powGamma = 1.0; // Power of gamma.
		int step=0;
		// Fixes the initial state.
		currentState = initialState;
		// Adds R_0. In some problems, it is possible to obtain the utility of the initial state like this.
		utility = problem.getReward(currentState); 
		// While the currentState is not final moves.
		while (!problem.isFinal(currentState) && step<maxSteps){
			nextAction = policy.getAction(currentState);
			newState = problem.applyAction(currentState, nextAction);
			// System.out.println(currentState +" "+nextAction+"-->\t"+newState);
			powGamma = powGamma * gamma;
			// Adds boths de rewards of the state and transition.
			utility = utility + powGamma*(problem.getReward(newState) + problem.getTransitionReward(currentState, nextAction, newState));
			// Updates the current state.
			currentState = newState;
			// New step
			step++;
		}
		return utility;
	}
	
	/** 
	 * Applies the policy to the problem and returns the utility.
	 */
	public static double evalPolicy(LearningProblem problem, Policy policy, double gamma){
		double averageUtility = 0;
		for (int it=0;it<numEvaluations;it++){
			State initialState = problem.initialState();
			averageUtility += applyPolicy(problem,initialState, policy, gamma);
		}
		return averageUtility/numEvaluations;
	}
	
	public static void main(String[] args){
		// The first argument is the size of the window (0 means no window)
		int sizePx = Integer.parseInt(args[0]);
		
		// Separation mark between problem and algorithms ('--')
		int sep;
		for (sep=1;sep<args.length;sep++)
			if (args[sep].equals("--"))
				break;
		
		// Generates the problem. 
		String problemName = args[1];
		// Reads the gamma parameter
		double gamma = Double.parseDouble(args[2]);
		
		String[] problemParams = Arrays.copyOfRange(args, 3, sep);
		LearningProblem problem = LearningProblem.generateProblem(problemName, problemParams);
		problem.setGamma(gamma);

		// Generates the algorithm. 
		String algorithmName = args[sep+1];
		String[] algorithmParams = Arrays.copyOfRange(args, sep+2, args.length);
		LearningAlgorithm algorithm = LearningAlgorithm.generateAlgorithm(algorithmName, algorithmParams);

		// Learns the policy
		Policy policy = algorithm.learnPolicy(problem);
		System.out.println("Policy:\n"+policy);
		System.out.println("Policy size: "+policy.size());
		
		
		// Test the policy with the initial state provided in the algorithm. 
		// This can be useful for debugging and gain some view.
		State initialState = problem.getRandomState();
		double utility = applyPolicy(problem, initialState, policy, gamma);
		System.out.println("\nUtility starting in the initial state (one run): "+utility);
		
		// Shows the policy graphically if the problem provides this interface.
		if ((problem instanceof ProblemVisualizable) && sizePx>300) {
			ProblemVisualization visualization = new ProblemVisualization(problem, 600);
			visualization.visualizePolicy(policy);
		}
		
		// Evaluates the policy. Besides randomness, starts in different (random) states.
		double policyValue = evalPolicy(problem, policy, gamma);
		System.out.println("\n\nEvaluating policy: ");
		System.out.println("\t Average utility over "+Solver.numEvaluations+" executions: "+policyValue);
	}	
}
