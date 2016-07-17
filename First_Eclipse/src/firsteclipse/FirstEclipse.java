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
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.glass.ui.TouchInputSupport;
import com.sun.jmx.snmp.tasks.ThreadService;

import javafx.scene.shape.Box;
import peasy.*;
import controlP5.*;

public class FirstEclipse extends PApplet {

	Environment env = new Environment(this);
	//DataAnalysis dataAnalysis = new DataAnalysis(this);

	public void settings() {
		size(2400, 1200, P3D);
	}

	/*
	 * SETUPS
	 */
	public void setup() {

		env.setupGui(Glv.shouldDimReduction); // Sets up the user interface
		env.loadData(); // 03. Loads the CSV file for the surrounding buildings.
		env.checkFilesUpdateSeed(); // Checks how many analysis have been done already.

		randomSeed(Glv.seed);
		colorMode(PConstants.HSB, 360);

		//noStroke();
		rectMode(PConstants.CENTER);

		if (Glv.programMode == 1)
			loadDataSetup();
		//loadDataSetup();
		//analysisSetup();
		//dataAnalysis.setup();
	}

	public void loadDataSetup() {
		if (Glv.threadNN == null) {
			Glv.threadNN = new MyThreadNeuralNet(this, 999);
			Glv.threadNN.start();
			env.cp5.remove("setupNeuralNetwork");
			env.cp5.addBang("setupNeuralNetwork").setPosition(230, 20).setSize(100, 100).moveTo(env.g1)
					.plugTo(Glv.threadNN.net);
		} else {
			println("Data already loaded");
		}
	}

	public void analysisSetup() {
		Glv.threads = new ArrayList<MyThread>();
		env.checkFilesUpdateSeed(); // Checks how many analysis have been done already.

		for (int i = 0; i < Glv.numOfThreads; i++) {
			Glv.threads.add(new MyThread(this, Glv.howManyUntilNow + i));
		}

		for (MyThread thread : Glv.threads) {
			if (!thread.VGADone) // If it is a new thread then start it!
				thread.start();
		}
	}

	public void stopAnalysis() {
		Glv.threadSuspended = !Glv.threadSuspended;

		if (!Glv.threadSuspended) {
			for (MyThread thread : Glv.threads) {
				thread.notify();
			}
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
			drawAnalysis();
			break;

		case 3:
			drawFormLoaded();
			break;
		}

		//println(Glv.shouldDimReduction);
		env.drawGui();
		writeToFile(); // Checks if threads have terminated or not, and write to CSV if yes.
	}

	public void drawGenerating() {
		pushMatrix();
		{
			rotate(HALF_PI); // Rotate the whole scene.

			if (Glv.threads != null) {
				if (Glv.threads.size() > 0 && Glv.threads.get(Glv.whichToDisplay) != null) {
					if (Glv.threads.get(Glv.whichToDisplay).manBox.boxes[0][0] != null
							&& Glv.threads.get(Glv.whichToDisplay).manBox.boxes[0][0].height != null
							&& Glv.threads.get(Glv.whichToDisplay).manBox != null) {
						Glv.threads.get(Glv.whichToDisplay).manBox.draw();
					}
					if (Glv.shouldSpaceSyntax && Glv.threads.get(Glv.whichToDisplay).spaceSyntax.highLow != null)
						Glv.threads.get(Glv.whichToDisplay).spaceSyntax.draw(); // Draws the first SpaceSyntax analysis 
				}
				env.draw(); // Draws the environment.
			}
		}
		popMatrix();
	}

	public void drawTeaching() {
		if (Glv.threadNN != null) {
			if (Glv.threadNN.net.dataLoaded && Glv.threadNN.net.neuralnet != null) {
				env.cam.beginHUD();
				{
					noLights();
					Glv.threadNN.net.neuralnet.draw();
				}
				env.cam.endHUD();
			}
		}
	}

	public void drawFormLoaded() {

	}

	public void drawAnalysis() {
		//dataAnalysis.draw();
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

		if (key == 'l')
			loadDataSetup();

		if (key == 'q')
			stopAnalysis();

		if (key == ' ') {
			if (Glv.threadNN != null) {
				if (Glv.threadNN.net.dataLoaded) {
					int ellapsedTime = second() + minute() * 60 + hour() * 360;
					Glv.threadNN.net.trainNN();

					if (Glv.shP)
						println("< Training NN. Ellapsed time: "
								+ ((second() + minute() * 60 + hour() * 360) - ellapsedTime) + " >");
				} else
					println("Load Cards first.");
			} else
				println("Load Cards first.");
		}

		if (keyCode == ENTER) {
			if (Glv.threadNN != null) {
				if (Glv.threadNN.net.dataLoaded) {
					Glv.threadNN.net.testNN();
				} else
					println("Load Cards first.");
			} else
				println("Load Cards first.");
		}

		Glv.whichToDisplay = constrain(Glv.whichToDisplay, 0, Glv.threads.size() - 1);
		Glv.programMode = constrain(Glv.programMode, 0, 3);
	}

	/*
	 * OUTPUT
	 */
	private void writeToFile() {
		Glv.isDone = 0;
		if (Glv.threads.size() > 0) {
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
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { firsteclipse.FirstEclipse.class.getName() });
	}
}
