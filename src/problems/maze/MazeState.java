package problems.maze;

import utils.Position;
import learning.State;

/**
 *  Represents an state, which corresponds with a position (cell) of the maze.
 */
public class MazeState extends State {
	
	/** An state is a position given by the coordinates (y,x) and the number of bites. */
	protected Position position;

	/** Constructor. Receives the pair of coordinates represented by the state. */
	public MazeState(int x, int y){
		position = new Position(x, y);
	}
	
	/** Constructor. Receives a MazePosition */
	public MazeState(Position position){
		this.position = new Position(position.x,position.y);
	}	
	
	/** Returns the position represented by the state. */
	public Position position(){
		return position;
	}
	
	/** Returns the coordinate x. */
	public int X(){ return position.x; }
	
	/** Returns the coordinate y. */
	public int Y(){ return position.y; }
	
	/** 
	 * Checks if two states are similar. The method overrides the one provided by the Object class
	 * and is used by some classes in Java. For instance, the method HashSet.contains makes use of equals.
	 */
	@Override
	public boolean equals(Object anotherState){
		// If the object passed as parameter is not a state, returns false and reports an error
		if (!(anotherState instanceof MazeState)){
			System.out.println("Trying to compare two objects of different classes.");
			return false;
		}
		// If the two objects have the same class, compares their positions.
		return this.position().equals(((MazeState)anotherState).position());	
	}

	/**  Basic hashing function. Overrides the one in Object and is used in classes such as HashSet. */
	@Override
	public int hashCode(){ return position.hashCode(); }

	/**  Prints the state.*/
	public String toString(){ return position.toString();}
}
