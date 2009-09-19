import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class ObjectTracker implements Sink<ImageMoments>
{
	private CS440Image frame;
	private FrameReceiver receiver;
	private ResultWindow results;
	
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
		Graphics g = img.createGraphics();
		g.setColor(c);
		g.draw3DRect(150, 150, 40, 40, true);
		//g.draw3DRect(moment.x, moment.y, moment.L1, moment.L2, true);
	}
	
	public CS440Image GetTrackedFrame()
	{
		return frame;
	}
}
