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
	// SpaceSyntax spaceSyntax = new SpaceSyntax(this);
	Environment env = new Environment(this);

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
		SpaceSyntax.setup(manBox.boxes, manBox.rectangles);
		env.loadData();

		s = loadShape("solid.OBJ");
		i = loadImage("map2D.PNG");

		noStroke();
		rectMode(PConstants.CENTER);
	}

	public void draw() {
		background(110);
		lights();

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
			// shape(s, 0, 0);
			// s.disableStyle();
		}
		popStyle();

		manBox.draw();
		// SpaceSyntax.draw();
		Environment.draw();

	}

	public void keyPressed() {

		if (keyCode == LEFT) {
			Glv.seed--;
			manBox.setup();
			SpaceSyntax.setup(manBox.boxes, manBox.rectangles);
		}
		if (keyCode == RIGHT) {
			Glv.seed++;
			manBox.setup();
			SpaceSyntax.setup(manBox.boxes, manBox.rectangles);
		}
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { firsteclipse.FirstEclipse.class.getName() });
	}
}
