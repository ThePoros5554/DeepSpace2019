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
import frc.robot.commands.InitCollectMode;
import frc.robot.commands.InitHighMode;
import frc.robot.commands.InitLowMode;
import frc.robot.commands.InitMiddleMode;
import frc.robot.commands.ToggleGamepieceMode;
import frc.robot.commands.cargo_intake.IntakeCargo;
import frc.robot.commands.elevator.MoveElevator;
import frc.robot.commands.hatch_launcher.LaunchHatch;
import frc.robot.commands.lifter.CloseFrontLifters;
import frc.robot.commands.lifter.CloseRearLifters;
import frc.robot.commands.lifter.LiftRobot;
import frc.robot.commands.lifter.MoveLifterWheels;
import frc.robot.commands.wrist.AdjustWrist;
import frc.robot.subsystems.CargoIntake;
import frc.robot.subsystems.Lifter;
import frc.robot.subsystems.Wrist;
import frc.robot.triggers.ModeTrigger;
import poroslib.commands.TankDrive;
import poroslib.triggers.JoyAxis;
import poroslib.triggers.SmartJoystick;
import edu.wpi.first.wpilibj.XboxController;

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
    private static final int kMoveForwardLifterButton = 0; 
    private static final int kMoveBackwardsLifterButton = 0;
    private static final int kCloseRearLiftersButton = 0;
    private static final int kCloseFrontLiftersButton = 0;
    private static final int kElevatorUpAxis = 3; // RT
    private static final int kElevatorDownAxis = 2; // LT

    // Joystick Ports
    private static final int kDriverLeftJoystickPort = 0;
    private static final int kDriverRightJoystickPort = 1;
    private static final int kOperatorJoystickPort = 2;

    private SmartJoystick leftJoy;
    private SmartJoystick rightJoy;
    private SmartJoystick operatorJoy;

    private Button modeButton;
    private Button collectButton;
    private Button lowButton;
    private Button middleButton;
    private Button highButton;
    private Button liftButton;
    private Button moveForwardLifterButton;
    private Button moveBackwardLifterButton;
    private Button closeRearLiftersButton;
    private Button closeFrontLiftersButton;
    private ModeTrigger collectHatchButton;
    private ModeTrigger collectCargoButton;
    private ModeTrigger ejectHatchButton;
    private ModeTrigger ejectCargoButton;
    private JoyAxis elevatorDownAxis;
    private JoyAxis elevatorUpAxis;

    private TankDrive defaultDrive;
    private AdjustWrist collectHatch;
    private LaunchHatch ejectHatch;
    private IntakeCargo collectCargo;
    private IntakeCargo ejectCargo;
    private InitCollectMode collectMode;
    private InitLowMode lowMode;
    private InitMiddleMode middleMode;
    private InitHighMode highMode;
    private LiftRobot liftRobot;
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

        // buttons
        modeButton = new JoystickButton(operatorJoy, kRobotModeButton);
        highButton = new JoystickButton(operatorJoy, kHighModeButton);
        middleButton = new JoystickButton(operatorJoy, kMiddleModeButton);
        lowButton = new JoystickButton(operatorJoy, kLowModeButton);
        collectButton = new JoystickButton(operatorJoy, kCollectModeButton);
        moveBackwardLifterButton = new JoystickButton(operatorJoy, kMoveBackwardsLifterButton);
        moveForwardLifterButton = new JoystickButton(operatorJoy, kMoveForwardLifterButton);
        closeFrontLiftersButton = new JoystickButton(operatorJoy, kCloseFrontLiftersButton);
        closeRearLiftersButton = new JoystickButton(operatorJoy, kCloseRearLiftersButton);

        // mode buttons
        collectHatchButton = new ModeTrigger(operatorJoy, kCollectPartButton, RobotMode.HATCH);
        collectCargoButton = new ModeTrigger(operatorJoy, kCollectPartButton, RobotMode.CARGO);
        ejectHatchButton = new ModeTrigger(operatorJoy, kEjectPartButton, RobotMode.HATCH);
        ejectCargoButton = new ModeTrigger(operatorJoy, kEjectPartButton, RobotMode.CARGO);
        
        // axis
        elevatorDownAxis = new JoyAxis(operatorJoy, kElevatorDownAxis, 0, -1, 0, 1);
        elevatorUpAxis = new JoyAxis(operatorJoy, kElevatorUpAxis, 0, 1, 0, 1);

        // commands
        defaultDrive = new TankDrive(Robot.drivetrain, leftJoy, rightJoy);
        collectMode = new InitCollectMode();
        lowMode = new InitLowMode();
        middleMode = new InitMiddleMode();
        highMode = new InitHighMode();
        toggleGamepiece = new ToggleGamepieceMode();
        collectHatch = new AdjustWrist(Wrist.kFloor);
        ejectHatch = new LaunchHatch();
        collectCargo = new IntakeCargo(CargoIntake.kIntakeInPower);
        ejectCargo = new IntakeCargo(CargoIntake.kIntakeOutPower);
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

        collectButton.whenPressed(collectMode);
        lowButton.whenPressed(lowMode);
        middleButton.whenPressed(middleMode);
        highButton.whenPressed(highMode);
        
        modeButton.whenPressed(toggleGamepiece);

        liftButton.whenPressed(liftRobot);

        moveForwardLifterButton.whenPressed(moveForwardLifter);
        moveBackwardLifterButton.whenPressed(moveBackwardLifter);
        
        closeFrontLiftersButton.whenPressed(cFront);
        closeRearLiftersButton.whenPressed(cRear);

        collectHatchButton.whenActive(collectHatch);
        ejectHatchButton.whenActive(ejectHatch);

        collectCargoButton.whenActive(collectCargo);
        ejectCargoButton.whenActive(ejectCargo);
    }
}
