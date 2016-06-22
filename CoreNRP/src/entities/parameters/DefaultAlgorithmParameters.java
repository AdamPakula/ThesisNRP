package entities.parameters;

/**
 * The default values of algorithm parameters
 * @author Vavou
 *
 */
public class DefaultAlgorithmParameters {

	/**
	 * The crossover probability
	 */
	public final static double CROSSOVER_PROBABILITY = 0.8;
	
	/**
	 * Return the default value of mutation probability considering the number of tasks
	 * return 1.0/numberOfFeatures
	 * @param numberOfFeatures the number of features of the problem
	 * @return the probability of mutation
	 */
	public final static double MUTATION_PROBABILITY(int numberOfFeatures) {
		return 1.0/numberOfFeatures;
	}
}
