package entities;

import java.util.ArrayList;
import java.util.List;

public class Task {

	/* --- Atributes --- */
	
	/**
	 * The name of the task
	 */
	private String name;
	
	/**
	 * The priority of the task
	 * There is no special scale but we consider than the lowest number is the most important task
	 */
	private Priority priority;
	
	/**
	 * The duration of the task in hours
	 */
	private double duration;
	
	/**
	 * The tasks which needed to be executed before
	 */
	private List<Task> previousTasks;
	
	/**
	 * The skills required to do the task
	 */
	private List<Skill> requiredSkills;

	
	/* --- Getters and setters --- */
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the priority
	 */
	public Priority getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	/**
	 * @return the duration
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(double duration) {
		this.duration = duration;
	}

	/**
	 * @return the previous
	 */
	public List<Task> getPreviousTasks() {
		return previousTasks;
	}

	/**
	 * @param previous the previous to set
	 */
	public void setPreviousTasks(List<Task> previous) {
		this.previousTasks = previous;
	}
	
	/**
	 * @return the requiredSkills
	 */
	public List<Skill> getRequiredSkills() {
		return requiredSkills;
	}

	/**
	 * @param requiredSkills the requiredSkills to set
	 */
	public void setRequiredSkills(List<Skill> requiredSkills) {
		this.requiredSkills = requiredSkills;
	}
	
	
	/* --- Constructors --- */

	/**
	 * Construct a task which not needs skills and not have previous mandatory tasks
	 * @param name the name of the task
	 * @param priority the priority of the task
	 * @param duration the duration of the task
	 */
	public Task(String name, Priority priority, Double duration) {
		this(name, priority, duration, null, null);
	}
	
	/**
	 * Construct a task
	 * @param name the name of the task
	 * @param priority the priority of the task
	 * @param duration the duration of the task
	 * @param previousTasks the list of the previous tasks or null
	 * @param requiredSkills the required skills to do this task
	 */
	public Task(String name, Priority priority, Double duration, List<Task> previousTasks, List<Skill> requiredSkills) {
		this.name = name;
		this.priority = priority;
		this.duration = duration;
		this.previousTasks = previousTasks == null ? new ArrayList<Task>() : previousTasks;
		this.requiredSkills = requiredSkills == null ? new ArrayList<Skill>() : requiredSkills;
	}
}
