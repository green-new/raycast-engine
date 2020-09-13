import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Screen extends Canvas
{
	private static final long serialVersionUID = 1L;
	public int[] pixels;
	public int width;
	public int height;
	
	private BufferedImage img;
    
    public Screen(Input input, int width, int height)
    {
    	this.setSize(width, height);
    	this.addKeyListener(input);
    	this.addMouseMotionListener(input);
    	
    	pixels = new int[width * height];
    	this.width = width;
    	this.height = height;
    	this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
    
    public void clear()
    {
    	// Faster clear algorithm.
    	for (int i = 1; i < width * height; i += i) 
    	{
    	    System.arraycopy(pixels, 0, pixels, i, ((width * height - i) < i) ? (width * height - i) : i);
    	}
    }
    
    public void render() 
    {   
        BufferStrategy bs = getBufferStrategy();
        
        if (bs == null)
        {
        	createBufferStrategy(3);
        	return;
        }
        
        // HOW THE FUCK DOES THIS WORK????
        // BufferedImage isn't even directly changed. How does this work?
        
        // Recent changes: img.setRGB is too slow. Like, really fucking slow. Use a buffer
        
        Graphics g = bs.getDrawGraphics();
        int[] buffer = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        System.arraycopy(pixels, 0, buffer, 0, pixels.length);
        
        g.drawImage(img, 0, 0, null);
        g.setColor(Color.WHITE);
        g.drawString("fps: " + Game.FPS, 0, 9);
        g.drawString("(" + width + ", " + height + ")", 0, 20);
        
        g.dispose();
        bs.show();
        
        clear();
    }
}
