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
	
	private int level;
	
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

	private Priority(int level, int score) {
		this.level = level;
		this.score = score;
	}

}
