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
        if(pipeline <= 9)
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
}