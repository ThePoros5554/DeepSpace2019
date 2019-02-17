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

public class Drivetrain extends DiffDrivetrain
{
  // ports
  public static final int kFrontLeftPort = 0;
  public static final int kFrontRightPort = 1;
  private static final int kMiddleRightPort = 2;
  private static final int kRearRightPort = 3;
  private static final int kMiddleLeftPort = 0;
  private static final int kRearLeftPort = 1;

  // motion gains
  private static final double kP = 0;
  private static final double kI = 0;
  private static final double kD = 0;
  private static final double kF = 0;

  // config constants
  private static final double kVoltage = 12;
  private static final double kWheelDiameter = 10.16 * Math.PI;
  private static final double kEncoderTicks = 4096;
  private static final boolean kInvertEncLeft = true;
  private static final boolean kInvertEncRight = false;
  private static final double kRamp = 0.3;
  public static final double kEjectDriveBackDistance = 14.3;
  private static final NeutralMode kNeutralMode = NeutralMode.Brake;
  private static final int kTargetThreshold = 0;

  //

  private WPI_TalonSRX masterLeft;
  private WPI_TalonSRX masterRight;
  private WPI_VictorSPX middleLeft;
  private WPI_VictorSPX middleRight;
  private WPI_VictorSPX rearLeft;
  private WPI_VictorSPX rearRight;

  private AHRS navx;

  private ControlMode controlMode;

  public Drivetrain(WPI_TalonSRX masterLeft, WPI_TalonSRX masterRight)
  {
    super(masterLeft, masterRight);

    this.masterLeft = masterLeft;
    this.masterRight = masterRight;
    this.middleLeft = new WPI_VictorSPX(kMiddleLeftPort);
    this.middleRight = new WPI_VictorSPX(kMiddleRightPort);
    this.rearLeft = new WPI_VictorSPX(kRearLeftPort);
    this.rearRight = new WPI_VictorSPX(kRearRightPort);
    
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
    this.configRamp(kRamp);

    // sensors
    this.masterLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    this.masterRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    this.masterLeft.setSensorPhase(kInvertEncLeft);
    this.masterRight.setSensorPhase(kInvertEncRight);

    // neutral mode
    this.setNeutralMode(kNeutralMode);

    // voltage compensation
    this.configVoltageCompSaturation(kVoltage, false);

    this.masterLeft.configNominalOutputForward(0);
		this.masterLeft.configNominalOutputReverse(0);
		this.masterLeft.configPeakOutputForward(1);
    this.masterLeft.configPeakOutputReverse(-1);

    this.masterRight.configNominalOutputForward(0);
		this.masterRight.configNominalOutputReverse(0);
		this.masterRight.configPeakOutputForward(1);
    this.masterRight.configPeakOutputReverse(-1);

    this.controlMode = ControlMode.PercentOutput;
    this.set(0, 0);

    this.navx = new AHRS(SPI.Port.kMXP);
  }

  public void set(double valueLeft, double valueRight)
  {
    this.masterLeft.set(controlMode, valueLeft);
    this.masterRight.set(controlMode, valueRight);
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

  public double getLeftPositionInCm()
  {
    return rotationsToCm(getRawLeftPosition());
  }

  public double getRightPositionInCm()
  {
    return rotationsToCm(getRawRightPosition());
  }

  public double rotationsToCm(int rotations)
  {
    return rotations * kWheelDiameter / kEncoderTicks;
  }

  public void resetRawPosition()
  {
    this.masterLeft.setSelectedSensorPosition(0);
    this.masterRight.setSelectedSensorPosition(0);
  }

  public void configProfileSlot(int profileSlot, double kP, double kI, double kD, double kF)
  {
    this.masterLeft.config_kP(profileSlot, kP);
    this.masterLeft.config_kI(profileSlot, kI);
    this.masterLeft.config_kD(profileSlot, kD);
    this.masterLeft.config_kF(profileSlot, kF);

    this.masterRight.config_kP(profileSlot, kP);
    this.masterRight.config_kI(profileSlot, kI);
    this.masterRight.config_kD(profileSlot, kD);
    this.masterRight.config_kF(profileSlot, kF);
  }

  public void configMotionValues(int sensorUnitsPer100msPerSec, int sensorUnitsPer100ms)
  {
    this.masterLeft.configMotionAcceleration(sensorUnitsPer100msPerSec);
    this.masterLeft.configMotionCruiseVelocity(sensorUnitsPer100ms);

    this.masterRight.configMotionAcceleration(sensorUnitsPer100msPerSec);
    this.masterRight.configMotionCruiseVelocity(sensorUnitsPer100ms);
  }

  public void selectProfileSlot(int profileSlot)
  {
    this.masterLeft.selectProfileSlot(profileSlot, 0);
    this.masterRight.selectProfileSlot(profileSlot, 0);
  }

  public boolean isInTarget(double rightTarget, double leftTarget)
  {
    int rightpos = getRawRightPosition();
    int leftpos = getRawLeftPosition();
    boolean isInTargetRight = rightTarget >= (rightpos - kTargetThreshold) && rightTarget <= (rightpos + kTargetThreshold);
    boolean isInTargetLeft = leftTarget >= (leftpos - kTargetThreshold) && leftTarget <= (leftpos + kTargetThreshold);

    return isInTargetLeft && isInTargetRight;
  }

  @Override
  public void initDefaultCommand()
  {
  }
}
