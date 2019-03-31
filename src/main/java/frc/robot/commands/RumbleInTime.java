/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import poroslib.triggers.SmartJoystick;

public class RumbleInTime extends Command {
  
  private SmartJoystick joystick;
  private double rumble = 0;
  private double timeForVibration = 0;
  private double timeToVibrate = 0;
  
  public RumbleInTime(SmartJoystick joystick, double rumblePower, double timeToVibrate, double timeForVibration)
  {
    this.joystick = joystick;
    this.rumble = rumblePower;
    this.timeForVibration = timeForVibration;
    this.timeToVibrate = timeToVibrate;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
  }

  @Override
  protected void execute()
  {
    if ((joystick != null) && (Robot.timeLeft <= timeForVibration) && (Robot.timeLeft >= (timeForVibration - timeToVibrate)) && Robot.teleopInit)
    {
      this.joystick.setRumble(RumbleType.kRightRumble, rumble);
      this.joystick.setRumble(RumbleType.kLeftRumble, rumble);
    }
    else
    {
      this.joystick.setRumble(RumbleType.kRightRumble, 0);
      this.joystick.setRumble(RumbleType.kLeftRumble, 0);
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
  }
}