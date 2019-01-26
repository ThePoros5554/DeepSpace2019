/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.poroslib.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Command;
import frc.poroslib.triggers.SmartJoystick;

public class RumbleJoystick extends Command {
  
  private SmartJoystick joystick;
  private double rumble = 0;
  
  public RumbleJoystick(SmartJoystick joystick, double rumblePower)
  {
    this.joystick = joystick;
    this.rumble = rumblePower;
  }

  public RumbleJoystick(SmartJoystick joystick, double rumblePower, double seconds)
  {
    super(seconds);

    this.joystick = joystick;
    this.rumble = rumblePower;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
    if (joystick != null)
    {
      this.joystick.setRumble(RumbleType.kRightRumble, rumble);
      this.joystick.setRumble(RumbleType.kLeftRumble, rumble);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished()
  {
    return this.isTimedOut();
  }

  // Called once after isFinished returns true
  @Override
  protected void end()
  {
    if (joystick != null)
    {
      this.joystick.setRumble(RumbleType.kRightRumble, 0);
      this.joystick.setRumble(RumbleType.kLeftRumble, 0);
    }
  }
}
