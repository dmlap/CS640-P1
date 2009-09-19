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
public class VideoSink {

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
	
	
	 
	/**
	 * The central function of VideoSink and the place where students
	 * can edit the code. receiveFrame function is given an image. The 
	 * body of the code will perform high level manipulatations of the 
	 * image, then display the image in the imageViewer. The return values
	 * indicates to the the video source whether or not to keep sending 
	 * images.
	 * 
	 * @param frame The current frame of the video source/
	 * @param firstFrame Whether or not the frame is the first frame of the video
	 * @return true if the video source should continue, or false if the video source should stop.
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