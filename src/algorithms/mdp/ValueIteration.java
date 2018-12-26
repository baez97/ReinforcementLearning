package algorithms.mdp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import learning.*;

/** 
 * Implements the value iteration algorithm for Markov Decision Processes 
 */
public class ValueIteration extends LearningAlgorithm {
	
	/** Stores the utilities for each state */;
        private HashMap<State, Double> utilities, utilitiesAux;

	
	/** Max delta. Controls convergence.*/
	private double maxDelta = 0.01;
	
	/** 
	 * Learns the policy (notice that this method is protected, and called from the 
	 * public method learnPolicy(LearningProblem problem, double gamma) in LearningAlgorithm.
	 */
	/** Learns the policy */
	@Override
	protected void learnPolicy() {
            // This algorithm only works for MDPs
            if (!(problem instanceof MDPLearningProblem))
            {
                System.out.println("The algorithm ValueIteration can not be applied to this problem (model is not visible).");
                System.exit(0);
            } 
            
            //****************************/
            // TO DO
            //***************************/
            
            MDPLearningProblem MDPproblem = ((MDPLearningProblem) problem);
            double delta;
            utilities = new HashMap<State,Double>();
            utilitiesAux = new HashMap<State,Double>();

            // iniciamos la utilidad
            for (State state : MDPproblem.getAllStates()) {
                if (MDPproblem.isFinal(state))
                    utilities.put(state, MDPproblem.getReward(state));
                else
                    utilities.put(state,0.0);
            }

            do {
                delta = 0.0;
                for(State state : utilities.keySet()) {
                    if (!MDPproblem.isFinal(state)) {
                        // U'(s) <- R(s) + γ * max[a] ( SUM[s'] T(s,a,s')U(s') )
                        Action auxAction = argMaxAction(MDPproblem.getPossibleActions(state),state,utilities);
                        double auxUtility = (MDPproblem).getExpectedUtility(state,auxAction,utilities,MDPproblem.gamma);
                        solution.setAction(state, auxAction);
                        utilitiesAux.put(state, auxUtility);

                        // if |U'(s) - U(s)| > δ then
                        //    δ <- |U'(s) - U(s)|
                        double operation = Math.abs(utilitiesAux.get(state) - utilities.get(state));
                        if (operation > delta)
                            delta = operation;
                    }
                }
                // U <- U'
                utilities.putAll(utilitiesAux);
            }
            while(delta > maxDelta*(1-MDPproblem.gamma)/MDPproblem.gamma); // Para cuando hay convergencia
            //****************************/

            // Prints the utilities.
            System.out.println("Value Iteration: Utilities");
            for (Entry<State,Double> entry: utilities.entrySet())
            {
                State state = entry.getKey();
                double utility = entry.getValue();
                System.out.println("\t"+state +"  ---> "+utility);
            }
	}

	// γ * max[a] ( SUM[s'] T(s,a,s')U(s') )
	public Action argMaxAction(ArrayList<Action> actions, State state, HashMap<State,Double> utilities)
	{
            double max = Double.NEGATIVE_INFINITY;
            double auxMax = max;
            Action bestAction = null;

            for(Action action : actions) {
                auxMax = ((MDPLearningProblem) problem).getExpectedUtility(state,action,utilities,problem.gamma);
                if (auxMax > max) {
                    max = auxMax;
                    bestAction = action;
                }
            }
            return bestAction;
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
