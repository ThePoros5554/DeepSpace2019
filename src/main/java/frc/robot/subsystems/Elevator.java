package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.commands.elevator.ElevatorHold;

public class Elevator extends Subsystem
{
    // ports
    private static final int kElevatorMasterPort = 2;

    // positions for practice robot:
    private static final int kFloorPosition = 0;
    private static final int kHatchLowPosition = 10;
    private static final int kHatchMiddlePosition = 22000;
    private static final int kHatchHighPosition = 480;
    private static final int kCargoLowPosition = 50;
    private static final int kCargoMiddlePosition = 245;
    private static final int kCargoHighPosition = 500;
    public static final int kTopOfFirstLevel = 8000;
    
    // motion gains
    private static final int kMaxAcceleration = (int)(3126 * 0.8);
    private static final int kMaxVelocity = (int)(3126 * 0.8);
    private static final double kP = 0;
    private static final double kI = 0;
    private static final double kD = 0;

    // config constants
    private static final double kVoltage = 10;
    private static final boolean kInvertEnc = false;
    private static final NeutralMode kNeutralMode = NeutralMode.Brake;
    private static final double kRamp = 0;
    private static final int kMaxHeight = 48470;
    private static final int kMinHeight = 0;
    private static final int targetThreshold = 0;

    private WPI_TalonSRX master;
    
    private ControlMode controlMode;
    
    private ElevatorMode currentMode;

    public enum ElevatorMode
    {
        FLOOR, LOW_HATCH, LOW_CARGO, MIDDLE_HATCH, MIDDLE_CARGO, HIGH_HATCH, HIGH_CARGO
    }

    private int targetPosition;

    public Elevator()
    {
        master = new WPI_TalonSRX(kElevatorMasterPort);

        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        //limitswitches

        //softlimits (if needed)
        this.configForwardSoftLimitThreshold(kMaxHeight, true);

        //voltage
        //this.configVoltageCompSaturation(kVoltage, false);

        //config motion magic
        this.configMotionValues(kMaxAcceleration, kMaxVelocity);
        
        // rampi
        //this.configRamp(kRamp);

        //config (if needed) pid loops
		master.configNominalOutputForward(0);
		master.configNominalOutputReverse(0);
		master.configPeakOutputForward(1);
        master.configPeakOutputReverse(-1);

        //followers + slaves

        //Config direction of master and slaves
        master.setSensorPhase(kInvertEnc);
        master.setInverted(InvertType.InvertMotorOutput);

        //set neutral mode
        this.setNeutralMode(kNeutralMode);

        master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10); // TODO: WTF is this
        this.configProfileSlot(0, 1, 0, 0, 0.6); // up
        //configProfileSlot(1, 5.3, 0, 0.3, 0);

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
        master.configOpenloopRamp(ramp);
        master.configClosedloopRamp(ramp);
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

    public int getSelectedSensorVelocity()
    {
        return master.getSelectedSensorVelocity();
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

    public ControlMode getControlMode()
    {
        return this.controlMode;
    }

    public void set(ElevatorMode mode)
    {
      if (this.controlMode == ControlMode.MotionMagic || this.controlMode == ControlMode.Position)
      {
        System.out.println("ee");
        switch (mode)
        {
          case FLOOR:
          set(kFloorPosition);
          System.out.println("set");
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
      int sensorpos = getCurrentPosition();
  
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

    public int getCurrentPosition()
    {
        return master.getSelectedSensorPosition();
    }

    public void setElevatorMode(ElevatorMode currentLevel)
    {
        this.currentMode = currentLevel;
    }

    public ElevatorMode getElevatorMode()
    {
        return this.currentMode;
    }

    public int getTargetPosition()
    {
          return this.targetPosition;
      }
      
      //if valid position is inverted return false else, return true
    public void setTargetPosition(int position)
    {    
        this.targetPosition = position;
    }

    @Override
    protected void initDefaultCommand()
    {
        //setDefaultCommand(new ElevatorHold());
    }

    public void enableLimitSwitch(boolean isEnabled)
    {
        master.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX, LimitSwitchNormal.NormallyClosed, Robot.drivetrain.GetSwitchDeviceId(), 10);
        master.overrideLimitSwitchesEnable(isEnabled);
    }

    @Override
    public void periodic()
    {
        if (Robot.drivetrain.getIsElevatorLimit())
        {
            this.setSensorPosition(0);
        }

        if (this.controlMode == ControlMode.MotionMagic)
        {
          master.set(ControlMode.MotionMagic, this.targetPosition);

        }
    }
}