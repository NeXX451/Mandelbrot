package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import mandelbrot.Mandelbrot;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Dimension dim = new Dimension(800, 850);

	private static final int SCROLL_FACTOR = 100;

	private Point start;
	private Point end;
	private Mandelbrot set;
	private ImagePanel mandelbrot;

	private Rectangle rect;
	private OptionPanel options;

	public GUI() {
		initGUI();
	}

	private void initGUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(dim);
		set = new Mandelbrot(dim.width, dim.height - 50);
		BufferedImage image = set.getImage();
		mandelbrot = new ImagePanel(image);
		options = new OptionPanel(set);

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == 's') {
					try {
						saveImage(mandelbrot.getImage());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if (e.getKeyChar() == 'r') {
					setImage(Mandelbrot.RE_START_DEFAULT,
							Mandelbrot.IM_START_DEFAULT,
							Mandelbrot.RE_END_DEFAULT);
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {
				double step = set.getStep();
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					System.out.println("right scroll");
					setImage(set.getReStart() + step * SCROLL_FACTOR,
							set.getImStart(), set.getReEnd() + step
									* SCROLL_FACTOR);
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					System.out.println("left scroll");
					setImage(set.getReStart() - step * SCROLL_FACTOR,
							set.getImStart(), set.getReEnd() - step
									* SCROLL_FACTOR);
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					System.out.println("up scroll");
					setImage(set.getReStart(),
							set.getImStart() + step * SCROLL_FACTOR, set.getReEnd());
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					System.out.println("down scroll");
					setImage(set.getReStart(),
							set.getImStart() - step * SCROLL_FACTOR, set.getReEnd());
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});

		mandelbrot.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Point upperleft = getUpperLeftPoint(start, e.getPoint());
				Point downright = getDownRightPoint(start, e.getPoint());

				rect = new Rectangle(upperleft.x, upperleft.y, downright.x
						- upperleft.x, downright.y - upperleft.y);
				mandelbrot.repaint();
			}
		});

		mandelbrot.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				start = e.getPoint();
				System.out.println("pressed: " + start);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				rect = null;
				Point p = e.getPoint();
				if (p.x != start.x) {
					Point temp = getUpperLeftPoint(start, p);
					end = getDownRightPoint(start, p);
					start = temp;
					System.out.println("released, start: " + start + ", end: "
							+ end);
					GUI.this.zoom();
				}
			}

		});

		this.setLayout(new BorderLayout());
		add(mandelbrot, BorderLayout.CENTER);
		add(options, BorderLayout.PAGE_START);
		setVisible(true);
	}

	private Point getUpperLeftPoint(Point p1, Point p2) {
		Point upperleft = new Point();

		if (p1.x < p2.x) {
			upperleft.x = p1.x;
		} else {
			upperleft.x = p2.x;
		}

		if (p1.y < p2.y) {
			upperleft.y = p1.y;
		} else {
			upperleft.y = p2.y;
		}
		return upperleft;
	}

	private Point getDownRightPoint(Point p1, Point p2) {
		Point downright = new Point();

		if (p1.x < p2.x) {
			downright.x = p2.x;
		} else {
			downright.x = p1.x;
		}

		if (p1.y < p2.y) {
			downright.y = p2.y;
		} else {
			downright.y = p1.y;
		}

		return downright;
	}

	private void zoom() {
		double curReStart = set.getReStart();
		double curImStart = set.getImStart();
		double step = set.getStep();

		double newReStart = curReStart + start.x * step;
		double newImStart = curImStart - start.y * step;

		double newReEnd = curReStart + end.x * step;

		setImage(newReStart, newImStart, newReEnd);
	}

	private void setImage(double newReStart, double newImStart, double newReEnd) {
		BufferedImage image = set.getImage(newReStart, newImStart, newReEnd);
		mandelbrot.setImage(image);
		options.update();
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
		}

		public BufferedImage getImage() {
			return image;
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, this);
			g.setColor(Color.yellow);
			if (rect != null) {
				g.drawRect(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}

	private void saveImage(BufferedImage image) throws IOException {
		String name = "mandelbrot" + System.currentTimeMillis() + ".png";
		System.out.println("save image");
		File outputfile = new File(name);
		ImageIO.write(image, "png", outputfile);
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
