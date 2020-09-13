
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable 
{
	private static final long serialVersionUID = 1L;
	public static double FPS = 0.0D;
	public static int[][] LEVEL;

	private final int TICKRATE = 10000000;
	private final int WIDTH = 1080;
    private final int HEIGHT = WIDTH / 16 * 9;
    private final String title = "render";
    
    private boolean run;
    
    private Input input;
    private Thread thread;
	private Screen screen;
	private Camera camera;
	
    public Game() 
    {
    	this.setSize(WIDTH, HEIGHT);
    	this.setTitle(title);
    	this.setResizable(false);
    	this.setLayout(new BorderLayout());
    	this.setLocationRelativeTo(null);
    	this.setUndecorated(false);
    	this.setVisible(true);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
    	
    	run = true;
    	input = new Input();
    	screen = new Screen(input, this.WIDTH, this.HEIGHT);
    	camera = new Camera(screen, input, 6.0D, 6.0D, 1.0D, 0, 0.00D, -0.85D);
    	
    	this.add(screen);
   		
   		thread = new Thread(this, "Game-Thread");
   		thread.start();
    }
    
    public void run()
	{
		long pastTime = System.nanoTime();
		long currentTime;
		double seconds = 0;

		while (run)
		{	
			currentTime = System.nanoTime();
			if (input.escape) run = false; 
				
			if (currentTime - pastTime >= TICKRATE)
			{
				seconds = (currentTime - pastTime) / 1e9D;
				FPS = 1.0D / seconds;
				pastTime = currentTime;
				camera.update();
				screen.render();
			}
		}
		System.exit(0);
	}
    
    public static void loadLevel(String levelName)
    {
    	int[][] level;
    	BufferedImage img = null;
		
		try 
		{
			img = ImageIO.read(new File(levelName));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		int wMap = img.getWidth();
		int hMap = img.getHeight();
		int val = 0;
		level = new int[wMap][hMap];
		
		for (int x = 0; x < wMap; x++)
		{
			for (int y = 0; y < hMap; y++)
			{
				val = img.getRGB(x, y) ^ 0xFF000000;
				if (val == 0xFFFFFF)
					level[x][y] = 0;
				else if (val == 0xFF0000)
					level[x][y] = 2;
				else if (val == 0x0000FF)
					level[x][y] = 4;
				else if (val == 0x000000)
					level[x][y] = 1;
			}
		}
		
		Game.LEVEL = level;
    }
    
    public int getWidth()
    {
    	return this.WIDTH;
    }

    public int getHeight()
    {
    	return this.HEIGHT;
    }

    public static void main(String[] args) 
    {
    	Game.loadLevel("src/level.png");
        new Game();
    }
}