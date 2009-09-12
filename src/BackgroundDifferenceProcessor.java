import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A {@link CS440Image} processor that produces composite background differences
 * from a configurable number of {@link CS440Image CS440Images}. Note that
 * instances of this class are not thread-safe.
 * 
 * @author dml
 * 
 */
public class BackgroundDifferenceProcessor implements ImageSink, ImageSource {
	/**
	 * The number of {@link CS440Image CS440Images} to collect in each
	 * background difference {@link CS440Image}.
	 */
	private int window = 5;
	/**
	 * The buffer for holding {@link CS440Image CS440Images} to be processed.
	 * Its size should never exceed {@link #window}.
	 */
	private Queue<CS440Image> buffer = new LinkedList<CS440Image>();
	/**
	 * The {@link ImageSink} to delegate {@link CS440Image} processing to.
	 */
	private ImageSink processor = new Loading();
	/**
	 * The {@link ImageSink} subscribers to this
	 * {@link BackgroundDifferenceProcessor}.
	 */
	private List<ImageSink> subscribers = new ArrayList<ImageSink>(1);

	/**
	 * Load up received {@link CS440Image CS440Images} until the buffer's
	 * {@link Queue#size() size} reaches
	 * {@link BackgroundDifferenceProcessor#window} .
	 * 
	 * @author dml
	 * 
	 */
	private class Loading implements ImageSink {
		@Override
		public void receive(CS440Image frame) {
			buffer.add(frame);
			if (buffer.size() >= window) {
				processor = new BGDiff();
			}
		}
	}

	/**
	 * Process the existing {@link BackgroundDifferenceProcessor#buffer buffer}
	 * and then update it with the new {@link CS440Image}.
	 * 
	 * @author dml
	 * 
	 */
	private class BGDiff implements ImageSink {
		@Override
		public void receive(CS440Image frame) {
			// FIXME do processing here
			CS440Image result = frame;
			for (ImageSink subscriber : subscribers) {
				subscriber.receive(result);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ImageSink#receive(CS440Image)
	 */
	@Override
	public void receive(CS440Image frame) {
		processor.receive(frame);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ImageSource#subscribe(ImageSink)
	 */
	@Override
	public void subscribe(ImageSink sink) {
		subscribers.add(sink);
	}

}
