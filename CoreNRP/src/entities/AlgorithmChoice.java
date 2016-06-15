package entities;

public enum AlgorithmChoice {
	//GENERATIONAL("Generational GA"), 
	MOCell("MOCell"),
	NSGAII("NSGA II"),
	PESA2("PESA2"),
	//SMSEMOA("SMSEMOA"), // Does not evaluate the constraints...
	SPEA2("SPEA2"),
	//STEADY("Steady State GA") // The evaluator does not consider the constraints...
	;
	 
	private String name;
	
	private AlgorithmChoice(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}
