package algorithms.mdp;

import java.util.ArrayList;
import java.util.HashMap;
import utils.Utils;

import learning.*;

public class PolicyIteration extends LearningAlgorithm {
	/** Max delta. Controls convergence.*/
	private double maxDelta = 0.01;
	
	/** 
	 * Learns the policy (notice that this method is protected, and called from the 
	 * public method learnPolicy(LearningProblem problem, double gamma) in LearningAlgorithm.
	 */	
	@Override
        protected void learnPolicy()
        {
            /*
            function PolicyIteration(MDP< S,A,T,R,γ >)
                π' ← RandomInit(S)      Initializes states
                repeat
                    π  ← π'
                    U  ← PolicyEvaluation(MDP< S,A,T,R,γ >,π)
                    π' ← PolicyImprovement(MDP< S,A,T,R,γ >,U)
                until π = π'
                return π
             */

            if (!(problem instanceof MDPLearningProblem)){
                System.out.println("The algorithm PolicyIteration can not be applied to this problem (model is not visible).");
                System.exit(0);
            }
            solution = new Policy();
            Policy policyAux = new Policy();

            //****************************/
            // TO DO
            //***************************/
            HashMap<State,Double> utilities = new HashMap<State,Double>();
            policyAux = randomPolicy();
            do
            {
                solution = policyAux;
                utilities = policyEvaluation(solution);
                policyAux = policyImprovement(utilities);
            }
            while (!solution.equals(policyAux));
            //****************************/
        }

        /** Generates a random policy */
        public Policy randomPolicy()
        {
            Policy policy = new Policy();

            for(State state : ((MDPLearningProblem) problem).getAllStates())
            {
                ArrayList<Action> possibleActions = problem.getPossibleActions(state);
                int randomIndex = Utils.random.nextInt(possibleActions.size());
                if (!problem.isFinal(state))
                    policy.setAction(state, possibleActions.get(randomIndex));
            }

            return policy;
        }

        /** Calculates the utility given the policy */
        private HashMap<State,Double> policyEvaluation(Policy policy)
        {
            HashMap<State,Double> utilities = new HashMap<State,Double>();
            HashMap<State,Double> utilitiesAux = new HashMap<State,Double>();
            double delta;

            //****************************/
            // TO DO
            //***************************/
            utilities = new HashMap<State,Double>();
            utilitiesAux = new HashMap<State,Double>();

            // iniciamos la utilidad
            for (State state : ((MDPLearningProblem)problem).getAllStates())
            {
                if (problem.isFinal(state))
                    utilities.put(state, problem.getReward(state));
                else
                    utilities.put(state,0.0);
            }

            do
            {
                delta = 0.0;
                for(State state : utilities.keySet())
                {
                    if (!problem.isFinal(state))
                    {
                        // U'(s) <- R(s) + γ * SUM[s'] T(s,π(s),s')U(s')
                        Action auxAction = policy.getAction(state);
                        double utilityAux = ((MDPLearningProblem) problem).getExpectedUtility(state,auxAction,utilities,problem.gamma);
                        utilitiesAux.put(state, utilityAux);
                        // if |U'(s) - U(s)| > δ then
                        //    δ <- |U'(s) - U(s)|
                        if(Math.abs(utilityAux  - utilities.get(state)) > delta)
                            delta = Math.abs(utilitiesAux.get(state)  - utilities.get(state));
                    }
                }
                // U <- U'
                utilities.putAll(utilitiesAux);
            }
            while(delta > maxDelta*(1-problem.gamma)/problem.gamma);
            //***************************/

            return utilities;
        }

        /** Improves the policy given the utility */
        private Policy policyImprovement(HashMap<State,Double> utilities)
        {
            /*
            function PolicyImprovement(MDP< S,A,T,R,γ >,Uπ)
                for each s ∈ S do
                    π'(s) ← argmaxa R(s) + γ Sum[s'](T(s,a,s')U^π(s'))
                return π'
            */

            Policy newPolicy = new Policy();

            //****************************/
            // TO DO
            //***************************/
            for (State s : ((MDPLearningProblem)problem).getAllStates())
                newPolicy.setAction(s, argMaxAction(problem.getPossibleActions(s),s,utilities));
            //****************************/

            return newPolicy;
        }

        // γ * max[a] ( SUM[s'] T(s,a,s')U(s') ) */
        public Action argMaxAction(ArrayList<Action> actions, State state, HashMap<State,Double> utilities)
        {
            double max = Double.NEGATIVE_INFINITY;
            double auxMax = max;
            Action bestAction = null;

            for(Action action : actions)
            {
                auxMax = ((MDPLearningProblem) problem).getExpectedUtility(state,action,utilities,problem.gamma);
                if (auxMax > max)
                {
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
