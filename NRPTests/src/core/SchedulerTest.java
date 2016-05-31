/**
 * 
 */
package core;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import entities.Employee;
import entities.PlannedTask;
import entities.Task;
import junit.framework.TestCase;
import logic.NextReleaseProblem;
import logic.PlanningSolution;
import program.DataLoader;
import program.Program;
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
		Object inputLists[] = DataLoader.readData(TestFile.PRECEDENCES);
		List<Task> tasks = (List<Task>) inputLists[0];
		List<Employee> employees = (List<Employee>) inputLists[1];
		
		NextReleaseProblem nrp = new NextReleaseProblem(tasks, employees, 3, 35);
		PlanningSolution solution;
		List<PlannedTask> plannedTasks = new ArrayList<>();
		plannedTasks.add(new PlannedTask(tasks.get(0), employees.get(0)));
		plannedTasks.add(new PlannedTask(tasks.get(1), employees.get(0)));
		plannedTasks.add(new PlannedTask(tasks.get(2), employees.get(1)));
		plannedTasks.add(new PlannedTask(tasks.get(3), employees.get(1)));
		
		solution = new PlanningSolution(nrp, plannedTasks);
		nrp.evaluate(solution);
		plannedTasks = solution.getPlannedTasks();
		
		List<PlanningSolution> pop = new ArrayList<>();
		pop.add(solution);
		Program.printPopulation(pop);
		
		assertEquals(0.0, plannedTasks.get(0).getBeginHour());
		assertEquals(2.0, plannedTasks.get(0).getEndHour());
		
		assertEquals(2.0, plannedTasks.get(1).getBeginHour());
		assertEquals(5.0, plannedTasks.get(1).getEndHour());
		
		assertEquals(2.0, plannedTasks.get(2).getBeginHour());
		assertEquals(4.0, plannedTasks.get(2).getEndHour());
		
		assertEquals(solution.getPriorityScore(), solution.getObjective(NextReleaseProblem.INDEX_PRIORITY_OBJECTIVE));
		assertEquals(solution.getEndDate(), solution.getObjective(NextReleaseProblem.INDEX_END_DATE_OBJECTIVE));
		assertEquals(0, solution.getNumberOfViolatedConstraint());
	}

}
