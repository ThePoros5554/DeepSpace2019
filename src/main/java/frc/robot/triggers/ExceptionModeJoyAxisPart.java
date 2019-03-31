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
import poroslib.triggers.JoyAxisPart;

/**
 * Add your docs here.
 */
public class ExceptionModeJoyAxisPart extends JoyAxisPart {
  
  private RobotMode triggerMode;

  public ExceptionModeJoyAxisPart(GenericHID joystick, int axisNumber, double newMinValue, double newMaxValue, double oldMinValue, double oldMaxValue,
  double newAxisLowLimit, double newAxisUpperLimit, RobotMode triggerMode)
  {
    super(joystick, axisNumber, newMinValue, newMaxValue, oldMinValue, oldMaxValue, newAxisLowLimit, newAxisUpperLimit);
    this.triggerMode = triggerMode;
  }

  @Override
  public boolean get()
  {
    return (Robot.mode != this.triggerMode) && super.get();
  }
}
