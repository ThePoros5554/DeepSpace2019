package frc.poroslib.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import frc.poroslib.subsystems.DiffDrivetrain;
import frc.poroslib.systems.PIDProcessor;

/**
 *
 */
public class PIDTankDrive extends Command {

	private DiffDrivetrain drivetrain;
	
	private PIDProcessor rightProc;
	private PIDProcessor leftProc;
	private PIDProcessor angleProc;
	
    public PIDTankDrive(DiffDrivetrain drivetrain, PIDProcessor leftProc, double leftSetpoint, boolean resetLeftDistance,
    		PIDProcessor rightProc, double rightSetpoint, boolean resetRightDistance, 
    			PIDProcessor angleProc, double angleSetpoint, boolean resetAngle)
    {
        requires(drivetrain);
        
        this.drivetrain = drivetrain;
        this.leftProc = leftProc;
        this.rightProc = rightProc;
        this.angleProc = angleProc;
        
        if (this.leftProc != null)
        {
        	this.leftProc.SetForRun(leftSetpoint, resetLeftDistance);
        }
        
        if (this.rightProc != null)
        {
        	this.rightProc.SetForRun(rightSetpoint, resetRightDistance);
        }
       
        
        if (this.angleProc != null)
        {
        	this.angleProc.SetForRun(angleSetpoint, resetAngle);
        }
    }
	
    public PIDTankDrive(DiffDrivetrain drivetrain, PIDProcessor leftProc, PIDProcessor rightProc, PIDProcessor angleProc)
    {
        requires(drivetrain);
        
        this.drivetrain = drivetrain;
        this.rightProc = rightProc;
        this.leftProc = leftProc;
        this.angleProc = angleProc;
    }

    // Called just before this Command runs the first time
    protected void initialize()
    {
    	if (this.rightProc != null)
    	{
    		this.rightProc.ResetFeedbackDevice();
    		this.rightProc.enable();
    	}
    	
    	if (this.leftProc != null)
    	{
    		this.leftProc.ResetFeedbackDevice();
    		this.leftProc.enable();
    	}
    	
    	if (this.angleProc != null)
    	{
    		this.angleProc.ResetFeedbackDevice();
    		this.angleProc.enable();
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
    	double rightOutput = 0;
    	double leftOutput = 0;
    	double angleOutput = 0;
    	
    	if (this.angleProc != null)
    	{
    		angleOutput = this.angleProc.GetOutputValue();
    	}
    	
    	if (this.rightProc != null)
    	{
    		rightOutput = this.rightProc.GetOutputValue() - angleOutput;
    	}
    	
    	if (this.leftProc != null)
    	{
    		leftOutput = this.leftProc.GetOutputValue() + angleOutput;
    	}
    	
		this.drivetrain.tankDrive(leftOutput, rightOutput, 1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished()
    {
		boolean onTarget = false;
		
		if (this.rightProc != null)
		{
			onTarget = this.rightProc.onTarget();
		}
		
		if (this.leftProc != null)
		{
			onTarget = onTarget && this.leftProc.onTarget();
		}
		
		if (this.angleProc != null)
		{
			onTarget = onTarget && this.angleProc.onTarget();
		}
		
		return onTarget || this.isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end()
    {
    	if (this.rightProc != null)
    	{
    		this.rightProc.reset();
    	}
    	
    	if (this.leftProc != null)
    	{
    		this.leftProc.reset();
    	}
    	
    	if (this.angleProc != null)
    	{
    		this.angleProc.reset();
    	}
    	
    	this.drivetrain.tankDrive(0, 0, 1);
    	
    	System.out.println("End Auto Drive");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted()
    {
    	end();
    }
}
