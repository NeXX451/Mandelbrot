package mandelbrot;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Mandelbrot {

	private static final float ESCAPE_RADIUS = 2f;
	private static final int MAX_OF_MAX_IT = 1000;
	
	private int maxIt = 100;
	private final int width;
	private final int height;

	private double reStart = -2.15;
	private double imStart = 1.50;
	private double reEnd = 0.85;
	private double step;

	private double reC;
	private double imC;
	private double reZ;
	private double imZ;

	private int itCounter;

	private Color[] colors;
	private double modulus;

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
		int numOfColors = maxIt;
		colors = new Color[numOfColors];
		float hue = 0;
		float step = 0.003f;
		for (int i = 0; i < colors.length; i++) {
			step = (float) (1 / (Math.log(i + 2) * 100));
			hue += step;
			colors[i] = new Color(Color.HSBtoRGB(hue, 1f, 1f));
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

				if (itCounter >= maxIt) {
					image.setRGB(x, y, Color.black.getRGB());
				} else {
					modulus = absoluteValue(reZ, imZ);
					double mu = itCounter + 1 - Math.log(Math.log(modulus))
							/ Math.log(ESCAPE_RADIUS);
					mu = mu / maxIt * colors.length;
					image.setRGB(x, y, getColor(mu).getRGB());
				}
			}
		}
		return image;
	}

	/**
	 * Thanks to the following site that helped me smoothing the colors:
	 * http://www.vb-helper.com/howto_net_mandelbrot_smooth.html
	 * 
	 * @param mu
	 * @return
	 */
	private Color getColor(double mu) {
		int clr1 = (int) Math.floor(mu);
		float t2 = (float) (mu - clr1);
		float t1 = 1 - t2;
		clr1 = clr1 % colors.length;
		int clr2 = (clr1 + 1) % colors.length;
		int red = (int) (colors[clr1].getRed() * t1 + colors[clr2].getRed()
				* t2);
		int green = (int) (colors[clr1].getGreen() * t1 + colors[clr2]
				.getGreen() * t2);
		int blue = (int) (colors[clr1].getBlue() * t1 + colors[clr2].getBlue()
				* t2);
		return new Color(red, green, blue);
	}

	private void calculateF() {
		itCounter = 0;
		do {
			iterationStep();
			modulus = absoluteValue(reZ, imZ);
		} while (modulus <= ESCAPE_RADIUS && itCounter < maxIt);

		iterationStep();
		iterationStep();
	}

	private void iterationStep() {
		double tmp;
		itCounter++;
		tmp = reZ;
		reZ = reZ * reZ - imZ * imZ + reC;
		imZ = 2 * tmp * imZ + imC;
	}

	private double absoluteValue(double re, double im) {
		return Math.sqrt(re * re + im * im);
	}

	public BufferedImage getImage(double reStart, double imStart, double reEnd) {
		this.reStart = reStart;
		this.imStart = imStart;
		this.reEnd = reEnd;
		this.step = (reEnd - reStart) / width;
		double scale = 1 / (reEnd - reStart);
		System.out.println("scale: " + scale);
		calculateMaxIt(scale);
		if(maxIt > MAX_OF_MAX_IT) maxIt = MAX_OF_MAX_IT;
		System.out.println("MAX_IT: " + maxIt);
		initColors();
		System.out.println("new values: reStart " + reStart + ", imStart "
				+ imStart + ", reEnd " + reEnd + ", step " + step);
		return createImage();
	}

	//got the maxIt calculation from: http://math.stackexchange.com/questions/16970/a-way-to-determine-the-ideal-number-of-maximum-iterations-for-an-arbitrary-zoom
	private void calculateMaxIt(double scale) {
		this.maxIt = (int) (Math.sqrt(Math.abs(2 * Math.sqrt(Math.abs(1 - Math
				.sqrt(5 * scale))))) * 66.5);
	}

	public BufferedImage getImage() {
		return createImage();
	}

	public double getStep() {
		return step;
	}

}
