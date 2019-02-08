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
    private NetworkTableEntry tArea;
    private NetworkTableEntry tIsTarget;
    
    private NetworkTableEntry tCamMode;
    private NetworkTableEntry tPipeline;
    private NetworkTableEntry tLedMode;

    private int imgWidth = 320;
    private int horizontalFov = 54;
    private double focalLength = this.imgWidth / (2 * Math.tan(this.horizontalFov/2.0));
    
    private double camHeight;
    private double camHorizontalOffPoint;
    private double camHorizontalDegreeOffset;
    private double camVerticalDegreeOffset;
    private double camDiagonalOffPoint;

    private double lastNTUpdate;


    public Limelight()
    {
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
        
        limelightTable.addEntryListener((limelightTable, key, entry, value, flags) -> { updateLastUpdateTimeStamp(); }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        tHorizontalOffset = limelightTable.getEntry("tx"); 
        tVerticalOffset = limelightTable.getEntry("ty");
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
        return tHorizontalOffset.getDouble(0.0);
    }

    /**
     * Calculates the targets vertical off set
     * @return Targets horizontal off set
     */
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

    /**
     * Sets the position of the camera
     * @param diagonalOffPoint Cam diagonal placement error
     * @param horizontalOffPoint Cam horizontal placement error
     * @param height The height of the camera
     * @param horizontalDegreeOffset Cam horizontal degree placement error
     * @param verticalDegreeOffset Cam vertical degree placement error
     */
    public void SetCamPosistion(double diagonalOffPoint, double horizontalOffPoint, double height, double horizontalDegreeOffset, double verticalDegreeOffset)
    {
        this.camDiagonalOffPoint = diagonalOffPoint;
        this.camHorizontalOffPoint = horizontalOffPoint;
        this.camHeight = height;
        this.camHorizontalDegreeOffset = horizontalDegreeOffset;
        this.camVerticalDegreeOffset = verticalDegreeOffset;
    }

    /**
     * Finds the vector of the target
     * @param targetHeight The height of the target
     * @return The vector of the target
     */
    public Pose2d getHorizontalTargetDisplacement(double targetHeight)
    {   
        double camDistance;
        double xDistance;
        double zDistance;
        double robotHorizontalDegreeOfSet;
        double robotVerticalDegreeOfSet;
        Pose2d vector;

        robotHorizontalDegreeOfSet = this.camHorizontalDegreeOffset - this.getHorizontalOffset();
        robotVerticalDegreeOfSet = this.camVerticalDegreeOffset - this.getVerticalOffset();
        camDistance = (targetHeight - this.camHeight) / Math.tan(robotVerticalDegreeOfSet);
        xDistance = camDistance * Math.cos(robotHorizontalDegreeOfSet) + this.camHorizontalOffPoint;
        zDistance = camDistance * Math.sin(robotHorizontalDegreeOfSet) + this.camDiagonalOffPoint;
        vector = new Pose2d(new Translation2d(xDistance, zDistance), new Rotation2d(xDistance, zDistance, true));
        //{xDistance,zDistance,Math.atan(zDistance/xDistance)};
        return vector;
    }
}