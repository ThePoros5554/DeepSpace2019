/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Endgame mechanism
 */
public class Lifter extends Subsystem
{ 
  // ports
  private final int kFrontSolenoidForwardPort = 0;
  private final int kFrontSolenoidReversePort = 0;
  private final int kRearSolenoidForwardPort = 0;
  private final int kRearSolenoidReversePort = 0;

  private DoubleSolenoid frontPistons;
  private DoubleSolenoid rearPistons;

	public Lifter()
	{
    frontPistons = new DoubleSolenoid(kFrontSolenoidForwardPort, kFrontSolenoidReversePort);
    rearPistons = new DoubleSolenoid(kRearSolenoidForwardPort, kRearSolenoidReversePort);

    offFront();
    offRear();
	}
	
	public void openFront()
	{
    frontPistons.set(DoubleSolenoid.Value.kForward);
	}
	
	public void openRear()
	{
    rearPistons.set(DoubleSolenoid.Value.kForward);
  }
  
	public void closeFront()
	{
    frontPistons.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void closeRear()
	{
    rearPistons.set(DoubleSolenoid.Value.kReverse);
  }

	public void offFront()
	{
		frontPistons.set(DoubleSolenoid.Value.kOff);
  }
  
  public void offRear()
	{
		rearPistons.set(DoubleSolenoid.Value.kOff);
	}

  @Override
  public void initDefaultCommand()
  {    
  }
}
