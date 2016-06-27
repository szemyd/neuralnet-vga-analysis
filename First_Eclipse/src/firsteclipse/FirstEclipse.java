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
	//PImage i;

	ManageBoxes manBox = new ManageBoxes(this);
	// SpaceSyntax spaceSyntax = new SpaceSyntax(this);
	Environment env = new Environment(this);
	SpaceSyntax spaceSyntax = new SpaceSyntax(this);

	public void settings() {
		size(1600, 1200, P3D);
	}

	public void setup() {
		cam = new PeasyCam(this, 160);
		// cam.setMinimumDistance(50);
		// cam.setMaximumDistance(500);

		randomSeed(Glv.seed);
		colorMode(PConstants.HSB, 360);

		
		manBox.setup(); // 02. Creates the boxes in a random form.
		spaceSyntax.setup(manBox.boxes); // 01. Creates starting grid of rectangles for the spacesyntax VGA.
		env.loadData(); // 03. Loads the CSV file for the surrounding buildings.
		spaceSyntax.VGA(manBox.boxes); // 04. Calculates the VGA analysis (Which rect sees which).
		
		s = loadShape("solid.OBJ"); // Load the 3D model of the public space.
	
		noStroke();
		rectMode(PConstants.CENTER);
	}

	public void draw() {
		background(110);
		lights();

		//manBox.draw();
		spaceSyntax.draw();
		env.draw(s);
	}

	public void keyPressed() {

		if (keyCode == LEFT) {
			Glv.seed--;
			manBox.setup();
			spaceSyntax.setup(manBox.boxes);
		}
		if (keyCode == RIGHT) {
			Glv.seed++;
			manBox.setup();
			spaceSyntax.setup(manBox.boxes);
		}
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { firsteclipse.FirstEclipse.class.getName() });
	}
}
