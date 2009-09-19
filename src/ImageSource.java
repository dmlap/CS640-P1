/**
 * An object which can notify {@link ImageSink ImageSinks} of {@link CS440Image}
 * events.
 * 
 * @author dml
 * 
 */
public interface ImageSource<T> {

	/**
	 * Register <code>sink</code> to be notified via
	 * {@link ImageSink#receive(CS440Image)} when new {@link CS440Image
	 * CS440Images} become available.
	 * 
	 * @param sink
	 *            - the subscribing {@link ImageSink}
	 */
	void subscribe(ImageSink<T> sink);
}
