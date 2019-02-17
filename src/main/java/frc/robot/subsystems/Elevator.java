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
    public static final int kFloorPosition = 0;
    public static final int kHatchLowPosition = 10;
    public static final int kHatchMiddlePosition = 200;
    public static final int kHatchHighPosition = 480;
    public static final int kCargoLowPosition = 50;
    public static final int kCargoMiddlePosition = 245;
    public static final int kCargoHighPosition = 500;

    //
    private static final int kElevatorMasterPort = 2;
    //

    private static final int kMaxAcceleration = 2;
    private static final int kMaxVelocity = 6;
    private static final double kVoltage = 10;
    private static final boolean kInvertEnc = false;
    private static final double kP = 0;
    private static final double kI = 0;
    private static final double kD = 0;
    private static final NeutralMode kNeutralMode = NeutralMode.Brake;
    private static final double kRamp = 0.4;
    private static final int kMaxHeight = 500;
    private static final int kMinHeight = 0;
    private static final int targetThreshold = 2;

    private WPI_TalonSRX master;
    private ControlMode controlMode;
    private ElevatorMode currentMode;

    public enum ElevatorMode
    {
        FLOOR, LOW_HATCH, LOW_CARGO, MIDDLE_HATCH, MIDDLE_CARGO, HIGH_HATCH, HIGH_CARGO
    }

    public Elevator()
    {
        master = new WPI_TalonSRX(kElevatorMasterPort);

        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        //limitswitches

        //softlimits (if needed)
        this.configForwardSoftLimitThreshold(kMaxHeight, false);
        this.configReverseSoftLimitThreshold(kMinHeight, false);

        //voltage
        this.configVoltageCompSaturation(kVoltage, true);

        //config motion magic
        this.configMotionValues(kMaxAcceleration, kMaxVelocity);
        
        // rampi
        this.configRamp(kRamp);

        //config (if needed) pid loops

        //followers + slaves

        //Config direction of master and slaves
        master.setSensorPhase(kInvertEnc);
        master.setInverted(InvertType.None);

        //set neutral mode
        this.setNeutralMode(kNeutralMode);

        this.master.configOpenloopRamp(kRamp);

        currentMode = ElevatorMode.FLOOR;

        controlMode = ControlMode.PercentOutput;
        set(0);
    }

    public void setNeutralMode(NeutralMode neutralMode)
    {
        master.setNeutralMode(neutralMode);
    }

    public void configRamp(double ramp)
    {
        this.master.configOpenloopRamp(ramp);
//        this.master.configClosedloopRamp(ramp);
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
    }

    public void setControlMode(ControlMode controlMode)
    {
        this.controlMode = controlMode;
    }

    public void set(double output)
    {
        master.set(controlMode, output);
    }

    public void set(ElevatorMode mode)
    {
      if (this.controlMode == ControlMode.MotionMagic || this.controlMode == ControlMode.Position)
      {
        switch (this.currentMode)
        {
          case FLOOR:
          set(kFloorPosition);
          break;
  
          case LOW_HATCH:
          set(kHatchLowPosition);
          break;
  
          case LOW_CARGO:
          set(kCargoLowPosition);
          break;
  
          case MIDDLE_HATCH:
          set(kHatchMiddlePosition);
          break;
  
          case MIDDLE_CARGO:
          set(kCargoMiddlePosition);
          break;

          case HIGH_HATCH:
          set(kHatchHighPosition);
          break;
  
          case HIGH_CARGO:
          set(kCargoHighPosition);
          break;
        }
      }
    }
  
    public boolean isInMode(ElevatorMode mode)
    {
      if (mode == this.currentMode)
        return true;
  
        switch (mode)
        {
          case FLOOR:
          return isInTarget(kFloorPosition);
  
          case LOW_HATCH:
          return isInTarget(kHatchLowPosition);
  
          case LOW_CARGO:
          return isInTarget(kCargoLowPosition);
  
          case MIDDLE_HATCH:
          return isInTarget(kHatchMiddlePosition);
  
          case MIDDLE_CARGO:
          return isInTarget(kCargoMiddlePosition);

          case HIGH_HATCH:
          return isInTarget(kHatchHighPosition);
  
          case HIGH_CARGO:
          return isInTarget(kCargoHighPosition);

          default:
          return false;
        }
    }

    public boolean isInTarget(int target)
    {
      int sensorpos = getSensorPosition();
  
      return (target >= (sensorpos - targetThreshold) && target <= (sensorpos + targetThreshold));
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

    public int getSensorPosition()
    {
        return master.getSelectedSensorPosition();
    }

    public void setElevatorMode(ElevatorMode currentLevel)
    {
        this.currentMode = currentLevel;
    }

    public ElevatorMode getsetElevatorMode()
    {
        return this.currentMode;
    }

    @Override
    protected void initDefaultCommand()
    {
    }
}