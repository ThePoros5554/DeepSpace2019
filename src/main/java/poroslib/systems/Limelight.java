package poroslib.systems;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;

public class Limelight
{

    private NetworkTable limelightTable;

    private NetworkTableEntry tHorizontalOffset;
    private NetworkTableEntry tVerticalOffset;
    private NetworkTableEntry tArea;
    private NetworkTableEntry tIsTarget;
    
    private NetworkTableEntry tCamMode;
    private NetworkTableEntry tPipeline;
    private NetworkTableEntry tLedMode;

    private int imgWidth = 320;
    private int horizontalFov = 54;
    private double focalLength = this.imgWidth / (2 * Math.tan(this.horizontalFov/2.0));
    
    private double camHeight;
    private double camHorizontalOffset;
    private double camVerticalOffset;
    private double camHorizontalDegreeOffset;

    private double lastNTUpdate;


    public Limelight()
    {
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
        
        limelightTable.addEntryListener((limelightTable, key, entry, value, flags) -> { setLastUpdateTimeStamp(); }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        tHorizontalOffset = limelightTable.getEntry("tx"); 
        tVerticalOffset = limelightTable.getEntry("ty");
        tArea = limelightTable.getEntry("ta");
        tIsTarget = limelightTable.getEntry("tv");

        tCamMode = limelightTable.getEntry("camMode");
        tPipeline = limelightTable.getEntry("pipeline");
        tLedMode = limelightTable.getEntry("ledMode");
    }

    public double getHorizontalOffset()
    {
        return tHorizontalOffset.getDouble(0.0);
    }

    public double getVerticalOffset()
    {
        return tVerticalOffset.getDouble(0.0);
    }

    public double getArea()
    {
        return tArea.getDouble(0.0);
    }

    public boolean getIsTarget()
    {
        return tIsTarget.getDouble(0) == 1;
    }

    public void setCamDisplacement(double height)
    {
        this.camHeight = height;
    }

    public void setHorizontalOffset(double horizontalOffset)
    {
        this.camHorizontalOffset = horizontalOffset;
    }

    public void setVerticalOffset(double verticalOffset)
    {
        this.camHorizontalOffset = verticalOffset;
    }

    public void setHorizontalDegreeOffset(double horizontalDegreeOffset)
    {
        this.camHorizontalDegreeOffset = horizontalDegreeOffset;
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

    public void setLastUpdateTimeStamp()
    {
        this.lastNTUpdate = Timer.getFPGATimestamp();
    }

    public double calculateDistance(double targerHeight)
    {
        double camDistance = this.focalLength * targerHeight/this.camHeight;
        double robotHorizontalOffset = this.camHorizontalDegreeOffset - getHorizontalOffset();
        return Math.sqrt(Math.pow(camDistance*Math.sin(Math.abs(robotHorizontalOffset))+this.camHorizontalOffset, 2) + Math.pow(camDistance * Math.cos(robotHorizontalOffset) + this.camVerticalOffset, 2));
    }
}