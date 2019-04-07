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
import frc.robot.subsystems.Wrist.WristMode;

public class Elevator extends Subsystem
{
    // ports
    private final int kElevatorMasterPort = 2;
    
    // positions
    public static final int kLittleUpPosition = 2000;
    public static final int kWristCanGo90FromHere = 22600;
    public static final int kTopOfFirstLevel = 2800;
    public final int kCompletelyDownThreshold = 1410;
    
    // motion gains
    private final int kMotionMagicSlot = 0;
    private final int kPositionSlot = 1;
    private final int kMaxAcceleration = 5000;
    private final int kMaxVelocity = 5000;
    // private final int kMagicUpSlot = 0;
    // private final int kMagicDownSlot = 1;
    private final double kMagicP = 0.3; // 0.7
    private final double kMagicI = 0.0001; // 0.007
    private final double kMagicD = 0;
    private final double kMagicF =  0.25;
    private final double kPositionP = 0.07; // 0.5
    private final double kPositionI = 0;
    private final double kPositionD = 0;
    private final double kPositionF = 0;

    // config constants
    private final double kVoltage = 10;
    private final boolean kInvertEnc = false;
    private final NeutralMode kNeutralMode = NeutralMode.Brake;
    private final double kRamp = 0;

    private final int kMaxHeight = 48000;
    private final int kMinHeight = 0;
    private final int kTargetThreshold = 15;

    private WPI_TalonSRX master;
    
    private ControlMode controlMode;

    public enum ElevatorMode
    {
        FLOOR(0), //TODO: ADD FLOOR BUTTON
        COLLECT_CARGO(4780),
        COLLECT_CARGO_FEEDER(21300),
        COLLECT_HATCH(1500), 
        LOW_HATCH(6200),
        LOW_CARGO(8960),
        MIDDLE_HATCH(27570),
        MIDDLE_CARGO(30280),
        CARGO_SHIP(31500),
        HIGH_HATCH(45850),
        HIGH_CARGO(47500),
        LIFT(17000);

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

        // limitswitches
        master.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX, LimitSwitchNormal.NormallyClosed, Robot.drivetrain.GetSwitchDeviceId(), 10);
        // softlimits
        configForwardSoftLimitThreshold(kMaxHeight, true);

        //voltage
        configVoltageCompSaturation(kVoltage, false);

        //config motion magic
        configMotionValues(kMaxAcceleration, kMaxVelocity);
        
        // ramp
        configRamp(kRamp);

        // config output
		master.configNominalOutputForward(0);
		master.configNominalOutputReverse(0);
		master.configPeakOutputForward(1);
        master.configPeakOutputReverse(-1);

        // config direction of master and slaves
        master.setSensorPhase(kInvertEnc);
        master.setInverted(InvertType.InvertMotorOutput);

        // neutral mode
        setNeutralMode(kNeutralMode);

        master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10);
        configProfileSlot(kMotionMagicSlot, kMagicP, kMagicI, kMagicD, kMagicF); // up
        configProfileSlot(kPositionSlot, kPositionP, kPositionI, kPositionD, kPositionF);

        this.targetPosition = getCurrentPosition();

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
		int positionError = Math.abs(this.getCurrentPosition() - target);
    
        return positionError < kTargetThreshold;
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
    
    public void setTargetPosition(int position)
    {    
        this.targetPosition = position;
    }

    public void setTargetMode(ElevatorMode mode)
    {    
        setTargetPosition(mode.position);
    }

    @Override
    protected void initDefaultCommand()
    {
    }

    public void enableLimitSwitch(boolean isEnabled)
    {
        master.overrideLimitSwitchesEnable(isEnabled);
    }

    public void manageLimits()
    {
        if (Robot.wrist.getCurrentPosition() < Wrist.kFurtherInLimit && getCurrentPosition() < kCompletelyDownThreshold)
        {
            configForwardSoftLimitThreshold(kMinHeight, true);
        }
        else
        {
            configForwardSoftLimitThreshold(kMaxHeight, true);
        }

        if (getCurrentPosition() > 33000 && Robot.wrist.getCurrentPosition() < WristMode.UP.getPosition())
        {
            configReverseSoftLimitThreshold(33000, true);
            System.out.println("limited");
        }
        else
        {
            configReverseSoftLimitThreshold(kMinHeight, false);
        }
    }

    public void neutralOutput()
    {
        master.neutralOutput();
    }

    @Override
    public void periodic()
    {
        if (Robot.drivetrain.getIsElevatorLimit())
        {
            this.setSensorPosition(0);
        }

       manageLimits();

        if (this.controlMode == ControlMode.MotionMagic)
        {
            this.selectProfileSlot(kMotionMagicSlot);
            master.set(ControlMode.MotionMagic, targetPosition);
        }
        else if(this.controlMode == ControlMode.Position)
        {
            this.selectProfileSlot(kPositionSlot);
            master.set(ControlMode.Position, targetPosition);
        }
    }
}