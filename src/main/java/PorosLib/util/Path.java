package PorosLib.util;

import java.io.File;

import edu.wpi.first.wpilibj.DriverStation;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;

//If at some point in the future, want to move to hash style, add properties 
//necessary to create a trajectory and add a generate method with hash option 
public class Path 
{
	private String pathFolder;
	private String pathName;
	
	public Path(String pathFolder, String pathName)
	{
		this.pathFolder = pathFolder;
		this.pathName = pathName;
	}
	
	//If at some point in the future, want to move to hash style, add boolean hash to this function
	public Trajectory getTrajectory()
	{
		Trajectory trajectory;
		
		File pathFile = new File(pathFolder + pathName + ".csv");

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
