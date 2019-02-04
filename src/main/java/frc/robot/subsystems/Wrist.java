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
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Wrist extends Subsystem {
  
  public static final int kFloor = 0;
  public static final int kInside = 1;
  public static final int kStraight = 2;
  public static final double kManualPowerUp = 0.9;
  public static final double kManualPowerDown = -0.7;

  //
  private static final int kWristPort = 3;
  //

  private static final int kMaxTilt = 0;
  private static final int kMinTilt = 1;
  private static final double kVoltage = 0;
  private static final int kMaxAcceleration = 0;
  private static final NeutralMode kNeutralMode = null;
  private static final boolean kInvertPot = false;
  private static final int kMaxVelocity = 0;
  private static final double kRamp = 0;

  private WPI_TalonSRX master;
  private ControlMode controlMode;

  public Wrist()
  {
    master = new WPI_TalonSRX(kWristPort);

    master.configSelectedFeedbackSensor(FeedbackDevice.Analog);

    //limitswitches

    //softlimits (if needed)
    this.configForwardSoftLimitThreshold(kMaxTilt, true);
    this.configReverseSoftLimitThreshold(kMinTilt, true);

    //voltage
    this.configVoltageCompSaturation(kVoltage, true);

    //config motion magic
    this.configMotionValues(kMaxAcceleration, kMaxVelocity);
    
    //config (if needed) pid loops

    //followers + slaves

    //Config direction of master and slaves
    master.setSensorPhase(kInvertPot);
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

  public int getSensorPosition()
  {
      return master.getSelectedSensorPosition();
  }

  @Override
  public void initDefaultCommand()
  {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
