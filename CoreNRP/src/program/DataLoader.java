package program;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entities.Employee;
import entities.PriorityLevel;
import entities.ProblemData;
import entities.Skill;
import entities.Feature;

public class DataLoader {
	
	private final static int INDEX_FEATURE_NAME = 0;
	private final static int INDEX_FEATURE_PRIORITY = 1;
	private final static int INDEX_FEATURE_DURATION = 2;
	private final static int INDEX_FEATURE_SKILLS = 3;
	private final static int INDEX_FEATURE_PREVIOUS = 4;
	private final static int INDEX_EMPLOYEE_NAME = 0;
	private final static int INDEX_EMPLOYEE_AVAILABILITY = 1;
	private final static int INDEX_EMPLOYEE_SKILLS = 2;
	
	public final static String INPUT_DIRECTORY = new String("../test/inputs/");

	public static ProblemData readData(TestFile file) {
		ProblemData data = null;
		
		try(BufferedReader featuresBufferedReader = new BufferedReader(new FileReader(INPUT_DIRECTORY + file.getFeaturesFileName()));
				BufferedReader employeesBufferedReader = new BufferedReader(new FileReader(INPUT_DIRECTORY + file.getEmployeesFileName()))) {
			
			// Reading the features and skills
			List<Skill> skills = new ArrayList<>();
			List<Feature> features = new ArrayList<>();
			String line = featuresBufferedReader.readLine();

			while (line != null) {
				features.add(readFeature(line, skills, features));
				
				line = featuresBufferedReader.readLine();
			}
			featuresBufferedReader.close();
			
			// Reading the employees
			List<Employee> employees = new ArrayList<>();
			line = employeesBufferedReader.readLine();

			while (line != null) {
				employees.add(readEmployee(line, skills));
				
				line = employeesBufferedReader.readLine();
			}
			employeesBufferedReader.close();
			
			data = new ProblemData(features, employees, skills);
		} catch (FileNotFoundException e) {
			System.err.println("Input file not found");
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
	 * Reads a feature in the line and update the skills list if there is a new
	 * @param line data file line
	 * @param skills list of skills already known
	 * @param features The features already planned
	 * @return the new feature or null if the line is not well formated
	 */
	private static Feature readFeature(String line, List<Skill> skills, List<Feature> features) {
		String[] parts = line.split("\\t");
		
		if (parts.length < 4)
			return null;
		
		List<Feature> previousFeatures = new ArrayList<>();
		if(parts.length == 5) {
			String previousFeaturesNames[] = parts[INDEX_FEATURE_PREVIOUS].split(",");
			for (String previousFeatureName : previousFeaturesNames) {
				previousFeatures.add(findFeature(features, previousFeatureName));
			}
		}
		
		Feature feature = new Feature(parts[INDEX_FEATURE_NAME], 
						PriorityLevel.getPriorityByLevel(new Integer(parts[INDEX_FEATURE_PRIORITY])), 
						new Double(parts[INDEX_FEATURE_DURATION]), 
						previousFeatures, 
						readSkills(skills, parts[INDEX_FEATURE_SKILLS]));
		
		return feature;
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
	 * Find a feature by its name in the features list
	 * @param features the list of features
	 * @param name the name of the feature to search
	 * @return the corresponding feature or null if it does not exist
	 */
	private static Feature findFeature(List<Feature> features, String name) {
		Feature feature = null;
		int i = 0;
		
		while (feature == null && i < features.size()) {
			if (features.get(i).getName().equals(name)) {
				feature = features.get(i);
			}
			i++;
		}
		
		return feature;
	}
}
