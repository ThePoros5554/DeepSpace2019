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
import frc.robot.commands.ToggleGamepieceMode;
import poroslib.commands.TankDrive;
import poroslib.triggers.SmartJoystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI
{
    //Button Ports
    private static final int kCollectModeButton = 2; // B
    private static final int kLowModeButton = 1; // A
    private static final int kMiddleModeButton = 3; // X
    private static final int kHighModeButton = 4; // Y
    private static final int kRobotModeButton = 0; // TODO
    
    //Joystick Ports
    private static final int kDriverLeftJoystickPort = 0;
    private static final int kDriverRightJoystickPort = 1;
    private static final int kOperatorJoystickPort = 2;

    //

    private SmartJoystick leftJoy;
    private SmartJoystick rightJoy;
    private SmartJoystick operatorJoy;

    private Button modeButton;
    private Button collectButton;
    private Button lowButton;
    private Button middleButton;
    private Button highButton;

    private TankDrive defaultDrive;

    private InitCollectMode collectMode;
    private InitLowMode lowMode;
    private InitMiddleMode middleMode;
    private InitHighMode highMode;

    private ToggleGamepieceMode toggleGamepiece;

    public OI()
    {
        /************** Initialize **************/

        //joysticks
        leftJoy = new SmartJoystick(kDriverLeftJoystickPort); 
        rightJoy = new SmartJoystick(kDriverRightJoystickPort); 
        operatorJoy = new SmartJoystick(kOperatorJoystickPort); 

        //buttons
        modeButton = new JoystickButton(operatorJoy, kRobotModeButton);
        highButton = new JoystickButton(operatorJoy, kHighModeButton);
        middleButton = new JoystickButton(operatorJoy, kMiddleModeButton);
        lowButton = new JoystickButton(operatorJoy, kLowModeButton);
        collectButton  = new JoystickButton(operatorJoy, kCollectModeButton);
        
        //commands
        defaultDrive = new TankDrive(Robot.drivetrain, leftJoy, rightJoy);
        collectMode = new InitCollectMode();
        lowMode = new InitLowMode();
        middleMode = new InitMiddleMode();
        highMode = new InitHighMode();
        toggleGamepiece = new ToggleGamepieceMode();

        /****************************************/

        Robot.drivetrain.setDefaultCommand(defaultDrive);
        Robot.drivetrain.SetIsRanged(true);

        collectButton.whenPressed(collectMode);
        lowButton.whenPressed(lowMode);
        middleButton.whenPressed(middleMode);
        highButton.whenPressed(highMode);
        modeButton.whenPressed(toggleGamepiece);
    }
}
