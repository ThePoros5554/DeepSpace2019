package frc.poroslib.util;

public class MathHelper 
{	
	public static double mapRange(double oldMin, double oldMax, double newMin , double newMax, double value)
	{
		double newSlope = (newMax - newMin) / (oldMax - oldMin);
		double newB = newMax - (newSlope*oldMax);

		double output = (newSlope * value) + newB;
		
		return output;
	}
	
    public static double handleDeadband(double value, double deadband) 
    {
    	double newValue = 0;
    	
    	if(Math.abs(value) > Math.abs(deadband))
    	{
    		newValue = value;
    	}
    	
        return newValue;
    }
}

