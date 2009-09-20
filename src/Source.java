/**
 * An object which can notify {@link Sink Sinks} of <code>T</code> events.
 * 
 * @author dml
 * 
 */
public interface Source<T> {

	/**
	 * Register <code>sink</code> to be notified via
	 * {@link Sink#receive(CS440Image)} when a new <code>T</code> become
	 * available.
	 * 
	 * @param sink
	 *            - the subscribing {@link Sink}
	 */
	void subscribe(Sink<T> sink);
}
