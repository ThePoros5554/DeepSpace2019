package poroslib.util;

public class Speed 
{
	private double value;
	
	public Speed(double speedValue)
	{
		this.value = speedValue;
	}
	
	public void SetValue(double speedValue)
	{
		this.value = speedValue;
	}
	
	public double GetValue()
	{
		return this.value;
	}
}
