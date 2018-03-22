package stochasticSimulation;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.DoubleStream;

public class BrownianParticle implements Simulable {
	Point currentCoordinates;
	ArrayList<Point> pastCoordinates;
	Random rand = new Random(); //Random number generator
	Color color;
	ArrayList<Integer> stepChange;
	ArrayList<Double> probabilities;
	double cumProb;
	
	/**Calculates the probabilities of different steps and stores them in stepChange and probabilitise
	 * 
	 * @param dt: timestep to be used
	 */
	void calcProb(int timestep) {
		int dx = 1; //Currently integer due to pixels, may be changed if necessary
		int numCalcs = 1;
		double dt = timestep;
		cumProb = 0;
		probabilities.add(1.0/Math.sqrt(2.0 * Math.PI * dt)); //probability at dx = 0, i.e. that which the particle remains stationary
		stepChange.add(0);
		//Commented out section is used for the quick version of probability density. Left like this as it was the state during most of the data gathering
		while (cumProb <0.975 ) { //&& numCalcs < 25) {
			double prob = 2.0/Math.sqrt(2.0 * Math.PI * dt) * Math.exp(-(dx * dx)/(double)dt); //2 used due to symmetry in transition probabilities for positive or negative step.
			if (prob == 0) { //Prevents adding multiple zero probabilities to end of list
				break;
			}
			stepChange.add(dx++); //stores change in x by same index as probabilities.
			cumProb += prob;
			probabilities.add(prob);
			numCalcs++;
			
		}
		assert(cumProb <=1) : "Total probability cannot be more than one";
	}
	/**Constructor, takes coordinates and generates a random colour for this particle
	 * 
	 * @param x: x coordinate
	 * @param y: y coordinate
	 * @param dt: timestep for UpdatePos
	 */
	BrownianParticle(int x, int y, int dt) {
		currentCoordinates = new Point(x, y);
		color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		pastCoordinates = new ArrayList<Point>();
		probabilities = new ArrayList<Double>();
		stepChange = new ArrayList<Integer>();
		calcProb(dt);
	}
	
	/**Constructor, takes coordinates and also allows the addition of a complete path if this is necesary
	 * 
	 * @param x : x coordinate
	 * @param y : y coordinate
	 * @param past : predefined path
	 * @param dt : timestep
	 */
	BrownianParticle (int x, int y, ArrayList<Point>past, int dt) {
		currentCoordinates = new Point(x,  y);
		pastCoordinates = past;
		color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		probabilities = new ArrayList<Double>();
		stepChange = new ArrayList<Integer>();
		calcProb(dt);
	}
	
	/**Default constructor, sets the particle to the origin
	 * 
	 * @param dt: timestep for udpatePos
	 */
	BrownianParticle(int dt) {
		currentCoordinates = new Point(0,0);
		pastCoordinates = new ArrayList<Point>();
		color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		probabilities = new ArrayList<Double>();
		stepChange = new ArrayList<Integer>();
		calcProb (dt);
	}
	
	/**Calculates the change in position of the particle using the Weiner process.
	 * 
	 * @param dt: timestep in milliseconds
	 */
	public void updatePos(int dt) {
		pastCoordinates.add((Point)currentCoordinates.clone());
		for (int iii = 0; iii < 2; iii++) { //Ensures this is done once for x and y coordinates
			double result = rand.nextDouble();
			while (result >cumProb) { //Ensures we're looking for a probability within a region that we have calculated probability for
				result = rand.nextDouble();
			}
			double checkProb = 0;
			int step = 0;
			for (int jjj = 1; jjj < probabilities.size(); jjj++) { //Checks for each cumulative probability if this is the path the particle is taking
				checkProb += probabilities.get(jjj);
				if (result < checkProb) { //Essentially, we're finding this by looking at which gap the random number falls into
					step = stepChange.get(jjj);
					break; //Exits once the step is found
				}
			}
			//Determines positive or negative step with equal probability
			if (iii == 0) {
				currentCoordinates.x += (rand.nextDouble() <= 0.5) ? -step: step;
			}
			else {
				currentCoordinates.y +=(rand.nextDouble()  <= 0.5) ? -step: step;
			}
		}
		//Necessary to minimise lag for higher numbers of particles
		if (pastCoordinates.size() > 100) {
			pastCoordinates.remove(0);
		}
		
	}
	
