package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import poroslib.sensors.NavxSource;
import poroslib.sensors.NavxSource.NavxAxis;
import poroslib.subsystems.DiffDrivetrain;
import poroslib.systems.PIDProcessor;
import poroslib.systems.RobotProfile;
import poroslib.util.MathHelper;
import poroslib.util.Path;

public class FollowPath extends Command{

	private Drivetrain dt;
	
	private EncoderFollower left;
	private EncoderFollower right;
	
	double distanceKP;
	double distanceKD;

	PIDProcessor angleController;
	double headingKP;
	double headingKD;
	
	private Notifier closedLoop;

	private boolean isReversed;

	private boolean isNull = false;
		
	public FollowPath(Drivetrain dt, Path path, double distanceKP, double distanceKD, double headingKP, double headingKD, boolean isReversed)
	{
		requires(dt);
		
		this.dt = dt;
		
		this.distanceKP = distanceKP;
		this.distanceKD = distanceKD;
		this.headingKP = headingKP;
		this.headingKD = headingKD;
		
		if (path != null)
		{
		TankModifier modifier = new TankModifier(path.getTrajectory());
		modifier.modify((RobotProfile.getRobotProfile().getWheelbaseWidth()));
		
		left = new EncoderFollower(modifier.getLeftTrajectory());
		left.configurePIDVA(distanceKP, 0, distanceKD, RobotProfile.getRobotProfile().getAutoKV(), RobotProfile.getRobotProfile().getAutoKA());
		
		right = new EncoderFollower(modifier.getRightTrajectory());
		right.configurePIDVA(distanceKP, 0, distanceKD, RobotProfile.getRobotProfile().getAutoKV(), RobotProfile.getRobotProfile().getAutoKA());
		}
		else
		{
			isNull = true;
		}
		this.isReversed = isReversed;
	}
	
	public FollowPath(Drivetrain dt, Path leftPath, Path rightPath, double distanceKP, double distanceKD, double headingKP, double headingKD, boolean isReversed)
	{
		requires(dt);

		this.dt = dt;
		
		this.distanceKP = distanceKP;
		this.distanceKD = distanceKD;
		this.headingKP = headingKP;
		this.headingKD = headingKD;
		
		if (leftPath != null && rightPath != null)
		{
		left = new EncoderFollower(leftPath.getTrajectory());
		left.configurePIDVA(distanceKP, 0, distanceKD, RobotProfile.getRobotProfile().getAutoKV(), RobotProfile.getRobotProfile().getAutoKA());
		
		right = new EncoderFollower(rightPath.getTrajectory());
		right.configurePIDVA(distanceKP, 0, distanceKD, RobotProfile.getRobotProfile().getAutoKV(), RobotProfile.getRobotProfile().getAutoKA());
		}
		else
		{
			isNull = true;
		}
		this.isReversed = isReversed;
	}
	
	@Override
	protected void initialize()
	{
		if (!isNull)
		{
		left.configureEncoder(dt.getRawLeftPosition(), RobotProfile.getRobotProfile().getDriveEncTicksPerRevolution(), RobotProfile.getRobotProfile().getWheelDiameter());
		right.configureEncoder(dt.getRawRightPosition(), RobotProfile.getRobotProfile().getDriveEncTicksPerRevolution(), RobotProfile.getRobotProfile().getWheelDiameter());
	
		angleController = new PIDProcessor(headingKP, 0, headingKD, new NavxSource(Robot.drivetrain.getNavx(), NavxAxis.yaw),false);
		angleController.setInputRange(-180, 180);
		angleController.setContinuous(false);

		closedLoop = new Notifier(new Thread() 
		{
			private double lastAngleDifference = 0;
			private double time = 0;
			
			@Override
			public void run() 
			{
				time += RobotProfile.getRobotProfile().getPathTimeStep();
				if(!angleController.isEnabled())
				{
					angleController.enable();
				}

				double l;
				double r;

				if(isReversed)
				{
					l = left.calculate(-dt.getRawLeftPosition());
					r = right.calculate(+dt.getRawRightPosition());				}
				else
				{
					l = left.calculate(dt.getRawLeftPosition());
					r = right.calculate(-dt.getRawRightPosition());
				}

				SmartDashboard.putNumber("left", left.getSegment().velocity);
				SmartDashboard.putNumber("right", right.getSegment().velocity);

				double distance_covered = ((double)(dt.getRawLeftPosition() - 0) / 4096)
				* (Math.PI *0.1016);
								
				double angle = Pathfinder.r2d(left.getHeading());
				double boundedAngle = Pathfinder.boundHalfDegrees(angle);
				System.out.println("out: " + boundedAngle);
				angleController.setSetpoint(boundedAngle);
				lastAngleDifference = angle;
				double turnOut = angleController.GetOutputValue();

				if(isReversed)
				{
					dt.tankDrive( +l - turnOut, +r + turnOut, 1);		
				}
				else
				{
					dt.tankDrive( -l - turnOut, -r + turnOut, 1);		
				}
				SmartDashboard.putNumber("left real", dt.getLeftPositionInCm()*0.01 / time);
			}
		});
		
		closedLoop.startPeriodic(RobotProfile.getRobotProfile().getPathTimeStep());
		}	
	}
	
	@Override
	protected void execute()
	{
	}
	
	@Override
	protected boolean isFinished() 
	{
		if (isNull)
		{
			return true;
		}
		return left.isFinished() && right.isFinished();
	}
	
	@Override
	protected void end() 
	{
		closedLoop.stop();
		dt.StopSystem();
	}
	
	@Override
	protected void interrupted() 
	{
		end();
	}

}
