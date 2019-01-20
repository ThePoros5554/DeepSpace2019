package frc.poroslib.triggers;

import edu.wpi.first.wpilibj.Joystick;
import frc.poroslib.util.MathHelper;

public class SmartJoystick extends Joystick
{

	private boolean[] disabledAxis;
	
	private int speedAxis = 0;
	private int rotateAxis = 1;
	private int twistAxis = 2;
	
	private int sliderAxis = 3;
	private int oldMinSlider = -1;
	private int oldMaxSlider = 1;
	private int minSlider = 0;
	private int maxSlider = 1;
	
	public SmartJoystick(int port) 
	{
		super(port);
		
		this.disabledAxis = new boolean[this.getAxisCount()];
		
		for (int i = 0; i < this.disabledAxis.length; i++)
		{
			this.disabledAxis[i] = false;
		}
	}
	
	public void DisableAxis(int axis, boolean isDisabled)
	{
		if(axis < this.disabledAxis.length)
		{
			this.disabledAxis[axis] = isDisabled;
		}
	}
	
	public boolean GetIsDisabled(int axis)
	{
		if (axis < this.disabledAxis.length)
		{

			return this.disabledAxis[axis];

		}
		else
		{
			return false;
		}
	}
	
	@Override
	public double getRawAxis(int axis)
	{
		if (this.disabledAxis[axis] == false)
		{
			return super.getRawAxis(axis);
		}
		else
		{
			return 0;
		}
	}
	
	public void SetSpeedAxis(int speedAxis)
	{
		this.speedAxis = speedAxis;
	}
	
	public void SetRotateAxis(int rotateAxis)
	{
		this.rotateAxis = rotateAxis;
	}
	
	public void SetTwistAxis(int twistAxis)
	{
		this.twistAxis = twistAxis;
	}
	
	public void SetSlider(int sliderAxis, int newMin, int newMax, int oldMin, int oldMax)
	{
		this.sliderAxis = sliderAxis;
		this.oldMinSlider = oldMin;
		this.oldMaxSlider = oldMax;
		this.minSlider = newMin;
		this.maxSlider = newMax;
	}
	
	public double GetSpeedAxis()
	{
		return super.getRawAxis(this.speedAxis);
	}
	
	public double GetRotateAxis()
	{
		return super.getRawAxis(this.rotateAxis);
	}
	
	public double GetTwistAxis()
	{
		return super.getRawAxis(this.twistAxis);
	}
	
	public double GetSlider()
	{
		double sliderValue = this.getRawAxis(this.sliderAxis);
		sliderValue = MathHelper.mapRange(this.oldMinSlider, this.oldMaxSlider, this.minSlider, this.maxSlider, sliderValue);
		
		return (sliderValue);
	}
	
	

}
