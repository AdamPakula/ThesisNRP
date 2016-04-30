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
			System.out.println("End Date: " + currentSolution.getEndDate());
			System.out.println();
		}
	}

	public static void main(String[] args) {
		/*Skill cpp = new Skill("C++");
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
		employees.add(new Employee("Employee 3", 15.0, bothSkills));*/
		
		Object inputLists[] = DataLoader.readData();
		List<Task> tasks = (List<Task>) inputLists[0];
		List<Employee> employees = (List<Employee>) inputLists[1];
		
		NextReleaseProblem problem = new NextReleaseProblem(tasks, employees);
		Algorithm<List<PlanningSolution>> algorithm;
		CrossoverOperator<PlanningSolution> crossover;
	    MutationOperator<PlanningSolution> mutation;
	    SelectionOperator<List<PlanningSolution>, PlanningSolution> selection;
	    
	    double crossoverProbability = 0.1;
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
		
		System.err.println(population.size());
		Set<PlanningSolution> filteredPopulation = PopulationCleaner.getBestSolutions(population);
		System.err.println(filteredPopulation.size());
		printPopulation(filteredPopulation);
		printPopulation(population);
		HTMLPrinter browserDisplay = new HTMLPrinter(problem, new ArrayList<>(filteredPopulation));
		browserDisplay.run();
		
	}
}
