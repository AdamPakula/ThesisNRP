package logic;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.mocell.MOCellBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.pesa2.PESA2Builder;
//import org.uma.jmetal.algorithm.multiobjective.smsemoa.SMSEMOABuilder;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
//import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.AlgorithmRunner;

import entities.AlgorithmChoice;
import entities.ExecuteResult;
import entities.ExecutorParameters;
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
	private ExecutorParameters parameters;

	/**
	 * @param problem
	 */
	public AlgorithmExecutor(NextReleaseProblem problem, ExecutorParameters parameters) {
		this.problem = problem;
		this.parameters = parameters;
	}
	
	/**
	 * Execute an algorithm on the problem passed to the constructor
	 * @param algorithmChoice the algorithm to execute
	 * @return the result of the execution
	 */
	public ExecuteResult executeAlgorithm(AlgorithmChoice algorithmChoice) {
		ExecuteResult result = null;
		CrossoverOperator<PlanningSolution> crossoverOperator = new PlanningCrossoverOperator(problem);
	    MutationOperator<PlanningSolution> mutationOperator = new PlanningMutationOperator(problem);
	    
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
				AlgorithmRunner runner = new AlgorithmRunner.Executor(algorithm).execute();
				result = new ExecuteResult(PopulationFilter.getBestSolution(algorithm.getResult()), runner.getComputingTime());
				break;
			}
			case NSGAII: { 
				Algorithm<List<PlanningSolution>> algorithm = new NSGAIIBuilder<PlanningSolution>(problem, crossoverOperator, mutationOperator)
					.setMaxIterations(parameters.getNumberOfIterations())
					.setPopulationSize(parameters.getPopulationSize())
					.build();
				AlgorithmRunner runner = new AlgorithmRunner.Executor(algorithm).execute();
				result = new ExecuteResult(PopulationFilter.getBestSolution(algorithm.getResult()), runner.getComputingTime());
				break;
			}
			case PESA2: { 
				Algorithm<List<PlanningSolution>> algorithm = new PESA2Builder<PlanningSolution>(problem, crossoverOperator, mutationOperator)
					.setMaxEvaluations(parameters.getNumberOfIterations()*parameters.getPopulationSize())
					.setPopulationSize(parameters.getPopulationSize())
					.build();
				AlgorithmRunner runner = new AlgorithmRunner.Executor(algorithm).execute();
				result = new ExecuteResult(PopulationFilter.getBestSolution(algorithm.getResult()), runner.getComputingTime());
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
				AlgorithmRunner runner = new AlgorithmRunner.Executor(algorithm).execute();
				result = new ExecuteResult(PopulationFilter.getBestSolution(algorithm.getResult()), runner.getComputingTime());
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
				break;
		}
		
		return result;
	}

}
