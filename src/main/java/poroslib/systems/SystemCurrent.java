package poroslib.systems;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import poroslib.util.MotorCurrent;

public class SystemCurrent 
{
	private MotorCurrent[] motors;
	
	public SystemCurrent(PowerDistributionPanel PDP, int[] motorPorts)
	{
		this.motors = new MotorCurrent[motorPorts.length];
		
		for(int i = 0; i < this.motors.length; i++)
		{
			this.motors[i] = new MotorCurrent(PDP, motorPorts[i]);
		}
	}
	
	public MotorCurrent[] GetCurrent()
	{	
		UpdateCurrentInfo();
		return motors;
	}
	
	public boolean IsStalling()
	{
		UpdateCurrentInfo();
		
		boolean isStalling = false;
		
		for(int i = 0; i < this.motors.length; i++)
		{
			if(this.motors[i].isStalling)
			{
				isStalling = true; 
			}
			
		}
		
		return isStalling;
	}
	
	public void SetStallCurrent(double stallCurrent)
	{
		for(int i = 0; i < this.motors.length; i++)
		{
			this.motors[i].SetStallCurrent(stallCurrent);
		}
	}
	
	private void UpdateCurrentInfo()
	{
		for (int i = 0; i < motors.length; i++) 
		{
			motors[i].UpdateMotorCurrent();
		}
	}
	
}
