package problems.mdpexample1;

import learning.Action;

/** 
 * Actions that can be used in this particular problem. They can be enumerated. 
 */
public enum MDPExAction implements Action{ 
	
	/*  Values (possible actions).*/
	A0("0"),
	A1("1"),
	A2("2");
	
	/** Id field for each value */
	private final String id;
	
	/** Constructor for each value. */
	MDPExAction(String id){ this.id = id; }
	
	/** Returns the id of the action */
	@Override
	public String getId() { return id;} 
	
};
