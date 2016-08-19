package firsteclipse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.sun.javafx.geom.Line2D;

import processing.core.PApplet;
import processing.core.PVector;

public class SpaceSyntax {
	private PApplet p;
	public PVector highLow = new PVector();

	public ArrayList<String> toNN = new ArrayList<String>();
	public float[][] values;

	int threadID;

	public MyRect[][] rectangles = new MyRect[Glv.spaceDivisionX][Glv.spaceDivisionY];

	public SpaceSyntax(PApplet _p, int _threadID) {
		p = _p;

		threadID = _threadID;
	}

	public void setup(MyBox[][] boxes) {

		for (int i = 0; i < rectangles.length; i++) {
			for (int j = 0; j < rectangles[i].length; j++) {
				PVector position = new PVector(
						(Glv.spaceCubeSize * i) - (Glv.spaceCubeSize * Glv.spaceDivisionX) * 0.5f
								+ Glv.spaceCubeSize * 0.5f,
						(Glv.spaceCubeSize * j) - (Glv.spaceCubeSize * Glv.spaceDivisionY) * 0.5f - Glv.spaceCubeSize,
						0);
				rectangles[i][j] = new MyRect(p, position, rectangles.length * i + j);

				// Set rectangles according to boxes.
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
				if (highLow != null && rectangles[i][j] != null)
					rectangles[i][j].draw(highLow);
			}
		}
	}

	public void drawResponded() {
		for (int i = 0; i < rectangles.length; i++) {
			for (int j = 0; j < rectangles[i].length; j++) {
				if (highLow != null && rectangles[i][j] != null)
					rectangles[i][j].drawResponded(highLow);
			}
		}
	}

	public void VGA(MyBox[][] boxes) {
		highLow = new PVector(0f, 1000f); // This is so that the colours are rightly mapped.
		//for vi in V(G) { for vj in V(G) if vi sees vj then add vj to V(T); } // Alasdiars PseudoCode.		 

		for (int i = 0; i < rectangles.length; i++) {
			for (int j = 0; j < rectangles[i].length; j++) {
				// Find if rectangle is inside a building or not!
				for (Building build : Environment.buildings) {
					if (build.myPolygon.xpoints[0] < Glv.spaceRoomSizeY) {
						boolean isItIn = build.myPolygon.contains((int) rectangles[i][j].position.x,
								(int) rectangles[i][j].position.y);

						if (isItIn) {
							rectangles[i][j].height = 1.0f;
						}
					}
				}
			}
		}

		for (int i = 0; i < rectangles.length; i++) {
			for (int j = 0; j < rectangles[i].length; j++) {
				for (int k = 0; k < rectangles.length; k++) {
					for (int l = 0; l < rectangles[k].length; l++) {
						if (Glv.shouldDimReduction) {
							if (canIseeBigCube(rectangles[i][j], rectangles[k][l], boxes)) { // Check big boxes.
								rectangles[i][j].neighbourhood.add(rectangles[k][l]);
							}
						} else {
							if (canIsee(rectangles[i][j], rectangles[k][l], boxes)) { // Check every box
								rectangles[i][j].neighbourhood.add(rectangles[k][l]);
							}
						}
					}
				}
				// p.println(boxes[i][j].neighbourhood.size());
				calcHighLow(rectangles[i][j].neighbourhood.size());
			}
		}
		float ellapsedTimeVGA = p.second() + p.minute() * 60f + p.hour() * 360f;
		if (Glv.shP)
			p.println("Ellapsed time VGA: " + ((p.second() + p.minute() * 60f + p.hour() * 360f) - ellapsedTimeVGA));

		// p.println("high: " + highLow.x + " | low: " + highLow.y);
		//meanShortestPath();

		save(boxes);
		//return true;
	}

