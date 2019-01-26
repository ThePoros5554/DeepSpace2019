/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.poroslib.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import frc.poroslib.subsystems.DiffDrivetrain;

public class GTADrive extends Command {
  private DiffDrivetrain drivetrain;
	private XboxController xbox;
	
  public GTADrive(DiffDrivetrain drivetrain, XboxController xbox)
  {
    this.drivetrain = drivetrain;
    this.xbox = xbox;
    requires(this.drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize()
  {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute()
  {
    double forwardValue = this.xbox.getTriggerAxis(Hand.kRight);
    double reverseValue =  this.xbox.getTriggerAxis(Hand.kLeft);
    double turnValue = this.xbox.getX(Hand.kLeft);
    
    // turn in place
    if (this.xbox.getAButton())
    {
      this.drivetrain.arcadeDrive(0, turnValue, 1);
    }
    else
    {
      this.drivetrain.GTADrive(forwardValue, reverseValue, turnValue, 1.5);
    }
  }

  @Override
  protected boolean isFinished()
  {
      return false;
  }

  @Override
  protected void end()
  {
    this.drivetrain.GTADrive(0, 0, 0, 1);
  }

  @Override
  protected void interrupted()
  {
    end();
  }
}
