package visualization;

/** 
 * This interface must be implemented by all problems that can be evaluated graphically. In such a case,
 * they must provide an instance of ProblemView.
 */
public interface ProblemVisualizable {
	
	/** Returns a panel with the view of the problem. */
	public ProblemView getView(int sizePx);
	
}
