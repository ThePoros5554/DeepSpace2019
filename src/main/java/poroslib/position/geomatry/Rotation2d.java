package poroslib.position.geomatry;

import poroslib.util.Interpolable;

public class Rotation2d implements Interpolable<Rotation2d>
{
    private final double kEpsilon = 1e-12;

    private final double cos_angle;
    private final double sin_angle;

    public Rotation2d() 
    {
        this(1, 0, false);
    }

    public Rotation2d(double x, double y, boolean normalize) {
        if (normalize) 
        {
            // From trig, we know that sin^2 + cos^2 == 1, but as we do math on this object we might accumulate rounding errors.
            // Normalizing forces us to re-scale the sin and cos to reset rounding errors.
            double magnitude = Math.hypot(x, y);

            if (magnitude > kEpsilon) 
            {
                sin_angle = y / magnitude;
                cos_angle = x / magnitude;
            }
            else
            {
                sin_angle = 0;
                cos_angle = 1;
            }
        }
        else 
        {
            cos_angle = x;
            sin_angle = y;
        }
    }

    public Rotation2d(final Rotation2d other) 
    {
        cos_angle = other.cos_angle;
        sin_angle = other.sin_angle;
    }

    public Rotation2d(final Translation2d direction, boolean normalize) 
    {
        this(direction.x(), direction.y(), normalize);
    }

    public static Rotation2d fromRadians(double angle_radians) 
    {
        return new Rotation2d(Math.cos(angle_radians), Math.sin(angle_radians), false);
    }

    public static Rotation2d fromDegrees(double angle_degrees) 
    {
        return fromRadians(Math.toRadians(angle_degrees));
    }

    public double cos() 
    {
        return cos_angle;
    }

    public double sin() 
    {
        return sin_angle;
    }

    public double tan() 
    {
        if (Math.abs(cos_angle) < kEpsilon) {
            if (sin_angle >= 0.0)
            {
                return Double.POSITIVE_INFINITY;
            } 
            else 
            {
                return Double.NEGATIVE_INFINITY;
            }
        }
        return sin_angle / cos_angle;
    }

    public double getRadians() 
    {
        return Math.atan2(sin_angle, cos_angle);
    }

    public double getDegrees() 
    {
        return Math.toDegrees(getRadians());
    }

    /**
     * We can rotate this Rotation2d by adding together the effects of it and another rotation.
     *
     * @param other The other rotation. See: https://en.wikipedia.org/wiki/Rotation_matrix
     * @return This rotation rotated by other.
     */
    public Rotation2d rotateBy(final Rotation2d other) 
    {
        return new Rotation2d(cos_angle * other.cos_angle - sin_angle * other.sin_angle,
                cos_angle * other.sin_angle + sin_angle * other.cos_angle, true);
    }

    public Rotation2d normal() 
    {
        return new Rotation2d(-sin_angle, cos_angle, false);
    }

    /**
     * The inverse of a Rotation2d "undoes" the effect of this rotation.
     *
     * @return The opposite of this rotation.
     */
    public Rotation2d inverse() 
    {
        return new Rotation2d(cos_angle, -sin_angle, false);
    }

    @Override
    public Rotation2d interpolate(Rotation2d other, double x) 
    {
        if (x <= 0) 
        {
            return new Rotation2d(this);
        }
        else if (x >= 1) 
        {
            return new Rotation2d(other);
        }

        double angle_diff = inverse().rotateBy(other).getRadians();
        return this.rotateBy(Rotation2d.fromRadians(angle_diff * x));
    }
}