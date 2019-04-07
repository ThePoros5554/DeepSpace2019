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
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;

public class Wrist extends Subsystem
{
  // ports
  private final int kWristPort = 3;

  // positions
  private final int kBackLimitForElevatorPosition = 1980;
  public static final int kFurtherInLimit = 1410;

  // motion gains
  private final int kPositionSlot = 2;
  private final int kMagicSlot = 0;
  // private final int kMagicUpSlot = 0;
  // private final int kMagicDownSlot = 1;
  private final double kMagicP = 1;
  private final double kMagicI = 0;
  private final double kMagicD = 0.1;
  private final double kMagicF = 2.046;
  private final double kPositionP = 0.1;
  private final double kPositionI = 0;
  private final double kPositionD = 0;
  private final double kPositionF = 0;
  // private final double kUpP = 1;
  // private final double kUpI = 0;
  // private final double kUpD = 0.1;
  // private final double kUpF = 2.046;
  // private final double kDownP = 1;
  // private final double kDownI = 0;
  // private final double kDownD = 0.1;
  // private final double kDownF = 2.046;
  

  // config constants
  private final int kMaxTilt = 2825;
  private final int kMinTilt = 1390;
  private final double kVoltage = 10;

  private int maxPositionLimit = kMaxTilt;
  private int minPositionLimit = kMinTilt;

  public static final int kMaxAcceleration = 500;
  public static int kMaxVelocity = 500;
  public static final int kMaxAccelerationSpecial = 250;
  public static final int kMaxVelocitySpecial = 250;

  private final NeutralMode kNeutralMode = NeutralMode.Brake;
  private final boolean kInvertEnc = true;
  private final double kRamp = 0.2;
  private final int kTargetThreshold = 2;

  private WPI_TalonSRX master;

  private ControlMode controlMode;

  public enum WristMode
  {
    UP(1780),
    DOWN(2775),
    INSIDE(1000),
    COLLECT_CARGO(3100),
    HIGH_CARGO(2450);
    
    private final int position;

    private WristMode(int position)
    {
        this.position = position;
    }

    public int getPosition()
    {
      return position;
    }
  }

  private int targetPosition;

  public Wrist()
  {
    master = new WPI_TalonSRX(kWristPort);

    master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);

    // softlimits (if needed)
    configForwardSoftLimitThreshold(kMaxTilt, true);
    configReverseSoftLimitThreshold(kMinTilt, true);

    // voltage
    configVoltageCompSaturation(kVoltage, false);

    //config motion magic
    configMotionValues(kMaxAcceleration, kMaxVelocity);
    
    // config output
		master.configNominalOutputForward(0);
		master.configNominalOutputReverse(0);
		master.configPeakOutputForward(1);
    master.configPeakOutputReverse(-1);
        
    // config direction of master and slaves
    master.setSensorPhase(kInvertEnc);
    master.setInverted(InvertType.InvertMotorOutput);
    
    // motion parameters
    master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10);
    configProfileSlot(kPositionSlot, kPositionP, kPositionI, kPositionD, kPositionF);
    configProfileSlot(kMagicSlot, kMagicP, kMagicI, kMagicD, kMagicF); // up
    //configProfileSlot(kMagicUpSlot, kUpP, kUpI, kUpD, kUpF); // up
    //configProfileSlot(kMagicDownSlot, kDownP, kDownI, kDownD, kDownF); // TODO: check for down

    setNeutralMode(kNeutralMode);

    master.configOpenloopRamp(kRamp);

    this.targetPosition = getCurrentPosition();

    controlMode = ControlMode.PercentOutput;
    set(0);
  }

  public void setNeutralMode(NeutralMode neturalMode)
  {
    master.setNeutralMode(neturalMode);
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
    manageLimits();
    master.set(controlMode, value);
  }

  public boolean isInMode(WristMode mode)
  {
		int positionError = Math.abs(this.getCurrentPosition() - mode.position);
    
    return positionError < kTargetThreshold;
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

  public int getCurrentPosition()
  {
    return master.getSelectedSensorPosition();
  }

  public int getVelocity()
  {
    return master.getSelectedSensorVelocity();
  }

	/*
	 * choose which set of gains to use based on direction of travel.
	 */
  // public void manageMotion()
  // {  
  //   double currentPosition = getCurrentPosition();

  //   if (currentPosition > WristMode.UP.position) // outside robot
  //   {
  //     if (currentPosition < this.targetPosition) // if moving down
  //     {
	// 			selectProfileSlot(kMagicDownSlot);
  //     }
  //     else
  //     {
	// 			selectProfileSlot(kMagicUpSlot);
  //     }
  //   }
  //   else // outside robot
  //   {
  //     if (currentPosition > this.targetPosition) // if moving down
  //     {
	// 			selectProfileSlot(kMagicDownSlot);
  //     }
  //     else // moving up
  //     {
	// 			selectProfileSlot(kMagicUpSlot);
  //     }
	// 	}
	// }

  /*
   * limits wrist from colliding with the robot, the floor and the elevator.
   */
  private void manageLimits()
  {
    int elevatorPosition = Robot.elevator.getCurrentPosition();

    // if elevator is within the first level, wrist can be rotated inside the robot. else, limit wrist to upright position:
    if (elevatorPosition > Elevator.kTopOfFirstLevel && elevatorPosition < Elevator.kWristCanGo90FromHere)
    {
      this.minPositionLimit = kBackLimitForElevatorPosition;
      
      if (this.targetPosition < kBackLimitForElevatorPosition)
      {
        this.targetPosition = kBackLimitForElevatorPosition;
			}
    }
    else
    {
			this.minPositionLimit = kMinTilt;
    }

    // if elevator is a little up, wrist can be rotated further down. else, limit wrist to floor position:
    if (elevatorPosition > Elevator.kLittleUpPosition)
    {
      this.maxPositionLimit = WristMode.COLLECT_CARGO.position;
    }
    else
    {
      this.maxPositionLimit = kMaxTilt;

      if (this.targetPosition > kMaxTilt)
      {
        this.targetPosition = kMaxTilt;
      }
    }
    
    this.master.configReverseSoftLimitThreshold(this.minPositionLimit);
    this.master.configForwardSoftLimitThreshold(this.maxPositionLimit);
	}

  public int getTargetPosition()
  {
		return this.targetPosition;
  }
	
  public void setTargetPosition(int position)
  {
    // if valid position is inverted return false. else, return true.    
    if (isValidPosition(position))
    {
      this.targetPosition = position;
    }
  }
  
  public void setTargetPosition(WristMode mode)
  {
    setTargetPosition(mode.position);
  }
  	
  public boolean isValidPosition(int position)
  {
		return (position >= this.minPositionLimit && position <= this.maxPositionLimit);
  }	
	
  public void neutralOutput()
  {
    master.neutralOutput();
  }

  public int getWristDeviceId()
  {
    return master.getDeviceID();
  }

  @Override
  public void initDefaultCommand()
  {
  }

  @Override
  public void periodic()
  {
    manageLimits();

    if (this.controlMode == ControlMode.MotionMagic)
    {
      this.selectProfileSlot(kMagicSlot); // switch with ManageMotion() if necessary
      master.set(ControlMode.MotionMagic, targetPosition);
    }
    else if(this.controlMode == ControlMode.Position)
    {
      this.selectProfileSlot(kPositionSlot);
      master.set(ControlMode.Position, targetPosition);
    }
  }
}
