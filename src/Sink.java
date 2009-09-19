/**
 * A generic interface for objects which can be notified of <code>T</code>s.
 * 
 * @author dml
 * 
 */
public interface Sink<T> {

	/**
	 * Notify this {@link Sink} of a new <code>T</code>.
	 * 
	 * @param frame
	 *            - the new <code>T</code>
	 */
	void receive(T U);
}