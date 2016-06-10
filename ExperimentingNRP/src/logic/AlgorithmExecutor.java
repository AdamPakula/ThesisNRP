package logic;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.AlgorithmRunner;

import entities.AlgorithmChoice;
import entities.AlgorithmParameters;
import logic.comparators.PlanningSolutionDominanceComparator;
import logic.operators.PlanningCrossoverOperator;
import logic.operators.PlanningMutationOperator;

public class AlgorithmExecutor {
	
	/**
	 * The problem to solve
	 */
	private NextReleaseProblem problem;
	
	/**
	 * To parameters of the algorithm
	 */
	private AlgorithmParameters parameters;

	/**
	 * @param problem
	 */
	public AlgorithmExecutor(NextReleaseProblem problem, AlgorithmParameters parameters) {
		this.problem = problem;
		this.parameters = parameters;
	}
	
	public List<PlanningSolution> executeAlgorithm(AlgorithmChoice algorithmChoice) {
		Algorithm<List<PlanningSolution>> algorithm;
		CrossoverOperator<PlanningSolution> crossover;
	    MutationOperator<PlanningSolution> mutation;
	    SelectionOperator<List<PlanningSolution>, PlanningSolution> selection;
	    
		crossover = new PlanningCrossoverOperator(problem, parameters.getCrossoverProbability());
	    
	    mutation = new PlanningMutationOperator(problem, parameters.getMutationProbability());
	    
		selection = new BinaryTournamentSelection<>(new PlanningSolutionDominanceComparator());

		algorithm = new NSGAIIBuilder<PlanningSolution>(problem, crossover, mutation)
				.setSelectionOperator(selection)
				.setMaxIterations(parameters.getNumberOfIterations())
				.setPopulationSize(parameters.getPopulationSize())
				.build();
		
		AlgorithmRunner algoRunner = new AlgorithmRunner.Executor(algorithm).execute();
		
		return algorithm.getResult();
	}

}
