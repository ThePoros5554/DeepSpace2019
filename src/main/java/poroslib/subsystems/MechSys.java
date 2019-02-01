package poroslib.subsystems;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import poroslib.sensors.LimitSensor;
import poroslib.sensors.SysPosition;
import poroslib.systems.CurrentSafety;
import poroslib.systems.SafeSubsystem;
import poroslib.systems.SystemCurrent;
import poroslib.util.MotorCurrent;


public class MechSys extends Subsystem implements PidActionSubsys, SafeSubsystem {

	private SpeedController motor;
	
	private LimitSensor limitSwitch;
	private boolean isLimited = false;

	private boolean isReversed = false;

	private boolean isDisabled = false;
	private boolean isSafetyEnabled = false;
	
	private SystemCurrent systemCurrent;
	private CurrentSafety safety;

	public MechSys(SpeedController motor)
	{
		this.motor = motor;
	}
	
	public MechSys(SpeedController motor1, SpeedController motor2)
	{
		this.motor = new SpeedControllerGroup(motor1, motor2);
	}
	
	public MechSys(SpeedController motor1, SpeedController motor2, boolean inverted)
	{
		motor1.setInverted(inverted);
		this.motor = new SpeedControllerGroup(motor1, motor2);
	}
	

	@Override
	protected void initDefaultCommand()
	{
	}
	
	public void Activate(double speed)
	{				
		this.Activate(speed, 0);
	}
	
	public void Activate(double speed, double zeroValue)
	{		
		double procSpeed;
		
		if(this.isReversed) // reverses the speed so the limit if's will work properly
		{
			procSpeed = -speed;
		}
		else
		{
			procSpeed = speed;
		}
				
		if(this.limitSwitch != null && this.isLimited)
		{

			if(limitSwitch.GetPosition() != SysPosition.Blocked)
			{
				if(procSpeed < 0)
				{
					if(limitSwitch.GetPosition() == SysPosition.Bottom)
					{
						speed = 0;
					}
				}
				else if(procSpeed > 0)
				{
					if(limitSwitch.GetPosition() == SysPosition.Top)
					{
						speed = 0;
					}
				}
				else
				{
					speed = 0;
				}
			}
			else
			{
				speed = 0;
			}
		}
			
		if(speed == 0)
		{

			speed = zeroValue;
		}
		
		if(this.safety != null && this.isSafetyEnabled)
		{
			safety.calculate();
		}
		
		if(!this.isDisabled)
		{
			this.motor.set(speed);
		}
	}
	
	public void SetIsReversed(boolean isReversed)
	{
		this.isReversed = isReversed;
	}
	
	public boolean GetIsReversed()
	{
		return this.isReversed;
	}
	
	public SysPosition GetPosition()
	{
		return limitSwitch.GetPosition();
	}
	
	public void SetLimitSwitch(LimitSensor limitSwitch, boolean isLimited)
	{
		this.limitSwitch = limitSwitch;
		this.isLimited = isLimited;
	}
	
	public void SetLimitSwitch(LimitSensor limitSwitch)
	{
		this.limitSwitch = limitSwitch;
		this.isLimited = true;
	}
	
	public void PidWrite(double output)
	{
		this.motor.pidWrite(output);
	}
	
	public boolean GetIsLimited()
	{
		return this.isLimited;
	}
	
	public void SetIsLimited(boolean isLimited)
	{
		this.isLimited = isLimited;
	}
	
	public void SetSafetyEnabled(boolean isSafetyEnabled)
	{
		this.isSafetyEnabled = isSafetyEnabled;
	}
	
	public boolean IsSafetyEnabled()
	{
		return this.isSafetyEnabled;
	}
	
	public void Stop() 
	{
		this.motor.set(0);
    }
	
	public void SetSystemCurrent(SystemCurrent monitor)
	{
		this.systemCurrent = monitor;
	}
	
	public void SetSystemCurrent(PowerDistributionPanel PDP, int[] motorPorts)
	{
		this.systemCurrent = new SystemCurrent(PDP, motorPorts);
	}
	
	public void SetSafety(double maxAmp, double dangerTime, double sleepTimer)
	{
		if(this.systemCurrent != null)
		{
			this.systemCurrent.SetStallCurrent(maxAmp);
			this.safety = new CurrentSafety(this, this.systemCurrent, dangerTime, sleepTimer);
		}
	}
	
	public MotorCurrent[] GetCurrent()
	{
		if(systemCurrent == null)
		{
			return null;
		}
		else
		{
			return this.systemCurrent.GetCurrent();
		}
	}

	@Override
	public Subsystem GetSubsystem() 
	{
		return this;
	}
	
	@Override
	public void StopSystem() 
	{
		this.Stop();
	}

	@Override
	public void Disable() 
	{
		this.Stop();
		this.isDisabled = true;
		
	}

	@Override
	public void Enable()
	{
		this.isDisabled = false;
	}

	@Override
	public boolean IsDisabled() 
	{
		return this.isDisabled;
	}

	
	
}

