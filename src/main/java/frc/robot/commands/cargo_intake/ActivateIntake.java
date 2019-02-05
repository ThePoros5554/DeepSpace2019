/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.cargo_intake;

import frc.robot.Robot;
import poroslib.commands.ActivateMechSys;

public class ActivateIntake extends ActivateMechSys
{  
  public ActivateIntake(double power)
  {
    super(Robot.cargoIntake, power);
  }
}
