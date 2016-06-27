package firsteclipse;

import com.jogamp.nativewindow.util.Rectangle;
import com.sun.java_cup.internal.runtime.virtual_parse_stack;
import com.sun.javafx.geom.AreaOp.AddOp;
import com.sun.javafx.css.CalculatedValue;
import com.sun.javafx.geom.Line2D;
import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithmState;

import processing.core.PApplet;
import processing.core.PVector;

public class SpaceSyntax {
	private static PApplet p;
	public static PVector highLow = new PVector();

	public static MyRect[][] rectangles = new MyRect[Glv.spaceDivisionX][Glv.spaceDivisionY];

	public SpaceSyntax(PApplet _p) {
		p = _p;
	}

	public void setup(MyBox[][] boxes) {

		for (int i = 0; i < rectangles.length; i++) {
			for (int j = 0; j < rectangles[i].length; j++) {
				PVector position = new PVector(
						(Glv.spaceRoomSizeX / Glv.spaceDivisionX * i) - (Glv.spaceRoomSizeX) * 0.5f
								+ (Glv.spaceRoomSizeX / Glv.spaceDivisionX) * 0.5f,
						(Glv.spaceRoomSizeY / Glv.spaceDivisionY * j) - (Glv.spaceRoomSizeY) * 0.5f
								+ (Glv.spaceRoomSizeY / Glv.spaceDivisionY) * 0.5f,
						0);
				rectangles[i][j] = new MyRect(p, position);

				for (int k = 0; k < boxes.length; k++) {
					for (int l = 0; l < boxes[k].length; l++) {
						if (boxes[k][l].position.x - Glv.cubeSize * 0.5f < rectangles[i][j].position.x
								&& boxes[k][l].position.x + Glv.cubeSize * 0.5f > rectangles[i][j].position.x
								&& boxes[k][l].position.y - Glv.cubeSize * 0.5f < rectangles[i][j].position.y
								&& boxes[k][l].position.y + Glv.cubeSize * 0.5f > rectangles[i][j].position.y) {
							rectangles[i][j].height = boxes[k][l].height;
						}
					}
				}
			}
		}
	}

	public void draw() {
		for (int i = 0; i < rectangles.length; i++) {
			for (int j = 0; j < rectangles[i].length; j++) {
				rectangles[i][j].draw();
			}
		}
	}

	public void VGA(MyBox[][] boxes) {
		highLow = new PVector(0f, 1000f); // This is so that the colours are
		// rightly mapped
		/*
		 * for vi in V(G) { for vj in V(G) if vi sees vj then add vj to V(T); }
		 */
		for (int i = 0; i < rectangles.length; i++) {
			for (int j = 0; j < rectangles[i].length; j++) {
				for (int k = 0; k < rectangles.length; k++) {
					for (int l = 0; l < rectangles[k].length; l++) {
						if (canIsee(rectangles[i][j], rectangles[k][l], boxes)
								&& canIsee2(rectangles[i][j], rectangles[k][l])) {
							//p.println("this is true");
							rectangles[i][j].neighbourhood.add(rectangles[k][l]);
						}
					}
				}
				// p.println(boxes[i][j].neighbourhood.size());
				calcHighLow(rectangles[i][j].neighbourhood.size());
			}
		}
		// p.println("high: " + highLow.x + " | low: " + highLow.y);
	}

