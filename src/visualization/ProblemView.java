package visualization;

import javax.swing.JPanel;

import learning.Action;
import learning.State;

/** 
 * Extends JPanel to create the functionalities necessary to visualize problems. 
 * This functionalities are only graphical. The view always shows one state, and actions
 * from this state to other state, which is given
 */
public abstract class ProblemView extends JPanel{
	
	/** Current state.*/
	protected State currentState;

	/** Sets the current state in the view and prepares the visualization.  */
	public void setState(State state) {
		currentState = state;
		showState();
	}
	
	/** 
	 *  Performs an action in the view. In most cases, the action is not necessary. 
	 *  However, sometimes the graphical representation can depend also of the action.
	 *  For instance, running vs walking or jumping.
	 */
	public  void takeAction(Action action, State toState){
		showAction(action, toState);
		setState(toState);
	}
	
	/** Shows the current state. */
	protected abstract void showState();
	
	/** Shows the action  */
	protected abstract void showAction(Action action, State toState);
	
}
