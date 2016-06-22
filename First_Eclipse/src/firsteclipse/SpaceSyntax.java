package firsteclipse;

import com.jogamp.nativewindow.util.Rectangle;
import com.sun.java_cup.internal.runtime.virtual_parse_stack;
import com.sun.javafx.geom.AreaOp.AddOp;
import com.sun.javafx.geom.Line2D;

import oracle.jrockit.jfr.VMJFR;
import processing.core.PApplet;

public class SpaceSyntax {
	private PApplet p;

	public SpaceSyntax(PApplet _p) {
		p = _p;
	}

	public void setup(MyBox[][] boxes) {
		/*
		 * for vi in V(G) { for vj in V(G) if vi sees vj then add vj to V(T); }
		 */
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				for (int k = 0; k < boxes.length; k++) {
					for (int l = 0; l < boxes[k].length; l++) {
						if (canIsee(boxes[i][j], boxes[k][l], boxes)) {
							boxes[i][j].neighbourhood.add(boxes[k][l]);
						}
					}
				}
			}
		}
	}

	private boolean canIsee(MyBox me, MyBox other, MyBox[][] boxes) {

		// Rectangle r1 = new Rectangle(100, 100, 100, 100);
		// Line2D l1 = new Line2D(0, 200, 200, 0);
		// System.out.println("l1.intsects(r1) = " + l1.intersects(r1));
		//
		boolean result = false;

		Line2D line1 = new Line2D(me.position.x, me.position.y, other.position.x, other.position.y);

		if (me.height < 0.1f && other.height < 0.1f) {

			for (int i = 0; i < boxes.length; i++) {
				for (int j = 0; j < boxes[i].length; j++) {

					if (boxes[i][j].height > 0.1f) {
						if (boxes[i][j] != me && boxes[i][j] != other) {

							if (boxes[i][j].height > 0.1f) {
								Line2D line2 = new Line2D(
										boxes[i][j].position.x + (Glv.roomSizeX / Glv.divisionX) * 0.5f,
										boxes[i][j].position.y + (Glv.roomSizeY / Glv.divisionY) * 0.5f,
										boxes[i][j].position.x + (Glv.roomSizeX / Glv.divisionX) * 0.5f,
										boxes[i][j].position.y - (Glv.roomSizeY / Glv.divisionY) * 0.5f);
								Line2D line3 = new Line2D(
										boxes[i][j].position.x + (Glv.roomSizeX / Glv.divisionX) * 0.5f,
										boxes[i][j].position.y - (Glv.roomSizeY / Glv.divisionY) * 0.5f,
										boxes[i][j].position.x - (Glv.roomSizeX / Glv.divisionX) * 0.5f,
										boxes[i][j].position.y - (Glv.roomSizeY / Glv.divisionY) * 0.5f);
								Line2D line4 = new Line2D(
										boxes[i][j].position.x - (Glv.roomSizeX / Glv.divisionX) * 0.5f,
										boxes[i][j].position.y - (Glv.roomSizeY / Glv.divisionY) * 0.5f,
										boxes[i][j].position.x - (Glv.roomSizeX / Glv.divisionX) * 0.5f,
										boxes[i][j].position.y + (Glv.roomSizeY / Glv.divisionY) * 0.5f);
								Line2D line5 = new Line2D(
										boxes[i][j].position.x - (Glv.roomSizeX / Glv.divisionX) * 0.5f,
										boxes[i][j].position.y + (Glv.roomSizeY / Glv.divisionY) * 0.5f,
										boxes[i][j].position.x + (Glv.roomSizeX / Glv.divisionX) * 0.5f,
										boxes[i][j].position.y + (Glv.roomSizeY / Glv.divisionY) * 0.5f);

								//
								// Line2D line3 = new Line2D(150, 150, 150,
								// 200);
								// Line2D line4 = new Line2D(150, 150, 150,
								// 200);
								// Line2D line5 = new Line2D(150, 150, 150,
								// 200);

								if (line2.intersectsLine(line1))
									result = true;
								if (line3.intersectsLine(line1))
									result = true;
								if (line4.intersectsLine(line1))
									result = true;
								if (line5.intersectsLine(line1))
									result = true;
							}
						}
					}
				}
			}
			return result;
		}

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

		return result;

	}

	public void draw() {

	}
}
