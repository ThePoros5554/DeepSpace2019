package frc.poroslib.commands;

import java.util.List;

import edu.wpi.first.wpilibj.command.Command;
import frc.poroslib.subsystems.MechSys;

/**
 *
 */
public class SetAllLimits extends Command 
{
	
	private boolean setLimits;
	private List<MechSys> subsystems;
	
    public SetAllLimits(List<MechSys> subsystems, boolean setLimits) 
    {
    	this.setLimits = setLimits;
    	this.subsystems = subsystems;
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {    	
    	for(MechSys sys : this.subsystems)
    	{
    		sys.SetIsLimited(this.setLimits);
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
