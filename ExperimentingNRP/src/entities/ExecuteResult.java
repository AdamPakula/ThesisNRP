package entities;

import logic.PlanningSolution;

public class ExecuteResult {

	/**
	 * The result solution
	 */
	private PlanningSolution solution;
	
	/**
	 * The computing time
	 */
	private long computingTime;

	/**
	 * @return the solution
	 */
	public PlanningSolution getSolution() {
		return solution;
	}

	/**
	 * @param solution the solution to set
	 */
	public void setSolution(PlanningSolution solution) {
		this.solution = solution;
	}

	/**
	 * @return the computingTime
	 */
	public long getComputingTime() {
		return computingTime;
	}

	/**
	 * @param computingTime the computingTime to set
	 */
	public void setComputingTime(long computingTime) {
		this.computingTime = computingTime;
	}
	
	public ExecuteResult(PlanningSolution solution, long computingTime) {
		this.solution = solution;
		this.computingTime = computingTime;
	}
}
