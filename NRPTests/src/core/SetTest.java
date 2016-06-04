package core;

import java.util.List;

import org.junit.Test;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.AlgorithmRunner;

import entities.Employee;
import entities.PlannedTask;
import entities.Task;
import junit.framework.TestCase;
import logic.NextReleaseProblem;
import logic.PlanningSolution;
import logic.PopulationCleaner;
import logic.comparators.PlanningSolutionDominanceComparator;
import logic.operators.PlanningCrossoverOperator;
import logic.operators.PlanningMutationOperator;
import program.DataLoader;
import program.TestFile;

public class SetTest extends TestCase {

	public SetTest() {
	}

	public SetTest(String name) {
		super(name);
	}
	
	@Test
	public void testSkills() {
		PlanningSolution bestSolution = getBestSolution(TestFile.SKILLS, 2, 35.0);
		
		assertEquals(2, bestSolution.getNumberOfPlannedTasks());
		for (PlannedTask plannedTask : bestSolution.getPlannedTasks()) {
			assertEquals(plannedTask.getTask().getRequiredSkills(), plannedTask.getEmployee().getSkills());
		}
		assertEquals(4.0, bestSolution.getEndDate());
	}
	
	@Test
	public void testEmployeeOverflow() {
		PlanningSolution bestSolution = getBestSolution(TestFile.EMPLOYEE_OVERFLOW, 2, 35.0);
		
		assertEquals(1, bestSolution.getNumberOfPlannedTasks());
		assertEquals(40.0, bestSolution.getEndDate());
	}
	
	@Test
	public void testOverflow() {
		PlanningSolution bestSolution = getBestSolution(TestFile.OVERFLOW, 1, 35.0);
		
		assertEquals(0, bestSolution.getNumberOfPlannedTasks());
	}
	
	@Test
	public void testOverflowOptimisation() {
		PlanningSolution bestSolution = getBestSolution(TestFile.OVERFLOW_OPTIMISATION, 1, 35.0);
		
		assertTrue(bestSolution.getPriorityScore() <= 80.0);
	}
	
	@Test
	public void testSimplest() {
		PlanningSolution bestSolution = getBestSolution(TestFile.SIMPLEST, 3, 35.0);
		
		assertEquals(1, bestSolution.getNumberOfPlannedTasks());
		assertEquals(2.0, bestSolution.getEndDate());
	}
	
	@Test
	public void testSimpleOptimisation() {
		PlanningSolution bestSolution = getBestSolution(TestFile.SIMPLE_OPTIMISATION, 3, 35.0);
		
		assertEquals(2, bestSolution.getNumberOfPlannedTasks());
		assertEquals(4.0, bestSolution.getEndDate());
	}
	
	@Test
	public void testPrecedence() {
		PlanningSolution bestSolution = getBestSolution(TestFile.PRECEDENCE, 3, 35.0);
		
		assertEquals(2, bestSolution.getNumberOfPlannedTasks());
		assertEquals(4.0, bestSolution.getEndDate());
		assertTrue(bestSolution.getPlannedTask(0).getTask().getPreviousTasks().size() == 0);
	}
	
	@Test
	public void testPrecedences() {
		PlanningSolution bestSolution = getBestSolution(TestFile.PRECEDENCES, 3, 35.0);
		
		assertEquals(4, bestSolution.getNumberOfPlannedTasks());
		assertEquals(7.0, bestSolution.getEndDate());
		assertTrue(bestSolution.getPlannedTask(0).getTask().getPreviousTasks().size() == 0);
	}
	
	

	private PlanningSolution getBestSolution(TestFile testFile, int nbWEEk, double nbHoursByWeek) {
		Object inputLists[] = DataLoader.readData(testFile);
		List<Task> tasks = (List<Task>) inputLists[0];
		List<Employee> employees = (List<Employee>) inputLists[1];
		
		NextReleaseProblem problem = new NextReleaseProblem(tasks, employees, nbWEEk, nbHoursByWeek);
		Algorithm<List<PlanningSolution>> algorithm;
		CrossoverOperator<PlanningSolution> crossover;
	    MutationOperator<PlanningSolution> mutation;
	    SelectionOperator<List<PlanningSolution>, PlanningSolution> selection;
	    
	    double crossoverProbability = 0.1;
		crossover = new PlanningCrossoverOperator(problem, crossoverProbability);
	    
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
		
		return PopulationCleaner.getBestSolution(population);
	}
}
