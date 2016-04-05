package testPriceQuality;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

public class PriceQualityProblem extends AbstractIntegerProblem {
	
	public PriceQualityProblem() {
		this(2);
	}
	
	public PriceQualityProblem(Integer numberOfVariables) {
		setNumberOfVariables(numberOfVariables);
		setNumberOfObjectives(2);
		setName("Price Quality");
		
		List<Integer> lowerLimit = new ArrayList<>(numberOfVariables);
		List<Integer> upperLimit = new ArrayList<>(numberOfVariables);
		
		for (int i = 0 ; i < numberOfVariables ; ++i) {
			lowerLimit.add(1);
			upperLimit.add(100);
		}
		
		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
	}

	@Override
	public void evaluate(IntegerSolution solution) {
		solution.setObjective(0, solution.getVariableValue(0));
		solution.setObjective(1, solution.getVariableValue(1));
	}

}
