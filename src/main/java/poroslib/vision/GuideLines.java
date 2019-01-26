package poroslib.vision;

import org.opencv.core.Scalar;

/**
 * This class holds 4 coordinates that represents guide lines and the ability to close and open the guide lines
 * with a limitation.
 *
 */
public class GuideLines
{
	private int xLeft;
	private int xRight;
	private int yUp;
	private int yDown;
	private Scalar color;
	private int thickness;

	private int maxRange;
	private int minRange;

	/**
	 * Creates an object that holds the characteristics of a guide line
	 *
	 * @param xLeft The left x coordinate of the guide line
	 * @param xRight The right x coordinate of the guide line
	 * @param yUp The Upper y coordinate of the guide line
	 * @param yDown The Down y coordinate of the guide line
	 * @param color The guide line' color
	 * @param thickness The guide line's thickness
	 */
	public GuideLines(int xLeft , int xRight , int yUp , int yDown , Scalar color, int thickness)
	{
		this.xLeft = xLeft;
		this.xRight = xRight;
		this.yUp = yUp;
		this.yDown = yDown;
		this.color = color;
		this.thickness = thickness;
	}


	/**
	 * Narrows the distance between the two lines
	 *
	 * @param narrow The pixels the distance will be narrowed
	 */
	public void NarrowWidth(int narrow)
	{
		if(!(this.GetLeftX()+this.minRange >= this.GetRightX()))
		{
			xLeft = xLeft+narrow;
			xRight = xRight-narrow;
		}
	}

	/**
	 * Dilates the distance between the two lines
	 *
	 * @param dilate The pixels the distance will be dilated
	 */
	public void DialateWidth(int dilate)
	{
		if(!(Math.abs(this.xLeft - this.xRight) >= this.maxRange))
		{
			xLeft = xLeft-dilate;
			xRight = xRight+dilate;
		}
	}

	/**
	 * Sets The maximum and minimum distance the guide lines can get to each other
	 *
	 * @param minDis The maximum distance  between the guide lines
	 * @param maxDis The minimum distance between the guide lines
	 */
	public void SetBoundries(int minDis , int maxDis)
	{
		SetRange(minDis , maxDis);
	}

	/**
	 * Sets the maximum distance between the guide lines
	 *
	 * @param range The maximum distance
	 */
	public void MaxRange(int range)
	{
		SetRange(this.minRange, range);
	}

	/**
	 * Sets the minimum distance between the guide lines
	 *
	 * @param range The minimum distance
	 */
	public void MinRange(int range)
	{
		SetRange(range, this.maxRange);
	}

	/**
	 * Sets The maximum and minimum distance the guide lines can get to each other
	 *
	 * @param minDis The maximum distance  between the guide lines
	 * @param maxDis The minimum distance between the guide lines
	 */
	private void SetRange(int minDis, int maxDis)
	{
		this.minRange = minDis;
		this.maxRange = maxDis;
	}

	/**
	 * Gets the real distance between the camera and the object in between the guide lines according to the object's width and the camera's focal length
	 * using triangle similarity
	 *
	 * @param FocalLength The cameras focal length
	 * @param objectwidth The real object's width
	 * @return The distance from
	 */
	public double GetDistance(double FocalLength, double objectwidth)
	{
		int pixDistance = Math.abs(this.xLeft - this.xRight);

		return (objectwidth*FocalLength)/pixDistance;
	}

	/**
	 * Gets the distance in pixels between the two guide lines
	 *
	 * @return The distance in pixels between the two guide lines
	 */
	public int PixelDistance()
	{
		return Math.abs(this.xLeft - this.xRight);
	}

	/**
	 *Gets the left x coordinate of the guide line
	 *
	 * @return The left x coordinate of the guide line
	 */
	public int GetLeftX()
	{
		return xLeft;
	}

	/**
	 *Gets the right x coordinate of the guide line
	 *
	 * @return The right x coordinate of the guide line
	 */
	public int GetRightX()
	{
		return xRight;
	}

	/**
	 *Gets the upper y coordinate of the guide line
	 *
	 * @return The upper y coordinate of the guide line
	 */
	public int GetUpY()
	{
		return yUp;
	}

	/**
	 *Gets the lower y coordinate of the guide line
	 *
	 * @return The lower y coordinate of the guide line
	 */
	public int GetDownY()
	{
		return yDown;
	}

	/**
	 *Gets the guide line's thickness
	 *
	 * @return The guide line's thickness
	 */
	public int GetThickness()
	{
		return thickness;
	}

	/**
	 *Gets the guide line's color
	 *
	 * @return The guide line's color
	 */
	public Scalar GetColor()
	{
		return color;
	}

}
