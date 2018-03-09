package stochasticSimulation;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/**Exists purely to allow a progress display
 * 
 * @author rbroo
 *
 */
public class TimedDensityCalculator extends SwingWorker<Void, Void> {
	ArrayList<ArrayList<Double>> toChange;
	ArrayList<ArrayList<Double>> toCheck;
	double maxDensity;
	Simulable toModel;
	int width, height, startIndex, dt;

	ProgressMonitor monitor;
	TimedDensityCalculator(ArrayList<ArrayList<Double>> toChange, Simulable toModel, int width, int height, ArrayList<ArrayList<Double>> toCheck, int startIndex, int dt, SimulationPanel owner) {
		this.toChange = toChange;
		this.toCheck = toCheck;
		this.toModel = toModel;
		this.width = width;
		this.height = height;
		this.dt = dt;
		maxDensity = 0;
		monitor = new ProgressMonitor(owner, "Computing probability density", "", startIndex, width);
		monitor.setMillisToDecideToPopup(1);
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		ArrayList<ArrayList<Double>> newDensities = new ArrayList<ArrayList<Double>>();
		for (int iii = startIndex; iii < width; iii++) {
			newDensities.add(new ArrayList<Double>());
			for (int jjj = 0; jjj < height; jjj++) {
				double newDensity = 10000000 *  toModel.getDensity(iii, jjj, dt,toCheck); //Large multiplier to prevent probabilities from rounding to zero too quickly
				maxDensity = (maxDensity < newDensity) ? newDensity : maxDensity; //Trinary operator
				newDensities.get(iii).add(newDensity);
			}
		}
		return null;
	}
	
	public double getMax() {
		return maxDensity;
	}

}
