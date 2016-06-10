/**
 * 
 */
package entities;

import java.util.List;

/**
 * @author Vavou
 *
 */
public class ProblemData {
	
	/* --- Attributes --- */
	
	/**
	 * List of tasks
	 */
	private List<Task> tasks;
	
	/**
	 * List of employees
	 */
	private List<Employee> employees;
	
	/**
	 * List of skills
	 */
	private List<Skill> skills;

	
	/* --- Getters and setters --- */
	
	/**
	 * @return the tasks
	 */
	public List<Task> getTasks() {
		return tasks;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	/**
	 * @return the employees
	 */
	public List<Employee> getEmployees() {
		return employees;
	}

	/**
	 * @param employees the employees to set
	 */
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	/**
	 * @return the skills
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
	
	/* --- Constructors --- */

	/**
	 * @param tasks
	 * @param employees
	 * @param skills
	 */
	public ProblemData(List<Task> tasks, List<Employee> employees, List<Skill> skills) {
		this.tasks = tasks;
		this.employees = employees;
		this.skills = skills;
	}

}
