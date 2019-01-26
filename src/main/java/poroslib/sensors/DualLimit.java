package poroslib.sensors;

public class DualLimit implements LimitSensor
{
	private LimitSensor first;
	private LimitSensor second;
	
	public DualLimit (LimitSensor first, LimitSensor second)
	{
		this.first = first;
		this.second = second;
	}
	
	@Override
	public SysPosition GetPosition ()
	{
		if(first.GetPosition() != SysPosition.Free && second.GetPosition() != SysPosition.Free)
		{
			return SysPosition.Blocked;
		}
		else if(first.GetPosition() != SysPosition.Free)
		{
			return first.GetPosition();
		}
		else if(second.GetPosition() != SysPosition.Free)
		{
			return second.GetPosition();
		}
		else
		{
			return SysPosition.Free;
		}
	}
}
