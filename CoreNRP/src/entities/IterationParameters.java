/**
 * 
 */
package entities;

/**
 * @author Vavou
 *
 */
public class IterationParameters {
	
	/**
	 * The number of week of the iteration
	 */
	private int numberOfWeek;
	
	/**
	 * The number of worked hours by week
	 */
	private int hoursByWeek;
	
	/**
	 * @return the numberOfWeek
	 */
	public int getNumberOfWeek() {
		return numberOfWeek;
	}

	/**
	 * @param numberOfWeek the numberOfWeek to set
	 */
	public void setNumberOfWeek(int numberOfWeek) {
		this.numberOfWeek = numberOfWeek;
	}

	/**
	 * @return the hoursByWeek
	 */
	public int getHoursByWeek() {
		return hoursByWeek;
	}

	/**
	 * @param hoursByWeek the hoursByWeek to set
	 */
	public void setHoursByWeek(int hoursByWeek) {
		this.hoursByWeek = hoursByWeek;
	}

	/**
	 * Constructor
	 * @param numberOfWeek The number of week of the iteration
	 * @param hoursByWeek The number of worked hours by week
	 */
	public IterationParameters(int numberOfWeek, int hoursByWeek) {
		this.numberOfWeek = numberOfWeek;
		this.hoursByWeek = hoursByWeek;
	}

}
