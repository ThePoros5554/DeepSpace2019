package poroslib.commands;

import edu.wpi.first.wpilibj.command.Command;
import poroslib.subsystems.Drivetrain;

/**
 *
 */
public class ReverseDrivetrain extends Command {

	Drivetrain drivetrain;
	
    public ReverseDrivetrain(Drivetrain drivetrain) 
    {
    	this.drivetrain = drivetrain;
    	requires(this.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	if(this.drivetrain.IsReversed() == true)
    	{
    		this.drivetrain.SetIsReversed(false);
    	}
    	else
    	{
    		this.drivetrain.SetIsReversed(true);
    	}
    }



    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return true;
    }

}
