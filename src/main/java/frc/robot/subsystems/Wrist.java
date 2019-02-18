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
import frc.robot.Robot;
import frc.robot.subsystems.Elevator.ElevatorMode;

public class Wrist extends Subsystem {

  // manual values
  public static final double kManualPowerUp = 0.9;
  public static final double kManualPowerDown = -0.7;

  // ports
  private static final int kWristPort = 3;

  // positions for practice robot:
  private static final int kDownPosition = 312;
  private static final int kInsidePosition = 518;
  private static final int kUpPosition = 435;
  private static final int kCargoHighPosition = 365;

  // motion gains
  public static final double kMotionMagicUpSlot = 0;
  public static final double kMotionMagicDownSlot = 1;
  public static final double kF = 1.93018867; // 1024/530 result, 530 rawUnits/100ms

  // config constants
  private static final int kMaxTilt = 530;
  private static final int kMinTilt = 312;
  private static final double kVoltage = 10;
  private static final int kMaxAcceleration = 50;
  private static final NeutralMode kNeutralMode = NeutralMode.Brake;
  private static final boolean kInvertPot = true;
  private static final int kMaxVelocity = 105;
  private static final double kRamp = 0.2;
  private static final int kTargetThreshold = 2;

  private WPI_TalonSRX master;

  private ControlMode controlMode;
  private int maxPositionLimit = kMaxTilt;
  private int minPositionLimit = kMinTilt;

  public enum WristMode
  {
    UP, DOWN, INSIDE, HIGH_CARGO
  }

  private WristMode currentMode = WristMode.INSIDE;
  private int targetPosition;

  public Wrist()
  {
    master = new WPI_TalonSRX(kWristPort);

    master.configSelectedFeedbackSensor(FeedbackDevice.Analog);

    //limitswitches

    //softlimits (if needed)
    this.configForwardSoftLimitThreshold(kMaxTilt, true);
    this.configReverseSoftLimitThreshold(kMinTilt, true);

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
    master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10); // TODO: WTF is this
    this.configProfileSlot(0, 2.3, 0, 01, 30); // up
    this.configProfileSlot(1, 2.3, 0, 01, 30); // TODO: check for down
    this.setNeutralMode(kNeutralMode);

    //this.master.configOpenloopRamp(kRamp);

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

  public boolean isInPosition(int targetPosition)
  {
		int positionError = Math.abs(this.getSensorPosition() - targetPosition);
    
    return positionError < kTargetThreshold;
	}

  public boolean isInMode(WristMode mode)
  {
    switch (mode)
    {
      case DOWN:
      return isInPosition(kDownPosition);

      case UP:
      return isInPosition(kUpPosition);

      case INSIDE:
      return isInPosition(kInsidePosition);

      case HIGH_CARGO:
      return isInPosition(kCargoHighPosition);

      default:
      return false;
    }
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

  public void motionMagicControl()
  {
		manageMotion(targetPosition);
		master.set(ControlMode.MotionMagic, targetPosition);
	}

	/*
	 * controls the position of the collector between upPositionLimit and
	 * downPositionLimit based on the scalar input between -1 and 1.
	 */
  public void motionMagicPositionControl(double positionScalar)
  {
		double encoderPosition = 0;

    if (positionScalar > 0)
    {
			encoderPosition = positionScalar * this.maxPositionLimit;
    }
    else
    {
			encoderPosition = -positionScalar * this.minPositionLimit;
		}
    
    master.set(ControlMode.MotionMagic, encoderPosition);
	}

	/*
	 * choose which set of gains to use based on direction of travel.
	 */
  public void manageMotion(double targetPosition)
  {  
    double currentPosition = getSensorPosition();
  
    manageLimits();

    if (currentPosition < kUpPosition) // inside robot
    {
      if (currentPosition > targetPosition) // moving down
      {
				selectProfileSlot(0);
      }
      else // moving up
      {
				selectProfileSlot(1);
			}
    }
    else // outside robot
    {
      if (currentPosition < targetPosition) // moving down
      {
				selectProfileSlot(0);
      }
      else // moving up
      {
				selectProfileSlot(1);
			}
		}
		
		this.master.configForwardSoftLimitThreshold(kMaxTilt);
		this.master.configReverseSoftLimitThreshold(kMinTilt);
	}

	//Prevents wrist from moving behind the home position whilst elevator is not above first stage
  private void manageLimits()
  {
    if (Robot.elevator.getSensorPosition() < Elevator.kTopOfFirstLevel)
    {
			this.maxPositionLimit = kMaxTilt;
    }
    else
    {
			this.maxPositionLimit = kUpPosition;
      
      if (this.targetPosition < kUpPosition)
      {
				this.targetPosition = kUpPosition;
			}
		}
	}

  public int getTargetPosition()
  {
		return this.targetPosition;
	}
	
	//if valid position is inverted return false else, return true
  public void setTargetPosition(int position)
  {
		manageLimits();
    
    if (isValidPosition(position))
    {
      this.targetPosition = position;
    }
  }
  
  public void setTargetPosition(WristMode position)
  {
    switch (position)
    {
      case DOWN:
      setTargetPosition(kDownPosition);
      break;

      case UP:
      setTargetPosition(kUpPosition);
      break;

      case INSIDE:
      setTargetPosition(kInsidePosition);
      break;

      case HIGH_CARGO:
      setTargetPosition(kCargoHighPosition);
      break;
    }
	}	
	
  public void incrementTargetPosition(int increment)
  {
		int newTargetPosition = this.targetPosition + increment;
    
    if (isValidPosition(newTargetPosition))
    {
			this.targetPosition = newTargetPosition;
		}
	}
	
  public void stopWristInPlace()
  {
		this.master.set(ControlMode.MotionMagic, this.getSensorPosition());
	}
	
  public boolean isValidPosition(int position)
  {
		return (position >= this.minPositionLimit && position <= this.maxPositionLimit);
	}

  @Override
  public void initDefaultCommand()
  {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
