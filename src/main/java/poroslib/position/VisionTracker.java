package poroslib.position;

import java.util.Map.Entry;

import poroslib.util.MaxedTreeMap;

public class VisionTracker
{
    private MaxedTreeMap<Double, VisionInfo> visionReports;

    public VisionTracker(int slots)
    {
        visionReports = new MaxedTreeMap<Double, VisionInfo>(slots);
    }

    public void add(double timestamp, VisionInfo cameraError)
    {
        visionReports.put(timestamp, cameraError);
    }

    public void addReportFromTarget(double timestamp, double horizontalOffset, double verticalOffset, double camHeight, double targetHight,
            double horSideLength , double verSideLength, boolean isTarget)
    {
        VisionInfo report = new VisionInfo(horizontalOffset, verticalOffset, camHeight, targetHight, horSideLength, verSideLength, isTarget);
        visionReports.put(timestamp, report);
    }

    public void remove(double timestamp)
    {
        visionReports.remove(timestamp);
    }

    public Entry<Double, VisionInfo> getLastReport()
    {
        return visionReports.lastEntry();
    } 

    public void reset()
    {
        this.visionReports.clear();
    }
}