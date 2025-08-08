package entities;

import java.awt.image.BufferedImage;

public class MiniApple extends Entity {

	public MiniApple(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		depth = 0;
	}

}
