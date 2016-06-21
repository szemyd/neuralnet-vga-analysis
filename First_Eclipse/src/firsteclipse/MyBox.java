package firsteclipse;

import processing.core.PApplet;
import processing.core.PVector;

public class MyBox {

	private PApplet p;
	public float height;
	PVector position;
	float neighbourSum;

	public MyBox(PApplet _p, PVector _position) {
		p = _p;

		//height = _height;
		position = _position;
		p.noStroke();
		//p.strokeWeight(5f);
	}

	void draw() {
		//p.fill(p.map(height, 0, 360, 0, 360) * 3f, 180, 360);
		p.fill(360, height*110, height/4.0f * 75 + 250);

		p.pushMatrix();
		{
			p.translate(position.x, position.y, height*4.0f*0.5f);
			p.box(Glv.roomSizeX/Glv.divisionX, Glv.roomSizeY/Glv.divisionY, height*4.0f);
			//p.point(0, 0, 0);
		}
		p.popMatrix();
	}
}
