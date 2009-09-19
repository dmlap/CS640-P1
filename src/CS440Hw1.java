/*
 * Main class for Hw1. 
 * 
 */
public class CS440Hw1 {

	/**
	 * Entry point for the program
	 * @param args
	 */
	public static void main(String[] args) {

		try
		{
			
			//Initialize VideoSink
			VideoSink dvs = new VideoSink();

			//Initialize VideoSource
			ExtVideoSource evs = new ExtVideoSource();
			evs.setup(dvs,500);			

			//start grab
			evs.run();
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	static {
        System.loadLibrary("ExtVideoSource");
    }
	
	
}