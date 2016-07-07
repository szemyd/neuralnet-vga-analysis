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
import controlP5.*;

public class FirstEclipse extends PApplet {

	PeasyCam cam;
	PShape s;
	ControlP5 cp5;
	Accordion accordion;

	Environment env = new Environment(this);
	//	ManageBoxes manBox = new ManageBoxes(this);
	//	SpaceSyntax spaceSyntax = new SpaceSyntax(this);
	public ArrayList<MyThread> threads = new ArrayList<MyThread>();

	public void settings() {
		size(2400, 1200, P3D);
	}

	public void setup() {
		cp5 = new ControlP5(this);
		cam = new PeasyCam(this, 180);
		// cam.setMinimumDistance(50);
		// cam.setMaximumDistance(500);

		randomSeed(Glv.seed);
		colorMode(PConstants.HSB, 360);

		setupGui();

		s = loadShape("solid.OBJ"); // Load the 3D model of the public space.

		noStroke();
		rectMode(PConstants.CENTER);

		env.loadData(); // 03. Loads the CSV file for the surrounding buildings.
		analysisSetup();
	}

	public void draw() {
		background(110);
		lights();

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

		gui();
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

	private void setupGui() {
		Group g1 = cp5.addGroup("myGroup1").setBackgroundColor(color(0, 64)).setBackgroundHeight(150);
		Group g2 = cp5.addGroup("myGroup2").setBackgroundColor(color(0, 64)).setBackgroundHeight(150);
		Group g3 = cp5.addGroup("myGroup3").setBackgroundColor(color(0, 64)).setBackgroundHeight(150);
		//cp5.addBang("bang").setPosition(10, 20).setSize(100, 100).moveTo(g1).plugTo(this, "shuffle");
		cp5.addToggle("toggle").setValue(false).setPosition(100, 100).setSize(200, 19).moveTo(g1);
		//.plugTo(this,	"shuffle");
		accordion = cp5.addAccordion("acc").setPosition(40, 40).setWidth(200).addItem(g1).addItem(g2).addItem(g3);

		accordion.open(0, 1, 2);
		accordion.setCollapseMode(Accordion.MULTI);

		cp5.setAutoDraw(false);
	}

	public void gui() {
		//hint(DISABLE_DEPTH_TEST);
		cam.beginHUD();
		// group number 1, contains 2 bangs

		cp5.draw();

		cam.endHUD();
		//hint(ENABLE_DEPTH_TEST);
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
			GenerateCSV.save(Integer.toString(Glv.howManyUntilNow));
			if (Glv.howManyUntilNow < Glv.numOfSolutions) {
				analysisSetup();
				Glv.howManyUntilNow += Glv.numOfThreads;
			} else {
				println("All solutions done.");
				Glv.isDoneBool = true;
			}
		}

	}

	void toggle(boolean theFlag) {
		if (theFlag == true) {
			Glv.shouldSpaceSyntax = true;
			for (MyThread thread : threads) {
				thread.spaceSyntax();
			}
		} else {
			Glv.shouldSpaceSyntax = false;
			println("Off");
		}
		println("a toggle event.");
	}

	public void controlEvent(ControlEvent theEvent) {
		println(theEvent.getController().getName());
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { firsteclipse.FirstEclipse.class.getName() });
	}
}
