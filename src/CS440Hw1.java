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
			ResultWindow results = new ResultWindow();
			results.setVisible(true);

			VideoSink dvs = new VideoSink();
			//VideoSink tdvs = new VideoSink();

			//Initialize VideoSource
			ExtVideoSource evs = new ExtVideoSource();
			evs.setup(dvs,500);
			
			//ExtVideoSource tdevs = new ExtVideoSource();
			//tdevs.setup(tdvs, 500);

			ImageMomentsGenerator img = new ImageMomentsGenerator();
			TemporalDifferenceProcessor tdp = new TemporalDifferenceProcessor();
			ObjectTracker ot = new ObjectTracker(results);
			
			dvs.subscribe(tdp);
			dvs.subscribe(ot.GetFrameReceiver());
			tdp.subscribe(img);
			img.subscribe(ot);
			
			//start grab
			evs.run(ot);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	static {
        System.loadLibrary("ExtVideoSource");
    }
	
	
}
