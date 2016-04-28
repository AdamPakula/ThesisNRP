package entities;

public class Skill {
	
	/* --- Atributes --- */
	
	/**
	 * The name of the Skill
	 */
	private String name;
	
	
	/* --- Getters and setters --- */
	
	/**
	 * @return the name of the skill
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the skill
	 * @param name the new name of the skill, can't be null
	 */
	public void setName(String name) {
		if (name == null) {
			throw new NullPointerException();
		}
		this.name = name;
	}
	
	
	/* --- Constructors --- */

	/**
	 * Constructs a skill
	 * @param name the name of the skill to construct
	 */
	public Skill(String name) {
		this.name = name;
	}

}
