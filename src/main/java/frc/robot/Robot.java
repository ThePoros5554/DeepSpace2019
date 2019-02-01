/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.CargoIntake;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.HatchLauncher;
import frc.robot.subsystems.Lifter;
import frc.robot.subsystems.Wrist;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot
{
  
  public static Drivetrain drivetrain;
  private WPI_TalonSRX masterLeft;
  private WPI_TalonSRX masterRight;

  public static Elevator elevator;
  public static Wrist wrist;
  public static CargoIntake cargoIntake;
  public static HatchLauncher hatchLauncher;
  public static Lifter lifter;

  private OI oi;
  
  public enum RobotMode
  {
    CARGO, HATCH, CLIMB
  }

  public static RobotMode mode = RobotMode.HATCH;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit()
  {
    this.masterLeft = new WPI_TalonSRX(Drivetrain.kFrontLeftPort);
    this.masterRight = new WPI_TalonSRX(Drivetrain.kFrontRightPort);
    drivetrain = new Drivetrain(this.masterLeft, this.masterRight);
    
    elevator = new Elevator();
    wrist = new Wrist();
    cargoIntake = new CargoIntake();
    hatchLauncher = new HatchLauncher();
    lifter = new Lifter();

    oi = new OI();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic()
  {
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit()
  {
  }

  @Override
  public void disabledPeriodic()
  {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit()
  {
    drivetrain.resetHeading();
    drivetrain.resetRawPosition();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic()
  {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit()
  {
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic()
  {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic()
  {
  }
}
