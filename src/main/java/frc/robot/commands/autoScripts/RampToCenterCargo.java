package frc.robot.commands.autoScripts;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.VisionAlignment;
import frc.robot.commands.drive.FollowPath;
import frc.robot.commands.drive.ResetNavx;
import frc.robot.commands.elevator.AdjustElevator;
import frc.robot.commands.wrist.WristDownStart;
import poroslib.util.Path;

public class RampToCenterCargo extends CommandGroup 
{
    public RampToCenterCargo()
    {
        // Path rp = new Path(RobotMap.RAMPTOCENTERCARGO + ".left");
        // Path lp = new Path(RobotMap.RAMPTOCENTERCARGO + ".right");

        Path rp = new Path(RobotMap.RAMPTOCENTERCARGO + ".left");
        Path lp = new Path(RobotMap.RAMPTOCENTERCARGO + ".right");

      //  addParallel(new WristDownStart(1));
       // addParallel(new AdjustElevator(3500 ,1.5));
    // addSequential(new FollowPath(Robot.drivetrain, rp, lp, 0.004, 0.0001, 0.01, 0, false));
       //addSequential(new FollowPath(Robot.drivetrain, rp, lp, 0.0045, 0.0001, 0.02, 0));
       // addSequential(new VisionAlignment(0, false, 1, 0.009));
        //addSequential(new VisionAlignment(0.2, false, 5, 0.009));
        // addSequential(new AdjustElevator(-4000));

        addParallel(new WristDownStart(1.2));
        addParallel(new AdjustElevator(5100 ,2.5));
        addSequential(new FollowPath(Robot.drivetrain, rp, lp, 0.001, 0.08, 0.005, 0.05, false));
        addSequential(new VisionAlignment(0.3, false, 1.5, 0.012));
        addParallel(new AdjustElevator(-4000));
    }
}