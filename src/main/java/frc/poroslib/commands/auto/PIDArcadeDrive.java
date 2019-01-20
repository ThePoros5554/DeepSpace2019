package frc.poroslib.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import frc.poroslib.subsystems.DiffDrivetrain;
import frc.poroslib.systems.PIDProcessor;

/**
 *
 */
public class PIDArcadeDrive extends Command {

	private DiffDrivetrain drivetrain;
	
	private PIDProcessor speedProc;
	private PIDProcessor turnProc;
	
    public PIDArcadeDrive(DiffDrivetrain drivetrain, PIDProcessor speedProc, double speedSetpoint, boolean resetDistance,
    		PIDProcessor turnProc, double turnSetpoint, boolean resetAngle)
    {
        requires(drivetrain);
        
        this.drivetrain = drivetrain;
        this.speedProc = speedProc;
        this.turnProc = turnProc;
        
        if (this.speedProc != null)
        {
        	this.speedProc.SetForRun(speedSetpoint, resetDistance);
        }
        
        if (this.turnProc != null)
        {
        	this.turnProc.SetForRun(turnSetpoint, resetAngle);
        }
    }
    
    public PIDArcadeDrive(DiffDrivetrain drivetrain, PIDProcessor speedProc, PIDProcessor turnProc)
    {
        requires(drivetrain);
        
        this.drivetrain = drivetrain;
        this.speedProc = speedProc;
        this.turnProc = turnProc;
    }

    // Called just before this Command runs the first time
    protected void initialize()
    {
    	if (this.speedProc != null)
    	{
    		this.speedProc.ResetFeedbackDevice();
    		this.speedProc.enable();
    	}
    	
    	if (this.turnProc != null)
    	{
    		this.turnProc.ResetFeedbackDevice();
    		this.turnProc.enable();
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
    	double speedOutput = 0;
    	double turnOutput = 0;
    	
    	if (this.speedProc != null)
    	{
    		speedOutput = this.speedProc.GetOutputValue();
    	}
    	
    	if (this.turnProc != null)
    	{
    		turnOutput = this.turnProc.GetOutputValue();
    	}
    	
		this.drivetrain.ArcadeDrive(speedOutput, turnOutput, 1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished()
    {
		boolean onTarget = false;
		
		if (this.speedProc != null)
		{
			onTarget = this.speedProc.onTarget();
		}
		
		if (this.turnProc != null)
		{
			onTarget = onTarget && this.turnProc.onTarget();
		}
		
		return onTarget || this.isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end()
    {
    	if (this.speedProc != null)
    	{
    		this.speedProc.reset();
    	}
    	
    	if (this.turnProc != null)
    	{
    		this.turnProc.reset();
    	}
    	
    	this.drivetrain.ArcadeDrive(0, 0, 1);
    	
    	System.out.println("End Auto Drive");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted()
    {
    	end();
    }
}
