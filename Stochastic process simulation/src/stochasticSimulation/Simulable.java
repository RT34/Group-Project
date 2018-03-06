package stochasticSimulation;

import java.awt.Color;
import java.awt.Point;
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
	public Point getPos();
	
	
	/**Returns full path taken by the simulable object
	 * 
	 * @return: all points taken as an ArrayList
	 */
	public ArrayList<Point> getPath();
	
	/**Returns to default position and wipes path
	 * 
	 */
	public void reset();
	
	/**Calculates probability density at the given point
	 * 
	 * @param x: x coordinate
	 * @param y: y coordinate
	 * @param t: time
	 * @param points: all existing probability densities to check
	 * @return: probability density at given point
	 */
	public double getDensity(double x, double y, double t, ArrayList<ArrayList<Point>> points);
	
	
	/**Calculates probability density at the given point
	 * 
	 * @param position: position in cartesian coordinates
	 * @param t: current time
	 * @param points: all existing probability densities to check
	 * @return: probability density at given point
	 */
	public double getDensity(Point position, double t, ArrayList<ArrayList<Point>> points);
	
	
	/**Gets stored colour for display
	 * 
	 * @return: the colour used to identify the particle
	 */
	public Color getColor();
}
