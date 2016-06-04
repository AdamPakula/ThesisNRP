package logic.operators;

import java.util.Iterator;

import entities.PlannedTask;
import entities.Task;
import logic.NextReleaseProblem;
import logic.PlanningSolution;

public class RepairOperator {
	
	private NextReleaseProblem problem;
	
	public RepairOperator(NextReleaseProblem problem) {
		this.problem = problem;
	}
	
	public void repair(PlanningSolution solution) {
		Iterator<PlannedTask> it = solution.getPlannedTasks().iterator();
		problem.evaluate(solution);
		
		while (it.hasNext()) {
			PlannedTask currentPlannedTask = it.next();
			boolean fine = true;
			Iterator<Task> itPrevious = currentPlannedTask.getTask().getPreviousTasks().iterator();
			
			while (fine && itPrevious.hasNext()) {
				Task previousTask = itPrevious.next();
				PlannedTask currentPreviousPlannedTask = solution.findPlannedTask(previousTask);
				if (currentPreviousPlannedTask == null || currentPreviousPlannedTask.getEndHour() > currentPlannedTask.getBeginHour()) {
					solution.unschedule(currentPlannedTask);
					it.remove();
					fine = false;
					problem.evaluate(solution);
				}
			}
		}
	}

}
