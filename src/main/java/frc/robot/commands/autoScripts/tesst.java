package frc.robot.commands.autoScripts;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.VisionAlignment;
import frc.robot.commands.drive.FollowPath;
import frc.robot.commands.elevator.AdjustElevator;
import frc.robot.commands.wrist.WristDownStart;
import poroslib.util.Path;

public class tesst extends CommandGroup
{
    public tesst()
    {
        Path rp = new Path(RobotMap.RAMPTORIGHTROCKET + ".left");
        Path lp = new Path(RobotMap.RAMPTORIGHTROCKET + ".right");

        addParallel(new WristDownStart(1));
        addParallel(new AdjustElevator(5100 ,2.5));
        addSequential(new FollowPath(Robot.drivetrain, rp, lp, 0.0045, 0.0001, 0.005, 0, false));
        addSequential(new VisionAlignment(0, false, 0.4));
        addSequential(new VisionAlignment(0.195, false, 3));
        // addSequential(new AdjustElevator(-4000));


    }
}