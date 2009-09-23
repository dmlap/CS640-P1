import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class MotionAnalyzerTest {
	@Test
	public void leftToRight() {
		final List<String> rs = new ArrayList<String>();
		MotionAnalyzer ma = new MotionAnalyzer();
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
		assertEquals(MotionAnalyzer.L_TO_R, rs.get(0));
	}

	@Test
	public void rightToLeft() {
		final List<String> rs = new ArrayList<String>();
		MotionAnalyzer ma = new MotionAnalyzer();
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
		assertEquals(MotionAnalyzer.R_TO_L, rs.get(0));
	}

	@Test
	public void upToDown() {
		final List<String> rs = new ArrayList<String>();
		MotionAnalyzer ma = new MotionAnalyzer();
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
		assertEquals(MotionAnalyzer.U_TO_D, rs.get(0));
	}
	
	@Test
	public void downToUp() {
		final List<String> rs = new ArrayList<String>();
		MotionAnalyzer ma = new MotionAnalyzer();
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
		assertEquals(MotionAnalyzer.D_TO_U, rs.get(0));
	}
}
