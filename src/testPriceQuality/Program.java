package testPriceQuality;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.DefaultIntegerSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

/**
 * @author Vavou
 *
 */
public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Problem<IntegerSolution> problem = new PriceQualityProblem();
		Algorithm<List<IntegerSolution>> algorithm;
		CrossoverOperator<IntegerSolution> crossover;
		MutationOperator<IntegerSolution> mutation;
		SelectionOperator<List<IntegerSolution>, IntegerSolution> selection;
		
		double crossoverProbability = 0.9;
		double crossoverDistributionIndex = 20.0;
		crossover = new IntegerSBXCrossover(crossoverProbability, crossoverDistributionIndex);
		
		double mutationProbability = 1.0 / problem.getNumberOfVariables();
		double mutationDistributionIndex = 20.0;
		mutation = new IntegerPolynomialMutation(mutationProbability, mutationDistributionIndex);
		
		selection = new BinaryTournamentSelection<IntegerSolution>(new RankingAndCrowdingDistanceComparator<IntegerSolution>());
		
		algorithm = new NSGAIIBuilder<IntegerSolution>(problem, crossover, mutation)
				.setSelectionOperator(selection)
				.setMaxEvaluations(250)
				.setPopulationSize(100)
				.build();
		
		AlgorithmRunner algoRunner = new AlgorithmRunner.Executor(algorithm).execute();
		
		List<IntegerSolution> population = algorithm.getResult();
		
		for (IntegerSolution integerSolution : population) {
			System.out.println(integerSolution);
		}
	}

}