	private static boolean canIsee(MyRect me, MyRect other, MyBox[][] boxes) { // Check
																				// if
																				// the
																				// lines
																				// intersect

		// Rectangle r1 = new Rectangle(100, 100, 100, 100);
		// Line2D l1 = new Line2D(0, 200, 200, 0);
		// System.out.println("l1.intsects(r1) = " + l1.intersects(r1));
		//

		Line2D line1 = new Line2D(me.position.x, me.position.y, other.position.x, other.position.y);

		if (me.height < 0.1f && other.height < 0.1f) {

			for (int i = 0; i < boxes.length; i++) {
				for (int j = 0; j < boxes[i].length; j++) {

					// if (rectangles[i][j].height > 0.1f) {
					//if (rectangles[i][j] != me && rectangles[i][j] != other) {

						// if (boxes[i][j].height > 0.1f) {
						Line2D line2 = new Line2D(boxes[i][j].position.x + (Glv.cubeSize) * 0.5f,
								boxes[i][j].position.y + (Glv.cubeSize) * 0.5f,
								boxes[i][j].position.x + (Glv.cubeSize) * 0.5f,
								boxes[i][j].position.y - (Glv.cubeSize) * 0.5f);

						if (linesIntersect(line2.x1, line2.y1, line2.x2, line2.y2, line1.x1, line1.y1, line1.x2,
								line1.y2))
							return false;

						Line2D line3 = new Line2D(boxes[i][j].position.x + (Glv.cubeSize) * 0.5f,
								boxes[i][j].position.y - (Glv.cubeSize) * 0.5f,
								boxes[i][j].position.x - (Glv.cubeSize) * 0.5f,
								boxes[i][j].position.y - (Glv.cubeSize) * 0.5f);

						if (linesIntersect(line3.x1, line3.y1, line3.x2, line3.y2, line1.x1, line1.y1, line1.x2,
								line1.y2))
							return false;

						Line2D line4 = new Line2D(boxes[i][j].position.x - (Glv.cubeSize) * 0.5f,
								boxes[i][j].position.y - (Glv.cubeSize) * 0.5f,
								boxes[i][j].position.x - (Glv.cubeSize) * 0.5f,
								boxes[i][j].position.y + (Glv.cubeSize) * 0.5f);

						if (linesIntersect(line4.x1, line4.y1, line4.x2, line4.y2, line1.x1, line1.y1, line1.x2,
								line1.y2))
							return false;

						Line2D line5 = new Line2D(boxes[i][j].position.x - (Glv.cubeSize) * 0.5f,
								boxes[i][j].position.y + (Glv.cubeSize) * 0.5f,
								boxes[i][j].position.x + (Glv.cubeSize) * 0.5f,
								boxes[i][j].position.y + (Glv.cubeSize) * 0.5f);

						if (linesIntersect(line5.x1, line5.y1, line5.x2, line5.y2, line1.x1, line1.y1, line1.x2,
								line1.y2))
							return false;

						//
						// Line2D line3 = new Line2D(150, 150, 150,
						// 200);
						// Line2D line4 = new Line2D(150, 150, 150,
						// 200);
						// Line2D line5 = new Line2D(150, 150, 150,
						// 200);

						// if (line2.intersectsLine(line1))
						// result = true;
						// if (line3.intersectsLine(line1))
						// result = true;
						// if (line4.intersectsLine(line1))
						// result = true;
						// if (line5.intersectsLine(line1))
						// result = true;
						// }
						// }
					//}
				}
			}

		}
		else if(me.height > 0.1f) {
			return false;
		}

		return true;
		//
		// Line2D line1 = new Line2D(100, 100, 200, 200);
		// Line2D line2 = new Line2D(150, 150, 150, 200);
		// boolean result = line2.intersectsLine(line1);
		// System.out.println(result); // => true

		// Also check out linesIntersect() if you do not need to construct the
		// line objects
		// It will probably be faster due to putting less pressure on the
		// garbage collector
		// if running it in a loop
		// System.out.println(Line2D.linesIntersect(100,100,200,200,150,150,150,200));

	}

