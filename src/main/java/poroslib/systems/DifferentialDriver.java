package poroslib.systems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DifferentialDriver extends DifferentialDrive
{
	
	private SpeedController leftController;
	private SpeedController rightController;

	public DifferentialDriver(SpeedController leftSide, SpeedController rightSide) 
	{
		super(leftSide, rightSide);
		this.leftController = leftSide;
		this.rightController = rightSide;
		this.setSafetyEnabled(false);
	}

	public void PidTurnInPlace(double output)
	{
		this.leftController.pidWrite(output);		
		this.rightController.pidWrite(output);
	}
	
	public void PidDrive(double output)
	{
		this.leftController.pidWrite(output);		
		this.rightController.pidWrite(-output);
	}
	
}
