package poroslib.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem 
{

	private boolean isReversed = false;
		
	private boolean isRanged = false;
	
	private boolean enabled = false;
			
	private double minSpeedValue = 0;
	private double minRotateValue = 0;

	
    public void SetIsReversed(boolean isReversed)
    {
    	this.isReversed = isReversed;
    }
    
    public boolean IsReversed()
    {
    	return this.isReversed;
    }
    
    public void SetIsEnabled(boolean enabled)
    {
    	this.enabled = enabled;
    }
    
    public boolean IsEnabled()
    {
    	return this.enabled;
    }
    
    public void SetIsRanged(boolean isRanged)
    {
    	this.isRanged = isRanged;
    }
    
    public boolean IsRanged()
    {
    	return this.isRanged;
    }
    
    public void SetMinSpeedValue(double minSpeedValue)
    {
    	this.minSpeedValue = minSpeedValue;
    }
    
    public double GetMinSpeedValue()
    {
    	return this.minSpeedValue;
    }
    
    public void SetMinRotateValue(double minRotateValue)
    {
    	this.minRotateValue = minRotateValue;
    }
    
    public double GetMinRotateValue()
    {
    	return this.minRotateValue;
    }

	@Override
    protected void initDefaultCommand()
    {
	}
	

	
}

