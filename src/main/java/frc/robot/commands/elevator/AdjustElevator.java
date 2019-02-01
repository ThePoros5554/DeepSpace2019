/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ElevatorLevel;

public class AdjustElevator extends Command
{
  private ElevatorLevel targetLevel = ElevatorLevel.FLOOR;
  private int targetPosition = 0;
  
  public AdjustElevator(ElevatorLevel level)
  {
    requires(Robot.elevator);
    this.targetLevel = level;
  }

  private void autoSetPosition()
  {
    switch (this.targetLevel)
    {
      case FLOOR:
        this.targetPosition = Elevator.kFloor;
        break;
      
      case FIRST:
        switch (Robot.mode)
        {
          case HATCH: this.targetPosition = Elevator.kHatchLowHeight; break;
          case CARGO: this.targetPosition = Elevator.kCargoLowHeight; break;
          case CLIMB: this.targetPosition = Elevator.kFloor; break;
        }
        break;

      case SECOND:
        switch (Robot.mode)
        {
          case HATCH: this.targetPosition = Elevator.kHatchMiddleHeight; break;
          case CARGO: this.targetPosition = Elevator.kCargoMiddleHeight; break;
          case CLIMB: this.targetPosition = Elevator.kFloor; break;
        }
        break;
        
      case THIRD:
        switch (Robot.mode)
        {
          case HATCH: this.targetPosition = Elevator.kHatchHighHeight; break;
          case CARGO: this.targetPosition = Elevator.kCargoHighHeight; break;
          case CLIMB: this.targetPosition = Elevator.kFloor; break;
        }
        break;
    }
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
    autoSetPosition();
    Robot.elevator.setControlMode(ControlMode.MotionMagic);
    Robot.elevator.set(this.targetPosition);
    Robot.elevator.setCurrentLevel(this.targetLevel);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute()
  {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished()
  {
    return Robot.elevator.getSensorPosition() == this.targetPosition;
  }

  // Called once after isFinished returns true
  @Override
  protected void end()
  {
    Robot.elevator.setControlMode(ControlMode.PercentOutput);
    Robot.elevator.set(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted()
  {
    end();
  }
}
