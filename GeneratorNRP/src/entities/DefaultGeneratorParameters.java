package entities;


/**
 * The defaults values for the generator parameters
 * @author Vavou
 *
 */
public class DefaultGeneratorParameters {

	/**
	 * The default number of tasks to generate
	 */
	public final static int NUMBER_OF_FEATURES = 20;
	
	/**
	 * The default number of employees to generate
	 */
	public final static int NUMBER_OF_EMPLOYEES = 4;
	
	/**
	 * The default number of skills to generate
	 */
	public final static int NUMBER_OF_SKILLS = 2;
	
	/**
	 * The default precedence rate for the generation
	 */
	public final static double PRECEDENCE_RATE = 0.3;
	
	/**
	 * The max duration of a feature
	 */
	public final static double MAX_FEATURE_DURATION = 40.0;
	
	/**
	 * The max employee week availability
	 */
	public final static double MAX_EMPLOYEE_WEEK_AVAILABILITY = 35.0;
	
	/**
	 * The min employee week availability
	 */
	public final static double MIN_EMPLOYEE_WEEK_AVAILABILITY = 5.0;
}
