package PorosLib.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import PorosLib.subsystems.MechSys;

/**
 *
 */
public class TimedMechSys extends Command {

	private MechSys subsystem;
	private double speed;
	
	private double zeroValue = 0;
	
    public TimedMechSys(MechSys subsystem, double speed, double timeout) 
    {
        super(timeout);
        
        this.subsystem = subsystem;
        this.speed = speed;
        
        requires(this.subsystem);
    }
    
    public TimedMechSys(MechSys system, double speed, double zeroValue, double timeout) 
    {
        super(timeout);
        
        this.subsystem = system;
        this.speed = speed;
        
        requires(this.subsystem);
        
        this.zeroValue = zeroValue;
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
    	this.subsystem.Activate(this.speed, zeroValue);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	this.subsystem.Activate(0, zeroValue);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted()
    {
    	end();
    }
}
