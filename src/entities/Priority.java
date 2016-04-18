/**
 * 
 */
package entities;

/**
 * @author Vavou
 *
 */
public enum Priority {
	ONE(1, 5120),
	TWO(10, 2560),
	THREE(10, 1280),
	FOUR(10, 640),
	FIVE(10, 320),
	SIX(10, 160),
	SEVEN(10, 80),
	HEIGHT(10, 40),
	NINE(10, 20),
	TEN(10, 10);
	
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
