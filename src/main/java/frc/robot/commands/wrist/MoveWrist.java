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
import poroslib.triggers.JoyAxis;

public class MoveWrist extends Command
{
  private double power;
  private JoyAxis powerAxis;

  private double velocity;

  public MoveWrist(double power)
  {
    requires(Robot.wrist);
    this.power = power;
  }

  public MoveWrist(JoyAxis powerAxis)
  {
    requires(Robot.wrist);
    this.powerAxis = powerAxis;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
    Robot.wrist.setControlMode(ControlMode.PercentOutput);
    this.velocity = 0;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() 
  {
    if (powerAxis == null)
    {
      double currentVelocity = Robot.wrist.getCurrentPosition(); 

      if(currentVelocity > velocity)
      {
        velocity = currentVelocity;
      }
      Robot.wrist.set(power);
    }
    else
    {
      Robot.wrist.set(this.powerAxis.GetAxisValue());
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished()
  {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end()
  {
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