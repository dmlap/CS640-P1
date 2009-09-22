import java.awt.image.BufferedImage;


public class GUITest {
	public static void main(String[] args) {
		ImageViewer view = new ImageViewer();
		ObjectTracker ot = new ObjectTracker(new ResultWindow());
		BufferedImage i = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
		
		CS440Image ci = new CS440Image(i);

		ot.GetFrameReceiver().receive(ci);
		ImageMoments im = new ImageMoments();
		im.L1 = 500;
		im.L2 = 500;
		im.x = 50;
		im.y = 50;
		ot.receive(im);
		view.showImage(ci);
		view.repaint();
	}

}
