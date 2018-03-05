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
		protected GroupLayout layout;
		
		/**Enables/disables the buttons in the frame
		 * 
		 * @param start: toggles startbutton
		 * @param pause: toggles pausebutton
		 * @param reset: toggles resetbutton
		 * @param add: toggles add button
		 * @param clear: toggles clearbuton
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
		
		public StochasticSimulation() {
			buttons = new ArrayList<JButton>();
			frame = new JFrame ("Simple GUI");
			layout = new GroupLayout(frame.getContentPane());
			frame.setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//Display the window
			frame.setSize(950,900);
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
			
			this.buttonsOnOff(false, false, false, true, false);
			//TextField numParticles = new TextField("Enter the desired number of particles");
			
			
			display = new SimulationPanel(100);
			//Sets up horizontal positions of window components
			layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(display)
					.addGroup(
							layout.createSequentialGroup()
								.addComponent(addButton, 0, 150, 150)
								.addComponent(add10Button, 0, 150,150)
								.addComponent(add100Button, 0, 150, 150)
					)
					.addGroup(
							layout.createSequentialGroup()
								.addComponent(startButton, 0, 150, 150)
								.addComponent(pauseButton, 0, 150, 150)
								.addComponent(resetButton, 0, 150, 150)
								.addComponent(clearButton, 0,150,150)
							)
					);
			//Sets up vertical positions of window components
			layout.setVerticalGroup(layout.createSequentialGroup()
					.addComponent(display)
					.addGroup(
							layout.createParallelGroup()
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
			switch (command) {
			case "add":
				display.addParticles(new BrownianParticle());
				this.buttonsOnOff(true, false, false, true, true);
				break;
			case "start":
				display.start();
				this.buttonsOnOff(false, true, false, false, false);
				break;
			case "pause":
				display.stop();
				this.buttonsOnOff(true, false, true, true, true);
				break;
			case "clear":
				display.clear();
				this.buttonsOnOff(false, false, false, true, false);
				break;
			case "reset":
				display.reset();
				this.buttonsOnOff(true, false, false, true, true);
				break;
			case "add10":
				for (int iii = 0; iii < 10; iii++) {
					display.addParticles(new BrownianParticle());
					this.buttonsOnOff(true, false, false, true, true);
				}
				break;
			case "add100":
				for (int iii = 0; iii < 100; iii++) {
					display.addParticles(new BrownianParticle());
					this.buttonsOnOff(true, false, false, true, true);
				}
			}
			
		}

		Object getComponent(ButtonIndex index) {
			return this.getComponent(index.getValue());
		}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				StochasticSimulation mainFrame = new StochasticSimulation();
			}
		});
		
	}

}