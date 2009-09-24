import static java.lang.Math.atan;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * An object that examines successive {@link ImageMoments} and uses the
 * arithmetic average of magnitude and angle differences to determine whether
 * the motion is left-to-right, right-to-left, up-to-down, or down-to-up and
 * reports a confidence value.
 * 
 * @author dml
 * 
 */
public class MotionAnalyzer implements Sink<ImageMoments>, Source<String> {
	public static final String L_TO_R = "Right-to-left swipe - confidence: ";
	public static final String R_TO_L = "Left-to-right swipe - confidence: ";
	public static final String U_TO_D = "Down-to-up swipe - confidence: ";
	public static final String D_TO_U = "Up-to-down swipe - confidence: ";

	private static final double HALF_PI = Math.PI / 2;
	private static final double UP_LEFT = (3 * Math.PI) / 4;
	private static final double UP_RIGHT = Math.PI / 4;
	private static final double DOWN_RIGHT = -UP_RIGHT;
	private static final double DOWN_LEFT = -UP_LEFT;

	private double threshold = 0.70D;
	/**
	 * The number of {@link ImageMoments} to average across. It must be greater
	 * than or equal to two.
	 */
	public int window = 5;
	/**
	 * The minimum average magnitude to analyze.
	 */
	public double magnitudeThreshold = 0.5D;

	MotionAnalyzer(double thresh) {
		threshold = thresh;
	}
	
	private ImageMoments lastSeen;
	private Deque<Vector> vectors = new LinkedList<Vector>();
	private Sink<ImageMoments> process = new Sink<ImageMoments>() {
		@Override
		public void receive(ImageMoments imageMoment) {
			lastSeen = imageMoment;
			process = new Sink<ImageMoments>() {
				@Override
				public void receive(ImageMoments rhs) {
					vectors.addLast(calcVector(lastSeen, rhs));
					lastSeen = rhs;
					if (vectors.size() >= window - 1) {
						process = new Sink<ImageMoments>() {
							@Override
							public void receive(ImageMoments rhs) {
								vectors.addLast(calcVector(lastSeen, rhs));
								lastSeen = rhs;

								// calculate average motion vector
								double a = 0;
								double m = 0;
								for (Vector v : vectors) {
									a += v.angle;
									m += v.magnitude;
								}
								a /= window;
								m /= window;
								if (m <= magnitudeThreshold) {
									// too little motion, don't report results
									return;
								}
								double confidence = Math.abs((a % HALF_PI)
										/ HALF_PI);
								String result;
								if (a <= UP_LEFT && a > UP_RIGHT) {
									result = new StringBuilder(D_TO_U).append(
											confidence).toString();
								} else if (a <= UP_RIGHT && a > DOWN_RIGHT) {
									result = new StringBuilder(L_TO_R).append(
											confidence).toString();
								} else if (a >= DOWN_LEFT && a < DOWN_RIGHT) {
									result = new StringBuilder(U_TO_D).append(
											confidence).toString();
								} else {
									result = new StringBuilder(R_TO_L).append(
											confidence).toString();
								}
								for (Sink<String> subscriber : subscribers) {
									if (confidence >= threshold)
										subscriber.receive(result);
								}
								vectors.remove();
							}
						};
					}
				}
			};
		}
	};
	private List<Sink<String>> subscribers = new ArrayList<Sink<String>>();

	/**
	 * Construct the difference vector between <code>lhs</code> and
	 * <code>rhs</code>.
	 * 
	 * @param lhs
	 *            - the minuend
	 * @param rhs
	 *            - the subtrahend
	 * @return the difference vector
	 */
	private Vector calcVector(ImageMoments lhs, ImageMoments rhs) {
	double x = rhs.x - lhs.x;
		double y = lhs.y - rhs.y;
		double m = sqrt(pow(x, 2) + pow(y, 2));
		double a = atan(y / x);
		if (x < 0) {
			// mirror across y-axis
			a += Math.PI;
		}
		return new Vector(a, m);
	}

	@Override
	public void receive(ImageMoments imageMoment) {
		process.receive(imageMoment);
	}

	@Override
	public void subscribe(Sink<String> sink) {
		subscribers.add(sink);
	}

	/**
	 * An (angle, magnitude)-representation of a vector.
	 * 
	 * @author dml
	 * 
	 */
	private static class Vector {
		private final double angle;
		private final double magnitude;

		public Vector(double angle, double magnitude) {
			this.angle = angle;
			this.magnitude = magnitude;
		}

		@Override
		public String toString() {
			return new StringBuilder("[angle:").append(Math.toDegrees(angle))
					.append(",magnitude:").append(magnitude).append("]")
					.toString();
		}
	}

}
