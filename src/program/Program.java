package program;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.AlgorithmRunner;

import entities.Employee;
import entities.Task;
import logic.NextReleaseProblem;
import logic.PlanningSolution;
import logic.PopulationCleaner;
import logic.comparators.PlanningSolutionDominanceComparator;
import logic.operators.PlanningCrossoverOperator;
import logic.operators.PlanningMutationOperator;
import view.HTMLPrinter;

public class Program {
	
	public static void printPopulation(Collection<PlanningSolution> population) {
		int solutionCpt = 1;
		Iterator<PlanningSolution> iterator = population.iterator();
		
		while (iterator.hasNext()) {
			PlanningSolution currentSolution = iterator.next();
			System.out.println("Solution " + solutionCpt++ + ": (" 
					+ currentSolution.getObjective(0) + "\t" + currentSolution.getObjective(1) + ")");
			System.out.print(currentSolution);
			if (!currentSolution.isUpToDate()) {
				System.err.println("not up to date");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {		
		Object inputLists[] = DataLoader.readData(TestFile.OVERFLOW_OPTIMISATION);
		List<Task> tasks = (List<Task>) inputLists[0];
		List<Employee> employees = (List<Employee>) inputLists[1];
		
		NextReleaseProblem problem = new NextReleaseProblem(tasks, employees, 1, 35.0);
		Algorithm<List<PlanningSolution>> algorithm;
		CrossoverOperator<PlanningSolution> crossover;
	    MutationOperator<PlanningSolution> mutation;
	    SelectionOperator<List<PlanningSolution>, PlanningSolution> selection;
	    
	    double crossoverProbability = 0.5;
		crossover = new PlanningCrossoverOperator(crossoverProbability);
	    
	    double mutationProbability = 1.0 / problem.getTasks().size();
	    mutation = new PlanningMutationOperator(problem, mutationProbability);
	    
		selection = new BinaryTournamentSelection<>(new PlanningSolutionDominanceComparator());

		algorithm = new NSGAIIBuilder<PlanningSolution>(problem, crossover, mutation)
				.setSelectionOperator(selection)
				.setMaxIterations(500)
				.setPopulationSize(100)
				.build();
		
		AlgorithmRunner algoRunner = new AlgorithmRunner.Executor(algorithm).execute();
		
		List<PlanningSolution> population = algorithm.getResult();
		
		Set<PlanningSolution> filteredPopulation = PopulationCleaner.getBestSolutions(population);
		printPopulation(population);
		printPopulation(filteredPopulation);
		HTMLPrinter browserDisplay = new HTMLPrinter(problem, new ArrayList<>(filteredPopulation));
		browserDisplay.run();
	}
}
