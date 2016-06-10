package entities;

public enum AlgorithmChoice {
	NSGAII("NSGA II"),
	OTHER("other");
	
	private String name;
	
	private AlgorithmChoice(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}
