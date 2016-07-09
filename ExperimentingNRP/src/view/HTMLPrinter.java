package view;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import entities.Employee;
import entities.EmployeeWeekAvailability;
import entities.PlannedFeature;
import logic.NextReleaseProblem;
import logic.PlanningSolution;
import logic.SolutionQuality;

public class HTMLPrinter implements Runnable {
	
	/* --- Attributes --- */
	
	/**
	 * The solutions to display
	 */
	private List<PlanningSolution> solutions;
	
	
	/* --- Constructors --- */
	
	/**
	 * Constructor
	 * Prepares the problem and the solutions attributes in order to be able to compute the run() method
	 * @param solutions The Solutions of the NRP problem
	 */
	public HTMLPrinter(List<PlanningSolution> solutions) {
		this.solutions = solutions;
	}
	
	public HTMLPrinter(PlanningSolution solution) {
		this.solutions = new ArrayList<>();
		solutions.add(solution);
	}

	@Override
	public void run() {
		
		File htmlFile = new File("../test/output/planning.html");
		FileWriter fileW;

		try {
			fileW = new FileWriter(htmlFile);
			BufferedWriter bufferW = new BufferedWriter(fileW);
			bufferW.write(getHTMLPageCode());
			bufferW.close();
			Desktop.getDesktop().open(htmlFile);
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
		
		if (solution.getNumberOfPlannedFeatures() == 0) {
			return sb.append("There is no planned feature in this solution");
		}
		sb.append("<table><thead><tr><th></th>");
		
		// Head row of the planning table
		for (int i = 0 ; i < numberOfTimeSlots ; ++i) {
			sb.append("<th>").append(i).append("h - ").append(i+1).append("h").append("</th>");
		}
		sb.append("</tr></thead><tbody>");
		
		// Employee's rows of the planning table
		Map<Employee, List<EmployeeWeekAvailability>> availabilities = solution.getEmployeesPlanning();
		for (Employee e : availabilities.keySet()) {
			sb.append(getEmployeeRowTag(e, availabilities.get(e), numberOfTimeSlots));
		}
				
		sb.append("</tbody></table>");
		
		DecimalFormat df = new DecimalFormat() ; 
		df.setMaximumFractionDigits ( 2 ) ;
		
		sb.append("<p>End date: ").append(solution.getObjective(NextReleaseProblem.INDEX_END_DATE_OBJECTIVE))
			.append("<br />Quality: ").append(df.format(new SolutionQuality().getAttribute(solution))).append("%</p>");
		
		return sb;
	}
	
	private StringBuilder getEmployeeRowTag(Employee e, List<EmployeeWeekAvailability> plannings, int nbTimeSlot) {
		StringBuilder sb = new StringBuilder("<tr>");
		
		sb.append("<td>").append(e.getName()).append("</td>");
		
		int i = 0;
		Iterator<EmployeeWeekAvailability> it = plannings.iterator();
		EmployeeWeekAvailability currentPlanning = it.hasNext() ? it.next() : null;
		
		while (i < nbTimeSlot) {
			
			if (currentPlanning == null || currentPlanning.getBeginHour() != 1.0*i) { // If there is no more feature to display
				sb.append("<td></td>");
				i++;
			}
			else {
				sb.append(getWeekColumnTag(currentPlanning));
				i = new Double(currentPlanning.getEndHour()).intValue();
				currentPlanning = it.hasNext() ? it.next() : null;
			}
		}
		
		sb.append("</tr>");
		
		return sb;
	}
	

	private StringBuilder getWeekColumnTag(EmployeeWeekAvailability weekPlanning) {
		StringBuilder sb = new StringBuilder();
		double currentHour = weekPlanning.getBeginHour();
		
		for (PlannedFeature plannedFeature : weekPlanning.getPlannedFeatures()) {
			while (currentHour < plannedFeature.getBeginHour()) {
				sb.append("<td></td>");
				currentHour += 1.0;
			}
			double colspan = Math.min(weekPlanning.getEndHour(), plannedFeature.getEndHour()) - currentHour;
			sb.append("<td class=\"feature\" colspan=\"")
				.append(new Double(colspan).intValue())
				.append("\">").append(plannedFeature.getFeature().getName()).append("</td>");
			currentHour = plannedFeature.getEndHour();
		}
		
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
			.append(".feature { background-color: #dfdfdf; }</style>");
		
		return sb;
	}
}
