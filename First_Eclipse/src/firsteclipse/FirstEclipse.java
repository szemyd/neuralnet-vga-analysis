package firsteclipse;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import javafx.scene.shape.Box;
import peasy.*;

public class FirstEclipse extends PApplet {

	PeasyCam cam;

	PShape s;
	PImage i;

	ManageBoxes manBox = new ManageBoxes(this);

	public void settings() {
		size(1600, 1200, P3D);
	}

	public void setup() {
		// size(800, 800, P3D);
		cam = new PeasyCam(this, 160);
		// cam.setMinimumDistance(50);
		// cam.setMaximumDistance(500);

		randomSeed(Glv.seed);
		colorMode(PConstants.HSB, 360);
		manBox.setup();

		s = loadShape("solid.OBJ");
		i = loadImage("map2D.PNG");

		noStroke();
		rectMode(PConstants.CENTER);
	}

	public void draw() {
		background(0);
		lights();
		//stroke(GRAY);

//		pushStyle();
//		{
//			fill(120,0,360);
//			rect(0, 0, Glv.roomSizeX, Glv.roomSizeY);
//			// s.disableStyle();
//		}
//		popStyle();

		

		
		pushMatrix();
		{
			// rotateX(HALF_PI);
			translate(-556 * 0.5f, -556 * 0.5f);
			// image(i, 0, 0);
		}
		popMatrix();

		pushStyle();
		{
			fill(360);
			shape(s, 0, 0);
			// s.disableStyle();
		}
		popStyle();

		manBox.draw();

	}

	public void keyPressed() {

		if (keyCode == LEFT) {
			Glv.seed--;
			manBox.setup();
		}
		if (keyCode == RIGHT) {
			Glv.seed++;
			manBox.setup();
		}
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { firsteclipse.FirstEclipse.class.getName() });
	}
}
