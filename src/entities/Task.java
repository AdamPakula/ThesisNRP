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
	 * @return the priority
	 */
	public Priority getPriority() {
		return priority;
	}

	/**
	 * @return the duration
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * @return the previous
	 */
	public List<Task> getPreviousTasks() {
		return previousTasks;
	}
	
	/**
	 * @return the requiredSkills
	 */
	public List<Skill> getRequiredSkills() {
		return requiredSkills;
	}
	
	
	/* --- Constructors --- */
	
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
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (getClass() != obj.getClass())
			return false;

		Task other = (Task) obj;

		return other.getName().equals(this.getName());
	}
	
	@Override
	public int hashCode() {
		return getName().length();
	}
}
