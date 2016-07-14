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
	NeuralNetworkManagment net = new NeuralNetworkManagment(this);
	//	ManageBoxes manBox = new ManageBoxes(this);
	//	SpaceSyntax spaceSyntax = new SpaceSyntax(this);

	//private MyBox[][] boxes;

	public void settings() {
		size(2400, 1200, P3D);
	}

	/*
	 * SETUPS
	 */
	public void setup() {

		env.setupGui();

		randomSeed(Glv.seed);
		colorMode(PConstants.HSB, 360);

		s = loadShape("solid.OBJ"); // Load the 3D model of the public space.

		noStroke();
		rectMode(PConstants.CENTER);

		int ellapsedTime = second() + minute() * 60 + hour() * 360;
		if(Glv.programMode==1) net.loadGenData(); // Loads the generated data.
		if(Glv.shP) println("< Loading existing data. Ellapsed time: " + ((second() + minute() * 60 + hour() * 360) - ellapsedTime) + " >");
		
		env.loadData(); // 03. Loads the CSV file for the surrounding buildings.
		env.checkFilesUpdateSeed(); // Checks how many analysis have been done already.

		
		analysisSetup();
	}

	public void analysisSetup() {
		Glv.threads = new ArrayList<MyThread>();
		for (int i = 0; i < Glv.numOfThreads; i++) {
			Glv.threads.add(new MyThread(this, Glv.howManyUntilNow + i));
		}

		for (MyThread thread : Glv.threads) {
			if (!thread.VGADone) // If it is a new thread then start it!
				thread.start();
		}
	}

	/*
	 * DRAW FUNCTIONS
	 */
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

			if (Glv.threads.get(Glv.whichToDisplay).manBox.boxes[0][0] != null) {
				Glv.threads.get(Glv.whichToDisplay).manBox.draw();
			}
			if (Glv.shouldSpaceSyntax && Glv.threads.get(Glv.whichToDisplay).spaceSyntax.highLow != null)
				Glv.threads.get(Glv.whichToDisplay).spaceSyntax.draw(); // Draws the first SpaceSyntax analysis 

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
			//boxes = new MyBox[Glv.divisionX][Glv.divisionY];
		}
	}

	/*
	 * INTERACTION
	 */
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

		if (key == ' ')
		{
			int ellapsedTime = second() + minute() * 60 + hour() * 360;
			for (int i = 0; i < 1; i++) {
				net.trainNN();
			}
			if(Glv.shP) println("< Training NN. Ellapsed time: " + ((second() + minute() * 60 + hour() * 360) - ellapsedTime) + " >");
		}
			
		if (keyCode == ENTER)
			net.testNN();

		Glv.whichToDisplay = constrain(Glv.whichToDisplay, 0, Glv.threads.size() - 1);
		Glv.programMode = constrain(Glv.programMode, 0, 2);
	}

	/*
	 * OUTPUT
	 */
	private void writeToFile() {
		Glv.isDone = 0;
		if (Glv.isDone < Glv.threads.size()) {
			for (MyThread thread : Glv.threads) {
				//println(thread.t.isAlive());
				if (!thread.t.isAlive()) {
					Glv.isDone++;
				}
			}
		}

		if (Glv.isDone == Glv.threads.size() && !Glv.isDoneBool) {
			Glv.howManyUntilNow += Glv.numOfThreads;
			GenerateCSV.save(Integer.toString(Glv.howManyUntilNow + Glv.initialSeed));
			
			if (Glv.howManyUntilNow < Glv.numOfSolutions - 1) {
				analysisSetup();
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
