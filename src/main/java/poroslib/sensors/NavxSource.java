package poroslib.sensors;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class NavxSource implements PIDSource 
{
    private AHRS ahrs;
    private NavxAxis axis;

    public enum NavxAxis
    {
        yaw,
        roll,
        pitch;
    }

    public NavxSource(AHRS ahrs, NavxAxis axis) 
    {
        this.ahrs = ahrs;
        this.axis = axis;
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) 
    {

    }

    @Override
    public PIDSourceType getPIDSourceType()
     {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public double pidGet() 
    {
        
        if(this.axis == NavxAxis.pitch)
        {
            return ahrs.getPitch();
        }
        else if(this.axis == NavxAxis.roll)
        {
            return ahrs.getRoll();
        }
        
        return ahrs.getYaw();        
    }
	
}
