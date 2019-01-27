/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import poroslib.commands.TankDrive;
import poroslib.triggers.SmartJoystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI
{
    public static final int kDriverLeftJoystickPort = 0;
    public static final int kDriverRightJoystickPort = 1;
    public static final int kOperatorJoystickPort = 2;

    private SmartJoystick leftJoy;
    private SmartJoystick rightJoy;
    private SmartJoystick operatorJoy;

    public OI()
    {
        initMembers();
    }

    private void initMembers()
    {
        this.leftJoy = new SmartJoystick(kDriverLeftJoystickPort); 
        this.rightJoy = new SmartJoystick(kDriverRightJoystickPort); 
        this.operatorJoy = new SmartJoystick(kOperatorJoystickPort); 

        Robot.drivetrain.setDefaultCommand(new TankDrive(Robot.drivetrain, this.leftJoy, this.rightJoy));
        Robot.drivetrain.SetIsRanged(true);
    }
}
