package PorosLib.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import PorosLib.subsystems.PidActionSubsys;
import PorosLib.systems.PIDProcessor;

/**
 *
 */
public class SimplePIDAction extends Command {

	private PidActionSubsys subsystem;
	private PIDProcessor controller;
	
    public SimplePIDAction(PidActionSubsys subsystem, PIDProcessor controller, double sp, boolean resetFeedbackDevice) 
    {
        this.controller = controller;
        
        this.controller.SetForRun(sp, resetFeedbackDevice);
        
        requires(subsystem.GetSubsystem());
        
        this.subsystem = subsystem;
    }
    
    public SimplePIDAction(PidActionSubsys subsystem, PIDProcessor controller) 
    {
        this.controller = controller;
                
        requires(subsystem.GetSubsystem());
        
        this.subsystem = subsystem;
    }

    @Override
    protected void initialize() 
    {
    	this.controller.ResetFeedbackDevice();
    	
    	this.controller.enable();
    }
    
    @Override
    protected void execute()
    {
    }

    @Override
    protected boolean isFinished()
    {
        return this.controller.onTarget();
    }

    @Override
    protected void end() 
    {
    	this.subsystem.StopSystem();
    	this.controller.reset();
    	
    	System.out.println("PID Action Ended");
    }

    @Override
    protected void interrupted() 
    {
    	end();
    }
}
