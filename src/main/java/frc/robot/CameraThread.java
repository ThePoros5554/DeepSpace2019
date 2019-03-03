/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import poroslib.vision.CameraHandler;
import poroslib.vision.Stream;
import poroslib.vision.VideoBox;

public class CameraThread extends Thread
{
    private final int kNumberOfCameras = 1;
    private final int kAheadCameraPort = 0;
		
	public CameraThread()
	{
	}
	
	@Override	
	public void run()
	{		
		/****************************** Streaming Objects ******************************************/
        
        int currentCamera = kAheadCameraPort;
		
		CameraHandler cameras = new CameraHandler(kNumberOfCameras, 320, 240);
		VideoBox screen = new VideoBox("RoboRIO Camera", 320, 240);
		
		cameras.SetStreamer(currentCamera);
				
        /****************************** The Thread Main body ***************************************/
        
		while (!Thread.interrupted()) 
		{   
            Stream stream = cameras.GetStream();
            
            if (stream.GetReport() == 0) 
            {
                continue;
            }
            else
            {
                screen.stream(stream.GetImage());
            }
		}
	}
}