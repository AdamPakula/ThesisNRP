package logic;

import java.util.List;

import entities.AlgorithmChoice;
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
		List<PlanningSolution> population = executor.executeAlgorithm(algorithmChoice);
		
		PlanningSolution bestSolution = PopulationFilter.getBestSolution(population);
		
		HTMLPrinter browserDisplay = new HTMLPrinter(bestSolution);
		browserDisplay.run();
	}

}
