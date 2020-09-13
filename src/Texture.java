import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture 
{
	public int pixels[];
	public int[][] animation;
	private int width;
	private int height;
	private int aTickspeed;
	private int aIndex = 0;
	private int tick;
	private boolean doanimate;
	
	public Texture(File file)
	{
		BufferedImage img = null;
		doanimate = false;
		
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
		
		pixels = new int[width * height];
		this.aTickspeed = 20;
		
		for (int x = 0; x < height; x++)
		{
			for (int y = 0; y < width; y++)
			{
				pixels[x + y * width] = img.getRGB(x, y);
			}
		}
	}
	
	public Texture(int width, int height)
	{	
		this.width = width;
		this.height = height;
		
		this.pixels = new int[width * height];
		
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				this.pixels[x + (y * height)] = (x ^ y); // XOR texture
			}
		}
		
		this.doanimate = false;
		this.animation = new int[2][width * height];
		this.animation[0] = pixels;
		this.aTickspeed = 1000;
		
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				this.animation[1][x + (y * height)] = (x & y); // ??? texture
			}
		}
	}
	
	public void render(Screen screen, double texPos, int texX, double step, int side, int xo, int drawStart, int drawEnd)
	{
		this.animate();
		for(int y = drawStart; y < drawEnd; y++)
	    {
	    	int texY = (int)(texPos) & (this.getHeight() - 1);
	        texPos += step;
	        int color = this.pixels[this.getHeight() * texY + texX];
	        if(side == 1) color = (color >> 1) & 8355711;
	        screen.pixels[xo + (y * screen.width)] = color;
	    }
	}
	
	public void animate() // Will only run when rendered.
	{
		if (!this.doanimate) return;
		this.tick++;
		
		if (tick % aTickspeed == 0)
		{
			this.tick = 0;
			this.pixels = animation[aIndex];
			this.aIndex++;
			
			if (this.aIndex > 1)
				this.aIndex = 0;
		}
	}

	public int getWidth() 
	{
		return this.width;
	}

	public int getHeight() 
	{
		return this.height;
	}
}
