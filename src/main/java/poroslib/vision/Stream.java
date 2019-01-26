package poroslib.vision;

import org.opencv.core.Mat;

public class Stream 
{
	private long report;
	private Mat image;
	
	public Stream(long report, Mat image)
	{
		this.report = report;
		this.image = image;
	}
	
	public long GetReport()
	{
		return this.report;
	}
	
	public Mat GetImage()
	{
		return this.image;
	}
	
	public void SetReport(long report)
	{
		this.report = report;
	}
	
	public void SetImage(Mat image)
	{
		this.image = image;
	}
	
}
