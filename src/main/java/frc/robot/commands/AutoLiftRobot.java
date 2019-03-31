/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.drive.MagicDrive;
import frc.robot.commands.lifter.ChangeClimbSpeed;
import frc.robot.commands.lifter.LiftBack;
import frc.robot.commands.lifter.LiftFront;
import frc.robot.commands.lifter.LiftRobot;
import frc.robot.commands.lifter.RollWheels;
import frc.robot.commands.lifter.WaitForSwitches;
import frc.robot.subsystems.Lifter;
import poroslib.commands.auto.Timeout;

public class AutoLiftRobot extends CommandGroup {
  /**
   * Add your docs here.
   */
  public AutoLiftRobot(double elevatorDownPower)
  {

    // climb up 
    addParallel(new LiftRobot(Lifter.fwdClimbSpeed));
    addSequential(new ChangeClimbSpeed(Lifter.slowSpeedClimb));
    addSequential(new Timeout(1.5));
    addSequential(new ChangeClimbSpeed(Lifter.normalSpeedClimb));
    addSequential(new Timeout(2.2));
    addSequential(new ChangeClimbSpeed(Lifter.slowSpeedClimb));
    
    // wait for all switches to be pressed
    addSequential(new WaitForSwitches(false));
    
    // go forward
    addSequential(new RollWheels(Lifter.wheelFwdSpeed));
    addSequential(new Timeout(1.3));

    // retract front lifters
    addParallel(new LiftFront(Lifter.rvClimbSpeed));
    addSequential(new ChangeClimbSpeed(Lifter.slowSpeedClimb), 1);
    addSequential(new ChangeClimbSpeed(Lifter.normalSpeedClimb));
    
    // wait for front lifters switches to be pressed
    addSequential(new WaitForSwitches(true));

    // drive into the ramp
    addSequential(new MagicDrive(1000, 1000, false)); // gotta tune and find values

    // lift back
    addParallel(new LiftBack(Lifter.rvClimbSpeed)); // gotta tune and find values
    addSequential(new ChangeClimbSpeed(Lifter.slowSpeedClimb), 0.5);
    addSequential(new ChangeClimbSpeed(Lifter.normalSpeedClimb), 1);
    addParallel(new ChangeClimbSpeed(Lifter.slowSpeedClimb), 0.7);

    // drive into the ramp fully
    addSequential(new MagicDrive(100, 100, false)); // gotta tune and find values
  }
}
