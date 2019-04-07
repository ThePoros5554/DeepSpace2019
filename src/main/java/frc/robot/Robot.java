/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.UpdateRobotState;
import frc.robot.commands.autoScripts.RampToCenterCargo;
import frc.robot.commands.autoScripts.Empty;
import frc.robot.commands.autoScripts.RampToLeftRocket;
import frc.robot.commands.autoScripts.RampToRightRocket;
import frc.robot.subsystems.CargoIntake;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.HatchLauncher;
import frc.robot.subsystems.Lifter;
import frc.robot.subsystems.Wrist;
import poroslib.sensors.SysPosition;
import poroslib.systems.Limelight;
import poroslib.systems.RobotProfile;
import poroslib.systems.Limelight.LimelightCamMode;
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
  private SendableChooser<String> autonomousChooser;
  public static CommandGroup selectedCommand;

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

  public static CameraThread rioCamThread;
  
  public enum RobotMode
  {
    CARGO, HATCH, CLIMB
  }

  public enum LifterMode
  {
    FORWARD, BACK
  }

  public static RobotMode mode = RobotMode.HATCH;
  public static RobotMode lastRobotMode = RobotMode.HATCH;
  public static LifterMode liftMode = LifterMode.FORWARD;

  public static boolean isHook = true;
  public static boolean teleopInit = false;

  private UpdateRobotState updateRobotState;

  private double gameStartTime = 0;
  public static double timeLeft = 135;

  public Robot()
  {
  }

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit()
  {
    RobotProfile.getRobotProfile().setWheelbaseWidth(0.75);
    RobotProfile.getRobotProfile().setWheelDiameter(0.1016);
    RobotProfile.getRobotProfile().setAutoKV(0.2222);
    //RobotProfile.getRobotProfile().setAutoKV(0);

    RobotProfile.getRobotProfile().setAutoKA(0);
    
    RobotProfile.getRobotProfile().setPathTimeStep(0.05);
    RobotProfile.getRobotProfile().setDriveEncTicksPerRevolution(4096);

    // subsystems
    this.masterLeft = new WPI_TalonSRX(Drivetrain.kFrontLeftPort);
    this.masterRight = new WPI_TalonSRX(Drivetrain.kFrontRightPort);
    drivetrain = new Drivetrain(this.masterLeft, this.masterRight);
    drivetrain.SetIsReversed(true);
    elevator = new Elevator();
    wrist = new Wrist();
    elevator.enableLimitSwitch(true);
    cargoIntake = new CargoIntake();
    lifter = new Lifter();

    // oi
    oi = new OI();

    // cameras
    lime = new Limelight();
    rioCamThread = new CameraThread();
    rioCamThread.setDaemon(true);
    rioCamThread.start();

    // autonomous chooser
    autonomousChooser = new SendableChooser<String>();
    autonomousChooser.addOption("Empty", RobotMap.EMPTY);
    autonomousChooser.addOption("Ramp To Left Rocket", RobotMap.RAMPTOLEFTROCKET);
    autonomousChooser.addOption("Ramp To Center Cargo", RobotMap.RAMPTOCENTERCARGO);
    autonomousChooser.addOption("Ramp To Right Rocket", RobotMap.RAMPTORIGHTROCKET);
    SmartDashboard.putData("Autonomous Chooser", autonomousChooser);
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
    lime.setPipeline(7);
    lime.setCamMode(LimelightCamMode.VisionProcessor);

   SmartDashboard.putNumber("Yaw", drivetrain.getHeading());
    // SmartDashboard.putNumber("Roll", drivetrain.getSideTipAngle());
    // SmartDashboard.putNumber("pitch", drivetrain.getForwardTipAngle());

  //  SmartDashboard.putNumber("leftEnc", drivetrain.getLeftPositionInCm());
  //  SmartDashboard.putNumber("rightEnc", drivetrain.getRightPositionInCm());

    // SmartDashboard.putNumber("x position: " , RobotMonitor.getRobotMonitor().getLastPositionReport().getValue().getTranslation().getX());
    // SmartDashboard.putNumber("y position: " , RobotMonitor.getRobotMonitor().getLastPositionReport().getValue().getTranslation().getY());
    // SmartDashboard.putNumber("degrees: " , RobotMonitor.getRobotMonitor().getLastPositionReport().getValue().getRotation().getDegrees());

    // SmartDashboard.putNumber("target x: " , RobotMonitor.getRobotMonitor().getLastVisionReport().getValue().getHorizontalDisplacement().getTranslation().getX());
    // SmartDashboard.putNumber("target y: " , RobotMonitor.getRobotMonitor().getLastVisionReport().getValue().getHorizontalDisplacement().getTranslation().getY());
    // SmartDashboard.putNumber("target offset: " ,  RobotMonitor.getRobotMonitor().getLastVisionReport().getValue().getHorizontalDisplacement().getRotation().getDegrees());

     SmartDashboard.putNumber("Elevator Position:", elevator.getCurrentPosition());

      SmartDashboard.putNumber("Wrist Position:", wrist.getCurrentPosition());

    // SmartDashboard.putNumber("Elevator sp", elevator.getTargetPosition());
    // SmartDashboard.putNumber("Wrist sp", wrist.getTargetPosition());

    SmartDashboard.putBoolean("Cargo Mode", Robot.mode == RobotMode.CARGO);
    SmartDashboard.putBoolean("Hatch Mode", Robot.mode == RobotMode.HATCH);

    SmartDashboard.putString("Robot Mode", Robot.mode.toString());

    // SmartDashboard.putBoolean("Elevator Limit", Robot.drivetrain.getIsElevatorLimit());
    // SmartDashboard.putNumber("Time Left:", timeLeft);

    SmartDashboard.putBoolean("FrontLeftSwitch:", (lifter.GetFrontLeftPos() == SysPosition.Top || lifter.GetFrontLeftPos() == SysPosition.Bottom|| lifter.GetFrontLeftPos() == SysPosition.Blocked));
    SmartDashboard.putBoolean("FrontRightSwitch:", (lifter.GetFrontRightPos() == SysPosition.Top || lifter.GetFrontRightPos() == SysPosition.Bottom || lifter.GetFrontRightPos() == SysPosition.Blocked));
    SmartDashboard.putBoolean("BackSwitch:", lifter.GetBackPos() == SysPosition.Bottom);

    SmartDashboard.putString("FrontLeftPos:", lifter.GetFrontLeftPos().toString());
    SmartDashboard.putString("FrontRightPos:", lifter.GetFrontRightPos().toString());

  }


  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit()
  {
    teleopInit = false;

    if(updateRobotState != null)
    {
      updateRobotState.cancel();
    }

    if(selectedCommand != null)
    {
      selectedCommand.cancel();
    }

    drivetrain.resetHeading();

    elevator.neutralOutput();

  }

  @Override
  public void disabledPeriodic()
  {
    lime.setLedMode(LimelightLedMode.ForceOff);
    // Scheduler.getInstance().run();
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
    drivetrain.configVoltageCompensation(12, true);

    if (updateRobotState == null)
    {
      updateRobotState = new UpdateRobotState();
    }

    elevator.setControlMode(ControlMode.PercentOutput);
    wrist.setControlMode(ControlMode.PercentOutput);

    updateRobotState.start();

    elevator.neutralOutput();
    wrist.neutralOutput();

    System.out.println(autonomousChooser.getSelected());
    selectedCommand = getSelectedAutonomous(autonomousChooser.getSelected());
    selectedCommand.start();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic()
  {
    lime.setLedMode(LimelightLedMode.ForceOn);
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit()
  {
    gameStartTime = Timer.getFPGATimestamp();
    teleopInit = true;

    drivetrain.resetHeading();
    drivetrain.resetRawPosition();
    drivetrain.configVoltageCompensation(12, false);


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
    timeLeft = 135 - (Timer.getFPGATimestamp() - gameStartTime);

    lime.setLedMode(LimelightLedMode.ForceOn);

    Scheduler.getInstance().run();

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic()
  {
  }

  public CommandGroup getSelectedAutonomous(String value)
  {
    if(value == null)
    {
      return new Empty();
    }

    switch(value)
    {
      case RobotMap.EMPTY:

        return new Empty();
        
      case RobotMap.RAMPTOLEFTROCKET:

        return new RampToLeftRocket();

      case RobotMap.RAMPTOCENTERCARGO:

        return new RampToCenterCargo();

      case RobotMap.RAMPTORIGHTROCKET:

        return new RampToRightRocket();

      default:

        return new Empty();
    }
  }
}