	private static boolean canIsee2(MyRect me, MyRect other) { // Check if the
																// lines
																// intersect

		// Rectangle r1 = new Rectangle(100, 100, 100, 100);
		// Line2D l1 = new Line2D(0, 200, 200, 0);
		// System.out.println("l1.intsects(r1) = " + l1.intersects(r1));
		//

		Line2D line1 = new Line2D(me.position.x, me.position.y, other.position.x, other.position.y);

		for (int i = 0; i < Environment.buildings.size()-1; i++) {
			Building tempBuilding = Environment.buildings.get(i);

			for (int j = 0; j < tempBuilding.bLines.size()-1; j++) {

				Line2D line2 = new Line2D(tempBuilding.bLines.get(j).x, 
						tempBuilding.bLines.get(j).y,
						tempBuilding.bLines.get((j + 1) % (tempBuilding.bLines.size()-1)).x,
						tempBuilding.bLines.get((j + 1) % (tempBuilding.bLines.size()-1)).y);

				if (linesIntersect(line2.x1, line2.y1, line2.x2, line2.y2, line1.x1, line1.y1, line1.x2, line1.y2))
					return false;

				// Line2D line3 = new Line2D(
				// rectangles[i][j].position.x + (Glv.roomSizeX / Glv.divisionX)
				// * 0.5f,
				// rectangles[i][j].position.y - (Glv.roomSizeY / Glv.divisionY)
				// * 0.5f,
				// rectangles[i][j].position.x - (Glv.cubeSize)
				// * 0.5f,
				// rectangles[i][j].position.y - (Glv.roomSizeY / Glv.divisionY)
				// * 0.5f);
				//
				// if (linesIntersect(line3.x1, line3.y1, line3.x2, line3.y2,
				// line1.x1, line1.y1, line1.x2,
				// line1.y2))
				// return false;
				//
				// Line2D line4 = new Line2D(
				// rectangles[i][j].position.x - (Glv.roomSizeX / Glv.divisionX)
				// * 0.5f,
				// rectangles[i][j].position.y - (Glv.roomSizeY / Glv.divisionY)
				// * 0.5f,
				// rectangles[i][j].position.x - (Glv.roomSizeX / Glv.divisionX)
				// * 0.5f,
				// rectangles[i][j].position.y + (Glv.roomSizeY / Glv.divisionY)
				// * 0.5f);
				//
				// if (linesIntersect(line4.x1, line4.y1, line4.x2, line4.y2,
				// line1.x1, line1.y1, line1.x2,
				// line1.y2))
				// return false;
				//
				// Line2D line5 = new Line2D(
				// rectangles[i][j].position.x - (Glv.roomSizeX / Glv.divisionX)
				// * 0.5f,
				// rectangles[i][j].position.y + (Glv.roomSizeY / Glv.divisionY)
				// * 0.5f,
				// rectangles[i][j].position.x + (Glv.roomSizeX / Glv.divisionX)
				// * 0.5f,
				// rectangles[i][j].position.y + (Glv.roomSizeY / Glv.divisionY)
				// * 0.5f);
				//
				// if (linesIntersect(line5.x1, line5.y1, line5.x2, line5.y2,
				// line1.x1, line1.y1, line1.x2,
				// line1.y2))

			}
		}
		return true;
	}

