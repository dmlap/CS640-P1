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
			ResultWindow results = new ResultWindow(false);
			results.setVisible(true);

			VideoSink dvs = new VideoSink();
			ImageViewer viewer = new ImageViewer();
			ImageViewer diffViewer = new ImageViewer();

			//Initialize VideoSource
			ExtVideoSource evs = new ExtVideoSource();
			evs.setup(dvs,33);
			
			ImageMomentsGenerator img = new ImageMomentsGenerator(550);
			TemporalDifferenceProcessor tdp = new TemporalDifferenceProcessor();
			ObjectTracker ot = new ObjectTracker(results, false);
			MotionAnalyzer ma = new MotionAnalyzer();
			
			dvs.subscribe(tdp);
			dvs.subscribe(ot.GetFrameReceiver());
			ot.subscribe(viewer);
			tdp.subscribe(img);
			tdp.subscribe(diffViewer);
			img.subscribe(ot);
			ma.subscribe(results);
			
			//start grab
			evs.run(ot);
			
			viewer.dispose();
			diffViewer.dispose();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	static {
        System.loadLibrary("ExtVideoSource");
    }
	
	
}
