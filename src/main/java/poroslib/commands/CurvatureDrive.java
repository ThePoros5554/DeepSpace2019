package poroslib.commands;

import edu.wpi.first.wpilibj.command.Command;
import poroslib.subsystems.DiffDrivetrain;
import poroslib.triggers.SmartJoystick;

/**
 *
 */
public class CurvatureDrive extends Command
{
	private DiffDrivetrain drivetrain;
	private SmartJoystick joy;
	
    private double maxOutput = 1;
    private double boostMaxOutput = 1;
    private double aimMaxOutput = 1;
	
    public 
    CurvatureDrive(DiffDrivetrain drivetrain, SmartJoystick joy, double sensitivity, double boostSensitivity, double aimSensitivity)
    {
    	this.drivetrain = drivetrain;
        this.joy = joy;
        this.maxOutput = sensitivity;
        this.boostMaxOutput = boostSensitivity;
        this.aimMaxOutput = aimSensitivity;
        requires(this.drivetrain);
    }

    public CurvatureDrive(DiffDrivetrain drivetrain, SmartJoystick joy, double sensitivity)
    {
    	this.drivetrain = drivetrain;
        this.joy = joy;
        this.maxOutput = sensitivity;
        this.boostMaxOutput = sensitivity;
        this.aimMaxOutput = sensitivity;
        requires(this.drivetrain);
    }

    public CurvatureDrive(DiffDrivetrain drivetrain, SmartJoystick joy)
    {
    	this.drivetrain = drivetrain;
        this.joy = joy;
        requires(this.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize()
    {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
    	double speedValue = this.joy.GetSpeedAxis();
        double rotateValue =  this.joy.GetRotateAxis();
        
        
        if (joy.getRawAxis(3) >= 0.8)
        {
            this.drivetrain.curvatureDrive(-speedValue, rotateValue, joy.getRawButton(5), boostMaxOutput);
        }
        else if (joy.getRawAxis(2) >= 0.8)
        {
            this.drivetrain.curvatureDrive(-speedValue, rotateValue, joy.getRawButton(5), aimMaxOutput);
        }
        else
        {

            this.drivetrain.curvatureDrive(-speedValue, rotateValue, joy.getRawButton(5), maxOutput);
        }
    }

    protected boolean isFinished()
    {
        return false;
    }

    protected void end()
    {
    	this.drivetrain.arcadeDrive(0, 0, this.maxOutput);
    }

    protected void interrupted()
    {
    	end();
    }
}