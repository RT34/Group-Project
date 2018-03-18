package stochasticSimulation;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

/**Class to model a particle moving in two dimensions under the effects of a constant field. A and p's units are in terms of pixels per time unit, squared where necessary
 * 
 * @author rbroo
 *
 */
public class AccelParticle extends TwoDParticle {

	Point maxStep; //Number of allowed steps for x and y axes
	Point2D.Double a; //point values for x and y axes, flat acceleration
	
	public AccelParticle() {
		maxStep = new Point(5,2);
		a = new Point2D.Double(0.001, 0);
		p = new Point2D.Double(0.05, 0.2);
	}
	
	private void calcProbabilities () {
		ArrayList<Point2D.Double> newProbabilities = new ArrayList<Point2D.Double>();
		newProbabilities.add(new Point2D.Double(1, 1));
		p = new Point2D.Double(p.getX() + a.getX(), p.getY() + a.getY());
		if (p.getX() > 1 || p.getY() > 1) {
			if (p.getX()/maxStep.getX() > 1) {
			}
		}
		else {
			for (int iii = 1; iii <= maxStep.x; iii++) {
				probabilities.get(0).x -= p.x/iii;
				probabilities.add(new Point2D.Double(0, 0));
				probabilities.get(iii).x = (p.x/iii);
			}
			for (int jjj = 1; jjj <= maxStep.y; jjj++) {
				probabilities.get(0).y -= p.y/jjj;
				if (jjj > probabilities.size()) {
					probabilities.add(new Point2D.Double(0, 0));
				}
				probabilities.get(jjj).y = p.y/jjj;
			}
		}
		//Ensures that the sums of any probability is not greater than one
		while (probabilities.get(0).x <= 0) {
			probabilities.get(0).x += probabilities.get(maxStep.x).getX();
			probabilities.get(maxStep.x).x = 0;
			maxStep.x -=1;
			System.out.println("Too many steps provided");
		}
		while (probabilities.get(0).y <= 0) {
			probabilities.get(0).y += probabilities.get(maxStep.y).getY();
			probabilities.get(maxStep.y).y = 0;
			maxStep.y -=1;
			System.out.println("Too many steps provided");
		}
	}
	
	@Override
	public void updatePos(int dt) {
		calcProbabilities();
		super.updatePos(dt);
		
	}

	@Override
	public Point getPos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Point> getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeDensities(int x, int y, double t, ArrayList<ArrayList<Double>> probDensities,
			ArrayList<ArrayList<Double>> newDensities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeDensities(Point position, double t, ArrayList<ArrayList<Double>> probDensities,
			ArrayList<ArrayList<Double>> newDensities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<ArrayList<Double>> initDensity(int length, int height) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return null;
	}

}
