package program;

public enum TestFile {
	SIMPLEST("simplest"),
	SIMPLE_OPTIMISATION("simple_optimisation"),
	PRECEDENCE("precedence"),
	PRECEDENCES("precedences"),
	OVERFLOW("overflow"),
	OVERFLOW_OPTIMISATION("overflow_optimisation"),
	EMPLOYEE_OVERFLOW("employee_overflow"),
	GENERATED("generated"),
	SKILLS("skills");
	
	private String fileName;
	
	public String getFeaturesFileName() {
		return fileName + ".features";
	}
	
	public String getEmployeesFileName() {
		return fileName + ".employees";
	}
	
	private TestFile(String fileName) {
		this.fileName = fileName;
	}

}
