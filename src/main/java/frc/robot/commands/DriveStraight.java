/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import poroslib.triggers.SmartJoystick;

public class DriveStraight extends Command
{
  private SmartJoystick joystickLeft;
  private SmartJoystick joystickRight;

  public DriveStraight(SmartJoystick joyLeft, SmartJoystick joyRight)
  {
    requires(Robot.drivetrain);
    this.joystickLeft = joyLeft;
    this.joystickRight = joyRight;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
    Robot.drivetrain.setControlMode(ControlMode.PercentOutput);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute()
  {
    if (this.joystickLeft != null && this.joystickRight != null)
    {
      double power = (this.joystickLeft.GetSpeedAxis() + this.joystickRight.GetSpeedAxis()) / 2;

      Robot.drivetrain.tankDrive(power, power, this.joystickRight.GetSlider()); // TODO: may change max output parmeter to max possible output (1)
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
