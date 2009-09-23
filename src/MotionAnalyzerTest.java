import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.Test;


public class MotionAnalyzerTest {
	@Test
	public void leftToRight() {
		final List<String> rs = new ArrayList<String>();
		MotionAnalyzer ma = new MotionAnalyzer(0.70D);
		ma.subscribe(new Sink<String>() {
			@Override
			public void receive(String s) {
				rs.add(s);
			}
		});
		for(int i = 0; i < ma.window + 1; ++i) {
			ImageMoments im = new ImageMoments();
			im.x = i;
			im.y = 0;
			ma.receive(im);
		}
		assertEquals(1, rs.size());
		assertEquals(MotionAnalyzer.L_TO_R, rs.get(0).substring(0, rs.get(0).lastIndexOf(" ") + 1));
	}

	@Test
	public void rightToLeft() {
		final List<String> rs = new ArrayList<String>();
		MotionAnalyzer ma = new MotionAnalyzer(0.70D);
		ma.subscribe(new Sink<String>() {
			@Override
			public void receive(String s) {
				rs.add(s);
			}
		});
		for(int i = ma.window + 1; i > 0; --i) {
			ImageMoments im = new ImageMoments();
			im.x = i;
			im.y = 0;
			ma.receive(im);
		}
		assertEquals(1, rs.size());
		assertEquals(MotionAnalyzer.R_TO_L, rs.get(0).substring(0, rs.get(0).lastIndexOf(" ") + 1));
	}

	@Test
	public void upToDown() {
		final List<String> rs = new ArrayList<String>();
		MotionAnalyzer ma = new MotionAnalyzer(0.70D);
		ma.subscribe(new Sink<String>() {
			@Override
			public void receive(String s) {
				rs.add(s);
			}
		});
		for(int i = 0; i < ma.window + 1; ++i) {
			ImageMoments im = new ImageMoments();
			im.x = 0;
			im.y = i;
			ma.receive(im);
		}
		assertEquals(1, rs.size());
		assertEquals(MotionAnalyzer.U_TO_D, rs.get(0).substring(0, rs.get(0).lastIndexOf(" ") + 1));
	}
	
	@Test
	public void downToUp() {
		final List<String> rs = new ArrayList<String>();
		MotionAnalyzer ma = new MotionAnalyzer(0.70D);
		ma.subscribe(new Sink<String>() {
			@Override
			public void receive(String s) {
				rs.add(s);
			}
		});
		for(int i = ma.window + 1; i > 0; --i) {
			ImageMoments im = new ImageMoments();
			im.x = 0;
			im.y = i;
			ma.receive(im);
		}
		assertEquals(1, rs.size());
		assertEquals(MotionAnalyzer.D_TO_U, rs.get(0).substring(0, rs.get(0).lastIndexOf(" ") + 1));
	}
	
	@Test
	public void magnitudeThreshold() {
		final List<String> rs = new ArrayList<String>();
		MotionAnalyzer ma = new MotionAnalyzer(0.70D);
		ma.subscribe(new Sink<String>() {
			@Override
			public void receive(String s) {
				rs.add(s);
			}
		});
		ma.magnitudeThreshold = 0D;
		for(int i = 0; i < 1000; ++i) {
			ma.receive(new ImageMoments());
		}
		assertEquals(0, rs.size());
	}
	
	@Test
	public void arithmenticMeanOfMagnitudes() {
		Random r = new Random();
		final List<String> rs = new ArrayList<String>();
		MotionAnalyzer ma = new MotionAnalyzer(0.70D);
		ArrayList<ImageMoments> ims = new ArrayList<ImageMoments>();
		for(int i = 0; i < ma.window; ++i) {
			ImageMoments im = new ImageMoments();
			im.x = r.nextInt(5);
			im.y = r.nextInt(5) + 1;
			ims.add(im);
		}
		double acc = 0;
		Iterator<ImageMoments> itr = ims.iterator();
		ImageMoments lhs = itr.next();
		while (itr.hasNext()) {
			ImageMoments rhs = itr.next();
			acc += Math.sqrt(Math.pow(lhs.x - rhs.x, 2) + Math.pow(lhs.y - rhs.y, 2));
			lhs = rhs;
		}
		acc += Math.sqrt(Math.pow(lhs.x, 2) + Math.pow(lhs.y, 2));
		double mAvg = (double) acc / ma.window;
		ma.magnitudeThreshold = mAvg - 0.001D;
		ma.subscribe(new Sink<String>() {
			@Override
			public void receive(String s) {
				rs.add(s);
			}
		});
		for(ImageMoments im : ims) {
			ma.receive(im);
		}
		ma.receive(new ImageMoments());
		
		assertEquals(1, rs.size());
	}
	
	@Test
	public void confidence() {
		final List<String> rs = new ArrayList<String>();
		MotionAnalyzer ma = new MotionAnalyzer(0.70D);
		ma.subscribe(new Sink<String>() {
			@Override
			public void receive(String s) {
				rs.add(s);
			}
		});
		double y = Math.tan(Math.PI / 8);
		ma.magnitudeThreshold = -1D;
		for (int i = 0; i < ma.window + 1; ++i) {
			ImageMoments im = new ImageMoments();
			im.x = 1D + i;
			im.y = y + i;
			ma.receive(im);
		}
		assertEquals(1, rs.size());
		double c = Double.parseDouble(rs.get(0).substring(rs.get(0).lastIndexOf(" ")));
		assertTrue("expected 0.5, found: " + c, c + 0.001D > 0.5 && c - 0.001D < 0.5);
	}
}
