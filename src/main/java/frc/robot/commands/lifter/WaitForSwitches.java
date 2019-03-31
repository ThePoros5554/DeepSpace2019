/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.lifter;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import poroslib.sensors.SysPosition;

public class WaitForSwitches extends Command {
  
  private boolean waitForFrontUp;

  public WaitForSwitches(boolean waitForFrontUp)
  {
    this.waitForFrontUp = waitForFrontUp;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute()
  {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    
    if (waitForFrontUp)
    {
      return Robot.lifter.GetFrontLeftPos() == SysPosition.Top
      &&
      Robot.lifter.GetFrontRightPos() == SysPosition.Top;
    }
    return Robot.lifter.GetBackPos() == SysPosition.Bottom
    &&
    Robot.lifter.GetFrontLeftPos() == SysPosition.Bottom
    &&
    Robot.lifter.GetFrontRightPos() == SysPosition.Bottom;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
