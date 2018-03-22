package stochasticSimulation;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.InputMismatchException;

public interface Simulable {
	
	/**Updates the current position of the particle
	 * 
	 * @param dt : timestep in milliseconds
	 */
	public void updatePos(int dt) throws InputMismatchException;
	
	
	/**returns current position
	 * 
	 * @return : current position in cartesian coordinates
	 */
	public Point getPos();
	
	
	/**Returns full path taken by the simulable object
	 * 
	 * @return : all points taken as an ArrayList
	 */
	public ArrayList<Point> getPath();
	
	/**Returns to default position and wipes path
	 * 
	 */
	public void reset();
	
	/**Calculates probability density at the given point
	 * 
	 * @param newDensities : all existing probability densities to update
	 * @param x : x coordinate
	 * @param y : y coordinate
	 * @param t : time
	 * @param probDensities : all existing probability densities to check
	 */
	public void changeDensities(int x, int y, double t, ArrayList<ArrayList<Double>> probDensities, ArrayList<ArrayList<Double>> newDensities) throws InputMismatchException;
	
	
	/**Calculates probability density at the given point
	 * 
	 * @param newDensities : all existing probability densities to update
	 * @param position : position in cartesian coordinates
	 * @param t : current time
	 * @param probDensities : all existing probability densities to check
	 */
	public void changeDensities(Point position, double t, ArrayList<ArrayList<Double>> probDensities, ArrayList<ArrayList<Double>> newDensities) throws InputMismatchException;
	
	/**Generates initial particle density
	 * 
	 * @param length : width of the window
	 * @param height : height of the window
	 * @return 2D arraylist of initial densities
	 */
	public  ArrayList<ArrayList<Double>> initDensity(int length, int height);
	
	/**Gets stored colour for display
	 * 
	 * @return: the colour used to identify the particle
	 */
	public Color getColor();
}
