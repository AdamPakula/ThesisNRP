package Entities;

import java.util.List;

public class Task {

	/**
	 * The name of the task
	 */
	private String name;
	
	/**
	 * The priority of the task
	 * There is no special scale but we consider than the lowest number is the most important task
	 */
	private int priority;
	
	/**
	 * The duration of the task in hours
	 */
	private double duration;
	
	/**
	 * The tasks which needed to be executed before
	 */
	private List<Task> previous;

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
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
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
	public List<Task> getPrevious() {
		return previous;
	}

	/**
	 * @param previous the previous to set
	 */
	public void setPrevious(List<Task> previous) {
		this.previous = previous;
	}
	
	public Task(String name, int priority, Double duration) {
		this(name, priority, duration, null);
	}
	
	public Task(String name, int priority, Double duration, List<Task> previous) {
		this.name = name;
		this.priority = priority;
		this.duration = duration;
		this.previous = previous;
	}
}
