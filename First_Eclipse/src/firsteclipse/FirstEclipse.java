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
import com.sun.corba.se.impl.orb.ParserTable.TestAcceptor1;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.glass.ui.TouchInputSupport;
import com.sun.jmx.snmp.tasks.ThreadService;

import javafx.scene.shape.Box;
import peasy.*;
import controlP5.*;

public class FirstEclipse extends PApplet {

	Environment env = new Environment(this);
	public DataAnalysis graphs = new DataAnalysis(this);
	//DataAnalysis dataAnalysis = new DataAnalysis(this);

	public void settings() {
		size(2400, 1200, P3D);
	}

	/*
	 * SETUPS
	 */
	public void setup() {

		env.setupGui(); // Sets up the user interface
		env.loadData(); // 03. Loads the CSV file for the surrounding buildings.
		env.checkFilesUpdateSeed(); // Checks how many analysis have been done already.

		randomSeed(Glv.seed);
		colorMode(PConstants.HSB, 360);

		//noStroke();
		rectMode(PConstants.CENTER);

		graphs.setup();

		if (Glv.programMode == 1)
			loadDataSetup();
		//loadDataSetup();
		//analysisSetup();
		//dataAnalysis.setup();
	}

	/*
	 * DRAW FUNCTIONS
	 */
	public void draw() {
		background(110);
		lights();

		switch (Glv.programMode) {
		case 0:
			env.cam.setActive(true); // Enable rotation of camera
			drawGenerating();
			break;

		case 1:
			env.cam.setActive(false); // Disable rotation of camera
			drawTeaching();
			break;

		case 2:
			env.cam.setActive(false); // Disable rotation of camera
			drawAnalysis();
			break;

		case 3:
			env.cam.setActive(false); // Disable rotation of camera
			drawEditor();
			break;
		}

		//println(Glv.shouldDimReduction);
		env.drawGui();
		writeToFile(); // Checks if threads have terminated or not, and write to CSV if yes.
	}

	public void drawGenerating() { // ProgramMode == 0
		pushMatrix();
		{
			rotate(HALF_PI); // Rotate the whole scene.

			if (Glv.threads != null) {
				if (Glv.threads.size() > 0) {
					if (Glv.threads.get(Glv.whichToDisplay) != null) {
						if (Glv.threads.get(Glv.whichToDisplay).manBox.boxes[0][0] != null
								&& Glv.threads.get(Glv.whichToDisplay).manBox.boxes[0][0].height != null
								&& Glv.threads.get(Glv.whichToDisplay).manBox != null) {
							Glv.threads.get(Glv.whichToDisplay).manBox.draw();
						}
						if (Glv.shouldSpaceSyntax && Glv.threads.get(Glv.whichToDisplay).spaceSyntax.highLow != null)
							Glv.threads.get(Glv.whichToDisplay).spaceSyntax.draw(); // Draws the first SpaceSyntax analysis 
					}
				}
				env.draw(); // Draws the environment.
			}
		}
		popMatrix();
	}

	public void drawTeaching() { // ProgramMode == 1

		env.cam.beginHUD();
		{
			noLights();
			if (Glv.threadNN != null) {
				if (Glv.threadNN.net.dataLoaded && Glv.threadNN.net.neuralnet != null) {

					Glv.threadNN.net.neuralnet.draw();
				} else {
					textAlign(CENTER);
					text("Please first set Neural Network up", width * 0.5f, height * 0.5f);
				}
			}
			env.cam.endHUD();
		}
	}

	public void drawAnalysis() { // ProgramMode == 2
		env.cam.beginHUD();
		{
			noLights();

			if (graphs.lineChart.getXData().length > 0) {
				graphs.draw();
			}

			else {
				textAlign(CENTER);
				text("Please first set Neural Network up", width * 0.5f, height * 0.5f);
			}
			env.cam.endHUD();
		}

		env.cam.beginHUD();
		{
			noLights();

		}
		env.cam.endHUD();
	}

