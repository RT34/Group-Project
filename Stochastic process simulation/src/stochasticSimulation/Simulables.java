package stochasticSimulation;


/**Enum to store the different types of models
 * 
 * @author rbroo
 *
 */
public enum Simulables {
	BROWNIAN_PARTICLE, ONED_PARTICLE, TWOD_PARTICLE;
	public static Simulables fromIndex (int index){
		switch (index) {
		case 0:
			return BROWNIAN_PARTICLE;
		case 1:
			return ONED_PARTICLE;
		case 2:
			return TWOD_PARTICLE;
		default: assert(false) : "Invalid index";
		return null;
		}
	}
}
