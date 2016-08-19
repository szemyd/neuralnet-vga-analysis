package firsteclipse;

import javax.security.auth.PrivateCredentialPermission;

import controlP5.Println;
import processing.core.PApplet;
import processing.core.PVector;

public class ManageBoxes {

	private PApplet p;
	public MyBox[][] boxes = new MyBox[Glv.divisionX][Glv.divisionY];

	public ManageBoxes(PApplet _p) {
		p = _p;
	}

	void setup() {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				PVector position = new PVector((Glv.cubeSize * i) - (Glv.cubeSize * Glv.divisionX) * 0.5f,
						(Glv.cubeSize * j) - (Glv.cubeSize * Glv.divisionY) * 0.5f, 0);

				boxes[i][j] = new MyBox(p, position);
			}
		}
	}

	void draw() { // Draws the boxes
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].height > 0.1f)
					boxes[i][j].draw();
			}
		}
	}

	public void createHeights() { // Creates the height 1. choose randomly which one to switch on 2. iterate through and change rest accordingly
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				float randomity = choose(p.random(100f));
				boxes[i][j].height = randomity; // Assign a height value to each boxes[i][j].height = randomity; // box
			}
		}

		for (int i = 0; i < 50; i++) {
			calculateSum(); // Calculates number of neighbours
			checkNeighbourhood(); // Runs CA for a set number of iteration.
		}

		if (calcHowManyAreUp() > boxes[0].length * boxes.length * 0.3f) // If the numberOfBoxes up is higher than a certain %.
			reduceNumRandomly();

		if (Glv.shouldDimReduction)
			dimReduction();
	}

	private void checkNeighbourhood() {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				// boxes[i][j].height = ca(i, j);
				boxes[i][j].height = walls(i, j);
				// boxes[i][j].height = walls2(i, j);
				// boxes[i][j].height = separation(i, j);
			}
		}
	}

	private float calcHowManyAreUp() {
		float numUp = 0.0f;
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].height > 0.1f)
					numUp++;
			}
		}
		return numUp;
	}

	private void reduceNumRandomly() {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (p.random(100f) < 35.0f && boxes[i][j].height > 0.1f)
					boxes[i][j].height = 0.0f;
				//p.println("I've reached this.");
			}
		}
	}

	public float ca(int i, int j) {
		// p.println(boxes[i][j].neighbourSum);
		if (boxes[i][j].neighbourSum >= 5)
			return 0.0f;
		else if (boxes[i][j].neighbourSum >= 4)
			return boxes[i][j].height;
		else if (boxes[i][j].neighbourSum < 2)
			return 0.0f;
		else
			return 1.0f;
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

		if (counter >= 5)
			return 1.0f;
		else if (counter >= 4)
			return boxes[i][j].height;
		else
			return 0.0f;

		/*
		 * if (counter > 4) return 1.0f; else if (counter == 2) return
		 * boxes[i][j].height; else return 0.0f;
		 */
	}

	public float separation(int i, int j) {
		for (int k = -2; k < 3; k++) {
			for (int l = -2; l < 3; l++) {
				if (k < -1 || k > 1 && l < -1 || l > 1) {
					// p.println("im here");
					if ((i + k) >= 0 && (j + l) >= 0 && (i + k) < boxes.length && (j + l) < boxes[i].length) {
						if (boxes[i + k][j + l].height > 0.1f) {
							if (p.random(100) > 50f) {
								boxes[i + k][j + l].height = 0.0f;

							} else
								return 0.0f;

						}

					}
				}
			}
		}

		return boxes[i][j].height;
	}

	public float separation2(int i, int j) {
		for (int k = -4; k < 5; k++) {
			for (int l = -4; l < 5; l++) {
				if (k < -1 && k > 1 && l < -1 && l > 1) {
					if ((i + k) >= 0 && (j + l) >= 0 && (i + k) < boxes.length && (j + l) < boxes[i].length) {
						if (boxes[i + k][j + l].height > 0) {
							if (p.random(100) > 50f) {
								boxes[i + k][j + l].height = 0.0f;
							} else
								return 0.0f;
						}

					}
				}
			}
		}
		return boxes[i][j].height;
	}

	public void calculateSum() { // Fills up for each box how many neighbours it has. ƒ
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				for (int k = -1; k < 2; k++) {
					for (int l = -1; l < 2; l++) {

						if ((i + k) >= 0 && (j + l) >= 0 && (i + k) < boxes.length && (j + l) < boxes[i].length) {
							boxes[i][j].neighbourSum += boxes[(i + k)][(j + l)].height;
						}
					}
				}
			}
		}
	}

	public float choose(float chance) { // Gives either an on or an off value to the boxes according to a predefined chance. ƒ
		if (chance > Glv.chance) {
			return 1.0f;
		} else
			return 0.0f;
	}

	public void dimReduction() { // Goes through a predefined set and reduces the dimensionality.

		for (int i = 0; i < boxes.length; i += Glv.cubeSizeReduced / Glv.cubeSize) {
			for (int j = 0; j < boxes[i].length; j += Glv.cubeSizeReduced / Glv.cubeSize) {

				float counter = 0;
				for (int k = 0; k < Glv.cubeSizeReduced / Glv.cubeSize; k++) {
					for (int l = 0; l < Glv.cubeSizeReduced / Glv.cubeSize; l++) {
						if ((i + k) >= 0 && (j + l) >= 0 && (i + k) < boxes.length && (j + l) < boxes[i].length) {
							counter += boxes[i + k][j + l].height;
						}
					}
				}

				if (counter > 5.0f)
					changeVal(1.0f, i, j);
				else
					changeVal(0.0f, i, j);
			}
		}
	}

	private void changeVal(float num, int i, int j) {
		for (int k = 0; k < Glv.cubeSizeReduced / Glv.cubeSize; k++) {
			for (int l = 0; l < Glv.cubeSizeReduced / Glv.cubeSize; l++) {
				if ((i + k) >= 0 && (j + l) >= 0 && (i + k) < boxes.length && (j + l) < boxes[i].length) {
					boxes[i + k][j + l].height = num;

					//					if (k == 0 || k == (Glv.cubeSizeReduced / Glv.cubeSize) - 1 || l == 0
					//							|| l == (Glv.cubeSizeReduced / Glv.cubeSize) - 1) {
					//						boxes[i + k][j + l].amIEdge = true;
					//					}
					//					else
					//						boxes[i + k][j + l].amIEdge = false; // Switch amIEdge off if you are not on the edge of the box.

					if (k == 0 && l == 0) {
						boxes[i + k][j + l].amIEdge = true;
					} else
						boxes[i + k][j + l].amIEdge = false; // Switch amIEdge off if you are not on the edge of the box.

				}

			}
		}
	}

	public void setHeightToMyWill() {

		//p.println("_form.length: " + Glv.threadNN.net.neuralnet.lastCard._form.length + " | _form[0].length: " + Glv.threadNN.net.neuralnet.lastCard._form[0].length);

		//---> This is if I wanted to set it to the last card
		/*
			for (int i = 0; i < Glv.threadNN.net.neuralnet.lastCard._form.length; i++) {
				for (int j = 0; j < Glv.threadNN.net.neuralnet.lastCard._form[i].length; j++) {
					if (i < boxes.length && j < boxes[i].length) {
						if (boxes[i][j] != null && Glv.threadNN.net.neuralnet.lastCard._form[i][j] != null)
							boxes[i][j].height = Glv.threadNN.net.neuralnet.lastCard._form[i][j];
					}
				}
			}
			*/
		//<---
		int k = 0;
		for (int i = 0; i < boxes.length; i += Glv.cubeSizeReduced / Glv.cubeSize) {
			int l = 0;
			for (int j = 0; j < boxes[i].length; j += Glv.cubeSizeReduced / Glv.cubeSize) {
				if (k < Glv.threadNN.net.neuralnet.m_output_layer.length
						&& l < Glv.threadNN.net.neuralnet.m_output_layer[0].length) {
					if (Glv.threadNN.net.neuralnet.m_output_layer[k][l].m_output < 0.0f) // If it's not really black then take it as down (0).
					{
						for (int m = 0; m < Glv.cubeSizeReduced / Glv.cubeSize; m++) {
							for (int n = 0; n < Glv.cubeSizeReduced / Glv.cubeSize; n++) {
								if ((i + m) >= 0 && (j + n) >= 0 && (i + m) < boxes.length
										&& (j + n) < boxes[i].length) {
									boxes[i + m][j + n].height = 0.0f;
								}
							}
						}
					} else {
						for (int m = 0; m < Glv.cubeSizeReduced / Glv.cubeSize; m++) {
							for (int n = 0; n < Glv.cubeSizeReduced / Glv.cubeSize; n++) {
								if ((i + m) >= 0 && (j + n) >= 0 && (i + m) < boxes.length
										&& (j + n) < boxes[i].length) {
									boxes[i + m][j + n].height = 1.0f;
								}
							}
						}
					}
				}
				l++;
			}
			k++;
		}
		/*
				for (int i = 0; i < Glv.threadNN.net.neuralnet.m_output_layer.length; i++) {
					for (int j = 0; j < Glv.threadNN.net.neuralnet.m_output_layer[i].length; j++) {
						if (i < boxes.length && j < boxes[i].length) {
							if (boxes[i][j] != null && Glv.threadNN.net.neuralnet.m_output_layer[i][j] != null)
								if (Glv.threadNN.net.neuralnet.m_output_layer[i][j].m_output < -0.5f) // If it's not really black then take it as down (0).
									boxes[i][j].height = 0.0f;
								else
									boxes[i][j].height = 1.0f;
						}
					}
				}
				*/

		dimReduction();
	}
	/*
	public float walls2(int i, int j) {
		int counter = 0;
	
		for (int k = -2; k < 3; k++) {
			for (int l = -2; l < 3; l++) {
				if ((i + k) >= 0 && (j + l) >= 0 && (i + k) < boxes.length && (j + l) < boxes[i].length) {
					if (boxes[i][j].neighbourSum > boxes[k + i][l + j].neighbourSum)
						counter++;
				}
			}
		}
	
		if (counter <= 3) 
			return 1.0f;
		else
			return 0.0f;
	
	//		
	//		 if (counter > 4) return 1.0f; else if (counter == 2) return
	//		 boxes[i][j].height; else return 0.0f;
	//		 
	} */
}
