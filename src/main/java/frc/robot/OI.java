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
import frc.robot.commands.InitCargoCollectMode;
import frc.robot.commands.InitHatchLowMode;
import frc.robot.commands.InitCargoLowMode;
import frc.robot.commands.InitHatchMiddleMode;
import frc.robot.commands.InitLiftMode;
import frc.robot.commands.ToggleClimbMode;
import frc.robot.commands.InitCargoMiddleMode;
import frc.robot.commands.InitHatchHighMode;
import frc.robot.commands.InitCargoHighMode;
import frc.robot.commands.ToggleGamepieceMode;
import frc.robot.commands.VisionAlignment;
import frc.robot.commands.cargo_intake.ActivateIntake;
import frc.robot.commands.elevator.MoveElevator;
import frc.robot.commands.EjectHatch;
import frc.robot.commands.lifter.CloseFrontLifters;
import frc.robot.commands.lifter.CloseRearLifters;
import frc.robot.commands.lifter.LiftRobot;
import frc.robot.commands.wrist.AdjustWrist;
import frc.robot.commands.wrist.MoveWrist;
import frc.robot.subsystems.CargoIntake;
import frc.robot.triggers.ModeTrigger;
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
    private static final int kCollectModeButton = 2; // B
    private static final int kLowModeButton = 1; // A
    private static final int kMiddleModeButton = 3; // X
    private static final int kHighModeButton = 4; // Y
    private static final int kRobotModeButton = 8; // START
    private static final int kEjectPartButton = 5; // LB
    private static final int kCollectPartButton = 6; // RB
    private static final int kClimbModeButton = 7; // BACK
    private static final int kLiftCloseRearButton = 0;
    private static final int kLiftCloseFrontButton = 0;
    private static final int kElevatorUpAxis = 3; // RT
    private static final int kElevatorDownAxis = 2; // LT
    private static final int kWristAxis = 1; // L 
    private static final int kRobotLiftModeButton = 0;

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
    private Button climbModeButton;
    private Button prepareLiftButton;
    private Button moveToVisionTarget;

    private ModeTrigger prepareHatchCollectTrigger;
    private ModeTrigger prepareCargoCollectTrigger;
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
    private ModeTrigger liftRobotTrigger;
    private ModeTrigger liftCloseFrontTrigger;
    private ModeTrigger liftCloseRearTrigger;
    
    private JoyAxis wristDownAxis;
    private JoyAxis wristUpAxis;
    private JoyAxis elevatorDownAxis;
    private JoyAxis elevatorUpAxis;

    private TankDrive defaultDrive;
    private AdjustWrist collectHatch;
    private EjectHatch ejectHatch;
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
    private LiftRobot liftRobot;
    private InitLiftMode prepareLift;
    private CloseFrontLifters closeFrontLifters;
    private CloseRearLifters closeRearLifters;
    private MoveWrist wristDown;
    private MoveWrist wristUp;
    private MoveElevator elevatorUp;
    private MoveElevator elevatorDown;

    private VisionAlignment visionAllignment;

    private ToggleGamepieceMode toggleGamepiece;
    private ToggleClimbMode toggleClimb;

    public OI()
    {  
        /************** Initialize **************/

        // joysticks
        leftJoy = new SmartJoystick(kDriverLeftJoystickPort);
        rightJoy = new SmartJoystick(kDriverRightJoystickPort);
        operatorJoy = new SmartJoystick(kOperatorJoystickPort);
        leftJoy.SetSpeedAxis(1);
        rightJoy.SetSpeedAxis(1);
        Robot.drivetrain.SetIsRanged(true);

        // buttons and triggers
        prepareHatchCollectTrigger = new ModeTrigger(operatorJoy, kCollectModeButton, RobotMode.HATCH);
        prepareHatchLowTrigger = new ModeTrigger(operatorJoy, kLowModeButton, RobotMode.HATCH);
        prepareHatchMiddleTrigger = new ModeTrigger(operatorJoy, kMiddleModeButton, RobotMode.HATCH);
        prepareHatchHighTrigger = new ModeTrigger(operatorJoy, kHighModeButton, RobotMode.HATCH);
        collectHatchTrigger = new ModeTrigger(operatorJoy, kCollectPartButton, RobotMode.HATCH);
        ejectHatchTrigger = new ModeTrigger(operatorJoy, kEjectPartButton, RobotMode.HATCH);

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

        modeButton = new JoystickButton(operatorJoy, kRobotModeButton);
        climbModeButton = new JoystickButton(operatorJoy, kClimbModeButton);
        moveToVisionTarget = new JoystickButton(rightJoy, kMoveToVisionTargetButton);
        // prepareLiftButton = new JoystickButton(operatorJoy, kRobotLiftModeButton);

        // axis
        elevatorUpAxis = new JoyAxis(operatorJoy, kElevatorDownAxis, 0, 1, -1, 0);
        elevatorDownAxis = new JoyAxis(operatorJoy, kElevatorUpAxis, 0, -1, -1, 0);
        wristUpAxis = new JoyAxisPart(operatorJoy, kWristAxis, -1, 1, 1, -1, 0.2, 1);
        wristDownAxis = new JoyAxisPart(operatorJoy, kWristAxis, -1, 1, 1, -1, -1, -0.2);

        // commands
        defaultDrive = new TankDrive(Robot.drivetrain, leftJoy, rightJoy);
        
        prepareHatchCollect = new InitHatchCollectMode();
        prepareHatchLow = new InitHatchLowMode();
        prepareHatchMiddle = new InitHatchMiddleMode();
        prepareHatchHigh = new InitHatchHighMode();
        // collectHatch = new AdjustWrist(WristMode.DOWN);
        // ejectHatch = new EjectHatch(Drivetrain.kEjectDriveBackDistance);

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
        visionAllignment = new VisionAlignment();

        /****************************************/

        Robot.drivetrain.setDefaultCommand(defaultDrive);
        modeButton.whenPressed(toggleGamepiece);
        climbModeButton.whenPressed(toggleClimb);
        
        // // climb
        // prepareLiftButton.whenPressed(prepareLift);
        // liftRobotTrigger.whenActive(liftRobot);
        // liftCloseFrontTrigger.whenActive(closeFrontLifters);
        // liftCloseRearTrigger.whenActive(closeRearLifters);
        // moveForwardLifterTrigger.whenActive(moveForwardLifter);
        // moveBackwardsLifterTrigger.whenActive(moveBackwardLifter);

        // hatch
        prepareHatchCollectTrigger.whenActive(prepareHatchCollect);
        prepareHatchLowTrigger.whenActive(prepareHatchLow);
        prepareHatchMiddleTrigger.whenActive(prepareHatchMiddle);
        prepareHatchHighTrigger.whenActive(prepareHatchHigh);
        // collectHatchTrigger.whenActive(collectHatch);
        // ejectHatchTrigger.whenActive(ejectHatch);

        // cargo
        prepareCargoCollectTrigger.whenActive(prepareCargoCollect);
        prepareCargoLowTrigger.whenActive(prepareCargoLow);
        prepareCargoMiddleTrigger.whenActive(prepareCargoMiddle);
        prepareCargoHighTrigger.whenActive(prepareCargoHigh);
        collectCargoTrigger.whileActive(collectCargo);
        ejectCargoTrigger.whileActive(ejectCargo);
        prepareHatchCollectTrigger.whenActive(prepareHatchMiddle);

        // manual
        elevatorUpAxis.whileActive(elevatorUp);
        elevatorDownAxis.whileActive(elevatorDown);
        wristDownAxis.whileActive(wristDown);
        wristUpAxis.whileActive(wristUp);
    }
}
