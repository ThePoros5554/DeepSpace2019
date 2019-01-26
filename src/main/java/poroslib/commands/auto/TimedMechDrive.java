package frc.poroslib.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import frc.poroslib.subsystems.MechDrivetrain;

public class TimedMechDrive extends Command
{
	private MechDrivetrain drivetrain;
	
	private double sidewaysSpeed;
	private double forwardSpeed;
	private double rotationSpeed;
		
    
    public TimedMechDrive(MechDrivetrain drivetrain, double sidewaysSpeed, double forwardSpeed, double rotationSpeed, double timeout) 
    {
    	super(timeout);
    	
    	this.sidewaysSpeed = sidewaysSpeed;
    	this.forwardSpeed = forwardSpeed;
    	this.rotationSpeed = rotationSpeed;
    }
    
   

    protected void initialize() 
    {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    	this.drivetrain.MechanumDrive(this.sidewaysSpeed, this.forwardSpeed, this.rotationSpeed, 0, 1);       
    }

    protected boolean isFinished() 
    {
        return this.isTimedOut();
    }

    protected void end() 
    {    	
    	this.drivetrain.StopSystem();
    	    	
    	System.out.println("End Auto Drive");

    }

    protected void interrupted()
    {
    	this.end();
    }
}
