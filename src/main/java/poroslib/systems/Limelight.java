package poroslib.systems;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import poroslib.position.geometry.Pose2d;
import poroslib.position.geometry.Rotation2d;
import poroslib.position.geometry.Translation2d;

public class Limelight
{

    private NetworkTable limelightTable;

    private NetworkTableEntry tHorizontalOffset;
    private NetworkTableEntry tVerticalOffset;
    private NetworkTableEntry tHor;
    private NetworkTableEntry tVer;
    private NetworkTableEntry tArea;
    private NetworkTableEntry tIsTarget;
    
    private NetworkTableEntry tCamMode;
    private NetworkTableEntry tPipeline;
    private NetworkTableEntry tLedMode;
    
    private double hightFromFloor = 109;
    private double fixedHorizontalOffset = 0;
    private double fixedVerticalOffset = -12.5;
    private Pose2d cameraHorizontalDisplacementFromRobot = new Pose2d(new Translation2d(0, -50
    ), Rotation2d.fromDegrees(0));

    private double lastNTUpdate;


    public Limelight()
    {
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
        
        limelightTable.addEntryListener((limelightTable, key, entry, value, flags) -> { updateLastUpdateTimeStamp(); }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        tHorizontalOffset = limelightTable.getEntry("tx"); 
        tVerticalOffset = limelightTable.getEntry("ty");
        tHor = limelightTable.getEntry("thor"); 
        tVer = limelightTable.getEntry("tvert");
        tArea = limelightTable.getEntry("ta");
        tIsTarget = limelightTable.getEntry("tv");

        tCamMode = limelightTable.getEntry("camMode");
        tPipeline = limelightTable.getEntry("pipeline");
        tLedMode = limelightTable.getEntry("ledMode");
    }

    /**
     * Calculates the targets horizontal off set
     * @return Targets horizontal off set
     */
    public double getHorizontalOffset()
    {
        return tHorizontalOffset.getDouble(0.0) + fixedHorizontalOffset;
    }

    /**
     * Calculates the targets vertical off set
     * @return Targets horizontal off set
     */
    public double getVerticalOffset()
    {
        return tVerticalOffset.getDouble(0.0) + fixedVerticalOffset;
    }

    public double getVerticalSideLength()
    {
        return tVer.getDouble(0.0);
    }

    public double getHorizontalSideLength()
    {
        return tHor.getDouble(0.0);
    }

    public double getArea()
    {
        return tArea.getDouble(0.0);
    }

    public boolean getIsTarget()
    {
        return tIsTarget.getDouble(0) == 1;
    }

    public enum LimelightCamMode
    {
        VisionProcessor(0),
        DriverCamera(1);

        private final int value;

        LimelightCamMode(int modeIdx)
        {
            this.value = modeIdx;
        }
    }

    public void setCamMode(LimelightCamMode camMode)
    {
        tCamMode.setNumber(camMode.value);
    }

    public void setPipeline(int pipeline)
    {
        if (pipeline >= 0 && pipeline <= 9)
        {
            tPipeline.setNumber(pipeline);
        }
    }
    public enum LimelightLedMode
    {
        CurrentPipeline(0),
        ForceOff(1),
        ForceBlink(2),
        ForceOn(3);

        private final int value;

        private LimelightLedMode(int modeIdx)
        {
            this.value = modeIdx;
        }
    }

    public void setLedMode(LimelightLedMode ledMode)
    {
        tLedMode.setNumber(ledMode.value);
    }

    public void updateLastUpdateTimeStamp()
    {
        this.lastNTUpdate = Timer.getFPGATimestamp();
    }

    public double getLastNTUpdateTime()
    {
        return this.lastNTUpdate;
    }

    public void setFixedHeight(double height)
    {
        this.hightFromFloor = height;
    }

    public double getFixedHeight()
    {
        return this.hightFromFloor;
    }

    public void setFixedHorizontalOffset(double horizontalOffset)
    {
        this.fixedHorizontalOffset = horizontalOffset;
    }

    public void setFixedVerticalOffset(double verticalOffset)
    {
        this.fixedVerticalOffset = verticalOffset;
    }

    public void setCameraVerticalDisplacement(Pose2d cameraHorizontalDisplacementFromRobot)
    {
        this.cameraHorizontalDisplacementFromRobot = cameraHorizontalDisplacementFromRobot;
    }

    public Pose2d getCameraHorizontalDisplacementFromRobot()
    {
        return cameraHorizontalDisplacementFromRobot;
    }
}