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
import poroslib.systems.PIDProcessor;
import poroslib.triggers.SmartJoystick;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class VisionAlignment extends Command
{
    private Drivetrain driveTrain;
    private RobotMonitor monitor;

    private Pose2d bufferedTarget;
    private VisionInfo bufferedVisionTarget;

    // private DriveVelocity lastDriveSignal;

    // private final double maxDriveVelocity = 1000;
    // private final double headingGain = 4;
    private final double distanceGain = 0.5;

    private SmartJoystick joy;

    private boolean isDriverControl;

    private PIDProcessor angleController;

    public static double delta_v;


    public VisionAlignment(SmartJoystick joy, boolean isDriverControl)
    {
        driveTrain = Robot.drivetrain;
        monitor = RobotMonitor.getRobotMonitor();
        this.joy = joy;
        this.isDriverControl = isDriverControl;

        requires(driveTrain);

        angleController = new PIDProcessor(0.015, 0.0001, 0, driveTrain.getNavx(), false);
        angleController.setInputRange(-180, 180);
        angleController.setContinuous(true);
    }

    @Override
    protected void initialize()
    {
        bufferedTarget = null;

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

                bufferedTarget = target;

                Pose2d robotNow = monitor.getLastPositionReport().getValue();
                Pose2d cameraNow = robotNow.transformBy(cameraDisplacementFromRobot);

                // this vector represents the current position of the camera
                Pose2d cameraToTargetNow = bufferedTarget.transformBy(cameraNow.inverse());

                Twist2d cameraToTargetDelta = new Twist2d(Math.hypot(cameraToTargetNow.getTranslation().getX(), cameraToTargetNow.getTranslation().getY()),0, bufferedTarget.getRotation().getDegrees());

                //angle portion for the position loop
                delta_v = cameraToTargetDelta.dtheta;
                         
                double forwardValue = 0;

                if (isDriverControl)
                {
                    if(driveTrain.IsReversed())
                    {
                        forwardValue = -joy.GetSpeedAxis();
                    }
                    else
                    {
                        forwardValue = joy.GetSpeedAxis();
                    }

                    angleController.setSetpoint(delta_v);
                    angleController.enable();
                }
                else
                {
                    forwardValue = distanceGain * cameraToTargetDelta.dx;
                }


                // DriveVelocity velocity = new DriveVelocity(forwardValue - delta_v, forwardValue + delta_v);

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

                // int leftTicksToGo = Drivetrain.cmToRotations(velocity.left);
                // int rightTicksToGo = Drivetrain.cmToRotations(velocity.right);

                // driveTrain.selectProfileSlot(1);
                // driveTrain.set(-(driveTrain.getRawLeftPosition() + leftTicksToGo), (driveTrain.getRawRightPosition() + rightTicksToGo));

                // lastDriveSignal = velocity;
                driveTrain.curvatureDrive(forwardValue, angleController.GetOutputValue(), true, 1);
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
      angleController.reset();
    }
      
    @Override
    protected void interrupted()
    {
        end();
    }
}