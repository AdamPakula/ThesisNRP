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
import entities.DefaultGeneratorParameters;
import entities.GeneratorParameters;
import entities.ProblemData;
import entities.parameters.DefaultIterationParameters;
import entities.parameters.IterationParameters;
import parameters.DefaultParameters;
import program.GeneratorNRP;

public class ExperimentController {
	
	public ExperimentController() {
		XYDataset ds = executeExperiment();
		JFreeChart chart = ChartFactory.createScatterPlot("Experimenting chart", "size", "quality", ds);
		ChartFrame frame = new ChartFrame("First", chart);
        frame.pack();
        frame.setVisible(true);
	}
	
	public XYDataset executeExperiment() {
		int nbEmployees = 1;
		int nbFeatures = DefaultParameters.FEATURES_BY_EMPLOYEE;
		int size = nbEmployees * nbFeatures;
		SolutionQuality qualityAttribute = new SolutionQuality();
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
			XYSeries series = new XYSeries(algorithm.toString());
			dataset.addSeries(series);
		}
		
		while (size <= DefaultParameters.MAX_PROBLEM_SIZE) {
			ProblemData data = GeneratorNRP.generate(new GeneratorParameters(nbFeatures, nbEmployees, nbEmployees, DefaultGeneratorParameters.PRECEDENCE_RATE));
			Map<AlgorithmChoice, Double[]> qualityValues = new HashMap<>();
			for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
				qualityValues.put(algorithm, new Double[DefaultParameters.TEST_REPRODUCTION]);
			}
			
			for (int i = 0; i < DefaultParameters.TEST_REPRODUCTION ; i++) {
				NextReleaseProblem nrp = new NextReleaseProblem(data.getFeatures(), data.getEmployees(), new IterationParameters(DefaultIterationParameters.NUMBER_OF_WEEK, DefaultIterationParameters.HOURS_BY_WEEK));
				
				for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
					System.out.println("Executing algorithm " + algorithm.toString() + " (size: " + size +")");
					AlgorithmExecutor executor = new AlgorithmExecutor(nrp, new ExecutorParameters());
					PlanningSolution solution = PopulationFilter.getBestSolution(executor.executeAlgorithm(algorithm));
					Double[] values = qualityValues.get(algorithm);
					values[i] = qualityAttribute.getAttribute(solution);
				}
			}
			
			for (AlgorithmChoice algorithm : AlgorithmChoice.values()) {
				dataset.getSeries(algorithm.toString()).add(size, getAverage(qualityValues.get(algorithm)));
			}
			
			nbEmployees++;
			nbFeatures += DefaultParameters.FEATURES_BY_EMPLOYEE;
			size = nbEmployees * nbFeatures;
		}
		
		return dataset;
	}
	
	
	private double getAverage(Double[] values) {
		double sum = 0.0;
		
		for (Double value : values) {
			sum += value;
		}
		
		return sum/values.length;
	}
}
