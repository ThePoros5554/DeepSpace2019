package poroslib.position;

import poroslib.position.geometry.Pose2d;
import poroslib.position.geometry.Rotation2d;
import poroslib.position.geometry.Translation2d;

public class VisionInfo
{
    private double horizontalOffset;
    private double verticalOffset;

    private double camHeight;
    private double targetHeight;
    
    public VisionInfo(double horizontalOffset, double verticalOffset, double camHeight, double targetHeight)
    {
        this.horizontalOffset = horizontalOffset;
        this.verticalOffset = verticalOffset;
        this.camHeight = camHeight;
        this.targetHeight = targetHeight;
    }


    public Pose2d getHorizontalDisplacement()
    {
        double x;
        double y;
    
        y = (this.targetHeight - this.camHeight) / Math.tan(verticalOffset);
        x = y * Math.tan(horizontalOffset);

        return new Pose2d(new Translation2d(x, y), new Rotation2d(x, y, true));
    }

    public Pose2d getVerticalDisplacement()
    {
        double x;
        double y;
    
        y = this.targetHeight - this.camHeight;
        x = y / Math.tan(verticalOffset);

        return new Pose2d(new Translation2d(x, y), new Rotation2d(x, y, true));
    }

}