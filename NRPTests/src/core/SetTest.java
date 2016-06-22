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
import entities.ProblemData;
import entities.Task;
import entities.parameters.IterationParameters;
import junit.framework.TestCase;
import logic.NextReleaseProblem;
import logic.PlanningSolution;
import logic.PopulationFilter;
import logic.SolutionQuality;
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
		PlanningSolution bestSolution = getBestSolution(TestFile.SKILLS, new IterationParameters(2, 35.0));
		
		assertEquals(2, bestSolution.getNumberOfPlannedTasks());
		for (PlannedTask plannedTask : bestSolution.getPlannedTasks()) {
			assertEquals(plannedTask.getTask().getRequiredSkills(), plannedTask.getEmployee().getSkills());
		}
		assertEquals(4.0, bestSolution.getEndDate());
	}
	
	@Test
	public void testEmployeeOverflow() {
		PlanningSolution bestSolution = getBestSolution(TestFile.EMPLOYEE_OVERFLOW, new IterationParameters(2, 35.0));
		
		assertEquals(1, bestSolution.getNumberOfPlannedTasks());
		assertEquals(40.0, bestSolution.getEndDate());
	}
	
	@Test
	public void testOverflow() {
		PlanningSolution bestSolution = getBestSolution(TestFile.OVERFLOW, new IterationParameters(1, 35.0));
		
		assertEquals(0, bestSolution.getNumberOfPlannedTasks());
	}
	
	@Test
	public void testOverflowOptimisation() {
		PlanningSolution bestSolution = getBestSolution(TestFile.OVERFLOW_OPTIMISATION, new IterationParameters(1, 35.0));
		
		assertTrue(bestSolution.getPriorityScore() <= 80.0);
	}
	
	@Test
	public void testSimplest() {
		PlanningSolution bestSolution = getBestSolution(TestFile.SIMPLEST, new IterationParameters(3, 35.0));
		
		assertEquals(1, bestSolution.getNumberOfPlannedTasks());
		assertEquals(2.0, bestSolution.getEndDate());
		
		double expectedQuality = (1.0 - (2.0/(3.0*35.0)) + 1 ) / 2;
		assertEquals(expectedQuality, new SolutionQuality().getAttribute(bestSolution));
	}
	
	@Test
	public void testSimpleOptimisation() {
		PlanningSolution bestSolution = getBestSolution(TestFile.SIMPLE_OPTIMISATION, new IterationParameters(3, 35.0));
		
		assertEquals(2, bestSolution.getNumberOfPlannedTasks());
		assertEquals(4.0, bestSolution.getEndDate());
	}
	
	@Test
	public void testPrecedence() {
		PlanningSolution bestSolution = getBestSolution(TestFile.PRECEDENCE, new IterationParameters(3, 35.0));
		
		assertEquals(2, bestSolution.getNumberOfPlannedTasks());
		assertEquals(4.0, bestSolution.getEndDate());
		assertTrue(bestSolution.getPlannedTask(0).getTask().getPreviousTasks().size() == 0);
	}
	
	@Test
	public void testPrecedences() {
		PlanningSolution bestSolution = getBestSolution(TestFile.PRECEDENCES, new IterationParameters(3, 35.0));
		
		assertEquals(4, bestSolution.getNumberOfPlannedTasks());
		assertEquals(7.0, bestSolution.getEndDate());
		assertTrue(bestSolution.getPlannedTask(0).getTask().getPreviousTasks().size() == 0);
	}
	
	

	private PlanningSolution getBestSolution(TestFile testFile, IterationParameters iterationParam) {
		ProblemData data = DataLoader.readData(testFile);
		List<Task> tasks = data.getTasks();
		List<Employee> employees = data.getEmployees();
		
		NextReleaseProblem problem = new NextReleaseProblem(tasks, employees, iterationParam);
		Algorithm<List<PlanningSolution>> algorithm;
		CrossoverOperator<PlanningSolution> crossover;
	    MutationOperator<PlanningSolution> mutation;
	    SelectionOperator<List<PlanningSolution>, PlanningSolution> selection;
	    
		crossover = new PlanningCrossoverOperator(problem);
	    
	    mutation = new PlanningMutationOperator(problem);
	    
		selection = new BinaryTournamentSelection<>(new PlanningSolutionDominanceComparator());

		algorithm = new NSGAIIBuilder<PlanningSolution>(problem, crossover, mutation)
				.setSelectionOperator(selection)
				.setMaxIterations(500)
				.setPopulationSize(100)
				.build();
		
		new AlgorithmRunner.Executor(algorithm).execute();
		
		List<PlanningSolution> population = algorithm.getResult();
		
		return PopulationFilter.getBestSolution(population);
	}
}
