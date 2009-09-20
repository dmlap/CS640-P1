import static java.lang.Math.min;
import static java.lang.Math.abs;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
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
public class TemporalDifferenceProcessor implements Sink<CS440Image>, Source<CS440Image> {
	private static final int MAX_RED = Color.RED.getRed();
	private static final int MAX_GREEN = Color.GREEN.getGreen();
	private static final int MAX_BLUE = Color.BLUE.getBlue();
	/**
	 * The number of {@link CS440Image CS440Images} to collect in each
	 * background difference {@link CS440Image}.
	 */
	private int window = 2;
	/**
	 * The buffer for holding {@link CS440Image CS440Images} to be processed.
	 * Its size should never exceed {@link #window}.
	 */
	private Queue<CS440Image> buffer = new LinkedList<CS440Image>();
	/**
	 * The {@link Sink} to delegate {@link CS440Image} processing to.
	 */
	private Sink<CS440Image> processor = new Loading();
	/**
	 * The {@link Sink} subscribers to this
	 * {@link TemporalDifferenceProcessor}.
	 */
	private List<Sink<CS440Image>> subscribers = new ArrayList<Sink<CS440Image>>(1);

	/**
	 * Load up received {@link CS440Image CS440Images} until the buffer's
	 * {@link Queue#size() size} reaches the
	 * {@link TemporalDifferenceProcessor#window window} size.
	 * 
	 * @author dml
	 * 
	 */
	private class Loading implements Sink<CS440Image> {
		@Override
		public void receive(CS440Image frame) {
			buffer.add(frame);
			if (buffer.size() >= window) {
				processor = new BGDiff();
			}
		}
	}

	/**
	 * Find the absolute value of all "adjacent" {@link CS440Image CS440Images}
	 * in the {@link TemporalDifferenceProcessor#buffer buffer}, accumulate
	 * those difference magnitudes, notify subscribers of the result and then
	 * update the {@link TemporalDifferenceProcessor#buffer buffer} with the
	 * new {@link CS440Image}. This class assumes all {@link CS440Image
	 * CS440Images} in the {@link TemporalDifferenceProcessor#buffer buffer}
	 * are of the same height and width.
	 * 
	 * @author dml
	 * 
	 */
	private class BGDiff implements Sink<CS440Image> {
		@Override
		public void receive(CS440Image frame) {
			Iterator<CS440Image> itr = buffer.iterator();
			BufferedImage lhs = itr.next().getRawImage();
			BufferedImage result = new BufferedImage(lhs.getWidth(), lhs.getHeight(), lhs.getType());
			for (BufferedImage rhs; itr.hasNext(); lhs = rhs) {
				rhs = itr.next().getRawImage();
				// subtract RGB of lhs from RGB of rhs, accumulate in result
				for (int x = 0; x < result.getWidth(); ++x) {
					for (int y = 0; y < result.getHeight(); ++y) {
						Color lColor = new Color(lhs.getRGB(x, y));
						Color rColor = new Color(rhs.getRGB(x, y));
						Color accumulated = new Color(result.getRGB(x, y));
						result.setRGB(x, y, new Color(
								min(MAX_RED, accumulated.getRed() + abs(lColor.getRed() - rColor.getRed())), 
								min(MAX_GREEN, accumulated.getGreen() + abs(lColor.getGreen() - rColor.getGreen())), 
								min(MAX_BLUE, accumulated.getBlue() + abs(lColor.getBlue() - rColor.getBlue())))
							.getRGB());
					}
				}
			}
			// slide window
			buffer.add(frame);
			buffer.remove();
			// notify subscribers
			for (Sink<CS440Image> subscriber : subscribers) {
				subscriber.receive(new CS440Image(result));
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
	public void subscribe(Sink<CS440Image> sink) {
		subscribers.add(sink);
	}
}
