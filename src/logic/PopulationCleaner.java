package logic;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import logic.comparators.PlanningSolutionDominanceComparator;

public class PopulationCleaner {
	
	/**
	 * Returns a new sorted set without duplicates based on the population
	 * Specify the comparator as the <code>PlanningSolutionDominance</code> one
	 * @param population The population to sort
	 * @return sorted set population without duplicates
	 */
	public static SortedSet<PlanningSolution> getSorted(List<PlanningSolution> population) {
		SortedSet<PlanningSolution> sortedSolutions = new TreeSet<PlanningSolution>(
				new PlanningSolutionDominanceComparator());
		
		for (PlanningSolution planningSolution : sortedSolutions) {
			planningSolution.schedule();
		}
		sortedSolutions.addAll(population);
		
		return sortedSolutions;
	}
	
	/**
	 * Returns only the bests solutions using the comparator of the set
	 * @param sortedPopulation The population within will be only kept the best solutions
	 */
	public static void getBestSolutions(SortedSet<PlanningSolution> sortedPopulation) {
		PlanningSolution bestSolution = sortedPopulation.last();
		Iterator<PlanningSolution> iterator = sortedPopulation.iterator();
		
		while (sortedPopulation.comparator().compare(iterator.next(), bestSolution) < 0) {
			iterator.remove();
		}
	}

}
