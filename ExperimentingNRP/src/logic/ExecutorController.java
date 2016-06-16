package logic;

import java.util.List;

import entities.AlgorithmChoice;
import entities.AlgorithmParameters;
import entities.GeneratorParameters;
import entities.IterationParameters;
import entities.ProblemData;
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
		NextReleaseProblem nrp = new NextReleaseProblem(problemData.getTasks(), problemData.getEmployees(), iterationParam);
		AlgorithmParameters algoParam = new AlgorithmParameters();
		algoParam.setMutationProbability(1.0/problemData.getTasks().size());
		
		AlgorithmExecutor executor = new AlgorithmExecutor(nrp, algoParam);
		List<PlanningSolution> population = executor.executeAlgorithm(algorithmChoice);
		
		PlanningSolution bestSolution = PopulationFilter.getBestSolution(population);
		
		HTMLPrinter browserDisplay = new HTMLPrinter(bestSolution);
		browserDisplay.run();
	}

}
