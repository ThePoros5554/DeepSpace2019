package poroslib.position;

import frc.robot.Robot;
import poroslib.position.geometry.Pose2d;

public class VisionInfo
{
    private double horizontalOffset;
    private double verticalOffset;

    private double camHeight;
    private double targetHeight;
    private double focalLength;

    private static final double HATCH_HEIGHT = 80.01;//hatch height and ball height are different
    
    public VisionInfo()
    {

    }


    public Pose2d getHorizontalDisplacement()
    {
        return  Robot.lime.getHorizontalTargetDisplacement(HATCH_HEIGHT);
    }

    public Pose2d getVerticalDisplacement()
    {
        return  null;
    }

}