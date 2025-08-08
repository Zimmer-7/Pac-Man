package entities;

import java.awt.image.BufferedImage;

public class MegaApple extends Entity {

	public MegaApple(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		depth = 0;
	}

}
