package frc.robot.commands.autoScripts;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.InitHatchHookLowMode;
import frc.robot.commands.InitHatchLowMode;
import frc.robot.commands.VisionAlignment;
import frc.robot.commands.drive.FollowPath;
import frc.robot.commands.elevator.AdjustElevator;
import frc.robot.commands.wrist.WristDownStart;
import poroslib.commands.auto.Timeout;
import poroslib.util.Path;

public class RampToCenterCargo extends CommandGroup 
{
    public RampToCenterCargo()
    {
        Path rp = new Path(RobotMap.RAMPTOCENTERCARGO + ".left");
        Path lp = new Path(RobotMap.RAMPTOCENTERCARGO + ".right");

        addParallel(new WristDownStart(1));
        addParallel(new AdjustElevator(3500 ,1.5));
        addSequential(new FollowPath(Robot.drivetrain, rp, lp, 0.0045, 0.0001, 0.02, 0));
        addSequential(new VisionAlignment(0, false, 2, 0.009));
        addSequential(new VisionAlignment(0.15, false, 5, 0.009));
        addParallel(new AdjustElevator(-3000));
    }
}