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
import entities.Skill;
import entities.Task;

/**
 * @author Vavou
 * 
 * Objectives: 
 * 0: Doing the high score in priority
 * 1: The shortest endDate
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
	 * Number of violated constraints
	 */
	private OverallConstraintViolation<PlanningSolution> overallConstraintViolation;
	
	/**
	 * Employees sorted by skill 
	 * An employee is in a the lists of all his skills
	 */
	private Map<Skill, List<Employee>> skilledEmployees;

	/**
	 * The number of weeks of the iteration
	 */
	private int nbWeeks;
	
	/**
	 * The number of worked hours by week
	 */
	private double nbHoursByWeek;
	
	/**
	 * The priority score if there is no planned task
	 */
	private double worstScore;
	
	/**
	 * The index of the priority score objective in the objectives list
	 */
	public final static int INDEX_PRIORITY_OBJECTIVE = 0;
	
	/**
	 * The index of the end date objective in the objectives list
	 */
	public final static int INDEX_END_DATE_OBJECTIVE = 1;
	
	
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
	 * Returns the number of weeks of the iteration
	 * @return The number of weeks of the iteration
	 */
	public int getNbWeeks() {
		return nbWeeks;
	}
	
	/**
	 * Returns the number of worked hours by week
	 * @return the number of worked hours by week
	 */
	public double getNbHoursByWeek() {
		return nbHoursByWeek;
	}
	
	/**
	 * Return the number of employees
	 * @return the number of employees
	 */
	public int getNumberOfEmployees() {
		return employees.size();
	}
	
	/**
	 * @return the numberOfViolatedConstraints
	 */
	public NumberOfViolatedConstraints<PlanningSolution> getNumberOfViolatedConstraints() {
		return numberOfViolatedConstraints;
	}

	/**
	 * @return the employees with a skill
	 */
	public List<Employee> getSkilledEmployees(Skill skill) {
		return skilledEmployees.get(skill);
	}
	
	/**
	 * @return the list of the employees
	 */
	public List<Employee> getEmployees() {
		return employees;
	}
	
	/**
	 * @return the worstScore
	 */
	public double getWorstScore() {
		return worstScore;
	}


	/* --- Constructors --- */
	
	/**
	 * Constructor
	 * @param tasks tasks of the iteration
	 * @param employees employees available during the iteration
	 * @param nbWeeks The number of weeks of the iteration
	 */
	public NextReleaseProblem(List<Task> tasks, List<Employee> employees, int nbWeeks, double nbHoursByWeek) {
		this.tasks = tasks;
		this.employees = employees;
		this.nbWeeks = nbWeeks;
		this.nbHoursByWeek = nbHoursByWeek;
		
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
		initializeWorstScore();
		initializeNumberOfConstraint();
		
		numberOfViolatedConstraints = new NumberOfViolatedConstraints<PlanningSolution>();
		overallConstraintViolation = new OverallConstraintViolation<>();
	}
	
	
	/* --- Methods --- */
	
	private void initializeWorstScore() {
		worstScore = 0.0;
		for (Task task : tasks) {
			worstScore += task.getPriority().getScore();
		}
	}
	
	private void initializeNumberOfConstraint() {
		int numberOfConstraints = 0;
		
		//Precedences
		for (Task task : tasks) {
			numberOfConstraints += task.getPreviousTasks().size();
		}
		
		// Global overflow
		numberOfConstraints++;
		
		setNumberOfConstraints(numberOfConstraints);
	}


	@Override
	public PlanningSolution createSolution() {
		return new PlanningSolution(this);
	}

	@Override
	public void evaluate(PlanningSolution solution) {
		solution.updatePlanningDates();
		solution.setObjective(INDEX_PRIORITY_OBJECTIVE, solution.getPriorityScore());
	}

	@Override
	public void evaluateConstraints(PlanningSolution solution) {
		numberOfViolatedConstraints.setAttribute(solution, solution.getNumberOfViolatedConstraint());
		overallConstraintViolation.setAttribute(solution, solution.getNumberOfViolatedConstraint()*-1.0);
	}
}
