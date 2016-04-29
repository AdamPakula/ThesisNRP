package program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.AlgorithmRunner;

import entities.Employee;
import entities.PlannedTask;
import entities.Priority;
import entities.Skill;
import entities.Task;
import logic.NextReleaseProblem;
import logic.PlanningSolution;
import logic.comparators.PlanningSolutionDominanceComparator;
import logic.operators.PlanningCrossoverOperator;
import logic.operators.PlanningMutationOperator;
import view.HTMLPrinter;

public class Program {
	
	/**
	 * Returns only the best solutions
	 * @param population the population which will be ordered
	 */
	private static void filter(List<PlanningSolution> population) {
		List<PlanningSolution> newPop = new ArrayList<>();
		Comparator<PlanningSolution> comparator = new PlanningSolutionDominanceComparator();
		Collections.sort(population, comparator);

		if (population.size() > 0) {
			int i = population.size() - 1;
			PlanningSolution reference = population.get(population.size()-1);
			
			do {
				newPop.add(population.get(i));
				i--;
			} while (i >= 0 && comparator.compare(reference, population.get(i)) == 0);
		}
		
		population.clear();
		population.addAll(newPop);
	}
	
	private static void printPopulation(List<PlanningSolution> population) {
		int solutionCpt = 1;
		for (PlanningSolution solution : population) {
			System.out.println("Solution " + solutionCpt + ": (" 
					+ solution.getObjective(0) + "\t" + solution.getObjective(1) + ")");
			for (PlannedTask task : solution.getPlannedTasks()) {
				System.out.println("-" + task.getTask().getName() + " done by " + task.getEmployee().getName() + " at hour " + task.getBeginHour());
			}
			System.out.println("End Date: " + solution.getEndDate());
			System.out.println();
			solutionCpt++;
		}
	}

	public static void main(String[] args) {
		Skill cpp = new Skill("C++");
		Skill java = new Skill("Java");
		
		List<Skill> cppSkills = new ArrayList<>();
		cppSkills.add(cpp);
		List<Skill> javaSkills = new ArrayList<>();
		javaSkills.add(java);
		List<Skill> bothSkills = new ArrayList<>();
		bothSkills.add(java);
		bothSkills.add(java);
		
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task("Task 1", Priority.THREE, 2.0, null, cppSkills));
		tasks.add(new Task("Task 2", Priority.TWO, 5.0, null, javaSkills));
		tasks.add(new Task("Task 3", Priority.THREE, 2.0, null, cppSkills));
		tasks.add(new Task("Task 4", Priority.TWO, 10.0, null, cppSkills));
		tasks.add(new Task("Task 5", Priority.FIVE, 3.0, new ArrayList<>(tasks.subList(3, 4)), javaSkills));
		
		List<Employee> employees = new ArrayList<Employee>();
		employees.add(new Employee("Employee 1", 5.0, cppSkills));
		employees.add(new Employee("Employee 2", 20.0, javaSkills));
		employees.add(new Employee("Employee 3", 15.0, bothSkills));
		
		NextReleaseProblem problem = new NextReleaseProblem(tasks, employees);
		Algorithm<List<PlanningSolution>> algorithm;
		CrossoverOperator<PlanningSolution> crossover;
	    MutationOperator<PlanningSolution> mutation;
	    SelectionOperator<List<PlanningSolution>, PlanningSolution> selection;
	    
	    double crossoverProbability = 0.9;
		crossover = new PlanningCrossoverOperator(crossoverProbability);
	    
	    double mutationProbability = 1.0 / problem.getTasks().size();
	    mutation = new PlanningMutationOperator(problem, mutationProbability);
	    
		selection = new BinaryTournamentSelection<>(new PlanningSolutionDominanceComparator());

		algorithm = new NSGAIIBuilder<PlanningSolution>(problem, crossover, mutation)
				.setSelectionOperator(selection)
				.setMaxEvaluations(250)
				.setPopulationSize(100)
				.build();
		
		AlgorithmRunner algoRunner = new AlgorithmRunner.Executor(algorithm).execute();
		
		List<PlanningSolution> population = algorithm.getResult();
		for (PlanningSolution planningSolution : population) {
			problem.evaluate(planningSolution);
		}
		filter(population);
		printPopulation(population);
		HTMLPrinter browserDisplay = new HTMLPrinter(problem, population);
		browserDisplay.run();
		
	}
}
