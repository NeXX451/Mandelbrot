package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import mandelbrot.Mandelbrot;

public class OptionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel re = new JLabel("Re:");
	private final JLabel im = new JLabel("Im:");
	private final JLabel scale = new JLabel("Scale:");

	private final Mandelbrot set;

	public OptionPanel(Mandelbrot set) {
		this.set = set;
		init();
	}

	private void init() {
		re.setText("Re: " + set.getReStart());
		im.setText("Im: " + set.getImStart());
		scale.setText("Scale: " + set.getStep());

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		add(re, c);
		c.gridx = 1;
		c.gridy = 0;
		add(im, c);
		c.gridx = 2;
		c.gridy = 0;
		add(scale, c);
	}

	public void update() {
		re.setText("Re: " + set.getReStart());
		im.setText("Im: " + set.getImStart());
		scale.setText("Scale: " + set.getStep());
		revalidate();
	}

}
