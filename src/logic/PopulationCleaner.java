package logic;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import logic.comparators.PlanningSolutionDominanceComparator;

public class PopulationCleaner {
	
	/**
	 * Sorts the population by dominance
	 * uses a <code>PlaningSolutionDominanceComparator</code>
	 * @param population the population to sort
	 */
	private static void sortByDominance(List<PlanningSolution> population) {	
		Collections.sort(population, new PlanningSolutionDominanceComparator());
	}
	
	/**
	 * Returns only the bests solutions
	 * Uses the <code>PlanningSolutionDominanceComparator</code>
	 * @param sortedPopulation The population with only the best solutions
	 */
	public static Set<PlanningSolution> getBestSolutions(List<PlanningSolution> population) {
		Set<PlanningSolution> bestSolutions = new HashSet<PlanningSolution>();
		
		if (population.size() == 0) {
			return bestSolutions;
		}
		
		Comparator<PlanningSolution> comparator = new PlanningSolutionDominanceComparator();
		sortByDominance(population);
		
		PlanningSolution bestSolution = population.get(population.size()-1);
		Iterator<PlanningSolution> iterator = population.iterator();
		
		while (iterator.hasNext()) {
			PlanningSolution currentSolution = (PlanningSolution) iterator.next();
			if (comparator.compare(currentSolution, bestSolution) == 0) {
				bestSolutions.add(currentSolution);
			}
		}
		
		return bestSolutions;
	}

}