	public void drawEditor() { // ProgramMode == 3
		if (Glv.threadNN != null) {
			if (Glv.threadNN.net.dataLoaded) {
				if (env.editorLayer != null) {
					env.cam.beginHUD();
					{
						//translate(width/4,);
						noLights();
						env.drawEditor();
					}
					env.cam.endHUD();
				}
			}
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

		if (key == 'l')
			loadDataSetup();

		if (key == 'q')
			stopAnalysis();

		if (key == ' ') {
			if (Glv.threadNN != null) {
				if (Glv.threadNN.net.dataLoaded) {
					int ellapsedTime = second() + minute() * 60 + hour() * 360;
					Glv.threadNN.net.trainNN(graphs);

					if (Glv.shP)
						println("< Training NN. Ellapsed time: "
								+ ((second() + minute() * 60 + hour() * 360) - ellapsedTime) + " >");
				} else
					println("Load Cards first.");
			} else
				println("Load Cards first.");
		}

		if (keyCode == ENTER) {
			if (Glv.programMode == 1) {
				if (Glv.threadNN != null) {
					if (Glv.threadNN.net.dataLoaded) {
						Glv.threadNN.net.testNN();
					} else
						println("Load Cards first.");
				} else
					println("Load Cards first.");
			} else if (Glv.programMode == 3) {
				//if (!Glv.neuronsStored) {
				println("I'm going to store the neurons now.");
				if (Glv.threadNN.net.setInputNeurons(env) > 3) // Creates the network according to the selected neurons.
				{
					Glv.neuronsStored = true;
				} else
					println("Not enough input neurons specified.");
				//}
			}
		}

		Glv.whichToDisplay = constrain(Glv.whichToDisplay, 0, Glv.threads.size() - 1);
		Glv.programMode = constrain(Glv.programMode, 0, 3);
	}

	public void mousePressed() {
		if (Glv.programMode == 3) {
			env.reactEditor();
		}
	}

	public void mouseDragged() {
		if (Glv.programMode == 3) {
			env.reactEditor();
		}
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

	/*
	 * FOR CONTROLLER
	 */

	public void loadDataSetup() {
		if (Glv.threadNN == null) {
			Glv.threadNN = new MyThreadNeuralNet(this, 999);
			Glv.threadNN.start();
			env.cp5.remove("setupNeuralNetwork");
			env.cp5.addBang("setupNeuralNetwork").setPosition(340, 20).setSize(100, 100)
					.setImages(loadImage("networkRoll.PNG"), loadImage("network.PNG"), loadImage("networkPress.PNG"))
					.updateSize().moveTo(env.g1).plugTo(Glv.threadNN.net);
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

	public void numberOfThreads(int theValue) {
		Glv.numOfThreads = theValue;
	}

	public void dimensionalityReduction(boolean theValue) {
		Glv.shouldDimReduction = theValue;
		println(Glv.shouldDimReduction);
	}

	public void numOfLearning(int theValue) {
		//println("numOfLearning: " + Glv.numOfLearning);
		Glv.numOfLearning = theValue;
	}

	public void learningRate(float theValue) {
		Glv.LEARNING_RATE = theValue;
	}

	public void hiddenLayerSize(float theValue) {
		Glv.howMuchBiggerHidden = theValue;
	}

	public void genOrA(int theValue) {

	}

	public void startEditor() {
		println("I have entered this");
		if (Glv.threadNN != null)
			if (Glv.threadNN.net.dataLoaded)
				env.myEditor();
	}

	public void programMode(int theValue) {
		println("I'm in programmode");
		Glv.programMode = theValue;
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { firsteclipse.FirstEclipse.class.getName() });
	}
}

//if (Glv.whichInputs == null) { // If the object does't exist initialise it.
//	Glv.whichInputs = new ArrayList<ArrayList<Integer>>();
//	System.out.println("Glv.whichInputs was null.");
//
//	Glv.whichInputs.add(new ArrayList<Integer>());
//	Glv.whichInputs.get(i).add(1);
//} else {
//	if (Glv.whichInputs.size() > 0) {
//		System.out.println("I'm going to add a point.");
//		//System.out.println("Glv.whichInputs.get(i): " + Glv.whichInputs.get(i));
//		if (Glv.whichInputs.size() < i) {
//			Glv.whichInputs.add(new ArrayList<Integer>());
//			Glv.whichInputs.get(i).add(1);
//		} else if (Glv.whichInputs.size() > i) { // If the element j is null then add a one.
//			if (Glv.whichInputs.get(i).size() < j) {
//				Glv.whichInputs.get(i).add(1);
//			}
//		} else {
//			Glv.whichInputs.get(i).remove(j); // If it is clicked again but is already in the list then remove it.
//			if (Glv.whichInputs.size() == 0)
//				Glv.whichInputs = null;
//			env.editorLayer[i][j].colour = 0;
//			System.out.println("I'm going to remove element");
//		}
//	} else {
//		env.editorLayer[i][j].colour = 360;
//	}
//}
