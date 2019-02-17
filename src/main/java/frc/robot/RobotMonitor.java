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
            instance.positions = new PositionTracker(500);
            instance.visionTrgets = new VisionTracker(500);
		}
		
		return instance;
	}
	
	public static void setRobotMonitor(RobotMonitor state)
	{
		instance = state;
    }
    
    private PositionTracker positions;
    private VisionTracker visionTrgets;
    private Pose2d cameraVerticalDisplacement;

    public void addPositionObservation(double timeStamp, double left_encoder_delta_distance, double right_encoder_delta_distance, double currentGyroAngle)
    {

        positions.addReportFromSensors(new InterpolatingDouble(timeStamp), left_encoder_delta_distance, right_encoder_delta_distance, 
                Rotation2d.fromDegrees(currentGyroAngle));
    }

    public Pose2d getPositionAtTime(double timeStamp)
    {
        return positions.getPositionAtTime(new InterpolatingDouble(timeStamp));
    }

    public Entry<InterpolatingDouble, Pose2d> getLastPositionReport()
    {
        return positions.getLastReport();
    }

    public void addVisionReport(double timestamp, double horizontalOffset, double verticalOffset, double camHeight, double targetHight)
    {
        visionTrgets.addReportFromTarget(timestamp, horizontalOffset, verticalOffset, camHeight, targetHight);
    }

    
    public Entry<Double, VisionInfo> getLastVisionReport()
    {
        return visionTrgets.getLastReport();
    }

    public void setCameraVerticalDisplacement(Pose2d cameraVerticalDisplacement)
    {
        this.cameraVerticalDisplacement = cameraVerticalDisplacement;
    }

    public Pose2d getCameraVerticalDisplacement()
    {
        return cameraVerticalDisplacement;
    }

    public void resetMonitor()
    {
        this.positions.reset();
        this.visionTrgets.reset();
    }

}