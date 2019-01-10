package PorosLib.commands;

import edu.wpi.first.wpilibj.command.Command;
import PorosLib.subsystems.DiffDriveTrain;
import PorosLib.triggers.SmartJoystick;

/**
 *
 */
public class TankDrive extends Command
{

	private DiffDriveTrain driveTrain;
	private SmartJoystick leftJoy;
	private SmartJoystick rightJoy;
	
	private double maxOutput = 1;
	
    public TankDrive(DiffDriveTrain driveTrain, SmartJoystick leftJoy, SmartJoystick rightJoy)
    {
    	this.driveTrain = driveTrain;
    	this.leftJoy = leftJoy;
    	this.rightJoy = rightJoy;
        requires(this.driveTrain);
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

    	if(this.driveTrain.IsRanged())
    	{
    		this.maxOutput = this.rightJoy.GetSlider();
    	}
    	else
    	{
    		this.maxOutput = 1;
    	}
    	
    	this.driveTrain.TankDrive(leftValue, rightValue, this.maxOutput);
    }

    protected boolean isFinished()
    {
        return false;
    }

    protected void end()
    {
    	this.driveTrain.TankDrive(0, 0, this.maxOutput);
    }

    protected void interrupted()
    {
    	end();
    }
}
