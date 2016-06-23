package firsteclipse;

import java.awt.Stroke;
import java.util.ArrayList;

import com.jogamp.common.util.cache.TempCacheReg;

import processing.core.PApplet;
import processing.core.PVector;

public class Building {

	private PApplet p;
	public ArrayList<PVector> bLines = new ArrayList<PVector>();

	public Building(PApplet _p, PVector dataIn) {
		p = _p;

		bLines.add(dataIn);
	}

	public void draw() {

	
			for (int i = 0; i < bLines.size() - 1; i++) {
				PVector me = bLines.get(i % (bLines.size() - 1));
				PVector next = bLines.get((i + 1) % (bLines.size() - 1));

				p.line(me.x, me.y, 0,next.x, next.y,0);
			}
	
	}

}