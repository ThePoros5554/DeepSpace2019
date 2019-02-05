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
import frc.robot.commands.InitCargoMiddleMode;
import frc.robot.commands.InitHatchHighMode;
import frc.robot.commands.InitCargoHighMode;
import frc.robot.commands.ToggleGamepieceMode;
import frc.robot.commands.cargo_intake.ActivateIntake;
import frc.robot.commands.elevator.MoveElevator;
import frc.robot.commands.EjectHatch;
import frc.robot.commands.lifter.CloseFrontLifters;
import frc.robot.commands.lifter.CloseRearLifters;
import frc.robot.commands.lifter.LiftRobot;
import frc.robot.commands.lifter.MoveLifterWheels;
import frc.robot.commands.wrist.AdjustWrist;
import frc.robot.subsystems.CargoIntake;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Lifter;
import frc.robot.subsystems.Wrist.WristMode;
import frc.robot.triggers.ModeTrigger;
import poroslib.commands.TankDrive;
import poroslib.triggers.JoyAxis;
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
    private static final int kLiftRobotButton = 0;
    private static final int kMoveForwardLifterButton = 0; 
    private static final int kMoveBackwardsLifterButton = 0;
    private static final int kLiftCloseRearButton = 0;
    private static final int kLiftCloseFrontButton = 0;
    private static final int kElevatorUpAxis = 3; // RT
    private static final int kElevatorDownAxis = 2; // LT
    private static final int kRobotLiftModeButton = 0;

    // Joystick Ports
    private static final int kDriverLeftJoystickPort = 0;
    private static final int kDriverRightJoystickPort = 1;
    private static final int kOperatorJoystickPort = 2;

    private SmartJoystick leftJoy;
    private SmartJoystick rightJoy;
    private SmartJoystick operatorJoy;

    private Button modeButton;
    private Button prepareLiftButton;

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
    private ModeTrigger moveForwardLifterTrigger;
    private ModeTrigger moveBackwardsLifterTrigger;
    
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
    private MoveLifterWheels moveForwardLifter;
    private MoveLifterWheels moveBackwardLifter;
    private CloseFrontLifters cFront;
    private CloseRearLifters cRear;
    private MoveElevator elevatorUp;
    private MoveElevator elevatorDown;

    private ToggleGamepieceMode toggleGamepiece;

    public OI()
    {  
        /************** Initialize **************/

        // joysticks
        leftJoy = new SmartJoystick(kDriverLeftJoystickPort);
        rightJoy = new SmartJoystick(kDriverRightJoystickPort);
        operatorJoy = new SmartJoystick(kOperatorJoystickPort);

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

        liftRobotTrigger = new ModeTrigger(operatorJoy, kLiftRobotButton, RobotMode.CLIMB);
        liftCloseFrontTrigger = new ModeTrigger(operatorJoy, kLiftCloseFrontButton, RobotMode.CLIMB);
        liftCloseRearTrigger = new ModeTrigger(operatorJoy, kLiftCloseRearButton, RobotMode.CLIMB);
        moveBackwardsLifterTrigger = new ModeTrigger(operatorJoy, kMoveBackwardsLifterButton, RobotMode.CLIMB);
        moveForwardLifterTrigger = new ModeTrigger(operatorJoy, kMoveForwardLifterButton, RobotMode.CLIMB);

        modeButton = new JoystickButton(operatorJoy, kRobotModeButton);
        prepareLiftButton = new JoystickButton(operatorJoy, kRobotLiftModeButton);
        
        // axis
        elevatorDownAxis = new JoyAxis(operatorJoy, kElevatorDownAxis, 0, -1, 0, 1);
        elevatorUpAxis = new JoyAxis(operatorJoy, kElevatorUpAxis, 0, 1, 0, 1);

        // commands
        defaultDrive = new TankDrive(Robot.drivetrain, leftJoy, rightJoy);
        
        prepareHatchCollect = new InitHatchCollectMode();
        prepareHatchLow = new InitHatchLowMode();
        prepareHatchMiddle = new InitHatchMiddleMode();
        prepareHatchHigh = new InitHatchHighMode();
        collectHatch = new AdjustWrist(WristMode.DOWN);
        ejectHatch = new EjectHatch(Drivetrain.kEjectDriveBackDistance);

        prepareCargoCollect = new InitCargoCollectMode();
        prepareCargoLow = new InitCargoLowMode();
        prepareCargoMiddle = new InitCargoMiddleMode();
        prepareCargoHigh = new InitCargoHighMode();
        toggleGamepiece = new ToggleGamepieceMode();

        prepareLift = new InitLiftMode();

        collectCargo = new ActivateIntake(CargoIntake.kIntakeInPower);
        ejectCargo = new ActivateIntake(CargoIntake.kIntakeOutPower);
        liftRobot = new LiftRobot();
        moveForwardLifter = new MoveLifterWheels(Lifter.wheelForwardPower);
        moveBackwardLifter = new MoveLifterWheels(Lifter.wheelReversePower);
        cRear = new CloseRearLifters();
        cFront = new CloseFrontLifters();
        elevatorDown = new MoveElevator(elevatorDownAxis);
        elevatorUp = new MoveElevator(elevatorUpAxis);

        /****************************************/

        Robot.drivetrain.setDefaultCommand(defaultDrive);
        Robot.drivetrain.SetIsRanged(true);

        elevatorUpAxis.whileActive(elevatorUp);
        elevatorDownAxis.whileActive(elevatorDown);
        
        modeButton.whenPressed(toggleGamepiece);
        
        liftRobotTrigger.whenActive(liftRobot);

        prepareLiftButton.whenPressed(prepareLift);

        moveForwardLifterTrigger.whenActive(moveForwardLifter);
        moveBackwardsLifterTrigger.whenActive(moveBackwardLifter);
        
        liftCloseFrontTrigger.whenActive(cFront);
        liftCloseRearTrigger.whenActive(cRear);

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
        collectCargoTrigger.whenActive(collectCargo);
        ejectCargoTrigger.whenActive(ejectCargo);
    }
}
