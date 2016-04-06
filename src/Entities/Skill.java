package Entities;

public class Skill {
	
	/**
	 * The name of the Skill
	 */
	private String name;
	
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

	public Skill(String name) {
		this.name = name;
	}

}
