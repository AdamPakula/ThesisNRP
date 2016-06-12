package parameters;

public class DefaultParameters {
	
	/* --- Iteration --- */
	public final static int NUMBER_OF_WEEK = 3;
	public final static int HOURS_BY_WEEK = 35;
	
	/* --- Generator --- */
	public final static int NUMBER_OF_TASKS = 20;
	public final static int NUMBER_OF_EMPLOYEES = 2;
	public final static int NUMBER_OF_SKILLS = 2;
	public final static double PRECEDENCE_RATE = 0.1;
	
	
	/* --- Algorithm --- */
	public final static int nbIterations = 500;
	public final static int populationSize = 100;
	public final static double crossoverProbability = 0.5;
	public final static double mutationProbability = 1.0/NUMBER_OF_TASKS;

}
