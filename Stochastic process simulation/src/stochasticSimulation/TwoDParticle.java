package stochasticSimulation;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

/**Class to simulate a 2-Dimensional free particle in a stochastic context
 * 
 * @author rbroo
 *
 */
public class TwoDParticle implements Simulable {
	Point currentCoordinates;
	ArrayList<Point> pastCoordinates;
	Random rand = new Random(); //Initialises random number generator
	Color colour;
	Point2D.Double p; //Point values for x and y axes
	Point maxSteps; //Number of allowed steps for x and y axes
	ArrayList<Point2D.Double> probabilities = new ArrayList<Point2D.Double>();
	ArrayList<Point> steps = new ArrayList<Point>();
	
	/**Standard constructor, currently places all particles at origin.
	 * 
	 * May customise particle starting locations here, the commented out code spawns particles at three separate positions along the x-axis
	 */
	public TwoDParticle() {
		//int startPoint = rand.nextInt(3);
		this.currentCoordinates = new Point(0,0);//(startPoint == 0) ? -30 : (startPoint == 1) ? 0 : 30,0);
		this.colour = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		//Change max dq below
		maxSteps = new Point(6,6);
		//Change p below
		p = new Point2D.Double(0.4, 0.4);
		
		//Calculates transition probabilities in constructor to minimise calculations needed in each simulation step
		probabilities.add(new Point2D.Double(1, 1)); //Probability of remaining stationary
		for (int iii = 1; iii <= maxSteps.x; iii++) {
			probabilities.get(0).x -= p.x/iii; //Reduces probability of remaining stationary by stepping probability
			probabilities.add(new Point2D.Double(0, 0));
			probabilities.get(iii).x = (p.x/iii);
			steps.add(new Point(iii, 0)); //In case stepsizes other than one are to be used
		}
		for (int jjj = 1; jjj <= maxSteps.y; jjj++) {
			probabilities.get(0).y -= p.y/jjj; //Reduces probability of remaining stationary by stepping probability
			//The following if statements ensure the number of steps and probabilities is properly represented if y >x
			if (jjj >= probabilities.size()) {
				probabilities.add(new Point2D.Double(0, 0));
			}
			probabilities.get(jjj).y = p.y/jjj;
			if (jjj > steps.size()) {
				steps.add(new Point(0,jjj));
			}
			else {
				steps.get(jjj - 1).y = jjj;
			}
		}
		//Ensures that the sums of all probability in a given direction is not greater than one
		while (probabilities.get(0).x <= 0) {
			probabilities.get(0).x += probabilities.get(maxSteps.x).getX();
			probabilities.get(maxSteps.x).x = 0;
			maxSteps.x -=1;
			System.out.println("Too many steps provided");
		}
		while (probabilities.get(0).y <= 0) {
			probabilities.get(0).y += probabilities.get(maxSteps.y).getY();
			probabilities.get(maxSteps.y).y = 0;
			maxSteps.y -=1;
			System.out.println("Too many steps provided");
		}
	}
	
	/**Updates the position in accordance with the demands of Simulable interface, using probabilities defined in constructor
	 * 
	 * @param dt: timestep as required by interface, unused
	 */
	@Override
	public void updatePos(int dt) {
		double cumProb = 0;
		double prob = rand.nextDouble();
		for (int iii = 0; iii < maxSteps.x ; iii++) {
			cumProb += probabilities.get(iii+1).getX();
			if (prob <= cumProb) {
				this.currentCoordinates.x += steps.get(iii).x;
				break;
			}
		}
		prob = rand.nextDouble();
		cumProb = 0;
		for (int iii = 0; iii < maxSteps.y; iii++) {
			cumProb += probabilities.get(iii+1).getY();
			if (prob <= cumProb) {
				this.currentCoordinates.y += steps.get(iii).y;
				break;
			}
		}
	}

	/**Returns position of the particle
	 * 
	 * @return: the position as a Point
	 */
	@Override
	public Point getPos() {
		return currentCoordinates;
	}

	/**Returns the path taken by the particle. This is not considered to be worth displaying for reasons of clarity and visibility
	 * 
	 * @return: ArrayList consisting of currentCoordinates
	 */
	@Override
	public ArrayList<Point> getPath() {
		ArrayList<Point> path = new ArrayList<Point>();
		path.add(currentCoordinates);
		return path;
	}

