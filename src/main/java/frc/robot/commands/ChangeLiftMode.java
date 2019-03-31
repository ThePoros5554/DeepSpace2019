/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*------------------------------------------
----------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.Robot.LifterMode;

public class ChangeLiftMode extends Command
{
  /**
   * Add your docs here.
   */
  public ChangeLiftMode()
  {
 
  }

    @Override
    protected void initialize()
    {
        if(Robot.liftMode == LifterMode.BACK)
        {
            Robot.liftMode = LifterMode.FORWARD;
        }
        else
        {
            Robot.liftMode = LifterMode.BACK;
        }
    }

    @Override
    protected boolean isFinished() 
    {
        return true;
    }
}
