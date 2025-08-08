package grafics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import entities.Player;
import main.Game;
import world.World;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.drawString("Score = "+Game.player.score, 12, 14);
	}
	
}
