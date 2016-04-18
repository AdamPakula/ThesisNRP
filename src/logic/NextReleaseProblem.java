/**
 * 
 */
package logic;

import java.util.List;

import org.uma.jmetal.problem.impl.AbstractGenericProblem;

import entities.Employee;
import entities.Priority;
import entities.Task;

/**
 * @author Vavou
 * 
 * Objectives: 
 * 0: doing the most number of tasks
 *
 */
public class NextReleaseProblem extends AbstractGenericProblem<PlanningSolution> {

	/**
	 * Generated Id
	 */
	private static final long serialVersionUID = 3302475694747789178L;
	
	/**
	 * Tasks available for the iteration
	 */
	private List<Task> tasks;
	
	/**
	 * Employees available for the iteration
	 */
	private List<Employee> employees;

	/**
	 * Constructor
	 * @param tasks tasks of the iteration
	 * @param employees employees available during the iteration
	 */
	public NextReleaseProblem(List<Task> tasks, List<Employee> employees) {
		this.tasks = tasks;
		this.employees = employees;
		
		setNumberOfVariables(1);
		setName("Next Release Problem");
		setNumberOfObjectives(1);
	}
	
	@Override
	public void evaluate(PlanningSolution solution) {
		final int MAX_PRIORITY = tasks.size() * Priority.ONE.getScore();
		int totalScore = 0;
		
		for (int i = 0 ; i < solution.getPlannedTasks().size() ; i++) {
			totalScore += solution.getPlannedTasks().get(i).getTask().getPriority().getScore();
		}
		solution.setObjective(0, MAX_PRIORITY - totalScore);
	}

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

	@Override
	public PlanningSolution createSolution() {
		return new PlanningSolution(this);
	}

}
