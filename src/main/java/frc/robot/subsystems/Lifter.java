/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import poroslib.sensors.DualLimit;
import poroslib.sensors.LimitSwitch;
import poroslib.sensors.SysPosition;
import poroslib.subsystems.MechSys;
import poroslib.util.Speed;

/**
 * Endgame mechanism
 */
public class Lifter extends Subsystem
{ 
  // ports
  private final int kFrontRightPort = 1;
  private final int kFrontRightUpSwitchPort = 3;
  private final int kFrontRightDownSwitchPort = 2;
  private final int kFrontLeftPort = 2;
  private final int kFrontLeftUpSwitchPort = 0;
  private final int kFrontLeftDownSwitchPort = 1;
  private final int kBackPort = 4;
  private final int kBackDownSwitchPort = 4;
  private final int kWheelsPort = 3;

  public static Speed fwdClimbSpeed = new Speed(-0.8);
  public static Speed rvClimbSpeed = new Speed(0.8);
  public static final double slowSpeedClimb = 0.3;
  public static final double normalSpeedClimb = 0.8;
  public static final double wheelFwdSpeed = 0.8;
  public static final double wheelRvSpeed = -0.3;
  public static final double kConstPowerBack = -0.15;
  public static final double kConstPowerFront = -0.15;

  private MechSys frontRight;
  private MechSys frontLeft;
  private MechSys back;

  private MechSys wheels;

  private DualLimit frontRightSwitches;
  private DualLimit frontLeftSwitches;
  private LimitSwitch backSwitches;

	public Lifter()
	{
    frontRight = new MechSys(new Victor(kFrontRightPort));
    frontLeft = new MechSys(new Victor(kFrontLeftPort));
    back = new MechSys(new Victor(kBackPort));

    frontRightSwitches = new DualLimit(
      new LimitSwitch(kFrontRightUpSwitchPort, SysPosition.Bottom, true),
      new LimitSwitch(kFrontRightDownSwitchPort, SysPosition.Top, true));

    frontLeftSwitches = new DualLimit(
      new LimitSwitch(kFrontLeftUpSwitchPort, SysPosition.Bottom, true),
      new LimitSwitch(kFrontLeftDownSwitchPort, SysPosition.Top, true));

    backSwitches = new LimitSwitch(kBackDownSwitchPort, SysPosition.Bottom, true);

    wheels = new MechSys(new Victor(kWheelsPort));

    frontRight.SetLimitSwitch(frontRightSwitches);
    frontRight.SetStopOnBottom(true);
    frontLeft.SetLimitSwitch(frontLeftSwitches);
    frontLeft.SetStopOnTop(true);
    back.SetLimitSwitch(backSwitches);
  }
  
  public void setFrontRight(double power, double zeroValue)
  {
    frontRight.Activate(-power, -zeroValue);
  }

  public void setFrontLeft(double power, double zeroValue)
 
  {
    frontLeft.Activate(power, zeroValue);
  }

  public void setBack(double power, double zeroValue)
  {
    back.Activate(power, zeroValue);
  }

  public void setFront(double power, double zeroValue)
  {

    frontRight.Activate(-power, -zeroValue);
    frontLeft.Activate(power, zeroValue);
  }

  public void setWheels(double power)
  {
    wheels.Activate(power);
  }

  public SysPosition GetFrontLeftPos()
  {
    return frontLeftSwitches.GetPosition();
  }

  public SysPosition GetFrontRightPos()
  {
    return frontRightSwitches.GetPosition();
  }

  public SysPosition GetBackPos()
  {
    return backSwitches.GetPosition();
  }

  @Override
  public void initDefaultCommand()
  {    
  }
}
