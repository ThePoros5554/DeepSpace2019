/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autoScripts;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.VisionAlignment;
import frc.robot.commands.drive.FollowPath;
import frc.robot.commands.drive.ResetNavx;
import frc.robot.commands.elevator.AdjustElevator;
import frc.robot.commands.wrist.WristDownStart;
import poroslib.util.Path;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterRampToRightCenterCargo extends CommandGroup {
  /**
   * Add your docs here.
   */
  public CenterRampToRightCenterCargo() {
    Path rr1lp = new Path(RobotMap.CENTERRAMPTORIGHTCENTERCARGO + ".left");
    Path rr1rp = new Path(RobotMap.CENTERRAMPTORIGHTCENTERCARGO + ".right");

    addParallel(new WristDownStart(2));
    addParallel(new AdjustElevator(5100 ,2.5));
    addSequential(new FollowPath(Robot.drivetrain, rr1rp, rr1lp, 0.001, 0.08, 0.005, 0.05, false));
    addSequential(new VisionAlignment(0.3, false, 1.5, 0.012));
    addParallel(new AdjustElevator(-4000));

    // Add Commands here:
    // e.g. addSequential(new Command1());
    // addSequential(new Command2());
    // these will run in order.

    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
  }
}
