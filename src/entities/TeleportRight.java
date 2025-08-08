package entities;

import java.awt.image.BufferedImage;

public class TeleportRight extends Entity {
	
	public static double xRight;
	public static double yRight;

	public TeleportRight(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		xRight = x;
		yRight = y;
	}

}
