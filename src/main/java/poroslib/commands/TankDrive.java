package poroslib.commands;

import edu.wpi.first.wpilibj.command.Command;
import poroslib.subsystems.DiffDrivetrain;
import poroslib.triggers.SmartJoystick;

/**
 *
 */
public class TankDrive extends Command
{

	private DiffDrivetrain drivetrain;
	private SmartJoystick leftJoy;
	private SmartJoystick rightJoy;
	
	private double maxOutput = 1;
	
    public TankDrive(DiffDrivetrain drivetrain, SmartJoystick leftJoy, SmartJoystick rightJoy)
    {
    	this.drivetrain = drivetrain;
    	this.leftJoy = leftJoy;
    	this.rightJoy = rightJoy;
        requires(this.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize()
    {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
    	double leftValue = leftJoy.GetSpeedAxis();
    	double rightValue = rightJoy.GetSpeedAxis();

    	if(this.drivetrain.IsRanged())
    	{
    		this.maxOutput = this.rightJoy.GetSlider();
    	}
    	else
    	{
    		this.maxOutput = 1;
    	}
    	
    	this.drivetrain.tankDrive(leftValue, rightValue, this.maxOutput);
    }

    protected boolean isFinished()
    {
        return false;
    }

    protected void end()
    {
    	this.drivetrain.tankDrive(0, 0, this.maxOutput);
    }

    protected void interrupted()
    {
    	end();
    }
}
