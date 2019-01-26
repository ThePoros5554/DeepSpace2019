package poroslib.commands;

import edu.wpi.first.wpilibj.command.Command;
import poroslib.subsystems.DiffDrivetrain;
import poroslib.triggers.SmartJoystick;

/**
 *
 */
public class ArcadeDrive extends Command
{

	private DiffDrivetrain drivetrain;
	private SmartJoystick joy;
	
	private double maxOutput = 1;
	
    public ArcadeDrive(DiffDrivetrain drivetrain, SmartJoystick joy)
    {
    	this.drivetrain = drivetrain;
    	this.joy = joy;
        requires(this.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize()
    {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
    	double speedValue = this.joy.GetSpeedAxis();
    	double rotateValue =  this.joy.GetRotateAxis();
    	
    	if(this.drivetrain.IsRanged())
    	{
    		this.maxOutput = this.joy.GetSlider();
    	}
    	else
    	{
    		this.maxOutput = 1;
    	}
    	
		this.drivetrain.arcadeDrive(speedValue, rotateValue, maxOutput);
    }

    protected boolean isFinished()
    {
        return false;
    }

    protected void end()
    {
    	this.drivetrain.arcadeDrive(0, 0, this.maxOutput);
    }


    protected void interrupted()
    {
    	end();
    }
}
