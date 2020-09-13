import java.awt.Color;
import java.io.File;

public class Camera 
{
	/**
	 * In radians per tick.
	 */
	private final double TURN = 2 * 3.14D / 250.0D;
	private final double STRAFE = 0.05D;
	private final double FORWARD = 0.05D;
	private final double QUAR = 2 * 3.14D / 4;
	
	private Screen screen;
	private Input input;
	
	private double posX;
	private double posY;
	
	private Vector dir; // Current direction.
	private Vector moveDir; // Current moving vector.
	private Vector plane; // Plane vector.
	
	private Texture[] textures;
	
	public Camera(Screen screen, Input input, double posX, double posY, double dirX, double dirY, double planeX, double planeY)
	{
		this.screen = screen;
		this.input = input;
		this.posX = posX;
		this.posY = posY;
		this.dir = new Vector(dirX, dirY);
		this.moveDir = new Vector(dirX, dirY);
		this.plane = new Vector(planeX, planeY);
		this.textures = new Texture[5];
		
		textures[0] = new Texture(new File("src/brick.png"));
		textures[1] = new Texture(new File("src/brick_4.png"));
		textures[2] = new Texture(new File("src/wood.png"));
		textures[3] = new Texture(new File("src/clay.png"));
		textures[4] = new Texture(new File("src/sky.png"));
	}

