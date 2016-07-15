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
	

	//Textarea myTextarea;
	//Println console;

	public Environment(PApplet _p) {
		p = _p;
	}

	//	private void keyPressed() {
	//		switch (p.key) {
	//		case ('1'):
	//			console.pause();
	//			break;
	//		case ('2'):
	//			console.play();
	//			break;
	//		case ('3'):
	//			console.setMax(8);
	//			break;
	//		case ('4'):
	//			console.setMax(-1);
	//			break;
	//		case ('5'):
	//			console.clear();
	//			break;
	//		}
	//	}

	public void setupGui(boolean DimensionalityReduction) {

		cp5 = new ControlP5(p);
		cam = new PeasyCam(p, 180);
		// cam.setMinimumDistance(50);
		// cam.setMaximumDistance(500);
		cp5.enableShortcuts();

		Group g1 = cp5.addGroup("myGroup1").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(150);//BackgroundHeight(150);
		Group g2 = cp5.addGroup("myGroup2").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(150);
		Group g3 = cp5.addGroup("myGroup3").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(150);

		cp5.addBang("analysisSetup").setPosition(10, 20).setSize(100, 100).moveTo(g1).plugTo(this, "shuffle");
		cp5.addBang("loadDataSetup").setPosition(120, 20).setSize(100, 100).moveTo(g1).plugTo(this, "shuffle");

		cp5.addSlider("numberOfThreads").setPosition(10, 20).setSize(20, 100).setRange(0, 20).setNumberOfTickMarks(5).moveTo(g2);
		cp5.addToggle("DimensionalityReduction").setValue(false).setPosition(80, 20).setSize(100, 19).moveTo(g2);
		
		//.plugTo(this,	"shuffle");
		accordion = cp5.addAccordion("acc").setPosition(40, 40).setWidth(300).addItem(g1).addItem(g2).addItem(g3);

		accordion.open(0, 1, 2);
		//accordion.close(1);
		accordion.setCollapseMode(Accordion.MULTI);

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

	public void draw() {
		p.pushStyle();
		{
			p.strokeWeight(5.0f);
			p.stroke(360, 0, 360);
			for (Building building : buildings) {
				building.draw();
			}
		}
		p.popStyle();

		p.pushMatrix();
		{
			p.pushStyle();
			{
				p.rectMode(PConstants.CENTER);
				p.fill(360, 0, 280);

				p.translate(0, 0, -.005f);
				p.rect(0, 0, 520f, 560f);

				p.translate(0, 0, -.01f);

				s.disableStyle();
				p.fill(250);
				p.shape(s, 0, 0);
				s.enableStyle();

			}
			p.popStyle();
		}
		p.popMatrix();
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

	void toggle(boolean theFlag) {
		if (theFlag == true) {
			Glv.shouldSpaceSyntax = true;
			//			for (MyThread thread : threads) {
			//				thread.spaceSyntax();
			//			}
		} else {
			Glv.shouldSpaceSyntax = false;
			p.println("Off");
		}
		p.println("a toggle event.");
	}

	public void numberOfThreads(float theValue) {
		Glv.numOfThreads=(int)theValue;
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
		if (theEvent.getName() == "Number_Of_Threads")
		{
			Glv.numOfThreads = (int) theEvent.getController().getValue();
		}
		p.println("Number of threads: " + Glv.numOfThreads);
	}
}
