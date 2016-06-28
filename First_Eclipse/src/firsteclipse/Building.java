package firsteclipse;

import java.awt.Polygon;
import java.awt.Stroke;
import java.util.ArrayList;

import com.jogamp.common.util.cache.TempCacheReg;

import processing.core.PApplet;
import processing.core.PVector;

public class Building {

	private  PApplet p;
	public ArrayList<PVector> bLines = new ArrayList<PVector>();
	public Polygon myPolygon =new Polygon();

	public Building(PApplet _p, PVector dataIn) {
		p = _p;

		bLines.add(dataIn);
		myPolygon.addPoint((int)dataIn.x,(int) dataIn.y);
	}

	public void draw() {
			for (int i = 0; i < bLines.size() - 1; i++) {
				PVector me = bLines.get(i % (bLines.size()));
				PVector next = bLines.get((i + 1) % (bLines.size()));
						
				//p.println("me: " + me + " | next: " + next);
				//p.line((float)me.x, (float)me.y, 0.0f,(float)next.x, (float)next.y,0.0f);
			}
	
	}

}