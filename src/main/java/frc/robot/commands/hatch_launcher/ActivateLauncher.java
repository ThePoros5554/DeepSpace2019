/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hatch_launcher;

import frc.robot.Robot;
import poroslib.commands.OpenDoubleSolenoid;

public class ActivateLauncher extends OpenDoubleSolenoid
{
  public ActivateLauncher()
  {
    super(Robot.hatchLauncher);
  }
}