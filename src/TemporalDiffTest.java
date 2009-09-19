import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TemporalDiffTest {
	private TemporalDifferenceProcessor bdp;
	private List<CS440Image> os;
	@Before
	public void before() {
		bdp = new TemporalDifferenceProcessor();
		os = new ArrayList<CS440Image>(4);
		bdp.subscribe(new ImageSink() {
			@Override
			public void receive(CS440Image frame) {
				os.add(frame);
			}
		});
	}
	@Test
	public void process() {
		/*
		 (x,y) -> rgb
		 (0,0) -> 0 4 0
		 (1,0) -> 1 3 2
		 (2,0) -> 2 2 4
		 (3,0) -> 3 1 6
		 (4,0) -> 4 0 8
		 */
		BufferedImage[] is = new BufferedImage[5];
		for (int i = 0; i < is.length; ++i) {
			is[i] = new BufferedImage(is.length, 2, BufferedImage.TYPE_INT_RGB);
			is[i].setRGB(i, 0, new Color(i, is.length - 1 - i, i * 2).getRGB());
			bdp.receive(new CS440Image(is[i]));
		}
		// flush out the diff 
		bdp.receive(null);
		assertEquals(1, os.size());
		BufferedImage bi = os.get(0).getRawImage();
		
		assertEquals("Unexpected difference magnitude for (0,0)", new Color(0, 0, 0), new Color(bi.getRGB(0, 1)));
		assertEquals("Incorrect difference magnitude for (0,0)", new Color(0, is.length - 1, 0), new Color(bi.getRGB(0, 0)));
		for (int i = 1; i < is.length - 1; ++i) {
			assertEquals("Unexpected difference magnitude for (" + i + ",1)", new Color(0, 0, 0), new Color(bi.getRGB(i, 1)));
			assertEquals("Incorrect difference magnitude for (" + i + ",0)", new Color(2 * i, 2 * (is.length - 1 - i), i * (is.length - 1)), new Color(bi.getRGB(i, 0)));
		}
		assertEquals("Unexpected difference magnitude for (4,0)", new Color(0, 0, 0), new Color(bi.getRGB(4, 1)));
		assertEquals("Incorrect difference magnitude for (4,0)", new Color(4, 0, 8), new Color(bi.getRGB(4, 0)));
	}
	@Test
	public void accumulates() {
		/*
		rgb
		0 4 0
		1 3 2
		2 2 4
		3 1 6
		4 0 8
		-----
		4 4 8
		*/
		BufferedImage[] is = new BufferedImage[5];
		for (int i = 0; i < is.length; ++i) {
			is[i] = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			is[i].setRGB(0, 0, new Color(i, 4 - i, 2 * i).getRGB());
			bdp.receive(new CS440Image(is[i]));
		}
		// flush out the diff 
		bdp.receive(null);
		
		assertEquals(new Color(1 * 4, 1 * 4, 2 * 4), new Color(os.get(0).getRawImage().getRGB(0, 0)));
	}
	@Test
	public void slidesWindow() {
		BufferedImage black = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < 5; ++i) {
			bdp.receive(new CS440Image(black));
		}
		BufferedImage notBlack = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		notBlack.setRGB(0, 0, new Color(1,1,1).getRGB());
		bdp.receive(new CS440Image(notBlack));
		bdp.receive(new CS440Image(notBlack));

		assertEquals(2, os.size());
		assertEquals(new Color(0,0,0), os.get(0).get(0, 0));
		assertEquals(new Color(1,1,1), os.get(1).get(0, 0));
	}
	@Test
	public void capsOverflow() {
		BufferedImage black = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < 5; ++i) {
			bdp.receive(new CS440Image(black));
		}
		BufferedImage white = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		white.setRGB(0, 0, Color.WHITE.getRGB());
		bdp.receive(new CS440Image(white));
		bdp.receive(new CS440Image(black));
		bdp.receive(new CS440Image(white));
		
		assertEquals(Color.WHITE, os.get(2).get(0, 0));
	}
}
