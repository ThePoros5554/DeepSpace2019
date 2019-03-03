/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hatch_launcher;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.drive.MagicDrive;
import poroslib.commands.RumbleJoystick;
import poroslib.triggers.SmartJoystick;

public class StopHatchLauncher extends CommandGroup {
  /**
   * Add your docs here.
   */
  public StopHatchLauncher(SmartJoystick joystickForStopRumble)
  {
    addParallel(new RetractLauncher());
    addParallel(new RumbleJoystick(joystickForStopRumble, 0));
    //addSequential(new MagicDrive(backDistance, backDistance, true));
    //addParallel(new RetractLauncher());
  }
}
