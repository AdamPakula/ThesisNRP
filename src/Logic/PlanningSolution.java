/**
 * 
 */
package Logic;

import java.util.HashMap;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;

import Entities.PlannedTask;

/**
 * @author Vavou
 *
 */
public class PlanningSolution extends AbstractGenericSolution<PlannedTask, NextReleaseProblem> {

	protected PlanningSolution(NextReleaseProblem problem) {
		super(problem);
		overallConstraintViolationDegree = 0.0 ;
	    numberOfViolatedConstraints = 0 ;

	    initializePlannedTaskVariables();
	    initializeObjectiveValues();
	}

	/**
	 * Initialize the variables
	 */
	private void initializePlannedTaskVariables() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Copy constructor
	 * @param planningSolution PlanningSoltion to copy
	 */
	public PlanningSolution(PlanningSolution planningSolution) {
		super(planningSolution.problem) ;

	    for (int i = 0; i < problem.getNumberOfVariables(); i++) {
	      setVariableValue(i, planningSolution.getVariableValue(i));
	    }

	    for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
	      setObjective(i, planningSolution.getObjective(i)) ;
	    }

	    overallConstraintViolationDegree = planningSolution.overallConstraintViolationDegree ;
	    numberOfViolatedConstraints = planningSolution.numberOfViolatedConstraints ;

	    attributes = new HashMap<Object, Object>(planningSolution.attributes) ;
	}

	/**
	 * Generated Id
	 */
	private static final long serialVersionUID = 615615442782301271L;

	@Override
	public String getVariableValueString(int index) {
		return getVariableValueString(index).toString();
	}

	@Override
	public Solution<PlannedTask> copy() {
		return new PlanningSolution(this);
	}

}
