package frc.robot.commands;

import java.util.Map.Entry;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMonitor;
import frc.robot.subsystems.Drivetrain;
import poroslib.position.VisionInfo;
import poroslib.position.geometry.Kinematics;
import poroslib.position.geometry.Pose2d;
import poroslib.position.geometry.Translation2d;
import poroslib.position.geometry.Twist2d;
import poroslib.position.geometry.Rotation2d;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class VisionAllignment extends Command
{
    private Drivetrain driveTrain;
    private RobotMonitor monitor;

    private Pose2d bufferedTarget;

    public VisionAllignment()
    {
        driveTrain = Robot.drivetrain;
        monitor = RobotMonitor.getRobotMonitor();

        requires(driveTrain);
    }

    @Override
    protected void initialize()
    {
        bufferedTarget = null;
    }

    @Override
    protected void execute()
    {
        if(monitor.getLastVisionReport() != null)
        {
            Entry<Double, VisionInfo> visionData = monitor.getLastVisionReport();

            double proccesTimeStamp = visionData.getKey();

            Pose2d cameraToTargetAtProccesTime = visionData.getValue().getHorizontalDisplacement();
            Pose2d robotAtProccesTime = monitor.getPositionAtTime(proccesTimeStamp);
            Pose2d cameraDisplacementFromRobot = Robot.lime.getCameraHorizontalDisplacementFromRobot();

            //this vector represents the position of the target
            Pose2d target = (robotAtProccesTime.transformBy(cameraDisplacementFromRobot)).transformBy(cameraToTargetAtProccesTime);
            //Pose2d target = new Pose2d(new Translation2d(42,113), Rotation2d.fromDegrees(-41));

            if(target.getTranslation().getY() >= 40)
            {
                bufferedTarget = target;
            }

            Pose2d robotNow = monitor.getLastPositionReport().getValue();
            // System.out.println("robot now, x: " + robotNow.getTranslation().getX() 
            //     + ", y: "+ robotNow.getTranslation().getY() + ", deg: " + robotNow.getRotation().getDegrees());
            Pose2d cameraNow = robotNow.transformBy(cameraDisplacementFromRobot);

            //this vector represents the current position of the camera
            Pose2d cameraToTargetNow = bufferedTarget.transformBy(cameraNow.inverse());
            // System.out.println("robot now, x: " + cameraToTargetNow.getTranslation().getX() 
            // + ", y: "+ cameraToTargetNow.getTranslation().getY() + ", deg: " + cameraToTargetNow.getRotation().getDegrees());

            Twist2d cameraToTargetDelta = new Twist2d(Math.hypot(cameraToTargetNow.getTranslation().getX(), cameraToTargetNow.getTranslation().getY()),0, cameraToTargetNow.getRotation().getRadians());
            Kinematics.DriveVelocity velocity = Kinematics.inverseKinematics(cameraToTargetDelta, Drivetrain.TRACKWIDTH, 1);

            SmartDashboard.putNumber("go left: ", velocity.left);
            SmartDashboard.putNumber("go right: ",  velocity.right);

            System.out.println("deg: " + Math.toDegrees(cameraToTargetDelta.dtheta));

            driveTrain.setControlMode(ControlMode.MotionMagic);

            // Robot.drivetrain.set(-(velocity.left * 0.001), (velocity.right * 0.001));
            int leftTicksToGo = Drivetrain.cmToRotations(velocity.left);
            int rightTicksToGo = Drivetrain.cmToRotations(velocity.right);

            // System.out.println((driveTrain.getRawLeftPosition() + leftTicksToGo));

            driveTrain.set(-(driveTrain.getRawLeftPosition() + leftTicksToGo), (driveTrain.getRawRightPosition() + rightTicksToGo));
        }
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