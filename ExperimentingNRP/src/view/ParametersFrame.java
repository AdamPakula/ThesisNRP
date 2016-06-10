/**
 * 
 */
package view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entities.AlgorithmChoice;

/**
 * @author Vavou
 *
 */
public class ParametersFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ParametersFrame() {
		super("Experimenting Next Release Problem");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		int margin = 20;
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(margin, margin, margin, margin));
		setContentPane(mainPanel);
		GridLayout mainLayout = new GridLayout(2, 2);
		mainLayout.setHgap(10);
		mainLayout.setVgap(10);
		setLayout(mainLayout);
		
		mainPanel.add(new JLabel("Algorithm: "));
		mainPanel.add(new JComboBox<>(AlgorithmChoice.values()));
		
		mainPanel.add(new JButton("Launch"));
		
		pack();
		setVisible(true);
	}

}
