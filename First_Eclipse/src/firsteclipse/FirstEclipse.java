package firsteclipse;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

import java.io.File;
import java.util.ArrayList;

import org.omg.PortableInterceptor.ACTIVE;

import com.jogamp.opengl.GLStateKeeper.Listener;

import javafx.scene.shape.Box;
import peasy.*;
import controlP5.*;

public class FirstEclipse extends PApplet {

	PShape s;

	Environment env = new Environment(this);
	NeuralNetwork net = new NeuralNetwork(this);
	//	ManageBoxes manBox = new ManageBoxes(this);
	//	SpaceSyntax spaceSyntax = new SpaceSyntax(this);
	public ArrayList<MyThread> threads = new ArrayList<MyThread>();
	private MyBox[][] boxes;

	public void settings() {
		size(2400, 1200, P3D);
	}

	public void setup() {

		env.setupGui();

		randomSeed(Glv.seed);
		colorMode(PConstants.HSB, 360);

		s = loadShape("solid.OBJ"); // Load the 3D model of the public space.

		noStroke();
		rectMode(PConstants.CENTER);

		net.loadGenData(); // Loads the generated data.
		env.loadData(); // 03. Loads the CSV file for the surrounding buildings.
		env.checkFilesUpdateSeed(); // Checks how many analysis have been done already.

		analysisSetup();
	}

	public void draw() {
		background(110);
		lights();

		switch (Glv.programMode) {
		case 0:
			drawGenerating();
			break;

		case 1:
			drawTeaching();
			break;

		case 2:
			drawFormLoaded();
			break;
		}

		env.drawGui();
		writeToFile(); // Checks if threads have terminated or not, and write to CSV if yes.
	}

	public void drawGenerating() {
		pushMatrix();
		{
			rotate(HALF_PI); // Rotate the whole scene.

			if (threads.get(Glv.whichToDisplay).manBox.boxes[0][0] != null) {
				threads.get(Glv.whichToDisplay).manBox.draw();
			}
			if (Glv.shouldSpaceSyntax && threads.get(Glv.whichToDisplay).spaceSyntax.highLow != null)
				threads.get(Glv.whichToDisplay).spaceSyntax.draw(); // Draws the first SpaceSyntax analysis 

			env.draw(s); // Draws the environment.
		}
		popMatrix();
	}

	public void drawTeaching() {
		env.cam.beginHUD();
		{
			noLights();
			net.neuralnet.draw();
		}
		env.cam.endHUD();
	}

	public void drawFormLoaded() {
		if (net != null) {
			boxes = new MyBox[Glv.divisionX][Glv.divisionY];
		}
	}

	public void keyPressed() {
		if (keyCode == LEFT)
			Glv.whichToDisplay--;

		if (keyCode == RIGHT)
			Glv.whichToDisplay++;

		if (keyCode == UP)
			Glv.programMode++;

		if (keyCode == DOWN)
			Glv.programMode--;

		if (key == 's')
			Glv.shouldSpaceSyntax = !Glv.shouldSpaceSyntax;

		if (key == 'g')
			Glv.globalHighLow = !Glv.globalHighLow;

		Glv.whichToDisplay = constrain(Glv.whichToDisplay, 0, threads.size() - 1);
		Glv.programMode = constrain(Glv.programMode, 0, 2);
	}

	public void analysisSetup() {
		for (int i = 0; i < Glv.numOfThreads; i++) {
			threads.add(new MyThread(this, threads.size()));
		}

		for (MyThread thread : threads) {
			if (!thread.VGADone) // If it is a new thread then start it!
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
			GenerateCSV.save(Integer.toString(Glv.howManyUntilNow + Glv.initialSeed));

			if (Glv.howManyUntilNow < Glv.numOfSolutions - 1) {
				analysisSetup();
				Glv.howManyUntilNow += Glv.numOfThreads;
			} else {
				if (Glv.shP)
					println("All solutions done.");
				Glv.isDoneBool = true;
			}
		}
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { firsteclipse.FirstEclipse.class.getName() });
	}
}
