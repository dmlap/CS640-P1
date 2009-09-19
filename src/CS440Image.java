
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/*****
*  Data type for manipulating individual pixels of an image. The original
*  image can be read from a file in JPEG, GIF, or PNG format, or the
*  user can create a blank image of a given size. Includes methods for
*  saving to a file.
*  
*  Remarks
*  -------
*   - pixel (0, 0) is upper left hand corner
*
*   - if JPEG read in is in grayscale, then you can only set the
*     color to a graycale value
 * 
 * @author Rui Li, Sam Epstein
 *
 */
public final class CS440Image 
{
    private BufferedImage image;    // the rasterized image
        
    /*********
     * @return the raw BufferedImage
     *********/
    public BufferedImage getRawImage(){return this.image;}
    
    /**
     * 
     * @param image the raw BufferedImage to set
     */
    public void setRawImage(BufferedImage image)
    {
    	this.image = image;
    }
    
    
	// create a blank w by h image
    public CS440Image(int w, int h) {
    	image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    }
    
    // create an image from an existing BufferedImage
    public CS440Image(BufferedImage image) {
    	this.image = image;
    }
    
    
    // create an image by reading in the PNG, GIF, or JPEG from a file name
    public CS440Image(String filename) {
    	this(new File(filename));
    }
    
    // create an image by reading in the PNG, GIF, or JPEG from a file
    public CS440Image(File file) {
        try { image = ImageIO.read(file); }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file: " + file);
        }
        if (image == null) {
            throw new RuntimeException("Invalid image file: " + file);
        }
    }
    
    // accessor methods
    public int height() { return image.getHeight(null); }
    public int width()  { return image.getWidth(null);  }

    // return Color of pixel (i, j)
    public Color get(int i, int j) {
        return new Color(image.getRGB(i, j));
    }
    
    // change color of pixel (i, j) to c
    public void set(int i, int j, Color c) {
        image.setRGB(i, j, c.getRGB());
    }

    // save to given filename - suffix must be png, jpg, or gif
    public void save(String filename) { save(new File(filename)); }

    // save to given filename - suffix must be png, jpg, or gif
    public void save(File file) {
        String filename = file.getName();
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        suffix = suffix.toLowerCase();
        if (suffix.equals("jpg") || suffix.equals("png")) {
            try { ImageIO.write(image, suffix, file); }
            catch (IOException e) { e.printStackTrace(); }
        }
        else {
            System.out.println("Error: filename must end in .jpg or .png");
        }
    }

}
