package engine;

/**
 * This exception is thrown in classes of the whole Automaton engine.
 * @author Maciej Znamirowski
 */
public class AutomatonException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param msg Message passed to the exception object.
	 */
	public AutomatonException(String msg) {
		super(msg);
	}
}
