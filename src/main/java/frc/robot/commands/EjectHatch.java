/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.drive.MagicDrive;
import frc.robot.commands.hatch_launcher.ActivateLauncher;
import frc.robot.commands.hatch_launcher.RetractLauncher;
import poroslib.commands.auto.Timeout;

public class EjectHatch extends CommandGroup {
  /**
   * Add your docs here.
   */
  public EjectHatch(double backDistance)
  {
    addParallel(new ActivateLauncher());
    //addSequential(new MagicDrive(backDistance, backDistance, true));
    //addParallel(new RetractLauncher());
  }
}
