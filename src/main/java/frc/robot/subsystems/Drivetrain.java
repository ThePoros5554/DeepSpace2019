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
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import poroslib.subsystems.DiffDrivetrain;

/**
 * Add your docs here.
 */
public class Drivetrain extends DiffDrivetrain
{
  private WPI_TalonSRX masterLeft;
  private WPI_TalonSRX masterRight;
  private WPI_VictorSPX middleLeft;
  private WPI_VictorSPX middleRight;
  private WPI_VictorSPX rearLeft;
  private WPI_VictorSPX rearRight;

  private AHRS navx;

  private ControlMode controlMode;

  public Drivetrain(WPI_TalonSRX frontLeft, WPI_VictorSPX middleLeft, WPI_VictorSPX rearLeft,
  WPI_TalonSRX frontRight, WPI_VictorSPX middleRight, WPI_VictorSPX rearRight, double rampRate, double voltage)
  {
    super(frontLeft, frontRight);

    this.masterLeft = frontLeft;
    this.masterRight = frontRight;
    this.middleLeft = middleLeft;
    this.middleRight = middleRight;;
    this.rearLeft = rearLeft;
    this.rearRight = rearRight;
    
    // followers
    this.middleLeft.follow(this.masterLeft);
    this.middleRight.follow(this.masterRight);
    this.rearLeft.follow(this.masterLeft);
    this.rearRight.follow(this.masterRight);

    // invertion
    this.masterLeft.setInverted(InvertType.None);
    this.masterRight.setInverted(InvertType.None);
    this.middleLeft.setInverted(InvertType.FollowMaster);
    this.middleRight.setInverted(InvertType.FollowMaster);
    this.rearLeft.setInverted(InvertType.FollowMaster);
    this.rearRight.setInverted(InvertType.FollowMaster);

    // ramp
    this.configRamp(rampRate);

    // sensors
    this.masterLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    this.masterRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    this.masterLeft.setSensorPhase(false);
    this.masterRight.setSensorPhase(false);

    // neutral mode
    this.setNeutralMode(NeutralMode.Brake);

    // voltage compensation
    this.configVoltageCompSaturation(voltage, true);

    this.controlMode = ControlMode.PercentOutput;
    this.set(0, 0);

    this.navx = new AHRS(SPI.Port.kMXP);
  }

  public void set(double speedLeft, double speedRight)
  {
    this.masterLeft.set(controlMode, speedLeft);
    this.masterRight.set(controlMode, speedRight);
  }

  public void configVoltageCompSaturation(double voltage, boolean enableVoltageCompensation)
  {
    this.masterLeft.configVoltageCompSaturation(voltage);
    this.masterRight.configVoltageCompSaturation(voltage);
    this.middleLeft.configVoltageCompSaturation(voltage);
    this.middleRight.configVoltageCompSaturation(voltage);
    this.rearLeft.configVoltageCompSaturation(voltage);
    this.rearRight.configVoltageCompSaturation(voltage);

    this.masterLeft.enableVoltageCompensation(enableVoltageCompensation);
    this.masterRight.enableVoltageCompensation(enableVoltageCompensation);
    this.middleLeft.enableVoltageCompensation(enableVoltageCompensation);
    this.middleRight.enableVoltageCompensation(enableVoltageCompensation);
    this.rearLeft.enableVoltageCompensation(enableVoltageCompensation);
    this.rearRight.enableVoltageCompensation(enableVoltageCompensation);
  }

  public void configRamp(double rampRate)
  {
    this.masterLeft.configClosedloopRamp(rampRate);
    this.masterRight.configClosedloopRamp(rampRate);
    this.middleLeft.configClosedloopRamp(rampRate);
    this.middleRight.configClosedloopRamp(rampRate);
    this.rearLeft.configClosedloopRamp(rampRate);
    this.rearRight.configClosedloopRamp(rampRate);

    this.masterLeft.configOpenloopRamp(rampRate);
    this.masterRight.configOpenloopRamp(rampRate);
    this.middleLeft.configOpenloopRamp(rampRate);
    this.middleRight.configOpenloopRamp(rampRate);
    this.rearLeft.configOpenloopRamp(rampRate);
    this.rearRight.configOpenloopRamp(rampRate);
  }

  public void setControlMode(ControlMode controlMode)
  {
    this.controlMode = controlMode;
  }

  public void setNeutralMode(NeutralMode neutralMode)
  {
    this.masterLeft.setNeutralMode(neutralMode);
    this.masterRight.setNeutralMode(neutralMode);
    this.middleLeft.setNeutralMode(neutralMode);
    this.middleRight.setNeutralMode(neutralMode);
    this.rearLeft.setNeutralMode(neutralMode);
    this.rearRight.setNeutralMode(neutralMode);
  }

  public double getForwardTipAngle()
  {
    return this.navx.getPitch();
  }

  public double getSideTipAngle()
  {
    return this.navx.getRoll();
  }

  @Override
  public double getHeading()
  {
    return this.navx.getYaw();
  }

  public void resetHeading()
  {
    this.navx.reset();
  }

  @Override
  public int getRawLeftPosition()
  {
    return this.masterLeft.getSelectedSensorPosition();
  }

  @Override
  public int getRawRightPosition()
  {
    return this.masterRight.getSelectedSensorPosition();
  }

  public void resetRawPosition()
  {
    this.masterLeft.setSelectedSensorPosition(0);
    this.masterRight.setSelectedSensorPosition(0);
  }

  @Override
  public void initDefaultCommand()
  {
  }
}
