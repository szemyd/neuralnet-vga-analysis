package firsteclipse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.jogamp.opengl.util.packrect.Rect;
import com.sun.java_cup.internal.runtime.virtual_parse_stack;
import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import controlP5.Accordion;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Group;
import peasy.PeasyCam;
import peasy.*;
import controlP5.*;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PShape;
import processing.core.PVector;
import sun.net.www.content.text.plain;

public class Environment {

	private static PApplet p;
	public static ArrayList<Building> buildings = new ArrayList<Building>();

	ControlP5 cp5;
	Accordion accordion;

	PeasyCam cam;

	PShape s;

	public Neuron[][] editorLayer;;

	public Group g1, g2, g3;
	Bang b1, b2, b3, b4;
	RadioButton modeSwitch;

	//Textarea myTextarea;
	//Println console;

	public Environment(PApplet _p) {
		p = _p;
	}

	public void setupGui(boolean DimensionalityReduction) {

		cp5 = new ControlP5(p);
		cam = new PeasyCam(p, 180);
		// cam.setMinimumDistance(50);
		// cam.setMaximumDistance(500);
		cp5.enableShortcuts();

		cp5.begin(100, 20);

		PFont pfont = p.createFont("Arial", 20, true); // use true/false for smooth/no-smooth
		ControlFont font = new ControlFont(pfont, 241);

		//cp5.loadProperties(("controlP5.json"));

		g1 = cp5.addGroup("myGroup1").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(150);//BackgroundHeight(150);
		g2 = cp5.addGroup("myGroup2").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(150);
		g3 = cp5.addGroup("myGroup3").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(150);

		b1 = cp5.addBang("analysisSetup").setPosition(10, 20).setSize(100, 100).moveTo(g1).plugTo(this, "shuffle");
		b2 = cp5.addBang("loadDataSetup").setPosition(120, 20).setSize(100, 100).moveTo(g1).plugTo(this, "shuffle");
		b3 = cp5.addBang("startEditor").setPosition(230, 20).setSize(100, 100).moveTo(g1).plugTo(this, "shuffle");
		b4 = cp5.addBang("setupNeuralNetwork").setPosition(340, 20).setSize(100, 100).moveTo(g1).plugTo(this,
				"shuffle");
		//cp5.addBang("setupNeuralNetwork").setPosition(340, 20).setSize(100, 100).moveTo(g1).plugTo(this, "shuffle");

		modeSwitch = cp5.addRadioButton("radio").setPosition(10, 20).setItemWidth(20).setItemHeight(20)
				.setItemsPerRow(3).addItem("Generating", 0).addItem("Neural Network", 1).addItem("Analysis", 2)
				.setColorLabel(p.color(255)).activate(0).moveTo(g2);

		modeSwitch.plugTo(Glv.programMode);

		cp5.addSlider("numberOfThreads").setPosition(20, 20).setSize(20, 100).setRange(0, 20).setNumberOfTickMarks(5)
				.plugTo(p, "numOfThreads").moveTo(g3);

		cp5.addSlider("numberOfSolutions").setPosition(50, 20).setSize(20, 100).setRange(0, 5000)
				.setNumberOfTickMarks(11).plugTo(Glv.numOfSolutions).moveTo(g3);
		cp5.addToggle("dimensionalityReduction").setValue(true).setPosition(90, 20).setSize(100, 19).moveTo(g3)
				.plugTo(Glv.shouldDimReduction);

		//cp5.addButton("plug", 2);
		//		ButtonBar b = cp5.addButtonBar("bar").setPosition(0, 0).setSize(p.width, 40)
		//				.addItems(p.split("a b c d e f g h i j", " ")).setColorBackground(170);
		//		;

		//.plugTo(this,	"shuffle");

		//accordion.close(1);

		accordion = cp5.addAccordion("acc").setPosition(40, 40).setWidth(460).addItem(g1);
		accordion.open(0, 1, 2);
		accordion = cp5.addAccordion("what").setPosition(510, 40).setWidth(300).addItem(g2);

		accordion.open(0, 1, 2);
		accordion = cp5.addAccordion("yes").setPosition(820, 40).setWidth(300).addItem(g3);

		accordion.open(0, 1, 2);
		accordion.setCollapseMode(Accordion.MULTI);

		/*
				accordion = cp5.addAccordion("acc").setPosition(40, 40).setWidth(300).addItem(g1).addItem(g2).addItem(g3);
				accordion.open(0, 1, 2);
				accordion.setCollapseMode(Accordion.MULTI);
				*/

		/*	
		  
		 
			p.frameRate(50);
			myTextarea = cp5.addTextarea("txt").setPosition(100, 100).setSize(200, 200).setFont(p.createFont("", 10))
					.setLineHeight(14).setColor(p.color(200)).setColorBackground(p.color(0, 100))
					.setColorForeground(p.color(255, 100)).moveTo(g3);
			;
		
			console = cp5.addConsole(myTextarea);//
		*/

		cp5.setAutoDraw(false);
	}

