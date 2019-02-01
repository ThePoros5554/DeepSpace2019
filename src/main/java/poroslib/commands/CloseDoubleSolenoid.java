package poroslib.commands;

import edu.wpi.first.wpilibj.command.Command;
import poroslib.subsystems.DoubleSolenoidSys;

/**
 *
 */
public class CloseDoubleSolenoid extends Command {

	DoubleSolenoidSys subsystem;
	

    public CloseDoubleSolenoid(DoubleSolenoidSys subsystem)
    {
    	this.subsystem = subsystem;
        requires(this.subsystem);
    }

 
    protected void initialize()
    {
    	this.subsystem.close();
    }

   
    protected void execute()
    {
    	
    }

  
    protected boolean isFinished() 
    {
        return false;
    }

    
    protected void end()
    {
    	this.subsystem.off();
    }

    
    protected void interrupted() 
    {
    	
    }
}
