package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import world.World;

public class Menu {
	
	public String[] options1 = {"Novo Jogo", "Sair"};
	public String[] options2 = {"Continuar"};
	public int currOption = 0;
	public int maxOption = options1.length - 1;
	
	private static String saveFile = "save.txt";
	
	public boolean up = false;
	public boolean down = false;
	public boolean enter = false;
	public static boolean pause = false;
	
	public static boolean saveGame = false;
	
	public void tick() {
		if(up) {
			currOption --;
			up = false;
			if(currOption < 0) {
				currOption = maxOption;
			}
		}
		if(down) {
			currOption ++;
			down = false;
			if(currOption > maxOption) {
				currOption = 0;
			}
		}
		if(enter) {
			enter = false;
			if(currOption == 0) {
				Game.gameState = "Prep";
				if(!pause) {
					File file = new File(saveFile);
					file.delete();
				}
				pause = false;
			}
			if(currOption == 1) {
				System.exit(1);
			}
		}
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter(saveFile));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current += ":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value[n]+=encode;
				current+=value[n];
			}
			try {
				write.write(current);
				if(i < val1.length-1)
					write.newLine();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		try {
			write.flush();
			write.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File(saveFile);
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader(saveFile));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":", 2);
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i]-=encode;
							trans[1]+=val[i];
						}
						line+=trans[0];
						line+=":";
						line+=trans[1];
						line+="/";
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return line;
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.yellow);
		g.setFont(Game.newFontBig);
		g.drawString("Pac-Man", Game.WIDTH*Game.SCALE/2-195, Game.HEIGHT*Game.SCALE/2-95);
		g.setFont(Game.newFontSmall);
		if(!pause) {
			g.drawString(options1[0], Game.WIDTH*Game.SCALE/2-140, Game.HEIGHT*Game.SCALE/2+20);
		}
		if(pause) {
			g.drawString(options2[0], Game.WIDTH*Game.SCALE/2-140, Game.HEIGHT*Game.SCALE/2+20);
		}
		g.drawString(options1[1], Game.WIDTH*Game.SCALE/2-140, Game.HEIGHT*Game.SCALE/2+70);
		
		g.setFont(new Font("Arial", Font.BOLD, 32));
		
		if(currOption == 0) {
			g.drawString(">", Game.WIDTH*Game.SCALE/2-160, Game.HEIGHT*Game.SCALE/2+18);
		}
		if(currOption == 1) {
			g.drawString(">", Game.WIDTH*Game.SCALE/2-160, Game.HEIGHT*Game.SCALE/2+68);
		}
		
	}
}
