package program;

public enum TestFile {
	SIMPLEST("simplest"),
	SIMPLE_OPTIMISATION("simple_optimisation");
	
	private String fileName;
	
	public String getTasksFileName() {
		return fileName + ".tasks";
	}
	
	public String getEmployeesFileName() {
		return fileName + ".employees";
	}
	
	private TestFile(String fileName) {
		this.fileName = fileName;
	}

}