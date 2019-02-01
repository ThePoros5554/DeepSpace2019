package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem
{
    //TODO add real values and decide on the final place of constants
    public static final int kMasterPort = 6;
    
    public static final int kMaxAcceleration = 2;
    public static final int kMaxVelocity = 6;
    
    public static final double kVoltage = 10;
    
    public static final boolean kInvertEnc = false;
    
    public static final double kP = 0;
    public static final double kI = 0;
    public static final double kD = 0;
    
    public static final NeutralMode kNeutralMode = NeutralMode.Brake;
    
    public static final double kRamp = 0.4;
    
    public static final int kMaxHeight = 500;
    public static final int kMinHeight = 0;
    public static final int kHatchLowHeight = 10;
    public static final int kHatchMiddleHeight = 200;
    public static final int kHatchHighHeight = 480;
    public static final int kCargoLowHeight = 50;
    public static final int kCargoMiddleHeight = 245;
    public static final int kCargoHighHeight = 500;

    private WPI_TalonSRX master;
    private ControlMode controlMode;

    public Elevator()
    {
        master = new WPI_TalonSRX(6);

        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        //limitswitches

        //softlimits (if needed)
        this.configForwardSoftLimitThreshold(kMaxHeight, true);
        this.configReverseSoftLimitThreshold(kMinHeight, true);

        //voltage
        this.configVoltageCompSaturation(kVoltage, true);

        //config motion magic
        this.configMotionValues(kMaxAcceleration, kMaxVelocity);
        
        //config (if needed) pid loops

        //followers + slaves

        //Config direction of master and slaves
        master.setSensorPhase(kInvertEnc);
        master.setInverted(InvertType.None);

        //set neutral mode
        this.setNeutralMode(kNeutralMode);

        this.master.configOpenloopRamp(kRamp);

        controlMode = ControlMode.PercentOutput;
        set(0);
    }

    public void setNeutralMode(NeutralMode neturalMode)
    {
        master.setNeutralMode(neturalMode);
    }

    public void configReverseLimit(LimitSwitchSource switchSource, LimitSwitchNormal switchNormal)
    {
        master.configReverseLimitSwitchSource(switchSource, switchNormal, 0);
    }
    public void configForwardLimit(LimitSwitchSource switchSource, LimitSwitchNormal switchNormal)
    {
        master.configForwardLimitSwitchSource(switchSource, switchNormal, 0);
    }
    public void overrideLimitSwitchesEnable(boolean isSwitchesEnabled)
    {
        master.overrideLimitSwitchesEnable(isSwitchesEnabled);
    }

    public void configForwardSoftLimitThreshold(int forwardSensorLimit, boolean enableForwardLimit)
    {
        master.configForwardSoftLimitThreshold(forwardSensorLimit);
        master.configForwardSoftLimitEnable(enableForwardLimit);
    }

    public void configReverseSoftLimitThreshold(int reverseSensorLimit, boolean enableReverseLimit)
    {
        master.configReverseSoftLimitThreshold(reverseSensorLimit);
        master.configReverseSoftLimitEnable(enableReverseLimit);
    }

    public void overrideSoftLimitsEnable(boolean isLimitsEnabled)
    {
        master.overrideSoftLimitsEnable(isLimitsEnabled);
    }

    public void configVoltageCompSaturation(double voltage, boolean enableVoltageCompensation)
    {
        master.configVoltageCompSaturation(voltage);
        master.enableVoltageCompensation(enableVoltageCompensation);
    }

    public void setControlMode(ControlMode controlMode)
    {
        this.controlMode = controlMode;
    }

    public void set(double output)
    {
        master.set(controlMode, output);
    }

    public void configProfileSlot(int profileSlot, double kP, double kI, double kD, double kF)
    {
		master.config_kP(profileSlot, kP);
		master.config_kI(profileSlot, kI);
		master.config_kD(profileSlot, kD);
        master.config_kF(profileSlot, kF);
    }

    public void configMotionValues(int sensorUnitsPer100msPerSec, int sensorUnitsPer100ms)
    {
        master.configMotionAcceleration(sensorUnitsPer100msPerSec);
        master.configMotionCruiseVelocity(sensorUnitsPer100ms);
    }

    public void selectProfileSlot(int profileSlot)
    {
        master.selectProfileSlot(profileSlot, 0);
    }

    public void setSensorPosition(int position)
    {
        master.setSelectedSensorPosition(position);
    }

    @Override
    protected void initDefaultCommand()
    {
    }
}