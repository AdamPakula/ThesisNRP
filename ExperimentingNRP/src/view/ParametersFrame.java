/**
 * 
 */
package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import entities.AlgorithmChoice;
import parameters.DefaultParameters;

/**
 * @author Vavou
 *
 */
public class ParametersFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JSpinner nbWeekSpinner;
	private JSpinner hoursByWeekSpinner;
	private JSpinner nbTasksSpinner;
	private JSpinner nbEmployeesSpinner;
	private JSpinner nbSkillsSpinner;
	private JSpinner precedenceRateSpinner;

	/**
	 * 
	 */
	public ParametersFrame() {
		super("Execute Algorithm");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		initializeComponents();
		initializeValues();
		
		pack();
	}
	
	private void initializeValues() {
		hoursByWeekSpinner.setValue(DefaultParameters.HOURS_BY_WEEK);
		nbWeekSpinner.setValue(DefaultParameters.NUMBER_OF_WEEK);
		nbTasksSpinner.setValue(DefaultParameters.NUMBER_OF_TASKS);
		nbEmployeesSpinner.setValue(DefaultParameters.NUMBER_OF_EMPLOYEES);
		nbSkillsSpinner.setValue(DefaultParameters.NUMBER_OF_SKILLS);
		precedenceRateSpinner.setValue(DefaultParameters.PRECEDENCE_RATE);
		
	}

	private void initializeComponents() {
		int margin = 20;
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(margin, margin, margin, margin));
		setContentPane(mainPanel);
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Algorithm: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		mainPanel.add(new JComboBox<>(AlgorithmChoice.values()), gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(new JLabel("ITERATION"), gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Number of weeks: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		nbWeekSpinner = new JSpinner(getNewPositiveIntegerModel());
		raiseTextFieldSize(nbWeekSpinner);
		mainPanel.add(nbWeekSpinner, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Hours by week: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		hoursByWeekSpinner = new JSpinner(getNewPositiveIntegerModel());
		raiseTextFieldSize(hoursByWeekSpinner);
		mainPanel.add(hoursByWeekSpinner, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(new JLabel("TEST CASE"), gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Number of tasks: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		nbTasksSpinner = new JSpinner(getNewPositiveIntegerModel());
		raiseTextFieldSize(nbTasksSpinner);
		mainPanel.add(nbTasksSpinner, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Number of employees: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		nbEmployeesSpinner = new JSpinner(getNewPositiveIntegerModel());
		raiseTextFieldSize(nbEmployeesSpinner);
		mainPanel.add(nbEmployeesSpinner, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Number of skills: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		nbSkillsSpinner = new JSpinner(getNewPositiveIntegerModel());
		raiseTextFieldSize(nbSkillsSpinner);
		mainPanel.add(nbSkillsSpinner, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(new JLabel("Precedence rate: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		precedenceRateSpinner = new JSpinner(new SpinnerNumberModel(0.1, 0.0, 1.0, 0.1));
		raiseTextFieldSize(precedenceRateSpinner);
		mainPanel.add(precedenceRateSpinner, gbc);
		
		gbc.gridx = 1;
		gbc.gridy++;
		gbc.gridwidth = 1;
		mainPanel.add(new JButton("Launch"), gbc);
	}
    
    private void raiseTextFieldSize(JSpinner spinner) {
    	JComponent editor = spinner.getEditor();
    	JFormattedTextField ftf = ((JSpinner.DefaultEditor)editor).getTextField();
    	ftf.setColumns(6);
    }
    
    private SpinnerNumberModel getNewPositiveIntegerModel() {
    	SpinnerNumberModel model = new SpinnerNumberModel();
    	model.setMinimum(1);
    	return model;
    }
}
