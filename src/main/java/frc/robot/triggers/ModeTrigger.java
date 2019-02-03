/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.triggers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import frc.robot.Robot;
import frc.robot.Robot.RobotMode;

/**
 * Add your docs here.
 */
public class ModeTrigger extends Trigger {
  
  private Button button;
  private RobotMode triggerMode;

  public ModeTrigger(GenericHID joystick, int buttonNumber, RobotMode triggerMode)
  {
    this.button = new JoystickButton(joystick, buttonNumber);
    this.triggerMode = triggerMode;
  }

  @Override
  public boolean get()
  {
    return (Robot.mode == this.triggerMode) && this.button.get();
  }
}
