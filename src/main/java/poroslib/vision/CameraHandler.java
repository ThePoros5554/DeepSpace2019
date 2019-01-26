package poroslib.vision;

import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoException;
import edu.wpi.first.wpilibj.Joystick;

/**
 * This class handles the cameras in the robot, it can switch between them
 * and set their settings.
 * Stores cameras connected to the robot in a HashMap with a value.
 * Holds a CvSink object that listens to only 1 camera stream at a time and can sent the stream.
 *
 */
public class CameraHandler
{
	private Map<Integer , UsbCamera> cameras = new HashMap<>();
	private CvSink cvSink = new CvSink("streamer");
	private int currentCamera; //The port of the current camera
	private Mat mat;

	private int fps = 22;

	/**
	 * Creates a camera handler that can stream and switch between a specified number of cameras
	 * with a specified resolution.
	 *
	 * @param ports Number of cameras
	 * @param width Resolution width
	 * @param height Resolution Height
	 */
	public CameraHandler(int ports,int width, int height)
	{
		this.currentCamera = 0;
		this.mat = new Mat();

		for(int i = 0; i < ports; i++)
		{
			AddCamera(i, width, height , 20);
		}
		cvSink.setSource(cameras.get(0));
	}
	
	public CameraHandler(int ports,int width, int height, int fps)
	{
		this.currentCamera = 0;
		this.mat = new Mat();

		for(int i = 0; i < ports; i++)
		{
			AddCamera(i, width, height , fps);
		}
		cvSink.setSource(cameras.get(0));
	}


	/**
	 * Adds a new UsbCamera object to the cameras HashMap with an index.
	 *
	 * @param idx Camera index
	 * @param width Resolution width
	 * @param height Resolution Height
	 * @param fps The frames the camera will provide per second
	 * @param brightness Camera's brightness
	 */
	private void AddCamera(int idx, int width , int height , int fps)
	{
		if(!cameras.containsKey(idx))
		{
			try
			{
				UsbCamera cam = new UsbCamera("USB Camera " + idx, idx);
				cam.setResolution(width, height);
				cam.setFPS(fps);
				cameras.put(idx, cam);

			}
			catch(VideoException e)
			{
				System.out.println("Failed to initialize camera on port: " + idx);
				throw(e);
			}
		}
		else
		{
			System.out.println("Camera on port " + idx + " is already defined.");
		}
	}

	/**
	 * Sets the camera the camera handler object will stream.
	 *
	 * @param idx The index of the camera in the camera handler's hash map
	 */
	public void SetStreamer(int idx)
	{
		if(idx != currentCamera)
		{

			if(fps > 25)
			{
				cameras.get(currentCamera).setFPS(0);
				cameras.get(idx).setFPS(this.fps);
			}

			cvSink.setSource(cameras.get(idx));

			currentCamera = idx;
		}
	}

	/**
	 * Gets the camera stream
	 *
	 * @return The current frame
	 */
	public Stream GetStream()
	{
		long report = cvSink.grabFrame(mat);
		
		return new Stream(report, mat);
	}

	/**
	 * A function that picks and switches between the live camera according to the clicked button
	 *
	 * @param liveCamera The current live camera.
	 * @param cameraButtons An array of buttons indexes, a button's index in the array is the index of the camera he will turn on
	 * @param joy The joystick
	 * @param cameras Number of to switch from cameras
	 * @return
	 */
	public static int PickCamera(int liveCamera, int[] cameraButtons , Joystick joy, int cameras)
	{
		for(int i = 0; i < cameras ; i++)
		{
			if(joy.getRawButton(cameraButtons[i]))
			{
				liveCamera = i;
			}
		}

		return liveCamera;
	}

	/**
	 * A function that picks and switches between the live camera according to the POV
	 *
	 * @param liveCamera The current live camera.
	 * @param cameraButtons An array of POV degrees, a degree index in the array is the index of the camera he will turn on
	 * @param joy The joystick
	 * @param cameras Number of to switch from cameras
	 * @return The picked live camera
	 */
	public static int PickCamera(int liveCamera, int[] pov , Joystick joy, int cameras , int povIndex)
	{
		for(int i = 0; i < cameras ; i++)
		{
			if(joy.getPOV() == pov[i])
			{
				liveCamera = i;
			}
		}

		return liveCamera;
	}


}