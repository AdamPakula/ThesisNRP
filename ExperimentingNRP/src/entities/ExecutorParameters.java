package entities;

import parameters.DefaultParameters;

public class ExecutorParameters {
	
	/* --- Attributes --- */
	
	/**
	 * The number of iterations
	 */
	private int numberOfIterations;
	
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
	 * Constructor initializing the attributes with default values
	 */
	public ExecutorParameters() {
		this.numberOfIterations = DefaultParameters.NUMBER_OF_ITERATIONS;
		this.populationSize = DefaultParameters.POPULATION_SIZE;
	}

	/**
	 * Construct the parameters of the executor
	 * @param numberOfIterations the number of iteration to perform
	 * @param populationSize the population size
	 */
	public ExecutorParameters(int numberOfIterations, int populationSize) {
		this.numberOfIterations = numberOfIterations;
		this.populationSize = populationSize;
	}

}
