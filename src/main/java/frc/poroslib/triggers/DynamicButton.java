package frc.poroslib.triggers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Trigger;


public class DynamicButton extends Trigger
{
	private int buttonNum;
	private GenericHID joy;
	
	public DynamicButton(GenericHID joystick, int buttonNumber) 
	{
		
		this.buttonNum = buttonNumber;
		this.joy = joystick;
	}
	
	public void SetButton(int buttonNumber)
	{
		this.buttonNum = buttonNumber;
	}
	
	@Override
	public boolean get()
	{
		if(joy.getRawButton(this.buttonNum))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
}
