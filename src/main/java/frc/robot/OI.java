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
import frc.robot.commands.DriveStraight;
import frc.robot.commands.InitCargoCollectMode;
import frc.robot.commands.InitHatchLowMode;
import frc.robot.commands.InitCargoLowMode;
import frc.robot.commands.InitHatchMiddleMode;
import frc.robot.commands.InitLiftMode;
import frc.robot.commands.ToggleClimbMode;
import frc.robot.commands.InitCargoMiddleMode;
import frc.robot.commands.InitHatchHighMode;
import frc.robot.commands.InitHatchHookCollectMode;
import frc.robot.commands.InitHatchHookHighMode;
import frc.robot.commands.InitHatchHookLowMode;
import frc.robot.commands.InitHatchHookMiddleMode;
import frc.robot.commands.InitCargoHighMode;
import frc.robot.commands.ToggleGamepieceMode;
import frc.robot.commands.ToggleHookMode;
import frc.robot.commands.VisionAlignment;
import frc.robot.commands.cargo_intake.ActivateIntake;
import frc.robot.commands.elevator.MoveElevator;
import frc.robot.commands.hatch_launcher.EjectHatch;
import frc.robot.commands.lifter.CloseFrontLifters;
import frc.robot.commands.lifter.CloseRearLifters;
import frc.robot.commands.lifter.LiftRobot;
import frc.robot.commands.wrist.MoveWrist;
import frc.robot.commands.wrist.WristDownStart;
import frc.robot.subsystems.CargoIntake;
import frc.robot.triggers.HatchModeTrigger;
import frc.robot.triggers.ModeTrigger;
import poroslib.commands.CurvatureDrive;
import poroslib.commands.GTADrive;
import poroslib.commands.RumbleJoystick;
import poroslib.commands.TankDrive;
import poroslib.triggers.JoyAxis;
import poroslib.triggers.JoyAxisPart;
import poroslib.triggers.SmartJoystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI
{
    // Button Ports
    private static final int kDriveStraightButton = 2; // TODO: check if right
    private static final int kCollectModeButton = 2; // B
    private static final int kLowModeButton = 1; // A
    private static final int kMiddleModeButton = 3; // X
    private static final int kHighModeButton = 4; // Y
    private static final int kRobotModeButton = 10; // RIGHT AXIS BUTTON
    private static final int kEjectPartButton = 5; // LB
    private static final int kCollectPartButton = 6; // RB
    private static final int kClimbModeButton = 7; // BACK
    private static final int kHatchModeButton = 9; // LEFT AXIS BUTTON
    private static final int kLiftCloseRearButton = 0;
    private static final int kLiftCloseFrontButton = 0;
    private static final int kElevatorUpAxis = 3; // RT
    private static final int kElevatorDownAxis = 2; // LT
    private static final int kWristAxis = 1; // L 
    private static final int kRobotLiftModeButton = 0;
    private static final int kStartButton = 8;

    private static final int kMoveToVisionTargetButton = 1; // DRIVER TRIGGER

    // Joystick Ports
    private static final int kDriverLeftJoystickPort = 0;
    private static final int kDriverRightJoystickPort = 1;
    private static final int kOperatorJoystickPort = 2;
    
    //

    private SmartJoystick leftJoy;
    private SmartJoystick rightJoy;
    private SmartJoystick operatorJoy;

    private Button modeButton;
    private Button driveStraightButton;
    private Button climbModeButton;
    private Button prepareLiftButton;
    private Button moveToVisionTarget;
    private Button startButton;
    private ModeTrigger hookToggleButton;

    private HatchModeTrigger prepareHatchCollectTrigger;
    private HatchModeTrigger prepareHatchHookCollectTrigger;
    private ModeTrigger prepareCargoCollectTrigger;
    private HatchModeTrigger prepareHatchLowTrigger;
    private HatchModeTrigger prepareHatchHookLowTrigger;
    private ModeTrigger prepareCargoLowTrigger;
    private HatchModeTrigger prepareHatchMiddleTrigger;
    private HatchModeTrigger prepareHatchHookMiddleTrigger;
    private ModeTrigger prepareCargoMiddleTrigger;
    private HatchModeTrigger prepareHatchHighTrigger;
    private HatchModeTrigger prepareHatchHookHighTrigger;
    private ModeTrigger prepareCargoHighTrigger;
    private HatchModeTrigger collectHatchTrigger;
    private HatchModeTrigger collectHatchHookTrigger;
    private ModeTrigger collectCargoTrigger;
    private HatchModeTrigger ejectHatchTrigger;
    private HatchModeTrigger ejectHatchHookTrigger;
    private ModeTrigger ejectCargoTrigger;
    private ModeTrigger liftRobotTrigger;
    private ModeTrigger liftCloseFrontTrigger;
    private ModeTrigger liftCloseRearTrigger;
    
    private JoyAxis wristDownAxis;
    private JoyAxis wristUpAxis;
    private JoyAxis elevatorDownAxis;
    private JoyAxis elevatorUpAxis;

    //private TankDrive defaultDrive;
    private CurvatureDrive gtaDrive;
    private DriveStraight driveStraight;
    private EjectHatch ejectHatch;
    private ActivateIntake collectCargo;
    private ActivateIntake ejectCargo;
    private InitHatchHookCollectMode prepareHatchHookCollect;
    private InitHatchHookLowMode prepareHatchHookLow;
    private InitHatchHookMiddleMode prepareHatchHookMiddle;
    private InitHatchHookHighMode prepareHatchHookHigh;
    private InitHatchCollectMode prepareHatchCollect;
    private InitHatchLowMode prepareHatchLow;
    private InitHatchMiddleMode prepareHatchMiddle;
    private InitHatchHighMode prepareHatchHigh;
    private InitCargoCollectMode prepareCargoCollect;
    private InitCargoLowMode prepareCargoLow;
    private InitCargoMiddleMode prepareCargoMiddle;
    private InitCargoHighMode prepareCargoHigh;
    private LiftRobot liftRobot;
    private InitLiftMode prepareLift;
    private CloseFrontLifters closeFrontLifters;
    private CloseRearLifters closeRearLifters;
    private MoveWrist wristDown;
    private MoveWrist wristUp;
    private MoveElevator elevatorUp;
    private MoveElevator elevatorDown;
    private WristDownStart wristDownStart;

    private VisionAlignment visionAllignment;

    private ToggleGamepieceMode toggleGamepiece;
    private ToggleHookMode toggleHook;
    private ToggleClimbMode toggleClimb;

    public OI()
    {  
        /************** Initialize **************/

        // joysticks
        leftJoy = new SmartJoystick(kDriverLeftJoystickPort);
        rightJoy = new SmartJoystick(kDriverRightJoystickPort);
        operatorJoy = new SmartJoystick(kOperatorJoystickPort);
        //leftJoy.SetSpeedAxis(1);
        //rightJoy.SetSpeedAxis(1);
        leftJoy.SetSpeedAxis(1);
        leftJoy.SetRotateAxis(4);
        Robot.drivetrain.SetIsRanged(true);

        // buttons and triggers
        prepareHatchCollectTrigger = new HatchModeTrigger(operatorJoy, kCollectModeButton, false);
        prepareHatchLowTrigger = new HatchModeTrigger(operatorJoy, kLowModeButton, false);
        prepareHatchMiddleTrigger = new HatchModeTrigger(operatorJoy, kMiddleModeButton, false);
        prepareHatchHighTrigger = new HatchModeTrigger(operatorJoy, kHighModeButton, false);
        // collectHatchTrigger = new HatchModeTrigger(operatorJoy, kCollectPartButton, false);
        ejectHatchTrigger = new HatchModeTrigger(operatorJoy, kEjectPartButton, false);

        wristDownStart = new WristDownStart();
        startButton = new JoystickButton(operatorJoy, kStartButton);
        prepareHatchHookCollectTrigger = new HatchModeTrigger(operatorJoy, kCollectModeButton, true);
        prepareHatchHookLowTrigger = new HatchModeTrigger(operatorJoy, kLowModeButton, true);
        prepareHatchHookMiddleTrigger = new HatchModeTrigger(operatorJoy, kMiddleModeButton, true);
        prepareHatchHookHighTrigger = new HatchModeTrigger(operatorJoy, kHighModeButton, true);
        collectHatchHookTrigger = new HatchModeTrigger(operatorJoy, kCollectPartButton, true);
        ejectHatchHookTrigger = new HatchModeTrigger(operatorJoy, kEjectPartButton, true);

        prepareCargoCollectTrigger = new ModeTrigger(operatorJoy, kCollectModeButton, RobotMode.CARGO);
        prepareCargoLowTrigger = new ModeTrigger(operatorJoy, kLowModeButton, RobotMode.CARGO);
        prepareCargoMiddleTrigger = new ModeTrigger(operatorJoy, kMiddleModeButton, RobotMode.CARGO);
        prepareCargoHighTrigger = new ModeTrigger(operatorJoy, kHighModeButton, RobotMode.CARGO);
        collectCargoTrigger = new ModeTrigger(operatorJoy, kCollectPartButton, RobotMode.CARGO);
        ejectCargoTrigger = new ModeTrigger(operatorJoy, kEjectPartButton, RobotMode.CARGO);

        // liftRobotTrigger = new ModeTrigger(operatorJoy, kLiftRobotButton, RobotMode.CLIMB);
        // liftCloseFrontTrigger = new ModeTrigger(operatorJoy, kLiftCloseFrontButton, RobotMode.CLIMB);
        // liftCloseRearTrigger = new ModeTrigger(operatorJoy, kLiftCloseRearButton, RobotMode.CLIMB);
        // moveBackwardsLifterTrigger = new ModeTrigger(operatorJoy, kMoveBackwardsLifterButton, RobotMode.CLIMB);
        // moveForwardLifterTrigger = new ModeTrigger(operatorJoy, kMoveForwardLifterButton, RobotMode.CLIMB);

        hookToggleButton = new ModeTrigger(operatorJoy, kHatchModeButton, RobotMode.HATCH);
        modeButton = new JoystickButton(operatorJoy, kRobotModeButton);
        climbModeButton = new JoystickButton(operatorJoy, kClimbModeButton);
        moveToVisionTarget = new JoystickButton(leftJoy, kMoveToVisionTargetButton);
        driveStraightButton = new JoystickButton(rightJoy, kDriveStraightButton);
        // prepareLiftButton = new JoystickButton(operatorJoy, kRobotLiftModeButton);

        // axis
        elevatorUpAxis = new JoyAxis(operatorJoy, kElevatorDownAxis, 0, 1, -1, 0);
        elevatorDownAxis = new JoyAxis(operatorJoy, kElevatorUpAxis, 0, -1, -1, 0);
        wristUpAxis = new JoyAxisPart(operatorJoy, kWristAxis, -1, 1, 1, -1, 0.2, 1);
        wristDownAxis = new JoyAxisPart(operatorJoy, kWristAxis, -1, 1, 1, -1, -1, -0.2);

        // commands
        gtaDrive = new CurvatureDrive(Robot.drivetrain, leftJoy);
        //defaultDrive = new TankDrive(Robot.drivetrain, leftJoy, rightJoy);
        driveStraight = new DriveStraight(leftJoy, rightJoy);

        prepareHatchCollect = new InitHatchCollectMode();
        prepareHatchLow = new InitHatchLowMode();
        prepareHatchMiddle = new InitHatchMiddleMode();
        prepareHatchHigh = new InitHatchHighMode();
        ejectHatch = new EjectHatch(operatorJoy);
        
        prepareHatchHookCollect = new InitHatchHookCollectMode();
        prepareHatchHookLow = new InitHatchHookLowMode();
        prepareHatchHookMiddle = new InitHatchHookMiddleMode();
        prepareHatchHookHigh = new InitHatchHookHighMode();

        prepareCargoCollect = new InitCargoCollectMode();
        prepareCargoLow = new InitCargoLowMode();
        prepareCargoMiddle = new InitCargoMiddleMode();
        prepareCargoHigh = new InitCargoHighMode();
        collectCargo = new ActivateIntake(CargoIntake.kIntakeInPower);
        ejectCargo = new ActivateIntake(CargoIntake.kIntakeOutPower);

        // prepareLift = new InitLiftMode();
        // liftRobot = new LiftRobot();
        // moveForwardLifter = new MoveLifterWheels(Lifter.wheelForwardPower);
        // moveBackwardLifter = new MoveLifterWheels(Lifter.wheelReversePower);
        // closeRearLifters = new CloseRearLifters();
        // closeFrontLifters = new CloseFrontLifters();

        elevatorDown = new MoveElevator(elevatorDownAxis);
        elevatorUp = new MoveElevator(elevatorUpAxis);
        wristDown = new MoveWrist(wristDownAxis);
        wristUp = new MoveWrist(wristUpAxis);

        toggleGamepiece = new ToggleGamepieceMode();
        toggleHook = new ToggleHookMode();
        visionAllignment = new VisionAlignment(leftJoy, rightJoy, true);

        /****************************************/
        Robot.drivetrain.setDefaultCommand(gtaDrive);
        //Robot.drivetrain.setDefaultCommand(defaultDrive);
        // modeButton.whenPressed(toggleGamepiece);
        // // climbModeButton.whenPressed(toggleClimb);
        
        // // // climb
        // // prepareLiftButton.whenPressed(prepareLift);
        // // liftRobotTrigger.whenActive(liftRobot);
        // // liftCloseFrontTrigger.whenActive(closeFrontLifters);
        // // liftCloseRearTrigger.whenActive(closeRearLifters);
        // // moveForwardLifterTrigger.whenActive(moveForwardLifter);
        // // moveBackwardsLifterTrigger.whenActive(moveBackwardLifter);

        // // hatch
        // startButton.whenPressed(wristDownStart);
        // prepareHatchCollectTrigger.whenActive(prepareHatchCollect);
        // prepareHatchLowTrigger.whenActive(prepareHatchLow);
        // prepareHatchMiddleTrigger.whenActive(prepareHatchMiddle);
        // prepareHatchHighTrigger.whenActive(prepareHatchHigh);
        // ejectHatchTrigger.whileActive(ejectHatch);
        
        // // hatch with hook
        // prepareHatchHookCollectTrigger.whenActive(prepareHatchHookCollect);
        // prepareHatchHookLowTrigger.whenActive(prepareHatchHookLow);
        // prepareHatchHookMiddleTrigger.whenActive(prepareHatchHookMiddle);
        // prepareHatchHookHighTrigger.whenActive(prepareHatchHookHigh);
        // hookToggleButton.whenActive(toggleHook);

        // // cargo
        // prepareCargoCollectTrigger.whenActive(prepareCargoCollect);
        // prepareCargoLowTrigger.whenActive(prepareCargoLow);
        // prepareCargoMiddleTrigger.whenActive(prepareCargoMiddle);
        // prepareCargoHighTrigger.whenActive(prepareCargoHigh);
        // collectCargoTrigger.whileActive(collectCargo);
        // ejectCargoTrigger.whileActive(new RumbleJoystick(operatorJoy, 0.6, false));
        // ejectCargoTrigger.whileActive(ejectCargo);
        // prepareHatchCollectTrigger.whenActive(prepareHatchMiddle);

        // // manual
        // elevatorUpAxis.whileActive(elevatorUp);
        // elevatorDownAxis.whileActive(elevatorDown);
        // wristDownAxis.whileActive(wristDown);
        // wristUpAxis.whileActive(wristUp);
        // driveStraightButton.whileActive(driveStraight);
         moveToVisionTarget.whileActive(visionAllignment);
    }
}