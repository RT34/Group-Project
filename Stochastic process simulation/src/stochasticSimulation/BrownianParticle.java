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
		System.out.println(dt);
		System.out.println(cumProb);
		stepChange.add(0);
		while (cumProb <0.975  && numCalcs < 300) { //Necessary so program doesn't run indefinitely
			double prob = 2/Math.sqrt(2 * Math.PI * dt) * Math.exp(-(dx * dx)/(double)dt); //2 used due to symmetry in transition probabilities for positive or negative step.
			stepChange.add(dx++); //stores change in x by same index as probabilities.
			cumProb += prob;
			probabilities.add(prob);
			System.out.println(probabilities.get(numCalcs));
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
	public double getDensity(double x, double y, double t, ArrayList<ArrayList<Point>> points) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDensity(Point position, double t, ArrayList<ArrayList<Point>> points) {
		// TODO Auto-generated method stub
		return 0;
	}
}
