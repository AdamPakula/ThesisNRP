package program;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.DefaultGeneratorParameters;
import entities.Employee;
import entities.GeneratorParameters;
import entities.PriorityLevel;
import entities.ProblemData;
import entities.Skill;
import entities.Feature;

public class GeneratorNRP {

	/**
	 * Generate new data and write it into files
	 * @param args number of tasks, employees and skill
	 */
	public static void main(String[] args) {
		if (args.length == 3) {
			int nbTasks = new Integer(args[0]);
			int nbEmployees = new Integer(args[1]);
			int nbSkills = new Integer(args[2]);
			
			writeFiles(generate(new GeneratorParameters(nbTasks, nbEmployees, nbSkills, DefaultGeneratorParameters.PRECEDENCE_RATE)));
		}
		else {
			System.err.println("Need the numbers of tasks, employees and skills in parameter");
		}
	}
	
	/**
	 * Generate data for a next release problem
	 * @param parameters the parameters of the generator
	 * @return the generated data
	 */
	public static ProblemData generate(GeneratorParameters parameters) {
		List<Skill> skills = generateSkills(parameters.getNumberOfSkills());
		List<Feature> tasks = generateFeatures(parameters.getNumberOfTasks(), parameters.getRateOfPrecedenceConstraints(), skills);
		List<Employee> employees = generateEmployees(parameters.getNumberOfEmployees(), skills);
		
		return new ProblemData(tasks, employees, skills);
	}
	
	/**
	 * Generate numberOfSkills skills into a list
	 * @param numberOfSkills the number of skills to generate
	 * @return the list containing the skills
	 */
	private static List<Skill> generateSkills(int numberOfSkills) {
		List<Skill> skills = new ArrayList<>(numberOfSkills);
		
		for (int i = 1 ; i <= numberOfSkills ; i++) {
			skills.add(new Skill("Skill " + i));
		}
		
		return skills;
	}
	
	/**
	 * Generates new list of new features
	 * @param numberOfFeatures the number of features to generate
	 * @param precedenciesRate the rate of precedences constraint
	 * @param skills the available skills
	 * @return the list of the new generated features
	 */
	private static List<Feature> generateFeatures(int numberOfFeatures, double precedenciesRate, List<Skill> skills) {
		Random randomGenerator = new Random();
		PriorityLevel[] priorities = PriorityLevel.values();
		int remainPreviousConstraints = new Double(numberOfFeatures * precedenciesRate).intValue();
		List<Feature> features = new ArrayList<>(numberOfFeatures);
		
		for (int i = 0 ; i < numberOfFeatures ; i++) {
			List<Feature> previousFeatures = new ArrayList<>();
			if (features.size() > 0 && remainPreviousConstraints > 0) {
				double probability = remainPreviousConstraints/(1.0*numberOfFeatures-i);
				List<Feature> possiblePreviousFeatures = new ArrayList<>(features);
				while (remainPreviousConstraints > 0 && possiblePreviousFeatures.size() > 0 && randomGenerator.nextDouble() < probability) {
					int indexFeature = randomGenerator.nextInt(possiblePreviousFeatures.size());
					previousFeatures.add(possiblePreviousFeatures.get(indexFeature));
					possiblePreviousFeatures.remove(indexFeature);
					remainPreviousConstraints--;
					probability = remainPreviousConstraints/(numberOfFeatures-i);
				}
			}
			
			List<Skill> requiredSkills = new ArrayList<>(1);
			requiredSkills.add(skills.get(randomGenerator.nextInt(skills.size())));
			
			features.add(new Feature("Task " + i,
					priorities[randomGenerator.nextInt(priorities.length)],
					1.0 * (1 + randomGenerator.nextInt(new Double(DefaultGeneratorParameters.MAX_FEATURE_DURATION).intValue())),
					previousFeatures,
					requiredSkills));
		}
		
		return features;
	}
	
	/**
	 * Generates new employees
	 * @param numberOfEmployees the number of employees to generate
	 * @param skills the available skills
	 * @return the list of new employees
	 */
	private static List<Employee> generateEmployees(int numberOfEmployees, List<Skill> skills) {
		Random randomGenerator = new Random();
		List<Employee> employees = new ArrayList<>(numberOfEmployees);
		int weekAvailabilityHightLimit = new Double(DefaultGeneratorParameters.MAX_EMPLOYEE_WEEK_AVAILABILITY - DefaultGeneratorParameters.MIN_EMPLOYEE_WEEK_AVAILABILITY + 1).intValue();
		
		for (int i = 1 ; i <= numberOfEmployees ; i ++) {
			int numberOfSkills = 1 + randomGenerator.nextInt(skills.size());
			List<Skill> employeeSkills = new ArrayList<>(numberOfSkills);
			List<Skill> availableSkills = new ArrayList<>(skills);
			for (int j = 0 ; j < numberOfSkills ; j++) {
				int skillIndex = randomGenerator.nextInt(availableSkills.size());
				employeeSkills.add(availableSkills.get(skillIndex));
				availableSkills.remove(skillIndex);
			}
			employees.add(new Employee("Employee " + i, 
					1.0 * (DefaultGeneratorParameters.MIN_EMPLOYEE_WEEK_AVAILABILITY + randomGenerator.nextInt(weekAvailabilityHightLimit)), 
					employeeSkills));
		}
		
		return employees;
	}

	/**
	 * Writes the data into generated.tasks and generated.employees files
	 * @param data the data to write
	 */
	private static void writeFiles(ProblemData data) {
		File tasksFile = new File("generated.tasks");
		File employeesFile = new File("generated.employees");
		FileWriter fileW;

		try {
			fileW = new FileWriter(tasksFile);
			BufferedWriter bufferW = new BufferedWriter(fileW);
			bufferW.write(getTasksText(data.getFeatures()));
			bufferW.close();
			
			fileW = new FileWriter(employeesFile);
			bufferW = new BufferedWriter(fileW);
			bufferW.write(getEmployeesText(data.getEmployees()));
			bufferW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Return all the tasks rows in string format
	 * @param tasks the tasks to write
	 * @return the tasks into string file format
	 */
	private static String getTasksText(List<Feature> tasks) {
		StringBuilder sb = new StringBuilder();
		
		for (Feature task : tasks) {
			sb.append(task.getName()).append('\t')
				.append(task.getPriority().getLevel()).append('\t')
				.append(task.getDuration()).append('\t')
				.append(task.getRequiredSkills().get(0).getName()).append('\t');
			
			for (Feature previousTask : task.getPreviousFeatures()) {
				sb.append(previousTask.getName()).append(',');
			}
			if (task.getPreviousFeatures().size() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			
			sb.append('\n');
		}
		sb.deleteCharAt(sb.length() - 1); // Deletes the last '\n'
		
		return sb.toString();
	}
	
	/**
	 * Return the employees in order to write in a data file
	 * @param employees the employees to write
	 * @return the string representing the employees
	 */
	private static String getEmployeesText(List<Employee> employees) {
		StringBuilder sb = new StringBuilder();
		
		for (Employee employee : employees) {
			sb.append(employee.getName()).append('\t')
				.append(employee.getWeekAvailability()).append('\t');
			
			for (Skill eSkill : employee.getSkills()) {
				sb.append(eSkill.getName()).append(',');
			}
			sb.deleteCharAt(sb.length() - 1); // Deletes the last ','
			
			sb.append('\n');
		}
		sb.deleteCharAt(sb.length() - 1); // Deletes the last '\n'
		
		return sb.toString();
	}
}