	/**
	 * Resets to the origin with the path cleared
	 */
	public void reset() {
		currentCoordinates.x = 0;
		currentCoordinates.y = 0;
		pastCoordinates = new ArrayList<Point>();
	}
	
	/**Returns current coordinates as required by Simulable interface
	 * 
	 * @return the current coordinates
	 */
	public Point getPos() {
		return currentCoordinates;
	}

	/** returns the list of past coordinates
	 * 
	 * @return past coordinates as an ArrayList
	 */
	@Override
	public ArrayList<Point> getPath() {
		ArrayList<Point> results = (ArrayList<Point>)pastCoordinates.clone();
		results.add((Point)currentCoordinates.clone());
		return results;
	}


	/**Returns the particle's color as required by Simulable interface
	 * 
	 * @return the particle's colour
	 */
	@Override
	public Color getColor() {
		return color;
	}

	/**Updates probability densities based on this point's allowed transitions
	 * 
	 * @param x : x coordinate
	 * @param y : y coordinate
	 * @param t : timestep (unused)
	 * @param probDensities : previously calculated probability densities (SHOULD NOT BE CHANGED)
	 * @param newDensities : new probability densities, to be updated based on probability of step transitions
	 */
	@Override
	public void changeDensities(int x, int y, double t, ArrayList<ArrayList<Double>> points, ArrayList<ArrayList<Double>> newDensities) {
		double prob = points.get(x).get(y);
		assert (prob != 0);
		for (int iii = -stepChange.size() + 1; iii < stepChange.size(); iii++) {
			for (int jjj = -stepChange.size() + 1; jjj < stepChange.size(); jjj++) {
				//ignores undisplayed positions
				if (x + iii < 0 || x + iii >= points.size() || y + jjj < 0 || y + jjj >= points.get(0).size()) { 
					continue;
				}
				if (iii == 0 && jjj == 0) {
					continue;
				}
				else {
					double probChange = probabilities.get(Math.abs(iii)) * probabilities.get(Math.abs(jjj)) * prob / (((iii == 0) ? 1 : 2) * ((jjj == 0) ? 1: 2));
					newDensities.get(iii + x).set(jjj + y, newDensities.get(iii + x).get(jjj + y) + probChange);
					newDensities.get(x).set(y, newDensities.get(x).get(y) - probChange);
				}
			}
		}
	}

	/**Updates probability densities based on this point's allowed transitions, calls the other function
	 * 
	 * @parm position : the current coordinates
	 * @param t : timestep (unused)
	 * @param probDensities : previously calculated probability densities (SHOULD NOT BE CHANGED)
	 * @param newDensities : new probability densities, to be updated based on probability of step transitions
	 */
	@Override
	public void changeDensities(Point position, double t, ArrayList<ArrayList<Double>> points, ArrayList<ArrayList<Double>> newDensities) {
		changeDensities(position.x, position.y, t, points, newDensities);
	}
	
	/**Used to define the initial state of Probability Density
	 * @param length: width of the window
	 * @param height: height of the window
	 * @return: 2D ArrayList of probability densities
	 */
	@Override
	public ArrayList<ArrayList<Double>> initDensity(int x, int y) {
		//density is zero everywhere but at the centre
		int xStep = x/6; //Dividing by stepsize, stepsize must be even
		int yStep = y/6;
		ArrayList<ArrayList<Double>> densities = new ArrayList<ArrayList<Double>>();
		for (int iii = 0; iii < xStep; iii++) {
			densities.add(new ArrayList<Double>());
			for (int jjj = 0; jjj < yStep; jjj++) {
				if ((iii == xStep/2) && (jjj == yStep/2)) {
					densities.get(iii).add(1000000000000.0);
				}
				else {
					densities.get(iii).add(0.0);
				}
			}
		}
		return densities;
	}
}
