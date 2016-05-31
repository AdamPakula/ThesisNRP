package program;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entities.Employee;
import entities.Priority;
import entities.Skill;
import entities.Task;

public class DataLoader {
	
	private final static int INDEX_TASK_NAME = 0;
	private final static int INDEX_TASK_PRIORITY = 1;
	private final static int INDEX_TASK_DURATION = 2;
	private final static int INDEX_TASK_SKILLS = 3;
	private final static int INDEX_TASK_PREVIOUS = 4;
	private final static int INDEX_EMPLOYEE_NAME = 0;
	private final static int INDEX_EMPLOYEE_AVAILABILITY = 1;
	private final static int INDEX_EMPLOYEE_SKILLS = 2;
	
	public final static String INPUT_DIRECTORY = new String("test/inputs/");

	public static Object[] readData(TestFile file) {
		Object[] data = null;
		
		try(BufferedReader tasksBufferedReader = new BufferedReader(new FileReader(INPUT_DIRECTORY + file.getTasksFileName()));
				BufferedReader employeesBufferedReader = new BufferedReader(new FileReader(INPUT_DIRECTORY + file.getEmployeesFileName()))) {
			
			// Reading the tasks and skills
			List<Skill> skills = new ArrayList<>();
			List<Task> tasks = new ArrayList<>();
			String line = tasksBufferedReader.readLine();

			while (line != null) {
				tasks.add(readTask(line, skills, tasks));
				
				line = tasksBufferedReader.readLine();
			}
			tasksBufferedReader.close();
			
			// Reading the employees
			List<Employee> employees = new ArrayList<>();
			line = employeesBufferedReader.readLine();

			while (line != null) {
				employees.add(readEmployee(line, skills));
				
				line = employeesBufferedReader.readLine();
			}
			employeesBufferedReader.close();
			
			data = new Object[2];
			data[0] = tasks;
			data[1] = employees;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}

	/**
	 * Reads an employee data line
	 * Updates the skills list if there are news
	 * @param line the line to read
	 * @param skills the skills already known
	 * @return the new employee, or null if the line is not well-formed
	 */
	private static Employee readEmployee(String line, List<Skill> skills) {
		String[] parts = line.split("\\t");
		
		if (parts.length != 3)
			return null;
		
		Employee employee = new Employee(parts[INDEX_EMPLOYEE_NAME], 
								new Double(parts[INDEX_EMPLOYEE_AVAILABILITY]), 
								readSkills(skills, parts[INDEX_EMPLOYEE_SKILLS]));

		return employee;
	}

	/**
	 * Reads a task in the line and update the skills list if there is a new
	 * @param line data file line
	 * @param skills list of skills already known
	 * @param tasks The tasks already planned
	 * @return the new task or null if the line is not well formated
	 */
	private static Task readTask(String line, List<Skill> skills, List<Task> tasks) {
		String[] parts = line.split("\\t");
		
		if (parts.length < 4)
			return null;
		
		List<Task> previousTasks = new ArrayList<>();
		if(parts.length == 5) {
			String previousTaksNames[] = parts[INDEX_TASK_PREVIOUS].split(",");
			for (String previousTaskName : previousTaksNames) {
				previousTasks.add(findTask(tasks, previousTaskName));
			}
		}
		
		Task task = new Task(parts[INDEX_TASK_NAME], 
						Priority.getPriorityByLevel(new Integer(parts[INDEX_TASK_PRIORITY])), 
						new Double(parts[INDEX_TASK_DURATION]), 
						previousTasks, 
						readSkills(skills, parts[INDEX_TASK_SKILLS]));
		
		return task;
	}
	
	private static List<Skill> readSkills(List<Skill> skills, String skillsList) {
		String skillsStr[] = skillsList.split(",");
		List<Skill> result = new ArrayList<>();
		for (String skillStr : skillsStr) {
			Skill skill = findSkill(skills, skillStr);
			if (skill == null) {
				skill = new Skill(skillStr);
				skills.add(skill);
			}
			result.add(skill);
		}
		return result;
	}
	
	/**
	 * Find a skill by its name in the skills list
	 * @param skills the list of skills already known
	 * @param skillName the name of the searched skill
	 * @return the skill or null if it is not already in the list
	 */
	private static Skill findSkill(List<Skill> skills, String skillName) {
		for (Skill skill : skills) {
			if (skill.getName().equals(skillName)) {
				return skill;
			}
		}
		return null;
	}
	
	/**
	 * Find a task by its name in the tasks list
	 * @param tasks the list of tasks
	 * @param name the name of the task to search
	 * @return the corresponding task or null if it does not exist
	 */
	private static Task findTask(List<Task> tasks, String name) {
		Task task = null;
		int i = 0;
		
		while (task == null && i < tasks.size()) {
			if (tasks.get(i).getName().equals(name)) {
				task = tasks.get(i);
			}
			i++;
		}
		
		return task;
	}
}
