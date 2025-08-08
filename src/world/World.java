package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.*;
import grafics.SpriteSheet;
import main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH;
	public static int HEIGHT;
	private int countEn = 0;
	private int countTp = 0;
	//public TeleportRight tpr;
	
	public World(String path, int level) {
		
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			int[] pixels = new int[WIDTH * HEIGHT];
			tiles = new Tile[WIDTH * HEIGHT];
			map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
			int pos;
			for(int xx = 0; xx < WIDTH; xx++) {
				for(int yy = 0; yy < HEIGHT; yy++) {
					pos = xx + (yy*WIDTH);
					tiles[pos] = new Floor(xx*16, yy*16, Tile.TILE_FLOOR_GRASS_1);
					if(pixels[pos] == 0xFFFFFFFF) {
						//parede
						tiles[pos] = new Wall(xx*16, yy*16, Tile.TILE_WALL_DARK);
					}else if(pixels[pos] == 0xFF4838FF) {
						//personagem
						Game.player.setX(xx*16.0);
						Game.player.setY(yy*16.0);
					}else if(pixels[pos] == 0xFFFF0000) {
						//inimigo
						Enemy en = null;
						if(countEn == 0)
							en = new EnemyPink(xx*16.0, yy*16.0, 16, 16, null);
						if(countEn == 1)
							en = new EnemyBlue(xx*16.0, yy*16.0, 16, 16, null);
						if(countEn == 2)
							en = new EnemyGreen(xx*16.0, yy*16.0, 16, 16, null);
						if(countEn == 3)
							en = new EnemyOrange(xx*16.0, yy*16.0, 16, 16, null);
						Game.entities.add(en);
						Game.enemies.add(en);
						countEn++;
					}else if(pixels[pos] == 0xFFFFE97F) {
						MegaApple megaapple = new MegaApple(xx*16.0, yy*16.0, 16, 16, Entity.mega_apple);
						Game.entities.add(megaapple);
					}else if(pixels[pos] == 0xFFFF00DC) {
						if(countTp == 0) {
							TeleportRight tpr = new TeleportRight(xx*16.0, yy*16.0, 16, 16, null);
							Game.entities.add(tpr);
						}
						if(countTp == 1) {
							TeleportLeft tpl = new TeleportLeft(xx*16.0, yy*16.0, 16, 16, null);
							Game.entities.add(tpl);
						}
						
						countTp++;
					}else if(pixels[pos] == 0xFF00FF21) {
						//nada
					} else {
						MiniApple miniApple = new MiniApple(xx*16.0, yy*16.0, 16, 16, Entity.mini_apple);
						Game.entities.add(miniApple);
						Game.countApples++;
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean isFree(int x, int y) {
		int x1 = x/16;
		int y1 = y/16;
		int x2 = (x+16-1) / 16;
		int y2 = (y+16-1) / 16;
		
		return !(tiles[x1 + (y1*WIDTH)] instanceof Wall ||
				tiles[x1 + (y2*WIDTH)] instanceof Wall ||
				tiles[x2 + (y1*WIDTH)] instanceof Wall ||
				tiles[x2 + (y2*WIDTH)] instanceof Wall);
	}
	
	public static void startLevel(int level) {
		Game.entities.clear();
		Game.countApples = 0;
		Game.enemies.clear();
		Game.entities = new ArrayList<>();
		Game.enemies = new ArrayList<>();
		Game.spriteSheet = new SpriteSheet("/recursos.png");
		Game.player = new Player(0, 0, 16, 16, Game.spriteSheet.getSprite(32, 0, 16, 16));
		
		Game.world = new World("/mapa"+level+".png", level);
		
		Game.entities.add(Game.player);
		Game.gameState = "Normal";
	}

	public void render(Graphics g) {
		
		int xfinal = (Game.WIDTH >> 4);
		int yfinal = (Game.HEIGHT >> 4);
		
		Tile tile;
		for(int xx = 0; xx <= xfinal; xx++) {
			for(int yy = 0; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				
				tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
}
