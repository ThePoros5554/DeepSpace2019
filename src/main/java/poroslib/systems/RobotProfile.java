package poroslib.systems;

public class RobotProfile 
{
	private static RobotProfile instance;
	
	public static RobotProfile getRobotProfile()
	{			
		if(instance == null)
		{
			instance = new RobotProfile();
		}
		
		return instance;
	}
	
	public static void setRobotProfile(RobotProfile profile)
	{
		instance = profile;
	}
	
	private double pathTimeStep;
	private double autoKv;
	private double autoKa;
	private double wheelbaseWidth;
	private int driveEncTicksPerRevolution;
	private double wheelDiameter;
	
	
	public double getPathTimeStep()
	{
		return this.pathTimeStep;
	}
	
	public void setPathTimeStep(double pathTimeStep)
	{
		this.pathTimeStep = pathTimeStep;
	}
	
	public double getAutoKV()
	{
		return this.autoKv;
	}
	
	public void setAutoKV(double autoKv)
	{
		this.autoKv = autoKv;
	}
	
	public double getAutoKA()
	{
		return this.autoKa;
	}
	
	public void setAutoKA(double autoKa)
	{
		this.autoKa = autoKa;
	}
	
	public double getWheelbaseWidth()
	{
		return this.wheelbaseWidth;
	}
	
	public void setWheelbaseWidth(double wheelbaseWidth)
	{
		this.wheelbaseWidth = wheelbaseWidth;
	}
	
	public int getDriveEncTicksPerRevolution()
	{
		return this.driveEncTicksPerRevolution;
	}
	
	public void setDriveEncTicksPerRevolution(int driveEncTicksPerRevolution)
	{
		this.driveEncTicksPerRevolution = driveEncTicksPerRevolution;
	}
	
	public void setWheelDiameter(double wheelDiameter)
	{
		this.wheelDiameter = wheelDiameter;
	}
	
	public double getWheelDiameter()
	{
		return this.wheelDiameter;
	}
}
