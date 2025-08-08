package entities;

import java.awt.image.BufferedImage;

import main.AdvancedSound;
import main.Game;
import world.AStar;
import world.Vector2i;

public class EnemyPink extends Enemy {
	
	private int prep = 0;

	public EnemyPink(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		enemyRight = new BufferedImage[2];
		enemyLeft = new BufferedImage[2];
		
		enemyLeft[0] = Game.spriteSheet.getSprite(32, 48, 16, 16);
		enemyLeft[1] = Game.spriteSheet.getSprite(32, 64, 16, 16);
		enemyRight[0] = Game.spriteSheet.getSprite(48, 48, 16, 16);
		enemyRight[1] = Game.spriteSheet.getSprite(48, 64, 16, 16);
	}
	
	@Override
	public void tick() {
		if(!ghost && !dead){
	
			if(prep >= 60) {
				if(path == null || path.isEmpty() || ghostFrames == ghostMaxFrames) {
					ghostFrames = 0;
					Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
					Vector2i end = new Vector2i((int)((Game.player.x+8)/16), (int)((Game.player.y+8)/16));
					path = AStar.findPath(start, end);
				}
					
				if(touching() && frames == 0) {
					AdvancedSound.hurt.play();
					Game.player.life --;
					if(Game.rand.nextInt(100) < 25)
						Game.player.life --;
					Game.player.damaged = true;
				}
			} else {
				prep++;
			}
		} 
				
		super.tick();
	}

}
