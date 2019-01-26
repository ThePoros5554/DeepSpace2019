package poroslib.vision;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSource;
import edu.wpi.first.cameraserver.CameraServer;

/**
 *
 * This class represents an output stream that will be presented
 * on the dashboard to the driver.
 * The class contains functions that can draw shapes to a mat.
 *
 */
public class VideoBox
{
	private CvSource outputStream;

	/**
	 * Creates a video box object that represents an output stream that can show Mats to the user
	 *
	 * @param width Resolution width
	 * @param height Resolution height
	 * @param name The name of the output stream
	 */
	public VideoBox( String name, int width, int height)
	{
		outputStream = CameraServer.getInstance().putVideo(name, width, height);
	}

	/**
	 * Shows a frame on the output stream
	 * @param feed The frame
	 */
	public void stream(Mat feed)
	{
		outputStream.putFrame(feed);
	}

	/**
	 * Draws guide lines to a mat
	 *
	 * @param mat The mat the guide lines will be drawn to
	 * @param gl The guide line object that will be drawn
	 * @return The drawn mat
	 */
	public Mat DrawGuideLines(Mat mat , GuideLines gl)
	{
		Imgproc.line(mat, new Point(gl.GetLeftX(),gl.GetUpY()) , new Point(gl.GetLeftX(),gl.GetDownY()),gl.GetColor(),gl.GetThickness());
		Imgproc.line(mat, new Point(gl.GetRightX(),gl.GetUpY()) , new Point(gl.GetRightX(),gl.GetDownY()),gl.GetColor(),gl.GetThickness());
		return mat;
	}

	/**
	 * Draws the upper bound of a guide lines to a mat
	 *
	 * @param mat The mat the bound will be drawn to
	 * @param gl The guide line object that his bound will be drawn
	 * @return The drawn mat
	 */
	public Mat DrawUpperGL(Mat mat , GuideLines gl)
	{
		Imgproc.line(mat , new Point(gl.GetLeftX() , gl.GetUpY()) , new Point(gl.GetRightX() , gl.GetUpY()) , gl.GetColor() ,  gl.GetThickness());
		return mat;
	}

	/**
	 * Draws the lower bound of a guide lines to a mat
	 *
	 * @param mat The mat the bound will be drawn to
	 * @param gl The guide line object that his bound will be drawn
	 * @return The drawn mat
	 */
	public Mat DrawLowerGL(Mat mat , GuideLines gl)
	{
		Imgproc.line(mat , new Point(gl.GetLeftX() , gl.GetDownY()) , new Point(gl.GetRightX() , gl.GetDownY()) , gl.GetColor() ,  gl.GetThickness());
		return mat;
	}

	/**
	 * Draw a circle to a mat
	 *
	 * @param mat The mat the circle will be drawn to
	 * @param center The center point of the circle
	 * @param radius The circle's radius
	 * @param color The circle's color
	 * @param thickness The circle's line thickness
	 * @return The drawn mat
	 */
	public Mat DrawCircle(Mat mat , Point center , int radius , Scalar color , int thickness)
	{
		Imgproc.circle(mat, center, radius, color , thickness);
		return mat;
	}

	/**
	 * Draws a rectangle to a mat
	 *
	 * @param mat The mat the rectangle will be drawn to
	 * @param ul The upper left corner of the rectangle
	 * @param dr The down right corner of the rectangle
	 * @param color The rectangle's color
	 * @param thickness The rectangle's line thickness
	 * @return The drawn mat
	 */
	public Mat DrawRectangle(Mat mat, Point ul, Point dr, Scalar color , int thickness)
	{
		Imgproc.rectangle(mat, ul, dr, color, thickness);
		return mat;
	}

	/**
	 * Rotates a frame a certain amount of degrees
	 *
	 * @param mat The mat to rotate
	 * @param angle The angle that the mat will be rotated
	 * @return The rotated mat
	 */
	public Mat RotateFrame(Mat mat , double angle)
	{
		Point pt = new Point(mat.cols()/2 , mat.rows()/2);
		Imgproc.warpAffine(mat, mat, Imgproc.getRotationMatrix2D(pt, angle, 1.0), new Size(mat.cols(),mat.rows()));
		return mat;
	}

}
