/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.lifter;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Lifter;
import poroslib.sensors.NavxSource;
import poroslib.sensors.SysPosition;
import poroslib.sensors.NavxSource.NavxAxis;
import poroslib.systems.PIDProcessor;
import poroslib.util.MathHelper;
import poroslib.util.Speed;

public class LiftRobot extends Command
{
    private PIDProcessor sideController;
    private PIDProcessor frontController;
    
    private Speed speed;

  public LiftRobot(Speed speed)
  {
    requires(Robot.lifter);

    this.speed = speed;

    sideController = new PIDProcessor(0.04,0,0,new NavxSource(Robot.drivetrain.getNavx(), NavxAxis.roll), false);
    sideController.setInputRange(-180, 180);
    sideController.setContinuous(false);

    frontController = new PIDProcessor(0.035,0,0,new NavxSource(Robot.drivetrain.getNavx(), NavxAxis.pitch), false);
    frontController.setInputRange(-180, 180);
    frontController.setContinuous(false);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
    sideController.setSetpoint(0);
    sideController.enable();

    frontController.setSetpoint(0);
    frontController.enable();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute()
  {
      double power = speed.GetValue();

      System.out.println("power: " + power);
      if(power != 0)
      {
        double pitchCorrection;

        if(MathHelper.handleDeadband(frontController.getError(), 1.25) == 0)
        {
          pitchCorrection = 0;
        }
        else
        {
          pitchCorrection = frontController.GetOutputValue();  
        }
        
        Robot.lifter.setFrontRight(pitchCorrection + power + sideController.GetOutputValue(), Lifter.kConstPowerFront);//1.2, power 1.23
        Robot.lifter.setFrontLeft(pitchCorrection + power - sideController.GetOutputValue(), Lifter.kConstPowerFront);//1.2, power 1.23

        Robot.lifter.setBack(-pitchCorrection + power, Lifter.kConstPowerBack);//1.12      
      }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished()
  {
    // return (Robot.lifter.GetBackPos() == SysPosition.Bottom ||
    //         Robot.lifter.GetBackPos() == SysPosition.Blocked) &&
    //         Robot.lifter.GetFrontLeftPos() == SysPosition.Bottom &&
    //         Robot.lifter.GetFrontRightPos() == SysPosition.Bottom;
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end()
  {
    Robot.lifter.setFront(0, Lifter.kConstPowerFront);
    Robot.lifter.setBack(0, Lifter.kConstPowerBack);
    sideController.reset();
    frontController.reset();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted()
  {
    end();
  }
}