	/**Returns the particle to the origin
	 * 
	 */
	@Override
	public void reset() {
		this.currentCoordinates = new Point(0,0);
	}

	/**Updates probability densities based on this point's allowed transitions
	 * 
	 * @param x: x coordinate
	 * @param y: y coordinate
	 * @param t: timestep (unused)
	 * @param probDensities: previously calculated probability densities (SHOULD NOT BE CHANGED)
	 * @param newDensities: new probability densities, to be updated based on probability of step transitions
	 */
	@Override
	public void changeDensities(int x, int y, double t, ArrayList<ArrayList<Double>> probDensities,
			ArrayList<ArrayList<Double>> newDensities) {
		for (int iii = 0; iii  <= maxSteps.x; iii++) {
			double probChangeX = probabilities.get(iii).getX();
			for (int jjj = 0; jjj <= maxSteps.y; jjj++) {
				//Prevents cases where we are checking out of bounds regions. If iii xor jjj are zero, we still with to calculate the probability, but
				//if we evaluate steps.get(-1) we will go out of bounds, so short circuit evaluation of iii != 0 && accessing the array prevents this.
				//We evaluate steps of index -1 as there is no element in steps for a stepsize of zero. In retrospect, we should likely have just added a zero step
				//element, but at this stage it's too late to refactor appropriately.
				//However, if both iii and jjj are zero, there should be no change in probability density so this is also skipped.
				if ((iii != 0 && x + steps.get(iii-1).x >= newDensities.size()) || (jjj != 0 && y + steps.get(jjj-1).y >= newDensities.get(0).size()) || (iii == 0 && jjj == 0)) {
					continue;
				}
				double probChangeY = probabilities.get(jjj).getY() * probDensities.get(x).get(y);
				newDensities.get(x).set(y, newDensities.get(x).get(y) - probChangeX * probChangeY);
				newDensities.get(x + ((iii != 0) ? steps.get(iii-1).x : 0)).set(y + ((jjj != 0) ? steps.get(jjj-1).y : 0), 
						newDensities.get(x + ((iii != 0) ? steps.get(iii-1).x : 0)).get(y + ((jjj != 0) ? steps.get(jjj-1).y : 0)) + probChangeX * probChangeY);
			}
		}

	}
	
	/**Updates probability densities based on this point's allowed transitions, calls the other function
	 * 
	 * @param position: coordinates to check
	 * @param t: timestep (unused)
	 * @param probDensities: previously calculated probability densities (SHOULD NOT BE CHANGED)
	 * @param newDensities: new probability densities, to be updated based on probability of step transitions
	 */
	@Override
	public void changeDensities(Point position, double t, ArrayList<ArrayList<Double>> probDensities,
			ArrayList<ArrayList<Double>> newDensities) {
		this.changeDensities(position.x, position.y, t, probDensities, newDensities);

	}

	/**Used to define the initial state of Probability Density
	 * @param length: width of the window
	 * @param height: height of the window
	 * @return: 2D ArrayList of probability densities
	 */
	@Override
	public ArrayList<ArrayList<Double>> initDensity(int length, int height) {
		int xStep = length; //Dividing by stepsize, stepsize must be even
		int yStep = height;
		ArrayList<ArrayList<Double>> densities = new ArrayList<ArrayList<Double>>();
		for (int iii = 0; iii < xStep; iii++) {
			densities.add(new ArrayList<Double>());
			for (int jjj = 0; jjj < yStep; jjj++) {
				//Sets up checkered density as described in final report
				//if (iii >= xStep/2 - 200 && iii <= xStep/2 + 200 && jjj >= yStep/2 && jjj <= 3 * yStep/4 && iii %50 < 25 && jjj % 50 <25) {
				if (iii == xStep/2 && jjj == yStep/2) { //All density located at origin
					densities.get(iii).add(1000000000000.0); //Sufficiently large value to prevent early rounding to zero as a result of multiplying together small numbers
				}
				else {
					densities.get(iii).add(0.0);
				}
			}
		}
		return densities;
	}

	/**Fetches the colour to be used for the particle
	 * @return: the colour of the particle
	 */
	@Override
	public Color getColor() {
		return this.colour;
	}

}
