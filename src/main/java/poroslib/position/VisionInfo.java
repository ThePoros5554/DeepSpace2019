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

        double verticalOffsetRad = Math.toRadians(verticalOffset);
        double horizontalOffsetRad = Math.toRadians(horizontalOffset);
    
        y = (this.targetHeight - this.camHeight) / Math.tan(verticalOffsetRad);
        x = y * Math.sin(horizontalOffsetRad);

        return new Pose2d(new Translation2d(x, y), Rotation2d.fromDegrees(horizontalOffset));
    }

    public Pose2d getVerticalDisplacement()
    {
        double x;
        double y;
    
        y = this.targetHeight - this.camHeight;
        x = y / Math.tan(verticalOffset);

        return new Pose2d(new Translation2d(x, y), Rotation2d.fromDegrees(verticalOffset));
    }

}