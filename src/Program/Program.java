package Program;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.HUXCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import Entities.Task;
import Logic.NextReleaseProblem;

public class Program {

	public static void main(String[] args) {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task("Task 1", 3, 2.0));
		tasks.add(new Task("Task 2", 2, 5.0));
		tasks.add(new Task("Task 3", 3, 2.0));
		
		Problem<BinarySolution> problem = new NextReleaseProblem(tasks.size());
		Algorithm<List<BinarySolution>> algorithm;
		CrossoverOperator<BinarySolution> crossover;
	    MutationOperator<BinarySolution> mutation;
	    SelectionOperator<List<BinarySolution>, BinarySolution> selection;
	    
	    double crossoverProbability = 0.9;
		crossover = new HUXCrossover(crossoverProbability);
		
		double mutationProbability = 1.0 / problem.getNumberOfVariables();
		mutation = new BitFlipMutation(mutationProbability);
		
		selection = new BinaryTournamentSelection<BinarySolution>(new RankingAndCrowdingDistanceComparator<BinarySolution>());
		
		algorithm = new NSGAIIBuilder<BinarySolution>(problem, crossover, mutation)
				.setSelectionOperator(selection)
				.setMaxEvaluations(250)
				.setPopulationSize(100)
				.build();
		
		AlgorithmRunner algoRunner = new AlgorithmRunner.Executor(algorithm).execute();
		
		List<BinarySolution> population = algorithm.getResult();
		
		/* To filter the identical solutions
		for (int i = 0 ; i < population.size() ; i++) {
			BinarySolution solution = population.get(i);
			int j = population.size() -1;
			while (j > 0) {
				if (solution.equals(population.get(j))) {
					population.remove(j);
				}
				j--;
			}
		}*/
		
		int solutionCpt = 1;
		for (BinarySolution solution : population) {
			System.out.println("Solution " + solutionCpt + ":");
			for (int i = 0 ; i < tasks.size() ; i++) {
				if (solution.getVariableValue(i).get(0)) {
					System.out.println("-" + tasks.get(i).getName());
				}
			}
			solutionCpt++;
		}
	}

}
