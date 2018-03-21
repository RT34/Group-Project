package stochasticSimulation;

//FOR THE LOVE OF GOD DON'T USE AUTO INDENT ON THIS FILE, THE BUTTON ADDING BIT IS DONE THAT WAY FOR CLARITY
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class StochasticSimulation extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JFrame frame = null;
	private SimulationPanel display = null;
	private ArrayList<JButton> buttons;
	private JComboBox chooseSim;
	private JComboBox chooseSimulable;
	private int toSimulate;
	protected GroupLayout layout;
	int dt;
		
	/**Enables/disables the buttons in the frame
	 * 
	 * @param start: toggles startbutton
	 * @param pause: toggles pausebutton
	 * @param reset: toggles resetbutton
	 * @param add: toggles add button
	 * @param clear: toggles clearbutton
	 */
	private void buttonsOnOff(boolean start, boolean pause, boolean reset, boolean add, boolean clear) {
		buttons.get(ButtonIndex.START_BUTTON.getValue()).setEnabled(start);
		buttons.get(ButtonIndex.PAUSE_BUTTON.getValue()).setEnabled(pause);
		buttons.get(ButtonIndex.RESET_BUTTON.getValue()).setEnabled(reset);
		buttons.get(ButtonIndex.ADD_BUTTON.getValue()).setEnabled(add);
		buttons.get(ButtonIndex.ADD_TEN_BUTTON.getValue()).setEnabled(add);
		buttons.get(ButtonIndex.ADD_HUNDRED_BUTTON.getValue()).setEnabled(add);
		buttons.get(ButtonIndex.CLEAR_BUTTON.getValue()).setEnabled(clear);
	}
		
	/**General constructor. Sets up the window in all its questionable glory.
	 * 
	 * @param dt: timestep to be used throughout the simulation(s)
	 */
	public StochasticSimulation(int dt) {
		this.dt = dt;
		buttons = new ArrayList<JButton>();
		frame = new JFrame ("Simple GUI");
		layout = new GroupLayout(frame.getContentPane());
		frame.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Display the window
		frame.setSize(950, 1000);
		//Sets it to middle of screen (assuming the screen is sufficiently large
		frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 - frame.getWidth()/2,Toolkit.getDefaultToolkit().getScreenSize().height/2 - frame.getHeight()/2); 

		//IF YOU ADD MORE COMPONENTS UPDATE ENUM AS APPROPRIATE
		//Creates and greys out buttons as appropriate
		JButton startButton = new JButton("Start simulation");
		startButton.setActionCommand("start");
		buttons.add(startButton);
		startButton.addActionListener(this);

		JButton pauseButton = new JButton("Pause simulation");
		pauseButton.setActionCommand("pause");
		buttons.add(pauseButton);
		pauseButton.addActionListener(this);

		JButton resetButton = new JButton("Reset particle position");
		resetButton.setActionCommand("reset");
		buttons.add(resetButton);
		resetButton.addActionListener(this);

		JButton addButton = new JButton("Add particles");
		addButton.setActionCommand("add");
		buttons.add(addButton);
		addButton.addActionListener(this);

		JButton clearButton = new JButton("Clear the simulation");
		clearButton.setActionCommand("clear");
		buttons.add(clearButton);
		clearButton.addActionListener(this);

		JButton add10Button = new JButton("Add 10 particles");
		add10Button.setActionCommand("add10");
		buttons.add(add10Button);
		add10Button.addActionListener(this);

		JButton add100Button = new JButton("Add 100 particles");
		add100Button.setActionCommand("add100");
		buttons.add(add100Button);
		add100Button.addActionListener(this);

		String[] options = {"Simulate", "Probability density"};
		chooseSim = new JComboBox(options);
		chooseSim.setSelectedIndex(0);
		chooseSim.addActionListener(this);
		chooseSim.setActionCommand("simtype");

		//ADDING NEW OPTIONs
		String[] options2 = {"Brownian particle", "1D free particle", "2D free particle"};
		chooseSimulable = new JComboBox(options2);
		chooseSimulable.setSelectedIndex(0);
		chooseSimulable.addActionListener(this);
		chooseSimulable.setActionCommand("simulable");
		toSimulate = 0;

		this.buttonsOnOff(false, false, false, true, false);
		//TextField numParticles = new TextField("Enter the desired number of particles");

		display = new SimulationPanel(dt);

		//Sets up horizontal positions of window components
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(display)
				.addComponent(chooseSimulable, 0, 200, 200)
				.addGroup(
						layout.createSequentialGroup()
							.addComponent(chooseSim, 0, 200, 200)
							.addComponent(addButton, 0, 150, 150)
							.addComponent(add10Button, 0, 150, 150)
							.addComponent(add100Button, 0, 150, 150)
				)
				.addGroup(
						layout.createSequentialGroup()
							.addComponent(startButton, 0, 150, 150)
							.addComponent(pauseButton, 0, 150, 150)
							.addComponent(resetButton, 0, 150, 150)
							.addComponent(clearButton, 0, 150, 150)
						)
				);
		//Sets up vertical positions of window components
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(display)
				.addComponent(chooseSimulable, 0, 30, 30)
				.addGroup(
						layout.createParallelGroup()
							.addComponent(chooseSim, 0, 30, 30)
							.addComponent(addButton, 0, 30, 30)
							.addComponent(add10Button, 0, 30, 30)
							.addComponent(add100Button, 0, 30, 30)
				)
				.addGroup(
						layout.createParallelGroup()
							.addComponent(startButton, 0, 30, 30)
							.addComponent(pauseButton, 0, 30, 30)
							.addComponent(resetButton, 0, 30, 30)
							.addComponent(clearButton, 0, 30, 30)
						)
				);

		frame.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		boolean simDensity = (chooseSim.getSelectedIndex() == 0);
		switch (command) {
		case "add":
			this.addParticles(1);
			this.buttonsOnOff(true, false, false, true, true);
			chooseSim.setEnabled(false);
			break;
		case "start":
			simDensity = chooseSim.getSelectedIndex() == 1;
			if (simDensity && !display.hasParticles()) {
				this.addParticles(1); //Slightly hacky, but necessary
			}
			System.out.println(simDensity);
			display.start();
			this.buttonsOnOff(false, true, false, false, false);
			chooseSim.setEnabled(false);
			break;
		case "pause":
			display.stop();
			this.buttonsOnOff(true, false, true, simDensity, true);
			break;
		case "clear":
			display.clear();
			this.buttonsOnOff(!simDensity, false, false, simDensity, false);
			chooseSim.setEnabled(true);
			break;
		case "reset":
			display.reset();
			this.buttonsOnOff(true, false, false, simDensity, true);
			break;
		case "add10":
			this.addParticles(10);
			this.buttonsOnOff(true, false, false, true, true);
			break;
		case "add100":
			this.addParticles(100);
			this.buttonsOnOff(true, false, false, true, true);
			break;
		case "simtype":
			this.buttonsOnOff(true, false, false, false, true);
			simDensity = chooseSim.getSelectedIndex() == 1;
			System.out.println(simDensity);
			display.simDensity(simDensity);
			break;
		case "simulable":
			this.toSimulate = chooseSimulable.getSelectedIndex();
		}


	}

	/**Adds a number of the currently selected type of particles to the display
	 * 
	 * @param number: number to add
	 */
	void addParticles (int number) {
		switch (Simulables.fromIndex(toSimulate)) {
		case BROWNIAN_PARTICLE:
			for (int iii = 0; iii < number; iii++) {
				display.addParticles(new BrownianParticle(dt));
			}
			break;
		case ONED_PARTICLE:
			for (int iii = 0; iii < number; iii++) {
				display.addParticles(new OneDParticle(dt));
			}
			break;
		case TWOD_PARTICLE:
			for (int iii = 0; iii < number; iii++) {
				display.addParticles(new TwoDParticle());
			}
		}
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				StochasticSimulation mainFrame = new StochasticSimulation(100); //100 milliseconds seems to be the best to run this at: too laggy if much lower, too jerky if higher
			}
		});

	}

}
