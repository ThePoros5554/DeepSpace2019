package frc.poroslib.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.poroslib.systems.DifferentialDriver;
import frc.poroslib.util.MathHelper;

/**
 *
 */
public class DiffDrivetrain extends Drivetrain implements PidActionSubsys
{
	private DifferentialDriver driver;
	private boolean isSquared;
	
	private double rotateDeadband = 0;
	
    public DiffDrivetrain(SpeedController leftController, SpeedController rightController)
    {
    	driver = new DifferentialDriver(leftController , rightController);
    }
    
    public DiffDrivetrain(SpeedController leftController, SpeedController rightController, boolean isReversed)
    {
    	driver = new DifferentialDriver(leftController , rightController);
    	this.SetIsReversed(isReversed);
    }   
    
    public void arcadeDrive(double speed, double rotate, double maxOutput)
    {
    	this.driver.setMaxOutput(maxOutput);
    	
    	rotate = MathHelper.handleDeadband(rotate, rotateDeadband);
    	
    	if (this.IsReversed())
    	{
    		driver.arcadeDrive(-speed,-rotate, this.isSquared);
    	}
    	else
    	{
    		driver.arcadeDrive(speed, rotate, this.isSquared);
    	}
    }
    
    public void tankDrive(double leftSpeed ,double rightSpeed, double maxOutput)
    {
    	this.driver.setMaxOutput(maxOutput);
    	
    	if (Math.abs(leftSpeed - rightSpeed) < rotateDeadband)
    	{
    		double speed = (leftSpeed + rightSpeed)/2;
    		leftSpeed = speed;
    		rightSpeed = speed;
    	}
    	
    	if (this.IsReversed())
    	{
        	driver.tankDrive(-leftSpeed , -rightSpeed, this.isSquared);
    	}
    	else
    	{
        	driver.tankDrive(leftSpeed , rightSpeed, this.isSquared);
    	}
	}
	
	public void GTADrive(double forwardSpeed, double backwardsSpeed, double turn, double pow)
	{    
	  double kPow = pow;	
	  double speed = forwardSpeed - backwardsSpeed;
	  double leftSpeed = Math.signum(speed) * Math.pow(Math.abs(speed), kPow);
	  double rightSpeed = Math.signum(speed) * Math.pow(Math.abs(speed), kPow);
	  turn = MathHelper.handleDeadband(turn, rotateDeadband);
  
	  this.driver.setMaxOutput(1);

	  if (turn < 0)
	  {
		leftSpeed *=  Math.pow(MathHelper.mapRange(0, -1, 1, 0, turn), kPow);
	  }
	  else
	  {
		rightSpeed *= Math.pow(MathHelper.mapRange(0, 1, 1, 0, turn), kPow);
	  }
	  
	  if (this.IsReversed())
	  {
		driver.tankDrive(-leftSpeed, -rightSpeed);
	  }
	  else
	  {
		driver.tankDrive(leftSpeed, rightSpeed);
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
    
    public void SetIsSquared(boolean isSquared)
    {
    	this.isSquared = isSquared;
    }
    
    public boolean GetIsSquared()
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



