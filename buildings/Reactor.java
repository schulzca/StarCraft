package buildings;

import java.awt.image.BufferedImage;

import utilities.GameObject;
import utilities.Util;

public class Reactor extends GameObject{

	private static BufferedImage[] icons = null;

	public Reactor(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isStationary = true;
	}
}