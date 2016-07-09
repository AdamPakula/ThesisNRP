package logic;

import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import entities.AlgorithmChoice;
import entities.ExecutorParameters;
import entities.ExperimentType;
import entities.DefaultGeneratorParameters;
import entities.ExecuteResult;
import entities.GeneratorParameters;
import entities.ProblemData;
import entities.parameters.DefaultIterationParameters;
import entities.parameters.IterationParameters;
import parameters.DefaultParameters;
import program.GeneratorNRP;

public class ExperimentController {
	
	public ExperimentController(ExperimentType experiment) {
		XYDataset ds;
		String xname;
		switch (experiment) {
		case EMPLOYEES:
			ds = executeEmployeesExperiment();
			xname = "employees";
			break;
		case FEATURES:
			ds = executeFeaturesExperiment();
			xname = "features";
			break;
		default:
			ds = executeSizeExperiment();
			xname = "size";
			break;
		}
		JFreeChart chart = ChartFactory.createScatterPlot("Experimenting chart", xname, "quality", ds);
		ChartFrame frame = new ChartFrame("First", chart);
        frame.pack();
        frame.setVisible(true);
	}
	
	/**
	 * Execute an experiment raising the size
	 * @return the data of the experiment
	 */
	public XYDataset executeSizeExperiment() {
		int size = DefaultParameters.INITIAL_SIZE;
		ProblemData data;
		GeneratorParameters generatorParams = getParameters(size);
		SolutionQuality qualityAttribute = new SolutionQuality();
		
		XYSeriesCollection dataset = initializeSeries();
		
		while (size <= DefaultParameters.MAX_PROBLEM_SIZE) {
			Map<AlgorithmChoice, Double[]> qualityValues = new HashMap<>(AlgorithmChoice.values().length);
			for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
				qualityValues.put(algorithm, new Double[DefaultParameters.TEST_REPRODUCTION]);
			}
			
			for (int i = 0; i < DefaultParameters.TEST_REPRODUCTION ; i++) {
				data = GeneratorNRP.generate(generatorParams);
				NextReleaseProblem nrp = new NextReleaseProblem(data.getFeatures(), data.getEmployees(), new IterationParameters(DefaultIterationParameters.NUMBER_OF_WEEK, DefaultIterationParameters.HOURS_BY_WEEK));
				AlgorithmExecutor executor = new AlgorithmExecutor(nrp, new ExecutorParameters());

				for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
					printTrace(algorithm, size, i);
					ExecuteResult result = executor.executeAlgorithm(algorithm);
					Double[] values = qualityValues.get(algorithm);
					values[i] = qualityAttribute.getAttribute(result.getSolution());
				}
			}
			
