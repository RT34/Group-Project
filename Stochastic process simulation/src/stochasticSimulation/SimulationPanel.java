package stochasticSimulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

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
	
	/**Default constructor
	 * 
	 * @param dt: timestep for handled processes
	 */
	public SimulationPanel(int dt) {
		super();
		this.setPreferredSize(new Dimension(900,700));
		this.dt = dt;
		updateTimer = new Timer(this.dt, this);
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
			ArrayList<Point2D.Double> coordinates = particle.getPath();
			if (coordinates.size() ==1) {
				g.setColor(particle.getColor());
				g.fillOval((int)coordinates.get(0).getX() + this.getWidth()/2-2, (int)coordinates.get(0).getY() +this.getHeight()/2-2, 4, 4);
			}
			else if(coordinates.size() != 0) {
				//Draws path taken by particles. this.getDimension used so (0,0) in particle coordinates corresponds to the origin
				g.setColor(new Color(particle.getColor().getRed()/255f, particle.getColor().getGreen()/255f, particle.getColor().getBlue()/255f, 0.1f));
				for (int iii = 1; iii < coordinates.size(); iii++) {
					g.drawLine((int)coordinates.get(iii-1).getX() + this.getWidth()/2, (int)coordinates.get(iii-1).getY() + this.getHeight()/2,
							(int)coordinates.get(iii).getX() + this.getWidth()/2, (int)coordinates.get(iii).getY() + this.getHeight()/2);
				}
				g.setColor(particle.getColor());
				g.fillOval((int)coordinates.get(coordinates.size()-1).getX()+this.getWidth()/2 - 2, (int)coordinates.get(coordinates.size()-1).getY() + this.getHeight()/2 -2, 4, 4);
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
		simParticles(g);
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
}
