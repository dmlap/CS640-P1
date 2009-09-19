import java.awt.*;
import java.awt.image.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * This class represents the video feed. The ExtVideoSource 
 * will call the callback function in the passed in VideoSink 
 * class. To get the video, this class uses the JNI library
 * to call C++ functions in the ExtVideoSource.dll library.
 * This class should not be modified by the students.
 * 
 * @author Sam Epstein 
 *
 */
class ExtVideoSource implements VideoSource {
     
	public void run() {

		try
		{
			int [] dims = start();
			if(dims==null || dims.length<2)
				throw new Exception("Could not connect to camera");
			width = dims[0];
			height = dims[1];
			/* justin's code */
			/*
			int count = 0;
			int max = 5;
			File dir = new File("images");
			dir.mkdirs();
			String imagePath = "image";
			while(count < max) {
				BufferedImage bi = grabFrame();
				if(!videoSink.receiveFrame(new CS440Image(bi))) {
					try { stop(); } 
					catch(Exception e){}
					break;
				}
				else {
					File output = new File(dir, imagePath+count+".jpg");
					count++;
					try { ImageIO.write(bi, "JPG", output); } 
					catch (IOException e) { e.printStackTrace(); }
				}
				try { Thread.sleep(waitTime); }
				catch (Exception e) {}
			}
			*/
			/* end of justin's code */
			
	 		while(true)
	 		{
	 			BufferedImage bi = grabFrame();
	 			if(!videoSink.receiveFrame(new CS440Image(bi)))
	 			{
	 				try{stop();}catch(Exception e){}
	 				break;
	 			}
	 			try{Thread.sleep(waitTime);}catch(Exception e){}
	 		}
	 		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	 
 	 private native int[] start();
     private native int[] getPic();
     private native void stop();
     
     private boolean stop=true;
     private int width;
     private int height;
     
 	 //Timer timer;
     VideoSink videoSink;
     int waitTime = 0;

    /* (non-Javadoc)
     * @see VideoSource#setup(VideoSink)
     */
    public void setup(VideoSink videoSink)
 	{
 		setup(videoSink,15);
 	}
 	/* (non-Javadoc)
 	 * @see VideoSource#setup(VideoSink, int)
 	 */
 	public void setup(VideoSink videoSink,int framesPerSecond)
 	{
 		this.videoSink=videoSink;
 		waitTime = (int)(1000.0/((double)framesPerSecond)); 		
 	}
 
 	private BufferedImage grabFrame()
 	{
 		try
 		{
 	    	BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
 	    	int [] data = getPic();
 	    	if(data==null || data.length<10)
 	    		return bi;
 	    	
 			int counter = 0;
 	    	for(int y = 0; y < height; y++)
 			{
 				for(int x = 0; x < width; x++)
 				{
 					int r = data[counter+2];
 					int g = data[counter+1];
 					int b = data[counter+0];
 					bi.setRGB(x, height-y-1, ((r << 16) | (g << 8) | b));
 					counter += 3;	
 				}
 			}
 	    	return bi;
 	    	
 		}catch(Exception e)
 		{
 			e.printStackTrace();
 		}
 		return null;
 	}

}
