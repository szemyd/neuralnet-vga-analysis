package firsteclipse;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

import java.util.ArrayList;

import com.jogamp.opengl.GLStateKeeper.Listener;

import javafx.scene.shape.Box;
import peasy.*;

public class FirstEclipse extends PApplet {

	PeasyCam cam;

	PShape s;

	ManageBoxes manBox = new ManageBoxes(this);
	Environment env = new Environment(this);
	SpaceSyntax spaceSyntax = new SpaceSyntax(this);
	
	public ArrayList<MyThread> threads = new ArrayList<MyThread>();

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

		
		manBox.setup(); // 01. Creates the boxes in a random form.
		env.loadData(); // 03. Loads the CSV file for the surrounding buildings.
		

		threads.add(new MyThread("First Thread", this, manBox));
		threads.add(new MyThread("Second Thread", this, manBox));

		analysisSetup();
		
//		for (int i = 0; i < 2; i++) {
//			analysisSetup();
//			Glv.seed++;
//		}
	}

	public void draw() {
		background(110);
		lights();
		rotate(HALF_PI); // Rotate the whole scene.

		manBox.draw();
		if (Glv.shouldSpaceSyntax)
			spaceSyntax.draw();
		env.draw(s);

		
		if(!threads.get(0).isAlive() && !threads.get(1).isAlive())
		{
			GenerateCSV.save();
		}
//		if (thread.isAlive("startThread")) {
//			GenerateCSV.save();
//		}
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

		for (MyThread thread : threads) {
			thread.start();
		}
		
		
		if (Glv.shouldSpaceSyntax) {
			//thread("startThread");// spaceSyntax.VGA(manBox.boxes); // 04. Calculates the VGA analysis (Which rect sees which).
		}
	}
//
//	public void startThread() {
//
//		int ellapsedTime = second() + minute() * 60 + hour() * 360;
//		SpaceSyntax.VGA(manBox.boxes);
//
//		//	Glv.isDone = true;
//		//println(Glv.isDone);
//		println("Ellapsed time: " + ((second() + minute() * 60 + hour() * 360) - ellapsedTime));
//
//	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { firsteclipse.FirstEclipse.class.getName() });
	}
}
