package stochasticSimulation;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.DoubleStream;

public class BrownianParticle implements Simulable {
	Point currentCoordinates;
	ArrayList<Point> pastCoordinates;
	Random rand = new Random();
	Color color;
	ArrayList<Integer> stepChange;
	ArrayList<Double> probabilities;
	double cumProb;
	
	/**Calculates the probabilities of different steps and stores them in stepChange and probabilitise
	 * 
	 * @param dt: timestep to be used
	 */
	void calcProb(int dt) {
		int dx = 1; //Currently integer due to pixels, may be changed if necessary
		int numCalcs = 1;
		cumProb = 0;
		probabilities.add(1d/Math.sqrt(2 * Math.PI * dt)); //probability at dx = 0, i.e. that which the particle remains stationary
		cumProb += probabilities.get(0);
		stepChange.add(0);
		while (cumProb <0.975) {
			double prob = 2/Math.sqrt(2 * Math.PI * dt) * Math.exp(-(dx * dx)/(double)dt); //2 used due to symmetry in transition probabilities for positive or negative step.
			if (prob == 0) { //Prevents adding multiple zero probabilities to end of list
				break;
			}
			stepChange.add(dx++); //stores change in x by same index as probabilities.
			cumProb += prob;
			probabilities.add(prob);
			numCalcs++;
			
		}
		assert(cumProb <=1) : "Total probability cannot be more than one, how did this even happen...";
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
	 * @param x: x coordinate
	 * @param y: y coordinate
	 * @param past: predefined path
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
			for (int jjj = 0; jjj < probabilities.size(); jjj++) { //Checks for each cumulative probability if this is the path the particle is taking
				checkProb += probabilities.get(jjj);
				if (result < checkProb) { //Essentially, we're finding this by looking at which gap the random number falls into
					step = stepChange.get(jjj);
					break; //Exits once the step is found
				}
			}
			if (iii == 0) {
				currentCoordinates.x += (rand.nextDouble() <= 0.5) ? -step: step;
			}
			else {
				currentCoordinates.y +=(rand.nextDouble()  <= 0.5) ? -step: step;
			}
		}
		//Necessary to minimise lag for higher numbers of particles at lower values of dt
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
	public Point getPos() {
		return currentCoordinates;
	}


	@Override
	public ArrayList<Point> getPath() {
		ArrayList<Point> results = (ArrayList<Point>)pastCoordinates.clone();
		results.add((Point)currentCoordinates.clone());
		return results;
	}


	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public double getDensity(int x, int y, double t, ArrayList<ArrayList<Double>> points) {

		double density = 0;
		int numCalcs = 0;
		if (x == points.size() && y == points.get(0).size()) {
			double asdf = 3;
		}
		for (int iii = -stepChange.get(stepChange.size() - 1); iii < stepChange.size(); iii++) {
			for (int jjj = -stepChange.get(stepChange.size() - 1); jjj < stepChange.size(); jjj++ ) {
				//ignores undisplayed positions
				if (x + iii < 0 || x + iii > points.size()/points.get(0).size() || y + jjj < 0 || y + jjj > points.get(0).size()) { 
					continue;
				}
				if (iii == 0 && jjj == 0) { //Odds of remaining where it is
					density += (100000 * probabilities.get(0)) * points.get(iii).get(jjj); 
				}
				if (points.get(x + iii).get(y + jjj) == 1.0) {
					System.out.println("Thank fuck for that");
				}
				else {
					density += 1000000000 * probabilities.get(Math.abs(iii)) * points.get(x + iii).get(y + jjj) / 2 *  probabilities.get(jjj) * points.get(x - iii).get(y - jjj) /2;
					numCalcs++;
				}
			}
		}
		if (density != 0.0) {
			int asdf = 2;
			System.out.println(x);
			System.out.println(y);
		}
		return density;
	}

	@Override
	public double getDensity(Point position, double t, ArrayList<ArrayList<Double>> points) {
		return getDensity(position.x, position.y, t, points);
	}
	
	@Override
	public ArrayList<ArrayList<Double>> initDensity(int x, int y) {
		//density is zero everywhere but at the centre
		int xStep = x/20; //Dividing by stepsize, stepsize must be even
		int yStep = y/20;
		ArrayList<ArrayList<Double>> densities = new ArrayList<ArrayList<Double>>();
		for (int iii = 0; iii < xStep; iii++) {
			densities.add(new ArrayList<Double>());
			for (int jjj = 0; jjj < yStep; jjj++) {
				if (iii == xStep/2 && jjj == yStep/2) {
					densities.get(iii).add(1.0);
				}
				else {
					densities.get(iii).add(0.0);
				}
			}
		}
		return densities;
	}
}
