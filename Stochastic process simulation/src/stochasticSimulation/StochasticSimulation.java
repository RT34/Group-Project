package stochasticSimulation;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

	public class StochasticSimulation extends JFrame implements ActionListener {

		private static final long serialVersionUID = 1L;
		private JFrame frame = null;
		private SimulationPanel display = null;
		private ArrayList<Button> buttons;
		protected GroupLayout layout;
		public StochasticSimulation() {
			buttons = new ArrayList<Button>();
			frame = new JFrame ("Simple GUI");
			layout = new GroupLayout(frame.getContentPane());
			frame.setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//Display the window
			frame.setSize(950,900);
			frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 - frame.getWidth()/2,Toolkit.getDefaultToolkit().getScreenSize().height/2 - frame.getHeight()/2); 
			
			//IF YOU ADD MORE COMPONENTS UPDATE ENUM AS APPROPRIATE
			//Creates and greys out buttons as appropriate
			Button startButton = new Button("Start simulation");
			startButton.setEnabled(false);
			startButton.setActionCommand("start");
			buttons.add(startButton);
			
			Button pauseButton = new Button("Pause simulation");
			pauseButton.setEnabled(false);
			pauseButton.setActionCommand("pause");
			buttons.add(pauseButton);
			
			Button resetButton = new Button("Reset particle position");
			resetButton.setEnabled(false);
			resetButton.setActionCommand("reset");
			buttons.add(resetButton);
			
			Button addButton = new Button("Add particles");
			addButton.setActionCommand("add");
			buttons.add(addButton);
			
			Button clearButton = new Button("Clear the simulation");
			clearButton.setEnabled(false);
			clearButton.setActionCommand("clear");
			buttons.add(clearButton);
			TextField numParticles = new TextField("Enter the desired number of particles");
			
			
			addButton.setActionCommand("add");
			display = new SimulationPanel(1);
			//Sets up horizontal positions of window components
			layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(display)
					.addComponent(addButton)
					.addGroup(
							layout.createSequentialGroup()
								.addComponent(startButton, 0, 150, 150)
								.addComponent(pauseButton, 0, 150, 150)
								.addComponent(resetButton, 0, 150, 150))
					);
			//Sets up vertical positions of window components
			layout.setVerticalGroup(layout.createSequentialGroup()
					.addComponent(display)
					.addComponent(addButton, 0, 30, 30)
					.addGroup(
							layout.createParallelGroup()
								.addComponent(startButton, 0, 30, 30)
								.addComponent(pauseButton, 0, 30, 30)
								.addComponent(resetButton, 0, 30, 30))
					);

			frame.setVisible(true);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			switch (command) {
			case "add":
				display.addParticles(new BrownianParticle());
				buttons.get(ButtonIndex.START_BUTTON.getValue()).setEnabled(true);
				break;
			case "start":
				display.start();
				buttons.get(ButtonIndex.ADD_BUTTON.getValue()).setEnabled(false);
				buttons.get(ButtonIndex.PAUSE_BUTTON.getValue()).setEnabled(true);
				break;
			case "stop":
				display.stop();
			}
			
		}

		Object getComponent(ButtonIndex index) {
			return this.getComponent(index.getValue());
		}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StochasticSimulation mainFrame = new StochasticSimulation();
	}

}
