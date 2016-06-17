package firsteclipse;

import processing.core.PApplet;
import processing.core.PConstants;
import javafx.scene.shape.Box;
import peasy.*;

public class FirstEclipse extends PApplet {

	PeasyCam cam;

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

		noStroke();
		rectMode(PConstants.CENTER);
	}

	public void draw() {
		background(0);
		rect(0, 0, Glv.roomSize, Glv.roomSize);

		lights();

		manBox.draw();

		println(Glv.seed);
	}

	public void keyPressed() {

		if (keyCode == LEFT)
			Glv.seed--;
		manBox.setup();
		if (keyCode == RIGHT)
			Glv.seed++;
		manBox.setup();

	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { firsteclipse.FirstEclipse.class.getName() });
	}
}
