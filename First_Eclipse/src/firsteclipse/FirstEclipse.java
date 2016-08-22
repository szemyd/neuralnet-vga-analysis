package firsteclipse;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;
import sun.security.krb5.internal.EncAPRepPart;

import java.io.File;
import java.util.ArrayList;

import org.gicentre.utils.stat.XYChart;
import org.omg.PortableInterceptor.ACTIVE;

import com.jogamp.opengl.GLStateKeeper.Listener;
import com.sun.corba.se.impl.orb.ParserTable.TestAcceptor1;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.glass.ui.TouchInputSupport;
import com.sun.jmx.snmp.tasks.ThreadService;

import javafx.scene.chart.LineChart;
import javafx.scene.shape.Box;
import peasy.*;
import controlP5.*;

public class FirstEclipse extends PApplet {

	Environment env = new Environment(this);
	public DataAnalysis graphs = new DataAnalysis(this);
	PFont titleFont, smallFont;
	//DataAnalysis dataAnalysis = new DataAnalysis(this);

	public void settings() {
		size(2800, 1400, P3D);
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

		titleFont = createFont("Museo300-Regular.otf", 22);
		smallFont = createFont("Museo300-Regular.otf", 12);
		textFont(smallFont);
		//noStroke();
		rectMode(PConstants.CENTER);

		graphs.setup();

		if (Glv.programMode == 1)
			loadDataSetup();
		//loadDataSetup();
		//analysisSetup();
		//dataAnalysis.setup();

		println("cores: " + Runtime.getRuntime().availableProcessors());
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
			draw3D();
			break;

		case 1:
			env.cam.setActive(false); // Disable rotation of camera
			switch (Glv.genOrA) {
			case 0:
				drawNeuralNetworkGenerating();
				break;
			case 1:
				drawNeuralNetworkGeneratingRaw();
				break;
			case 2:
				drawNeuralNetworkAnalysing();
				break;
			}
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

	public void draw3D() { // ProgramMode == 0
		pushMatrix();
		{
			rotate(HALF_PI); // Rotate the whole scene.

			if (Glv.newOrNet) {

				if (Glv.threads != null) {
					if (Glv.threads.size() > 0) {
						if (Glv.whichToDisplay >= 0) {
							if (Glv.threads.get(Glv.whichToDisplay) != null) {
								if (Glv.threads.get(Glv.whichToDisplay).manBox.boxes[0][0] != null
										&& Glv.threads.get(Glv.whichToDisplay).manBox.boxes[0][0].height != null
										&& Glv.threads.get(Glv.whichToDisplay).manBox != null) {
									Glv.threads.get(Glv.whichToDisplay).manBox.draw();
								}
								if (Glv.shouldSpaceSyntax
										&& Glv.threads.get(Glv.whichToDisplay).spaceSyntax.highLow != null)
									Glv.threads.get(Glv.whichToDisplay).spaceSyntax.draw(); // Draws the first SpaceSyntax analysis 
							}
						}
					}
				}

			} else if (Glv.genOrA == 0) {
				if (Glv.threadNN != null) {
					if (Glv.threadNN.net != null) {
						if (Glv.threadNN.net.thread != null) {
							if (Glv.threadNN.net.thread.manBox.boxes[0][0] != null
									&& Glv.threadNN.net.thread.manBox.boxes[0][0].height != null
									&& Glv.threadNN.net.thread.manBox != null) {
								Glv.threadNN.net.thread.manBox.draw();
							}
							if (Glv.shouldSpaceSyntax && Glv.threadNN.net.thread.spaceSyntax.highLow != null)
								Glv.threadNN.net.thread.spaceSyntax.draw(); // Draws the first SpaceSyntax analysis 
						}
					}
				}

			}

			env.draw(); // Draws the environment.

			if (Glv.genOrA == 2) {
				if (env.editorBoxes != null) {
					if (env.editorBoxes.boxes != null) {
						env.drawEditorBoxes();
					}
				}

				if (Glv.editorForAnalysisOn && !Glv.newOrNet) {
					if (env.spaceSyntax != null) {
						if (env.spaceSyntax.rectangles != null) {
							env.spaceSyntax.drawResponded();
							env.setSpaceSyntaxValues(); // Constantly update the values: make neuron network react.
						}
					}

					if (env.editorBoxes != null) {
						env.drawThreeDEditor();
					}
				}
			}
		}
		popMatrix();
	}

	public void drawNeuralNetworkGenerating() { // ProgramMode == 1 | 0

		env.cam.beginHUD();
		{
			noLights();
			if (Glv.threadNN != null) {
				if (Glv.threadNN.net.dataLoaded && Glv.threadNN.net.neuralnet != null) {
					Glv.threadNN.net.neuralnet.draw(env);

					if (Glv.threadNN.net.thread != null) {
						if (Glv.threadNN.net.thread.spaceSyntax.values != null) {
							graphs.drawNeuron(env); // Draw the comparing neurons.
						}
					}
				}

			} else {
				textAlign(CENTER);
				text("Please first set Neural Network up", width * 0.5f, height * 0.5f);
			}
			env.cam.endHUD();
		}
	}

	public void drawNeuralNetworkGeneratingRaw() // ProgramMode == 1 | 1
	{
		env.cam.beginHUD();
		{
			noLights();
			if (Glv.threadNN != null) {
				if (Glv.threadNN.net.dataLoaded && Glv.threadNN.net.neuralnet != null) {
					Glv.threadNN.net.neuralnet.draw(env);

					if (Glv.threadNN.net.thread != null) {
						if (Glv.threadNN.net.thread.spaceSyntax.values != null) {
							graphs.drawNeuron(env); // Draw the comparing neurons.
						}
					}
				}

			} else {
				textAlign(CENTER);
				text("Please first set Neural Network up", width * 0.5f, height * 0.5f);
			}
			env.cam.endHUD();
		}
	}

	public void drawNeuralNetworkAnalysing() { // ProgramMode == 1 | 2

		env.cam.beginHUD();
		{
			noLights();
			if (Glv.threadNN != null) {
				if (!Glv.splitNetwork) {
					if (Glv.threadNN.net.dataLoaded && Glv.threadNN.net.neuralnet != null) {
						Glv.threadNN.net.neuralnet.drawAnalysis(env);
					}
				} else {
					if (Glv.threadNN.net.dataLoaded && Glv.threadNN.net.splitNeuralnets != null) {
						Glv.threadNN.net.drawSplitNetworks(env);
					}
				}

			} else {
				textAlign(CENTER);
				text("Please first set Neural Network up", width * 0.5f, height * 0.5f);
			}
		}
		env.cam.endHUD();
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
		pushMatrix();
		{

			if (Glv.threadNN != null) {
				if (Glv.threadNN.net.dataLoaded) {
					if (env.editorLayer != null) {
						env.cam.beginHUD();
						{
							//rotate(HALF_PI); // Rotate the whole scene.
							//							translate(-((env.editorLayer[0][0].position.x)+0.5f*(env.editorLayer[0][0].position.x + env.editorLayer[env.editorLayer.length-1][env.editorLayer[0].length-1].position.x)),-((env.editorLayer[0][0].position.y)+0.5f*(env.editorLayer[0][0].position.y + env.editorLayer[env.editorLayer.length-1][env.editorLayer[0].length-1].position.y)));
							//							rotate(HALF_PI); // Rotate the whole scene.
							//							translate(((env.editorLayer[0][0].position.x)+0.5f*(env.editorLayer[0][0].position.x + env.editorLayer[env.editorLayer.length-1][env.editorLayer[0].length-1].position.x)),((env.editorLayer[0][0].position.y)+0.5f*(env.editorLayer[0][0].position.y + env.editorLayer[env.editorLayer.length-1][env.editorLayer[0].length-1].position.y)));
							//							
							//							translate(-env.editorLayer[0][0].position.x, -env.editorLayer[0][0].position.y);
							//							rotate(HALF_PI); // Rotate the whole scene.
							//							translate(env.editorLayer[0][0].position.x, env.editorLayer[0][0].position.y);			
							//translate(width/4,);
							noLights();
							env.drawEditor();
						}
						env.cam.endHUD();
					}
				}
			}
		}
		popMatrix();
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
			Glv.VGAormeanShort = !Glv.VGAormeanShort;

		if (key == ' ') {
			trainNeurons();
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

		if (Glv.threads.size() > 0) {
			Glv.whichToDisplay = constrain(Glv.whichToDisplay, 0, Glv.threads.size() - 1);
		} else
			Glv.whichToDisplay = 0;

		Glv.programMode = constrain(Glv.programMode, 0, 3);
		env.modeSwitch.activate(Glv.programMode);
	}

	public void mousePressed() {
		if (Glv.programMode == 3) {
			env.reactEditor();
		}

		if (env.editorRect != null) {
			env.reactEditor2();
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

				String fileName = Integer.toString(Glv.howManyUntilNow + Glv.initialSeed);
				String myDirectory = "GeneratedData";
				GenerateCSV.createDir(myDirectory);
				GenerateCSV.generateCsvFile(
						new File("").getAbsolutePath() + "\\" + myDirectory + "\\" + fileName + ".csv");

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

	public void trainNeurons() {
		if (Glv.threadNN != null) {
			if (Glv.threadNN.net.dataLoaded) {
				if (Glv.threadNN.net.neuralnet != null || Glv.threadNN.net.splitNeuralnets != null) {
					float ellapsedTime = second() + minute() * 60 + hour() * 360;

					Glv.threadNN.net.trainNN(graphs);
					Glv.timeToCalc.add(ellapsedTime / Glv.numOfCycles);

					println("< Training NN. Ellapsed time: "
							+ ((second() + minute() * 60 + hour() * 360) - ellapsedTime) + " >");
				} else
					println("Setup NN first.");
			} else
				println("Load Cards first.");
		} else
			println("Load Cards first.");
	}

	public void loadDataSetup() {
		if (Glv.threadNN == null) {
			Glv.threadNN = new MyThreadNeuralNet(this, 999);
			Glv.threadNN.start();
			//			env.cp5.remove("setupNeuralNetwork");
			//			env.cp5.addBang("setupNeuralNetwork").setPosition(340, 20).setSize(100, 100)
			//					.setImages(loadImage("networkRoll.PNG"), loadImage("network.PNG"), loadImage("networkPress.PNG"))
			//					.updateSize().moveTo(env.g1).plugTo(Glv.threadNN.net);
		} else if (!Glv.threadNN.net.dataLoaded) {
			//Glv.threadNN.t.start();
		} else
			println("Data already loaded");
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

	public void setupNeuralNetwork() {
		if (Glv.threadNN != null) {
			if (Glv.threadNN.net != null) {
				Glv.threadNN.net.setupNeuralNetwork(env);
				graphs.setup();

				Glv.bestE = 100f;
				Glv.bestMSE = 100f;
				Glv.bestVGAMSE = 100f;
				Glv.bestVGAE = 100f;
				Glv.timeToCalc = new ArrayList<Float>();
			}
		}
	}

	public void numberOfThreads(int theValue) {
		Glv.numOfThreads = theValue;
	}

	public void numberOfSolutions(int theValue) {
		Glv.numOfSolutions = theValue;
	}

	public void numberOfRead(int theValue) {
		Glv.numOfRead = theValue;
	}

	public void dimensionalityReduction(boolean theValue) {
		Glv.shouldDimReduction = theValue;
		println(Glv.shouldDimReduction);
	}

	//---> For controlling the Neural Network
	public void numOfLearning(int theValue) {
		//println("numOfLearning: " + Glv.numOfLearning);
		Glv.numOfLearning = theValue;
	}

	public void learningRate(float theValue) {
		Glv.LEARNING_RATE = theValue;
		println("Learning rate: " + Glv.LEARNING_RATE);
	}

	public void hiddenLayerSize(float theValue) {
		Glv.howMuchBiggerHidden = theValue;
	}

	public void numOfCycles(int theValue) {
		Glv.numOfCycles = theValue;
	}

	public void splitSize(int theValue) {
		Glv.splitSize = theValue;
	}
	//<---

	public void genOrA(int theValue) {
		Glv.genOrA = theValue;
		setupNeuralNetwork();
	}

	public void programMode(int theValue) {
		println("I'm in programmode");
		Glv.programMode = theValue;
	}

	public void newOrNet(int theValue) {
		if (theValue == 0)
			Glv.newOrNet = true;
		else
			Glv.newOrNet = false;
	}

	//---> For controlling the editor
	public void startEditor() {
		println("I have entered this");
		if (Glv.threadNN != null)
			if (Glv.threadNN.net.dataLoaded)
				env.myEditor();

		env.setupThreeDEditor();
	}

	public void resetEditor() {
		//println("I am definitely here.");
		if (env.editorLayer != null) {
			for (int i = 0; i < env.editorLayer.length; i++) {
				for (int j = 0; j < env.editorLayer[i].length; j++) {
					env.editorLayer[i][j].iAmChosen = false;
					env.editorLayer[i][j].colour = 0;
				}
			}
		}
	}

	public void selectAll() {
		if (env.editorLayer != null) {
			for (int i = 0; i < env.editorLayer.length; i++) {
				for (int j = 0; j < env.editorLayer[i].length; j++) {
					if (i > 2 && i < env.editorLayer.length - 6 && j > 5 && j < env.editorLayer[i].length - 3) { // Leave the side area out.
						env.editorLayer[i][j].iAmChosen = true;
						env.editorLayer[i][j].colour = 360;
					}
				}
			}
		}
	}

	public void everySecond() {
		if (env.editorLayer != null) {
			for (int i = 0; i < env.editorLayer.length; i++) {
				for (int j = 0; j < env.editorLayer[i].length; j++) {
					if (i > 2 && i < env.editorLayer.length - 5 && j > 5 && j < env.editorLayer[i].length - 3) { // Leave the side area out.
						if (i % 2 == 0 && j % 2 == 0) {
							env.editorLayer[i][j].iAmChosen = true;
							env.editorLayer[i][j].colour = 360;
						} else {
							env.editorLayer[i][j].iAmChosen = false;
							env.editorLayer[i][j].colour = 0;
						}
					} else {
						env.editorLayer[i][j].iAmChosen = false;
						env.editorLayer[i][j].colour = 0;
					}
				}
			}
		}
	}

	public void everyThird() {
		if (env.editorLayer != null) {
			for (int i = 0; i < env.editorLayer.length; i++) {
				for (int j = 0; j < env.editorLayer[i].length; j++) {
					if (i > 2 && i < env.editorLayer.length - 5 && j > 5 && j < env.editorLayer[i].length - 3) { // Leave the side area out.
						if (i % 3 == 0 && j % 3 == 0) {
							env.editorLayer[i][j].iAmChosen = true;
							env.editorLayer[i][j].colour = 360;
						} else {
							env.editorLayer[i][j].iAmChosen = false;
							env.editorLayer[i][j].colour = 0;
						}
					} else {
						env.editorLayer[i][j].iAmChosen = false;
						env.editorLayer[i][j].colour = 0;
					}
				}
			}
		}
	}

	public void everyFourth() {
		if (env.editorLayer != null) {
			for (int i = 0; i < env.editorLayer.length; i++) {
				for (int j = 0; j < env.editorLayer[i].length; j++) {
					if (i > 2 && i < env.editorLayer.length - 5 && j > 5 && j < env.editorLayer[i].length - 3) { // Leave the side area out.
						if (i % 4 == 0 && j % 4 == 0) {
							env.editorLayer[i][j].iAmChosen = true;
							env.editorLayer[i][j].colour = 360;
						} else {
							env.editorLayer[i][j].iAmChosen = false;
							env.editorLayer[i][j].colour = 0;
						}
					} else {
						env.editorLayer[i][j].iAmChosen = false;
						env.editorLayer[i][j].colour = 0;
					}
				}
			}
		}
	}

	public void editorForAnalysisOn(boolean theValue) {
		Glv.editorForAnalysisOn = theValue;
	}

	public void splitNetwork(boolean theValue) {
		Glv.splitNetwork = theValue;
		//println(Glv.splitNetwork );
	}
	//<---

	//---> For Data analysis

	public void saveData() {

		if (env.editorLayer != null) {
			if (Glv.threadNN != null) {
				if (Glv.threadNN.net != null) {
					if (Glv.threadNN.net.neuralnet != null) {

						int counter = 0;
						float avarageDistance = 0.0f;

						for (int i = 0; i < env.editorLayer.length; i++) {
							for (int j = 0; j < env.editorLayer[i].length; j++) {
								if (env.editorLayer[i][j].iAmChosen) {
									counter++;
									avarageDistance += PVector
											.dist(env.editorLayer[i][j].position,
													PVector.div(
															PVector.add(env.editorLayer[0][0].position,
																	env.editorLayer[env.editorLayer.length
																			- 1][env.editorLayer[0].length
																					- 1].position),
															2));
								}
							}
						}
						avarageDistance /= counter;

						String counterS = Integer.toString(counter);
						String avarageDistanceS = Float.toString(avarageDistance);

						String fileName = Integer.toString(Glv.genOrA) + "_" + counterS + "_" + avarageDistanceS;
						String myDirectory = "AnalysisData";
						String myDirectorySecond;

						if (Glv.genOrA == 0)
							myDirectorySecond = "Generating";
						else
							myDirectorySecond = "Analysing";

						GenerateCSV.createDir(myDirectory); // Creats Directory
						GenerateCSV.createDir(myDirectory + "\\" + myDirectorySecond);
						String combination = myDirectory + "\\" + myDirectorySecond + "\\" + fileName;
						GenerateCSV.createDir(combination);

						GenerateCSV.saveValuesToCSV(
								new File("").getAbsolutePath() + "\\" + combination + "\\" + fileName,
								counterS, avarageDistanceS, env, this);
					}
				}
			}
		}
	}

	public void compareValues() {

		if (Glv.threadNN.net.thread == null) {
			Glv.threadNN.net.backTo3D();
		}

		if (Glv.threadNN.net.thread != null) {
			if (graphs != null) {
				graphs.compare(env);
			}
		}
	}

	public void saveNN() {
		if (env.editorLayer != null) {
			if (Glv.threadNN != null) {
				if (Glv.threadNN.net != null) {
					if (Glv.threadNN.net.neuralnet != null) {

						int counter = 0;
						float avarageDistance = 0.0f;

						for (int i = 0; i < env.editorLayer.length; i++) {
							for (int j = 0; j < env.editorLayer[i].length; j++) {
								if (env.editorLayer[i][j].iAmChosen) {
									counter++;
									avarageDistance += PVector
											.dist(env.editorLayer[i][j].position,
													PVector.div(
															PVector.add(env.editorLayer[0][0].position,
																	env.editorLayer[env.editorLayer.length
																			- 1][env.editorLayer[0].length
																					- 1].position),
															2));
								}
							}
						}
						avarageDistance /= counter;

						String counterS = Integer.toString(counter);
						String avarageDistanceS = Float.toString(avarageDistance);

						String myDirectory = "NetworkSettings";
						GenerateCSV.createDir(myDirectory);
						GenerateCSV.createDir(myDirectory + "\\" + Integer.toString(Glv.genOrA) + "_" + counterS + "_"
								+ avarageDistanceS);

						GenerateCSV.saveNeuralNetwork(new File("").getAbsolutePath() + "\\" + myDirectory + "\\"
								+ Integer.toString(Glv.genOrA) + "_" + counterS + "_" + avarageDistanceS + "\\"
								+ "neuralNetwork" + ".csv");
					}
				}
			}
		}
	}

	public void loadNN() {

	}

	public void saveSettings() {
		if (env.editorLayer != null) {
			if (Glv.threadNN != null) {
				if (Glv.threadNN.net != null) {
					if (Glv.threadNN.net.neuralnet != null) {

						int counter = 0;
						float avarageDistance = 0.0f;

						for (int i = 0; i < env.editorLayer.length; i++) {
							for (int j = 0; j < env.editorLayer[i].length; j++) {
								if (env.editorLayer[i][j].iAmChosen) {
									counter++;
									avarageDistance += PVector
											.dist(env.editorLayer[i][j].position,
													PVector.div(
															PVector.add(env.editorLayer[0][0].position,
																	env.editorLayer[env.editorLayer.length
																			- 1][env.editorLayer[0].length
																					- 1].position),
															2));
								}
							}
						}
						avarageDistance /= counter;

						String counterS = Integer.toString(counter);
						String avarageDistanceS = Float.toString(avarageDistance);

						String myDirectory = "NetworkSettings";
						GenerateCSV.createDir(myDirectory);
						GenerateCSV.createDir(myDirectory + "\\" + Integer.toString(Glv.genOrA) + "_" + counterS + "_"
								+ avarageDistanceS);

						GenerateCSV.saveSettings(new File("").getAbsolutePath() + "\\" + myDirectory + "\\"
								+ Integer.toString(Glv.genOrA) + "_" + counterS + "_" + avarageDistanceS + "\\"
								+ "generalSettings" + ".csv", counterS, avarageDistanceS, env);
					}
				}
			}
		}
	}

	//<---

	//---> For navigating in 3D
	public void ninetyD() {
		env.cam.rotateZ(HALF_PI);
	}

	public void mNinetyD() {
		env.cam.rotateZ(-HALF_PI);

	}
	//<---

	public void closeIt() {
		if (env.g1.isOpen())
			env.g1.close();
		else
			env.g1.open();

		if (env.g2.isOpen())
			env.g2.close();
		else
			env.g2.open();

		if (env.g3.isOpen())
			env.g3.close();
		else
			env.g3.open();

		if (env.g4.isOpen())
			env.g4.close();
		else
			env.g4.open();

		if (env.g5.isOpen())
			env.g5.close();
		else
			env.g5.open();

		if (env.g6.isOpen())
			env.g6.close();
		else
			env.g6.open();

		if (env.g7.isOpen())
			env.g7.close();
		else
			env.g7.open();

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
