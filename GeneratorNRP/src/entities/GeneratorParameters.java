package entities;

public class GeneratorParameters {
	
	/* --- Attributes --- */
	
	/**
	 * The number of features to generate
	 */
	private int numberOfFeatures;
	
	/**
	 * The number of employees to generate
	 */
	private int numberOfEmployees;
	
	/**
	 * The number of skills used
	 */
	private int numberOfSkills;
	
	/**
	 * The rate of features with precedence constraints
	 */
	private double rateOfPrecedenceConstraints;

	
	/* --- Getters and setters --- */
	
	/**
	 * @return the numberOfFeatures
	 */
	public int getNumberOfFeatures() {
		return numberOfFeatures;
	}

	/**
	 * @param numberOfFeatures the numberOfFeatures to set
	 */
	public void setNumberOfFeatures(int numberOfFeatures) {
		this.numberOfFeatures = numberOfFeatures;
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
		this(DefaultGeneratorParameters.NUMBER_OF_FEATURES,
				DefaultGeneratorParameters.NUMBER_OF_EMPLOYEES,
				DefaultGeneratorParameters.NUMBER_OF_SKILLS,
				DefaultGeneratorParameters.PRECEDENCE_RATE);
	}
	
	/**
	 * @param numberOfFeatures
	 * @param numberOfEmployees
	 * @param numberOfSkills
	 * @param rateOfPrecedenceConstraints
	 */
	public GeneratorParameters(int numberOfFeatures, int numberOfEmployees, int numberOfSkills,
			double rateOfPrecedenceConstraints) {
		this.numberOfFeatures = numberOfFeatures;
		this.numberOfEmployees = numberOfEmployees;
		this.numberOfSkills = numberOfSkills;
		this.rateOfPrecedenceConstraints = rateOfPrecedenceConstraints;
	}
}
