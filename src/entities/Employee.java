package entities;

import java.util.List;

/**
 * Describes an employee which can realize a task
 * @author Vavou
 *
 */
public class Employee {

	/**
	 * The name of the employee
	 */
	private String name;
	
	/**
	 * The skills of the employee
	 */
	private List<Skill> skills;
	
	/**
	 * The available number of hours per week
	 */
	private double weekAvailability;

	/**
	 * Returns the name of the employee
	 * @return the name of the employee
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the employee
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the skills of the employee
	 * @return the skills of the employee
	 */
	public List<Skill> getSkills() {
		return skills;
	}

	/**
	 * @param skills the skills to set
	 */
	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	/**
	 * @return the weekAvailability
	 */
	public double getWeekAvailability() {
		return weekAvailability;
	}

	/**
	 * @param weekAvailability the weekAvailability to set
	 */
	public void setWeekAvailability(double weekAvailability) {
		this.weekAvailability = weekAvailability;
	}
	
	/**
	 * Constructs a new employee
	 * @param name
	 * @param weekAvailability in hours per week
	 */
	public Employee(String name, double weekAvailability) {
		this.name = name;
		this.weekAvailability = weekAvailability;
	}
}
