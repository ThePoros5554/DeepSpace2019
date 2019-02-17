package poroslib.triggers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Trigger;
import poroslib.util.MathHelper;

public class JoyAxis extends Trigger
{
	protected int axisNumber;
	protected GenericHID joy;
	
	protected double newMinValue;
	protected double newMaxValue;
	
	protected double oldMinValue;
	protected double oldMaxValue;

	public JoyAxis(GenericHID joystick, int axisNumber, double newMinValue, double newMaxValue, double oldMinValue, double oldMaxValue) 
	{
		this.axisNumber = axisNumber;
		this.joy = joystick;
		
		this.newMinValue = newMinValue;
		this.newMaxValue = newMaxValue;
		
		this.oldMinValue = oldMinValue;
		this.oldMaxValue = oldMaxValue;
	}
	
	
	@Override
	public boolean get()
	{
		if(joy.getRawAxis(this.axisNumber) != 0)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	public double GetAxisValue()
	{
		double value = joy.getRawAxis(this.axisNumber);
		
		return MathHelper.mapRange(this.oldMaxValue, this.oldMinValue, this.newMinValue, this.newMaxValue, value);
	}
}
	


