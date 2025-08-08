package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import main.AdvancedSound;
import main.Game;
import main.Sound;
import world.AStar;
import world.Node;
import world.Vector2i;
import world.World;

public class Enemy extends Entity {
	
	protected double initSpeed = 0.9;
	protected double speed = initSpeed;;
	protected boolean right = false;
	protected boolean left = false;
	protected boolean dead = false;
	
	private double initX;
	private double initY;
	
	public boolean ghost = false;
	public int ghostFrames = 0;
	protected int ghostMaxFrames = 60*8;
	
	protected int frames = 0;
	protected int damageFrames = 0;
	protected int maxFrames = 6;
	protected int index = 0;
	protected int maxIndex = 1;
	
	protected BufferedImage[] enemyRight;
	protected BufferedImage[] enemyLeft;
	protected BufferedImage[] enemyGhostLeft;
	protected BufferedImage[] enemyGhostRight;
	protected BufferedImage enemyDeadLeft;
	protected BufferedImage enemyDeadRight;

	public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		initX = x;
		initY = y;
		
		enemyGhostRight = new BufferedImage[2];
		enemyGhostLeft = new BufferedImage[2];
		
		enemyGhostLeft[0] = Game.spriteSheet.getSprite(32, 80, 16, 16);
		enemyGhostLeft[1] = Game.spriteSheet.getSprite(32, 96, 16, 16);
		enemyGhostRight[0] = Game.spriteSheet.getSprite(48, 80, 16, 16);
		enemyGhostRight[1] = Game.spriteSheet.getSprite(48, 96, 16, 16);
		enemyDeadLeft = Game.spriteSheet.getSprite(64, 80, 16, 16);
		enemyDeadRight = Game.spriteSheet.getSprite(80, 80, 16, 16);
		
		depth = 1;
		
	}
	
	@Override
	public void tick() {
		
		if(ghost && !dead) {
			
			if(ghostFrames == 0 || path == null || path.isEmpty()) {
				path = avoidPath();
				speed = initSpeed/2;
			}
			
			ghostFrames++;
			
			if(touching()) {
				AdvancedSound.hurt.play();
				Game.player.score += 101;
				dead = true;
				ghostFrames = ghostMaxFrames;
				ghost = false;
				speed = initSpeed*2;
				path = ressurectPath();
				return;
			}
			
			if(ghostFrames == ghostMaxFrames) {
				ghost = false;
				speed = initSpeed;
			}
		}
		
		if(dead && (path == null || path.isEmpty())) {
			speed = initSpeed;
			dead = false;
		}
		
		followPath(path, speed);
		
		frames ++;
		if(frames == maxFrames) {
			index++;
			frames = 0;
				
			if(index > maxIndex) 
				index = 0;
				
		}
		
	}
	
	public boolean touching() {
		Rectangle current = new Rectangle(this.getX() + maskx+1, this.getY() + masky+1, maskw-2, maskh-2);
		Rectangle player = new Rectangle(Game.player.getX()+1, Game.player.getY()+1, 14, 14);
		
		return current.intersects(player);
	}
	
	public boolean checkDamage() {
		
		return false;
	}
	
	public void followPath(List<Node> path, double speed) {
		if (path != null && !path.isEmpty()) {
	        Vector2i target = path.get(path.size() - 1).tile;

	        double targetX = target.x * 16;
	        double targetY = target.y * 16;

	        double dx = targetX - x;
	        double dy = targetY - y;

	        double distance = Math.sqrt(dx * dx + dy * dy);

	        if (distance >= speed) {

	            if (dx > 0 && World.isFree((int)(x + speed), (int)y)) {
	            	x += (dx / distance) * speed;
	                right = true;
	                left = false;
	            } else if (dx < 0 && World.isFree((int)(x - speed), (int)y)) {
	            	x += (dx / distance) * speed;
	                right = false;
	                left = true;
	            } 
	            if((dy > 0 && World.isFree((int)x, (int)(y + speed))) || (dy < 0 && World.isFree((int)x, (int)(y - speed)))) {
	            	 y += (dy / distance) * speed;
	            }
	        } else {
	            x = targetX;
	            y = targetY;
	            path.remove(path.size() - 1);
	        }
	    }
			
	}
	
	public List<Node> avoidPath() {
		Vector2i start = new Vector2i((int)(this.x/16), (int)(this.y/16));
		Vector2i end = null;
		
		double[] corners = new double[4];
		corners[0] = calcDistance((int)(Game.player.x+8), 16, (int)(Game.player.y+8), 16);
		corners[1] = calcDistance((int)(Game.player.x+8), 448, (int)(Game.player.y+8), 16);
		corners[2] = calcDistance((int)(Game.player.x+8), 16, (int)(Game.player.y+8), 288);
		corners[3] = calcDistance((int)(Game.player.x+8), 448, (int)(Game.player.y+8), 288);
		
		double maior = corners[0];
		
		for(int i = 1; i < 4; i++)
			if(corners[i] > maior)
				maior = corners[i];
		
		for(int i = 1; i < 4; i++)
			if(corners[i] == maior) {
				if(i == 0)
					end = new Vector2i((16/16), (16/16));
				if(i == 1)
					end = new Vector2i((448/16), (16/16));
				if(i == 2)
					end = new Vector2i((16/16), (288/16));
				if(i == 3)
					end = new Vector2i((448/16), (288/16));
				
				return AStar.findPath(start, end);
			}
		
		return Collections.emptyList();
		
	}
	
	public List<Node> ressurectPath() {
		Vector2i start = new Vector2i((int)(this.x/16), (int)(this.y/16));
		Vector2i end = new Vector2i((int)(initX/16), (int)(initY/16));
		
		return AStar.findPath(start, end);
	}
	
	@Override
	public void render(Graphics g) {
		if(right) {
			if(dead) {
				g.drawImage(enemyDeadRight, this.getX(), this.getY(), null);
			} else if(ghost) {
				g.drawImage(enemyGhostRight[index], this.getX(), this.getY(), null);
			} else {
				g.drawImage(enemyRight[index], this.getX(), this.getY(), null);
			}
			
		}else if(left) {
			if(dead) {
				g.drawImage(enemyDeadLeft, this.getX(), this.getY(), null);
			} else if(ghost) {
				g.drawImage(enemyGhostLeft[index], this.getX(), this.getY(), null);
			} else {
				g.drawImage(enemyLeft[index], this.getX(), this.getY(), null);
			}
		} else {
			g.drawImage(enemyRight[0], this.getX(), this.getY(), null);
		}
		
		//g.setColor(Color.blue);
		//g.drawRect(this.getX() + maskx, this.getY() + masky, maskw, maskh);
	}

}
