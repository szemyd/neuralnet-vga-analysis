package firsteclipse;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

import java.util.ArrayList;

import org.omg.PortableInterceptor.ACTIVE;

import com.jogamp.opengl.GLStateKeeper.Listener;

import javafx.scene.shape.Box;
import peasy.*;

public class FirstEclipse extends PApplet {

	PeasyCam cam;
	PShape s;

	Environment env = new Environment(this);
	//	
	//	ManageBoxes manBox = new ManageBoxes(this);
	//	SpaceSyntax spaceSyntax = new SpaceSyntax(this);

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

		env.loadData(); // 03. Loads the CSV file for the surrounding buildings.

		for (int i = 0; i < Glv.numOfThreads; i++) {
			threads.add(new MyThread(this, i));
		}

		analysisSetup();
	}

	public void draw() {
		background(110);
		lights();
		rotate(HALF_PI); // Rotate the whole scene.

		if (threads.get(Glv.whichToDisplay).manBox.boxes[0][0] != null) {
			threads.get(Glv.whichToDisplay).manBox.draw();
		}
		if (Glv.shouldSpaceSyntax && threads.get(Glv.whichToDisplay).spaceSyntax.highLow != null)
			threads.get(Glv.whichToDisplay).spaceSyntax.draw(); // Draws the first SpaceSyntax analysis 

		env.draw(s); // Draws the environment.

		writeToFile(); // Checks if threads have terminated or not, and write to CSV if yes.
	}

	public void keyPressed() {
		if (keyCode == LEFT) {
			Glv.whichToDisplay--;
		}
		
		if (keyCode == RIGHT) {
			Glv.whichToDisplay++;
		}
		Glv.whichToDisplay = constrain(Glv.whichToDisplay, 0, threads.size() - 1);

		if (key == 's') {
			Glv.shouldSpaceSyntax = !Glv.shouldSpaceSyntax;
			thread("startThread");
		}
	}

	public void analysisSetup() {
		for (MyThread thread : threads) {
			thread.start();
		}
	}

	private void writeToFile() {
		if (Glv.isDone < threads.size()) {
			Glv.isDone = 0;
			for (MyThread thread : threads) {
				//println(thread.t.isAlive());
				if (!thread.t.isAlive()) {
					Glv.isDone++;
				}
			}
		}

		if (Glv.isDone == threads.size() && !Glv.isDoneBool) {
			GenerateCSV.save();
			Glv.isDoneBool = true;
		}

	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { firsteclipse.FirstEclipse.class.getName() });
	}
}
