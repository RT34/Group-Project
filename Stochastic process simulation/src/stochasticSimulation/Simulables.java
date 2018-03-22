package stochasticSimulation;


/**Enum to store the different types of models
 * 
 * @author rbroo
 *
 */
public enum Simulables {
	BROWNIAN_PARTICLE, ONED_PARTICLE, TWOD_PARTICLE;
	
	/**Allows enum to be determined from an integer index, such as that used in the JComboBox for simulable selection
	 * 
	 * @param index : number to associate with a Simulable
	 * @return
	 */
	public static Simulables fromIndex (int index){
		switch (index) {
		
		case 0:
			return BROWNIAN_PARTICLE;
			
		case 1:
			return ONED_PARTICLE;
			
		case 2:
			return TWOD_PARTICLE;
			
		default: assert(false) : "Invalid index"; //Throws error in case of invalid value
		return null;
		}
	}
}
