/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class MagicDrive extends Command
{
  private double rightTarget;
  private double leftTarget;

  public MagicDrive(double rightDistance, double leftDistance, boolean reverse)
  {
    requires(Robot.drivetrain);
    
    if (reverse)
    {
      this.rightTarget = -leftDistance;
      this.leftTarget =  -rightDistance;
    }
    else
    {
      this.rightTarget = rightDistance;
      this.leftTarget = leftDistance;
    }
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
    Robot.drivetrain.setControlMode(ControlMode.MotionMagic);
    Robot.drivetrain.set(this.leftTarget, this.rightTarget);
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
    return Robot.drivetrain.isInTarget(this.rightTarget, this.leftTarget);
  }

  // Called once after isFinished returns true
  @Override
  protected void end()
  {
    Robot.drivetrain.setControlMode(ControlMode.PercentOutput);
    Robot.drivetrain.set(0, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted()
  {
    end();
  }
}
