package poroslib.systems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight
{

    private NetworkTable limelightTable;

    private NetworkTableEntry tHorizontalOffset;
    private NetworkTableEntry tVerticalOffset;
    private NetworkTableEntry tArea;

    public Limelight()
    {
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

        tHorizontalOffset = limelightTable.getEntry("tx"); 
        tVerticalOffset = limelightTable.getEntry("ty");
        tArea = limelightTable.getEntry("ta");
    }

    public double GetHorizontalOffset()
    {
        return tHorizontalOffset.getDouble(0.0);
    }

    public double GetVerticalOffset()
    {
        return tVerticalOffset.getDouble(0.0);
    }

    public double GetOffset()
    {
        return tArea.getDouble(0.0);
    }
}