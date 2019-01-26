package poroslib.commands.auto;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import poroslib.subsystems.DiffDrivetrain;
import poroslib.systems.RobotProfile;
import poroslib.util.Path;

public class FollowPath extends Command{

	private DiffDrivetrain dt;
	
	private EncoderFollower left;
	private EncoderFollower right;
	
	double distanceKP;
	double distanceKD;
	double headingKP;
	double headingKD;
	
	private Notifier closedLoop;
		
	public FollowPath(DiffDrivetrain dt, Path path, double distanceKP, double distanceKD, double headingKP, double headingKD)
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
	
	public FollowPath(DiffDrivetrain dt, Path leftPath, Path rightLeft, double distanceKP, double distanceKD, double headingKP, double headingKD)
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
				double l = left.calculate(dt.getRawLeftPosition());
				double r = right.calculate(dt.getRawRightPosition());

				double gyro_heading = dt.getHeading();
				double desired_heading = Pathfinder.r2d(left.getHeading()); 
				
				
				double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
				double turn = (headingKP * angleDifference)
						+ (headingKD * ((angleDifference - lastAngleDifference) / RobotProfile.getRobotProfile().getPathTimeStep()));
				
				lastAngleDifference = angleDifference;
				
				dt.tankDrive(l + turn, r - turn, 1);		
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
