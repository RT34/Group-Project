package stochasticSimulation;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**1D particle corresponding to equation 9, drho/dt = 0.5/dq(rho(x + dx) - rho_bar(x))
 * 
 * @author rbroo
 *
 */
public class OneDParticle implements Simulable {
	Point currentCoordinates;
	ArrayList<Point> pastCoordinates;
	Random rand = new Random();
	Color colour;
	int numSteps; //Number of values of dx
	double p;
	
	public OneDParticle(int dt) {
		this.currentCoordinates = new Point(0,rand.nextInt(100) - 50);
		this.colour = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		numSteps = 5;
		p = 0.1;
	}
	@Override
	public void updatePos(int dt) {
		double cumProb = 0;
		for (int iii = 1; iii < numSteps +1; iii++) {
			double prob = rand.nextDouble();
			cumProb += p/iii;
			if (prob <= cumProb) {
				this.currentCoordinates.x += iii;
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
		ArrayList<Point> result = new ArrayList<Point>();
		result.add(currentCoordinates);
		return result; //Path will always be a line, so not super useful to draw
	}

	@Override
	public void reset() {
		this.currentCoordinates = new Point(0,0);
	}

	@Override
	public void changeDensities(int x, int y, double t, ArrayList<ArrayList<Double>> probDensities,
			ArrayList<ArrayList<Double>> newDensities) {
		for (int iii = 1; iii  < numSteps + 1; iii++) {
			double probChange = p/iii * probDensities.get(x).get(y);
			if (x + iii >= newDensities.size()) {
				continue;
			}
			newDensities.get(x).set(y, newDensities.get(x).get(y) - probChange);
			newDensities.get(x + iii).set(y, newDensities.get(x + iii).get(y) + probChange);
		}
	}

	@Override
	public void changeDensities(Point position, double t, ArrayList<ArrayList<Double>> probDensities,
			ArrayList<ArrayList<Double>> newDensities) {
		changeDensities(position.x, position.y, t, probDensities, newDensities);

	}

	@Override
	public ArrayList<ArrayList<Double>> initDensity(int length, int height) {
		//density is zero everywhere but a vertical line at the centre, makes it easier to see
				int xStep = length; //Dividing by stepsize, stepsize must be even
				int yStep = height;
				ArrayList<ArrayList<Double>> densities = new ArrayList<ArrayList<Double>>();
				for (int iii = 0; iii < xStep; iii++) {
					densities.add(new ArrayList<Double>());
					for (int jjj = 0; jjj < yStep; jjj++) {
						if (iii == xStep/2 && jjj > yStep/2 - 50 && jjj < yStep/2 + 50) {
							densities.get(iii).add(1000000000000.0);
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
