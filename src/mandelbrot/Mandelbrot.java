package mandelbrot;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Mandelbrot {

	private static final int MAX_IT = 100;
	private final int width;
	private final int height;

	private double reC;
	private double imC;

	private double reStart = -2.15;
	private double imStart = 1.50;
	private double reEnd = 0.85;
	private double step;

	private double reZ;
	private double imZ;

	private int counter;

	private Color[] colors;

	public double getReStart() {
		return reStart;
	}

	public double getReEnd() {
		return reEnd;
	}

	public double getImStart() {
		return imStart;
	}

	public Mandelbrot(int width, int height) {
		this.width = width;
		this.height = height;
		this.step = (reEnd - reStart) / width;
		initColors();

	}

	private void initColors() {
		int numOfColors = 100;
		double blue = 100;
		double red = 0;
		double green = 0;
		colors = new Color[numOfColors];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = new Color((int) red, (int) green, (int) blue);
			blue = blue + 155 / (double) colors.length;
			red = red + 255 / (double) colors.length;
		}
	}

	private BufferedImage createImage() {
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				reC = reStart + step * x;
				imC = imStart - step * y;
				reZ = 0;
				imZ = 0;
				calculateF();

				if (counter == MAX_IT) {
					image.setRGB(x, y, Color.black.getRGB());
				} else {
					image.setRGB(x, y, colors[counter % colors.length].getRGB());
				}

			}
		}
		return image;
	}

	private void calculateF() {
		counter = 0;

		while (absoluteValue(reZ, imZ) < 4.0 && counter < MAX_IT) {
			counter++;
			double tmp = reZ;
			reZ = reZ * reZ - imZ * imZ + reC;
			imZ = 2 * tmp * imZ + imC;

		}
	}

	private double absoluteValue(double re, double im) {
		return Math.sqrt(re * re + im * im);
	}

	public BufferedImage getImage(double reStart, double imStart, double reEnd) {
		this.reStart = reStart;
		this.imStart = imStart;
		this.reEnd = reEnd;
		this.step = (reEnd - reStart) / width;
		System.out.println("new values: reStart " + reStart + ", imStart "
				+ imStart + ", reEnd " + reEnd + ", step " + step);
		return createImage();
	}

	public BufferedImage getImage() {
		return createImage();
	}

	public double getStep() {
		return step;
	}

}
