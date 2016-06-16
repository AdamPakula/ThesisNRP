package parameters;

public class DefaultParameters {
	
	/* --- Iteration --- */
	public final static int NUMBER_OF_WEEK = 3;
	public final static double HOURS_BY_WEEK = 35.0;
	
	/* --- Generator --- */
	public final static int NUMBER_OF_TASKS = 20;
	public final static int NUMBER_OF_EMPLOYEES = 2;
	public final static int NUMBER_OF_SKILLS = 2;
	public final static double PRECEDENCE_RATE = 0.1;
	
	
	/* --- Algorithm --- */
	public final static int NUMBER_OF_ITERATIONS = 250;
	public final static int POPULATION_SIZE = 100;
	public final static double CROSSOVER_PROBABILITY = 0.5;
	public final static double MUTATION_PROBABILITY = 1.0/NUMBER_OF_TASKS;
	
	
	/* Experiment */
	public final static int TASKS_BY_EMPLOYEE = 5;
	public final static int MAX_PROBLEM_SIZE = 400;

}
