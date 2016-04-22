package program;

import java.util.ArrayList;
import java.util.List;

import entities.PlannedTask;
import entities.Task;
import logic.PlanningSolution;

public class UtilDebug {

	private static boolean isThereUndoneAndDoneTask(PlanningSolution s) {
		for (PlannedTask plannedTask : s.getPlannedTasks()) {
			if (s.getUndoneTasks().contains(plannedTask.getTask())) {
				return true;
			}
		}
		return false;
	}
	
	private static void printSolutionLists(String name, PlanningSolution s) {
			System.out.println(name + ":");
			System.out.print("-planned: ");
			for (PlannedTask task : s.getPlannedTasks()) {
				System.out.print(task.getTask().getName() + ", ");
			}
			System.out.println();
			System.out.print("-unplanned: ");
			for (Task task : s.getUndoneTasks()) {
				System.out.print(task.getName() + ", ");
			}
			System.out.println();
	}

	public static void printCrossoverTrace(PlanningSolution parent1, PlanningSolution parent2, PlanningSolution child1,
			PlanningSolution child2, int splitPosition) {
		if (isSolutionCoherent(parent1) && isSolutionCoherent(parent2) && (!isSolutionCoherent(child2) || !isSolutionCoherent(child1))) {
			System.out.println("SplitPosition: " + splitPosition);
			printSolutionLists("parent1", parent1);
			printSolutionLists("parent2", parent2);
			printSolutionLists("child1", child1);
			printSolutionLists("child2", child2);
		}		
	}
	
	public static void printMutationTrace(PlanningSolution parent, PlanningSolution child) {
		if (!isSolutionCoherent(child)) {
			printSolutionLists("parent", parent);
			printSolutionLists("child", child);
			System.out.println();
		}		
	}
	
	public static void printInCaseOfError(PlanningSolution s, String location) {
		if (!isSolutionCoherent(s)) {
			System.out.println(location);
		}
	}
	
	private static boolean containsDuplicate(List<PlannedTask> list) {
		ArrayList<Task> uniques = new ArrayList<>();
		for (PlannedTask task : list) {
			if (!uniques.contains(task.getTask())) {
				uniques.add(task.getTask());
			} 
			else {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isSolutionCoherent(PlanningSolution s) {
		return !containsDuplicate(s.getPlannedTasks()) && !isThereUndoneAndDoneTask(s);
	}
}
