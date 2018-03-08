package stochasticSimulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

/**Panel for handling and display of the simulation
 * 
 * @author rbroo
 *
 */
public class SimulationPanel extends JPanel implements ActionListener {
	ArrayList<Simulable>toModel = new ArrayList<Simulable>();
	private static final long serialVersionUID = 1L;
	int dt;
	private Timer updateTimer;
	boolean changePath;
	boolean simDensity;
	boolean densityInit = false;
	ArrayList<ArrayList<Double>> densities = new ArrayList<ArrayList<Double>>();
	
	/**Default constructor
	 * 
	 * @param dt: timestep for handled processes
	 */
	public SimulationPanel(int dt) {
		super();
		this.setPreferredSize(new Dimension(900,700));
		this.dt = dt;
		updateTimer = new Timer(this.dt, this);
		simDensity = false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		
	}
	private void simParticles(Graphics g) {
		for (Simulable particle: toModel) {
			if (changePath) {
				particle.updatePos(dt);
			}
			ArrayList<Point> coordinates = particle.getPath();
			if (coordinates.size() ==1) {
				g.setColor(particle.getColor());
				g.fillOval(coordinates.get(0).x + this.getWidth()/2-2, coordinates.get(0).y +this.getHeight()/2-2, 4, 4);
			}
			else if(coordinates.size() != 0) {
				//Draws path taken by particles. this.getDimension used so (0,0) in particle coordinates corresponds to the origin
				g.setColor(new Color(particle.getColor().getRed()/255f, particle.getColor().getGreen()/255f, particle.getColor().getBlue()/255f, 0.1f));
				for (int iii = 1; iii < coordinates.size(); iii++) {
					g.drawLine( coordinates.get(iii-1).x + this.getWidth()/2, coordinates.get(iii-1).y + this.getHeight()/2,
							coordinates.get(iii).x + this.getWidth()/2, coordinates.get(iii).y + this.getHeight()/2);
				}
				g.setColor(particle.getColor());
				g.fillOval(coordinates.get(coordinates.size()-1).x+this.getWidth()/2 - 2, coordinates.get(coordinates.size()-1).y + this.getHeight()/2 -2, 4, 4);
			}
		}
	}
	
	/**Displays components to the window when update
	 * 
	 * @param g: I have no idea what this is :P It does some graphics things
	 */
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0,0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1); //-1s required to make sure bottom and right borders display correctly
		g.drawLine(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
		g.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
		System.out.println("Redrawing");
		int colHeight, rowWidth, stepSize;
		if (!simDensity) {
			simParticles(g);
		}
		else {
			double maxDensity = 0;
			ArrayList<ArrayList<Double>> newDensities = new ArrayList<ArrayList<Double>>();
			if(densities.isEmpty())  {
				densities = toModel.get(0).initDensity(this.getWidth(), this.getHeight());
				for (ArrayList<Double> row : densities) {
					double testMax = Collections.max(row);
					maxDensity = (maxDensity < testMax) ? testMax : maxDensity;
				}
				for (ArrayList<Double> row : densities) {
					newDensities.add((ArrayList<Double>)row.clone());
				}
				colHeight = densities.get(0).size();
				rowWidth = densities.size();
				stepSize = this.getHeight()/colHeight;
				densityInit = true;
			}
			else {
				colHeight = densities.get(0).size();
				rowWidth = densities.size();
				stepSize = this.getHeight()/colHeight;
				for (int iii = 0; iii < rowWidth; iii++) {
					newDensities.add(new ArrayList<Double>());
					for (int jjj = 0; jjj < colHeight; jjj++) {
						
						double newDensity = toModel.get(0).getDensity(iii, jjj, dt,densities);
						maxDensity = (maxDensity < newDensity) ? newDensity : maxDensity; //Trinary operator
						newDensities.get(iii).add(newDensity);
						if (newDensity != 0)
							System.out.println(newDensity);
					}
				}
				System.out.println(maxDensity);
			}
			System.out.println(colHeight);
			System.out.println(rowWidth);
			for (int iii = 0; iii < rowWidth; iii++) {
				for (int jjj = 0; jjj < colHeight; jjj++) {
					if (newDensities.get(iii).get(jjj) == 0.0) {
						continue;
					}
					System.out.println(maxDensity);
					g.setColor(new Color(0f, 1.f, 0f, (float)(newDensities.get(iii).get(jjj)/maxDensity)));
					g.fillRect(iii * stepSize + stepSize/2, jjj * stepSize + stepSize/2, stepSize, stepSize);
				}
			}
			densities = new ArrayList<ArrayList<Double>>();
			for (int iii = 0; iii < newDensities.size(); iii++) {
				densities.add( (ArrayList<Double>) newDensities.get(iii).clone());
			}
		}
	}
	
	/**Adds particles to the system for simulation
	 * 
	 * @param toAdd: an array of particles to add
	 */
	public void addParticles(ArrayList<Simulable> toAdd) {
		this.toModel.addAll(toAdd);
		this.repaint();
	}
	
	/**Adds a single particle to the system
	 * 
	 * @param toAdd: the particle to add
	 */
	public void addParticles(Simulable toAdd) {
		this.changePath = false;
		this.toModel.add(toAdd);
		this.repaint();
	}
	
	/**Deletes all particles from the system
	 * 
	 */
	public void clear() {
		this.toModel = new ArrayList<Simulable>();
		this.repaint();
	}
	
	/**Starts/runs the simulation
	 * 
	 */
	public void start() {
		changePath = true;
		updateTimer.start();
	}

	/**Pauses/stops the simulation
	 * 
	 */
	public void stop() {
		updateTimer.stop();
		changePath = false;
	}
	public void reset() {
		for (Simulable particle : toModel) {
			particle.reset();
			repaint();
		}
	}
	public void simDensity(boolean simDensity) {
		this.simDensity = simDensity;
	}
}
