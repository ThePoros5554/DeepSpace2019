package poroslib.sensors;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class AnalogUltrasonic implements PIDSource
{
	private double VI;
	private AnalogInput ultraSonic;
	
	private PIDSourceType pst;
	
    public AnalogUltrasonic(int port)
    {
    	this.ultraSonic = new AnalogInput(port);
    	System.out.println(5/512);
    	VI = (512/5);
    	System.out.println(VI);
    }
    
    public AnalogUltrasonic(int port, double VI)
    {
    	this.ultraSonic = new AnalogInput(port);
    	this.VI = VI;
    }
    public double GetRange()
    {
        double Vm = ultraSonic.getVoltage();
        double Ri;
        
        Ri = Vm*this.VI;
        
        return Ri;
    }
    

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) 
	{
		this.pst= pidSource;
		
	}

	@Override
	public PIDSourceType getPIDSourceType() 
	{
		return this.pst;
	}

	@Override
	public double pidGet() 
	{
		return GetRange();
	}
}
