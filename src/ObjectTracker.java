import java.awt.Color;


public class ObjectTracker implements Sink<ImageMoments>
{
	private CS440Image frame;
	private FrameReceiver receiver;
	
	public ObjectTracker()
	{
		receiver = new FrameReceiver();
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
		this.DrawBoundingBox(moment);
	}
	
	public FrameReceiver GetFrameReceiver()
	{
		return this.receiver;
	}
	
	public void DrawBoundingBox(ImageMoments moment)
	{
		// Set bounding box color
		Color box = new Color(255, 255, 255);
		
		Pair<Integer, Integer> centroid = new Pair<Integer, Integer>(moment.x, moment.y);
		
		int bottomx = ((centroid.getFirst() + moment.L1) > frame.height()) ? frame.height() : centroid.getFirst() + moment.L1;
		int topx = ((centroid.getFirst() - moment.L1) < 0) ? 0 : centroid.getFirst() - moment.L1;
		int lefty = ((centroid.getSecond() - moment.L2) < 0) ? 0 : centroid.getSecond() - moment.L2;
		int righty = ((centroid.getSecond() + moment.L2) > frame.width()) ? frame.width() : centroid.getSecond() + moment.L2;
		
		// Draw box
		for (int i = 0; i <= (moment.L2 * 2); i++)
		{
			if ((lefty + i) > frame.width())
				break;
			
			frame.set(topx, lefty + i, box);
			frame.set(bottomx, lefty + i, box);
		}
		
		for (int i = 0; i < (moment.L1 * 2); i++)
		{
			if ((topx + i) > frame.height())
				break;
			
			frame.set(topx + i, lefty, box);
			frame.set(topx + i, righty, box);
		}
	}
	
	public CS440Image GetTrackedFrame()
	{
		return frame;
	}
}
