/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.Robot.RobotMode;
import frc.robot.commands.InitHatchCollectMode;
import frc.robot.commands.AutoLiftRobot;
import frc.robot.commands.CancelAuto;
import frc.robot.commands.DriveStraight;
import frc.robot.commands.InitCargoCollectFeederMode;
import frc.robot.commands.InitCargoCollectMode;
import frc.robot.commands.InitHatchLowMode;
import frc.robot.commands.InitCargoLowMode;
import frc.robot.commands.InitHatchMiddleMode;
import frc.robot.commands.InitLiftMode;
import frc.robot.commands.RumbleInTime;
import frc.robot.commands.ToggleClimbMode;
import frc.robot.commands.InitCargoMiddleMode;
import frc.robot.commands.InitCargoShipMode;
import frc.robot.commands.InitHatchHighMode;
import frc.robot.commands.InitCargoHighMode;
import frc.robot.commands.ToggleGamepieceMode;
import frc.robot.commands.VisionAlignment;
import frc.robot.commands.cargo_intake.ActivateIntake;
import frc.robot.commands.elevator.AdjustElevator;
import frc.robot.commands.elevator.MoveElevator;
import frc.robot.commands.lifter.ChangeClimbSpeed;
import frc.robot.commands.lifter.LiftBack;
import frc.robot.commands.lifter.LiftFront;
import frc.robot.commands.lifter.LiftFrontLeft;
import frc.robot.commands.lifter.LiftFrontRight;
import frc.robot.commands.lifter.LiftRobot;
import frc.robot.commands.lifter.ResetConstBack;
import frc.robot.commands.lifter.ResetConstPower;
import frc.robot.commands.lifter.RollWheels;
import frc.robot.commands.wrist.MoveWrist;
import frc.robot.commands.wrist.WristDownStart;
import frc.robot.subsystems.CargoIntake;
import frc.robot.subsystems.Lifter;
import frc.robot.triggers.ExceptionModeJoyAxis;
import frc.robot.triggers.ExceptionModeJoyAxisPart;
import frc.robot.triggers.ModeJoyAxis;
import frc.robot.triggers.ModeJoyAxisPart;
import frc.robot.triggers.ModeTrigger;
import poroslib.commands.CurvatureDrive;
import poroslib.commands.RumbleJoystick;
import poroslib.triggers.POVTrigger;
import poroslib.triggers.SmartJoystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI
{
    // Button Ports
    private static final int kDriveStraightButton = 2; // SIDE BUTTON
    private static final int kCollectModeButton = 2; // B
    private static final int kLowModeButton = 1; // A
    private static final int kMiddleModeButton = 3; // X
    private static final int kHighModeButton = 4; // Y
    private static final int kRobotModeButton = 10; // RIGHT AXIS BUTTON
    private static final int kEjectPartButton = 5; // LB
    private static final int kCollectPartButton = 6; // RB
    private static final int kClimbModeButton = 9; // BACK
    private static final int kElevatorUpAxis = 3; // RT
    private static final int kElevatorDownAxis = 2; // LT
    private static final int kWristAxis = 1; // L 
    private static final int kRobotLiftModeButton = 0;
    private static final int kCargoShipButton = 8;
    private static final int kCollectCargoFeederModeButton = 7;

    private static final int kFrontLeftFwd = 3;
    private static final int kFrontLeftRv = 4;
    private static final int kFrontRightFwd = 1;
    private static final int kFrontRightRv = 2;
    private static final int kLiftRobotFwd = 5;
    private static final int kLiftRobotRv = 6;
    private static final int kLiftBack = 1;
    private static final int kLiftFront = 5;
    private static final int kRollPovFwd = 0;
    private static final int kRollPovRv = 180;
    private static final int kResetConstPower = 8;
    private static final int kResetConstBack = 7;
    private static final int kSlowSpeedClimb = 2;


    private static final int kMoveToVisionTargetButton = 1; // DRIVER TRIGGER

    // Joystick Ports
    // private static final int kDriverLeftJoystickPort = 0;
    // private static final int kDriverRightJoystickPort = 1;
    private static final int kDriverJoyPort = 0;
    private static final int kOperatorJoystickPort = 1; // change to 2 if using logitech joysticks again
    
    //

    // private SmartJoystick leftJoy;
    // private SmartJoystick rightJoy;
    private SmartJoystick driverJoy;
    private SmartJoystick operatorJoy;

    private Button modeButton;
    private Button driveStraightButton;
    private Button climbModeButton;
    private Button moveToVisionTarget;
    private Button cancelAuto;

    private ModeTrigger prepareHatchCollectTrigger;
    private ModeTrigger prepareCargoCollectTrigger;
    private ModeTrigger prepareCargoShipTrigger;
    private ModeTrigger prepareCargoCollectFeederTrigger;
    private ModeTrigger prepareHatchLowTrigger;
    private ModeTrigger prepareCargoLowTrigger;
    private ModeTrigger prepareHatchMiddleTrigger;
    private ModeTrigger prepareCargoMiddleTrigger;
    private ModeTrigger prepareHatchHighTrigger;
    private ModeTrigger prepareCargoHighTrigger;
    private ModeTrigger collectHatchTrigger;
    private ModeTrigger collectCargoTrigger;
    private ModeTrigger ejectHatchTrigger;
    private ModeTrigger ejectCargoTrigger;

    private ModeTrigger fwdLiftFrontLeftBtn;
    private ModeTrigger rvLiftFrontLeftBtn;
    private ModeTrigger fwdLiftFrontRightBtn;
    private ModeTrigger rvLiftFrontRightBtn;
    private ModeJoyAxisPart fwdLiftBackAxis;
    private ModeJoyAxisPart rvLiftBackAxis;
    private ModeTrigger fwdLiftRobotBtn;
    private ModeTrigger rvLiftRobotBtn;
    private POVTrigger rollFwdBtn;
    private POVTrigger rollRvBtn;
    private ModeTrigger resetConstPowerBtn;
    private ModeTrigger resetConstBackBtn;
    private ModeJoyAxisPart fwdLiftFrontAxis;
    private ModeJoyAxisPart rvLiftFrontAxis;
    private ModeJoyAxis slowSpeedClimbAxis;
    
    private ExceptionModeJoyAxisPart wristDownAxis;
    private ExceptionModeJoyAxisPart wristUpAxis;
    private ExceptionModeJoyAxis elevatorDownAxis;
    private ExceptionModeJoyAxis elevatorUpAxis;

    private AutoLiftRobot autoClimb;
    private LiftFrontLeft fwdLiftFrontLeft;
    private LiftFrontRight fwdLiftFrontRight;
    private LiftBack fwdLiftBack;
    private LiftFront fwdLiftFront;
    private LiftRobot fwdLiftRobot;
    private LiftFrontLeft rvLiftFrontLeft;
    private LiftFrontRight rvLiftFrontRight;
    private LiftBack rvLiftBack;
    private LiftFront rvLiftFront;
    private LiftRobot rvLiftRobot;
    private RollWheels rollFwd;
    private RollWheels rollRv;
    private ResetConstPower resetLiftConstPower;
    private ResetConstBack resetLiftConstBack;
    private ChangeClimbSpeed climbSlowSpeed;
    private ChangeClimbSpeed climbFwdNormalSpeed;

    //private TankDrive defaultDrive;
    private RumbleInTime rumbleAt10;
    private CurvatureDrive poroDrive;
    private DriveStraight driveStraight;
    private InitCargoCollectFeederMode prepareCargoCollectFeeder;
    private AdjustElevator collectHatch;
    private AdjustElevator ejectHatch;
    private ActivateIntake collectCargo;
    private ActivateIntake ejectCargo;
    private InitHatchCollectMode prepareHatchCollect;
    private InitHatchLowMode prepareHatchLow;
    private InitHatchMiddleMode prepareHatchMiddle;
    private InitHatchHighMode prepareHatchHigh;
    private InitCargoCollectMode prepareCargoCollect;
    private InitCargoLowMode prepareCargoLow;
    private InitCargoMiddleMode prepareCargoMiddle;
    private InitCargoHighMode prepareCargoHigh;
    private InitCargoShipMode prepareCargoShip;
    private InitLiftMode prepareLift;
    private MoveWrist wristDown;
    private MoveWrist wristUp;
    private MoveElevator elevatorUp;
    private MoveElevator elevatorDown;

    private VisionAlignment visionAllignment;
    private ToggleGamepieceMode toggleGamepiece;

    public OI()
    {  
        /************** Initialize **************/

        // joysticks
        //leftJoy = new SmartJoystick(kDriverLeftJoystickPort);
        //rightJoy = new SmartJoystick(kDriverRightJoystickPort);
        //leftJoy.SetSpeedAxis(1);
        //rightJoy.SetSpeedAxis(1);
        driverJoy = new SmartJoystick(kDriverJoyPort);
        operatorJoy = new SmartJoystick(kOperatorJoystickPort);
        driverJoy.SetSpeedAxis(1);
        driverJoy.SetRotateAxis(4);
        Robot.drivetrain.SetIsRanged(true);

        // buttons and triggers
        cancelAuto = new JoystickButton(driverJoy, 7);

        //rumbleAt10 = new RumbleInTime(driverJoy, 0.65, 2, 12);
        prepareHatchCollectTrigger = new ModeTrigger(operatorJoy, kCollectModeButton, RobotMode.HATCH);
        prepareHatchLowTrigger = new ModeTrigger(operatorJoy, kLowModeButton, RobotMode.HATCH);
        prepareHatchMiddleTrigger = new ModeTrigger(operatorJoy, kMiddleModeButton, RobotMode.HATCH);
        prepareHatchHighTrigger = new ModeTrigger(operatorJoy, kHighModeButton, RobotMode.HATCH);
        collectHatchTrigger = new ModeTrigger(operatorJoy, kCollectPartButton, RobotMode.HATCH);
        ejectHatchTrigger = new ModeTrigger(operatorJoy, kEjectPartButton, RobotMode.HATCH);

        prepareCargoCollectTrigger = new ModeTrigger(operatorJoy, kCollectModeButton, RobotMode.CARGO);
        prepareCargoCollectFeederTrigger = new ModeTrigger(operatorJoy, kCollectCargoFeederModeButton, RobotMode.CARGO);
        prepareCargoLowTrigger = new ModeTrigger(operatorJoy, kLowModeButton, RobotMode.CARGO);
        prepareCargoMiddleTrigger = new ModeTrigger(operatorJoy, kMiddleModeButton, RobotMode.CARGO);
        prepareCargoHighTrigger = new ModeTrigger(operatorJoy, kHighModeButton, RobotMode.CARGO);
        prepareCargoShipTrigger = new ModeTrigger(operatorJoy, kCargoShipButton, RobotMode.CARGO);
        collectCargoTrigger = new ModeTrigger(operatorJoy, kCollectPartButton, RobotMode.CARGO);
        ejectCargoTrigger = new ModeTrigger(operatorJoy, kEjectPartButton, RobotMode.CARGO);

        fwdLiftFrontLeftBtn = new ModeTrigger(operatorJoy, kFrontLeftFwd, RobotMode.CLIMB);
        rvLiftFrontLeftBtn = new ModeTrigger(operatorJoy, kFrontLeftRv, RobotMode.CLIMB);
        fwdLiftFrontRightBtn = new ModeTrigger(operatorJoy, kFrontRightFwd, RobotMode.CLIMB);
        rvLiftFrontRightBtn = new ModeTrigger(operatorJoy, kFrontRightRv, RobotMode.CLIMB);
        fwdLiftRobotBtn = new ModeTrigger(operatorJoy, kLiftRobotFwd, RobotMode.CLIMB);
        rvLiftRobotBtn = new ModeTrigger(operatorJoy, kLiftRobotRv, RobotMode.CLIMB);
        fwdLiftBackAxis = new ModeJoyAxisPart(operatorJoy, kLiftBack, -1, 1, 1, -1, -1, -0.2, RobotMode.CLIMB);
        rvLiftBackAxis = new ModeJoyAxisPart(operatorJoy, kLiftBack, -1, 1, 1, -1, 0.2, 1, RobotMode.CLIMB);
        fwdLiftFrontAxis = new ModeJoyAxisPart(operatorJoy, kLiftFront, -1, 1, 1, -1, -1, -0.2, RobotMode.CLIMB);
        rvLiftFrontAxis = new ModeJoyAxisPart(operatorJoy, kLiftFront, -1, 1, 1, -1, 0.2, 1, RobotMode.CLIMB);

        rollFwdBtn = new POVTrigger(operatorJoy, kRollPovFwd);
        rollRvBtn = new POVTrigger(operatorJoy, kRollPovRv);

        slowSpeedClimbAxis = new ModeJoyAxis(operatorJoy, kSlowSpeedClimb, -1, 0, -1, 0, RobotMode.CLIMB);

        resetConstPowerBtn = new ModeTrigger(operatorJoy, kResetConstPower, RobotMode.CLIMB);
        resetConstBackBtn = new ModeTrigger(operatorJoy, kResetConstBack, RobotMode.CLIMB);

        modeButton = new JoystickButton(operatorJoy, kRobotModeButton);
        climbModeButton = new JoystickButton(operatorJoy, kClimbModeButton);
        moveToVisionTarget = new JoystickButton(driverJoy, kMoveToVisionTargetButton);
        // driveStraightButton = new JoystickButton(rightJoy, kDriveStraightButton);

        // axis
        elevatorUpAxis = new ExceptionModeJoyAxis(operatorJoy, kElevatorDownAxis, 0, 1, -1, 0, RobotMode.CLIMB);
        elevatorDownAxis = new ExceptionModeJoyAxis(operatorJoy, kElevatorUpAxis, 0, -1, -1, 0, RobotMode.CLIMB);
        wristUpAxis = new ExceptionModeJoyAxisPart(operatorJoy, kWristAxis, -1, 1, 1, -1, 0.2, 1, RobotMode.CLIMB);
        wristDownAxis = new ExceptionModeJoyAxisPart(operatorJoy, kWristAxis, -1, 1, 1, -1, -1, -0.2, RobotMode.CLIMB);

        // commands
        poroDrive = new CurvatureDrive(Robot.drivetrain, driverJoy, 0.6, 1, 0.3);
        // defaultDrive = new TankDrive(Robot.drivetrain, leftJoy, rightJoy);
        // driveStraight = new DriveStraight(leftJoy, rightJoy);

        prepareHatchCollect = new InitHatchCollectMode();
        prepareHatchLow = new InitHatchLowMode();
        prepareHatchMiddle = new InitHatchMiddleMode();
        prepareHatchHigh = new InitHatchHighMode();
        collectHatch = new AdjustElevator(-4500);
        ejectHatch = new AdjustElevator(4500);

        prepareCargoCollect = new InitCargoCollectMode();
        prepareCargoCollectFeeder = new InitCargoCollectFeederMode();
        prepareCargoLow = new InitCargoLowMode();
        prepareCargoMiddle = new InitCargoMiddleMode();
        prepareCargoHigh = new InitCargoHighMode();
        prepareCargoShip = new InitCargoShipMode();
        collectCargo = new ActivateIntake(CargoIntake.kIntakeInPower);
        ejectCargo = new ActivateIntake(CargoIntake.kIntakeOutPower);

        fwdLiftFrontLeft = new LiftFrontLeft(0.3);
        fwdLiftFrontRight = new LiftFrontRight(0.3);
        fwdLiftFront = new LiftFront(Lifter.rvClimbSpeed);
        fwdLiftBack = new LiftBack(Lifter.rvClimbSpeed);
        fwdLiftRobot = new LiftRobot(Lifter.fwdClimbSpeed);

        rvLiftFrontLeft = new LiftFrontLeft(-0.3);
        rvLiftFrontRight = new LiftFrontRight(-0.3);
        rvLiftFront = new LiftFront(Lifter.fwdClimbSpeed);
        rvLiftBack = new LiftBack(Lifter.fwdClimbSpeed);
        rvLiftRobot = new LiftRobot(Lifter.rvClimbSpeed);

        rollFwd = new RollWheels(Lifter.wheelFwdSpeed);
        rollRv = new RollWheels(Lifter.wheelRvSpeed);
        
        climbSlowSpeed = new ChangeClimbSpeed(Lifter.slowSpeedClimb);
        climbFwdNormalSpeed = new ChangeClimbSpeed(Lifter.normalSpeedClimb);

        resetLiftConstPower = new ResetConstPower();
        resetLiftConstBack = new ResetConstBack();

        autoClimb = new AutoLiftRobot();

        elevatorDown = new MoveElevator(elevatorDownAxis);
        elevatorUp = new MoveElevator(elevatorUpAxis);
        wristDown = new MoveWrist(wristDownAxis);
        wristUp = new MoveWrist(wristUpAxis);

        toggleGamepiece = new ToggleGamepieceMode();
        prepareLift = new InitLiftMode();
        // visionAllignment = new VisionAlignment(leftJoy, rightJoy, true);
        visionAllignment = new VisionAlignment(driverJoy, true);

        /****************************************/

        Robot.drivetrain.setDefaultCommand(poroDrive);
        // Robot.drivetrain.setDefaultCommand(defaultDrive);
        modeButton.whenPressed(toggleGamepiece);
        climbModeButton.whenPressed(prepareLift);
        
        // climb
        fwdLiftFrontLeftBtn.whileActive(fwdLiftFrontLeft);
        rvLiftFrontLeftBtn.whileActive(rvLiftFrontLeft);
        fwdLiftFrontRightBtn.whileActive(fwdLiftFrontRight);
        rvLiftFrontRightBtn.whileActive(rvLiftFrontRight);
        fwdLiftRobotBtn.whileActive(fwdLiftRobot);
        // fwdLiftRobotBtn.whenActive(autoClimb);
        rvLiftRobotBtn.whileActive(rvLiftRobot);
        fwdLiftBackAxis.whileActive(fwdLiftBack);
        rvLiftBackAxis.whileActive(rvLiftBack);
        fwdLiftFrontAxis.whileActive(fwdLiftFront);
        rvLiftFrontAxis.whileActive(rvLiftFront);

        rollFwdBtn.whileActive(rollFwd);
        rollRvBtn.whileActive(rollRv);

        resetConstPowerBtn.whenActive(resetLiftConstPower);
        resetConstBackBtn.whenActive(resetLiftConstBack);

        
        slowSpeedClimbAxis.whenActive(climbSlowSpeed);
        slowSpeedClimbAxis.whenInactive(climbFwdNormalSpeed);

        // hatch
        prepareHatchCollectTrigger.whenActive(prepareHatchCollect);
        prepareHatchLowTrigger.whenActive(prepareHatchLow);
        prepareHatchMiddleTrigger.whenActive(prepareHatchMiddle);
        prepareHatchHighTrigger.whenActive(prepareHatchHigh);
        collectHatchTrigger.whenActive(collectHatch);
        ejectHatchTrigger.whenActive(ejectHatch);

        // cargo
        prepareCargoCollectTrigger.whenActive(prepareCargoCollect);
        prepareCargoLowTrigger.whenActive(prepareCargoLow);
        prepareCargoMiddleTrigger.whenActive(prepareCargoMiddle);
        prepareCargoHighTrigger.whenActive(prepareCargoHigh);
        collectCargoTrigger.whileActive(collectCargo);
        prepareCargoCollectFeederTrigger.whenActive(prepareCargoCollectFeeder);
        prepareCargoShipTrigger.whenActive(prepareCargoShip);
        ejectCargoTrigger.whileActive(new RumbleJoystick(operatorJoy, 0.6, false));
        ejectCargoTrigger.whileActive(ejectCargo);

        // manual
        cancelAuto.whenPressed(new CancelAuto());
        elevatorUpAxis.whileActive(elevatorUp);
        elevatorDownAxis.whileActive(elevatorDown);
        wristDownAxis.whileActive(wristDown);
        wristUpAxis.whileActive(wristUp);
        // driveStraightButton.whileActive(driveStraight);
        moveToVisionTarget.whileActive(visionAllignment);
    }
}