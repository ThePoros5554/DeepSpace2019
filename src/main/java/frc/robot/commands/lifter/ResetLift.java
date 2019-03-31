/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.lifter;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Lifter;
import poroslib.sensors.SysPosition;

public class ResetLift extends Command
{

    private double power;

  public ResetLift(double power)
  {
    requires(Robot.lifter);
    this.power = power;
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
    Robot.lifter.setFront(power, Lifter.kConstPowerFront);
    Robot.lifter.setBack(power, Lifter.kConstPowerBack);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished()
  {
    return (Robot.lifter.GetBackPos() == SysPosition.Top) && (Robot.lifter.GetFrontLeftPos() == SysPosition.Top)
        && (Robot.lifter.GetFrontRightPos() == SysPosition.Top);
  }

  // Called once after isFinished returns true
  @Override
  protected void end()
  {
    Robot.lifter.setFrontRight(0, 0);
    Robot.lifter.setFrontRight(0, 0);

  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted()
  {
    end();
  }
}
