package poroslib.commands;

import poroslib.triggers.JoyAxis;
import poroslib.util.Speed;
import edu.wpi.first.wpilibj.command.Command;
import poroslib.subsystems.MechSys;

/**
 *
 */
public class ActivateMechSys extends Command 
{

	private MechSys subsystem;
	
	private Speed speedObj;
	private double speed;
	private JoyAxis speedAxis;
	
	private double zeroValue = 0;

    public ActivateMechSys(MechSys subsystem, Speed speed) 
    {
    	this.subsystem = subsystem;
    	this.speedObj = speed;
        requires(this.subsystem);
    }
    
    public ActivateMechSys(MechSys subsystem, Speed speed, int zeroValue) 
    {
    	this.subsystem = subsystem;
    	this.speedObj = speed;
        requires(this.subsystem);

    }
    
    public ActivateMechSys(MechSys subsystem, double speed) 
    {
    	this.subsystem = subsystem;
        requires(this.subsystem);
        this.speed = speed;
    }
    
    public ActivateMechSys(MechSys subsystem, double speed, double zeroValue) 
    {
    	this.subsystem = subsystem;
        requires(this.subsystem);
        this.speed = speed;
        this.zeroValue = zeroValue;
    }
    
    public ActivateMechSys(MechSys subsystem, JoyAxis speedAxis) 
    {
    	this.subsystem = subsystem;
        requires(this.subsystem);
        this.speedAxis = speedAxis;
    }
    
    public ActivateMechSys(MechSys subsystem, JoyAxis speedAxis, double zeroValue) 
    {
    	this.subsystem = subsystem;
        requires(this.subsystem);
        this.speedAxis = speedAxis;
        this.zeroValue = zeroValue;
    }

    protected void initialize() {
    	
    }

    protected void execute() 
    {
    	if(this.speedObj != null)
    	{
    		this.subsystem.Activate(this.speedObj.GetValue(), this.zeroValue);
    	}
    	else if(this.speedAxis != null)
    	{
    		this.subsystem.Activate(speedAxis.GetAxisValue(), this.zeroValue);
    	}
    	else
    	{
    		this.subsystem.Activate(this.speed, this.zeroValue);
    	}
    }

    protected boolean isFinished() 
    {
        return false;
    }

    protected void end() 
    {
    	this.subsystem.Activate(0, this.zeroValue);
    }


    protected void interrupted() 
    {
    	this.end();
    }
}
