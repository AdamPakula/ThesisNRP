package entities;

public class GeneratorParameters {
	
	/* --- Attributes --- */
	
	/**
	 * The number of tasks to generate
	 */
	private int numberOfTasks;
	
	/**
	 * The number of employees to generate
	 */
	private int numberOfEmployees;
	
	/**
	 * The number of skills used
	 */
	private int numberOfSkills;
	
	/**
	 * The rate of tasks with precedence constraints
	 */
	private double rateOfPrecedenceConstraints;

	
	/* --- Getters and setters --- */
	
	/**
	 * @return the numberOfTasks
	 */
	public int getNumberOfTasks() {
		return numberOfTasks;
	}

	/**
	 * @param numberOfTasks the numberOfTasks to set
	 */
	public void setNumberOfTasks(int numberOfTasks) {
		this.numberOfTasks = numberOfTasks;
	}

	/**
	 * @return the numberOfEmployees
	 */
	public int getNumberOfEmployees() {
		return numberOfEmployees;
	}

	/**
	 * @param numberOfEmployees the numberOfEmployees to set
	 */
	public void setNumberOfEmployees(int numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}

	/**
	 * @return the numberOfSkills
	 */
	public int getNumberOfSkills() {
		return numberOfSkills;
	}

	/**
	 * @param numberOfSkills the numberOfSkills to set
	 */
	public void setNumberOfSkills(int numberOfSkills) {
		this.numberOfSkills = numberOfSkills;
	}

	/**
	 * @return the rateOfPrecedenceConstraints
	 */
	public double getRateOfPrecedenceConstraints() {
		return rateOfPrecedenceConstraints;
	}

	/**
	 * @param rateOfPrecedenceConstraints the rateOfPrecedenceConstraints to set
	 */
	public void setRateOfPrecedenceConstraints(double rateOfPrecedenceConstraints) {
		this.rateOfPrecedenceConstraints = rateOfPrecedenceConstraints;
	}

	
	/* --- Constructors --- */
	
	/**
	 * Default constructors that initializes the parameters with their default values
	 * Default values can be found in the {@link DefaultGeneratorParameters} class
	 */
	public GeneratorParameters() {
		this(DefaultGeneratorParameters.NUMBER_OF_TASKS,
				DefaultGeneratorParameters.NUMBER_OF_EMPLOYEES,
				DefaultGeneratorParameters.NUMBER_OF_SKILLS,
				DefaultGeneratorParameters.PRECEDENCE_RATE);
	}
	
	/**
	 * @param numberOfTasks
	 * @param numberOfEmployees
	 * @param numberOfSkills
	 * @param rateOfPrecedenceConstraints
	 */
	public GeneratorParameters(int numberOfTasks, int numberOfEmployees, int numberOfSkills,
			double rateOfPrecedenceConstraints) {
		this.numberOfTasks = numberOfTasks;
		this.numberOfEmployees = numberOfEmployees;
		this.numberOfSkills = numberOfSkills;
		this.rateOfPrecedenceConstraints = rateOfPrecedenceConstraints;
	}
}
