package poroslib.commands;

import edu.wpi.first.wpilibj.command.Command;
import poroslib.subsystems.DiffDrivetrain;

/**
 *
 */
public class SwitchIsSquared extends Command {

	DiffDrivetrain drivetrain;
	
    public SwitchIsSquared(DiffDrivetrain drivetrain) 
    {
        this.drivetrain = drivetrain;
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	if(this.drivetrain.GetIsSquared())
    	{
    		this.drivetrain .SetIsSquared(false);
    	}
    	else
    	{
    		this.drivetrain .SetIsSquared(true);
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
