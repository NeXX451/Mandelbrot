package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import mandelbrot.Mandelbrot;

public class InfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel minRe = new JLabel("Min Re:");
	private final JLabel minIm = new JLabel("Min Im:");
	private final JLabel maxRe = new JLabel("Max Re:");
	private final JLabel maxIm = new JLabel("Max Im:");
	private final JLabel scale = new JLabel("Scale:");

	private final SpinnerModel hueFactorModel = new SpinnerNumberModel(100, 1,
			2000, 10);
	private final JSpinner hueFactor = new JSpinner(hueFactorModel);

	private final SpinnerModel initialHueModel = new SpinnerNumberModel(0.33, 0.01,
			0.99, 0.01);
	private final JSpinner initialHue = new JSpinner(initialHueModel);
	
	private final Mandelbrot set;
	private final GUI gui;

	private final JButton update = new JButton("Update");

	public InfoPanel(Mandelbrot set, GUI gui) {
		this.gui = gui;
		this.set = set;
		init();
		initActionListeners();
	}

	private void initActionListeners() {
		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				set.setHueFactor((int) hueFactor.getValue());
				set.setInitialHue(((Double) initialHue.getValue()).floatValue());
				gui.updateImage();
			}
		});
	}

	private void init() {
		updateLabels();

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		add(minRe, c);

		c.gridx = 1;
		c.gridy = 0;
		add(minIm, c);
//		
//		c.gridx = 0;
//		c.gridy = 1;
//		c.gridwidth = 1;
//		add(maxRe, c);
//
//		c.gridx = 1;
//		c.gridy = 1;
//		add(maxIm, c);

		c.gridx = 2;
		c.gridy = 0;
		add(scale, c);

		c.gridx = 0;
		c.gridy = 2;
		add(new JLabel("Hue Factor:"), c);

		c.gridx = 1;
		c.gridy = 2;
		add(hueFactor, c);
		
		c.gridx = 2;
		c.gridy = 2;
		add(new JLabel("Initial Hue:"), c);
		
		c.gridx = 3;
		c.gridy = 2;
		add(initialHue, c);
		
		c.gridx = 4;
		c.gridy = 2;
		add(update, c);

	}

	private void updateLabels() {
		minRe.setText("Min Re: " + set.getReStart());
		minIm.setText("Min Im: " + set.getImStart());
		maxRe.setText("Max Re: " + set.getReEnd());
		maxIm.setText("Max Im: " + (set.getImStart() + (set.getReEnd() - set.getReStart())));
		scale.setText("Scale: " + set.getStep());
	}

	public void update() {
		updateLabels();
		revalidate();
	}

}
