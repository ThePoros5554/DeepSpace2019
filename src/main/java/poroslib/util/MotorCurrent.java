package poroslib.util;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class MotorCurrent
{
	private PowerDistributionPanel PDP;
	private int port;
	
	private double current;
	
	private double stallCurrent = 0;
	public boolean isStalling = false;
	
	public MotorCurrent(PowerDistributionPanel PDP, int port)
	{
		this.PDP = PDP;
		this.port = port;
	}
	
	public int GetPort()
	{
		return this.port;
	}
	
	public double GetCurrent()
	{
		UpdateMotorCurrent();
		return this.current;
	}
	
	public boolean IsStalling()
	{
		UpdateMotorCurrent();
		return this.isStalling;
	}
	
	public void UpdateMotorCurrent()
	{
		this.current = this.PDP.getCurrent(this.port);
		
		if (stallCurrent != 0 && Math.abs(this.current) >= Math.abs(stallCurrent))
		{
			this.isStalling = true;
		}
		else
		{
			this.isStalling = false;
		}
	}
	
	public void SetStallCurrent(double stallCurrent)
	{
		this.stallCurrent = stallCurrent;
	}
}
