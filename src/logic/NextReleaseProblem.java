/**
 * 
 */
package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractGenericProblem;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import entities.Employee;
import entities.PlannedTask;
import entities.Priority;
import entities.Skill;
import entities.Task;

/**
 * @author Vavou
 * 
 * Objectives: 
 * 0: Doing the high score in priority
 *
 */
public class NextReleaseProblem extends AbstractGenericProblem<PlanningSolution> implements ConstrainedProblem<PlanningSolution> {

	/* --- Attributes --- */
	
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
	 * Number of violated constraints
	 */
	private NumberOfViolatedConstraints<PlanningSolution> numberOfViolatedConstraints;
	
	/**
	 * Overall constraint violation degree
	 */
	private OverallConstraintViolation<PlanningSolution> overallConstraintViolationDegree;
	
	/**
	 * Employees sorted by skill 
	 * An employee is in a the lists of all his skills
	 */
	private Map<Skill, List<Employee>> skilledEmployees;
	
	/**
	 * The index of the priority score objective in the objectives list
	 */
	public final static int INDEX_OBJECTIVE_PRIORITY = 0;
	
	/**
	 * The index of the end date objective in the objectives list
	 */
	public final static int INDEX_OBJECTIVE_END_DATE = 1;
	
	
	/* --- Constructors --- */

	/**
	 * Constructor
	 * @param tasks tasks of the iteration
	 * @param employees employees available during the iteration
	 */
	public NextReleaseProblem(List<Task> tasks, List<Employee> employees) {
		this.tasks = tasks;
		this.employees = employees;
		
		skilledEmployees = new HashMap<>();
		for (Employee employee : employees) {
			for (Skill skill : employee.getSkills()) {
				List<Employee> employeesList = skilledEmployees.get(skill);
				if (employeesList == null) {
					employeesList = new ArrayList<>();
					skilledEmployees.put(skill, employeesList);
				}
				employeesList.add(employee);
			}
		}
		
		setNumberOfVariables(1);
		setName("Next Release Problem");
		setNumberOfObjectives(2);
		
		int numberOfConstraints = 0;
		for (Task task : tasks) {
			numberOfConstraints += task.getPreviousTasks().size();
		}
		setNumberOfConstraints(numberOfConstraints);
		
		numberOfViolatedConstraints = new NumberOfViolatedConstraints<PlanningSolution>();
		overallConstraintViolationDegree = new OverallConstraintViolation<>();
	}
	
	
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
	 * Return the number of employees
	 * @return the number of employees
	 */
	public int getNumberOfEmployees() {
		return employees.size();
	}
	
	/**
	 * @return the employees with a skill
	 */
	public List<Employee> getEmployees(Skill skill) {
		return skilledEmployees.get(skill);
	}

	/**
	 * @param employees the employees to set
	 */
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
	
	/* --- Methods --- */
	
	@Override
	public void evaluate(PlanningSolution solution) {
		final int MAX_PRIORITY = tasks.size() * Priority.ONE.getScore();
		int priorityScore = 0;
		
		solution.schedule();
		
		for (int i = 0 ; i < solution.getPlannedTasks().size() ; i++) {
			priorityScore += solution.getPlannedTasks().get(i).getTask().getPriority().getScore();
		}
		
		solution.setObjective(INDEX_OBJECTIVE_PRIORITY, MAX_PRIORITY - priorityScore);
		solution.setObjective(INDEX_OBJECTIVE_END_DATE, evaluateEndDate(solution));
	}
	
	private double evaluateEndDate(PlanningSolution solution) {
		double endHour = 0.0;
		
		for (PlannedTask task : solution.getPlannedTasks()) {
			endHour = Math.max(endHour, task.getBeginHour() + task.getTask().getDuration());
		}
		
		return endHour;
	}

	/**
	 * @return the numberOfViolatedConstraints
	 */
	public NumberOfViolatedConstraints<PlanningSolution> getNumberOfViolatedConstraints() {
		return numberOfViolatedConstraints;
	}

	@Override
	public PlanningSolution createSolution() {
		return new PlanningSolution(this);
	}

	@Override
	public void evaluateConstraints(PlanningSolution solution) {
		int numViolatedConstraints = 0;
		for (int i = 0 ; i < solution.getPlannedTasks().size() ; i++) {
			Task currentTask = solution.getPlannedTasks().get(i).getTask();
			for (Task previousTask : currentTask.getPreviousTasks()) {
				boolean found = false;
				int j = 0;
				while (!found && j < i) { //TODO update condition when we will compare by time and not by order
					if (solution.getPlannedTasks().get(j).getTask() == previousTask) {
						found = true;
					}
					j++;
				}
				if (!found) {
					numViolatedConstraints++;
				}
			}
		}
		overallConstraintViolationDegree.setAttribute(solution, -1.0 * numViolatedConstraints);
		numberOfViolatedConstraints.setAttribute(solution, numViolatedConstraints);
	}
}
