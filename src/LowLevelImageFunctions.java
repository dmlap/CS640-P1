/*
 * YOU SHOULD PROBABLY NOT BE NEEDING TO EDIT THIS CODE
 *
 * LowLevelImageFunction class defines STATIC methods to manipulate and
 * handle images and pixels. 
 *
 */

import javax.swing.*;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.awt.*;

/**
 * 
 * Low low utility functions.
 * 
 * @author Sam Epstein
 *
 */
class LowLevelImageFunctions {
	final static int GREY_SCALE = 0;
	final static int RED = 0;
	final static int GREEN = 0;
	final static int BLUE = 0;

	public static BufferedImage toBufferedImage(Image image) {
			if (image instanceof BufferedImage) {
					return (BufferedImage)image;
			}
			BufferedImage bi = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_RGB);
			Graphics bg = bi.getGraphics();
			bg.drawImage(image, 0, 0, null);
    		bg.dispose();
    		return bi;
	}

	public static double [][] toDataArray (BufferedImage image, int ColorFlag)
	{
		return toDataArray(image, ColorFlag, null);
	}

	public static double [][] toDataArray (BufferedImage image, int ColorFlag, double [][] dataArray)  {
		if(dataArray==null)
			dataArray = new double [image.getHeight()][image.getWidth()];

		for (int y = 0; y < image.getHeight(); y++)
			for (int x = 0; x < image.getWidth(); x++) {
				Color rgb = new Color(image.getRGB(x, y));
				switch (ColorFlag) {
				   case 0 :
					   dataArray[y][x] = (double) (((double)(rgb.getRed() + rgb.getGreen() + rgb.getBlue())) / 3.0);
					   break;
				   case 1 :
					   dataArray[y][x] = (double) rgb.getRed();
					   break;
				   case 2 :
					   dataArray[y][x] = (double) rgb.getGreen();
					   break;
				   case 3 :
					   dataArray[y][x] = (double) rgb.getBlue();
					   break;
				}
			}
		return  dataArray;
	}

	// Returns a new BufferedImage object which is a image copy of the original
	public static BufferedImage CopyOf (BufferedImage image)  {
		BufferedImage bi = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_RGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(image, 0, 0, null);
	 	bg.dispose();
    	return bi;
	}

	// Convert a colored image to a greyscale one
	public static BufferedImage toGreyScale (BufferedImage image)  {
		BufferedImage temp =  new BufferedImage(image.getWidth(), image.getHeight(), 1);
		ColorConvertOp convert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		convert.filter(image, temp);
		return temp;
	}

}