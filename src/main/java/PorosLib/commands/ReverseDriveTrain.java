package PorosLib.commands;

import edu.wpi.first.wpilibj.command.Command;
import PorosLib.subsystems.DriveTrain;

/**
 *
 */
public class ReverseDriveTrain extends Command {

	DriveTrain driveTrain;
	
    public ReverseDriveTrain(DriveTrain driveTrain) 
    {
    	this.driveTrain = driveTrain;
    	requires(this.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	if(this.driveTrain.IsReversed() == true)
    	{
    		this.driveTrain.SetIsReversed(false);
    	}
    	else
    	{
    		this.driveTrain.SetIsReversed(true);
    	}
    }



    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return true;
    }

}
