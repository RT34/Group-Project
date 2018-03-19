package stochasticSimulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;

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
	int numDraws = 0;
	ArrayList<ArrayList<Double>> densities = new ArrayList<ArrayList<Double>>();
	boolean lastDraw = false;

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

	/**Handles actions performed by the panel. At current, merely redraws
	 * 
	 * @param e: the event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	/**Handles the drawing and display of particles being modelled
	 * 
	 * @param g: the graphics object the particles are being output to
	 * @throws Exception 
	 */
	private void simParticles(Graphics g) throws InputMismatchException {
		for (Simulable particle: toModel) {
			if (changePath) {
				particle.updatePos(dt);
			}
			ArrayList<Point> coordinates = particle.getPath();
			if(coordinates.size() > 1) {
				//Draws path taken by particles. this.getDimension used so (0,0) in particle coordinates corresponds to the origin
				g.setColor(new Color(particle.getColor().getRed()/255f, particle.getColor().getGreen()/255f, particle.getColor().getBlue()/255f, 0.1f));
				for (int iii = 1; iii < coordinates.size(); iii++) {
					g.drawLine( coordinates.get(iii-1).x + this.getWidth()/2, coordinates.get(iii-1).y + this.getHeight()/2,
							coordinates.get(iii).x + this.getWidth()/2, coordinates.get(iii).y + this.getHeight()/2);
				}
			}
		}
		for (Simulable particle: toModel) {//Separate loop so particles are drawn on top of paths, instead of being obscured
			g.setColor(particle.getColor());
			g.fillOval(particle.getPos().x+this.getWidth()/2 - 2, particle.getPos().y + this.getHeight()/2 -2, 4, 4); 
		}
	}

	/**Displays components to the window when update
	 * 
	 * @param g: I have no idea what this is :P It does some graphics things
	 */
	public void paint(Graphics g) {
		try {
			g.setColor(Color.WHITE);
			g.fillRect(0,0, this.getWidth(), this.getHeight());
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1); //-1s required to make sure bottom and right borders display correctly
			g.drawLine(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
			g.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
			System.out.println("Redrawing");
			if (toModel.isEmpty()) {
				return;
			}
			if (!simDensity) {
				simParticles(g);
			}
			else {
				int colHeight, rowWidth, stepSize;
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
					for (ArrayList<Double> row : densities) {
						newDensities.add((ArrayList<Double>)row.clone());
					}
					colHeight = densities.get(0).size();
					rowWidth = densities.size();
					stepSize = this.getHeight()/colHeight;
					int nProgress = 10;
					for (int iii = 0; iii < rowWidth; iii++) {
						if (iii/(double)rowWidth * 100 > nProgress) {
							System.out.println("This is " + nProgress + "% complete");
							nProgress+=10;
						}
						for (int jjj = 0; jjj < colHeight; jjj++) {
							if (densities.get(iii).get(jjj) == 0.0) {
								continue;
							}
							toModel.get(0).changeDensities(iii, jjj, dt, densities, newDensities);
						}
					}
				}
				for (ArrayList<Double> row : newDensities) {
					double toCheck = Collections.max(row);
					maxDensity = (toCheck > maxDensity) ? toCheck : maxDensity;
				}
				System.out.println(maxDensity);
				//System.out.println(++numDraws);
				for (int iii = 0; iii < rowWidth; iii++) {
					for (int jjj = 0; jjj < colHeight; jjj++) {
						if (newDensities.get(iii).get(jjj) == 0.0) {
							continue;
						}
						try {
							g.setColor(new Color(0f, 1.f, 0f, (float)(newDensities.get(iii).get(jjj)/maxDensity)));
						}
						catch (IllegalArgumentException e) {
							System.out.println(newDensities.get(iii).get(jjj)/maxDensity);
							assert (false) : "Negative density encountered";
						}
						g.fillRect(iii * stepSize + stepSize/2, jjj * stepSize + stepSize/2, stepSize, stepSize);
					}
				}
				densities = new ArrayList<ArrayList<Double>>();
				for (ArrayList<Double> row : newDensities) {
					densities.add( (ArrayList<Double>) row.clone());
				}
			}
			g.setColor(Color.BLACK);
			if (changePath || lastDraw) {
				g.drawString(++numDraws + " Ticks have elapsed", 10, 15);
				lastDraw = false;
			}
		}
		catch (InputMismatchException e) {
			//StochasticSimulation parent = (StochasticSimulation) SwingUtilities.getRoot(this);
			//parent.pause();
			System.out.println(e);

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
		System.out.println(toModel.size());
	}

	/**Deletes all particles from the system
	 * 
	 */
	public void clear() {
		this.toModel = new ArrayList<Simulable>();
		this.numDraws = 0;
		this.densityInit = false;
		if (this.densities.size() != 0) {
			this.densities = new ArrayList<ArrayList<Double>>();
		}
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
		lastDraw = true; //Ensures number of ticks is still displayed
	}

	/**Resets particle positions
	 * 
	 */
	public void reset() {
		for (Simulable particle : toModel) {
			particle.reset();
			repaint();
			this.numDraws = 0;
		}
	}

	/**Changes the boolean that determines whether particles or simulated or density is displayed
	 * 
	 * @param simDensity: should density or particle movement be displayed
	 */
	public void simDensity(boolean simDensity) {
		this.simDensity = simDensity;
	}

	/**States whether or not this has modelled particles
	 * 
	 * @return: true if there are particles to model, false otherwise
	 */
	public boolean hasParticles() {
		return !this.toModel.isEmpty();
	}
}
