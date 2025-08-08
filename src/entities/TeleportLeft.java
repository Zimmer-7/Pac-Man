package entities;

import java.awt.image.BufferedImage;

public class TeleportLeft extends Entity{
	
	public static double xLeft;
	public static double yLeft;

	public TeleportLeft(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		xLeft = x;
		yLeft = y;
	}

}
