package frc.robot.commands;

import java.util.Map.Entry;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMonitor;
import frc.robot.subsystems.Drivetrain;
import poroslib.position.VisionInfo;
import poroslib.position.geometry.Kinematics;
import poroslib.position.geometry.Pose2d;
import poroslib.position.geometry.Twist2d;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class VisionAllignment extends Command
{
    private Drivetrain driveTrain;
    private RobotMonitor monitor;

    public VisionAllignment()
    {
        driveTrain = Robot.drivetrain;
        monitor = RobotMonitor.getRobotMonitor();

        requires(driveTrain);
    }

    @Override
    protected void initialize()
    {
    }

    @Override
    protected void execute()
    {
        if(monitor.getLastVisionReport() != null)
        {
            Entry<Double, VisionInfo> visionData = monitor.getLastVisionReport();

            double proccesTimeStamp = visionData.getKey();

            Pose2d displacementFromTarget = visionData.getValue().getVerticalDisplacement();
            Pose2d robotPoseAtProccesTime = monitor.getPositionAtTime(proccesTimeStamp);
            Pose2d cameraDisplacementFromRobot = monitor.getCameraVerticalDisplacement();

            //this vector represents the position of the target
            Pose2d target = (robotPoseAtProccesTime.transformBy(cameraDisplacementFromRobot)).transformBy(displacementFromTarget);

            Pose2d currentRobotPose = monitor.getLastPositionReport().getValue();
            Pose2d robotToCamera = currentRobotPose.transformBy(cameraDisplacementFromRobot);

            //this vector represents the current position of the camera
            Pose2d cameraToTarget = target.transformBy(robotToCamera.inverse());

            Twist2d cameraToTargetDelta = Kinematics.forwardKinematics(cameraToTarget.getTranslation().getX(), cameraToTarget.getTranslation().getY(), cameraToTarget.getRotation().getRadians());
            Kinematics.DriveVelocity velocity = Kinematics.inverseKinematics(cameraToTargetDelta, 0, 0);

            driveTrain.setControlMode(ControlMode.MotionMagic);
            driveTrain.set(driveTrain.getLeftPositionInCm() + velocity.left, driveTrain.getRightPositionInCm() + velocity.right);
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
      
    }
      
    @Override
    protected void interrupted()
    {
        end();
    }
}