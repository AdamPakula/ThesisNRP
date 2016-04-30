/**
 * 
 */
package entities;

/**
 * @author Vavou
 *
 */
public enum Priority {
	ONE(1, 160),
	TWO(2, 80),
	THREE(3, 40),
	FOUR(4, 20),
	FIVE(5, 10);
	
	/**
	 * The priority level
	 */
	private int level;
	
	/**
	 * The score of the priority
	 */
	private int score;
	
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Constructor
	 * @param level the level of the priority
	 * @param score the score of the priority
	 */
	private Priority(int level, int score) {
		this.level = level;
		this.score = score;
	}

	public static Priority getPriorityByLevel(int level) {
		switch (level) {
			case 1:
				return Priority.ONE;
			case 2:
				return Priority.TWO;
			case 3:
				return Priority.THREE;
			case 4:
				return Priority.FOUR;
			default:
				return Priority.FIVE;
		}
	}
}
