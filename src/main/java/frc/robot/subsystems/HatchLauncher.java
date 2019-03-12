/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * Add your docs here.
 */
public class HatchLauncher extends Subsystem
{

  // ports
  public static final int kSolenoidPort = 0;

  private DoubleSolenoid launcher;

  public enum LauncherPistonsMode
  {
    OPENED, CLOSED
  }

  private LauncherPistonsMode currentMode;

  public HatchLauncher()
  {
    launcher = new DoubleSolenoid(3,4);

    this.currentMode = LauncherPistonsMode.CLOSED;
    stop();
  }

  public LauncherPistonsMode getCurrentMode()
  {
    return this.currentMode;
  }

  public void launch()
  {
    this.launcher.set(DoubleSolenoid.Value.kForward);
  }

  public void retract()
  {
    this.launcher.set(DoubleSolenoid.Value.kReverse);
  }

  public void stop()
  {
    this.launcher.set(DoubleSolenoid.Value.kOff);
  }

  @Override
  public void initDefaultCommand()
  {
  }
}
