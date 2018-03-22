package stochasticSimulation;

/**enum to store the indices of the various buttons with the button array
 * 
 * @author rbroo
 *
 */
public enum ButtonIndex {
	START_BUTTON(0), PAUSE_BUTTON(1), RESET_BUTTON(2), ADD_BUTTON(3), CLEAR_BUTTON(4), ADD_TEN_BUTTON(5), ADD_HUNDRED_BUTTON(6);
	int index;
	
	/**Constructor, required to add values to enums
	 * 
	 * @param index: the value to assign
	 */
	ButtonIndex(int index) {
		this.index = index;
	}
	
	/**Returns the index position associated with the button
	 * 
	 * @return index position
	 */
	public int getValue() {
		return this.index;
	}
	
}
