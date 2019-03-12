package frc.robot.commands.autoScripts;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.VisionAlignment;
import frc.robot.commands.drive.FollowPath;
import frc.robot.commands.elevator.AdjustElevator;
import frc.robot.commands.wrist.WristDownStart;
import poroslib.util.Path;

public class RampToLeftRocket extends CommandGroup
{
    public RampToLeftRocket()
    {
        Path rp = new Path(RobotMap.RAMPTOLEFTROCKET + ".left");
        Path lp = new Path(RobotMap.RAMPTOLEFTROCKET + ".right");

        addParallel(new WristDownStart(1));
        addParallel(new AdjustElevator(5100 ,2.5));
        addSequential(new FollowPath(Robot.drivetrain, rp, lp, 0.0045, 0.0001, 0.005, 0));
        addSequential(new VisionAlignment(0, false, 0.3));
        addSequential(new VisionAlignment(0.18, false, 1.5));
        addParallel(new AdjustElevator(-4000));

    }
}