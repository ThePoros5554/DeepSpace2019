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

    private double horSideLength;
    private double verSideLength;

    private boolean isTarget;
    
    public VisionInfo(double horizontalOffset, double verticalOffset, double camHeight, double targetHeight,
                double horSideLength, double verSideLength, boolean isTarget)
    {
        this.horizontalOffset = horizontalOffset;
        this.verticalOffset = verticalOffset;
        this.camHeight = camHeight;
        this.targetHeight = targetHeight;

        this.horSideLength = horSideLength;
        this.verSideLength = verSideLength;

        this.isTarget = isTarget;
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

    public double getVerticalSideLength()
    {
        return this.verSideLength;
    }

    public double getHorizontalSideLength()
    {
        return this.horSideLength;
    }

    public boolean getIsTarget()
    {
        return this.isTarget;
    }

}