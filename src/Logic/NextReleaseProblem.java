/**
 * 
 */
package Logic;

import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.solution.BinarySolution;

/**
 * @author Vavou
 * 
 * Objectives: 
 * 0: doing the most number of tasks
 *
 */
public class NextReleaseProblem extends AbstractBinaryProblem {

	public NextReleaseProblem(int numberOfVariables) {
		setNumberOfVariables(numberOfVariables);
		setName("Next Release Problem");
		setNumberOfObjectives(1);
	}
	
	@Override
	public void evaluate(BinarySolution solution) {
		int numberOfTasksToDo = 0;
		int numberOfVariables = getNumberOfVariables();
		for (int i = 0 ; i < numberOfVariables ; i++) {
			if (solution.getVariableValue(i).get(0)) {
				numberOfTasksToDo++;
			}
		}
		solution.setObjective(0, numberOfVariables - numberOfTasksToDo);
	}

	@Override
	protected int getBitsPerVariable(int index) {
		return 1;
	}

}
