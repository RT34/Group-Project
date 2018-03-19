package stochasticSimulation;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class TwoDParticle implements Simulable {
	Point currentCoordinates;
	ArrayList<Point> pastCoordinates;
	Random rand = new Random();
	Color colour;
	Point2D.Double p; //Point values for x and y axes
	Point maxSteps; //Number of allowed steps for x and y axes
	ArrayList<Point2D.Double> probabilities = new ArrayList<Point2D.Double>();
	ArrayList<Point> steps = new ArrayList<Point>();
	
	public TwoDParticle() {
		int startPoint = rand.nextInt(3);
		ArrayList<Double> locationProbs = new ArrayList<Double>();
		double totalProb = 0;
		for (int iii = 0; iii <= 100; iii++) {
			locationProbs.add(Math.abs(Math.cos(iii * Math.PI /50)));
			totalProb += Math.abs(Math.cos(iii * Math.PI / 50));
		}
		double cumProb = 0;
		double location = rand.nextDouble();
		for (int iii = 0; iii <= 100; iii++) {
			double prob = locationProbs.get(iii);
			prob/= totalProb;
			cumProb += prob;
			if (location <= cumProb) {
				this.currentCoordinates = new Point((rand.nextInt(2) != 0)? -iii : iii, 0);
				break;
			}
		}
		//this.currentCoordinates = new Point((startPoint == 0) ? -30 : (startPoint == 1) ? 0 : 30,0);
		this.colour = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		maxSteps = new Point(2,2);
		p = new Point2D.Double(0.4, 0.4);
		probabilities.add(new Point2D.Double(1, 1));
		for (int iii = 1; iii <= maxSteps.x; iii++) {
			probabilities.get(0).x -= p.x/iii;
			probabilities.add(new Point2D.Double(0, 0));
			probabilities.get(iii).x = (p.x/iii);
			steps.add(new Point(iii, 0));
		}
		for (int jjj = 1; jjj <= maxSteps.y; jjj++) {
			probabilities.get(0).y -= p.y/jjj;
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
		//Ensures that the sums of any probability is not greater than one
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

	@Override
	public Point getPos() {
		return currentCoordinates;
	}

	@Override
	public ArrayList<Point> getPath() {
		ArrayList<Point> path = new ArrayList<Point>();
		path.add(currentCoordinates);
		return path;
	}

	@Override
	public void reset() {
		this.currentCoordinates = new Point(0,0);
	}

	@Override
	public void changeDensities(int x, int y, double t, ArrayList<ArrayList<Double>> probDensities,
			ArrayList<ArrayList<Double>> newDensities) {
		for (int iii = 0; iii  <= maxSteps.x; iii++) {
			double probChangeX;
			probChangeX = probabilities.get(iii).getX(); //*probDensities.get(x).get(y);
			for (int jjj = 0; jjj <= maxSteps.y; jjj++) {
				if ((iii != 0 && x + steps.get(iii-1).x >= newDensities.size()) || (jjj != 0 && y + steps.get(jjj-1).y >= newDensities.get(0).size())) {
					continue;
				}
				double probChangeY = probabilities.get(jjj).getY() * probDensities.get(x).get(y);
				newDensities.get(x).set(y, newDensities.get(x).get(y) - probChangeX * probChangeY);
				newDensities.get(x + ((iii != 0) ? steps.get(iii-1).x : 0)).set(y + ((jjj != 0) ? steps.get(jjj-1).y : 0), 
						newDensities.get(x + ((iii != 0) ? steps.get(iii-1).x : 0)).get(y + ((jjj != 0) ? steps.get(jjj-1).y : 0)) + probChangeX * probChangeY);
			}
		}

	}

	@Override
	public void changeDensities(Point position, double t, ArrayList<ArrayList<Double>> probDensities,
			ArrayList<ArrayList<Double>> newDensities) {
		this.changeDensities(position.x, position.y, t, probDensities, newDensities);

	}

	@Override
	public ArrayList<ArrayList<Double>> initDensity(int length, int height) {
		int xStep = length; //Dividing by stepsize, stepsize must be even
		int yStep = height;
		ArrayList<ArrayList<Double>> densities = new ArrayList<ArrayList<Double>>();
		for (int iii = 0; iii < xStep; iii++) {
			densities.add(new ArrayList<Double>());
			for (int jjj = 0; jjj < yStep; jjj++) {
				if (iii >= xStep/2 - 200 && iii <= xStep/2+ 200 && jjj == yStep/2) {
					densities.get(iii).add(1000000000000.0 * Math.abs(Math.cos(iii * Math.PI /100.0)));
				}
				else {
					densities.get(iii).add(0.0);
				}
			}
		}
		return densities;
	}

	@Override
	public Color getColor() {
		return this.colour;
	}

}
