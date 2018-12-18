package learning;

/**
 *  Represents a generic action. Actions can be enumerated and common to each state 
 *  in the problem (for example, mazes), but can also be unique for each state (for example, routing). 
 *  Therefore, an action must be able to return its own description (which is returned as a String, for versatility). 
 */
public interface Action {
	public String getId();
}
