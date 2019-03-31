package poroslib.systems;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import poroslib.util.ControllerOutput;


public class PIDProcessor extends PIDController 
{	
	public PIDSource feedbackDevice;
	
	private boolean resetWhenEnabled;
	
	public enum ToleranceType
	{
		AbsoluteTolerance,
		PercentageTolerance
	}
	
	public PIDProcessor(double Kp, double Ki, double Kd, PIDSource source, boolean isReversed) 
	{
		super(Kp, Ki, Kd, source, new ControllerOutput(isReversed));	
		this.feedbackDevice = source;
	}
	
	public PIDProcessor(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output, boolean isReversed) 
	{
		super(Kp, Ki, Kd, 0, source, new ControllerOutput(isReversed, output), kDefaultPeriod);
		this.feedbackDevice = source;
	}
		
	public void ResetFeedbackDevice()
	{		
		
		if(this.feedbackDevice instanceof edu.wpi.first.wpilibj.Encoder)
		{
			((Encoder)this.feedbackDevice).reset();
		}
		else if(this.feedbackDevice instanceof edu.wpi.first.wpilibj.ADXRS450_Gyro)
		{
			((ADXRS450_Gyro)this.feedbackDevice).reset();
		}		
	}

	public void SetOutput(PIDOutput output)
	{
		this.m_pidOutput = output;
	}
	
	public void SetForRun(double setPoint, boolean resetWhenEnabled)
	{
		double maxInput;
		double minInput;
		
		if(setPoint > 0)
		{
			maxInput = setPoint;
			minInput = 0;
		}
		else
		{
			maxInput = 0;
			minInput = setPoint;
		}
		

		
		this.SetForRun(setPoint, maxInput, minInput, 1 , ToleranceType.AbsoluteTolerance, false, resetWhenEnabled);
	}
	
	public void SetForRun(double setPoint, double absTolerance, boolean resetWhenEnabled)
	{
		this.SetForRun(setPoint, 0, 0, absTolerance, ToleranceType.AbsoluteTolerance, false, resetWhenEnabled);
	}
	
	public void SetForRun(double setPoint, double maxInput, double minInput, double toleranceValue, ToleranceType toleranceType, boolean isContinous, boolean resetWhenEnabled)
	{		
		if(minInput != 0 && maxInput != 0) 
		{
			this.setInputRange(minInput, maxInput);
		}
		
		if(toleranceType == ToleranceType.AbsoluteTolerance)
		{
			this.setAbsoluteTolerance(toleranceValue);
		}
		else
		{
			double inputRange = maxInput- minInput;
			this.setAbsoluteTolerance(((inputRange) / 100.0) * toleranceValue);
		}
		
		this.resetWhenEnabled = resetWhenEnabled;
		
		this.setSetpoint(setPoint);
	}
	
	
	public double GetOutputValue()
	{
		return ((ControllerOutput)this.m_pidOutput).GetOutputValue();
	}
	
	@Override
	public void enable()
	{
		if (this.resetWhenEnabled)
		{
			this.ResetFeedbackDevice();
		}
		
		super.enable();
	}

	// @Override
	// public void calculate()
	// {
		
	// }



}
