package stochasticSimulation;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class DensityCalculator implements Runnable {
	ArrayList<ArrayList<Double>> toChange;
	ArrayList<ArrayList<Double>> toCheck;
	double maxDensity;
	Simulable toModel;
	int width, height, startIndex, dt;
	int progress;
	DensityCalculator(ArrayList<ArrayList<Double>> toChange, Simulable toModel, int width, int height, ArrayList<ArrayList<Double>> toCheck, int startIndex, int dt) {
		this.toChange = toChange;
		this.toCheck = toCheck;
		this.toModel = toModel;
		this.width = width;
		this.height = height;
		this.dt = dt;
		maxDensity = 0;
	}

	@Override
	public void run() {
		ArrayList<ArrayList<Double>> newDensities = new ArrayList<ArrayList<Double>>();
		System.out.println(width);
		double progress = 0.1;
		for (int iii = startIndex; iii < width; iii++) {
			newDensities.add(new ArrayList<Double>());
			if (iii > width * progress) {
				System.out.println(iii);
				System.out.println(width * progress);
				progress += 0.1;
			}
			for (int jjj = 0; jjj < height; jjj++) {
				double newDensity = toModel.getDensity(iii, jjj, dt,toCheck); //Large multiplier to prevent probabilities from rounding to zero too quickly
				maxDensity = (maxDensity < newDensity) ? newDensity : maxDensity; //Trinary operator
				newDensities.get(iii).add(newDensity);
			}
		}
		
	}
	public double getMax() {
		return maxDensity;
	}
	public int getProgress() {
		return progress;
	}
}
