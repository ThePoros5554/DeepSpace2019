package frc.robot.commands;

import java.util.Map.Entry;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMonitor;
import frc.robot.subsystems.Drivetrain;
import poroslib.position.VisionInfo;
import poroslib.position.geometry.Pose2d;
import poroslib.position.geometry.Twist2d;
import poroslib.position.geometry.Kinematics.DriveVelocity;
import poroslib.triggers.SmartJoystick;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class VisionAlignment extends Command
{
    private Drivetrain driveTrain;
    private RobotMonitor monitor;

    private Pose2d bufferedTarget;
    private VisionInfo bufferedVisionTarget;

    private DriveVelocity lastDriveSignal;

    private final double maxDriveVelocity = 1000;
    private final double headingGain = 4;
    private final double distanceGain = 0.5;

    private SmartJoystick leftJoy;
    private SmartJoystick rightJoy;
    private boolean isDriverControl;


    public VisionAlignment(SmartJoystick leftJoy, SmartJoystick rightJoy, boolean isDriverControl)
    {
        driveTrain = Robot.drivetrain;
        monitor = RobotMonitor.getRobotMonitor();
        this.leftJoy = leftJoy;
        this.rightJoy = rightJoy;
        this.isDriverControl = isDriverControl;

        requires(driveTrain);
    }

    @Override
    protected void initialize()
    {
        bufferedTarget = null;
        driveTrain.setControlMode(ControlMode.Position);
        driveTrain.selectProfileSlot(1);
    }

    @Override
    protected void execute()
    {
        if (monitor.getLastVisionReport() != null)
        {

            Entry<Double, VisionInfo> visionData = monitor.getLastVisionReport();

            double processTimeStamp = visionData.getKey();

            if (this.validateVisionTarget(visionData.getValue()))
            {
                bufferedVisionTarget = visionData.getValue();
            }

            if (bufferedVisionTarget != null)
            {
                Pose2d cameraToTargetAtProcessTime = bufferedVisionTarget.getHorizontalDisplacement();
                Pose2d robotAtProcessTime = monitor.getPositionAtTime(processTimeStamp);
                Pose2d cameraDisplacementFromRobot = Robot.lime.getCameraHorizontalDisplacementFromRobot();

                // this vector represents the position of the target
                Pose2d target = (robotAtProcessTime.transformBy(cameraDisplacementFromRobot)).transformBy(cameraToTargetAtProcessTime);

                if (target.getTranslation().getY() >= 30 || bufferedTarget == null)
                {
                    bufferedTarget = target;
                }

                Pose2d robotNow = monitor.getLastPositionReport().getValue();
                Pose2d cameraNow = robotNow.transformBy(cameraDisplacementFromRobot);

                // this vector represents the current position of the camera
                Pose2d cameraToTargetNow = bufferedTarget.transformBy(cameraNow.inverse());

                Twist2d cameraToTargetDelta = new Twist2d(Math.hypot(cameraToTargetNow.getTranslation().getX(), cameraToTargetNow.getTranslation().getY()),0, cameraToTargetNow.getRotation().getDegrees());

                //angle portion for the position loop
                double delta_v = headingGain * cameraToTargetDelta.dtheta;

                
                double forwardValue;
                if(isDriverControl)
                {
                    if (Robot.drivetrain.IsReversed())
                    {  
                        forwardValue = -distanceGain * (((leftJoy.GetSpeedAxis() + rightJoy.GetSpeedAxis()) / 2) * -250);
                    }
                    else
                    {
                        forwardValue = distanceGain * (((leftJoy.GetSpeedAxis() + rightJoy.GetSpeedAxis()) / 2) * -250);
                    }
                }
                else
                {
                    forwardValue = distanceGain * cameraToTargetDelta.dx;
                }
                DriveVelocity velocity = new DriveVelocity(forwardValue - delta_v, forwardValue + delta_v);

                // double forwardSpeed = (Math.abs(velocity.left) + Math.abs(velocity.right)) / 2;
                // if(forwardSpeed > this.maxDriveVelocity)
                // {
                //     if(lastDriveSignal != null)
                //     {
                //         velocity = lastDriveSignal;
                //     } 
                //     else
                //     {
                //         double diffrence = forwardSpeed / maxDriveVelocity;
                //         velocity = new DriveVelocity((velocity.left / diffrence), (velocity.right / diffrence));
                //     }
                // }

                SmartDashboard.putNumber("Go Left:", velocity.left);
                SmartDashboard.putNumber("Go Right:",  velocity.right);

                int leftTicksToGo = Drivetrain.cmToRotations(velocity.left);
                int rightTicksToGo = Drivetrain.cmToRotations(velocity.right);

                driveTrain.selectProfileSlot(1);
                driveTrain.set(-(driveTrain.getRawLeftPosition() + leftTicksToGo), (driveTrain.getRawRightPosition() + rightTicksToGo));

                lastDriveSignal = velocity;
            }
        }
    }

    private boolean validateVisionTarget(VisionInfo value) 
    {
        boolean isValid = true;

        if (value.getIsTarget() == false)
        {
            isValid = false;
        }

        if (value.getHorizontalSideLength() < value.getVerticalSideLength())
        {
            isValid = false;
        }

        return isValid;
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
      driveTrain.setControlMode(ControlMode.PercentOutput);
      driveTrain.set(0, 0);
    }
      
    @Override
    protected void interrupted()
    {
        end();
    }
}