/**
 * 
 */
package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
		double minHour;
		
		resetBeginHours();
		
		for (PlannedTask plannedTask : plannedTasks) {
			minHour = 0.0;
			for (Task task : plannedTask.getTask().getPreviousTasks()) {
				minHour = Math.max(minHour, getBeginHour(task) + task.getDuration());
			}
			
			// TODO check employees
			// TODO check employee timetable
			plannedTask.setBeginHour(minHour);
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

	@Override
	public String getVariableValueString(int index) {
		return getVariableValueString(index).toString();
	}

	@Override
	public Solution<PlannedTask> copy() {
		return new PlanningSolution(this);
	}

}
