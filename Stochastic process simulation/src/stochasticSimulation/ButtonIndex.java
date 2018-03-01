package stochasticSimulation;

public enum ButtonIndex {
	START_BUTTON(0), PAUSE_BUTTON(1), RESET_BUTTON(2), ADD_BUTTON(3), CLEAR_BUTTON(4);
	int index;
	ButtonIndex(int index) {
		this.index = index;
	}
	public int getValue() {
		return this.index;
	}
	
}
