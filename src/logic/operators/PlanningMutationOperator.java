/**
 * 
 */
package logic.operators;

import java.util.List;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import entities.Employee;
import entities.PlannedTask;
import entities.Task;
import logic.NextReleaseProblem;
import logic.PlanningSolution;

/**
 * @author Vavou
 *
 */
public class PlanningMutationOperator implements MutationOperator<PlanningSolution> {

	/* --- Attributes --- */
	
	/**
	 * Generated Id
	 */
	private static final long serialVersionUID = 6178932989907368331L;
	
	/**
	 * The number of tasks of the problem
	 */
	private int numberOfTasks;

	/**
	 * Mutation probability between 0.0 and 1.0
	 */
	private double mutationProbability;

	/**
	 * Random generator
	 */
	private JMetalRandom randomGenerator;

	/**
	 * The Next Release Problem which contents the employees and tasks list
	 */
	private NextReleaseProblem problem;

	/* --- Getters and setters --- */
	
	/**
	 * @return the mutationProbability
	 */
	public double getMutationProbability() {
		return mutationProbability;
	}
	
	/* --- Constructors */
	
	/**
	 * Constructor
	 * @param problem The problem
	 * @param mutationProbability The mutation probability between 0.0 and 1.0
	 */
	public PlanningMutationOperator(NextReleaseProblem problem, double mutationProbability) {
		if (mutationProbability < 0) {
			throw new JMetalException("Mutation probability is negative: " + mutationProbability) ;
		}
		
		this.numberOfTasks = problem.getTasks().size();
		this.mutationProbability = mutationProbability;
		this.problem = problem;
		randomGenerator = JMetalRandom.getInstance() ;
	}
	
	/* --- Methods --- */
	
	@Override
	public PlanningSolution execute(PlanningSolution source) {
		for (int i = 0 ; i < source.getPlannedTasks().size() ; i++) {
			if (doMutation()) { // If we have to do a mutation
				PlannedTask taskToMutate = source.getPlannedTasks().get(i);
				if (randomGenerator.nextDouble() < 0.5) {
					changeEmployee(taskToMutate);
				}
				else {
					changeTask(source, taskToMutate, i);
				}
			}
		}
		for (int i = 0 ; i < source.getUndoneTasks().size() ; i++) {
			if (doMutation()) {
				addNewTask(source);
			}
		}
		
		return source;
	}
	
	/**
	 * Defines if we do or not the mutation
	 * It randomly chose a number and checks if it is lower than the mutation probability
	 * @return true if the mutation must be done
	 */
	private boolean doMutation() {
		return randomGenerator.nextDouble() <= mutationProbability;
	}

	/**
	 * Add an random unplanned task in the planning
	 * - chose randomly an unplanned task
	 * - remove it from the unplanned tasks list of the solution
	 * - chose randomly an employee
	 * - create and add the planned task with the chosen task and employee
	 * @param solution the solution to mutate
	 */
	private void addNewTask(PlanningSolution solution) {
		int insertionPosition = randomGenerator.nextInt(0, solution.getPlannedTasks().size());
		int removePosition = randomGenerator.nextInt(0, solution.getUndoneTasks().size()-1);
		Task newTask = solution.getUndoneTasks().get(removePosition);
		List<Employee> skilledEmployees = problem.getEmployees(newTask.getRequiredSkills().get(0));
		Employee newEmployee = skilledEmployees.get(randomGenerator.nextInt(0, skilledEmployees.size()-1));
		solution.getUndoneTasks().remove(removePosition);
		solution.getPlannedTasks().add(insertionPosition, new PlannedTask(newTask, newEmployee));
	}
	
	/**
	 * Replaces a task by another one.
	 * It can be a planned or an unplanned task, it updates the unplannedTasks list in the second case
	 * @param solution The solution to mutate
	 * @param taskToChange The planned task to modify
	 * @param taskPosition The position of the task to modify in the planning (the plannedTask list)
	 */
	private void changeTask(PlanningSolution solution, PlannedTask taskToChange, int taskPosition) {
		int randomNumTask = randomGenerator.nextInt(0, numberOfTasks - 1);
		int numberOfPlannedTasks = solution.getPlannedTasks().size();
		if (randomNumTask < numberOfPlannedTasks) { // If the random selected task is already planned then exchange with the current
			PlannedTask otherToChange = solution.getPlannedTasks().get(randomNumTask);
			solution.getPlannedTasks().set(taskPosition, new PlannedTask(otherToChange.getTask(), otherToChange.getEmployee()));
			solution.getPlannedTasks().set(randomNumTask, new PlannedTask(taskToChange.getTask(), taskToChange.getEmployee()));
		}
		else { // If the random selected task is not yet planned, let's change it
			int positionNewTask = randomNumTask - numberOfPlannedTasks;
			Task taskToAdd = solution.getUndoneTasks().remove(positionNewTask);
			solution.getUndoneTasks().add(taskToChange.getTask());
			solution.getPlannedTasks().set(taskPosition, new PlannedTask(taskToAdd, taskToChange.getEmployee()));
		}
	}
	
	/**
	 * Change the employee of a planned task by a random one
	 * @param taskToChange the planned task to modify
	 */
	private void changeEmployee(PlannedTask taskToChange) {
		List<Employee> skilledEmployees = problem.getEmployees(taskToChange.getTask().getRequiredSkills().get(0));
		if (skilledEmployees.size() > 1) {
			taskToChange.setEmployee(skilledEmployees.get(randomGenerator.nextInt(0, skilledEmployees.size()-1)));
		}
	}
}
