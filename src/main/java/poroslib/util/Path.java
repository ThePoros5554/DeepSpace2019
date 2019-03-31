package poroslib.util;

import java.io.File;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;

//If at some point in the future, want to move to hash style, add properties 
//necessary to create a trajectory and add a generate method with hash option 
public class Path 
{
	private String pathName;
	
	public Path(String pathName)
	{
		this.pathName = pathName;
	}
	
	//If at some point in the future, want to move to hash style, add boolean hash to this function
	public Trajectory getTrajectory()
	{
		//File pathFile = new File(Filesystem.getDeployDirectory() + "/output/" + pathName + ".csv");
		File pathFile = new File(Filesystem.getDeployDirectory() + "/output/" + pathName + ".pf1.csv");
		Trajectory trajectory;

		if (pathFile.exists()) 
		{ 
			trajectory = Pathfinder.readFromCSV(pathFile);
		}
		else
		{
			DriverStation.reportError("trajectory at pathFile wasn't found", false);
			return null;
		}
		
		return trajectory;
	}
}