			for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
				int realSize = generatorParams.getNumberOfEmployees() + generatorParams.getNumberOfFeatures();
				dataset.getSeries(algorithm.toString()).add(realSize, getAverage(qualityValues.get(algorithm)));
			}
			
			size += DefaultParameters.SIZE_INCREMENT;
			generatorParams = getParameters(size);
		}
		
		return dataset;
	}
	
	public XYDataset executeEmployeesExperiment() {
		int numberOfEmployees = DefaultParameters.INTIAL_EMPLOYEES;
		SolutionQuality qualityAttribute = new SolutionQuality();
		ProblemData data;
		XYSeriesCollection dataset = initializeSeries();
		
		GeneratorParameters generatorParams = new GeneratorParameters(
				DefaultParameters.NUMBER_OF_FEATURES, 
				numberOfEmployees, 
				(int) Math.round(DefaultParameters.NUMBER_OF_FEATURES * DefaultParameters.RATE_SKILLS_BY_FEATURE), 
				DefaultGeneratorParameters.PRECEDENCE_RATE);
		
		while (numberOfEmployees <= DefaultParameters.MAX_EMPLOYEES) {
			Map<AlgorithmChoice, Double[]> qualityValues = new HashMap<>(AlgorithmChoice.values().length);
			
			for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
				qualityValues.put(algorithm, new Double[DefaultParameters.TEST_REPRODUCTION]);
			}
			
			for (int i = 0; i < DefaultParameters.TEST_REPRODUCTION ; i++) {
				data = GeneratorNRP.generate(generatorParams);
				NextReleaseProblem nrp = new NextReleaseProblem(data.getFeatures(), data.getEmployees(), new IterationParameters(DefaultIterationParameters.NUMBER_OF_WEEK, DefaultIterationParameters.HOURS_BY_WEEK));
				AlgorithmExecutor executor = new AlgorithmExecutor(nrp, new ExecutorParameters());
				
				for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
					printTrace(algorithm, numberOfEmployees, i);
					ExecuteResult result = executor.executeAlgorithm(algorithm);
					Double[] values = qualityValues.get(algorithm);
					values[i] = qualityAttribute.getAttribute(result.getSolution());
				}
			}
			
			for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
				dataset.getSeries(algorithm.toString()).add(numberOfEmployees, getAverage(qualityValues.get(algorithm)));
			}
			
			numberOfEmployees += DefaultParameters.EMPLOYEES_INCREMENT;
			generatorParams.setNumberOfEmployees(numberOfEmployees);
		}
		
		return dataset;
	}
	
	public XYDataset executeFeaturesExperiment() {
		int numberOfFeatures = DefaultParameters.INTIAL_FEATURES;
		SolutionQuality qualityAttribute = new SolutionQuality();
		ProblemData data;
		XYSeriesCollection dataset = initializeSeries();
		
		GeneratorParameters generatorParams = new GeneratorParameters(
				numberOfFeatures, 
				DefaultParameters.NUMBER_OF_EMPLOYEES, 
				(int) Math.round(numberOfFeatures * DefaultParameters.RATE_SKILLS_BY_FEATURE), 
				DefaultGeneratorParameters.PRECEDENCE_RATE);
		
		while (numberOfFeatures <= DefaultParameters.MAX_FEATURES) {
			Map<AlgorithmChoice, Double[]> qualityValues = new HashMap<>(AlgorithmChoice.values().length);
			
			for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
				qualityValues.put(algorithm, new Double[DefaultParameters.TEST_REPRODUCTION]);
			}
			
			for (int i = 0; i < DefaultParameters.TEST_REPRODUCTION ; i++) {
				data = GeneratorNRP.generate(generatorParams);
				NextReleaseProblem nrp = new NextReleaseProblem(data.getFeatures(), data.getEmployees(), new IterationParameters(DefaultIterationParameters.NUMBER_OF_WEEK, DefaultIterationParameters.HOURS_BY_WEEK));
				AlgorithmExecutor executor = new AlgorithmExecutor(nrp, new ExecutorParameters());
				
				for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
					printTrace(algorithm, numberOfFeatures, i);
					ExecuteResult result = executor.executeAlgorithm(algorithm);
					Double[] values = qualityValues.get(algorithm);
					values[i] = qualityAttribute.getAttribute(result.getSolution());
				}
			}
			
			for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
				dataset.getSeries(algorithm.toString()).add(numberOfFeatures, getAverage(qualityValues.get(algorithm)));
			}
			
			numberOfFeatures += DefaultParameters.FEATURES_INCREMENT;
			generatorParams.setNumberOfFeatures(numberOfFeatures);
			generatorParams.setNumberOfSkills((int)Math.round(numberOfFeatures * DefaultParameters.RATE_SKILLS_BY_FEATURE));
		}
		
		return dataset;
	}
	
	/**
	 * Return the generator parameters for a size
	 * @param size the size
	 * @return the generator parameters
	 */
	private GeneratorParameters getParameters(int size) {
		int numberOfTasks = (int) Math.round(size/(1.0+DefaultParameters.RATE_EMPLOYEES_BY_FEATURE)),
			numberOfEmployees = (int) Math.round(numberOfTasks * DefaultParameters.RATE_EMPLOYEES_BY_FEATURE),
			numberOfSkills = (int) Math.round(numberOfTasks * DefaultParameters.RATE_SKILLS_BY_FEATURE);
		
		return new GeneratorParameters(numberOfTasks, numberOfEmployees, numberOfSkills, DefaultGeneratorParameters.PRECEDENCE_RATE);
	}
	
	/**
	 * Return the average of values
	 * @param values the values
	 * @return the average of the values
	 */
	private double getAverage(Double[] values) {
		double sum = 0.0;
		
		for (Double value : values) {
			sum += value;
		}
		
		return sum/values.length;
	}
	
	/**
	 * Print the current algorithm which is executed
	 * @param algorithm the algorithm
	 * @param size the size of problem
	 * @param repetition the repetition number
	 */
	private void printTrace(AlgorithmChoice algorithm, int size,  int repetition) {
		System.out.println(new StringBuilder("Executing algorithm ")
				.append(algorithm.toString())
				.append(" (size: ")
				.append(size)
				.append(", i = ")
				.append(repetition)
				.append(")"));
	}
	
	/**
	 * Create a new collection of series for all the algorithms
	 * @return initialized collection of algorithm series
	 */
	private XYSeriesCollection initializeSeries() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
			XYSeries series = new XYSeries(algorithm.toString());
			dataset.addSeries(series);
		}
		
		return dataset;
	}
}
