package entities;

import java.awt.image.BufferedImage;

import main.AdvancedSound;
import main.Game;
import world.AStar;
import world.Vector2i;

public class EnemyGreen extends Enemy {
	
	private int random = 0;
	private int countRandom = 0;
	private int maxCount = 60;

	public EnemyGreen(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		enemyRight = new BufferedImage[2];
		enemyLeft = new BufferedImage[2];
		
		enemyLeft[0] = Game.spriteSheet.getSprite(0, 80, 16, 16);
		enemyLeft[1] = Game.spriteSheet.getSprite(0, 96, 16, 16);
		enemyRight[0] = Game.spriteSheet.getSprite(16, 80, 16, 16);
		enemyRight[1] = Game.spriteSheet.getSprite(16, 96, 16, 16);
	}
	
	@Override
	public void tick() {
		if(!ghost && !dead){
			countRandom++;
			
			if(countRandom == maxCount) {
				random = Game.rand.nextInt(1000);
				countRandom = 0;
			}
	
			if(path == null || path.isEmpty() || ghostFrames == ghostMaxFrames || random >= 950) {
				ghostFrames = 0;
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i((int)((Game.player.x+8)/16), (int)((Game.player.y+8)/16));
				path = AStar.findPath(start, end);
			} else if(random >= 925) {
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i(16/16, 16/16);
				path = AStar.findPath(start, end);
			} else if(random >= 900) {
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i(448/16, 16/16);
				path = AStar.findPath(start, end);
			} else if(random >= 875) {
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i(16/16, 288/16);
				path = AStar.findPath(start, end);
			} else if(random >= 850) {
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i(448/16, 288/16);
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
