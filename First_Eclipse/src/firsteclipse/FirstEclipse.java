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
	// PImage i;

	ManageBoxes manBox = new ManageBoxes(this);
	// SpaceSyntax spaceSyntax = new SpaceSyntax(this);
	Environment env = new Environment(this);
	SpaceSyntax spaceSyntax = new SpaceSyntax(this);
	//GenerateCSV generateCSV = new GenerateCSV();

	public void settings() {
		size(2400, 1200, P3D);
	}

	public void setup() {
		cam = new PeasyCam(this, 180);
		// cam.setMinimumDistance(50);
		// cam.setMaximumDistance(500);

		randomSeed(Glv.seed);
		colorMode(PConstants.HSB, 360);

		s = loadShape("solid.OBJ"); // Load the 3D model of the public space.

		noStroke();
		rectMode(PConstants.CENTER);

		analysisSetup();
	}

	public void draw() {
		background(110);
		lights();
		rotate(HALF_PI); // Rotate the whole scene.

		manBox.draw();
		if (Glv.shouldSpaceSyntax)
			spaceSyntax.draw();
		env.draw(s);
	}

	public void keyPressed() {

		if (keyCode == LEFT) {
			Glv.seed--;
			analysisSetup();
		}
		if (keyCode == RIGHT) {
			Glv.seed++;
			analysisSetup();
		}
		if (key == 's') {
			Glv.shouldSpaceSyntax = !Glv.shouldSpaceSyntax;
			thread("startThread");

			//			Runnable r = new MyThread(spaceSyntax,manBox.boxes);
			//			new Thread(r).start();
			//			new Thread(r).run();
		}
	}

	public void analysisSetup() {

		manBox.setup(); // 01. Creates the boxes in a random form.
		env.loadData(); // 03. Loads the CSV file for the surrounding buildings.
		spaceSyntax.setup(manBox.boxes); // 02. Creates starting grid of
		// rectangles for the spacesyntax
		// VGA.

		if (Glv.shouldSpaceSyntax) {
			thread("startThread");// spaceSyntax.VGA(manBox.boxes); // 04. Calculates the VGA analysis (Which rect sees which).
		}
	}

	public void startThread() {
		int ellapsedTime = second() + minute() * 60 + hour() * 360;
		spaceSyntax.VGA(manBox.boxes);

		GenerateCSV.save();
		
	//	Glv.isDone = true;
		//println(Glv.isDone);
		println("Loading Done. Ellapsed time: " + ((second() + minute() * 60 + hour() * 360) - ellapsedTime));
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { firsteclipse.FirstEclipse.class.getName() });
	}
}
