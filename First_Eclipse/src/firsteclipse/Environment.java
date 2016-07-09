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
	public static ArrayList<MyData> allAnalysis = new ArrayList<MyData>();

	ControlP5 cp5;
	Accordion accordion;

	PeasyCam cam;

	public Environment(PApplet _p) {
		p = _p;
	}

	public void drawGui() {
		//hint(DISABLE_DEPTH_TEST);
		cam.beginHUD();
		// group number 1, contains 2 bangs

		cp5.draw();

		cam.endHUD();
		//hint(ENABLE_DEPTH_TEST);
	}

	public void draw(PShape s) {
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
				p.shape(s, 0, 0);
				// s.disableStyle();
			}
			p.popStyle();
		}
		p.popMatrix();
	}

	public void setupGui() {

		cp5 = new ControlP5(p);
		cam = new PeasyCam(p, 180);
		// cam.setMinimumDistance(50);
		// cam.setMaximumDistance(500);

		Group g1 = cp5.addGroup("myGroup1").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(150);
		Group g2 = cp5.addGroup("myGroup2").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(150);
		Group g3 = cp5.addGroup("myGroup3").setBackgroundColor(p.color(0, 64)).setBackgroundHeight(150);
		//cp5.addBang("bang").setPosition(10, 20).setSize(100, 100).moveTo(g1).plugTo(this, "shuffle");
		cp5.addToggle("toggle").setValue(false).setPosition(100, 100).setSize(200, 19).moveTo(g1);
		//.plugTo(this,	"shuffle");
		accordion = cp5.addAccordion("acc").setPosition(40, 40).setWidth(200).addItem(g1).addItem(g2).addItem(g3);

		accordion.open(0, 1, 2);
		accordion.setCollapseMode(Accordion.MULTI);

		cp5.setAutoDraw(false);
	}

	public void loadData() {

		String filePath = new File("").getAbsolutePath();

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

	public void loadGenData() {
		String filePath = new File("").getAbsolutePath();
		File folder = new File(filePath + "\\" + "GeneratedData");
		File[] listOfFiles = folder.listFiles();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		for (int i = 0; i < listOfFiles.length; i++) {

			try {
				allAnalysis.add(new MyData(p));
				boolean analysisOrForm = false;
				boolean isItID = true;

				br = new BufferedReader(new FileReader(listOfFiles[i]));
				while ((line = br.readLine()) != null) {

					MyData data = allAnalysis.get(allAnalysis.size() - 1);
					// use comma as separator
					String[] thisRow = line.split(cvsSplitBy);
					//p.println(thisRow);

					for (int j = 0; j < thisRow.length; j++) {

						//if (thisRow[j] != "/n") {
						//char c = thisRow[j].charAt(0);
						//p.print(c);
						if (!thisRow[j].isEmpty()) {
							char c = thisRow[j].charAt(0);

							if (isItID) {
								data.analysisID = Integer.valueOf(thisRow[j]);
								isItID = false;
							} else if (c == '_') {
								allAnalysis.add(new MyData(p)); // Add new object MyData, essentially a new form + analysis.
								analysisOrForm = !analysisOrForm;
								isItID = true;

								//p.println("true" + "_");
								break;
							} else if (c == ':') {
								analysisOrForm = !analysisOrForm; // Is the data I am looking at the form or the analysis?
								isItID = false;
								//p.println("true" + ":");
								break;
							} else {
								//int[] array = Arrays.stream(thisRow).mapToInt(Integer::parseInt).toArray();
								if (analysisOrForm) {
									//p.println("Form");
									data.form.add(thisRow); // If form put line in ArrayList of MyData form.
									isItID = false;
									break;
								} else {
									//p.println("Analysing");
									data.analysis.add(thisRow); // If analysis put line in ArrayList of MyData analysis.
									isItID = false;
									break;
								}
								//}
							}
						}
					}
					//p.println();
				}

				//---> Remove element if it has an index of 0.
				for (int k = 0; k < allAnalysis.size(); k++) {
					if (k < allAnalysis.size() - 1) {
						MyData data = allAnalysis.get(k + 1);

						if (data.analysisID == 0) {
							allAnalysis.remove(data);
						}
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
		}

		//		Collections.sort(allAnalysis, new Comparator<MyData>() {
		//			@Override
		//			public int compare(MyData p1, MyData p2) {
		//				return p1.analysisID - p2.analysisID; // Ascending
		//			}
		//
		//		});

		//p.println("DataLoading: " + allAnalysis.get(0).analysis.get(0)[0]);
		/*
				for (MyData data : allAnalysis) {
					p.println("ID of data: " + data.analysisID);
				}
				p.println("The size of all loaded analysis: " + allAnalysis.size());
			
				*/
		/*
		for (MyData data : allAnalysis) {
			for (String[] oneForm : data.form) {
				for (int i = 0; i < oneForm.length; i++) {
					System.out.print(oneForm[i]);
					System.out.print(',');
				}
				System.out.println();
			}
		}
		*/
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

	public void controlEvent(ControlEvent theEvent) {
		p.println(theEvent.getController().getName());
	}

}

//// --->Checking the biggest column size (if we have data that is not
// homogenous).
// for (int i=1; i<sData.length; i++)
// {
// String [] thisRow= split(sData[i], ",");
//
// cellCount=thisRow.length;
// if (maxNum<cellCount)
// {
// maxNum=cellCount;
// }
// }