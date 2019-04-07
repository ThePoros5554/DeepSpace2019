/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Elevator.ElevatorMode;

public class AdjustElevator extends Command
{
  private ElevatorMode targetMode;
  private int fromCurrentPosition;
  private int targetPosition;
  private double time;
  
  public AdjustElevator(ElevatorMode mode)
  {
    requires(Robot.elevator);
    this.targetMode = mode;
  }

  public AdjustElevator(ElevatorMode mode, double delay)
  {
    requires(Robot.elevator);
    this.targetMode = mode;

    time = Timer.getFPGATimestamp() + delay;

  }
    
  public AdjustElevator(int valueToGoFromCurrentPosition)
  {
    requires(Robot.elevator);
    this.fromCurrentPosition = valueToGoFromCurrentPosition;
    this.targetMode = null;

    time = Timer.getFPGATimestamp();
  }

  public AdjustElevator(int valueToGoFromCurrentPosition, double delay)
  {
    requires(Robot.elevator);
    this.fromCurrentPosition = valueToGoFromCurrentPosition;
    this.targetMode = null;

    time = Timer.getFPGATimestamp() + delay;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
    targetPosition = Robot.elevator.getCurrentPosition() + this.fromCurrentPosition;

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute()
  {
    if (this.targetMode == null)
    {

      if(Timer.getFPGATimestamp() >= time)
      {
        Robot.elevator.setControlMode(ControlMode.MotionMagic);
        Robot.elevator.setTargetPosition(this.targetPosition);
      }
    }
    else
    {
      if(Timer.getFPGATimestamp() >= time)
      {
        Robot.elevator.setControlMode(ControlMode.MotionMagic);
        Robot.elevator.setTargetMode(this.targetMode);
      }
    }

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished()
  {
    if (this.targetMode == null)
    {
      return Robot.elevator.isInTarget(this.targetPosition);
    }
    else
    {
      return Robot.elevator.isInMode(this.targetMode);
    }
  }

  // Called once after isFinished returns true
  @Override
  protected void end()
  {
    Robot.elevator.setControlMode(ControlMode.Position);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted()
  {
    end();
  }
}
