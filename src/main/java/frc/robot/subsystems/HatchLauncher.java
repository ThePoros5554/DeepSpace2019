/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import poroslib.subsystems.DoubleSolenoidSys;

/**
 * Add your docs here.
 */
public class HatchLauncher extends DoubleSolenoidSys {

  // ports
  public static final int kSolenoidForwardPort = 0;
  public static final int kSolenoidReversePort = 1;

  public enum LauncherPistonsMode
  {
    OPENED, CLOSED
  }

  private LauncherPistonsMode currentMode;

  public HatchLauncher()
  {
    super(kSolenoidForwardPort, kSolenoidReversePort);

    this.currentMode = LauncherPistonsMode.CLOSED;
  }

  public LauncherPistonsMode getCurrentMode()
  {
    return this.currentMode;
  }

  @Override
  public void initDefaultCommand()
  {
  }
}
