package PorosLib.commands;

import edu.wpi.first.wpilibj.command.Command;
import PorosLib.subsystems.DiffDriveTrain;

/**
 *
 */
public class SwitchIsSquared extends Command {

	DiffDriveTrain driveTrain;
	
    public SwitchIsSquared(DiffDriveTrain driveTrain) 
    {
        this.driveTrain = driveTrain;
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	if(this.driveTrain.GetIsSquared())
    	{
    		this.driveTrain .SetIsSquared(false);
    	}
    	else
    	{
    		this.driveTrain .SetIsSquared(true);
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
