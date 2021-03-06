package firsteclipse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Dictionary;

import com.jogamp.opengl.util.packrect.Rect;
import com.sun.java_cup.internal.runtime.virtual_parse_stack;
import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Single;
import com.sun.xml.internal.ws.dump.LoggingDumpTube.Position;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import controlP5.Accordion;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Group;
import javafx.scene.input.KeyCode;
import junit.framework.Test;
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

	//---> For editor && Dinamic VGA.
	public static ManageBoxes editorBoxes;
	public static MyRect[][] editorRect;
	PVector edSize, edPosition;
	SpaceSyntax spaceSyntax;

	ControlP5 cp5;
	Accordion accordion;

	PeasyCam cam;

	PShape s;

	public static boolean isUpdated = false;

	public Neuron[][] editorLayer;

	public Group g1, g2, g3, g4, g5, g6, g7;
	Bang b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17;
	public RadioButton modeSwitch, genOrASwitch, newOrNet;

	private boolean closed = false;

	//Textarea myTextarea;
	//Println console;

	public Environment(PApplet _p) {
		p = _p;
	}

	public void setupGui(String path) {

		cp5 = new ControlP5(p);
		cam = new PeasyCam(p, 180);
		// cam.setMinimumDistance(50);
		// cam.setMaximumDistance(500);
		cp5.enableShortcuts();

		cp5.begin(100, 20);

		PFont pfont = p.createFont("Arial", 20, true); // use true/false for smooth/no-smooth
		ControlFont font = new ControlFont(pfont, 241);
		/*
				String filePath = new File("").getAbsolutePath();
				File folder = new File(filePath + "\\" + "GeneratedData");
				File[] listOfFiles = folder.listFiles();
				*/

		File folder = new File(path + "\\" + "GeneratedData");
		File[] listOfFiles = folder.listFiles();

		//cp5.loadProperties(("controlP5.json"));

		g1 = cp5.addGroup("Setup").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(0).setPosition(40, 40)
				.hideBar();//BackgroundHeight(150);
		g2 = cp5.addGroup("Modes").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(0).setPosition(510, 40)
				.hideBar();
		g3 = cp5.addGroup("NeuralNetwork").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(0)
				.setPosition(820, 40).hideBar();
		g4 = cp5.addGroup("FormGeneration").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(0)
				.setPosition(1130, 40).hideBar();
		g5 = cp5.addGroup("ManageEditor").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(0)
				.setPosition(1440, 40).hideBar();
		g6 = cp5.addGroup("CameraNav").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(0).setPosition(1750, 40)
				.hideBar();
		g7 = cp5.addGroup("DataAnalysis").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(0)
				.setPosition(2060, 40).hideBar();

		//		  cp5.addButton("buttonA")
		//		     .setPosition(175,575)
		//		     .setImages(loadImage("Arrow-Left.png"), loadImage("Arrow-Right.png"), loadImage("Refresh.png"))
		//		     .updateSize();
		//p.println("filePath: " + filePath);

		String folderWithResources = path + "\\" + "src" + "\\" + "data";

		try {
			String what = new File(".").getCanonicalPath();
			p.println("what: " + what);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//p.println(new File(".").getCanonicalPath());

		//p.println("newPath: " + path);

		p.println("folderWithResources: " + folderWithResources);
		//folderWithResources = path;
		//---> For ProgramModes
		//URL tempURL = getClass().getClassLoader().getResource("playIconRoll.png");

		b1 = cp5.addBang("analysisSetup").setSize(100, 100).setPosition(10, 20)
				.setImages(p.loadImage(folderWithResources + "\\" + "playIconRoll.png"),
						p.loadImage(folderWithResources + "\\" + "playIcon.png"),
						p.loadImage(folderWithResources + "\\" + "playIconPress.png"))
				.updateSize().moveTo(g1).plugTo(this, "shuffle");

		b2 = cp5.addBang("loadDataSetup").setPosition(120, 20).setSize(100, 100)
				.setImages(p.loadImage(folderWithResources + "\\" + "loadRoll.png"),
						p.loadImage(folderWithResources + "\\" + "load.png"),
						p.loadImage(folderWithResources + "\\" + "loadPress.png"))
				.updateSize().moveTo(g1).plugTo(this, "shuffle");

		b3 = cp5.addBang("startEditor").setPosition(230, 20).setSize(100, 100)
				.setImages(p.loadImage(folderWithResources + "\\" + "editorRoll.png"),
						p.loadImage(folderWithResources + "\\" + "editor.png"),
						p.loadImage(folderWithResources + "\\" + "editorPress.png"))
				.updateSize().moveTo(g1).plugTo(this, "shuffle");

		b4 = cp5.addBang("setupNeuralNetwork").setPosition(340, 20).setSize(100, 100)
				.setImages(p.loadImage(folderWithResources + "\\" + "networkRoll.png"),
						p.loadImage(folderWithResources + "\\" + "network.png"),
						p.loadImage(folderWithResources + "\\" + "networkPress.png"))
				.updateSize().moveTo(g1).plugTo(this, "shuffle");
		//<---

		b5 = cp5.addBang("closeIt").setPosition(p.width - 40f, 40f).setSize(20, 40);

		b6 = cp5.addBang("resetEditor").setPosition(20, 20).setSize(60, 20).moveTo(g5).plugTo(this, "shuffle");
		b13 = cp5.addBang("selectMiddle").setPosition(90, 20).setSize(60, 20).moveTo(g5).plugTo(this, "shuffle");
		b17 = cp5.addBang("selectAll").setPosition(160, 20).setSize(60, 20).moveTo(g5).plugTo(this, "shuffle");
		b10 = cp5.addBang("everySecond").setPosition(20, 60).setSize(60, 20).moveTo(g5).plugTo(this, "shuffle");
		b11 = cp5.addBang("everyThird").setPosition(90, 60).setSize(60, 20).moveTo(g5).plugTo(this, "shuffle");
		b12 = cp5.addBang("everyFourth").setPosition(160, 60).setSize(60, 20).moveTo(g5).plugTo(this, "shuffle");

		b7 = cp5.addBang("ninetyD").setPosition(20, 20).setSize(40, 20).moveTo(g6).plugTo(this, "shuffle")
				.setLabel("+90�");
		b8 = cp5.addBang("mNinetyD").setPosition(90, 20).setSize(40, 20).moveTo(g6).plugTo(this, "shuffle")
				.setLabel("-90�");

		b9 = cp5.addBang("compareValues").setPosition(20, 20).setSize(60, 20).moveTo(g7).plugTo(this, "shuffle")
				.setLabel("COMPARE");

		b14 = cp5.addBang("saveData").setPosition(90, 20).setSize(60, 20).moveTo(g7).plugTo(this, "shuffle")
				.setLabel("saveData");

		b15 = cp5.addBang("saveNN").setPosition(160, 20).setSize(60, 20).moveTo(g7).plugTo(this, "shuffle")
				.setLabel("saveNN");

		b16 = cp5.addBang("loadNN").setPosition(160, 60).setSize(60, 20).moveTo(g7).plugTo(this, "shuffle")
				.setLabel("loadNN");

		//p.println(b5.getName());

		//cp5.addBang("setupNeuralNetwork").setPosition(340, 20).setSize(100, 100).moveTo(g1).plugTo(this, "shuffle");

		modeSwitch = cp5.addRadioButton("programMode").setPosition(20, 20).setItemWidth(20).setItemHeight(50)
				.setItemsPerRow(5).addItem("Generating", 0).addItem("Neural Network", 1).addItem("Analysis", 2)
				.addItem("Editor", 3).setColorLabel(p.color(255)).activate(0).moveTo(g2).hideLabels().setSpacingRow(20)
				.setSpacingColumn(10);

		genOrASwitch = cp5.addRadioButton("genOrA").setPosition(20, 80).setItemWidth(20).setItemHeight(50)
				.setItemsPerRow(5).addItem("Specified", 0).addItem("Raw", 1).addItem("Analys", 2)
				.setColorLabel(p.color(360)).activate(0).moveTo(g2).hideLabels().setSpacingRow(20).setSpacingColumn(10);

		newOrNet = cp5.addRadioButton("newOrNet").setPosition(200, 80).setItemWidth(20).setItemHeight(50)
				.setItemsPerRow(5).addItem("new", 0).addItem("net", 1).setColorLabel(p.color(360)).activate(1)
				.moveTo(g4).hideLabels().setSpacingRow(20).setSpacingColumn(10);

		//---> Sliders for NN
		cp5.addSlider("numOfLearning").setPosition(25, 20).setSize(20, 100).setRange(0, 500).setNumberOfTickMarks(81)
				.plugTo(Glv.numOfLearning).moveTo(g3).setValue(500).setLabel("Learning").setSliderMode(Slider.FIX);
		cp5.addSlider("learningRate").setPosition(85, 20).setSize(20, 100).setRange(0f, 0.01f).setNumberOfTickMarks(51)
				.plugTo(Glv.LEARNING_RATE).moveTo(g3).setValue(0.001f).setLabel("L-Rate");
		cp5.addSlider("numOfCycles").setPosition(145, 20).setSize(20, 100).setRange(0, 100).setNumberOfTickMarks(21)
				.plugTo(Glv.numOfCycles).moveTo(g3).setValue(10).setLabel("Cycles/Press");
		cp5.addSlider("hiddenLayerSize").setPosition(205, 20).setSize(20, 100).setRange(0f, 5f).setNumberOfTickMarks(21)
				.plugTo(Glv.howMuchBiggerHidden).moveTo(g3).setValue(1.5f).setLabel("Hidden Layer");
		cp5.addSlider("splitSize").setPosition(265, 20).setSize(20, 100).setRange(0, 20).setNumberOfTickMarks(21)
				.plugTo(Glv.splitSize).moveTo(g3).setValue(5).setLabel("Split Size");

		//---> Sliders for Generating Data.
		cp5.addSlider("numberOfThreads").setPosition(35, 20).setSize(20, 100).setRange(0, 20).setNumberOfTickMarks(21)
				.plugTo(Glv.numOfThreads).moveTo(g4).setValue(5).setLabel("Threads");
		cp5.addSlider("numberOfSolutions").setPosition(95, 20).setSize(20, 100).setRange(0, 5000)
				.setNumberOfTickMarks(21).plugTo(Glv.numOfSolutions).moveTo(g4).setValue(1).setLabel("Solutions");

		if (listOfFiles != null) {
			cp5.addSlider("numberOfRead").setPosition(155, 20).setSize(20, 100).setRange(0, listOfFiles.length)
					.setNumberOfTickMarks(21).plugTo(Glv.numOfRead).moveTo(g4).setValue(listOfFiles.length)
					.setLabel("Reading");
		} else {
			cp5.addSlider("numberOfRead").setPosition(145, 20).setSize(20, 100).setRange(0, 1).setNumberOfTickMarks(21)
					.plugTo(Glv.numOfRead).moveTo(g4).setValue(0).setLabel("Reading");
		}

		cp5.addToggle("dimensionalityReduction").setValue(true).setPosition(200, 20).setSize(60, 20).moveTo(g4)
				.plugTo(Glv.shouldDimReduction).setLabel("DimReduction");
		cp5.addToggle("editorForAnalysisOn").setValue(false).setPosition(20, 100).setSize(60, 20).moveTo(g5)
				.plugTo(Glv.editorForAnalysisOn).setLabel("ANAEditor");
		;
		cp5.addToggle("splitNetwork").setValue(true).setPosition(90, 100).setSize(60, 20).moveTo(g5)
				.plugTo(Glv.splitNetwork).setLabel("Split");
		cp5.addToggle("clustering").setValue(true).setPosition(160, 100).setSize(60, 20).moveTo(g5)
				.plugTo(Glv.neighbourHoodOrClustering).setLabel("Neighbourhood");

		cp5.addToggle("shouldICalculateWhole").setValue(true).setPosition(20, 60).setSize(60, 20).moveTo(g7)
				.plugTo(Glv.shouldICalculateWhole);

		g1.setColorBackground(p.color(360, 360, 360, 160)).setColorForeground(p.color(360, 360, 360, 250));
		g2.setColorBackground(p.color(360, 360, 360, 160)).setColorForeground(p.color(360, 360, 360, 250));
		g3.setColorBackground(p.color(360, 360, 360, 160)).setColorForeground(p.color(360, 360, 360, 250));
		g4.setColorBackground(p.color(360, 360, 360, 160)).setColorForeground(p.color(360, 360, 360, 250));

		//cp5.addButton("plug", 2);
		//		ButtonBar b = cp5.addButtonBar("bar").setPosition(0, 0).setSize(p.width, 40)
		//				.addItems(p.split("a b c d e f g h i j", " ")).setColorBackground(170);
		//		;

		//.plugTo(this,	"shuffle");

		//accordion.close(1);

		/*
		accordion = cp5.addAccordion("acc").setPosition(40, 40).setWidth(460).addItem(g1).hideBar().setColorBackground(p.color(360,360,360,100)).setColorForeground(p.color(360,360,360,100));
		//.setColorBackground(p.color(360, 100, 360));
		accordion.open(0, 1, 2, 3, 4);
		accordion = cp5.addAccordion("what").setPosition(510, 40).setWidth(300).addItem(g2).setColorBackground(p.color(360,360,360,100)).setColorForeground(p.color(360,360,360,100));
		
		accordion.open(0, 1, 2, 3, 4);
		accordion = cp5.addAccordion("yes").setPosition(820, 40).setWidth(300).addItem(g3).setColorBackground(p.color(360,360,360,100)).setColorForeground(p.color(360,360,360,100));
		accordion.open(0, 1, 2, 3, 4);
		accordion = cp5.addAccordion("hmm").setPosition(1130, 40).setWidth(300).addItem(g4).setColorBackground(p.color(360,360,360,60)).setColorForeground(p.color(360,360,360,100));
		
		accordion.open(0, 1, 2, 3, 4);
		accordion.setCollapseMode(Accordion.MULTI);
		*/

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

	public void setupThreeDEditor() {
		spaceSyntax = new SpaceSyntax(p, 1999);

		editorBoxes = new ManageBoxes(p);
		editorRect = new MyRect[p.floor(editorBoxes.boxes.length / (Glv.cubeSizeReduced / Glv.cubeSize))][p
				.floor(editorBoxes.boxes[0].length / (Glv.cubeSizeReduced / Glv.cubeSize))];

		edSize = new PVector(editorBoxes.boxes.length * 30f, editorBoxes.boxes[0].length * 30f);
		edPosition = new PVector(p.width - edSize.x - 20, ((p.height-150f) * 0.5f+150f) - edSize.y * 0.5f);

		for (int i = 0; i < editorRect.length; i++) {
			for (int j = 0; j < editorRect[i].length; j++) {
				editorRect[i][j] = new MyRect(p, new PVector(
						edPosition.x + 20f
								+ (p.floor((edSize.x - 40f)
										/ (editorBoxes.boxes.length / (Glv.cubeSizeReduced / Glv.cubeSize)))) * i,
						edPosition.y + 20f
								+ (p.floor((edSize.y - 40f)
										/ (editorBoxes.boxes[i].length / (Glv.cubeSizeReduced / Glv.cubeSize)))) * j),
						new PVector(
								(p.floor((edSize.x - 40f)
										/ (editorBoxes.boxes.length / (Glv.cubeSizeReduced / Glv.cubeSize)))),
								(p.floor((edSize.y - 40f)
										/ (editorBoxes.boxes[i].length / (Glv.cubeSizeReduced / Glv.cubeSize))))),
						editorRect.length * i + j);
			}
		}

		for (int i = 0; i < editorBoxes.boxes.length; i++) {
			for (int j = 0; j < editorBoxes.boxes[i].length; j++) {
				PVector position = new PVector((Glv.cubeSize * i) - (Glv.cubeSize * Glv.divisionX) * 0.5f,
						(Glv.cubeSize * j) - (Glv.cubeSize * Glv.divisionY) * 0.5f, 0);

				editorBoxes.boxes[i][j] = new MyBox(p, position);
			}
		}
		spaceSyntax.setup(editorBoxes.boxes);
	}

	//---> DRAWING THINGS
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
				//p.rect(0, 0, 520f, 560f);

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

		if (cp5.isMouseOver()) {
			cam.setActive(false);
		}

		cam.beginHUD();
		{
			p.noLights();

			if (g1.isOpen()) {
				p.pushStyle();
				{
					p.rectMode(PConstants.CORNER);
					p.noStroke();
					p.stroke(360, 0, 160, 280);
					p.fill(360, 0, 0, 200);
					p.rect(30, 40, 470, 150, 30);
					p.rect(510, 40, 300, 150, 30);
					p.rect(820, 40, 310, 150, 30);
					p.rect(1140, 40, 290, 150, 30);
					p.rect(1440, 40, 300, 150, 30);
					p.rect(1750, 40, 300, 150, 30);
					p.rect(2060, 40, 300, 150, 30);

					//	p.rect(40, 40, p.width - 80, 150, 15);
					//p.rect(40, 40, p.width - 80, 150, 15);
				}
				p.popStyle();
			}
			cp5.draw();
		}
		cam.endHUD();
		//hint(ENABLE_DEPTH_TEST);
	}

	public void drawEditor() {

		drawBoundary(editorLayer[0][0].position, editorLayer.length, editorLayer[0].length, "Editor");

		for (int i = 0; i < editorLayer.length; i++) {
			for (int j = 0; j < editorLayer[i].length; j++) {
				editorLayer[i][j].draw();
			}
		}
	}

	private void drawBoundary(PVector position, int sizeX, int sizeY, String myText) {
		p.pushStyle();
		{
			/*
			p.noFill();
			//p.fill(360);
			p.stroke(360);
			*/
			p.stroke(360, 0, 160, 280);
			p.fill(360, 0, 0, 200);
			
			p.pushMatrix();
			{
				p.rectMode(PConstants.CORNER);
				p.rect(position.x - 10f - Glv.neuronSize, position.y - 10f - Glv.neuronSize,
						(sizeX * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize,
						(sizeY * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize, 20f);
			}
			p.popMatrix();
		}
		p.popStyle();

		drawText(myText, new PVector(position.x - Glv.neuronSize,
				position.y - 10f - Glv.neuronSize + (sizeY * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize + 20f));
	}

	private void drawText(String myText, PVector position) {
		p.pushMatrix();
		{
			p.pushStyle();
			{
				p.textAlign(PConstants.LEFT, PConstants.CENTER);
				p.textSize(18);
				p.fill(360);
				p.text(myText, position.x + 15f, position.y);
				p.ellipse(position.x, position.y, 8f, 8f);
			}
			p.popStyle();

		}
		p.popMatrix();
	}

	public void drawThreeDEditor() {
		cam.beginHUD();
		{
			p.noLights();

			p.pushStyle();
			{
				p.rectMode(PConstants.CORNER);
				p.noStroke();
				p.stroke(360, 0, 160, 280);
				p.fill(360, 0, 0, 200);
				p.rect(edPosition.x, edPosition.y, edSize.x-25f, edSize.y, 30);

				p.stroke(0);
				p.fill(280);

				for (int i = 0; i < editorRect.length; i++) {
					for (int j = 0; j < editorRect[i].length; j++) {
						editorRect[i][j].drawEditor();
					}
				}
			}
			p.popStyle();
		}
		cam.endHUD();

	}

	public void drawEditorBoxes() {
		for (int i = 0; i < editorBoxes.boxes.length; i++) {
			for (int j = 0; j < editorBoxes.boxes[i].length; j++) {
				editorBoxes.boxes[i][j].draw();
			}
		}
	}
	//<---

	//---> Data Loading for envionrment.
	public void loadData(String path) {

		String filePath = new File("").getAbsolutePath();
		//		s = p.loadShape(filePath + "\\src\\data\\solid.obj"); // Load the 3D model of the public space.
		s = p.loadShape(path + "\\src\\data\\solid2.obj"); // Load the 3D model of the public space.
		//	String csvFile = filePath + "\\src\\data\\sur.csv";
		String csvFile = path + "\\src\\data\\sur.csv";

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
			p.println("Environment CSV Data Loaded!");
	}

	public void checkFilesUpdateSeed(String path) {

		/*
		String filePath = new File("").getAbsolutePath();
		File folder = new File(filePath + "\\" + "GeneratedData");
		*/
		File folder = new File(path + "\\" + "GeneratedData");
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
	//<---

	//---> SETUP Editormode.
	public void myEditor() {
		p.println("I have reached this.");

		//MyData card = Glv.threadNN.net.testingSet.get(Glv.cardContainingHighest); //Glv.cardContainingHighest);
		MyData card = Glv.threadNN.net.testingSet.get(0); //Glv.cardContainingHighest);

		editorLayer = new Neuron[Glv.threadNN.net.trainingSet
				.get(1)._analysis.length][Glv.threadNN.net.trainingSet.get(1)._analysis[0].length];

		Neuron[][] dummyInput = new Neuron[1][1];

		for (int i = 0; i < editorLayer.length; i++) {
			for (int j = 0; j < editorLayer[i].length; j++) {
				PVector position = new PVector(
						(Glv.neuronSize * 1.2f * i)
								+ 60f,//(30f - (Glv.neuronSize * 1.2f * editorLayer.length) * 0.5f),
						(Glv.neuronSize * 1.2f * j)
								+ (((p.height-150f) *0.5f + 150f) - (Glv.neuronSize * 1.2f * editorLayer[0].length) * 0.5f));

				editorLayer[i][j] = new Neuron(p, position, dummyInput, new PVector(i, j));
				if (card._analysis[i][j] != null) {
					editorLayer[i][j].m_output = card._analysis[i][j];
				} else
					editorLayer[i][j].m_output = 0f;
			}
		}
	}

	public void reactEditor() { // Reacts to the input so you can select the neurons.

		if (editorLayer != null) {
			for (int i = 0; i < editorLayer.length; i++) {
				for (int j = 0; j < editorLayer[i].length; j++) {

					if (p.mouseX > editorLayer[i][j].position.x - Glv.neuronSize * 0.5f
							&& p.mouseX < editorLayer[i][j].position.x + Glv.neuronSize * 0.5f
							&& p.mouseY > editorLayer[i][j].position.y - Glv.neuronSize * 0.5f
							&& p.mouseY < editorLayer[i][j].position.y + Glv.neuronSize * 0.5f) {

						//editorLayer[i][j].iAmChosen = !editorLayer[i][j].iAmChosen;
						if (p.keyPressed) {
							if (p.keyCode == PConstants.SHIFT)
								editorLayer[i][j].iAmChosen = true;
							else if (p.keyCode == PConstants.ALT)
								editorLayer[i][j].iAmChosen = false;
						}

						if (editorLayer[i][j].iAmChosen) {
							editorLayer[i][j].colour = 360;
						} else
							editorLayer[i][j].colour = 0;
					}
				}
			}
		}
	}

	public void reactEditor2() { // Changes the values of the rectangles in the editor and modifies the height of the boxes.
		for (int i = 0; i < editorRect.length; i++) {
			for (int j = 0; j < editorRect[i].length; j++) {
				if (p.mouseX > editorRect[i][j].position.x
						&& p.mouseX < editorRect[i][j].position.x + editorRect[i][j].size.x
						&& p.mouseY > editorRect[i][j].position.y
						&& p.mouseY < editorRect[i][j].position.y + editorRect[i][j].size.y) {

					if (editorRect[i][j].height < 1.0f) {
						editorRect[i][j].height = 1.0f;
					} else
						editorRect[i][j].height = 0.0f;

					for (int k = 0; k < Glv.cubeSizeReduced / Glv.cubeSize; k++) {
						for (int l = 0; l < Glv.cubeSizeReduced / Glv.cubeSize; l++) {
							if ((i + k) >= 0 && (j + l) >= 0 && (i + k) < editorBoxes.boxes.length
									&& (j + l) < editorBoxes.boxes[i].length) {
								editorBoxes.boxes[p.floor(i * Glv.cubeSizeReduced / Glv.cubeSize) + k][p.floor(
										j * Glv.cubeSizeReduced / Glv.cubeSize) + l].height = editorRect[i][j].height;
							}
						}
					}
				}
			}

		}

		isUpdated = false;
	}

	public void setSpaceSyntaxValues() {
		if (Glv.genOrA == 2) { // If I am actually in the mode where I trained the network to understand analysis.
			if (Glv.threadNN != null) {
				if (Glv.threadNN.net != null) {
					if (Glv.threadNN.net.dataLoaded) {
						if (Glv.threadNN.net.neuralnet != null || Glv.threadNN.net.splitNeuralnets != null) {
							Float[][] temporaryInfo = new Float[editorRect.length][editorRect[0].length];

							for (int i = 0; i < editorRect.length; i++) {
								for (int j = 0; j < editorRect[i].length; j++) {
									if (editorRect[i][j].height > 0.1f)
										temporaryInfo[i][j] = 1.0f;
									else
										temporaryInfo[i][j] = -1.0f;
								}
							}

							for (int i = 0; i < spaceSyntax.rectangles.length; i++) {
								for (int j = 0; j < spaceSyntax.rectangles[i].length; j++) {
									spaceSyntax.rectangles[i][j].height = 0.0f;
								}
							}

							if (Glv.splitNetwork) {
								for (int m = 0; m < Glv.threadNN.net.splitNeuralnets.length; m++) {
									Glv.threadNN.net.splitNeuralnets[m].respond(Glv.threadNN.net.testingSet.get(0),
											temporaryInfo);

									for (int i = 0; i < Glv.threadNN.net.splitNeuralnets[m].m_output_layer.length; i++) {
										for (int j = 0; j < Glv.threadNN.net.splitNeuralnets[m].m_output_layer[i].length; j++) {
											spaceSyntax.rectangles[(int) Glv.threadNN.net.splitNeuralnets[m].m_output_layer[i][j].idNum.x][(int) Glv.threadNN.net.splitNeuralnets[m].m_output_layer[i][j].idNum.y].height = Glv.threadNN.net.splitNeuralnets[m].m_output_layer[i][j].m_output;
										}
									}
								}
							} else {
								Glv.threadNN.net.neuralnet.respond(Glv.threadNN.net.testingSet.get(0), temporaryInfo);

								for (int i = 0; i < Glv.threadNN.net.neuralnet.m_output_layer.length; i++) {
									for (int j = 0; j < Glv.threadNN.net.neuralnet.m_output_layer[i].length; j++) {
										spaceSyntax.rectangles[(int) Glv.threadNN.net.neuralnet.m_output_layer[i][j].idNum.x][(int) Glv.threadNN.net.neuralnet.m_output_layer[i][j].idNum.y].height = Glv.threadNN.net.neuralnet.m_output_layer[i][j].m_output;
									}

								}
							}

							isUpdated = true;
						}
					}
				}
			}
		}
	}
	//<---

	public void closeIt() {
		g1.close();
		g2.close();
		g3.close();
		g4.close();

		closed = !closed;
	}

	public void controlEvent(ControlEvent theEvent) {
		if (theEvent.getName() == "closeIt") {
			g1.close();
			g2.close();
			g3.close();
			g4.close();

			closed = !closed;
		}

		//				Glv.shouldDimReduction = !Glv.shouldDimReduction;
		//			}
		//p.println(theEvent.getController().getName());

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

		//p.println("Number of threads: " + Glv.numOfThreads);
	}
}
