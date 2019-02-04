package poroslib.position.geomatry;

public class Kinematics
{
    private static final double kEpsilon = 1E-9;

    /**
     * Forward kinematics using encoders and explicitly measured rotation (ex.
     * from gyro)
     */
    public static Twist2d forwardKinematics(double left_wheel_delta, double right_wheel_delta,
            double delta_rotation_rads) {
        return new Twist2d((left_wheel_delta + right_wheel_delta) / 2, 0, delta_rotation_rads);
    }

    /** Append the result of forward kinematics to a previous pose. */
    public static Pose2d integrateForwardKinematics(Pose2d current_pose, double left_wheel_delta,
            double right_wheel_delta, Rotation2d current_heading) {
        Twist2d with_gyro = forwardKinematics(left_wheel_delta, right_wheel_delta,
                current_pose.getRotation().inverse().rotateBy(current_heading).getRadians());
        return current_pose.transformBy(Pose2d.fromVelocity(with_gyro));
    }

    public static class DriveVelocity {
        public final double left;
        public final double right;

        public DriveVelocity(double left, double right) {
            this.left = left;
            this.right = right;
        }
    }

}
