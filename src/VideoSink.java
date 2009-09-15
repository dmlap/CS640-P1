import java.awt.Image;
import java.awt.image.*;


/***********
 * The VideoSink class represents the entry point for high level
 * analysis of videos. Images are fed to the VideoSink class via 
 * the receiveFrame. The videoSink has the ability to display 
 * images with the ImageViewer class.
 * 
 * @author Sam Epstein
 **********/
public class VideoSink implements ImageSink {

	//The window to display images
	ImageViewer imageViewer;
	
	//Simple counter for video cutoff
	long counter;
	
	//The constructor initializes the window for display
	VideoSink()
	{
		imageViewer=new ImageViewer("Image Viewer");
		counter = 0;
	}
	
	/* (non-Javadoc)
	 * @see ImageSink#receive(CS440Image)
	 */
	@Override
	public void receive(CS440Image frame) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ImageSink#receiveFrame(CS440Image)
	 */
	public boolean receiveFrame(CS440Image frame) {
	
		/**********
		 * Replace function with your code
		 **********/
		
		counter++;
		
		if(counter>=500)
		{
			close();
			return false;
		}
		
		boolean shouldStop = displayImage(frame); 
		return 	shouldStop;
	}

	/**
	 * This function displays the passed image in a frame.
	 * @param image The image to be displayed
	 */
	public boolean displayImage(CS440Image image)
	{
		if(imageViewer == null || !imageViewer.isActive())
			return false;
		imageViewer.showImage(image);
		return true;
	}
	
	/***
	 * Closes the window
	 */
	public void close()
	{
		if(imageViewer!=null)
		{
			this.imageViewer.dispose();
			imageViewer=null;
		}
	}

}
