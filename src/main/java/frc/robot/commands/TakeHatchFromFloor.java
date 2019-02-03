/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.wrist.AdjustWrist;
import frc.robot.subsystems.Wrist;
import poroslib.commands.auto.Timeout;

public class TakeHatchFromFloor extends CommandGroup
{
  /**
   * Add your docs here.
   */
  public TakeHatchFromFloor()
  {
   addSequential(new AdjustWrist(Wrist.kFloor));
   addSequential(new Timeout(0.3));
   addSequential(new AdjustWrist(Wrist.kStraight));
  }
}
