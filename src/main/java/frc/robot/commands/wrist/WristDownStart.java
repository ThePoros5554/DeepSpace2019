/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.wrist;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristMode;

public class WristDownStart extends Command
{
  private WristMode targetMode;

  public WristDownStart()
  {
    requires(Robot.wrist);
    this.targetMode = WristMode.DOWN;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
    Robot.wrist.configMotionValues(Wrist.kMaxAccelerationSpecial, Wrist.kMaxVelocitySpecial);
    Robot.wrist.setControlMode(ControlMode.MotionMagic);

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute()
  {
      Robot.wrist.setTargetPosition(this.targetMode);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished()
  {
    return Robot.wrist.isInMode(this.targetMode);
  }

  // Called once after isFinished returns true
  @Override
  protected void end()
  {
    Robot.wrist.configMotionValues(Wrist.kMaxAcceleration, Wrist.kMaxVelocity);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted()
  {
    end();
  }
}
