import static java.lang.Math.atan;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * An object that examines {@link ImageMoments} and determines an average
 * velocity vector.
 * 
 * @author dml
 * 
 */
public class MotionAnalyzer implements Sink<ImageMoments>, Source<String> {
	public static final String L_TO_R = "Left-to-right swipe";
	public static final String R_TO_L = "Right-to-left swipe";
	public static final String U_TO_D = "Up-to-down swipe";
	public static final String D_TO_U = "Down-to-up swipe";
	
	private static final double UP_LEFT    = (3 * Math.PI) / 4;
	private static final double UP_RIGHT   = Math.PI / 4;
	private static final double DOWN_RIGHT = -UP_RIGHT;
	private static final double DOWN_LEFT  = -UP_LEFT;
	
	public int window = 5;
	
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
					if(vectors.size() >= window - 1) {
						process = new Sink<ImageMoments>() {
							@Override
							public void receive(ImageMoments rhs) {
								vectors.addLast(calcVector(lastSeen, rhs));
								lastSeen = rhs;
								vectors.remove();
								
								// calculate average motion vector
								double a = 0; 
								double m = 0;
								for (Vector v : vectors) {
									a += v.angle;
									m += v.magnitude;
								}
								a /= vectors.size();
								m /= vectors.size();
								String result;
								if(a <= UP_LEFT && a > UP_RIGHT) {
									result = D_TO_U;
								} else if(a <= UP_RIGHT && a > DOWN_RIGHT) {
									result = L_TO_R;
								} else if(a >= DOWN_LEFT && a < DOWN_RIGHT) {
									result = U_TO_D; 
								} else {
									result = R_TO_L;
								}
								for(Sink<String> sub : subscribers) {
									sub.receive(result);
								}
							}
						};
					}
				}
			};
		}
	};
	private List<Sink<String>> subscribers = new ArrayList<Sink<String>>();
	
	private Vector calcVector(ImageMoments lhs, ImageMoments rhs) {
		double x = rhs.x - lhs.x;
		double y = lhs.y - rhs.y;
		double m = sqrt(pow(x, 2) + pow(y, 2));
		double a = atan(y/x);
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
	
	private static class Vector {
		private final double angle;
		private final double magnitude;

		public Vector(double angle, double magnitude) {
			this.angle = angle;
			this.magnitude = magnitude;
		}

		@Override
		public String toString() {
			return new StringBuilder("[angle:").append(Math.toDegrees(angle)).append(",magnitude:").append(magnitude).append("]").toString();
		}
	}

}
