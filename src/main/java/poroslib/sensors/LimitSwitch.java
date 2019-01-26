package poroslib.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

public class LimitSwitch extends DigitalInput implements LimitSensor
{
	private SysPosition limitPos;
	private boolean isNormallyClosed;
	
	public LimitSwitch (int channel, SysPosition limitPos, boolean isNormallyClosed)
	{
		super(channel);
		
		this.limitPos = limitPos;
		this.isNormallyClosed = isNormallyClosed;
	}
	
	@Override
	public SysPosition GetPosition ()
	{
		if(this.isNormallyClosed)
		{
			if(get())
			{
				return this.limitPos;
			}
			else
			{
				return SysPosition.Free;
			}
		}
		else
		{
			if(!get())
			{
				return this.limitPos;
			}
			else
			{
				return SysPosition.Free;
			}
		}
		
	}
}
