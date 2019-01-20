package frc.poroslib.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import frc.poroslib.subsystems.MechDrivetrain;
import frc.poroslib.subsystems.MechDrivetrain.MechDrivingDirection;
import frc.poroslib.systems.PIDProcessor;

public class PIDMechDrive extends Command
{
	private MechDrivetrain dt;
	
	private PIDProcessor powerProc;
	private double constPower;
	
	private PIDProcessor fixProc;
	private PIDProcessor angleProc;
	
	private MechDrivingDirection direction;
		
	public PIDMechDrive(MechDrivetrain drivetrain, MechDrivingDirection direction, PIDProcessor powerProc, double powerSp, boolean resetDistance,
			PIDProcessor fixProc, double fixSp, boolean resetFix,
				PIDProcessor angleProc, double angleSp,boolean resetAngle)
	{
		this.LoadCmd(drivetrain, direction, 0, powerProc, fixProc, angleProc);
    	this.SetSetpoints(powerSp, resetDistance, fixSp, resetFix, angleSp, resetAngle);
	}
	
	public PIDMechDrive(MechDrivetrain drivetrain, MechDrivingDirection direction, PIDProcessor powerProc, double powerSp, boolean resetDistance,
			PIDProcessor fixProc, double fixSp, boolean resetFix,
				PIDProcessor angleProc, double angleSp, boolean resetAngle, double timeout)
	{
		super(timeout);
		this.LoadCmd(drivetrain, direction, 0, powerProc, fixProc, angleProc);
    	this.SetSetpoints(powerSp, resetDistance, fixSp, resetFix, angleSp, resetAngle);
	}
	
	public PIDMechDrive(MechDrivetrain drivetrain, MechDrivingDirection direction, double constPower, PIDProcessor fixProc, double fixSp, boolean resetFix,
			PIDProcessor angleProc, double angleSp, boolean resetAngle, double timeout)
	{
		super(timeout);
		this.LoadCmd(drivetrain, direction, constPower, null, fixProc, angleProc);
    	this.SetSetpoints(0, false, fixSp, resetFix, angleSp, resetAngle);
	}
	
	public PIDMechDrive(MechDrivetrain drivetrain, MechDrivingDirection direction, PIDProcessor powerProc, PIDProcessor fixProc, PIDProcessor angleProc)
	{
		this.LoadCmd(drivetrain, direction, 0, powerProc, fixProc, angleProc);
	}
	
	private void LoadCmd(MechDrivetrain dt, MechDrivingDirection direction, double constPower, PIDProcessor powerProc,
			PIDProcessor fixProc, PIDProcessor angleProc)
	{
		requires(dt);

		this.dt = dt;		
		
    	if (powerProc != null)
    	{
    		this.powerProc = powerProc;
    	}
    	else
    	{
    		this.constPower = constPower;
    	}
    	
    	if (fixProc != null)
    	{
    		this.fixProc = fixProc;
    	}
    	
    	if (angleProc != null)
    	{
    		this.angleProc = angleProc;
    	}
    	
    	this.direction = direction;
	}
	
	private void SetSetpoints(double powerSetpoint, boolean resetDistance, double fixSetpoint, boolean resetFix, double angleSetpoint, boolean resetAngle)
	{
		if (this.powerProc != null)
		{
			this.powerProc.SetForRun(powerSetpoint, resetDistance);
		}
		
		if (this.fixProc != null)
		{
			this.fixProc.SetForRun(fixSetpoint, resetFix);
		}
		
		if (this.angleProc != null)
		{
			this.angleProc.SetForRun(angleSetpoint, resetAngle);
		}
	}
	
	@Override
    protected void initialize() 
    {
    	if (powerProc != null)
    	{
    		powerProc.ResetFeedbackDevice();
    		powerProc.enable();
    	}
    	
    	if (fixProc != null)
    	{
    		fixProc.ResetFeedbackDevice();
    		fixProc.enable();
    	}
    	
    	if (angleProc != null)
    	{
    		angleProc.ResetFeedbackDevice();
    		angleProc.enable();
    	}
    }
	
	@Override
    protected void execute() 
    {
    	double powerOutput = 0;
    	double fixOutput = 0;
    	double angleOutput = 0;
    	
    	if (this.powerProc != null)
    	{
    		powerOutput = this.powerProc.GetOutputValue();
    	}
    	else
    	{
    		powerOutput = constPower;
    	}
    	
    	if (this.fixProc != null)
    	{
    		fixOutput = this.fixProc.GetOutputValue();
    	}
    	
    	if (this.angleProc != null)
    	{
    		angleOutput = this.angleProc.GetOutputValue();
    	}
    	
    	if (this.direction == MechDrivingDirection.Forward)
    	{
    		this.dt.MechanumDrive(fixOutput, powerOutput, angleOutput, 0, 1);
    	}
    	else
    	{
    		this.dt.MechanumDrive(powerOutput, fixOutput, angleOutput, 0, 1);
    	}
    }

	@Override
	protected boolean isFinished() 
	{
		boolean onTarget = false;
		
		if (this.powerProc != null)
		{
			onTarget = this.powerProc.onTarget();
		}
		
		if (this.angleProc != null)
		{
			onTarget = onTarget && this.angleProc.onTarget();
		}
		
		return onTarget || this.isTimedOut();
	}
	
	@Override
	protected void end() 
	{
    	if (powerProc != null)
    	{
    		powerProc.reset();
    	}
    	
    	if (fixProc != null)
    	{
    		fixProc.reset();
    	}
    	
    	if (angleProc != null)
    	{
    		angleProc.reset();
    	}
    	
		this.dt.MechanumDrive(0, 0, 0, 0, 1);
		
    	System.out.println("End Auto Drive");
	}
	
	@Override
	protected void interrupted() 
	{
		end();
	}
}
