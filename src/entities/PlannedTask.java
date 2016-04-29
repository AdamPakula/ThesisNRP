package entities;

import java.util.Objects;

/**
 * Describes a task in a planning
 * @author Vavou
 *
 */
public class PlannedTask {
	
	/**
	 * The task to do
	 */
	private Task task;
	
	/**
	 * The begin hour of the planned task
	 */
	private double beginHour;
	
	/**
	 * The employee who will do the task
	 */
	private Employee employee;

	/**
	 * @return the task
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(Task task) {
		this.task = task;
	}

	/**
	 * @return the beginHour
	 */
	public double getBeginHour() {
		return beginHour;
	}

	/**
	 * @param beginHour the beginHour to set
	 */
	public void setBeginHour(double beginHour) {
		this.beginHour = beginHour;
	}

	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public PlannedTask(Task task, Employee employee) {
		this.task = task;
		this.employee = employee;
		beginHour = 0.0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (getClass() != obj.getClass())
			return false;

		PlannedTask other = (PlannedTask) obj;

		return Objects.equals(other.getTask(), this.getTask()) &&
				Objects.equals(other.getEmployee(), this.getEmployee()) &&
				Objects.equals(other.getBeginHour(), this.getBeginHour());
	}
}
