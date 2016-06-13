package core;

import java.util.List;

import org.junit.Test;
import org.uma.jmetal.util.SolutionUtils;

import entities.Employee;
import entities.IterationParameters;
import entities.ProblemData;
import entities.Task;
import junit.framework.TestCase;
import logic.NextReleaseProblem;
import logic.PlanningSolution;
import logic.comparators.PlanningSolutionDominanceComparator;
import program.DataLoader;
import program.TestFile;


public class ComparatorTest extends TestCase {
	
	public ComparatorTest() {
	}

	public ComparatorTest(String name) {
		super(name);
	}
	
	@Test
	public void testDates() {		
		ProblemData data = DataLoader.readData(TestFile.PRECEDENCES);
		List<Task> tasks = data.getTasks();
		List<Employee> employees = data.getEmployees();
		
		NextReleaseProblem nrp = new NextReleaseProblem(tasks, employees, new IterationParameters(3, 35));
		
		PlanningSolution emptySolution = new PlanningSolution(nrp);
		while (emptySolution.getNumberOfPlannedTasks() > 0) {
			emptySolution.unschedule(emptySolution.getPlannedTask(0));
		}
		nrp.evaluate(emptySolution);
		nrp.evaluateConstraints(emptySolution);
		
		PlanningSolution fullSolution = new PlanningSolution(emptySolution);
		fullSolution.scheduleAtTheEnd(tasks.get(0), employees.get(0));
		fullSolution.scheduleAtTheEnd(tasks.get(1), employees.get(0));
		fullSolution.scheduleAtTheEnd(tasks.get(2), employees.get(1));
		fullSolution.scheduleAtTheEnd(tasks.get(3), employees.get(1));
		nrp.evaluate(fullSolution);
		nrp.evaluateConstraints(fullSolution);
		
		PlanningSolution bestSolution = SolutionUtils.getBestSolution(fullSolution, emptySolution, new PlanningSolutionDominanceComparator());

		assertEquals(bestSolution, fullSolution);
		assertEquals(-1, new PlanningSolutionDominanceComparator().compare(fullSolution, emptySolution));
	}
}
