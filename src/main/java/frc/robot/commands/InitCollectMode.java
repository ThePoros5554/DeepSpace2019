/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.elevator.AdjustElevator;
import frc.robot.commands.wrist.AdjustWrist;
import frc.robot.subsystems.Elevator.ElevatorLevel;

public class InitCollectMode extends CommandGroup
{
  /**
   * Add your docs here.
   */
  public InitCollectMode()
  {
    addSequential(new AdjustElevator(ElevatorLevel.FLOOR));
    addParallel(new AdjustWrist());
  }
}
