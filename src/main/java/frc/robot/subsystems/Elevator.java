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
import frc.robot.subsystems.Wrist.WristMode;

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

    private static final int kMaxHeight = 47500;
    private static final int kMinHeight = 0;
    private static final int targetThreshold = 0;

    private WPI_TalonSRX master;
    
    private ControlMode controlMode;

    public enum ElevatorMode
    {
        FLOOR(0),
        LOW_HATCH(12000),
        LOW_CARGO(13000),
        MIDDLE_HATCH(22000),
        MIDDLE_CARGO(23000),
        HIGH_HATCH(30000),
        HIGH_CARGO(31000);

        private final int position;

        private ElevatorMode(int position)
        {
            this.position = position;
        }

        public int getPosition()
        {
            return position;
        }
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
        this.configProfileSlot(0, 0.5, 0, 0, 0.6); // up
        //configProfileSlot(1, 5.3, 0, 0.3, 0);

        this.master.configOpenloopRamp(kRamp);

        this.targetPosition = ElevatorMode.FLOOR.position;

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
          this.set(mode.position);
      }
    }
  
    public boolean isInMode(ElevatorMode mode)
    {
        return this.isInTarget(mode.position);
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

    public int getTargetPosition()
    {
        return this.targetPosition;
    }
      
      //if valid position is inverted return false else, return true
    public void setTargetPosition(int position)
    {    
        this.targetPosition = position;
    }

    public void setTargetMode(ElevatorMode mode)
    {    
        this.targetPosition = mode.position;
    }

    @Override
    protected void initDefaultCommand()
    {
    }

    public void enableLimitSwitch(boolean isEnabled)
    {
        master.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX, LimitSwitchNormal.NormallyClosed, Robot.drivetrain.GetSwitchDeviceId(), 10);
        master.overrideLimitSwitchesEnable(isEnabled);
    }

    public void manageLimits()
    {
        if (Robot.wrist.getCurrentPosition() < WristMode.UP.getPosition())
        {
            configForwardSoftLimitThreshold(0, true);
        }
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
          master.set(ControlMode.MotionMagic, targetPosition);
        }
    }

    public void neutralOutput()
    {
        master.neutralOutput();
    }
}