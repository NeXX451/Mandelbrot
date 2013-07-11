package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import mandelbrot.Mandelbrot;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private Point start;
	private Point end;
	private Mandelbrot set;
	private ImagePanel panel;

	private Rectangle rect;

	public GUI() {
		initGUI();
	}

	private void initGUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.set = new Mandelbrot(800, 800);
		BufferedImage image = set.getImage();
		panel = new ImagePanel(image);

		panel.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				GUI.this.start = e.getPoint();
				System.out.println("pressed: " + start);
			}

			@Override
			public void mouseDragged(MouseEvent e) { //TODO
				rect = new Rectangle(start.x, start.y, e.getX() - start.x, e
						.getY() - start.y);
				System.out.println("set rect");
				GUI.this.revalidate();
				panel.repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				rect = null;
				GUI.this.end = e.getPoint();
				System.out.println("released: " + end);
				GUI.this.zoom();
			}

		});

		add(panel);
		setVisible(true);
	}

	private void zoom() {
		double curReStart = set.getReStart();
		double curImStart = set.getImStart();
		double step = set.getStep();

		double newReStart = curReStart + start.x * step;
		double newImStart = curImStart - start.y * step;

		double newReEnd = curReStart + end.x * step;

		BufferedImage image = set.getImage(newReStart, newImStart, newReEnd);
		System.out.println("set new image");
		panel.setImage(image);
	}

	private class ImagePanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private BufferedImage image;

		public ImagePanel(BufferedImage image) {
			this.image = image;
		}

		public void setImage(BufferedImage image) {
			this.image = image;
			repaint();
			System.out.println("repaint");
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, this);
			g.setColor(Color.yellow);
			if (rect != null) {
				System.out.println("draw rect");
				g.fillRect(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GUI();
			}
		});
	}
}
