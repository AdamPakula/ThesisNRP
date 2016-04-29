package logic.comparators;

import org.uma.jmetal.util.comparator.ConstraintViolationComparator;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;

import logic.PlanningSolution;

public class PlanningSolutionConstraintViolationComparator implements ConstraintViolationComparator<PlanningSolution> {

	/* --- Atributes --- */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4451090564962725930L;
	
	/**
	 * Number of Violated Constraint Atribute
	 */
	private NumberOfViolatedConstraints<PlanningSolution> numberOfViolatedConstraints;
	
	
	/* --- Constructors --- */
	
	/**
	 * Constructor
	 * Initializes the number of violated constraints atribute
	 */
	public PlanningSolutionConstraintViolationComparator() {
		numberOfViolatedConstraints = new NumberOfViolatedConstraints<>();
	}
	
	
	/* --- Methods --- */
	
	@Override
	public int compare(PlanningSolution solution1, PlanningSolution solution2) {
		int numViolatedConstraintInSol1 = numberOfViolatedConstraints.getAttribute(solution1),
				numViolatedConstraintInSol2 = numberOfViolatedConstraints.getAttribute(solution2);
		if (numViolatedConstraintInSol1 == numViolatedConstraintInSol2)
			return 0;
		else if (numViolatedConstraintInSol1 > numViolatedConstraintInSol2)
			return -1;
		else
			return 1;
	}

}
