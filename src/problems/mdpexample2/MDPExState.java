package problems.mdpexample2;

import learning.State;

/**
 *  Represents an state for the MDP seen in the class example. 
 *  Each state has an id. 
 */

public class MDPExState extends State {
	
    /* Identifier of the state. Using an integer instead of a String is useful for indexing data structures. */
	private int id;
	
	/** Constructor */
	public MDPExState(int id){ this.id = id; }
	
	/** Returns the id */
	public int id(){ return id; }
	
	/** Whether two states are equal */
	@Override
	public boolean equals(Object anotherState) {
		// If the object passed as parameter is not a state, returns false and reports an error
		if (!(anotherState instanceof MDPExState)){
			System.out.println("Trying to compare two objects of different classes.");
			return false;
		}
		// If the two objects have the same class, compares their positions.
		return this.id == ((MDPExState) anotherState).id;
	}

	/** Hash code, very simple */
	@Override
	public int hashCode() { return id; }

	/** Returns a String with the description of the state */
	@Override
	public String toString() { return Integer.toString(id); }
}
