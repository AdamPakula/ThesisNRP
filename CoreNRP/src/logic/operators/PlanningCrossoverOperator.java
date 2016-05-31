/**
 * 
 */
package logic.operators;

import java.util.ArrayList;
import java.util.Iterator;
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
			// The two final solutions containing, at the beginning, a copy of the parents
			PlanningSolution child1 = offspring.get(0);
			PlanningSolution child2 = offspring.get(1);
			
			int minSize = Math.min(parent1.getNumberOfPlannedTasks(), parent2.getNumberOfPlannedTasks());
			
			if (minSize > 0) {
				int splitPosition;
				
				if (minSize == 1) {
					splitPosition = 1;
				} 
				else {
					splitPosition = randomGenerator.nextInt(1, minSize);
				}
				
				// Copy and unschedule the post-cut tasks
				List<PlannedTask> futurEndChild1 = child2.getEndPlannedTasksSubListCopy(splitPosition);
				List<PlannedTask> futurEndChild2 = child1.getEndPlannedTasksSubListCopy(splitPosition);
				for (PlannedTask plannedTask : futurEndChild2) {
					child1.unschedule(plannedTask);
				}
				for (PlannedTask plannedTask : futurEndChild1) {
					child2.unschedule(plannedTask);
				}
				
				
				// schedule the new ends and keep it in the list only if they were already planned
				Iterator<PlannedTask> iteratorEndChild1 = futurEndChild1.iterator();
				while (iteratorEndChild1.hasNext()) {
					PlannedTask plannedTask = (PlannedTask) iteratorEndChild1.next();
					if (!child1.isAlreadyPlanned(plannedTask.getTask())) {
						child1.scheduleAtTheEnd(plannedTask.getTask(), plannedTask.getEmployee());
						iteratorEndChild1.remove();
					}
				}
				Iterator<PlannedTask> iteratorEndChild2 = futurEndChild2.iterator();
				while (iteratorEndChild2.hasNext()) {
					PlannedTask plannedTask = (PlannedTask) iteratorEndChild2.next();
					if (!child2.isAlreadyPlanned(plannedTask.getTask())) {
						child2.scheduleAtTheEnd(plannedTask.getTask(), plannedTask.getEmployee());
						iteratorEndChild2.remove();
					}
				}
				
				// Exchanging the tasks
				iteratorEndChild1 = futurEndChild1.iterator();
				iteratorEndChild2 = futurEndChild2.iterator();
				while (iteratorEndChild1.hasNext() && iteratorEndChild2.hasNext()) {
					PlannedTask task = iteratorEndChild1.next();
					child1.scheduleAtTheEnd(task.getTask(), task.getEmployee());
					task = iteratorEndChild2.next();
					child2.scheduleAtTheEnd(task.getTask(), task.getEmployee());
				}
			}
		}
		
		return offspring;
	}
}
