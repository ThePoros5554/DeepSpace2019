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
import frc.robot.Robot.LifterMode;

/**
 * Add your docs here.
 */
public class LiftTrigger extends Trigger {
  
  private Button button;
  private LifterMode triggerMode;

  public LiftTrigger(GenericHID joystick, int buttonNumber, LifterMode triggerMode)
  {
    this.button = new JoystickButton(joystick, buttonNumber);
    this.triggerMode = triggerMode;
  }

  @Override
  public boolean get()
  {
    return (Robot.liftMode == this.triggerMode) && this.button.get();
  }
}
