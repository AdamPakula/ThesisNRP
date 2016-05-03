/**
 * 
 */
package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

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
	private List<Task> undoneTasks;
	
	/**
	 * A boolean that save performance, calculating the end date only when changes have been done
	 */
	private boolean isUpToDate = false;

	/**
	 * The end hour of the solution
	 * Is up to date only when isUpToDate field is true
	 */
	private double endDate;
	
	
	/* --- Getters and Setters --- */

	/**
	 * Return the hour in all of the planned tasks will be done
	 * @return the end hour
	 */
	public double getEndDate() {
		if (!isUpToDate) {
			updatePlanningDates();
		}
	
		return endDate;
	}
	
	/**
	 * Returns the number of tasks already planned
	 * @return The number of tasks already planned
	 */
	public int getNumberOfPlannedTasks() {
		return plannedTasks.size();
	}
	
	/**
	 * Get the number of violated constraint
	 * update the planning if it is not up to date
	 * @return the number of violated constraints
	 */
	public int getNumberOfViolatedConstraint() {
		if (!isUpToDate) {
			updatePlanningDates();
		}
		
		return numberOfViolatedConstraints;
	}

	/**
	 * @return the plannedTasks
	 */
	private List<PlannedTask> getPlannedTasks() {
		return plannedTasks;
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
	
	public void setPlannedTasks(List<PlannedTask> list) {
		isUpToDate = false;
		plannedTasks = list;
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
	
	/* --- Constructors --- */
	
	/**
	 * @return the isUpToDate
	 */
	public boolean isUpToDate() {
		return isUpToDate;
	}

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
	    isUpToDate = false;
	}
	
	
	/* --- Methods --- */
	
	/**
	 * Exchange the two tasks in positions pos1 and pos2
	 * @param pos1 The position of the first planned task to exchange
	 * @param pos2 The position of the second planned task to exchange
	 */
	public void exchange(int pos1, int pos2) {
		if (pos1 >= 0 && pos2 >= 0 && pos1 < plannedTasks.size() && pos2 < plannedTasks.size() && pos1 != pos2) {
			isUpToDate = false;
			PlannedTask task1 = plannedTasks.get(pos1);
			plannedTasks.set(pos1, new PlannedTask(plannedTasks.get(pos2)));
			plannedTasks.set(pos2, new PlannedTask(task1));
		}
	}
	
	/**
	 * Calculate the sum of the priority of each task
	 * @return the priority score
	 */
	public int getPriorityScore() {
		int score = 0;
		updatePlanningDates();
		
		for (PlannedTask plannedTask : plannedTasks) {
			score += plannedTask.getTask().getPriority().getScore();
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
			if (plannedTask.getTask() == task) {
				found = true;
			}
		}
		
		return found;
	}

	/* --- Methods --- */
	
	private PlannedTask findPlannedTask(Task task) {
		for (Iterator<PlannedTask> iterator = plannedTasks.iterator(); iterator.hasNext();) {
			PlannedTask plannedTask = iterator.next();
			if (plannedTask.getTask().equals(task)) {
				return plannedTask;
			}
		}
		
		return null;
	}
	
	/**
	 * Initialize the variables
	 * Load a random number of planned tasks
	 */
	private void initializePlannedTaskVariables() {
		int numberOfTasks = problem.getTasks().size();
		int nbTasksToDo = randomGenerator.nextInt(0, numberOfTasks);
		
		undoneTasks = new CopyOnWriteArrayList<Task>(problem.getTasks());
		plannedTasks = new CopyOnWriteArrayList<PlannedTask>();
	
		Task taskToDo;
		List<Employee> skiledEmployees;
		
		for (int i = 0 ; i < nbTasksToDo ; i++) {
			taskToDo = undoneTasks.get(randomGenerator.nextInt(0, undoneTasks.size()-1));
			skiledEmployees = problem.getEmployees(taskToDo.getRequiredSkills().get(0));
			scheduleAtTheEnd(taskToDo,
					skiledEmployees.get(randomGenerator.nextInt(0, skiledEmployees.size()-1)));
		}
	}

	/**
	 * Reset the begin hours of all the planned task to 0.0
	 */
	private void resetBeginHours() {
		for (PlannedTask plannedTask : plannedTasks) {
			plannedTask.setBeginHour(0.0);
		}
		isUpToDate = false;
	}
	
	/**
	 * Schedule a planned task to a position in the planning
	 * @param position the position of the planning
	 * @param plannedTask the planned task to integrate to the planning
	 */
	public void schedule(int position, Task task, Employee e) {
		isUpToDate = false;
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
		isUpToDate = false;
		undoneTasks.remove(task);
		plannedTasks.add(new PlannedTask(task, e));
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
		List<Employee> skilledEmployees = problem.getEmployees(newTask.getRequiredSkills().get(0));
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
		isUpToDate = false;
		undoneTasks.add(plannedTask.getTask());
		plannedTasks.remove(plannedTask);
	}

	/**
	 * Updates the dates of each planned task
	 * Executes only if isUpToDate is false
	 * Updates he isUpToDate field to true
	 */
	public void updatePlanningDates() {
		if (!isUpToDate) {
			double newBeginHour;
			Map<Employee, Double[][]> employeeAvailability = new HashMap<>();
			
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
				Double employeeAvailableHour[][] = employeeAvailability.get(currentEmployee);
				// 0 : min available hour in the week
				// 1 : number of remain hours in the week
				int currentWeek = 0;
				
				if (employeeAvailableHour == null) {
					employeeAvailableHour = new Double[problem.getNbWeeks()][];
					// Initialization of the table of availability
					for (int i = 0 ; i < problem.getNbWeeks() ; i++) {
						employeeAvailableHour[i] = new Double[2];
						employeeAvailableHour[i][0] = i * problem.getNbHoursByWeek();
						employeeAvailableHour[i][1] = 0.0;
					}
					employeeAvailability.put(currentEmployee, employeeAvailableHour);
				}
				else {
					int i = problem.getNbWeeks() -1;
					boolean found = false;
					while (!found && i >= 0) {
						if (employeeAvailableHour[i][1] != 0.0) {
							found = true;
							currentWeek = i;
						}
						i--;
					}
					newBeginHour = Math.max(newBeginHour, employeeAvailableHour[currentWeek][0]);
				}
				
				double remainHours = currentTask.getDuration();
				plannedTask.setBeginHour(newBeginHour);
				
				do {
					double endWeek = (currentWeek + 1) * problem.getNbHoursByWeek();
					if (remainHours < currentEmployee.getWeekAvailability() - employeeAvailableHour[currentWeek][1]
							&& remainHours + employeeAvailableHour[currentWeek][0] < endWeek) { // If the task can be terminated in this week
						employeeAvailableHour[currentWeek][0] = Math.max(newBeginHour, employeeAvailableHour[currentWeek][0]) + remainHours;
						employeeAvailableHour[currentWeek][1] -= remainHours;
						plannedTask.setEndHour(employeeAvailableHour[currentWeek][0]);
						
						remainHours = 0.0;
					}
					else {
						double nbHoursToAddInThisWeek = Math.min(endWeek - remainHours, 
								employeeAvailableHour[currentWeek][1] - remainHours);
						if (nbHoursToAddInThisWeek > 0.0) { // If we can add some hours
							employeeAvailableHour[currentWeek][0] += nbHoursToAddInThisWeek;
							employeeAvailableHour[currentWeek][1] -= nbHoursToAddInThisWeek;
							remainHours -= nbHoursToAddInThisWeek;
						}
					}
					
					currentWeek++;
				} while (currentWeek < problem.getNbWeeks() && remainHours > 0.0 );
				
				if (remainHours > 0.0) {
					//TODO Could no be added so constraint ++
				}
				
				endDate = Math.max(plannedTask.getEndHour(), endDate);
			}
			updateConstraints();
			isUpToDate = true;
		}
	}

	private void updateConstraints() {
		if (!isUpToDate) {
			int numViolatedConstraints = 0;		
			Iterator<PlannedTask> iterator = plannedTasks.iterator();
			
			while (iterator.hasNext()) {
				PlannedTask currentTask = iterator.next();
				for (Task previousTask : currentTask.getTask().getPreviousTasks()) {
					boolean found = false;
					int j = 0;
					while (!found && plannedTasks.get(j) != currentTask) { //TODO update condition when we will compare by time and not by order
						if (plannedTasks.get(j).getTask() == previousTask) {
							found = true;
						}
						j++;
					}
					if (!found) {
						numViolatedConstraints++;
					}
				}
			}
			
			this.numberOfViolatedConstraints = numViolatedConstraints;
		}
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
