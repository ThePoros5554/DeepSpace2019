package poroslib.systems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class CurrentSafety 
{
	private SafeSubsystem subsystem;
	
	private SystemCurrent systemCurrent;
	
	private double dangerTime;	
	private Timer dangerTimer = new Timer();
	
	private double sleepTime;
	private Timer sleepTimer = new Timer();
	
	private boolean isTimerEnabled = false;
	
	public CurrentSafety(SafeSubsystem subsystem, SystemCurrent systemCurrent, double dangerTime, double sleepTimer)
	{
		this.subsystem = subsystem;

		this.systemCurrent = systemCurrent;
		
		this.dangerTime = dangerTime;
		this.sleepTime = sleepTimer;
	}

	public void calculate() 
	{
		if (!subsystem.IsDisabled()) 
		{			
			if (this.systemCurrent.IsStalling()) 
			{
				if (!this.isTimerEnabled) 
				{
					this.dangerTimer.reset();
					this.dangerTimer.start();
					this.isTimerEnabled = true;
				}
					
				if (this.dangerTimer.hasPeriodPassed(this.dangerTime)) 
				{ 					
					this.SendStallingReport();
					this.subsystem.Disable();
					this.dangerTimer.stop();
					this.isTimerEnabled = false;
					
					this.sleepTimer.reset();
					this.sleepTimer.start();
				}
			} 
			else 
			{
				dangerTimer.stop();
				dangerTimer.reset();
				isTimerEnabled = false;
					
			}
			
		}
		else if (this.WakeUp()) 
		{
			subsystem.Enable();
		}
		
	}
	
	private void SendStallingReport()
	{
		for (int i = 0; i < this.systemCurrent.GetCurrent().length; i++) 
		{
			if(this.systemCurrent.GetCurrent()[i].isStalling)
			{
				int stallingPort = this.systemCurrent.GetCurrent()[i].GetPort();
				DriverStation.reportError(String.format("It appears that the motor connected to port %s is stalling... "
						+ "\nPutting motor in sleep mode to avoid additional damage.", stallingPort), false);
						
			}
		}
	}
	
	private boolean WakeUp()
	{
		if (this.sleepTimer.get() >= this.sleepTime) 
		{
			this.sleepTimer.stop();
			this.sleepTimer.reset();
			return true;
		}
		
		return false;
	}
		
	
}
