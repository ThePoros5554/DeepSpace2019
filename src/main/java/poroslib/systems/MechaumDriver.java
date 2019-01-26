package poroslib.systems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class MechaumDriver extends MecanumDrive{

	private SpeedController frontLeftMotor;
	private SpeedController rearLeftMotor;
	private SpeedController frontRightMotor;
	private SpeedController rearRightMotor;

	
	public MechaumDriver(SpeedController frontLeftMotor, SpeedController rearLeftMotor,
            SpeedController frontRightMotor, SpeedController rearRightMotor) 
	{
		super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
		
		this.frontLeftMotor = frontLeftMotor;
		this.rearLeftMotor = rearLeftMotor;
		this.frontRightMotor = frontRightMotor;
		this.rearRightMotor = rearRightMotor;
		this.setSafetyEnabled(false);
	}
	
	public void PidDrive(double output)
	{
		this.frontLeftMotor.pidWrite(output);
		this.rearLeftMotor.pidWrite(output);
		this.frontRightMotor.pidWrite(-output);
		this.rearRightMotor.pidWrite(-output);
	}
	
	public void PidTwist(double output)
	{
//		this.frontLeftMotor.pidWrite(output);
//		this.rearLeftMotor.pidWrite(-output);
//		this.frontRightMotor.pidWrite(output);
//		this.rearRightMotor.pidWrite(-output);
	}
	
	public void PidTurnInPlace(double output)
	{
		this.frontLeftMotor.pidWrite(output);
		this.rearLeftMotor.pidWrite(output);
		this.frontRightMotor.pidWrite(output);
		this.rearRightMotor.pidWrite(output);
	}




}
