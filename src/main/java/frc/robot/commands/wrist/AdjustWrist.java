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

public class AdjustWrist extends Command
{

  private int targetPosition = 0;

  public AdjustWrist()
  {
    requires(Robot.wrist);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
    Robot.wrist.setControlMode(ControlMode.MotionMagic);
    
    switch (Robot.mode)
    {
      case HATCH:
        this.targetPosition = Wrist.kStraight;
        break;
      
      case CARGO:
        this.targetPosition = Wrist.kFloor;
        break;

      case CLIMB:
        this.targetPosition = Wrist.kInside;
        break;
    }

    Robot.wrist.set(this.targetPosition);
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
    return Robot.wrist.getSensorPosition() == this.targetPosition;
  }

  // Called once after isFinished returns true
  @Override
  protected void end()
  {
    Robot.wrist.setControlMode(ControlMode.PercentOutput);
    Robot.wrist.set(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted()
  {
    end();
  }
}
