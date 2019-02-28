/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.Robot.RobotMode;

public class ToggleClimbMode extends Command
{
  private RobotMode lastGamepieceMode;

  public ToggleClimbMode()
  {
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
    switch (Robot.mode)
    {
      case HATCH:
        this.lastGamepieceMode = RobotMode.HATCH;
        Robot.mode = RobotMode.CLIMB;
        break;
      case CARGO:
        this.lastGamepieceMode = RobotMode.CARGO;
        Robot.mode = RobotMode.CLIMB;
        break;
      case CLIMB:
        Robot.mode = this.lastGamepieceMode;
        break;
    }
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
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end()
  {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted()
  {
  }
}
