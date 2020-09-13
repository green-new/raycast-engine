import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseMotionListener
{
	public boolean forward = false;
	public boolean backward = false;
	public boolean lstrafe = false;
	public boolean rstrafe = false;
	public boolean escape = false;
	public boolean lturn = false;
	public boolean rturn = false;
	public boolean mMoving;
	public boolean mDrag;
	
	private int oldDistX;
	public int distX;
	private int oldDistY;
	public int distY;
	
	public double mouse;
	
	@Override
	public void keyPressed(KeyEvent event) 
	{
		forward = (event.getKeyCode() == KeyEvent.VK_W ? true : forward);
		backward = (event.getKeyCode() == KeyEvent.VK_S ? true : backward);
		lstrafe = (event.getKeyCode() == KeyEvent.VK_A ? true : lstrafe);
		rstrafe = (event.getKeyCode() == KeyEvent.VK_D ? true : rstrafe);
		rturn = (event.getKeyCode() == KeyEvent.VK_RIGHT ? true : rturn);
		lturn = (event.getKeyCode() == KeyEvent.VK_LEFT ? true : lturn);
		escape = (event.getKeyCode() == KeyEvent.VK_ESCAPE ? true : escape);
	}

	@Override
	public void keyReleased(KeyEvent event) 
	{
		forward = (event.getKeyCode() == KeyEvent.VK_W ? false : forward);
		backward = (event.getKeyCode() == KeyEvent.VK_S ? false : backward);
		lstrafe = (event.getKeyCode() == KeyEvent.VK_A ? false : lstrafe);
		rstrafe = (event.getKeyCode() == KeyEvent.VK_D ? false : rstrafe);
		rturn = (event.getKeyCode() == KeyEvent.VK_RIGHT ? false : rturn);
		lturn = (event.getKeyCode() == KeyEvent.VK_LEFT ? false : lturn);
		escape = (event.getKeyCode() == KeyEvent.VK_ESCAPE ? false : escape);
	}

	@Override
	public void keyTyped(KeyEvent event) { }

	@Override
	public void mouseDragged(MouseEvent me) 
	{
		mDrag = true;
	}

	@Override
	public void mouseMoved(MouseEvent me) 
	{
		mMoving = true;
		distX = me.getLocationOnScreen().x - oldDistX;
		distY = me.getLocationOnScreen().y - oldDistY;
				
		oldDistX = me.getLocationOnScreen().x;
		oldDistY = me.getLocationOnScreen().y;
		
		double euclidDist = Math.sqrt(distX * distX - (distY * distY));
		mouse = euclidDist;
	}
}
