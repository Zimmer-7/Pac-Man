package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import grafics.SpriteSheet;
import main.AdvancedSound;
import main.Game;
import main.Sound;
import world.Wall;
import world.World;

public class Player extends Entity {
	
	public double maxLife = 1;
	public double life = maxLife;
	
	public boolean left;
	public boolean right;
	public boolean up;
	public boolean down;
	public double speed = 1;
	
	public int mx;
	public int my;
	
	public int score = 0;
	private int scoreCount = 0;
	
	private int frames = 0;
	private int damageFrames = 0;
	private int maxFrames = 6;
	private int index = 0;
	private int maxIndex = 1;
	
	private boolean movedRight = false;
	private boolean movedLeft = false;
	private boolean movedUp = false;
	private boolean movedDown = false;
	public boolean damaged = false;
	
	private BufferedImage[] playerRight;
	private BufferedImage[] playerLeft;
	private BufferedImage[] playerUp;
	private BufferedImage[] playerDown;
	private BufferedImage playerDamage;
	
	public Player(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	
		playerRight = new BufferedImage[2];
		playerLeft = new BufferedImage[2];
		playerUp = new BufferedImage[2];
		playerDown = new BufferedImage[2];
		
		for(int i = 0; i < 2; i++) {
			playerRight[i] = Game.spriteSheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		for(int i = 0; i < 2; i++) {
			playerLeft[i] = Game.spriteSheet.getSprite(80 + (i*16), 0, 16, 16);
		}
		for(int i = 0; i < 2; i++) {
			playerUp[i] = Game.spriteSheet.getSprite(32 + (i*16), 16, 16, 16);
		}
		for(int i = 0; i < 2; i++) {
			playerDown[i] = Game.spriteSheet.getSprite(80 + (i*16), 16, 16, 16);
		}
		
		depth = 2;
	}
	
	@Override
	public void tick() {
		movedRight = false;
		movedLeft = false;
		movedUp = false;
		movedDown = false;
		
		if(right && !isCollidingWall((int)(x+speed), (int)y)) {
			movedRight = true;
			x+=speed;
			scoreCount++;
			if(scoreCount == 16) {
				score--;
				scoreCount = 0;
			}
			
			if(isCollidingWall((int)(x+speed), (int)y)) {
				right = false;
				left = false;
				up = false;
				down = false;
			}
		}
		
		if(left && !isCollidingWall((int)(x-speed), (int)y)) {
			movedLeft = true;
			x-=speed;
			scoreCount++;
			if(scoreCount == 16) {
				score--;
				scoreCount = 0;
			}
			if(isCollidingWall((int)(x-speed), (int)y)) {
				right = false;
				left = false;
				up = false;
				down = false;
			}
		}
		
		if(up && !isCollidingWall((int)x, (int)(y-speed))) {
			movedUp = true;
			y-=speed;
			scoreCount++;
			if(scoreCount == 16) {
				score--;
				scoreCount = 0;
			}
			if(isCollidingWall((int)x, (int)(y-speed))) {
				right = false;
				left = false;
				up = false;
				down = false;
			}
		}
		
		if(down && !isCollidingWall((int)x, (int)(y+speed))) {
			movedDown = true;
			y+=speed;
			scoreCount++;
			if(scoreCount == 16) {
				score--;
				scoreCount = 0;
			}
			if(isCollidingWall((int)x, (int)(y+speed))) {
				right = false;
				left = false;
				up = false;
				down = false;
			}
		}
			
		frames ++;
		if(frames == maxFrames) {
			index++;
			frames = 0;
			
			if(index > maxIndex) 
				index = 0;
				
		}
		
		if(damaged) {
			damageFrames ++;
			if(damageFrames == maxFrames) {
				damageFrames = 0;
				damaged = false;
			}
		}
		
		if(life <= 0) {
			Game.gameState = "Game Over";
		}
		
		checkApples();
		
	}
	
	public void checkApples() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if(isColliding(this, current)) {
				if(current instanceof MiniApple) {
					AdvancedSound.pickup.play();
					Game.entities.remove(i);
					Game.countApples--;
					score += 11;
					return;
				}
				if(current instanceof MegaApple) {
					AdvancedSound.powerup.play();
					AdvancedSound.powerup.play();
					Game.entities.remove(i);
					for(int j = 0; j < Game.enemies.size(); j++) {
						Game.enemies.get(j).ghost = true;
						Game.enemies.get(j).ghostFrames = 0;
					}
					return;
				}
				if(current instanceof TeleportRight) {
					x = TeleportLeft.xLeft-16;
					y = TeleportLeft.yLeft;
					right = false;
					left = false;
					up = false;
					down = false;
				}
				if(current instanceof TeleportLeft) {
					x = TeleportRight.xRight+16;
					y = TeleportRight.yRight;
					right = false;
					left = false;
					up = false;
					down = false;
				}
			}
		}
	}
	
	@Override
	public void render(Graphics g){
		if(movedRight) {
			g.drawImage(playerRight[index], this.getX(), this.getY(), null);
		}else if(movedLeft) {
			g.drawImage(playerLeft[index], this.getX(), this.getY(), null);
		}else if(movedUp) {
			g.drawImage(playerUp[index], this.getX(), this.getY(), null);
		}else if(movedDown) {
			g.drawImage(playerDown[index], this.getX(), this.getY(), null);
		} else {
			g.drawImage(playerRight[0], this.getX(), this.getY(), null);
		}
		/*g.setColor(Color.yellow);
		g.drawRect(16, 16, 16, 16);
		g.drawRect(448, 16, 16, 16);
		g.drawRect(16, 288, 16, 16);
		g.drawRect(448, 288, 16, 16);*/
	}
	
}
