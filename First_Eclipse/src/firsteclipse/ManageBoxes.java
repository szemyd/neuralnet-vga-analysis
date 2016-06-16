package firsteclipse;

import processing.core.PApplet;
import processing.core.PVector;

public class ManageBoxes {

	private PApplet p;
	private float[][] hArray;// = new float[Glv.division][Glv.division];
	private MyBox[][] boxes = new MyBox[Glv.division][Glv.division];

	public ManageBoxes(PApplet _p) {
		p = _p;
	}

	void setup() {

		hArray = new float[Glv.division][Glv.division];
		createHeights();

		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				PVector position = new PVector((Glv.roomSize / Glv.division * i) - (Glv.roomSize) * 0.5f,
						(Glv.roomSize / Glv.division * j) - (Glv.roomSize) * 0.5f, hArray[i][j] * 0.5f);
				boxes[i][j] = new MyBox(p, hArray[i][j], position);
			}
		}
	}

	void draw() {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				boxes[i][j].draw();
			}
		}
	}

	public void createHeights() {
		for (int i = 0; i < hArray.length; i++) {
			for (int j = 0; j < hArray[i].length; j++) {
				PApplet.println("i: " + i + " | j: " + j + "hArray: ");

				hArray[i][j] = choose(p.random(100f)); // new
														// float(choose(p.random(100f)));
			}
		}

		for (int i = 0; i < 10; i++) {
			checkNeighbourhood();
		}
	}

	public void checkNeighbourhood() {

		for (int i = 0; i < hArray.length; i++) {
			for (int j = 0; j < hArray[i].length; j++) {
				hArray[i][j] = decideH(i, j);
			}
		}
		for (int i = hArray.length - 1; i > 0; i--) {
			for (int j = hArray[i].length - 1; j > 0; j--) {
				hArray[i][j] = decideH(i, j);
			}
		}
	}

	public float choose(float chance) {

		PApplet.println("im here");
		float myHeight = 0f;

		if (chance > 99.98f) {
			myHeight = p.random(16000f, 80000f);
		} else if (chance > 99.65f)
			myHeight = p.random(8000f);
		else
			return 0f;

		return myHeight;
	}

	public float decideH(int i, int j) {
		float myHeight = 0f;
		//
		// if (hArray[i][j] > 1f)
		// return hArray[i][j];
		// else
		if (i > 0 && j > 0 && i < hArray.length - 1 && j < hArray[i].length - 1)
			myHeight = (+hArray[(i - 1)][j] + hArray[i][(j - 1)] + hArray[(i + 1)][j] + hArray[i][(j + 1)]
					+ hArray[i + 1][j + 1] + hArray[i - 1][j - 1] + hArray[i - 1][j + 1]
					// + (hArray[i][j])
					+ hArray[i + 1][j - 1]) / 8.3f;

		return myHeight;
	}

}
