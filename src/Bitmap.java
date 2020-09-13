
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bitmap 
{
	public int[] pixels; // Change this into BufferedImage in the future.
	private int xo;
	private int yo;
	private int width;
	private int height;
	
	/**
	 *  Array of images that handle animation. Loaded into memory on class initialization.
	 */
	public BufferedImage[] animation; // Work on optimization.
	
	/**
	 * Current animation sprite (index).
	 */
	private int animate = 0;
	
	/**
	 * Number of ticks this texture has been alive.
	 */
	private int tick = 0; 
	
	/**
	 * Length of one image from the animation.
	 */
	public final int ANI_LENGTH = 200;
	
	/**
	 * Should we play the animation?
	 */
	private boolean doAnimation;
	
	public Screen screen;
	
	/**
	 * Creates a bitmap from a BufferedImage
	 * @param screen
	 * @param image
	 * @param xo
	 * @param yo
	 */
	public Bitmap(BufferedImage img, int xo, int yo)
	{
		this.width = img.getWidth();
		this.height = img.getHeight();
		pixels = new int[width * height];
		this.xo = xo;
		this.yo = yo;
		this.doAnimation = false;
		
		for (int x = 0; x < height; x++)
		{
			for (int y = 0; y < width; y++)
			{
				pixels[x + y * width] = img.getRGB(x, y);
			}
		}
	}
	
	/**
	 * Generates a bitmap based on external image.
	 * @param file
	 */
	public Bitmap(File file, int xo, int yo)
	{
		BufferedImage img = null;
		
		try 
		{
			img = ImageIO.read(file);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		this.width = img.getWidth();
		this.height = img.getHeight();
		this.xo = xo;
		this.yo = yo;
		
		pixels = new int[width * height];
		
		for (int x = 0; x < height; x++)
		{
			for (int y = 0; y < width; y++)
			{
				pixels[x + y * width] = img.getRGB(x, y);
			}
		}
		
		doAnimation = true;
		
		animation = new BufferedImage[2];
		animation[0] = img;
		
		// Do some hard code to get the second sprite in the animation array.
		BufferedImage img2 = null;
		try 
		{
			img2 = ImageIO.read(new File("src/sprite2.png"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		animation[1] = img2; 
	}
	
	/**
	 * Generates a gradient bitmap.
	 * @param width
	 * @param height
	 */
	public Bitmap(int width, int height)
	{
		doAnimation = false;
		
		this.width = width;
		this.height = height;
		
		pixels = new int[width * height];
		
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				pixels[x + (y * height)] = (x ^ y); // XOR texture
			}
		}
	}
	
	void render()
	{
		this.animate();
		
   		for (int x = 0; x < this.height; x++)
   		{
   			for (int y = 0; y < this.width; y++)        		
   			{
    			screen.pixels[y+(screen.width * (x+this.yo))+this.xo] = this.pixels[y+(this.width * x)];
   			}
    	}
	}
	
	void animate()
	{
   		
		if (doAnimation)
		{
			tick++;
			
			if (tick % ANI_LENGTH == 0)
			{
				if (animate > animation.length - 1)
				{
					animate = 0;
				}
				
				for (int x = 0; x < height; x++)
				{
					for (int y = 0; y < width; y++)
					{
						pixels[x + y * width] = animation[animate].getRGB(x, y);
					}
				}
				animate++;
			}
		}
	}
	
	public BufferedImage getImage()
	{
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		img.setRGB(0, 0, width, height, pixels, 0, width);
		return img;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getXOffset()
	{
		return xo;
	}
	
	public int getYOffset()
	{
		return yo;
	}
}
