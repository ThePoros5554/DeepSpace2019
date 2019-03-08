// /*----------------------------------------------------------------------------*/
// /* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
// /* Open Source Software - may be modified and shared by FRC teams. The code   */
// /* must be accompanied by the FIRST BSD license file in the root directory of */
// /* the project.                                                               */
// /*----------------------------------------------------------------------------*/

// package frc.robot.commands;

// import edu.wpi.first.wpilibj.command.CommandGroup;
// import frc.robot.commands.cargo_intake.ActivateIntake;
// import frc.robot.commands.elevator.MoveElevator;
// import frc.robot.commands.lifter.CloseFrontLifters;
// import frc.robot.commands.lifter.LiftRobot;
// import frc.robot.commands.wrist.AdjustWrist;
// import frc.robot.subsystems.Wrist.WristMode;

// public class AutoLiftRobot extends CommandGroup {
//   /**
//    * Add your docs here.
//    */
//   public AutoLiftRobot(double elevatorDownPower)
//   {
//     addSequential(new AdjustWrist(WristMode.DOWN));
//     addParallel(new LiftRobot());
//     addSequential(new MoveElevator(elevatorDownPower));
//     addSequential(new ActivateIntake(1), 2);
//     addParallel(new CloseFrontLifters());
//   }
// }
