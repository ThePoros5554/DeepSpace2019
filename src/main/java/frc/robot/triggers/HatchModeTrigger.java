/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.triggers;

import edu.wpi.first.wpilibj.GenericHID;
import frc.robot.Robot;
import frc.robot.Robot.RobotMode;

/**
 * Add your docs here.
 */
public class HatchModeTrigger extends ModeTrigger
{
  private boolean activateInHookMode;

  public HatchModeTrigger(GenericHID joystick, int buttonNumber, boolean activateInHookMode)
  {
    super(joystick, buttonNumber, RobotMode.HATCH);
    this.activateInHookMode = activateInHookMode;
  }

  @Override
  public boolean get()
  {
    return (super.get() && (Robot.isHook == activateInHookMode));
  }
}