	private boolean canIsee(MyRect me, MyRect other, MyBox[][] boxes) { // Check if the lines intersect

		if (me != null && other != null) {

			Line2D line1 = new Line2D(me.position.x, me.position.y, other.position.x, other.position.y);

			if (me.height < 0.1f && other.height < 0.1f) {

				for (int i = 0; i < boxes.length; i++) {
					for (int j = 0; j < boxes[i].length; j++) {

						if (boxes[i][j].height > 0.1f) { // Is the box up
							if (boxes[i][j].amIEdge) { // Is the box on the edge of the obstacle (because of Dim Reduction)
								if (boxes[i][j].position.x - Glv.cubeSize > me.position.x // Is the box surely not near any of the points?
										&& boxes[i][j].position.x - Glv.cubeSize > other.position.x
										|| boxes[i][j].position.x + Glv.cubeSize < me.position.x
												&& boxes[i][j].position.x + Glv.cubeSize < other.position.x) { // Is the box surely not near any of the points?
								} else if (boxes[i][j].position.y - Glv.cubeSize > me.position.y
										&& boxes[i][j].position.y - Glv.cubeSize > other.position.y
										|| boxes[i][j].position.y + Glv.cubeSize < me.position.y
												&& boxes[i][j].position.y + Glv.cubeSize < other.position.y) {

								} else {
									Line2D line2 = new Line2D(boxes[i][j].position.x + (Glv.cubeSize) * 0.5f,
											boxes[i][j].position.y + (Glv.cubeSize) * 0.5f,
											boxes[i][j].position.x - (Glv.cubeSize) * 0.5f,
											boxes[i][j].position.y - (Glv.cubeSize) * 0.5f);

									if (linesIntersect(line2.x1, line2.y1, line2.x2, line2.y2, line1.x1, line1.y1,
											line1.x2, line1.y2))
										return false;

									Line2D line3 = new Line2D(boxes[i][j].position.x - (Glv.cubeSize) * 0.5f,
											boxes[i][j].position.y + (Glv.cubeSize) * 0.5f,
											boxes[i][j].position.x + (Glv.cubeSize) * 0.5f,
											boxes[i][j].position.y - (Glv.cubeSize) * 0.5f);

									if (linesIntersect(line3.x1, line3.y1, line3.x2, line3.y2, line1.x1, line1.y1,
											line1.x2, line1.y2))
										return false;

								}
							}
						}
					}
				}

			} else if (me.height > 0.1f || other.height > 0.1f) {
				return false;
			}
			return true;
		}
		return false;

	}

	private boolean canIseeBigCube(MyRect me, MyRect other, MyBox[][] boxes) { // Check if the lines intersect

		if (me != null && other != null) {
			if (me.height < 0.1f && other.height < 0.1f) {

				for (int i = 0; i < boxes.length; i++) {
					for (int j = 0; j < boxes[i].length; j++) {

						if (boxes[i][j].height > 0.1f) { // Is the box up
							if (boxes[i][j].amIEdge) { // Is the box on the edge of the obstacle (because of Dim Reduction)
								if (boxes[i][j].position.x - Glv.cubeSize > me.position.x // Is the box surely not near any of the points?
										&& boxes[i][j].position.x - Glv.cubeSize > other.position.x
										|| boxes[i][j].position.x + Glv.cubeSize + Glv.cubeSizeReduced < me.position.x
												&& boxes[i][j].position.x + Glv.cubeSize
														+ Glv.cubeSizeReduced < other.position.x) { // Is the box surely not near any of the points?
								} else if (boxes[i][j].position.y - Glv.cubeSize > me.position.y
										&& boxes[i][j].position.y - Glv.cubeSize > other.position.y
										|| boxes[i][j].position.y + Glv.cubeSize + Glv.cubeSizeReduced < me.position.y
												&& boxes[i][j].position.y + Glv.cubeSize
														+ Glv.cubeSizeReduced < other.position.y) {

								} else {
									Line2D line1 = new Line2D(me.position.x, me.position.y, other.position.x,
											other.position.y);

									Line2D line2 = new Line2D(boxes[i][j].position.x - (Glv.cubeSize) * 0.5f,
											boxes[i][j].position.y + (Glv.cubeSize) * 0.5f,
											boxes[i][j].position.x + (Glv.cubeSize) * 0.5f + Glv.cubeSizeReduced,
											boxes[i][j].position.y - (Glv.cubeSize) * 0.5f + Glv.cubeSizeReduced);

									if (linesIntersect(line2.x1, line2.y1, line2.x2, line2.y2, line1.x1, line1.y1,
											line1.x2, line1.y2))
										return false;

									Line2D line3 = new Line2D(
											boxes[i][j].position.x + (Glv.cubeSize) * 0.5f + Glv.cubeSizeReduced,
											boxes[i][j].position.y + (Glv.cubeSize) * 0.5f,
											boxes[i][j].position.x - (Glv.cubeSize) * 0.5f,
											boxes[i][j].position.y - (Glv.cubeSize) * 0.5f + Glv.cubeSizeReduced);

									if (linesIntersect(line3.x1, line3.y1, line3.x2, line3.y2, line1.x1, line1.y1,
											line1.x2, line1.y2))
										return false;

								}
							}
						}
					}
				}

			} else if (me.height > 0.1f || other.height > 0.1f) {
				return false;
			}
			return true;
		}
		return false;

	}

