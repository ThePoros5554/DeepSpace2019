package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Drivetrain;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import poroslib.subsystems.DiffDrivetrain;
import poroslib.systems.RobotProfile;
import poroslib.util.Path;

public class FollowPath extends Command{

	private Drivetrain dt;
	
	private EncoderFollower left;
	private EncoderFollower right;
	
	double distanceKP;
	double distanceKD;
	double headingKP;
	double headingKD;
	
	private Notifier closedLoop;
		
	public FollowPath(Drivetrain dt, Path path, double distanceKP, double distanceKD, double headingKP, double headingKD)
	{
		requires(dt);
		
		this.dt = dt;
		
		this.distanceKP = distanceKP;
		this.distanceKD = distanceKD;
		this.headingKP = headingKP;
		this.headingKD = headingKD;
		
		TankModifier modifier = new TankModifier(path.getTrajectory());
		modifier.modify((RobotProfile.getRobotProfile().getWheelbaseWidth()));
		
		left = new EncoderFollower(modifier.getLeftTrajectory());
		left.configurePIDVA(distanceKP, 0, distanceKD, RobotProfile.getRobotProfile().getAutoKV(), RobotProfile.getRobotProfile().getAutoKA());
		
		right = new EncoderFollower(modifier.getRightTrajectory());
		right.configurePIDVA(distanceKP, 0, distanceKD, RobotProfile.getRobotProfile().getAutoKV(), RobotProfile.getRobotProfile().getAutoKA());
	}
	
	public FollowPath(Drivetrain dt, Path leftPath, Path rightLeft, double distanceKP, double distanceKD, double headingKP, double headingKD)
	{
		requires(dt);

		this.dt = dt;
		
		this.distanceKP = distanceKP;
		this.distanceKD = distanceKD;
		this.headingKP = headingKP;
		this.headingKD = headingKD;
		
		left = new EncoderFollower(leftPath.getTrajectory());
		left.configurePIDVA(distanceKP, 0, distanceKD, RobotProfile.getRobotProfile().getAutoKV(), RobotProfile.getRobotProfile().getAutoKA());
		
		right = new EncoderFollower(leftPath.getTrajectory());
		right.configurePIDVA(distanceKP, 0, distanceKD, RobotProfile.getRobotProfile().getAutoKV(), RobotProfile.getRobotProfile().getAutoKA());
		
	}
	
	@Override
	protected void initialize()
	{
		left.configureEncoder(dt.getRawLeftPosition(), RobotProfile.getRobotProfile().getDriveEncTicksPerRevolution(), RobotProfile.getRobotProfile().getWheelDiameter());
		right.configureEncoder(dt.getRawRightPosition(), RobotProfile.getRobotProfile().getDriveEncTicksPerRevolution(), RobotProfile.getRobotProfile().getWheelDiameter());
	
		closedLoop = new Notifier(new Thread() 
		{
			private double lastAngleDifference = 0;
			
			@Override
			public void run() 
			{
				System.out.println("pos: " + left.getSegment().position);
				System.out.println("calcPos: " +  ((double) (dt.getRawLeftPosition() / 4096)
				* 0.1016 * Math.PI));

				System.out.println("heading: " + left.getSegment().heading);
				System.out.println("yaw: " + dt.getHeading());
				double l = left.calculate(dt.getRawLeftPosition());
				double r = right.calculate(-dt.getRawRightPosition());

				double heading = dt.getHeading();
				double desired_heading = Pathfinder.r2d(left.getHeading());
				double heading_difference = Pathfinder.boundHalfDegrees(desired_heading - heading);
				System.out.println("heading_difference: " + heading_difference);

				double turn =  heading_difference;

				double turnOut = (headingKP * turn);

				lastAngleDifference = heading_difference;
				
				dt.tankDrive( -l - turnOut, -r + turnOut, 1);		
			}
		});
		
		closedLoop.startPeriodic(RobotProfile.getRobotProfile().getPathTimeStep());
		
	}
	
	@Override
	protected void execute()
	{
	}
	
	@Override
	protected boolean isFinished() 
	{
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