	public void update()
	{
		for (int x = 0; x < screen.width; x++)
		{
			for (int y = 0; y < screen.height; y++)
			{
				screen.pixels[y * screen.width + x] = 0x1;
			}
			for (int y = screen.height / 2; y < screen.height; y++)
			{
				screen.pixels[y * screen.width + x] = 0x0;
			}
			double cameraX = 2 * x / (double)(screen.width) - 1;
			// Here, we calculate the x position along the camera plane. Note that the camera plane isn't actually 2D, 
			// since this engine is rendering a 2D map. The x position has been set to follow these rules: the left side 
			// of the plane is -1, the center 0, and the far right side +1.
			Vector rayDir = new Vector(this.dir.getX() + (this.plane.getX() * cameraX),
										this.dir.getY() + (this.plane.getY() * cameraX));
			// Some voodoo shit. These calculate the ray direction vector's x and y values.
			// The direction of the ray is found using the sum of the direction vector and a multiple of the plane
			// vector. Usually this multiple is 0 < x < 1.
			// Out of this, the direction of the ray can be calculated as was explained earlier: as the sum of the
			// direction vector, and a part of the plane vector. This has to be done both for the x and y coordinate 
			// of the vector (since adding two vectors is adding their x-coordinates, and adding their y-coordinates).\
			
			// Which box of the map we're in
		    int mapX = (int)this.posX;
		    int mapY = (int)this.posY;
		      
		    // Length of ray from current position to next x or y-side
		    double sideDistX;
		    double sideDistY;

		    // Length of ray from one x or y-side to next x or y-side
		    double deltaDistX = Math.abs(1 / rayDir.getX());
		    double deltaDistY = Math.abs(1 / rayDir.getY());
		    double perpWallDist; // Calculate this later. Length of ray.

		    // What direction to step in x or y-direction (either +1 or -1)
		    int stepX;
		    int stepY;
		    
		    int side = 0; // Was a Yside or a Xside wall hit?
		    boolean hit = false; // Have we hit a wall yet?
		    
		    // Calculate step and initial sideDist.
		    if (rayDir.getX() < 0) // If the ray is going in the negative x direction, stepX has to be negative.
		    {
		    	stepX = -1;
		    	sideDistX = (posX - mapX) * deltaDistX; // Calculates the first X-side the ray hits. 
		    }
		    else // If the ray is going in the positive x direction, stepX has to be positive.
		    {
		    	stepX = 1;
		        sideDistX = (mapX + 1.0D - posX) * deltaDistX; // Same thing.
		    }
		    if (rayDir.getY() < 0) // If the ray is going in the negative y direction, stepY has to be negative.
		    {
		        stepY = -1;
		        sideDistY = (posY - mapY) * deltaDistY; // Calculates the initial distance to the first y-side hit.
		    } 
		    else // If the ray is going in the positive y direction, stepY has to be positive.
		    {
		        stepY = 1;
		        sideDistY = (mapY + 1.0D - posY) * deltaDistY; // Same thing.
		    }
			
		    // This is the DDA algorithm. 
			while (!hit)
			{
				// Jump to next map square, OR in x-direction, OR in y-direction.
		        if (sideDistX < sideDistY) // If the current position's x distance to the closest x-side is smaller than the Y component, do this.
		        {
		        	sideDistX += deltaDistX; // Add to the current position the deltaX. This is the distance from the current Xside to the next Xside.
		        	mapX += stepX; // Add to the mapX the stepX. In this case it could be either -1 or +1. 
		        	side = 0; // This hit an x side.
		        }
		        else
		        {
		        	sideDistY += deltaDistY; // Add to the current position's Y the deltaY. This is the distance from the current Yside to the next Yside.
		        	mapY += stepY; // Add to the mapY the stepY. 
		        	side = 1; // This hit a y side. 
		        }
		        
		        // Check if ray has hit a wall.
		        if (Game.LEVEL[mapX][mapY] > 0) hit = true;
			}
			
			// Next, calculate the distance of the ray.
			// To avoid the fish eye effect, which can be quite nauseating, we will instead find the perpendicular distance from the camera plane rather than the Euclidean distance.
			if (side == 0) // If the ray hit the wall on it's x side, then do this shit:
				perpWallDist = (mapX - posX + (1 - stepX) / 2) / rayDir.getX(); 
				// This is the perpendicular wall distance to the camera plane. mapX - posX is the x distance from the current player position and the wallX.
				// (1 - stepX)/2 corrects for error like the addition of the + 1.0D for sideDist above.
				// Dividing by rayDirX makes the length longer in cases when the ray isn't perpendicular to the x axis (happens most if not all the time)
		    else           
		    	perpWallDist = (mapY - posY + (1 - stepY) / 2) / rayDir.getY(); // Same thing in the y direction.
			
			//Calculate height of line to draw on screen
			int h = screen.height;
		    int lineHeight = (int)(h / perpWallDist);

		    //calculate lowest and highest pixel to fill in current stripe
		    int drawStart = -lineHeight / 2 + h / 2;
		    
		    if(drawStart < 0) drawStart = 0;
		    int drawEnd = lineHeight / 2 + h / 2;
		    if(drawEnd >= h) drawEnd = h - 1;
		    
		    //texturing calculations
		    int texNum = Game.LEVEL[mapX][mapY] - 1; //1 subtracted from it so that texture 0 can be used!
		    Texture texture = textures[texNum];

		    //calculate value of wallX. This is required to know which x-coordinate of the texture we have to use
		    double wallX; //where exactly the wall was hit
		    if (side == 0) wallX = posY + perpWallDist * rayDir.getY(); // Find wallX by using the ray y variable, and the perp wall distance. Why not sidedist?
		    else           wallX = posX + perpWallDist * rayDir.getX();
		    wallX -= (int)(wallX); // Becomes a number between 0 and 1.

		    // Calculates the x coordinate on the texture using wallx.
		    int texX = (int)(wallX * (double)(texture.getWidth())); // Calculates the x coordinate on the texture. wallX is a double between 0 and 1.
		    if(side == 0 && rayDir.getX() > 0) texX = texture.getWidth() - texX - 1; // x side
		    if(side == 1 && rayDir.getY() < 0) texX = texture.getWidth() - texX - 1; // y side
		    
		    // How much to increase the texture coordinate per screen pixel
		    double step = 1.0D * texture.getWidth() / lineHeight;
		    // Starting texture coordinate
		    double texPos = (drawStart - h / 2 + lineHeight / 2) * step;
		   
		    texture.render(screen, texPos, texX, step, side, x, drawStart, drawEnd);
		}

		Vector lStrafeDir = Vector.rotate(dir, QUAR);
		Vector rStrafeDir = Vector.rotate(dir, -QUAR);
		
	    if (input.forward)
	    {
	    	moveDir = Vector.add(moveDir, Vector.mul(dir, FORWARD));
	    }
	    if (input.backward)
	    {
	    	moveDir = Vector.add(moveDir, Vector.negate(Vector.mul(dir, FORWARD)));
	    }
	    if (input.lstrafe)
	    {
	    	moveDir = Vector.add(moveDir, Vector.mul(lStrafeDir, STRAFE));
	    }
	    if (input.rstrafe)
	    {
	    	moveDir = Vector.add(moveDir, Vector.mul(rStrafeDir, STRAFE));
	    }
	    else if (!input.forward && !input.backward && !input.lstrafe && !input.rstrafe)
	    {
	    	moveDir = new Vector(0.0D, 0.0D);
	    }
	    if (input.lturn)
	    {
	    	this.rotate(TURN);
	    }
	    if (input.rturn)
	    {
	    	this.rotate(-TURN);
	    }
	    if (input.mMoving)
	    {
	    	// Sort of like an enhanced pointer precision
	    	// Allows for more accurate turning based on smaller mouse movements using input.mouse
	    	double rot = (double)input.distX / 500.00D; // * (input.mouse / 10000.0D); 
	    	double sensitivity = 5.00D;
	    	
	    	if (rot != rot) rot = 0;
	    	
	    	this.rotate(-rot * sensitivity);
		    
		    input.mMoving = false;
	    }
	    
	    this.collisionCheck();
	    this.moveCamera();
	    moveDir = Vector.zero();
	}
	
	public void rotate(double rot)
	{
	    dir = Vector.rotate(dir, rot);
	    plane = Vector.rotate(plane, rot);
	}
	
	public void moveCamera()
	{
		posX += moveDir.getX();
		posY += moveDir.getY();
	}
	
	public void collisionCheck()
	{ 
		double hitbox = 1.0D;   	
		boolean x_side = false;
	    // Circle is x^2 + y^2 = hitbox^2. useful?
		
		if (Game.LEVEL[(int)(posX + moveDir.getX())][(int)(posY + moveDir.getY())] > 0)
		{
			
			System.out.println("time: " + System.currentTimeMillis() + ", x_side?: " + x_side);
		}	
	}
}
