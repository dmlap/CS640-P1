import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class ObjectTracker implements Sink<ImageMoments>, Source<CS440Image> {
	private CS440Image frame;
	private FrameReceiver receiver;
	private ResultWindow results;
	private List<Sink<CS440Image>> subscribers = new ArrayList<Sink<CS440Image>>();
	private boolean debugOutput = false;

	/**
	 * Constructor
	 * 
	 * @param window
	 *            - the results window to output to
	 * @param debug
	 *            - boolean determining whether output is printed to the results
	 *            window or not
	 */
	public ObjectTracker(ResultWindow window, boolean debug) {
		receiver = new FrameReceiver();
		results = window;
		debugOutput = debug;
	}

	/**
	 * Private inner class to receive original image frame
	 * 
	 * @author jdavis
	 * 
	 */
	private class FrameReceiver implements Sink<CS440Image> {
		@Override
		public void receive(CS440Image image) {
			frame = image;
		}
	}

	@Override
	public void receive(ImageMoments moment) {
		if (debugOutput) {
			String text = "L1 = " + moment.L1 + ", L2 = " + moment.L2
					+ ", THETA = " + moment.theta + ", X = " + moment.x
					+ ", Y = " + moment.y + ", M00 = " + moment.m00
					+ ", M10 = " + moment.m10 + ", M01 = " + moment.m01
					+ ", M11 = " + moment.m11 + ", M20 = " + moment.m20
					+ ", M02 = " + moment.m02;
			this.results.updateText(text);
		}

		this.DrawBoundingBox(moment);

		for (Sink<CS440Image> subscriber : subscribers) {
			subscriber.receive(frame);
		}
	}

	public Sink<CS440Image> GetFrameReceiver() {
		return this.receiver;
	}

	/**
	 * Draws a box around the centroid based on L1 and L2
	 * 
	 * @param moment
	 *            - the image moment
	 */
	public void DrawBoundingBox(ImageMoments moment) {
		// Set bounding box
		Graphics2D g = frame.getRawImage().createGraphics();
		g.setColor(Color.WHITE);

		int x = (int) Math.round(moment.x);
		int y = (int) Math.round(moment.y);
		int ulx = (int) Math.round(moment.x - (moment.L1 / 2));
		int uly = (int) Math.round(moment.y - (moment.L2 / 2));
		int l1 = (int) moment.L1;
		int l2 = (int) moment.L2;

		g.drawRect(ulx, uly, l1, l2);
		StringBuilder centroid = new StringBuilder("(").append(moment.x)
				.append(",").append(moment.y).append(")");
		g.drawString(centroid.toString(), x + 5, y);
		StringBuilder moments = new StringBuilder("m00: ").append(moment.m00)
				.append(" m01: ").append(moment.m01).append(" m10: ").append(
						moment.m10).append(" m11: ").append(moment.m11).append(
						" m20: ").append(moment.m20);
		g.drawString(moments.toString(), 30, 30);
		g.fillOval(x - 2, y - 2, 4, 4);
	}

	public CS440Image GetTrackedFrame() {
		return frame;
	}

	@Override
	public void subscribe(Sink<CS440Image> sink) {
		subscribers.add(sink);
	}
}
