package poroslib.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;


public class DoubleSolenoidSys extends Subsystem {
	
	private DoubleSolenoid piston;
	
	public DoubleSolenoidSys(int forward, int reverse)
	{
		piston = new DoubleSolenoid(forward, reverse);
	}
	
	public void open()
	{
		piston.set(DoubleSolenoid.Value.kForward);
	}
	
	public void close()
	{
		piston.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void off()
	{
		piston.set(DoubleSolenoid.Value.kOff);
	}
	
    public void initDefaultCommand() {
        
    }
}

