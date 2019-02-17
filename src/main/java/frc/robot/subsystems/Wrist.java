/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Wrist extends Subsystem {

  //
  public static final double kManualPowerUp = 0.9;
  public static final double kManualPowerDown = -0.7;

  // ports:
  private static final int kWristPort = 3;

  // positions for practice robot:
  private static final int kDownPosition = 312;
  private static final int kInsidePosition = 518;
  private static final int kUpPosition = 435;
  private static final int kCargoHighPosition = 365;

  //gains
  public static final double motionMagicUpSlot = 0;
  public static final double motionKF = 1.93018867;//1024/530;//530 rawUnits/100ms

  // config constants:
  private static final int kMaxTilt = 530;
  private static final int kMinTilt = 312;
  private static final double kVoltage = 10;
  private static final int kMaxAcceleration = 3;
  private static final NeutralMode kNeutralMode = NeutralMode.Brake;
  private static final boolean kInvertPot = true;
  private static final int kMaxVelocity = 5;
  private static final double kRamp = 0.2;
  private static final int kTargetThreshold = 2;

  private WPI_TalonSRX master;
  private ControlMode controlMode;

  public enum WristMode
  {
    UP, DOWN, INSIDE, HIGH_CARGO
  }

  private WristMode currentMode = WristMode.INSIDE;

  public Wrist()
  {
    master = new WPI_TalonSRX(kWristPort);

    master.configSelectedFeedbackSensor(FeedbackDevice.Analog);

    //limitswitches

    //softlimits (if needed)
    this.configForwardSoftLimitThreshold(kMaxTilt, false);
    this.configReverseSoftLimitThreshold(kMinTilt, false);

    //voltage
    this.configVoltageCompSaturation(kVoltage, false);

    //config motion magic
    this.configMotionValues(kMaxAcceleration, kMaxVelocity);
    
    //config (if needed) pid loops

		master.configNominalOutputForward(0);
		master.configNominalOutputReverse(0);
		master.configPeakOutputForward(1);
    master.configPeakOutputReverse(-1);
        
    //Config direction of master and slaves
    master.setSensorPhase(kInvertPot);
    master.setInverted(InvertType.InvertMotorOutput);
    
    //motion parameters
    master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10);
    this.configProfileSlot(0, 2.3, 0, 01, 30);
    master.configMotionCruiseVelocity(105, 0);
    master.configMotionAcceleration(50, 0);
    
		master.configSelectedFeedbackSensor(FeedbackDevice.Analog);

    //set neutral mode
    this.setNeutralMode(kNeutralMode);

//  this.master.configOpenloopRamp(kRamp);

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

  public void set(double value)
  {
    master.set(controlMode, value);
  }

  public void set(WristMode mode)
  {
    if (this.controlMode == ControlMode.MotionMagic || this.controlMode == ControlMode.Position)
    {
      System.out.println("passed if");
      switch (mode)
      {
        case DOWN:
        set(kDownPosition);
        break;

        case UP:
        set(kUpPosition);
        System.out.println("Set wrist up");
        break;

        case INSIDE:
        set(kInsidePosition);
        break;

        case HIGH_CARGO:
        set(kCargoHighPosition);
        break;
      }
    }
  }

  public boolean isInMode(WristMode mode)
  {
    if (mode == this.currentMode)
      return true;

    switch (mode)
    {
      case DOWN:
      return isInTarget(kDownPosition);

      case UP:
      return isInTarget(kUpPosition);

      case INSIDE:
      return isInTarget(kInsidePosition);

      case HIGH_CARGO:
      return isInTarget(kCargoHighPosition);

      default:
      return false;
    }
  }

  public boolean isInTarget(int target)
  {
    int sensorpos = getSensorPosition();

    return (target >= (sensorpos - kTargetThreshold) && target <= (sensorpos + kTargetThreshold));
  }

  public void configProfileSlot(int profileSlot, double kP, double kI, double kD, double kF)
  {
    master.config_kP(profileSlot, kP, 10);
    master.config_kI(profileSlot, kI, 10);
    master.config_kD(profileSlot, kD, 10);
    master.config_kF(profileSlot, kF, 10);
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

  public int getWristVelocity()
  {
      return master.getSelectedSensorVelocity();
  }

  public void setWristMode(WristMode mode)
  {
    this.currentMode = mode; 
  }

  public WristMode getWristMode()
  {
    return this.currentMode;
  }

  @Override
  public void initDefaultCommand()
  {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
