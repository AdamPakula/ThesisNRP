package parameters;

import entities.DefaultGeneratorParameters;

public class DefaultParameters {
	
	/* --- Algorithm --- */
	public final static double CROSSOVER_PROBABILITY = 0.8;
	public final static double MUTATION_PROBABILITY = 1.0/DefaultGeneratorParameters.NUMBER_OF_TASKS;
	public final static int POPULATION_SIZE = 100;
	public final static int NUMBER_OF_ITERATIONS = 250;
	
	/* Experiment */
	public final static int TASKS_BY_EMPLOYEE = 5;
	public final static int MAX_PROBLEM_SIZE = 400;
	public final static int TEST_REPRODUCTION = 10;

}