	private static boolean linesIntersect(float x1, float y1, float x2, float y2, float x3, float y3, float x4,
			float y4) {
		// Return false if either of the lines have zero length
		if (x1 == x2 && y1 == y2 || x3 == x4 && y3 == y4) {
			return false;
		}
		// Fastest method, based on Franklin Antonio's "Faster Line Segment
		// Intersection" topic "in Graphics Gems III" book
		// (http://www.graphicsgems.org/)
		double ax = x2 - x1;
		double ay = y2 - y1;
		double bx = x3 - x4;
		double by = y3 - y4;
		double cx = x1 - x3;
		double cy = y1 - y3;

		double alphaNumerator = by * cx - bx * cy;
		double commonDenominator = ay * bx - ax * by;
		if (commonDenominator > 0) {
			if (alphaNumerator < 0 || alphaNumerator > commonDenominator) {
				return false;
			}
		} else if (commonDenominator < 0) {
			if (alphaNumerator > 0 || alphaNumerator < commonDenominator) {
				return false;
			}
		}
		double betaNumerator = ax * cy - ay * cx;
		if (commonDenominator > 0) {
			if (betaNumerator < 0 || betaNumerator > commonDenominator) {
				return false;
			}
		} else if (commonDenominator < 0) {
			if (betaNumerator > 0 || betaNumerator < commonDenominator) {
				return false;
			}
		}
		if (commonDenominator == 0) {
			// This code wasn't in Franklin Antonio's method. It was added by
			// Keith Woodward.
			// The lines are parallel.
			// Check if they're collinear.
			double y3LessY1 = y3 - y1;
			double collinearityTestForP3 = x1 * (y2 - y3) + x2 * (y3LessY1) + x3 * (y1 - y2); // see
																								// http://mathworld.wolfram.com/Collinear.html
			// If p3 is collinear with p1 and p2 then p4 will also be collinear,
			// since p1-p2 is parallel with p3-p4
			if (collinearityTestForP3 == 0) {
				// The lines are collinear. Now check if they overlap.
				if (x1 >= x3 && x1 <= x4 || x1 <= x3 && x1 >= x4 || x2 >= x3 && x2 <= x4 || x2 <= x3 && x2 >= x4
						|| x3 >= x1 && x3 <= x2 || x3 <= x1 && x3 >= x2) {
					if (y1 >= y3 && y1 <= y4 || y1 <= y3 && y1 >= y4 || y2 >= y3 && y2 <= y4 || y2 <= y3 && y2 >= y4
							|| y3 >= y1 && y3 <= y2 || y3 <= y1 && y3 >= y2) {
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}

	private static void calcHighLow(float num) { // Calculate which number is
													// the highest and which one
													// is the lowest.
		if (num < 700f) {
			if (num >= highLow.x)
				highLow.x = num;
			if (num <= highLow.y)
				highLow.y = num;
		}
	}
}

// //if (me.height < 0.1f && other.height < 0.1f) {
//
// for (int i = 0; i < rectangles.length; i++) {
// for (int j = 0; j < rectangles[i].length; j++) {
//
// if (rectangles[i][j].height > 0.1f) {
// if (rectangles[i][j] != me && rectangles[i][j] != other) {
//
// if (boxes[i][j].height > 0.1f) {
// Line2D line2 = new Line2D(
// boxes[i][j].position.x + (Glv.roomSizeX / Glv.divisionX) * 0.5f,
// boxes[i][j].position.y + (Glv.roomSizeY / Glv.divisionY) * 0.5f,
// boxes[i][j].position.x + (Glv.roomSizeX / Glv.divisionX) * 0.5f,
// boxes[i][j].position.y - (Glv.roomSizeY / Glv.divisionY) * 0.5f);
//
// if (linesIntersect(line2.x1, line2.y1, line2.x2, line2.y2, line1.x1,
// line1.y1, line1.x2,
// line1.y2))
// return false;
//
// Line2D line3 = new Line2D(
// boxes[i][j].position.x + (Glv.roomSizeX / Glv.divisionX) * 0.5f,
// boxes[i][j].position.y - (Glv.roomSizeY / Glv.divisionY) * 0.5f,
// boxes[i][j].position.x - (Glv.roomSizeX / Glv.divisionX) * 0.5f,
// boxes[i][j].position.y - (Glv.roomSizeY / Glv.divisionY) * 0.5f);
//
// if (linesIntersect(line3.x1, line3.y1, line3.x2, line3.y2, line1.x1,
// line1.y1, line1.x2,
// line1.y2))
// return false;
//
// Line2D line4 = new Line2D(
// boxes[i][j].position.x - (Glv.roomSizeX / Glv.divisionX) * 0.5f,
// boxes[i][j].position.y - (Glv.roomSizeY / Glv.divisionY) * 0.5f,
// boxes[i][j].position.x - (Glv.roomSizeX / Glv.divisionX) * 0.5f,
// boxes[i][j].position.y + (Glv.roomSizeY / Glv.divisionY) * 0.5f);
//
// if (linesIntersect(line4.x1, line4.y1, line4.x2, line4.y2, line1.x1,
// line1.y1, line1.x2,
// line1.y2))
// return false;
//
// Line2D line5 = new Line2D(
// boxes[i][j].position.x - (Glv.roomSizeX / Glv.divisionX) * 0.5f,
// boxes[i][j].position.y + (Glv.roomSizeY / Glv.divisionY) * 0.5f,
// boxes[i][j].position.x + (Glv.roomSizeX / Glv.divisionX) * 0.5f,
// boxes[i][j].position.y + (Glv.roomSizeY / Glv.divisionY) * 0.5f);
//
// if (linesIntersect(line5.x1, line5.y1, line5.x2, line5.y2, line1.x1,
// line1.y1, line1.x2,
// line1.y2))
// return false;
//
// //
// // Line2D line3 = new Line2D(150, 150, 150,
// // 200);
// // Line2D line4 = new Line2D(150, 150, 150,
// // 200);
// // Line2D line5 = new Line2D(150, 150, 150,
// // 200);
//
// // if (line2.intersectsLine(line1))
// // result = true;
// // if (line3.intersectsLine(line1))
// // result = true;
// // if (line4.intersectsLine(line1))
// // result = true;
// // if (line5.intersectsLine(line1))
// // result = true;
// }
// }
// }
// }
// }
//
// }return true;
////
//// Line2D line1 = new Line2D(100, 100, 200, 200);
//// Line2D line2 = new Line2D(150, 150, 150, 200);
//// boolean result = line2.intersectsLine(line1);
//// System.out.println(result); // => true
//
//// Also check out linesIntersect() if you do not need to construct the
//// line objects
//// It will probably be faster due to putting less pressure on the
//// garbage collector
//// if running it in a loop
//// System.out.println(Line2D.linesIntersect(100,100,200,200,150,150,150,200));
//
// }
