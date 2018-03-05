package stochasticSimulation;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class BrownianParticle implements Simulable {
	Point2D.Double currentCoordinates;
	ArrayList<Point2D.Double> pastCoordinates;
	Random rand = new Random();
	Color color;
	
	/**Constructor, takes coordinates and generates a random colour for this particle
	 * 
	 * @param x: x coordinate
	 * @param y: y coordinate
	 */
	BrownianParticle(double x, double y) {
		currentCoordinates = new Point2D.Double(x, y);
		color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		pastCoordinates = new ArrayList<Point2D.Double>();
	}
	
	/**Constructor, takes coordinates and also allows the addition of a complete path if this is necesary
	 * 
	 * @param x: x coordinate
	 * @param y: y coordinate
	 * @param past: predefined path
	 */
	BrownianParticle (double x, double y, ArrayList<Point2D.Double>past ) {
		currentCoordinates = new Point2D.Double(x,  y);
		pastCoordinates = past;
		color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
	}
	
	/**Default constructor, sets the particle to the origin
	 * 
	 */
	BrownianParticle() {
		currentCoordinates = new Point2D.Double(0,0);
		pastCoordinates = new ArrayList<Point2D.Double>();
		color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
	}
	
	/**Calculates the change in position of the particle using the Weiner process.
	 * 
	 * @param dt: timestep in milliseconds
	 */
	public void updatePos(int dt) {
		pastCoordinates.add((Point2D.Double)currentCoordinates.clone());
		ArrayList<Double> probabilities = new ArrayList<Double>();
		ArrayList<Integer> coordChange = new ArrayList<Integer>();
		int dx = 1; //Currently integer due to pixels, may be changed if necessary
		int numCalcs = 1;
		double cumProb = 0;
		probabilities.add(1/Math.sqrt(2 * Math.PI * dt)); //probability at dx = 0, i.e. that which the particle remains stationary
		cumProb += probabilities.get(0);
		coordChange.add(0);
		while (cumProb <0.975  && numCalcs < 300) {
			double prob = 2/Math.sqrt(2 * Math.PI * dt) * Math.exp(-(dx * dx)/(double)dt); //2 used due to symmetry in transition probabilities for positive or negative step.
			coordChange.add(dx++); //stores change in x by same index as probabilities.
			cumProb += prob;
			probabilities.add(prob);
			numCalcs++;
		}
		assert(cumProb <=1) : "Total probability cannot be more than one, how did this even happen...";
		for (int iii = 0; iii < 2; iii++) { //Ensures this is done once for x and y coordinates
			double result = rand.nextDouble();
			while (result > cumProb) { //Ensures we're looking for a probability within a region that we have calculated probability for
				result = rand.nextDouble();
			}
			cumProb = 0;
			int step = 0;
			for (int jjj = 0; jjj < probabilities.size(); jjj++) { //Checks for each cumulative probability if this is the path the particle is taking
				cumProb += probabilities.get(jjj);
				if (result < cumProb) { //Essentially, we're finding this by looking at which gap the random number falls into
					step = coordChange.get(jjj);
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
		currentCoordinates.x = currentCoordinates.y = 0;
		pastCoordinates = new ArrayList<Point2D.Double>();
	}
	public Point2D.Double getPos() {
		return currentCoordinates;
	}


	@Override
	public ArrayList<Point2D.Double> getPath() {
		ArrayList<Point2D.Double> results = (ArrayList<Point2D.Double>)pastCoordinates.clone();
		results.add((Point2D.Double)currentCoordinates.clone());
		return results;
	}


	@Override
	public double getDensity(double x, double y, double t) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public double getDensity(Point2D position, double t) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Color getColor() {
		return color;
	}
}