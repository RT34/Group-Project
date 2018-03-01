package stochasticSimulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class SimulationPanel extends JPanel implements ActionListener {
	ArrayList<Simulable>toModel = new ArrayList<Simulable>();
	private static final long serialVersionUID = 1L;
	double dt;
	private Timer updateTimer;
	
	public SimulationPanel(double dt) {
		super();
		this.setPreferredSize(new Dimension(900,700));
		this.dt = dt;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0,0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1); //-1s required to make sure bottom and right borders display correctly
		g.drawLine(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
		g.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
		for (Simulable particle: toModel) {
			particle.updatePos(dt);
			ArrayList<Point2D.Double> coordinates = particle.getPath();
			g.setColor(particle.getColor());
			if (coordinates.size() ==1) {
				g.fillOval((int)coordinates.get(0).getX() + this.getWidth()/2, (int)coordinates.get(0).getY() +this.getHeight()/2, 4, 4);
			}
			else if(coordinates.size() != 0) {
				//Draws path taken by particles. this.getDimension used so (0,0) in particle coordinates corresponds to the origin
				for (int iii = 1; iii < coordinates.size(); iii++) {
					g.drawLine((int)coordinates.get(iii-1).getX() + this.getWidth()/2, (int)coordinates.get(iii-1).getY() + this.getHeight()/2,
							(int)coordinates.get(iii).getX() + this.getWidth()/2, (int)coordinates.get(iii).getY() + this.getHeight()/2);
				}
				g.fillOval((int)coordinates.get(coordinates.size()-1).getX(), (int)coordinates.get(coordinates.size()-1).getY(), 4, 4);
			}
		}
	}
	public void addParticles(ArrayList<Simulable> toAdd) {
		this.toModel.addAll(toAdd);
		this.repaint();
	}
	
	public void addParticles(Simulable toAdd) {
		this.toModel.add(toAdd);
		this.repaint();
	}
	
	public void clear() {
		this.toModel = new ArrayList<Simulable>();
		this.repaint();
	}
	public void start() {
		updateTimer.start();
	}
	public void stop() {
		updateTimer.stop();
	}

}
