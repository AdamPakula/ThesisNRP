/**
 * 
 */
package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;

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
	
	public PlanningSolution(NextReleaseProblem problem) {
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
	    
	    plannedTasks = new ArrayList<>();
	    for (PlannedTask task : planningSolution.getPlannedTasks()) {
	    	plannedTasks.add(new PlannedTask(task));
	    }
	    undoneTasks = new ArrayList<>(planningSolution.getUndoneTasks());
	}
	
	
	/* --- Methods --- */
	
	/**
	 * Initialize the variables
	 */
	private void initializePlannedTaskVariables() {
		int numberOfTasks = problem.getTasks().size();
		int numberOfEmployees = problem.getEmployees().size();
		
		int nbTasksToDo = randomGenerator.nextInt(0, numberOfTasks);
		
		undoneTasks = new ArrayList<Task>(problem.getTasks());
		
		plannedTasks = new ArrayList<PlannedTask>(nbTasksToDo);

		int hightEmployeeGeneratorLimit = numberOfEmployees-1;
		
		int numTaskToDo;
		
		for (int i = 0 ; i < nbTasksToDo ; i++) {
			numTaskToDo = randomGenerator.nextInt(0, undoneTasks.size()-1);
			plannedTasks.add(new PlannedTask(
				undoneTasks.get(numTaskToDo),
				problem.getEmployees().get(randomGenerator.nextInt(0, hightEmployeeGeneratorLimit))));
			undoneTasks.remove(numTaskToDo);
		}
	}

	@Override
	public String getVariableValueString(int index) {
		return getVariableValueString(index).toString();
	}

	@Override
	public PlanningSolution copy() {
		return new PlanningSolution(this);
	}

}