	private boolean buildingSee(PVector me, PVector other) { // Check if the lines intersect

		Line2D line1 = new Line2D(me.x, me.y, other.x, other.y);

		for (int i = 0; i < Environment.buildings.size() - 1; i++) {
			Building tempBuilding = Environment.buildings.get(i);

			for (int j = 0; j < tempBuilding.bLines.size() - 1; j++) {

				Line2D line2 = new Line2D(tempBuilding.bLines.get(j).x, tempBuilding.bLines.get(j).y,
						tempBuilding.bLines.get((j + 1) % (tempBuilding.bLines.size() - 1)).x,
						tempBuilding.bLines.get((j + 1) % (tempBuilding.bLines.size() - 1)).y);

				if (linesIntersect(line2.x1, line2.y1, line2.x2, line2.y2, line1.x1, line1.y1, line1.x2, line1.y2))
					return false;
			}
		}
		return true;
	}

	private boolean linesIntersect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
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

	private void calcHighLow(float num) { // Calculate which number is the highest and which one is the lowest.

		// Calculate local highLow, meaning find the local highest visibility and the lowest
		if (num < 10000f) {
			if (num >= Glv.highLow.x)
				Glv.highLow.x = num;
			if (num <= Glv.highLow.y)
				Glv.highLow.y = num;
		}

		// Calculate the global highLow, meaning find the global highest visibility and the lowest
		if (num < 10000f) {
			if (num >= highLow.x)
				highLow.x = num;
			if (num <= highLow.y)
				highLow.y = num;
		}

	}

	public void save(MyBox[][] boxes) { // Saves the calculated information into an ArrayList of Strings.

		values = new float[rectangles.length][rectangles[0].length];

		for (int i = 0; i < rectangles.length; i++) {
			for (int j = 0; j < rectangles[i].length; j++) {
				values[i][j] = rectangles[i][j].neighbourhood.size();
			}
		}

		toNN.add(Integer.toString(threadID + Glv.initialSeed));
		toNN.add("\n");

		for (int j = 0; j < rectangles[0].length; j++) {
			for (int i = 0; i < rectangles.length; i++) {
				toNN.add(Integer.toString(rectangles[i][j].neighbourhood.size()));
				toNN.add(",");
			}
			toNN.add("\n");
		}
		toNN.add(":");
		toNN.add("\n");

		for (int j = 0; j < boxes[0].length; j++) {
			for (int i = 0; i < boxes.length; i++) {
				toNN.add(Integer.toString(boxes[i][j].height.intValue()));//.intValue());
				toNN.add(",");
			}
			toNN.add("\n");
		}

		toNN.add("_");
		toNN.add("\n");

		if (Glv.shP)
			p.println("I have finished saving to string");
	}

