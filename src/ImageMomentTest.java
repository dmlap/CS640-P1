import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ImageMomentTest {
	private double delta = 0.001;
	
	private void assertCloseTo(double expected, double found) {
		assertTrue("expected: " + expected + ", found: " + found,
				found < (expected + delta) && found > (expected - delta));
	}

	@Test
	public void calc() {
		BufferedImage i = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		i.setRGB(0, 0, Color.WHITE.getRGB());
		i.setRGB(1, 1, Color.WHITE.getRGB());
		final List<ImageMoments> ims = new ArrayList<ImageMoments>();
		ImageMomentsGenerator img = new ImageMomentsGenerator();
		img.subscribe(new Sink<ImageMoments>() {
			@Override
			public void receive(ImageMoments im) {
				ims.add(im);
			}
		});
		img.receive(new CS440Image(i));
		assertEquals(1, ims.size());
		ImageMoments im = ims.get(0);
		assertCloseTo(2.0D, im.m00);
		assertCloseTo(1.0D, im.m01);
		assertCloseTo(1.0D, im.m02);
		assertCloseTo(1.0D, im.m10);
		assertCloseTo(1.0D, im.m11);
		assertCloseTo(1.0D, im.m20);
		assertCloseTo(0.5D, im.x);
		assertCloseTo(0.5D, im.y);
		assertCloseTo(2.45D, im.L1);
		assertCloseTo(0D, im.L2);
	}
}
