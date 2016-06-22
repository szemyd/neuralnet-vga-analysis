package firsteclipse;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class MyBox {

	private PApplet p;
	public float height;
	PVector position;
	float neighbourSum;
	ArrayList<MyBox> neighbourhood = new ArrayList<MyBox>();

	public MyBox(PApplet _p, PVector _position) {
		p = _p;

		//height = _height;
		position = _position;
		p.noStroke();
		//p.strokeWeight(5f);
	}

	void draw() {
		//p.fill(p.map(height, 0, 360, 0, 360) * 3f, 180, 360);
		p.fill(360, height*110, neighbourhood.size() + 250);

		p.pushMatrix();
		{
			//p.translate((Glv.roomSizeX/Glv.divisionX)*0.5f, (Glv.roomSizeY/Glv.divisionY)*0.5f, 0); // Shift because boxesare in center mode.
			p.translate(position.x, position.y, height*4.0f*0.5f);
			p.box(Glv.roomSizeX/Glv.divisionX, Glv.roomSizeY/Glv.divisionY, height*4.0f);
			//p.point(0, 0, 0);
		}
		p.popMatrix();
	}
}
