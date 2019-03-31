package frc.robot.commands.autoScripts;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.InitHatchCollectMode;
import frc.robot.commands.VisionAlignment;
import frc.robot.commands.drive.FollowPath;
import frc.robot.commands.drive.ResetNavx;
import frc.robot.commands.elevator.AdjustElevator;
import frc.robot.commands.wrist.WristDownStart;
import poroslib.commands.auto.Timeout;
import poroslib.util.Path;

public class RampToLeftRocket extends CommandGroup
{
    public RampToLeftRocket()
    {
        Path rr1lp = new Path(RobotMap.RAMPTOLEFTROCKET + ".left");
        Path rr1rp = new Path(RobotMap.RAMPTOLEFTROCKET + ".right");

        addParallel(new WristDownStart(2));
        addParallel(new AdjustElevator(5100 ,2.5));
        addSequential(new FollowPath(Robot.drivetrain, rr1rp, rr1lp, 0.001, 0.08, 0.005, 0.05, false));
        addSequential(new VisionAlignment(0.3, false, 1.5, 0.012));
        addParallel(new AdjustElevator(-4000));
        addSequential(new ResetNavx());

        Path rbrp = new Path(RobotMap.LEFTROCKETBACK + ".left");
        Path rblp = new Path(RobotMap.LEFTROCKETBACK + ".right");
        addSequential(new FollowPath(Robot.drivetrain, rblp, rbrp, 0.001, 0.08, 0.007, 0.05, true));
        addSequential(new ResetNavx());
        addSequential(new ResetNavx());
        addSequential(new ResetNavx());
        addSequential(new ResetNavx());
        addSequential(new ResetNavx());

        addParallel(new InitHatchCollectMode());
        Path rflp = new Path(RobotMap.LEFTROCKETOFEEDER + ".left");
        Path rfrp = new Path(RobotMap.LEFTROCKETOFEEDER + ".right");
        addSequential(new FollowPath(Robot.drivetrain, rfrp, rflp, 0.001, 0.08, 0.009, 0.005, false));
        addSequential(new ResetNavx());
        addSequential(new VisionAlignment(0.2, false, 2, 0.012));
        addParallel(new AdjustElevator(4000));
        addSequential(new ResetNavx());



    }
}