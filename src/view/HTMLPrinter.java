package view;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import entities.Employee;
import entities.PlannedTask;
import logic.NextReleaseProblem;
import logic.PlanningSolution;

public class HTMLPrinter implements Runnable {
	
	/* --- Attributes --- */
	
	/**
	 * The NRP to solve
	 */
	private NextReleaseProblem problem;
	
	/**
	 * The solutions to display
	 */
	private List<PlanningSolution> solutions;
	
	
	/* --- Constructors --- */
	
	/**
	 * Constructor
	 * Prepares the problem and the solutions attributes in order to be able to compute the run() method
	 * @param problem The NRP problem to solve
	 * @param solutions The Solutions of the NRP problem
	 */
	public HTMLPrinter(NextReleaseProblem problem, List<PlanningSolution> solutions) {
		this.problem = problem;
		this.solutions = solutions;
	}

	@Override
	public void run() {
		
		File htmlFile = new File("test/output/planning.html");
		FileWriter fileW;

		try {
			fileW = new FileWriter(htmlFile);
			BufferedWriter bufferW = new BufferedWriter(fileW);
			bufferW.write(getHTMLPageCode());
			bufferW.close();
			Desktop.getDesktop().browse(htmlFile.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the necessary column number to display
	 * @param solution the solution to display
	 * @return The number of columns necessary to display the planning
	 */
	private int getNumberOfTimeSlots(PlanningSolution solution) {
		return new Double(solution.getEndDate()).intValue();
	}
	
	/**
	 * Return the HTML content of the page to display
	 * @return the String containing the HTML code to display
	 */
	private String getHTMLPageCode() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("<!doctype html><html lang=\"en\"><head><meta charset=\"utf-8\">")
			.append("<title>").append("Planning for the Next Release").append("</title>")
			.append(getStyleBalise()).append("</head><body>");
		
		int i = 1;
		for (PlanningSolution solution : solutions) {
			builder.append("<h2>Solution ").append(i++).append("</h2>")
				.append(getSolutionTableTag(solution));
		}
		
		builder.append("</body></html>");
		
		return builder.toString();
	}
	
	/**
	 * Returns the solution's planning table tag
	 * @param solution the solution to display
	 * @return The table tag containing all the planning of the solution
	 */
	private StringBuilder getSolutionTableTag(PlanningSolution solution) {
		StringBuilder sb = new StringBuilder();
		int numberOfTimeSlots = getNumberOfTimeSlots(solution);
		
		if (solution.getNumberOfPlannedTasks() == 0) {
			return sb.append("There is no planned task in this solution");
		}
		sb.append("<table><thead><tr><th></th>");
		
		// Head row of the planning table
		for (int i = 0 ; i < numberOfTimeSlots ; ++i) {
			sb.append("<th>").append(i).append("h - ").append(i+1).append("h").append("</th>");
		}
		sb.append("</tr></thead><tbody>");
		
		// Employee's rows of the planning table
		for (Employee employee : problem.getEmployees()) {
			sb.append("<tr><td>").append(employee.getName()).append("</td>");
			List<PlannedTask> tasksOfEmployee = solution.getTasksDoneBy(employee);
			int colspan = 0;
			for (int j = 0 ; j < numberOfTimeSlots ; j++) {
				PlannedTask currentTask = null;
				if (tasksOfEmployee.size() > 0) {
					currentTask = tasksOfEmployee.get(0);
				}
				if (currentTask != null && currentTask.getBeginHour() == 1.0*j) {
					colspan = new Double(currentTask.getTask().getDuration()).intValue();
					sb.append("<td class=\"task\" colspan=\"").append(colspan).append("\">").append(currentTask.getTask().getName()).append("</td>");
					tasksOfEmployee.remove(0);
					colspan--;
				}
				else {
					if (colspan == 0) {
						sb.append("<td></td>");
					}
					else {
						colspan--;
					}
				}
			}
			// In case of bug (tasks not displayed) //TODO Quit when resolved
			while (tasksOfEmployee.size() > 0) {
				sb.append("<td>").append(tasksOfEmployee.get(0).getTask().getName()).append(" undisplayed</td>");
				tasksOfEmployee.remove(0);
			}
		}
				
		sb.append("</tr></tbody></table>");
		
		return sb;
	}
	

	/**
	 * Returns the HTML style tag to put in the header of the HTML page
	 * @return the HTML style tag with style data
	 */
	private StringBuilder getStyleBalise() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<style>")
			.append("table { width: auto; overflow-x: scroll; white-space: nowrap; border-collapse: collapse; } ")
			.append("table, th, td { border: 1px solid black; } ")
			.append("th, td { padding: 5px; } ")
			.append(".task { background-color: #dfdfdf; }</style>");
		
		return sb;
	}
}
