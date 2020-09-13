
public class Vector 
{
	private double x;
	private double y;
	
	public Vector(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public static Vector add(Vector a, Vector b)
	{
		return new Vector(a.getX() + b.getX(), a.getY() + b.getY());
	}
	
	public static Vector sub(Vector a, Vector b)
	{
		return new Vector(a.getX() - b.getX(), a.getY() - b.getY());
	}
	
	public static Vector mul(Vector a, double s)
	{
		return new Vector(a.getX() * s, a.getY() * s);
	}
	
	public static Vector div(Vector a, double s)
	{
		return new Vector(a.getX() / s, a.getY() / s);
	}
	
	public static Vector rotate(Vector a, double quotient)
	{
		double x = a.getX() * Math.cos(quotient) - a.getY() * Math.sin(quotient);
		double y = a.getX() * Math.sin(quotient) + a.getY() * Math.cos(quotient);
		return new Vector(x, y);
	}
	
	public static Vector zero()
	{
		return new Vector(0.0D, 0.0D);
	}
	
	public static double length(double a, double b)
	{
		return Math.hypot(a, b);
	}
	
	public static Vector angle() //Quicker arctan function.
	{
		
	}
	
	public static Vector negate(Vector a)
	{
		return new Vector(-1.0D * a.getX(), -1.0D * a.getY());
	}
	
	public void scale(double factor)
	{
		this.x *= factor;
		this.y *= factor;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
}
