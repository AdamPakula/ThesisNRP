package testPriceQuality;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

public class PriceQualityProblem extends AbstractIntegerProblem {
	
	public PriceQualityProblem() {
		this(2);
	}
	
	private final static int VARIABLE_MAX_VALUES[] = {1000, 1000};
	private final static int VARIABLE_MIN_VALUES[] = {1, 1};
	
	public PriceQualityProblem(Integer numberOfVariables) {
		setNumberOfVariables(numberOfVariables);
		setNumberOfObjectives(2);
		setName("Price Quality");
		
		List<Integer> lowerLimit = new ArrayList<>(numberOfVariables);
		List<Integer> upperLimit = new ArrayList<>(numberOfVariables);
		
		for (int i = 0 ; i < numberOfVariables ; ++i) {
			lowerLimit.add(VARIABLE_MIN_VALUES[i]);
			upperLimit.add(VARIABLE_MAX_VALUES[i]);
		}
		
		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
	}

	@Override
	public void evaluate(IntegerSolution solution) {
		solution.setObjective(0, solution.getVariableValue(0));
		solution.setObjective(1, VARIABLE_MAX_VALUES[1] - solution.getVariableValue(1));
	}

}
