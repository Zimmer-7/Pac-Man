package entities;

import java.awt.image.BufferedImage;

import main.AdvancedSound;
import main.Game;
import world.AStar;
import world.Vector2i;

public class EnemyOrange extends Enemy {
	
	public static boolean rightOn = false;
	public static boolean leftOn = false;
	public static boolean upOn = false;
	public static boolean downOn = false;

	public EnemyOrange(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		enemyRight = new BufferedImage[2];
		enemyLeft = new BufferedImage[2];
		
		enemyLeft[0] = Game.spriteSheet.getSprite(64, 48, 16, 16);
		enemyLeft[1] = Game.spriteSheet.getSprite(64, 64, 16, 16);
		enemyRight[0] = Game.spriteSheet.getSprite(80, 48, 16, 16);
		enemyRight[1] = Game.spriteSheet.getSprite(80, 64, 16, 16);
	}
	
	@Override
	public void tick() {
		if(!ghost && !dead){
	
			if(path == null || path.isEmpty() || ghostFrames == ghostMaxFrames) {
				ghostFrames = 0;
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i((int)((Game.player.x+8)/16), (int)((Game.player.y+8)/16));
				path = AStar.findPath(start, end);
			} else if(rightOn) {
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i((int)((Game.player.x+8)/16)+2, (int)((Game.player.y+8)/16));
				rightOn = false;
				path = AStar.findPath(start, end);
			} else if(leftOn) {
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i((int)((Game.player.x+8)/16)-2, (int)((Game.player.y+8)/16));
				leftOn = false;
				path = AStar.findPath(start, end);
			} else if(upOn) {
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i((int)((Game.player.x+8)/16), (int)((Game.player.y+8)/16)-2);
				upOn = false;
				path = AStar.findPath(start, end);
			} else if(downOn) {
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i((int)((Game.player.x+8)/16), (int)((Game.player.y+8)/16)+2);
				downOn = false;
				path = AStar.findPath(start, end);
			}
				
			if(touching() && frames == 0) {
				AdvancedSound.hurt.play();
				Game.player.life --;
				if(Game.rand.nextInt(100) < 25)
					Game.player.life --;
				Game.player.damaged = true;
			}
		} 
				
		super.tick();
	}

}
