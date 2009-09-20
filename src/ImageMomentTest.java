import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ImageMomentTest {

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
		assertEquals(2, im.m00);
		assertEquals(1, im.m01);
		assertEquals(1, im.m02);
		assertEquals(1, im.m10);
		assertEquals(1, im.m11);
		assertEquals(1, im.m20);
		assertTrue("expected x = 0.5, found: " + im.x, im.x > 0.49D && im.x < 0.51D);
		assertTrue("expected y = 0.5, found: " + im.y, im.y > 0.49D && im.y < 0.51D);
		assertTrue("expected L1 = 2.45, found: " + im.L1, im.L1 > 2.44D && im.L1 < 2.46D);
		assertTrue("expected L2 = 0, found: " + im.L2, im.L2 > -0.1D && im.L2 < 0.1D);
	}
}
