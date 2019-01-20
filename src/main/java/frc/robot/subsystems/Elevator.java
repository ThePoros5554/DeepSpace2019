package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Elevator
{
    private WPI_TalonSRX master;
    private ControlMode controlMode;

    public Elevator(int port)
    {
        master = new WPI_TalonSRX(port);

        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        //limitswitches

        //softlimits (if needed)

        //voltage

        //config motion magic

        //config (if needed) pid loops

        //followers + slaves

        //Config direction of master and slaves
        master.setSensorPhase(false);
        master.setInverted(InvertType.None);

        controlMode = ControlMode.PercentOutput;
        set(0);
    }

    public void setNeutralMode(NeutralMode neturalMode)
    {
        master.setNeutralMode(neturalMode);
    }

    public void configurateReverseLimit(LimitSwitchSource switchSource, LimitSwitchNormal switchNormal)
    {
        master.configReverseLimitSwitchSource(switchSource, switchNormal, 0);
    }
    public void configurateForwardLimit(LimitSwitchSource switchSource, LimitSwitchNormal switchNormal)
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
		master.config_kI(profileSlot, kD);
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
}