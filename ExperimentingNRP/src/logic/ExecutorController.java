package logic;

import entities.AlgorithmChoice;
import entities.ExecuteResult;
import entities.ExecutorParameters;
import entities.GeneratorParameters;
import entities.ProblemData;
import entities.parameters.IterationParameters;
import program.GeneratorNRP;
import view.HTMLPrinter;
import view.ParametersFrame;

public class ExecutorController {
	
	public ExecutorController() {
		ParametersFrame frame = new ParametersFrame(this);
		frame.setVisible(true);
	}

	public void launch(AlgorithmChoice algorithmChoice, GeneratorParameters genParam, IterationParameters iterationParam) {
		ProblemData problemData =  GeneratorNRP.generate(genParam);
		NextReleaseProblem nrp = new NextReleaseProblem(problemData.getFeatures(), problemData.getEmployees(), iterationParam);
		ExecutorParameters algoParam = new ExecutorParameters();
		
		AlgorithmExecutor executor = new AlgorithmExecutor(nrp, algoParam);
		ExecuteResult result = executor.executeAlgorithm(algorithmChoice);
		
		HTMLPrinter browserDisplay = new HTMLPrinter(result.getSolution());
		browserDisplay.run();
	}

}
