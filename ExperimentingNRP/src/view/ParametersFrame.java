/**
 * 
 */
package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

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
import entities.DefaultGeneratorParameters;
import entities.GeneratorParameters;
import entities.parameters.DefaultIterationParameters;
import entities.parameters.IterationParameters;
import logic.ExecutorController;

/**
 * @author Vavou
 *
 */
public class ParametersFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JSpinner nbWeekSpinner;
	private JSpinner hoursByWeekSpinner;
	private JSpinner nbFeaturesSpinner;
	private JSpinner nbEmployeesSpinner;
	private JSpinner nbSkillsSpinner;
	private JSpinner precedenceRateSpinner;
	private JButton launchButton;
	private JComboBox<AlgorithmChoice> algorithmComboBox;
	
	private ExecutorController controller;

	/**
	 * 
	 */
	public ParametersFrame(ExecutorController controller) {
		super("Execute Algorithm");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.controller = controller;
		
		initializeComponents();
		initializeValues();
		
		pack();
	}
	
	private void initializeValues() {
		hoursByWeekSpinner.setValue(DefaultIterationParameters.HOURS_BY_WEEK);
		nbWeekSpinner.setValue(DefaultIterationParameters.NUMBER_OF_WEEK);
		nbFeaturesSpinner.setValue(DefaultGeneratorParameters.NUMBER_OF_FEATURES);
		nbEmployeesSpinner.setValue(DefaultGeneratorParameters.NUMBER_OF_EMPLOYEES);
		nbSkillsSpinner.setValue(DefaultGeneratorParameters.NUMBER_OF_SKILLS);
		precedenceRateSpinner.setValue(DefaultGeneratorParameters.PRECEDENCE_RATE);
		
	}

	private void initializeComponents() {
		int margin = 20;
		Locale.setDefault(Locale.Category.FORMAT, Locale.ENGLISH);
		
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
		algorithmComboBox = new JComboBox<>(AlgorithmChoice.values());
		mainPanel.add(algorithmComboBox, gbc);
		
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
		hoursByWeekSpinner = new JSpinner(new SpinnerNumberModel(1.0, 1.0, 24.0*7, 1.0));
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
		mainPanel.add(new JLabel("Number of features: "), gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		nbFeaturesSpinner = new JSpinner(getNewPositiveIntegerModel());
		raiseTextFieldSize(nbFeaturesSpinner);
		mainPanel.add(nbFeaturesSpinner, gbc);
		
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
		launchButton = new JButton("Launch");
		getRootPane().setDefaultButton(launchButton);
		launchButton.addActionListener(this);
		launchButton.setFocusPainted(false);
		mainPanel.add(launchButton, gbc);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == launchButton) {
			int nbFeatures = (int) nbFeaturesSpinner.getValue(),
				nbEmployees = (int) nbEmployeesSpinner.getValue(),
				nbSkills = (int) nbSkillsSpinner.getValue(),
				nbWeeks = (int)nbWeekSpinner.getValue();

			double precedenceRate = (double)precedenceRateSpinner.getValue(),
				hoursByWeek = (double)hoursByWeekSpinner.getValue();
			
			GeneratorParameters genParam = new GeneratorParameters(nbFeatures, nbEmployees, 
					nbSkills, precedenceRate);
			IterationParameters iterationParam = new IterationParameters(nbWeeks, hoursByWeek);
			controller.launch((AlgorithmChoice)algorithmComboBox.getSelectedItem(), genParam, iterationParam);
		}
	}
}
