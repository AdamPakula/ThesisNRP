/**
 * 
 */
package core;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import entities.Employee;
import entities.PlannedFeature;
import entities.ProblemData;
import entities.Feature;
import entities.parameters.IterationParameters;
import junit.framework.TestCase;
import logic.NextReleaseProblem;
import logic.PlanningSolution;
import program.DataLoader;
import program.TestFile;

/**
 * @author Vavou
 *
 */
public class SchedulerTest extends TestCase {

	/**
	 * 
	 */
	public SchedulerTest() {
		super();
	}

	/**
	 * @param name
	 */
	public SchedulerTest(String name) {
		super(name);
	}
	
	
	@Test
	public void testDates() {
		ProblemData data = DataLoader.readData(TestFile.PRECEDENCES);
		List<Feature> tasks = data.getFeatures();
		List<Employee> employees = data.getEmployees();
		
		NextReleaseProblem nrp = new NextReleaseProblem(tasks, employees, new IterationParameters(3, 35));
		PlanningSolution solution;
		List<PlannedFeature> plannedTasks = new ArrayList<>();
		plannedTasks.add(new PlannedFeature(tasks.get(0), employees.get(0)));
		plannedTasks.add(new PlannedFeature(tasks.get(1), employees.get(0)));
		plannedTasks.add(new PlannedFeature(tasks.get(2), employees.get(1)));
		plannedTasks.add(new PlannedFeature(tasks.get(3), employees.get(1)));
		
		solution = new PlanningSolution(nrp, plannedTasks);
		nrp.evaluate(solution);
		plannedTasks = solution.getPlannedFeatures();
		
		List<PlanningSolution> pop = new ArrayList<>();
		pop.add(solution);
		
		assertEquals(0.0, plannedTasks.get(0).getBeginHour());
		assertEquals(2.0, plannedTasks.get(0).getEndHour());
		
		assertEquals(2.0, plannedTasks.get(1).getBeginHour());
		assertEquals(5.0, plannedTasks.get(1).getEndHour());
		
		assertEquals(2.0, plannedTasks.get(2).getBeginHour());
		assertEquals(4.0, plannedTasks.get(2).getEndHour());
		
		assertEquals(solution.getPriorityScore(), solution.getObjective(NextReleaseProblem.INDEX_PRIORITY_OBJECTIVE));
		assertEquals(solution.getEndDate(), solution.getObjective(NextReleaseProblem.INDEX_END_DATE_OBJECTIVE));
		assertEquals(0, new OverallConstraintViolation<>().getAttribute(solution));
	}

}
