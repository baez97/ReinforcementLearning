package learning;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/** 
 * Represents and manages a Policy (a function P(state) ---> action).
 */
public class Policy {
	
	/* This HashMap contains the action assigned to each state (the policy). */
	private HashMap<State, Action> actionForState = new HashMap<State, Action>();
	
	/** Adds (or replaces) the action corresponding to a certain state. */
	public void setAction(State state, Action action){ actionForState.put(state, action); }
	
	/** Gets the action corresponding to a certain state. */
	public Action getAction(State state){ return actionForState.get(state);	}
	
	/** Resets the policy (deletes all the entries).*/
	public void reset(){ actionForState = new HashMap<State, Action>(); }
	
	/** 
	 *  Compares two policies. Comparison returns True only if both policies contain a 
	 *  similar set of pair state-action. 
	 */
	public boolean equals(Object anotherPolicy){
		// If the object passed as parameter is not a state, returns false 
		if (!(anotherPolicy instanceof Policy)){
			System.out.println("Trying to compare two objects of different classes.");
			return false;
		}
		// Compares the sizes of both policies, that must be equal.
		if (actionForState.size()!=((Policy)anotherPolicy).actionForState.size())
			return false;		
		// If both have the same size, compares all the elements
		for (Entry<State,Action> entry: actionForState.entrySet()){
			State state = entry.getKey();
			Action action = entry.getValue();
			// Returns false if some action in one does not exist in the other. 
			if (((Policy)anotherPolicy).getAction(state)!=action)
				return false;
		}		
		// Otherwise, the policies are similar.
		return true;
	}
	
	/** Size of the policy.*/
	public int size() {
		return actionForState.size();
	}
	
	/** Prints the policy as a list. */
	public String toString(){
		String output = "";
		// Gets the states in the table.
		Set<State> states = actionForState.keySet();
		for (State state: states){
			output += "\t" + state.toString();
			output += " -> " + actionForState.get(state)+"\n";
		}
		return output;
	}	
}