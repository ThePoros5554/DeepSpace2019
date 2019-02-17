package poroslib.triggers;

import edu.wpi.first.wpilibj.GenericHID;

public class JoyAxisPart extends JoyAxis
{
    private double newAxisLowLimit;
    private double newAxisUpperLimit;

    public JoyAxisPart(GenericHID joystick, int axisNumber, double newMinValue, double newMaxValue, double oldMinValue, double oldMaxValue,
        double newAxisLowLimit, double newAxisUpperLimit) 
	{
        super(joystick, axisNumber, newMinValue, newMaxValue, oldMinValue, oldMaxValue);

		this.newAxisLowLimit = newAxisLowLimit;
		this.newAxisUpperLimit = newAxisUpperLimit;
	}
	
	
	@Override
	public boolean get()
	{
        double axisValue = joy.getRawAxis(this.axisNumber);

		if(axisValue >= newAxisLowLimit &&  axisValue <= newAxisUpperLimit)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
}
	


