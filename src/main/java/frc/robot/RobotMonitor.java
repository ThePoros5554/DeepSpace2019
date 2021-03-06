package frc.robot;

import java.util.Map.Entry;

import poroslib.position.PositionTracker;
import poroslib.position.VisionInfo;
import poroslib.position.VisionTracker;
import poroslib.position.geometry.Pose2d;
import poroslib.position.geometry.Rotation2d;
import poroslib.util.InterpolatingDouble;

public class RobotMonitor
{
	private static RobotMonitor instance;
	
	public static RobotMonitor getRobotMonitor()
	{			
		if(instance == null)
		{
            instance = new RobotMonitor();
            instance.positions = new PositionTracker(1000);
            instance.visionTargets = new VisionTracker(1000);
		}
		
		return instance;
	}
	
	public static void setRobotMonitor(RobotMonitor state)
	{
		instance = state;
    }
    
    private PositionTracker positions;
    private VisionTracker visionTargets;

    public static final double targetFixedHeight = 80.01; // Hatch target height at the HIGHEST POINT

    public void addPositionObservation(double timestamp, double left_encoder_delta_distance, double right_encoder_delta_distance, double currentGyroAngle)
    {
        positions.addReportFromSensors(new InterpolatingDouble(timestamp), left_encoder_delta_distance, right_encoder_delta_distance, 
        Rotation2d.fromDegrees(currentGyroAngle));
    }

    public Pose2d getPositionAtTime(double timestamp)
    {
        return positions.getPositionAtTime(new InterpolatingDouble(timestamp));
    }

    public Entry<InterpolatingDouble, Pose2d> getLastPositionReport()
    {
        return positions.getLastReport();
    }

    public void addVisionReport(double timestamp)
    {
        visionTargets.addReportFromTarget(timestamp, Robot.lime.getHorizontalOffset(), Robot.lime.getVerticalOffset(), Robot.lime.getFixedHeight(), targetFixedHeight,
            Robot.lime.getHorizontalSideLength(), Robot.lime.getVerticalSideLength(), Robot.lime.getIsTarget());
    }

    
    public Entry<Double, VisionInfo> getLastVisionReport()
    {
        Entry<Double, VisionInfo> lastReport = visionTargets.getLastReport();
        
        if(lastReport == null)
        {
            visionTargets.add(-1, new VisionInfo(0, 0, 0, 0, 0, 0, false));
            return visionTargets.getLastReport();
        }
        else
        {
            return lastReport;
        }

    }



    public void resetMonitor()
    {
        this.positions.reset();
        this.visionTargets.reset();
    }

}