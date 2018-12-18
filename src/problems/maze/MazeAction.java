package problems.maze;

import learning.Action;

/** 
 *   Implements the different actions that can be applied in a given state.
 *   As the actions are the same for each state, use an enumeration instead
 *   of a class which allows defining unique actions.
 */
public enum MazeAction implements Action{ 
	/** Values */
	RIGHT("RIGHT"),
	LEFT("LEFT"),
	UP("UP"),
	DOWN("DOWN"),
	DIVE("DIVE");
	
	/** Id field for each value */
	private final String id;
	
	/** Constructor for each value. */
    MazeAction(String id){ this.id = id;}
	
	/** Returns the description of the action */
	@Override
	public String getId() { return id; } 
};
