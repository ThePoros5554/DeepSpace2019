/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.IMUProtocol.GyroUpdate;

import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.UpdateRobotState;
import frc.robot.subsystems.CargoIntake;
import frc.robot.subsystems.Drivetrain;
//import frc.robot.subsystems.SendableForShuffleBoard;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.HatchLauncher;
import frc.robot.subsystems.Lifter;
import frc.robot.subsystems.Wrist;
import poroslib.systems.Limelight;
import poroslib.systems.Limelight.LimelightCamMode;
//import sun.jvm.hotspot.runtime.solaris_sparc.SolarisSPARCJavaThreadPDAccess;
import poroslib.systems.Limelight.LimelightLedMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot
{
  private SendableChooser<String> autonomouChooser;

  public static Drivetrain drivetrain;
  private WPI_TalonSRX masterLeft;
  private WPI_TalonSRX masterRight;

  public static Elevator elevator;
  public static Wrist wrist;
  public static CargoIntake cargoIntake;
  public static HatchLauncher hatchLauncher;
  public static Lifter lifter;
  public static Limelight lime;

  private OI oi;
  
  public enum RobotMode
  {
    CARGO, HATCH, CLIMB
  }

  public static RobotMode mode = RobotMode.HATCH;

  private UpdateRobotState updateRobotState;

  private double gameStartTime;

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
    elevator.enableLimitSwitch(true);
    cargoIntake = new CargoIntake();
    // hatchLauncher = new HatchLauncher();
    // lifter = new Lifter();

    oi = new OI();

    lime = new Limelight();

    autonomouChooser = new SendableChooser<String>();
    autonomouChooser.addOption("LeftRocketHatch", RobotMap.LEFTROCKETHATCH);    
    autonomouChooser.addOption("RightRocketHatch", RobotMap.RIGHTROCKETHATCH);
    autonomouChooser.addOption("LeftCargoHatch", RobotMap.LEFTSHIPHATCH);
    autonomouChooser.addOption("RightShipHatch", RobotMap.RIGHTSHIPHATCH);
    autonomouChooser.addOption("LeftShipCargo", RobotMap.LEFTSHIPCARGO);
    autonomouChooser.addOption("RightShipCargo", RobotMap.RIGHTSHIPCARGO);
    autonomouChooser.addOption("RightRocketCargo", RobotMap.RIGHTROCKETCARGO);
    autonomouChooser.addOption("LeftRocketCargo", RobotMap.LEFTROCKETCARGO);

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
    //
    // 
    lime.setPipeline(7);
    lime.setCamMode(LimelightCamMode.VisionProcessor);
    // SmartDashboard.putNumber("Yaw", drivetrain.getHeading());
    // SmartDashboard.putNumber("Roll", drivetrain.getSideTipAngle());
    // SmartDashboard.putNumber("Pitch", drivetrain.getForwardTipAngle());

    // SmartDashboard.putNumber("leftEnc", drivetrain.getRawLeftPosition());
    // SmartDashboard.putNumber("rightEnc", drivetrain.getRawRightPosition());

    // SmartDashboard.putNumber("x position: " , RobotMonitor.getRobotMonitor().getLastPositionReport().getValue().getTranslation().getX());
    // SmartDashboard.putNumber("y position: " , RobotMonitor.getRobotMonitor().getLastPositionReport().getValue().getTranslation().getY());
    // SmartDashboard.putNumber("degrees: " , RobotMonitor.getRobotMonitor().getLastPositionReport().getValue().getRotation().getDegrees());


    SmartDashboard.putNumber("target x: " , RobotMonitor.getRobotMonitor().getLastVisionReport().getValue().getHorizontalDisplacement().getTranslation().getX());
    SmartDashboard.putNumber("target y: " , RobotMonitor.getRobotMonitor().getLastVisionReport().getValue().getHorizontalDisplacement().getTranslation().getY());
    SmartDashboard.putNumber("target offset: " ,  RobotMonitor.getRobotMonitor().getLastVisionReport().getValue().getHorizontalDisplacement().getRotation().getDegrees());

    SmartDashboard.putNumber("Elevator Position", elevator.getCurrentPosition());

    SmartDashboard.putNumber("Wrist Position", wrist.getCurrentPosition());

    SmartDashboard.putNumber("Ele sp", elevator.getTargetPosition());
    SmartDashboard.putNumber("Wrist sp", wrist.getTargetPosition());


    // SmartDashboard.putBoolean("Cargo Mode", Robot.mode == RobotMode.CARGO);

    // SmartDashboard.putBoolean("Hatch Mode", Robot.mode == RobotMode.HATCH);
    // //Shuffleboard.getTab("SmartDashboard").add("Gyro", SmartDashboard.putData(drivetrain.getHeading()).withWidget(BuiltInWidgets.kGyro);

    SmartDashboard.putBoolean("ElevatorLimit", Robot.drivetrain.getIsElevatorLimit());


  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit()
  {
    if(updateRobotState != null)
    {
      updateRobotState.cancel();
    }

    drivetrain.resetHeading();

    elevator.neutralOutput();

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
    lime.setLedMode(LimelightLedMode.ForceOn);

    drivetrain.resetHeading();
    drivetrain.resetRawPosition();

    if(updateRobotState == null)
    {
      updateRobotState = new UpdateRobotState();
    }

    elevator.setControlMode(ControlMode.PercentOutput);
    wrist.setTargetPosition(wrist.getCurrentPosition());

    updateRobotState.start();

    elevator.neutralOutput();
    wrist.neutralOutput();
    

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
    lime.setLedMode(LimelightLedMode.ForceOn);

    gameStartTime = Timer.getFPGATimestamp();

    drivetrain.resetHeading();
    drivetrain.resetRawPosition();


    if(updateRobotState == null)
    {
      updateRobotState = new UpdateRobotState();
    }
    
    updateRobotState.start();
    RobotMonitor.getRobotMonitor().resetMonitor();

    elevator.neutralOutput();
    wrist.neutralOutput();
    elevator.setControlMode(ControlMode.PercentOutput);
    wrist.setControlMode(ControlMode.PercentOutput);
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic()
  {

    Scheduler.getInstance().run();


    double currenttime = Timer.getFPGATimestamp() - gameStartTime;


  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic()
  {
  }
}
