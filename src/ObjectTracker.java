import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class ObjectTracker implements Sink<ImageMoments>, Source<CS440Image>
{
	private CS440Image frame;
	private FrameReceiver receiver;
	private ResultWindow results;
	private List<Sink<CS440Image>> subscribers = new ArrayList<Sink<CS440Image>>();
	
	public ObjectTracker(ResultWindow window)
	{
		receiver = new FrameReceiver();
		results = window;
	}
	
	private class FrameReceiver implements Sink<CS440Image>
	{
		@Override
		public void receive(CS440Image image)
		{
			frame = image;
		}
	}
	
	@Override
	public void receive(ImageMoments moment) 
	{
		String text = "L1 = " + moment.L1 + ", L2 = " + moment.L2 + ", THETA = " + moment.theta + ", X = " + moment.x + ", Y = " + moment.y;
		this.results.updateText(text);
		this.DrawBoundingBox(moment);
		for(Sink<CS440Image> subscriber : subscribers) {
			subscriber.receive(frame);
		}
	}
	
	public FrameReceiver GetFrameReceiver()
	{
		return this.receiver;
	}
	
	public void DrawBoundingBox(ImageMoments moment)
	{
		// Set bounding box
		Color c = new Color(255, 255, 255);
		BufferedImage img = frame.getRawImage();
		Graphics2D g = img.createGraphics();
		g.setColor(c);
		g.draw3DRect(150, 150, 40, 40, true);
		//g.draw3DRect(moment.x, moment.y, moment.L1, moment.L2, true);
		
		Pair<Integer, Integer> centroid = new Pair<Integer, Integer>(moment.x, moment.y);
		
		int bottomx = ((centroid.getFirst() + moment.L1) > frame.height()) ? frame.height() : centroid.getFirst() + moment.L1;
		int topx = ((centroid.getFirst() - moment.L1) < 0) ? 0 : centroid.getFirst() - moment.L1;
		int lefty = ((centroid.getSecond() - moment.L2) < 0) ? 0 : centroid.getSecond() - moment.L2;
		int righty = ((centroid.getSecond() + moment.L2) > frame.width()) ? frame.width() : centroid.getSecond() + moment.L2;
		
		results.updateText("Bounding Box");
		results.updateText("Top X: " + topx + ", Bottom X: " + bottomx + ", Left Y:" + lefty + ", Right Y:" + righty);

	}
	
	public CS440Image GetTrackedFrame()
	{
		return frame;
	}

	@Override
	public void subscribe(Sink<CS440Image> sink) {
		subscribers.add(sink);
	}
}
