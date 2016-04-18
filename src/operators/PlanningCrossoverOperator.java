/**
 * 
 */
package operators;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import entities.PlannedTask;
import logic.PlanningSolution;

/**
 * @author Vavou
 *
 */
public class PlanningCrossoverOperator implements CrossoverOperator<PlanningSolution> {

	/* --- Attributes --- */
	
	/**
	 * Generated Id
	 */
	private static final long serialVersionUID = -9127657374482018148L;

	/**
	 * The crossover probability, between 0.0 and 1.0
	 */
	private double crossoverProbability  ;

	/**
	 * Random Generator
	 */
	private JMetalRandom randomGenerator ;

	
	/* --- Constructors --- */

	/**
	 * Constructor
	 * @param crossoverProbability the probability to do crossover, between 0.0 and 1.0
	 */
	public PlanningCrossoverOperator(double crossoverProbability) {
		if (crossoverProbability < 0) {
			throw new JMetalException("Crossover probability is negative: " + crossoverProbability) ;
		}

		this.crossoverProbability = crossoverProbability;
		randomGenerator = JMetalRandom.getInstance() ;
	}

	
	/* --- Methods --- */
	
	@Override
	public List<PlanningSolution> execute(List<PlanningSolution> solutions) {
		if (null == solutions) {
			throw new JMetalException("Null parameter") ;
		} else if (solutions.size() != 2) {
			throw new JMetalException("There must be two parents instead of " + solutions.size()) ;
		}

		return doCrossover(crossoverProbability, solutions.get(0), solutions.get(1)) ;
	}
	
	public List<PlanningSolution> doCrossover(double probability, PlanningSolution parent1, PlanningSolution parent2) {
		List<PlanningSolution> offspring = new ArrayList<PlanningSolution>(2);

		offspring.add((PlanningSolution) parent1.copy()) ;
		offspring.add((PlanningSolution) parent2.copy()) ;
		
		if (randomGenerator.nextDouble() < crossoverProbability) {
			PlanningSolution child1 = offspring.get(0);
			PlanningSolution child2 = offspring.get(1);
			
			int minSize = Math.min(parent1.getPlannedTasks().size(), parent2.getPlannedTasks().size());
			
			if (minSize > 0) {
				int splitPosition;
				
				if (minSize == 1) {
					splitPosition = 1;
				} 
				else {
					splitPosition = randomGenerator.nextInt(1, minSize - 1);
				}
				
				child1.setPlannedTasks(new ArrayList<PlannedTask>(parent1.getPlannedTasks().subList(0, splitPosition)));
				List<PlannedTask> endParent1 = new ArrayList<PlannedTask>(parent1.getPlannedTasks().subList(splitPosition, parent1.getPlannedTasks().size()));
				
				child2.setPlannedTasks(new ArrayList<PlannedTask>(parent2.getPlannedTasks().subList(0, splitPosition)));
				List<PlannedTask> endParent2 = new ArrayList<PlannedTask>(parent2.getPlannedTasks().subList(splitPosition, parent2.getPlannedTasks().size()));
				
				updateUnPlannedTasks(child1, endParent1);
				updateUnPlannedTasks(child2, endParent2);
				
				List<PlannedTask> duplicatesInChild1 = findDuplicates(child1.getPlannedTasks(), endParent2);
				List<PlannedTask> duplicatesInChild2 = findDuplicates(child2.getPlannedTasks(), endParent1);
				
				// Exchange the duplicates tasks in child 1 with the ones in child 2
				while (duplicatesInChild1.size() > 0 && duplicatesInChild2.size() > 0) {
					PlannedTask taskToChangeFromEndParent2ToEndParent1 = duplicatesInChild1.get(0),
							taskToChangeFromEndParent1ToEndParent2 = duplicatesInChild2.get(0);
					
					endParent2.set(endParent2.indexOf(taskToChangeFromEndParent2ToEndParent1), taskToChangeFromEndParent1ToEndParent2);
					endParent1.set(endParent1.indexOf(taskToChangeFromEndParent1ToEndParent2), taskToChangeFromEndParent2ToEndParent1);
					
					child1.getUndoneTasks().remove(taskToChangeFromEndParent1ToEndParent2);
					child2.getUndoneTasks().remove(taskToChangeFromEndParent2ToEndParent1);
				
					duplicatesInChild1.remove(0);
					duplicatesInChild2.remove(0);
				}
				
				while (duplicatesInChild1.size() > 0) {
					endParent2.remove(duplicatesInChild1.get(0));
					duplicatesInChild1.remove(0);
				}
				
				while (duplicatesInChild2.size() > 0) {
					endParent1.remove(duplicatesInChild2.get(0));
					duplicatesInChild2.remove(0);
				}
				
				child1.getPlannedTasks().addAll(endParent2);
				child2.getPlannedTasks().addAll(endParent1);
			}
		}

		return offspring;
	}
	
	/**
	 * Find the duplicated tasks in two lists
	 * @param list1 the first list
	 * @param list2 the second list
	 * @return the list of the duplicate tasks
	 */
	// TODO: improve replacing by a map with the indexes
	private List<PlannedTask> findDuplicates(List<PlannedTask> list1, List<PlannedTask> list2) {
		List<PlannedTask> duplicateTasks = new ArrayList<PlannedTask>();
		
		for (PlannedTask task1 : list1) {
			for (PlannedTask task2 : list2) {
				if (task1.getTask() == task2.getTask()) {
					duplicateTasks.add(task1);
				}
			}
		}
		
		return duplicateTasks;
	}

	@Override
	public int getNumberOfParents() {
		return 2;
	}

	
	private void updateUnPlannedTasks(PlanningSolution solution, List<PlannedTask> tasks) {
		for (PlannedTask task : tasks) {
			solution.getUndoneTasks().add(task.getTask());
		}
	}
}
