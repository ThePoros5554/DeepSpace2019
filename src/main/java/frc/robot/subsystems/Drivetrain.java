/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

//robot width 85
//robot length 66.5
//robot max speed 7750 native/100ms


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
  public static final int kFrontLeftPort = 0; // Talon SRX with encoder
  public static final int kFrontRightPort = 1; // Talon SRX with encoder
  private static final int kMiddleLeftPort = 1; // Talon SRX
  private static final int kMiddleRightPort = 3; // Talon SRX
  private static final int kRearLeftPort = 5; // Victor SPX
  private static final int kRearRightPort = 6; // Victor SPX

  // motion gains
  
  // motion magic
  private static final int kMagicSlot = 0; 
  private static final double kMagicP = 0.005; 
  private static final double kMagicI = 0;
  private static final double kMagicD = 0;
  private static final double kMagicF = 0.132; //kf = 1023/7750
  
  // position
  private static final int kPositionSlot = 1; 
  private static final double kPositionP = 0.015; 
  private static final double kPositionI = 0.0000015;
  private static final int kPositionIMaxAccum = 60000;
  private static final double kPositionD = 0.005;
  private static final double kPositionF = 0;

  // config constants
  private static final double kVoltage = 12;
  private static final double kWheelDiameter = 10.16 * Math.PI;
  private static final double kEncoderTicks = 4096;
  private static final boolean kInvertEncLeft = false;
  private static final boolean kInvertEncRight = true;
  private static final double kRamp = 0;
  public static final double kEjectDriveBackDistance = 14.3;
  //public static final double TRACKWIDTH = 72.5;
  private static final NeutralMode kNeutralMode = NeutralMode.Brake;
  private static final int kTargetThreshold = 0;

  //

  private WPI_TalonSRX masterLeft;
  private WPI_TalonSRX masterRight;
  private WPI_TalonSRX middleLeft;
  private WPI_TalonSRX middleRight;
  private WPI_VictorSPX rearLeft;
  private WPI_VictorSPX rearRight;

  private AHRS navx;

  private ControlMode controlMode;

  public Drivetrain(WPI_TalonSRX masterLeft, WPI_TalonSRX masterRight)
  {
    super(masterLeft, masterRight);

    this.masterLeft = masterLeft;
    this.masterRight = masterRight;
    this.middleLeft = new WPI_TalonSRX(kMiddleLeftPort);
    this.middleRight = new WPI_TalonSRX(kMiddleRightPort);
    this.rearLeft = new WPI_VictorSPX(kRearLeftPort);
    this.rearRight = new WPI_VictorSPX(kRearRightPort);
    
    // followers
    this.middleLeft.follow(this.masterLeft);
    this.middleRight.follow(this.masterRight);
    this.rearLeft.follow(this.masterLeft);
    this.rearRight.follow(this.masterRight);

    // invertion
    this.masterLeft.setInverted(InvertType.InvertMotorOutput);
    this.masterRight.setInverted(InvertType.InvertMotorOutput);
    this.middleLeft.setInverted(InvertType.FollowMaster);
    this.middleRight.setInverted(InvertType.FollowMaster);
    this.rearLeft.setInverted(InvertType.FollowMaster);
    this.rearRight.setInverted(InvertType.FollowMaster);

    // ramp
    this.configOpenLoopRamp(kRamp);

    // sensors
    this.masterLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    this.masterRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    this.masterLeft.setSensorPhase(kInvertEncLeft);
    this.masterRight.setSensorPhase(kInvertEncRight);

    // motion magic
    this.configMotionValues(2000, 1500);
    this.configProfileSlot(kMagicSlot, kMagicP, kMagicI, kMagicD, kMagicF); 

    // position control
    this.configProfileSlot(kPositionSlot, kPositionP, kPositionI, kPositionD, kPositionF);
    this.masterLeft.configMaxIntegralAccumulator(kPositionSlot, kPositionIMaxAccum);
    this.masterRight.configMaxIntegralAccumulator(kPositionSlot, kPositionIMaxAccum);

    // neutral mode
    this.setNeutralMode(kNeutralMode);

    // voltage compensation
    this.configVoltageCompensation(kVoltage, false);

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

  public void configVoltageCompensation(double voltage, boolean enableVoltageCompensation)
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

  public void configOpenLoopRamp(double rampRate)
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
    return this.masterLeft.getSelectedSensorPosition(); // TODO: if not good change left phase to true and add - here (though should work?)
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

  public static int cmToRotations(double cm)
  {
    return (int)(kEncoderTicks * cm / kWheelDiameter);
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

  public boolean isInTarget(int rightTarget, int leftTarget)
  {
    int rPositionError = Math.abs(getRawRightPosition() - rightTarget);
    int lPositionError = Math.abs(getRawLeftPosition() - leftTarget);
    
    boolean isInTargetRight = rPositionError < kTargetThreshold;
    boolean isInTargetLeft = lPositionError < kTargetThreshold;

    return isInTargetLeft && isInTargetRight;
  }

  public int getVelocity()
  {
    return (this.masterLeft.getSelectedSensorVelocity() + this.masterRight.getSelectedSensorVelocity()) / 2;
  }

  public int GetSwitchDeviceId()
  {
    return middleLeft.getDeviceID();
  }

  public boolean getIsElevatorLimit()
  {
    return !middleLeft.getSensorCollection().isRevLimitSwitchClosed();
  }

  @Override
  public void initDefaultCommand()
  {
  }
}
