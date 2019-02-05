package poroslib.position.geometry;

import poroslib.util.Interpolable;

public class Pose2d implements Interpolable<Pose2d>
{
    private final static double kEps = 1E-9;

    protected Translation2d translation_;
    protected Rotation2d rotation_;

    public Pose2d() {
        translation_ = new Translation2d();
        rotation_ = new Rotation2d();
    }

    public Pose2d(Translation2d translation, Rotation2d rotation) {
        translation_ = translation;
        rotation_ = rotation;
    }

    public Pose2d(Pose2d other) {
        translation_ = new Translation2d(other.translation_);
        rotation_ = new Rotation2d(other.rotation_);
    }

    public static Pose2d fromTranslation(Translation2d translation) {
        return new Pose2d(translation, new Rotation2d());
    }

    public static Pose2d fromRotation(Rotation2d rotation) {
        return new Pose2d(new Translation2d(), rotation);
    }

    /**
     * Obtain a new Pose2d from a (constant curvature) velocity. See:
     * https://github.com/strasdat/Sophus/blob/master/sophus/se2.hpp
     */
    public static Pose2d fromVelocity(Twist2d delta) {
        double sin_theta = Math.sin(delta.dtheta);
        double cos_theta = Math.cos(delta.dtheta);
        double s, c;
        if (Math.abs(delta.dtheta) < kEps) {
            s = 1.0 - 1.0 / 6.0 * delta.dtheta * delta.dtheta;
            c = .5 * delta.dtheta;
        } else {
            s = sin_theta / delta.dtheta;
            System.out.println("s: " + s);
            c = (1.0 - cos_theta) / delta.dtheta;
            System.out.println("c: " + c);
        }
        return new Pose2d(new Translation2d(delta.dx * s - delta.dy * c, delta.dx * c + delta.dy * s),
                new Rotation2d(cos_theta, sin_theta, false));
    }

    public Translation2d getTranslation() {
        return translation_;
    }

    public void setTranslation(Translation2d translation) {
        translation_ = translation;
    }

    public Rotation2d getRotation() {
        return rotation_;
    }

    public void setRotation(Rotation2d rotation) {
        rotation_ = rotation;
    }

    /**
     * Transforming this Pose2d means first translating by
     * other.translation and then rotating by other.rotation
     * 
     * @param other
     *            The other transform.
     * @return This transform * other
     */
    public Pose2d transformBy(Pose2d other) {
        return new Pose2d(translation_.translateBy(other.translation_.rotateBy(rotation_)),
                rotation_.rotateBy(other.rotation_));
    }

    /**
     * The inverse of this transform "undoes" the effect of translating by this
     * transform.
     * 
     * @return The opposite of this transform.
     */
    public Pose2d inverse() {
        Rotation2d rotation_inverted = rotation_.inverse();
        return new Pose2d(translation_.inverse().rotateBy(rotation_inverted), rotation_inverted);
    }

    @Override
    public String toString() {
        return "T:" + translation_.toString() + ", R:" + rotation_.toString();
    }

    /**
     * Do linear interpolation of this transform (there are more accurate ways
     * using constant curvature, but this is good enough).
     */
    @Override
    public Pose2d interpolate(Pose2d other, double x) 
    {
        if (x <= 0) 
        {
            return new Pose2d(this);
        }
        else if (x >= 1)
        {
            return new Pose2d(other);
        }
        return new Pose2d(translation_.interpolate(other.translation_, x),
                rotation_.interpolate(other.rotation_, x));
    }
}