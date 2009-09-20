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
	public void checkerboard() {
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
	
	@Test
	public void upperLeft() {
		BufferedImage i = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		i.setRGB(0, 0, Color.WHITE.getRGB());
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
		assertCloseTo(1.0D, im.m00);
		assertCloseTo(0D, im.m01);
		assertCloseTo(0D, im.m02);
		assertCloseTo(0D, im.m10);
		assertCloseTo(0D, im.m11);
		assertCloseTo(0D, im.m20);
		assertCloseTo(0D, im.x);
		assertCloseTo(0D, im.y);
		assertCloseTo(0D, im.L1);
		assertCloseTo(0D, im.L2);
	}
	
	@Test
	public void big() {
		BufferedImage i = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		int w = Color.WHITE.getRGB();
		i.setRGB(5, 3, w);
		i.setRGB(5, 4, w);
		i.setRGB(5, 5, w);
		i.setRGB(5, 6, w);
		i.setRGB(5, 7, w);

		i.setRGB(6, 4, w);
		i.setRGB(6, 5, w);
		i.setRGB(6, 6, w);
		i.setRGB(6, 7, w);
		
		i.setRGB(7, 5, w);
		i.setRGB(7, 6, w);
		i.setRGB(7, 7, w);
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
		assertCloseTo(12D, im.m00);
		assertCloseTo(65D, im.m01);
		assertCloseTo(371D, im.m02);
		assertCloseTo(70D, im.m10);
		assertCloseTo(383D, im.m11);
		assertCloseTo(416D, im.m20);
		assertCloseTo(5.833D, im.x);
		assertCloseTo(5.416D, im.y);
		assertCloseTo(4.483D, im.L1);
		assertCloseTo(2.5465D, im.L2);
	}
}
