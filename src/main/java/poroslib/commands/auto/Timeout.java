package poroslib.commands.auto;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This command function as a delay command.
 * Since timer.delay doesn't work in command groups,
 * I created an empty command with only a timeout
 *
 *
 */
public class Timeout extends Command {

	/**
	 * Creates a Timeout command and specified its timeout
	 *
	 * @param timeout
	 */
    public Timeout(double timeout)
    {
        super("Timeout" , timeout);
    }

    /**
     * Command ends when the timeout ended
     *
     */
    protected boolean isFinished()
    {
        if(isTimedOut())
        {
        	return true;
        }
        else
        {
        	return false;
        }
    }
}
