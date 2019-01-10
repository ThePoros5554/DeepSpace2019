package PorosLib.commands;

import edu.wpi.first.wpilibj.command.Command;
import PorosLib.subsystems.DiffDriveTrain;
import PorosLib.triggers.SmartJoystick;

/**
 *
 */
public class ArcadeDrive extends Command
{

	private DiffDriveTrain driveTrain;
	private SmartJoystick joy;
	
	private double maxOutput = 1;
	
    public ArcadeDrive(DiffDriveTrain driveTrain, SmartJoystick joy)
    {
    	this.driveTrain = driveTrain;
    	this.joy = joy;
        requires(this.driveTrain);
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
    	
    	if(this.driveTrain.IsRanged())
    	{
    		this.maxOutput = this.joy.GetSlider();
    	}
    	else
    	{
    		this.maxOutput = 1;
    	}
    	
		this.driveTrain.ArcadeDrive(speedValue, rotateValue, maxOutput);
    }

    protected boolean isFinished()
    {
        return false;
    }

    protected void end()
    {
    	this.driveTrain.ArcadeDrive(0, 0, this.maxOutput);
    }


    protected void interrupted()
    {
    	end();
    }
}