	public void draw() {
		//		p.pushStyle();
		//		{
		//			p.strokeWeight(5.0f);
		//			p.stroke(360, 0, 360);
		//			for (Building building : buildings) {
		//				building.draw();
		//			}
		//		}
		//		p.popStyle();

		p.pushMatrix();
		{
			p.pushStyle();
			{
				p.rectMode(PConstants.CENTER);
				p.fill(360, 0, 280);

				p.translate(0, 0, -.005f);
				p.rect(0, 0, 520f, 560f);

				p.translate(0, 0, -.01f);

				s.setFill(p.color(p.random(255)));
				s.disableStyle();
				//	s.enableStyle();
				p.fill(360, 0, 360);

				p.shape(s, 0, 0);
				s.enableStyle();
				//s.disableStyle();

			}
			p.popStyle();
		}
		p.popMatrix();
	}

	public void drawGui() {
		//hint(DISABLE_DEPTH_TEST);
		cam.beginHUD();
		{
			p.noLights();
			cp5.draw();
		}
		cam.endHUD();
		//hint(ENABLE_DEPTH_TEST);
	}

	public void drawEditor() {
		for (int i = 0; i < editorLayer.length; i++) {
			for (int j = 0; j < editorLayer[i].length; j++) {
				editorLayer[i][j].draw();
			}
		}
	}

	public void loadData() {

		String filePath = new File("").getAbsolutePath();
		s = p.loadShape(filePath + "/src/data/solid.obj"); // Load the 3D model of the public space.

		String csvFile = filePath + "/src/data/sur.csv";

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				PVector temp = new PVector();
				// use comma as separator
				String[] thisRow = line.split(cvsSplitBy);

				temp.x = Float.parseFloat(thisRow[1]) / 100;
				temp.y = Float.parseFloat(thisRow[2]) / 100;
				temp.z = Float.parseFloat(thisRow[4]);

				// p.println(buildings.size() + " temp.z: " + temp.z);
				if (buildings.size() < (int) temp.z)
					buildings.add(new Building(p, temp));
				else {
					Building tempBuilding = buildings.get((int) temp.z - 1);
					tempBuilding.bLines.add(temp);
					tempBuilding.myPolygon.addPoint((int) temp.x, (int) temp.y);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (Glv.shP)
			p.println("CSV Data Loaded!");
	}

	public void checkFilesUpdateSeed() {
		String filePath = new File("").getAbsolutePath();
		File folder = new File(filePath + "\\" + "GeneratedData");

		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String fileName = listOfFiles[i].getName();
				if (fileName.contains(".")) {
					int cutFilename = Integer.valueOf(fileName.substring(0, fileName.lastIndexOf('.')));
					//if(Glv.shP) System.out.println("File " + cutFilename);

					if (cutFilename > Glv.initialSeed) {
						Glv.initialSeed = cutFilename;
						Glv.seed = cutFilename;
					}
				}
			}
		}
		//if(Glv.shP) System.out.println(Glv.initialSeed);
		if (Glv.shP)
			System.out.println("Biggest already available number (seed): " + Glv.seed);
	}

	public void myEditor() {
		p.println("I have reached this.");

		MyData card = Glv.threadNN.net.testingSet.get(Glv.cardContainingHighest); //Glv.cardContainingHighest);
		
		editorLayer = new Neuron[Glv.threadNN.net.trainingSet
				.get(0)._analysis.length][Glv.threadNN.net.trainingSet.get(0)._analysis[2].length];

		for (int i = 0; i < editorLayer.length; i++) {
			for (int j = 0; j < editorLayer[i].length; j++) {
				PVector position = new PVector(
						(Glv.neuronSize * 1.2f * i)
								+ (p.width / 2 - (Glv.neuronSize * 1.2f * editorLayer.length) * 0.5f),
						(Glv.neuronSize * 1.2f * j)
								+ (p.height / 2 - (Glv.neuronSize * 1.2f * editorLayer[0].length) * 0.5f));
				editorLayer[i][j] = new Neuron(p, position);
				editorLayer[i][j].m_output = card._analysis[i][j];
			}
		}
	}

	public void reactEditor()
	{
		if (editorLayer != null) {
			for (int i = 0; i < editorLayer.length; i++) {
				for (int j = 0; j < editorLayer[i].length; j++) {

					if (p.mouseX > editorLayer[i][j].position.x - Glv.neuronSize * 0.5f
							&& p.mouseX < editorLayer[i][j].position.x + Glv.neuronSize * 0.5f
							&& p.mouseY > editorLayer[i][j].position.y - Glv.neuronSize * 0.5f
							&& p.mouseY < editorLayer[i][j].position.y + Glv.neuronSize * 0.5f) {

						editorLayer[i][j].iAmChosen = !editorLayer[i][j].iAmChosen;
						if (editorLayer[i][j].iAmChosen) {
							editorLayer[i][j].colour = 360;
						}
						else editorLayer[i][j].colour = 0;
					}
				}
			}
		}
	}
	
	public void controlEvent(ControlEvent theEvent) {
		p.println(theEvent.getController().getName());

		//
		//		if (theEvent.isController()) {
		//
		//			p.print("control event from : " + theEvent.getController().getName()); //controller().name());
		//			p.println(", value : " + theEvent.getController().getValue());
		//
		//			if (theEvent.getController().getName() == "Glv.shouldDimReduction") {
		//				Glv.shouldDimReduction = !Glv.shouldDimReduction;
		//			}
		//		}
		if (theEvent.getName() == "Number_Of_Threads") {
			Glv.numOfThreads = (int) theEvent.getController().getValue();
		}

		if (theEvent.getName() == "setupNeuralNetwork") {
			System.out.println("Please load data first");
		}

		p.println("Number of threads: " + Glv.numOfThreads);
	}
}
