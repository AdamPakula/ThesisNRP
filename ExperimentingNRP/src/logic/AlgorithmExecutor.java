package logic;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.mocell.MOCellBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.pesa2.PESA2Builder;
//import org.uma.jmetal.algorithm.multiobjective.smsemoa.SMSEMOABuilder;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.AlgorithmRunner;

import entities.AlgorithmChoice;
import entities.AlgorithmParameters;
import logic.operators.PlanningCrossoverOperator;
import logic.operators.PlanningMutationOperator;

public class AlgorithmExecutor {
	
	/**
	 * The problem to solve
	 */
	private NextReleaseProblem problem;
	
	/**
	 * To parameters of the algorithm
	 */
	private AlgorithmParameters parameters;

	/**
	 * @param problem
	 */
	public AlgorithmExecutor(NextReleaseProblem problem, AlgorithmParameters parameters) {
		this.problem = problem;
		this.parameters = parameters;
	}
	
	public List<PlanningSolution> executeAlgorithm(AlgorithmChoice algorithmChoice) {
		List<PlanningSolution> population;
		CrossoverOperator<PlanningSolution> crossoverOperator = new PlanningCrossoverOperator(problem, parameters.getCrossoverProbability());
	    MutationOperator<PlanningSolution> mutationOperator = new PlanningMutationOperator(problem, parameters.getMutationProbability());
	    
		switch (algorithmChoice) {
			/*case GENERATIONAL: { 
				Algorithm<PlanningSolution> algorithm = new GeneticAlgorithmBuilder<PlanningSolution>(problem, crossoverOperator, mutationOperator)
					.setMaxEvaluations(parameters.getPopulationSize()*parameters.getNumberOfIterations())
					.setPopulationSize(parameters.getPopulationSize())
					.build();
				
				population = new ArrayList<>(1);
				new AlgorithmRunner.Executor(algorithm).execute();
				population.add(algorithm.getResult());
				break;
			}*/
			case MOCell: { 
				Algorithm<List<PlanningSolution>> algorithm = new MOCellBuilder<PlanningSolution>(problem, crossoverOperator, mutationOperator)
					.setMaxEvaluations(parameters.getNumberOfIterations())
					.setPopulationSize(parameters.getPopulationSize())
					.build();
				new AlgorithmRunner.Executor(algorithm).execute();
				population = algorithm.getResult();
				break;
			}
			case NSGAII: { 
				Algorithm<List<PlanningSolution>> algorithm = new NSGAIIBuilder<PlanningSolution>(problem, crossoverOperator, mutationOperator)
					.setMaxIterations(parameters.getNumberOfIterations())
					.setPopulationSize(parameters.getPopulationSize())
					.build();
				new AlgorithmRunner.Executor(algorithm).execute();
				population = algorithm.getResult();
				break;
			}
			case PESA2: { 
				Algorithm<List<PlanningSolution>> algorithm = new PESA2Builder<PlanningSolution>(problem, crossoverOperator, mutationOperator)
					.setMaxEvaluations(parameters.getNumberOfIterations())
					.setPopulationSize(parameters.getPopulationSize())
					.build();
				new AlgorithmRunner.Executor(algorithm).execute();
				population = algorithm.getResult();
				break;
			}
			/*case SMSEMOA: { 
				Algorithm<List<PlanningSolution>> algorithm = new SMSEMOABuilder<PlanningSolution>(problem, crossoverOperator, mutationOperator)
					.setMaxEvaluations(parameters.getNumberOfIterations())
					.setPopulationSize(parameters.getPopulationSize())
					.build();
				new AlgorithmRunner.Executor(algorithm).execute();
				population = algorithm.getResult();
				break;
			}*/
			case SPEA2: { 
				Algorithm<List<PlanningSolution>> algorithm = new SPEA2Builder<PlanningSolution>(problem, crossoverOperator, mutationOperator)
					.setMaxIterations(parameters.getNumberOfIterations())
					.setPopulationSize(parameters.getPopulationSize())
					.build();
				new AlgorithmRunner.Executor(algorithm).execute();
				population = algorithm.getResult();
				break;
			}
			/*case STEADY: {
				Algorithm<PlanningSolution> algorithm = new GeneticAlgorithmBuilder<PlanningSolution>(problem, crossoverOperator, mutationOperator)
						.setMaxEvaluations(parameters.getPopulationSize()*parameters.getNumberOfIterations())
						.setPopulationSize(parameters.getPopulationSize())
						.setVariant(GeneticAlgorithmBuilder.GeneticAlgorithmVariant.STEADY_STATE)
						.build();
				new AlgorithmRunner.Executor(algorithm).execute();
				population = new ArrayList<>();
				population.add(algorithm.getResult());
				break;
			}*/
			default:
				System.err.println("Algorithm not implemented");
				population = null;
				break;
		}
		
		return population;
	}

}
