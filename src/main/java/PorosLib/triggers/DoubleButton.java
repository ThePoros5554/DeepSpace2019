package PorosLib.triggers;

import edu.wpi.first.wpilibj.buttons.Trigger;

public class DoubleButton extends Trigger
{
	private Trigger buttonOne;
	private Trigger buttonTwo;
		
	private boolean asOne;
	
	public DoubleButton(Trigger buttonOne, Trigger buttonTwo) 
	{
		this.buttonOne = buttonOne;
		this.buttonTwo = buttonTwo;
		
		this.asOne = true;
	}
	
	public DoubleButton(Trigger buttonOne, Trigger buttonTwo, boolean asOne) 
	{
		this.buttonOne = buttonOne;
		this.buttonTwo = buttonTwo;
		
		this.asOne = asOne;
	}

	@Override
	public boolean get() 
	{
		if(asOne)
		{
			return (this.buttonOne.get() && this.buttonTwo.get());
		}
		else
		{
			return (this.buttonOne.get() || this.buttonTwo.get());
		}
	}

}
