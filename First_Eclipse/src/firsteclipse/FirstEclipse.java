package firsteclipse;

import processing.core.PApplet;
import processing.core.PConstants;
import javafx.scene.shape.Box;
import peasy.*;

public class FirstEclipse extends PApplet {

	PeasyCam cam;

	public int division = 100;
	float roomSize = 80f;
	ManageBoxes manBox= new ManageBoxes(this);

	public void settings() {
		size(800, 800, P3D);
	}

	public void setup() {
		// size(800, 800, P3D);
		cam = new PeasyCam(this, 120);
		// cam.setMinimumDistance(50);
		// cam.setMaximumDistance(500);

		colorMode(PConstants.HSB, 360);
		manBox.setup();

		noStroke();
		rectMode(PConstants.CENTER);
	}

	public void draw() {
		background(210);
		rect(0, 0, roomSize, roomSize);

		lights();

		manBox.draw();

	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { firsteclipse.FirstEclipse.class.getName() });
	}
}
