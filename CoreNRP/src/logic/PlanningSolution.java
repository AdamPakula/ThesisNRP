/**
 * A solution of the NRP
 * It contains a plannedTasks list which give the order of the tasks which are planned
 */
package logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;

import entities.Employee;
import entities.EmployeeWeekAvailability;
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
	private List<Task> undoneTasks;

	/**
	 * The end hour of the solution
	 * Is up to date only when isUpToDate field is true
	 */
	private double endDate;
	
	/**
	 * The employees' week planning
	 */
	private Map<Employee, List<EmployeeWeekAvailability>> employeesPlanning;
	
	
	/* --- Getters and Setters --- */

	/**
	 * Return the hour in all of the planned tasks will be done
	 * @return the end hour
	 */
	public double getEndDate() {
		return endDate;
	}
	
	/**
	 * Setter of the end date
	 * @param endDate the new end date of the solution
	 */
	public void setEndDate(double endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * Returns the number of tasks already planned
	 * @return The number of tasks already planned
	 */
	public int getNumberOfPlannedTasks() {
		return plannedTasks.size();
	}

	/**
	 * @return the plannedTasks
	 */
	public List<PlannedTask> getPlannedTasks() {
		return new ArrayList<>(plannedTasks);
	}
	
	/**
	 * Return the planned task at position in the list
	 * @param position The position in the list
	 * @return the planned task or null
	 */
	public PlannedTask getPlannedTask(int position) {
		if (position >= 0 && position < plannedTasks.size())
			return plannedTasks.get(position);
		return null;
	}
	
	/**
	 * Get a copy of the planned task from position in the original list
	 * @param beginPosition the begin position
	 * @return a copy of the end list
	 */
	public List<PlannedTask> getEndPlannedTasksSubListCopy(int beginPosition) {
		return new ArrayList<>(plannedTasks.subList(beginPosition, plannedTasks.size()));
	}
	
	/**
	 * private getter for undone tasks list
	 * @return the undone tasks list
	 */
	private List<Task> getUndoneTasks() {
		return undoneTasks;
	}
	
	/**
	 * @return the employeesPlannings
	 */
	public Map<Employee, List<EmployeeWeekAvailability>> getEmployeesPlanning() {
		return employeesPlanning;
	}

	/**
	 * @param employeesPlannings the employeesPlannings to set
	 */
	public void setEmployeesPlanning(Map<Employee, List<EmployeeWeekAvailability>> employeesPlanning) {
		this.employeesPlanning = employeesPlanning;
	}

	
	/* --- Constructors --- */

	/**
	 * Constructor
	 * initialize a random set of planned tasks
	 * @param problem
	 */
	public PlanningSolution(NextReleaseProblem problem) {
		super(problem);
	    numberOfViolatedConstraints = 0;

	    initializePlannedTaskVariables();
	    initializeObjectiveValues();
	}
	
	/**
	 * Constructor
	 * initialize a random set of planned tasks
	 * @param problem
	 */
	public PlanningSolution(NextReleaseProblem problem, List<PlannedTask> plannedTasks) {
		super(problem);
	    numberOfViolatedConstraints = 0;

	    undoneTasks = new CopyOnWriteArrayList<Task>();
		undoneTasks.addAll(problem.getTasks());
		this.plannedTasks = new CopyOnWriteArrayList<PlannedTask>();
		for (PlannedTask plannedTask : plannedTasks) {
			scheduleAtTheEnd(plannedTask.getTask(), plannedTask.getEmployee());
		}
	    initializeObjectiveValues();
	}

	/**
	 * Copy constructor
	 * @param planningSolution PlanningSoltion to copy
	 */
	public PlanningSolution(PlanningSolution planningSolution) {
		super(planningSolution.problem);

	    numberOfViolatedConstraints = planningSolution.numberOfViolatedConstraints;
	    
	    plannedTasks = new CopyOnWriteArrayList<>();
	    for (PlannedTask plannedTask : planningSolution.getPlannedTasks()) {
			plannedTasks.add(new PlannedTask(plannedTask));
		}
	    undoneTasks = new CopyOnWriteArrayList<>(planningSolution.getUndoneTasks());
	}
	
	
	/* --- Methods --- */
	
	/**
	 * Exchange the two tasks in positions pos1 and pos2
	 * @param pos1 The position of the first planned task to exchange
	 * @param pos2 The position of the second planned task to exchange
	 */
	public void exchange(int pos1, int pos2) {
		if (pos1 >= 0 && pos2 >= 0 && pos1 < plannedTasks.size() && pos2 < plannedTasks.size() && pos1 != pos2) {
			PlannedTask task1 = plannedTasks.get(pos1);
			plannedTasks.set(pos1, new PlannedTask(plannedTasks.get(pos2)));
			plannedTasks.set(pos2, new PlannedTask(task1));
		}
	}
	
	/**
	 * Calculate the sum of the priority of each task
	 * @return the priority score
	 */
	public double getPriorityScore() {
		double score = problem.getWorstScore();
		
		for (PlannedTask plannedTask : plannedTasks) {
			score -= plannedTask.getTask().getPriority().getScore();
		}
		
		return score;
	}
	
	/**
	 * Returns all of the planned tasks done by a specific employee
	 * @param e The employee
	 * @return The list of tasks done by the employee
	 */
	public List<PlannedTask> getTasksDoneBy(Employee e) {
		List<PlannedTask> tasksOfEmployee = new ArrayList<>();

		for (PlannedTask plannedTask : plannedTasks) {
			if (plannedTask.getEmployee() == e) {
				tasksOfEmployee.add(plannedTask);
			}
		}

		return tasksOfEmployee;
	}

	/**
	 * Return true if the task is already in the planned tasks
	 * @param task Task to search
	 * @return true if the task is already planned
	 */
	public boolean isAlreadyPlanned(Task task) {
		boolean found = false;
		Iterator<PlannedTask> it = plannedTasks.iterator();
		
		while (!found && it.hasNext()) {
			PlannedTask plannedTask = (PlannedTask) it.next();
			if (plannedTask.getTask().equals(task)) {
				found = true;
			}
		}
		
		return found;
	}

	/* --- Methods --- */
	
	/**
	 * Returns the planned task corresponding to the task given in parameter
	 * @param task The searched task
	 * @return The planned Task or null if it is not yet planned
	 */
	public PlannedTask findPlannedTask(Task task) {
		for (Iterator<PlannedTask> iterator = plannedTasks.iterator(); iterator.hasNext();) {
			PlannedTask plannedTask = iterator.next();
			if (plannedTask.getTask().equals(task)) {
				return plannedTask;
			}
		}
		
		return null;
	}
	
	/**
	 * Initializes the planned tasks randomly
	 * @param number the number of tasks to plan
	 */
	private void initializePlannedTasksRandomly(int number) {
		Task taskToDo;
		List<Employee> skilledEmployees;
		
		for (int i = 0 ; i < number ; i++) {
			taskToDo = undoneTasks.get(randomGenerator.nextInt(0, undoneTasks.size()-1));
			skilledEmployees = problem.getSkilledEmployees(taskToDo.getRequiredSkills().get(0));
			scheduleAtTheEnd(taskToDo,
					skilledEmployees.get(randomGenerator.nextInt(0, skilledEmployees.size()-1)));
		}
	}

	/**
	 * Initialize the variables
	 * Load a random number of planned tasks
	 */
	private void initializePlannedTaskVariables() {
		int numberOfTasks = problem.getTasks().size();
		int nbTasksToDo = randomGenerator.nextInt(0, numberOfTasks);
		
		undoneTasks = new CopyOnWriteArrayList<Task>();
		undoneTasks.addAll(problem.getTasks());
		plannedTasks = new CopyOnWriteArrayList<PlannedTask>();
	
		if (randomGenerator.nextDouble() < 0.3) {
			initializePlannedTasksRandomly(nbTasksToDo);
		}
		else {
			initializePlannedTasksWithPrecedences(nbTasksToDo);
		}
	}
	
	/**
	 * Initializes the planned tasks considering the precedences
	 * @param number the number of tasks to plan
	 */
	private void initializePlannedTasksWithPrecedences(int number) {
		Task taskToDo;
		List<Employee> skilledEmployees;
		List<Task> possibleTasks = updatePossibleTasks();
		
		for (int i = 0 ; i < number ; i++) {
			taskToDo = possibleTasks.get(randomGenerator.nextInt(0, possibleTasks.size()-1));
			skilledEmployees = problem.getSkilledEmployees(taskToDo.getRequiredSkills().get(0));
			scheduleAtTheEnd(taskToDo,
					skilledEmployees.get(randomGenerator.nextInt(0, skilledEmployees.size()-1)));
			possibleTasks = updatePossibleTasks();
		}
	}
	
	/**
	 * Reset the begin hours of all the planned task to 0.0
	 */
	public void resetBeginHours() {
		for (PlannedTask plannedTask : plannedTasks) {
			plannedTask.setBeginHour(0.0);
		}
	}
	
	/**
	 * Schedule a planned task to a position in the planning
	 * @param position the position of the planning
	 * @param plannedTask the planned task to integrate to the planning
	 */
	public void schedule(int position, Task task, Employee e) {
		undoneTasks.remove(task);
		plannedTasks.add(position, new PlannedTask(task, e));
	}
	
	/**
	 * Schedule a task in the planning
	 * Remove the task from the undoneTasks 
	 * and add the planned Task at the end of the planned tasks list
	 * @param plannedTask
	 */
	public void scheduleAtTheEnd(Task task, Employee e) {
		if (!isAlreadyPlanned(task)) {
			undoneTasks.remove(task);
			plannedTasks.add(new PlannedTask(task, e));
		}
	}
	
	/**
	 * Schedule a random undone task to a random place in the planning
	 */
	public void scheduleRandomTask() {
		scheduleRandomTask(randomGenerator.nextInt(0, plannedTasks.size()));
	}
	
	/**
	 * Schedule a random task to insertionPosition of the planning list
	 * @param insertionPosition the insertion position
	 */
	public void scheduleRandomTask(int insertionPosition) {
		if (undoneTasks.size() <= 0)
			return;
		Task newTask = undoneTasks.get(randomGenerator.nextInt(0, undoneTasks.size() -1)); //Maybe size-1
		List<Employee> skilledEmployees = problem.getSkilledEmployees(newTask.getRequiredSkills().get(0));
		Employee newEmployee = skilledEmployees.get(randomGenerator.nextInt(0, skilledEmployees.size()-1));
		schedule(insertionPosition, newTask, newEmployee);
	}
	
	/**
	 * Schedule the planned task at a random position in the planning
	 * @param plannedTask the plannedTask to integrate to the planning
	 */
	public void scheduleRandomly(PlannedTask plannedTask) {
		schedule(randomGenerator.nextInt(0, plannedTasks.size()), plannedTask.getTask(), plannedTask.getEmployee());
	}

	/**
	 * Unschedule a task : remove it from the planned tasks and add it to the undone ones
	 * <code>isUpToDate field becomes false
	 * @param plannedTask
	 */
	public void unschedule(PlannedTask plannedTask) {
		if (isAlreadyPlanned(plannedTask.getTask())) {
			undoneTasks.add(plannedTask.getTask());
			plannedTasks.remove(plannedTask);
				
		}
	}

	/**
	 * Updates the dates of each planned task
	 * Executes only if isUpToDate is false
	 * Updates he isUpToDate field to true
	 */
	/*public void updatePlanningDates() {
		if (!isUpToDate) {
			double newBeginHour;
			Map<Employee, Double> employeeAvailability = new HashMap<>();
			
			resetBeginHours();
			endDate = 0.0;
			
			for (PlannedTask plannedTask : plannedTasks) {
				newBeginHour = 0.0;
				Task currentTask = plannedTask.getTask();
				
				// Checks the previous tasks end hour
				for (Task previousTask : currentTask.getPreviousTasks()) {
					PlannedTask previousPlannedTask = findPlannedTask(previousTask);
					if (previousPlannedTask != null) {
						newBeginHour = Math.max(newBeginHour, previousPlannedTask.getEndHour());
					}
				}
				
				// Checks the employee availability
				Employee currentEmployee = plannedTask.getEmployee();
				Double employeeAvailableHour = employeeAvailability.get(currentEmployee);
				
				if (employeeAvailableHour == null) {
					employeeAvailableHour = new Double(0.0);
					employeeAvailability.put(currentEmployee, employeeAvailableHour);
				}
				else {
					newBeginHour = Math.max(newBeginHour, employeeAvailableHour);
				}
				
				plannedTask.setBeginHour(newBeginHour);
				plannedTask.setEndHour(newBeginHour + currentTask.getDuration());

				endDate = Math.max(plannedTask.getEndHour(), endDate);
				employeeAvailability.put(currentEmployee, plannedTask.getEndHour());
			}
			
			updateConstraints();
			setObjective(NextReleaseProblem.INDEX_PRIORITY_OBJECTIVE, getPriorityScore());
			setObjective(NextReleaseProblem.INDEX_END_DATE_OBJECTIVE, endDate);
			isUpToDate = true;
		}
	}*/

	/**
	 * Creates a list of the possible tasks to do regarding to the precedences of the undone tasks
	 * @return the list of the possible tasks to do
	 */
	private List<Task> updatePossibleTasks() {
		List<Task> possibleTasks = new ArrayList<>();
		boolean possible;
		int i;
		
		for (Task task : undoneTasks) {
			possible = true;
			i = 0;
			while (possible && i < task.getPreviousTasks().size()) {
				if (!isAlreadyPlanned(task.getPreviousTasks().get(i))) {
					possible = false;
				}
				i++;
			}
			if (possible) {
				possibleTasks.add(task);
			}
		}
		
		return possibleTasks;
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
		String lineSeparator = System.getProperty("line.separator");
		
		sb.append('(');
		for (int i = 0 ; i < getNumberOfObjectives() ; i++) {
			sb.append(getObjective(i)).append('\t');
		}
		
		sb.append(new NumberOfViolatedConstraints<>().getAttribute(this));
		sb.append(')').append(lineSeparator);
		
		for (PlannedTask task : getPlannedTasks()) {
			sb.append("-").append(task);
			sb.append(lineSeparator);
		}
		
		sb.append("End Date: ").append(getEndDate()).append(System.getProperty("line.separator"));
		
		return sb.toString();
	}
}
