package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMonitor;

public class UpdateRobotState extends Command
{
    private RobotMonitor robotMonitor;

    private double prevLastLeftEncCountCm;
    private double prevLastRightEncCountCm;


    public UpdateRobotState()
    {
        robotMonitor = RobotMonitor.getRobotMonitor();
    }

    @Override
    protected void initialize()
    {
        prevLastLeftEncCountCm = Robot.drivetrain.getLeftPositionInCm();
        prevLastRightEncCountCm = Robot.drivetrain.getRightPositionInCm();
    }
  
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute()
    {
      
        double leftPositionInCm = Robot.drivetrain.getLeftPositionInCm();
        double rightPositionInCm = Robot.drivetrain.getRightPositionInCm();

        robotMonitor.addPositionObservation(Timer.getFPGATimestamp(), (leftPositionInCm - prevLastLeftEncCountCm) , (rightPositionInCm - prevLastRightEncCountCm), 
                    Robot.drivetrain.getHeading());

        prevLastLeftEncCountCm = leftPositionInCm;
        prevLastRightEncCountCm = rightPositionInCm;

        SmartDashboard.putNumber("Distance left: ", leftPositionInCm);
        SmartDashboard.putNumber("Distance right: ", rightPositionInCm);

        // if(robotMonitor.getLastVisionReport().getKey() != Robot.lime.getLastNTUpdateTime())
        // {
        //     robotMonitor.addVisionReport(Robot.lime.getLastNTUpdateTime());
        // }
    }
  
    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished()
    {
      return false;
    }
  
    // Called once after isFinished returns true
    @Override
    protected void end()
    {
  
    }
  
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted()
    {
      end();
    }
}