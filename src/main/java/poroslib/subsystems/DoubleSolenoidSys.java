package frc.poroslib.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;


public class DoubleSolenoidSys extends Subsystem {
	
	private DoubleSolenoid piston;
	
	public DoubleSolenoidSys(int foward, int reverse)
	{
		piston = new DoubleSolenoid(foward, reverse);
	}
	
	public void foward()
	{
		piston.set(DoubleSolenoid.Value.kForward);
	}
	
	public void reverse()
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

