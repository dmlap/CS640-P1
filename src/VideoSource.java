/**
 * A provider for {@link CS440Image CS440Images}.
 * 
 * @author dml
 * 
 */
public interface VideoSource {

	/**
	 * Attach this {@link VideoSource} to the specified {@link VideoSink}.
	 * 
	 * @param videoSink
	 *            - the {@link VideoSink} to feed {@link CS440Image} frames to
	 * @see #setup(VideoSink, int)
	 */
	void setup(VideoSink videoSink);

	/**
	 * Attach this {@link VideoSource} to the specified {@link VideoSink} with a
	 * specified frame capture rate.
	 * 
	 * @param videoSink
	 *            - the {@link VideoSink} to feed {@link CS440Image} frames to
	 * @param framesPerSecond
	 *            - the number of {@link CS440Image CS440Images} to capture per
	 *            second.
	 */
	void setup(VideoSink videoSink, int framesPerSecond);

	/**
	 * Begin feeding {@link CS440Image CS440Images} to the {@link VideoSink}
	 * specified by calling {@link #setup(VideoSink)}.
	 */
	void run();

}
