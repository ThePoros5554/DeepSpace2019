package PorosLib.commands;

import edu.wpi.first.wpilibj.command.Command;
import PorosLib.subsystems.MechDriveTrain;
import PorosLib.triggers.SmartJoystick;

/**
 *
 */
public class DriveMechanum extends Command{
	
	private MechDriveTrain driveTrain;
	private SmartJoystick joy;
	
	private double maxOutput = 1;

    public DriveMechanum(MechDriveTrain driveTrain, SmartJoystick joy)
    {   
    	this.driveTrain = driveTrain;
    	this.joy = joy;
    	requires(this.driveTrain);
    }

    
    protected void initialize()
    {
    	
    }

    
    protected void execute()
    {
    	double speedValue = this.joy.GetSpeedAxis();
    	double rotateValue = this.joy.GetRotateAxis();
    	double twistValue =  this.joy.GetTwistAxis();
    	
    	
    	if(this.driveTrain.IsRanged())
    	{
    		this.maxOutput = this.joy.GetSlider();
    	}
    	else
    	{
    		this.maxOutput = 1;
    	}
    	
    	this.driveTrain.MechanumDrive(speedValue, rotateValue, twistValue, 0, this.maxOutput);
    }

    
    protected boolean isFinished()
    {
        return false;
    }

    
    protected void end()
    {
    	this.driveTrain.MechanumDrive(0, 0, 0, 0, this.maxOutput);
    }

    
    protected void interrupted()
    {
    	end();
    }
}
