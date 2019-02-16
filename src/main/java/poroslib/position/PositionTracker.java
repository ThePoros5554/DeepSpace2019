package poroslib.position;

import java.util.Map.Entry;

import edu.wpi.first.wpilibj.Timer;
import poroslib.position.geometry.Kinematics;
import poroslib.position.geometry.Pose2d;
import poroslib.position.geometry.Rotation2d;
import poroslib.util.InterpolatingDouble;
import poroslib.util.InterpolatingTreeMap;

public class PositionTracker
{
    private InterpolatingTreeMap<InterpolatingDouble, Pose2d> positionReports;

    public PositionTracker(int slots)
    {
        positionReports = new InterpolatingTreeMap<InterpolatingDouble, Pose2d>(slots);
    }

    public void add(InterpolatingDouble timestamp, Pose2d robotPose)
    {
        positionReports.put(timestamp, robotPose);
    }

    public void addReportFromSensors(InterpolatingDouble timeStamp, double left_encoder_delta_distance, double right_encoder_delta_distance, Rotation2d current_gyro_angle) 
    {
        Pose2d last_measurement = getLastReport().getValue();

        add(timeStamp, Kinematics.integrateForwardKinematics(last_measurement, left_encoder_delta_distance,
                right_encoder_delta_distance, current_gyro_angle));
    }

    public void remove(InterpolatingDouble timestamp)
    {
        positionReports.remove(timestamp);
    }

    public Entry<InterpolatingDouble, Pose2d> getLastReport()
    {
        if(positionReports.lastEntry() == null)
        {
            positionReports.put(new InterpolatingDouble(Timer.getFPGATimestamp()), new Pose2d());
        }
        return positionReports.lastEntry();
    }

    public Pose2d getPositionAtTime(InterpolatingDouble timestamp)
    {
        return positionReports.getInterpolated(timestamp);
    }

    public void reset()
    {
        this.positionReports.clear();
    }


}