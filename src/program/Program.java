package program;

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

import entities.Employee;
import entities.PlannedTask;
import entities.Priority;
import entities.Task;
import logic.NextReleaseProblem;
import logic.PlanningSolution;
import operators.PlanningCrossoverOperator;
import operators.PlanningMutationOperator;

public class Program {

	public static void main(String[] args) {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task("Task 1", Priority.THREE, 2.0));
		tasks.add(new Task("Task 2", Priority.TWO, 5.0));
		tasks.add(new Task("Task 3", Priority.THREE, 2.0));
		
		List<Employee> employees = new ArrayList<Employee>();
		employees.add(new Employee("Employee 1", 5.0));
		employees.add(new Employee("Employee 2", 20.0));
		employees.add(new Employee("Employee 3", 15.0));
		
		NextReleaseProblem problem = new NextReleaseProblem(tasks, employees);
		Algorithm<List<PlanningSolution>> algorithm;
		CrossoverOperator<PlanningSolution> crossover;
	    MutationOperator<PlanningSolution> mutation;
	    SelectionOperator<List<PlanningSolution>, PlanningSolution> selection;
	    
	    double crossoverProbability = 0.9;
		crossover = new PlanningCrossoverOperator(crossoverProbability);
	    
	    double mutationProbability = 1.0 / problem.getTasks().size();
	    mutation = new PlanningMutationOperator(problem, mutationProbability);
	    
	    /*
		selection = new BinaryTournamentSelection<BinarySolution>(new RankingAndCrowdingDistanceComparator<BinarySolution>());
		*/
		algorithm = new NSGAIIBuilder<PlanningSolution>(problem, crossover, mutation)
				.setMaxEvaluations(250)
				.setPopulationSize(100)
				.build();
		
		AlgorithmRunner algoRunner = new AlgorithmRunner.Executor(algorithm).execute();
		
		List<PlanningSolution> population = algorithm.getResult();
		
		int solutionCpt = 1;
		for (PlanningSolution solution : population) {
			System.out.println("Solution " + solutionCpt + ":");
			for (PlannedTask task : solution.getPlannedTasks()) {
				System.out.println("-" + task.getTask().getName() + " done by " + task.getEmployee().getName());
			}
			solutionCpt++;
		}
	}

}
