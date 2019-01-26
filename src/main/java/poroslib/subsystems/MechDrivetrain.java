package poroslib.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import poroslib.systems.MechaumDriver;

/**
 *
 */
public class MechDrivetrain extends Drivetrain implements PidActionSubsys {

	private MechaumDriver driver;
		
    public MechDrivetrain(SpeedController frontLeftMotor, SpeedController rearLeftMotor,
            SpeedController frontRightMotor, SpeedController rearRightMotor)
    {
    	this.driver = new MechaumDriver(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    }
    
    public MechDrivetrain(SpeedController frontLeftMotor, SpeedController rearLeftMotor,
            SpeedController frontRightMotor, SpeedController rearRightMotor, boolean isReversed)
    {
    	this.driver = new MechaumDriver(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    	this.SetIsReversed(isReversed);
    }

    public void MechanumDrive(double sidewaysSpeed, double forwardSpeed, double rotationSpeed, double gyroAngle, double maxOutput)
    {
    	this.driver.setMaxOutput(maxOutput);
    	
    	if(this.IsReversed())
    	{
    		driver.driveCartesian(-sidewaysSpeed, -forwardSpeed, -rotationSpeed, gyroAngle);

    	}
    	else
    	{
    		driver.driveCartesian(-sidewaysSpeed, forwardSpeed, -rotationSpeed, gyroAngle);
    	}
    }

	@Override
	public void StopSystem() 
	{
		driver.stopMotor();
	}

	public void PidDrive(double output)
	{
		driver.PidDrive(output);
	}
	
	public void PidTwist(double output)
	{
		driver.PidTwist(output);

	}
	
	public void PidTurnInPlace(double output)
	{
		if(this.IsReversed())
		{
			driver.PidTurnInPlace(output);
		}
		else
		{
			driver.PidTurnInPlace(-output);
		}
	}

	@Override
	public Subsystem GetSubsystem() 
	{
		return this;
	}
	
    public enum MechDrivingDirection
    {
    	Forward,
    	Sideways
    }
}

