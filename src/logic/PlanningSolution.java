/**
 * 
 */
package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;

import entities.Employee;
import entities.PlannedTask;
import entities.Task;

/**
 * @author Vavou
 *
 */
public class PlanningSolution extends AbstractGenericSolution<PlannedTask, NextReleaseProblem> {

	/* --- Attributes --- */
	
	/**
	 * Generated Id
	 */
	private static final long serialVersionUID = 615615442782301271L;
	
	/**
	 * Tasks planned for the solution
	 */
	private List<PlannedTask> plannedTasks;
	
	/**
	 * Tasks unplanned for the solution
	 */
	private ArrayList<Task> undoneTasks;
	
	
	/* --- Getters and Setters --- */

	/**
	 * @return the undoneTasks
	 */
	public ArrayList<Task> getUndoneTasks() {
		return undoneTasks;
	}

	/**
	 * @return the plannedTasks
	 */
	public List<PlannedTask> getPlannedTasks() {
		return plannedTasks;
	}

	/**
	 * @param plannedTasks the plannedTasks to set
	 */
	public void setPlannedTasks(List<PlannedTask> plannedTasks) {
		this.plannedTasks = plannedTasks;
	}
	
	
	/* --- Constructors --- */
	
	protected PlanningSolution(NextReleaseProblem problem) {
		super(problem);
		overallConstraintViolationDegree = 0.0 ;
	    numberOfViolatedConstraints = 0 ;

	    initializePlannedTaskVariables();
	    initializeObjectiveValues();
	}

	/**
	 * Copy constructor
	 * @param planningSolution PlanningSoltion to copy
	 */
	public PlanningSolution(PlanningSolution planningSolution) {
		super(planningSolution.problem) ;

	    for (int i = 0; i < problem.getNumberOfVariables(); i++) {
	      setVariableValue(i, planningSolution.getVariableValue(i));
	    }

	    for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
	      setObjective(i, planningSolution.getObjective(i)) ;
	    }

	    overallConstraintViolationDegree = planningSolution.overallConstraintViolationDegree ;
	    numberOfViolatedConstraints = planningSolution.numberOfViolatedConstraints ;

	    attributes = new HashMap<Object, Object>(planningSolution.attributes) ;
	    
	    plannedTasks = new ArrayList<>(planningSolution.getPlannedTasks());
	    undoneTasks = new ArrayList<>(planningSolution.getUndoneTasks());
	}
	
	
	/* --- Methods --- */
	
	/**
	 * Initialize the variables
	 */
	private void initializePlannedTaskVariables() {
		int numberOfTasks = problem.getTasks().size();
		
		int nbTasksToDo = randomGenerator.nextInt(0, numberOfTasks);
		
		undoneTasks = new ArrayList<Task>(problem.getTasks());
		plannedTasks = new ArrayList<PlannedTask>(nbTasksToDo);

		Task taskToDo;
		List<Employee> employees;
		
		for (int i = 0 ; i < nbTasksToDo ; i++) {
			taskToDo = undoneTasks.get(randomGenerator.nextInt(0, undoneTasks.size()-1));
			employees = problem.getEmployees(taskToDo.getRequiredSkills().get(0));
			plannedTasks.add(new PlannedTask(
				taskToDo,
				employees.get(randomGenerator.nextInt(0, employees.size()-1))));
			undoneTasks.remove(taskToDo);
		}
	}
	
	public void schedule() {
		double newBeginHour;
		Map<Employee, Double> employeeAvailability = new HashMap<>();
		
		resetBeginHours();
		
		for (PlannedTask plannedTask : plannedTasks) {
			newBeginHour = 0.0;
			Task currentTask = plannedTask.getTask();
			
			// Checks the previous tasks end hour
			for (Task previousTask : currentTask.getPreviousTasks()) {
				newBeginHour = Math.max(newBeginHour, getBeginHour(previousTask) + previousTask.getDuration());
			}
			
			// Checks the employee availability
			Employee currentEmployee = plannedTask.getEmployee();
			Double employeeAvailableHour = employeeAvailability.get(currentEmployee);
			if (employeeAvailableHour != null) {
				newBeginHour = Math.max(newBeginHour, employeeAvailableHour.doubleValue());
			}
			
			// TODO check employee timetable
			plannedTask.setBeginHour(newBeginHour);
			employeeAvailability.put(currentEmployee, new Double(newBeginHour + currentTask.getDuration()));
		}
	}
	
	/**
	 * Reset the begin hours of all the planned task to 0.0
	 */
	private void resetBeginHours() {
		for (PlannedTask plannedTask : plannedTasks) {
			plannedTask.setBeginHour(0.0);
		}
	}
	
	/**
	 * Get the begin hour of a planned task wich includes the parameter task
	 * @param task
	 * @return the begin hour of the planned task or 0.0 if it is not yet planned
	 */
	private double getBeginHour(Task task) {
		for (PlannedTask plannedTask : plannedTasks) {
			if (plannedTask.getTask() == task) {
				return plannedTask.getBeginHour();
			}
		}
		return 0.0;
	}
	
	/**
	 * Return the hour in all of the planned tasks will be done
	 * @return the end hour
	 */
	public double getEndDate() {
		double endHour = 0.0;

		for (PlannedTask task : plannedTasks) {
			endHour = Math.max(endHour, task.getBeginHour() + task.getTask().getDuration());
		}

		return endHour;
	}

	@Override
	public String getVariableValueString(int index) {
		return getVariableValueString(index).toString();
	}

	@Override
	public Solution<PlannedTask> copy() {
		return new PlanningSolution(this);
	}
	
	@Override
	public int hashCode() {
		return getPlannedTasks().size();
	};
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (getClass() != obj.getClass())
			return false;

		PlanningSolution other = (PlanningSolution) obj;
		
		int size = this.getPlannedTasks().size();
		boolean equals = other.getPlannedTasks().size() == size;
		int i = 0;
		while (equals && i < size) {
			if (!other.getPlannedTasks().contains(this.getPlannedTasks().get(i))) {
				equals = false;
			}
			i++;
		}

		return equals;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (PlannedTask task : getPlannedTasks()) {
			sb.append("-").append(task.getTask().getName())
				.append(" done by ").append(task.getEmployee().getName())
				.append(" at hour " + task.getBeginHour());
			sb.append(System.getProperty("line.separator"));
		}
		
		return sb.toString();
	}
}
