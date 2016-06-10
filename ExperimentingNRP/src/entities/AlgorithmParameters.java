package entities;

public class AlgorithmParameters {
	
	/* --- Attributes --- */
	
	/**
	 * The number of iterations
	 */
	private int numberOfIterations;
	
	/**
	 * The mutation probability
	 */
	private double mutationProbability;
	
	/**
	 * The crossover probability
	 */
	private double crossoverProbability;
	
	/**
	 * The size of the population
	 */
	private int populationSize;

	
	/* --- Getters and setters --- */
	
	/**
	 * @return the numberOfIterations
	 */
	public int getNumberOfIterations() {
		return numberOfIterations;
	}

	/**
	 * @param numberOfIterations the numberOfIterations to set
	 */
	public void setNumberOfIterations(int numberOfIterations) {
		this.numberOfIterations = numberOfIterations;
	}

	/**
	 * @return the mutationProbability
	 */
	public double getMutationProbability() {
		return mutationProbability;
	}

	/**
	 * @param mutationProbability the mutationProbability to set
	 */
	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	/**
	 * @return the crossoverProbability
	 */
	public double getCrossoverProbability() {
		return crossoverProbability;
	}

	/**
	 * @param crossoverProbability the crossoverProbability to set
	 */
	public void setCrossoverProbability(double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}
	
	/**
	 * @return the populationSize
	 */
	public int getPopulationSize() {
		return populationSize;
	}

	/**
	 * @param populationSize the populationSize to set
	 */
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
	
	
	/* --- Constructors --- */

	/**
	 * Constructor initializing the attributes:
	 * - number of iterations to 500
	 * - probability of mutation to 0.1
	 * - crossover probability to 0.5
	 * - population size to 100
	 */
	public AlgorithmParameters() {
		this.numberOfIterations = 500;
		this.mutationProbability = 0.1;
		this.crossoverProbability = 0.5;
		this.populationSize = 100;
	}

	/**
	 * Construct the parameters of the algorithm
	 * @param numberOfIterations the number of iteration to perform
	 * @param populationSize the population size
	 * @param mutationProbability the mutation probability
	 * @param crossoverProbability the crossover probability
	 */
	public AlgorithmParameters(int numberOfIterations, int populationSize, double mutationProbability, double crossoverProbability) {
		this.numberOfIterations = numberOfIterations;
		this.mutationProbability = mutationProbability;
		this.crossoverProbability = crossoverProbability;
		this.populationSize = populationSize;
	}
	
	

}
