package poroslib.util;

import edu.wpi.first.wpilibj.PIDOutput;

public class ControllerOutput implements PIDOutput
{
		
	private double outputValue;
	private PIDOutput outputDevice;
	private boolean isReversed;
	
	public ControllerOutput(boolean isReversed)
	{
		this.isReversed = isReversed;
	}
	
	public ControllerOutput(boolean isReversed, PIDOutput outputDevice)
	{
		this.isReversed = isReversed;
		this.outputDevice = outputDevice;
	}
		
	@Override
	public void pidWrite(double output) 
	{
		if(this.isReversed)
		{
			this.outputValue = -output;
		}
		else
		{
			this.outputValue = output;
		}
		
		if(this.outputDevice != null)
		{
			this.outputDevice.pidWrite(this.outputValue);	
		}
	}
		
	public double GetOutputValue()
	{
		return this.outputValue;
	}
		
}

