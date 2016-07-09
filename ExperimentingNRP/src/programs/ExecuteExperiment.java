package programs;

import entities.ExperimentType;
import logic.ExperimentController;

public class ExecuteExperiment {

	public static void main(String[] args) {
		new ExperimentController(ExperimentType.FEATURES);
	}
}
