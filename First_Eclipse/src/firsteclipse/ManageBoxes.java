package firsteclipse;

import processing.core.PApplet;
import processing.core.PVector;

public class ManageBoxes {

	private PApplet p;
	// private float[][] hArray;// = new float[Glv.division][Glv.division];
	private MyBox[][] boxes = new MyBox[Glv.division][Glv.division];

	public ManageBoxes(PApplet _p) {
		p = _p;
	}

	void setup() {

		// hArray = new float[Glv.division][Glv.division];

		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				PVector position = new PVector((Glv.roomSize / Glv.division * i) - (Glv.roomSize) * 0.5f,
						(Glv.roomSize / Glv.division * j) - (Glv.roomSize) * 0.5f, 0);
				boxes[i][j] = new MyBox(p, position);
			}
		}

		createHeights();
	}

	void draw() {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				boxes[i][j].draw();
			}
		}
	}

	public void createHeights() {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				boxes[i][j].height = choose(p.random(100f)); // new
				// float(choose(p.random(100f)));
			}
		}

		// for (int i = 0; i < 10; i++) {
		checkNeighbourhood();
		// }
	}

	public void checkNeighbourhood() {

		calculateSum();

		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				boxes[i][j].height = walls(i, j);
				p.println(boxes[i][j].height);
				// boxes[i][j].height = separation(i, j);
			}
		}
		// for (int i = hArray.length - 1; i > 0; i--) {
		// for (int j = hArray[i].length - 1; j > 0; j--) {
		// hArray[i][j] = decideH(i, j);
		// }
		// }
	}

	public void calculateSum() {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				for (int k = -1; k < 2; k++) {
					for (int l = -1; l < 2; l++) {

						if ((i + k) >= 0 && (j + l) >= 0 && (i + k) < boxes.length && (j + l) < boxes[i].length) {
							// p.println(i + k);
							// p.println(j + l);
							boxes[i][j].neighbourSum += boxes[(i + k)][(j + l)].height;

						}
					}
					p.println(boxes[i][j].neighbourSum);
				}
			}
		}
	}

	public float choose(float chance) {
		if (chance > 60.0f) {
			return 1.0f;
		} else
			return 0.0f;
	}

	public float walls(int i, int j) {
		int counter = 0;

		for (int k = -1; k < 2; k++) {
			for (int l = -1; l < 2; l++) {
				if ((i + k) >= 0 && (j + l) >= 0 && (i + k) < boxes.length && (j + l) < boxes[i].length) {
					if (boxes[i][j].neighbourSum > boxes[k + i][l + j].neighbourSum)
						counter++;
				}
			}
		}

		if (counter < 2)
			return 0f;
		else
			return 1.0f;
	}

	// public float separation(int i, int j) {

	// }
}
