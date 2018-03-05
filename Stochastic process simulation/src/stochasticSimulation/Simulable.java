package stochasticSimulation;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public interface Simulable {
	
	/**Updates the current position of the particle
	 * 
	 * @param dt: timestep in milliseconds
	 */
	public void updatePos(int dt);
	
	
	/**returns current position
	 * 
	 * @return: current position in cartesian coordinates
	 */
	public Point2D.Double getPos();
	
	
	/**Returns full path taken by the simulable object
	 * 
	 * @return: all points taken as an ArrayList
	 */
	public ArrayList<Point2D.Double> getPath();
	
	/**Returns to default position and wipes path
	 * 
	 */
	public void reset();
	
	/**Calculates probability density at the given point
	 * 
	 * @param x: x coordinate
	 * @param y: y coordinate
	 * @param t: time
	 * @return: probability density at given point
	 */
	public double getDensity(double x, double y, double t);
	
	
	/**Calculates probability density at the given point
	 * 
	 * @param position: position in cartesian coordinates
	 * @param t: current time
	 * @return: probability density at given point
	 */
	public double getDensity(Point2D position, double t);
	
	
	/**Gets stored colour for display
	 * 
	 * @return: the colour used to identify the particle
	 */
	public Color getColor();
}
