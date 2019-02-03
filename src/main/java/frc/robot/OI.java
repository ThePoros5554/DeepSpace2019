/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.InitCollectMode;
import frc.robot.commands.InitHighMode;
import frc.robot.commands.InitLowMode;
import frc.robot.commands.InitMiddleMode;
import frc.robot.commands.TakeHatchFromFloor;
import frc.robot.commands.ToggleGamepieceMode;
import frc.robot.commands.cargo_intake.IntakeCargo;
import frc.robot.commands.elevator.MoveElevator;
import frc.robot.commands.hatch_launcher.LaunchHatch;
import frc.robot.commands.lifter.CloseFrontLifters;
import frc.robot.commands.lifter.CloseRearLifters;
import frc.robot.commands.lifter.LiftRobot;
import frc.robot.commands.lifter.MoveLifterWheels;
import frc.robot.subsystems.CargoIntake;
import frc.robot.subsystems.Lifter;
import poroslib.commands.TankDrive;
import poroslib.triggers.JoyAxis;
import poroslib.triggers.SmartJoystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    // Button Ports
    private static final int kCollectModeButton = 2; // B
    private static final int kLowModeButton = 1; // A
    private static final int kMiddleModeButton = 3; // X
    private static final int kHighModeButton = 4; // Y
    private static final int kRobotModeButton = 0; // TODO
    private static final int kEjectPartButton = 0; // TODO
    private static final int kCollectPartButton = 0; // TODO
    private static final int kMoveForwardLifterButton = 0;
    private static final int kMoveBackwardLifterButton = 0;
    private static final int kcRearButton = 0;
    private static final int kcFrontButton = 0;
    private static final int kElevatorUpAxis = 0;
    private static final int kElevatorDownAxis = 0;

    // Joystick Ports
    private static final int kDriverLeftJoystickPort = 0;
    private static final int kDriverRightJoystickPort = 1;
    private static final int kOperatorJoystickPort = 2;
    //Power


    //


    private SmartJoystick leftJoy;
    private SmartJoystick rightJoy;
    private SmartJoystick operatorJoy;

    private Button modeButton;
    private Button collectButton;
    private Button lowButton;
    private Button middleButton;
    private Button highButton;
    private Button ejectPartButton;
    private Button collectPartButton;
    private Button liftButton;
    private Button moveForwardLifterButton;
    private Button moveBackwardLifterButton;
    private Button cRearButton;
    private Button cFrontButton;
    private JoyAxis elevatorDownAxis;
    private JoyAxis elevatorUpAxis;

    private TankDrive defaultDrive;
    private LaunchHatch lhatch;
    private IntakeCargo intakeCargo;
    private IntakeCargo outakeCargo;
    private InitCollectMode collectMode;
    private InitLowMode lowMode;
    private InitMiddleMode middleMode;
    private InitHighMode highMode;
    private LiftRobot lift;
    private MoveLifterWheels moveForwardLifter;
    private MoveLifterWheels moveBackwardLifter;
    private CloseFrontLifters cFront;
    private CloseRearLifters cRear;
    private MoveElevator elevatorUp;
    private MoveElevator elevatorDown;
    private TakeHatchFromFloor hatchCollect;

    private ToggleGamepieceMode toggleGamepiece;

    public OI() {
        /************** Initialize **************/

        // joysticks
        leftJoy = new SmartJoystick(kDriverLeftJoystickPort);
        rightJoy = new SmartJoystick(kDriverRightJoystickPort);
        operatorJoy = new SmartJoystick(kOperatorJoystickPort);

        // buttons
        modeButton = new JoystickButton(operatorJoy, kRobotModeButton);
        highButton = new JoystickButton(operatorJoy, kHighModeButton);
        middleButton = new JoystickButton(operatorJoy, kMiddleModeButton);
        lowButton = new JoystickButton(operatorJoy, kLowModeButton);
        collectButton = new JoystickButton(operatorJoy, kCollectModeButton);
        collectPartButton = new JoystickButton(operatorJoy, kCollectPartButton);
        ejectPartButton = new JoystickButton(operatorJoy, kEjectPartButton);
        moveBackwardLifterButton = new JoystickButton(operatorJoy, kMoveBackwardLifterButton);
        moveForwardLifterButton = new JoystickButton(operatorJoy, kMoveForwardLifterButton);
        cRearButton = new JoystickButton(operatorJoy, kcRearButton);
        cFrontButton = new JoystickButton(operatorJoy, kcFrontButton);
        elevatorDownAxis = new JoyAxis(operatorJoy, kElevatorDownAxis, 0, -1, 0, 1);
        elevatorUpAxis = new JoyAxis(operatorJoy, kElevatorDownAxis, 0, 1, 0, 1);

        // commands
        defaultDrive = new TankDrive(Robot.drivetrain, leftJoy, rightJoy);
        collectMode = new InitCollectMode();
        lowMode = new InitLowMode();
        middleMode = new InitMiddleMode();
        highMode = new InitHighMode();
        toggleGamepiece = new ToggleGamepieceMode();

        lhatch = new LaunchHatch();
        intakeCargo = new IntakeCargo(CargoIntake.kIntakeInPower);
        outakeCargo = new IntakeCargo(CargoIntake.kIntakeOutPower);
        lift = new LiftRobot();
        moveForwardLifter = new MoveLifterWheels(Lifter.wheelForwardPower);
        moveBackwardLifter = new MoveLifterWheels(Lifter.wheelReversePower);
        cRear = new CloseRearLifters();
        cFront = new CloseFrontLifters();
        elevatorDown = new MoveElevator(elevatorDownAxis);
        elevatorDown = new MoveElevator(elevatorUpAxis);
        hatchCollect = new TakeHatchFromFloor();
        


        // eject

        /****************************************/

        Robot.drivetrain.setDefaultCommand(defaultDrive);
        Robot.drivetrain.SetIsRanged(true);

        collectButton.whenPressed(collectMode);
        lowButton.whenPressed(lowMode);
        middleButton.whenPressed(middleMode);
        highButton.whenPressed(highMode);
        modeButton.whenPressed(toggleGamepiece);
        collectPartButton.whenPressed(intakeCargo);
        collectPartButton.whenPressed(hatchCollect);
        ejectPartButton.whenPressed(lhatch);
        ejectPartButton.whenPressed(outakeCargo);
        liftButton.whenPressed(lift);
        moveForwardLifterButton.whenPressed(moveForwardLifter);
        moveBackwardLifterButton.whenPressed(moveBackwardLifter);
        cRearButton.whenPressed(cRear);
        cFrontButton.whenPressed(cFront);

        //TODO FIX COMMANDS TO ACTIVATE IN THEIR MODE!
    }
}
