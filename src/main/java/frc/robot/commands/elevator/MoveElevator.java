/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import poroslib.triggers.JoyAxis;

public class MoveElevator extends Command {

  private double power;
  private JoyAxis powerAxis;

  private double velocity;


  public MoveElevator(JoyAxis moveAxis)
  {
    requires(Robot.elevator);
    this.powerAxis = moveAxis;
  }

  public MoveElevator(double power)
  {
    requires(Robot.elevator);
    this.power = power;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
    Robot.elevator.setControlMode(ControlMode.PercentOutput);
    this.velocity = 0;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute()
  {
    if(powerAxis == null)
    {
      double currentVelocity = Robot.elevator.getCurrentPosition(); 

      if(currentVelocity > velocity)
      {
        velocity = currentVelocity;
      }
      Robot.elevator.set(power);

      System.out.println("as;diasdfhasdfjkfuh");
      System.out.println("as;diasdfhasdfjkfuh");
      System.out.println("as;diasdfhasdfjkfuh");
      System.out.println("as;diasdfhasdfjkfuh");
      System.out.println("as;diasdfhasdfjkfuh");
      System.out.println("as;diasdfhasdfjkfuh");

    }
    else
    {
      double currentVelocity = Robot.elevator.getSelectedSensorVelocity(); 

      if(currentVelocity > velocity)
      {
        velocity = currentVelocity;
      }

      Robot.elevator.set(this.powerAxis.GetAxisValue());
      System.out.println(velocity);

      //System.out.println("power: " + this.powerAxis.GetAxisValue());

    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished()
  {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end()
  {
    Robot.elevator.set(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted()
  {
    end();
  }
}
