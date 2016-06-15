package entities;

import java.util.ArrayList;
import java.util.List;

public class EmployeeWeekAvailability {

	/**
	 * The begin hour of the employee in the week
	 */
	private double beginHour;
	
	/**
	 * The remain hours of the employee
	 */
	private double remainHoursAvailable;
	
	/**
	 * The end hour of the employee in the week
	 */
	private double endHour;
	
	/**
	 * The task done during the week
	 */
	private List<PlannedTask> plannedTasks;
	
	
	/* --- Getters and Setters --- */
	
	/**
	 * @return the remainHoursAvailable
	 */
	public double getRemainHoursAvailable() {
		return remainHoursAvailable;
	}

	/**
	 * @param remainHoursAvailable the remainHoursAvailable to set
	 */
	public void setRemainHoursAvailable(double remainHoursAvailable) {
		this.remainHoursAvailable = remainHoursAvailable;
	}

	/**
	 * @return the endHour
	 */
	public double getEndHour() {
		return endHour;
	}

	/**
	 * @param endHour the endHour to set
	 */
	public void setEndHour(double endHour) {
		this.endHour = endHour;
	}

	/**
	 * @return the beginHour
	 */
	public double getBeginHour() {
		return beginHour;
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
	public void addPlannedTask(PlannedTask plannedTask) {
		this.plannedTasks.add(plannedTask);
	}

	
	/* --- Constructors --- */

	/**
	 * Constructor
	 * @param beginHour the begin hour of the employee in the week
	 * @param remainHoursAvailable the number of hours the employee can do in the week
	 */
	public EmployeeWeekAvailability(double beginHour, double remainHoursAvailable) {
		this.beginHour = beginHour;
		this.remainHoursAvailable = remainHoursAvailable;
		endHour = beginHour;
		this.plannedTasks = new ArrayList<>();
	}
	
	
	public EmployeeWeekAvailability(EmployeeWeekAvailability origin) {
		this.beginHour = origin.getBeginHour();
		this.remainHoursAvailable = origin.getRemainHoursAvailable();
		this.endHour = origin.getEndHour();
		this.plannedTasks = new ArrayList<>(origin.getPlannedTasks().size());
		for (PlannedTask plannedTask : origin.getPlannedTasks()) {
			this.plannedTasks.add(new PlannedTask(plannedTask));
		}
	}
}