	public void meanShortestPath() {
		int adjacency_matrix[][];
		int number_of_vertices = 5; //rectangles.length * rectangles[0].length;
		int source = 1;

		//adjacency_matrix = new int[number_of_vertices + 1][number_of_vertices + 1];
		//int number_of_vertices = rectangles.length * rectangles[0].length;

		//adjacency_matrix = getGraph(number_of_vertices);

		// Source is the vertex that is being checked!!!

	
		
		
		adjacency_matrix= new int[number_of_vertices+1][number_of_vertices+1];
		for (int i = 0; i < adjacency_matrix.length; i++) {
			for (int j = 0; j < adjacency_matrix[i].length; j++) {
				adjacency_matrix[i][j]=0;
			}
		}
//		adjacency_matrix[2][1]=9;
//		adjacency_matrix[3][1]=6;
//		adjacency_matrix[4][1]=5;
//		adjacency_matrix[5][1]=3;
//		adjacency_matrix[2][3]=2;
//		adjacency_matrix[4][3]=4;
		
		
		
		
		adjacency_matrix[1][2]=9;
		adjacency_matrix[1][3]=6;
		adjacency_matrix[1][4]=5;
		adjacency_matrix[1][5]=3;
		adjacency_matrix[3][2]=2;
		adjacency_matrix[3][4]=4;
		
		for (int i = 1; i < adjacency_matrix.length; i++) {
			for (int j = 1; j < adjacency_matrix[i].length; j++) {
				p.print(adjacency_matrix[i][j]);
			}
			p.println("");
		}
		
		
		DijkstraAlgorithmSet dijkstrasAlgorithm = new DijkstraAlgorithmSet(number_of_vertices);
		dijkstrasAlgorithm.dijkstra_algorithm(adjacency_matrix, source);
		
		 for (int i = 1; i <= dijkstrasAlgorithm.distances.length - 1; i++)
         {
             System.out.println(source + " to " + i + " is "+ dijkstrasAlgorithm.distances[i]);
         }

	//	rectangles[0][0].shortestPath = 
		p.println(rectangles[0][0].shortestPath);
		//rectangles[0][0].calcAvarage(number_of_vertices);
		
		/*
		for (int i = 0; i < rectangles.length; i++) {
			for (int j = 0; j < rectangles[i].length; j++) {
				//array[rectangles.length * i + j] = value;  
				source =  i* rectangles[0].length + j  ;
				rectangles[i][j].shortestPath = new int[number_of_vertices];
				
				rectangles[i][j].shortestPath = dijkstrasAlgorithm.dijkstra_algorithm(adjacency_matrix, source);
				rectangles[i][j].calcAvarage(number_of_vertices);
			}
		}
		*/

		//			System.out.println("The Shorted Path to all nodes are ");
		//			for (int i = 1; i <= dijkstrasAlgorithm.distances.length - 1; i++) {
		//				System.out.println(source + " to " + i + " is " + dijkstrasAlgorithm.distances[i]);
		//			}

	}

	private int[][] getGraph(int number_of_vertices) {

		int[][] adjacency_matrix = new int[number_of_vertices][number_of_vertices];

		for (int i = 0; i < rectangles.length; i++) {
			for (int j = 0; j < rectangles[i].length; j++) {
				int k = 0;
				for (MyRect rect : rectangles[i][j].neighbourhood) {
					if(rect.iD != null) adjacency_matrix[i* rectangles[0].length + j ][rect.iD] = 1;
					k++;
				}

			}
		}

		return adjacency_matrix;
	}

}

/*	
	private static boolean canIsee(MyRect me, MyRect other, MyBox[][] boxes) { // Check if the lines intersect

		// Rectangle r1 = new Rectangle(100, 100, 100, 100);
		// Line2D l1 = new Line2D(0, 200, 200, 0);
		// System.out.println("l1.intsects(r1) = " + l1.intersects(r1));
		//
		if (me != null && other != null) {

			Line2D line1 = new Line2D(me.position.x, me.position.y, other.position.x, other.position.y);

			if (me.height < 0.1f && other.height < 0.1f) {

				for (int i = 0; i < boxes.length; i++) {
					for (int j = 0; j < boxes[i].length; j++) {

						if (boxes[i][j].height > 0.1f) {
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

						}
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
						// }
					}
				}

			} else if (me.height > 0.1f) {
				return false;
			}

			return true;
		}
		return false;
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
*/
