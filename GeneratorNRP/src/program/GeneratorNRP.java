package program;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.Employee;
import entities.GeneratorParameters;
import entities.Priority;
import entities.ProblemData;
import entities.Skill;
import entities.Task;

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
			
			writeFiles(generate(new GeneratorParameters(nbTasks, nbEmployees, nbSkills, 0.1)));
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
		List<Skill> skills = new ArrayList<>(parameters.getNumberOfSkills());
		List<Task> tasks = new ArrayList<>(parameters.getNumberOfTasks());
		List<Employee> employees = new ArrayList<>(parameters.getNumberOfEmployees());
		Random randomGenerator = new Random();
		
		// Initialization of the skills
		for (int i = 1 ; i <= parameters.getNumberOfSkills() ; i++) {
			skills.add(new Skill("Skill " + i));
		}
		
		
		// Initialization of the tasks
		Priority[] priorities = Priority.values();
		
		for (int i = 1 ; i <= parameters.getNumberOfTasks() ; i++) {
			List<Task> previousTasks = new ArrayList<>();
			if (randomGenerator.nextDouble() < parameters.getRateOfPrecedenceConstraints()) {
				if (tasks.size() > 0) {
					int nbPreviousTasks = randomGenerator.nextInt(tasks.size());
					List<Task> futurPreviousTasks = new ArrayList<>(tasks);
					for (int j = 0 ; j < nbPreviousTasks ; j++) {
						int indexTask = randomGenerator.nextInt(futurPreviousTasks.size());
						previousTasks.add(futurPreviousTasks.get(indexTask));
						futurPreviousTasks.remove(indexTask);
					}
				}
			}
			
			List<Skill> requiredSkills = new ArrayList<>(1);
			requiredSkills.add(skills.get(randomGenerator.nextInt(skills.size())));
			
			tasks.add(new Task("Task " + i,
					priorities[randomGenerator.nextInt(priorities.length)],
					1.0 * (1 + randomGenerator.nextInt(40)),
					previousTasks,
					requiredSkills));
		}
		
		
		// Initialization of the employees
		for (int i = 1 ; i <= parameters.getNumberOfEmployees() ; i ++) {
			List<Skill> employeeSkills = new ArrayList<>(1);
			employeeSkills.add(skills.get(randomGenerator.nextInt(skills.size())));
			employees.add(new Employee("Employee " + i, 
					1.0 * (5 + randomGenerator.nextInt(30)), 
					employeeSkills));
		}
		
		return new ProblemData(tasks, employees, skills);
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
			bufferW.write(getTasksText(data.getTasks()));
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
	private static String getTasksText(List<Task> tasks) {
		StringBuilder sb = new StringBuilder();
		
		for (Task task : tasks) {
			sb.append(task.getName()).append('\t')
				.append(task.getPriority().getLevel()).append('\t')
				.append(task.getDuration()).append('\t')
				.append(task.getRequiredSkills().get(0).getName()).append('\t');
			
			for (Task previousTask : task.getPreviousTasks()) {
				sb.append(previousTask.getName()).append(',');
			}
			if (task.getPreviousTasks().size() > 0) {
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
