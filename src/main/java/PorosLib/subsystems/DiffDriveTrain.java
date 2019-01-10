package PorosLib.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import PorosLib.systems.DifferentialDriver;
import PorosLib.util.MathHelper;

/**
 *
 */
public class DiffDriveTrain extends DriveTrain implements PidActionSubsys
{
	private DifferentialDriver driver;
	private boolean isSquared;
	
	private double rotateDeadband = 0;
	
    public DiffDriveTrain(SpeedController leftController, SpeedController rightController)
    {
    	driver = new DifferentialDriver(leftController , rightController);
    }
    
    public DiffDriveTrain(SpeedController leftController, SpeedController rightController, boolean isReversed)
    {
    	driver = new DifferentialDriver(leftController , rightController);
    	this.SetIsReversed(isReversed);
    }   
    
    public void ArcadeDrive(double speed, double rotate, double maxOutput)
    {
    	this.driver.setMaxOutput(maxOutput);
    	
    	rotate = MathHelper.handleDeadband(rotate, rotateDeadband);
    	
    	if(this.IsReversed())
    	{
    		driver.arcadeDrive(-speed,-rotate, this.isSquared);
    	}
    	else
    	{
    		driver.arcadeDrive(speed, rotate, this.isSquared);
    	}
    }
    
    public void TankDrive(double leftSpeed ,double rightSpeed, double maxOutput)
    {
    	this.driver.setMaxOutput(maxOutput);
    	
    	if(Math.abs(leftSpeed - rightSpeed) < rotateDeadband)
    	{
    		double speed = (leftSpeed + rightSpeed)/2;
    		leftSpeed = speed;
    		rightSpeed = speed;
    	}
    	
    	if(this.IsReversed())
    	{
        	driver.tankDrive(-leftSpeed , -rightSpeed, this.isSquared);
    	}
    	else
    	{
        	driver.tankDrive(leftSpeed , rightSpeed, this.isSquared);
    	}
    }
    
    public int getRawLeftPosition()
    {
    	return 0;
    }
    
    public int getRawRightPosition()
    {
    	return 0;
    }
    
    public double getHeading()
    {
    	return 0;
    }
    
    public void SetIsSquared (boolean isSquared)
    {
    	this.isSquared = isSquared;
    }
    
    public boolean GetIsSquared ()
    {
    	return this.isSquared;
    }
    
    public void PidTurnInPlace(double output)
    {
    	driver.PidTurnInPlace(output);
    }
    
    public void PidDrive(double output)
    {
    	driver.PidDrive(output);
    }
    
    public void setRotateDeadband(double deadband)
    {
    	this.rotateDeadband = deadband;
    }
    
    public double getRotateDeadband()
    {
    	return this.rotateDeadband;
    }
    
	@Override
	public void StopSystem()
	{
		this.driver.stopMotor();
	}
    

	@Override
	public Subsystem GetSubsystem() 
	{
		return this;
	}
	

}



