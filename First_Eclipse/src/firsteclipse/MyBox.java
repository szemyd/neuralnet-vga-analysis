package firsteclipse;

import processing.core.PApplet;
import processing.core.PVector;

public class MyBox {

	private PApplet p;
	private float height;
	PVector position;
	float neighbourSum;

	public MyBox(PApplet _p, float _height, PVector _position) {
		p = _p;

		height = _height*1.5f;
		position = _position;
		p.noStroke();
		//p.strokeWeight(5f);
	}

	void draw() {
		//p.fill(p.map(height, 0, 360, 0, 360) * 3f, 180, 360);
		p.fill(height/1.5f * 20 + 250, 220, 290);

		p.pushMatrix();
		{
			p.translate(position.x, position.y, height*0.5f);
			p.box(Glv.roomSize/Glv.division, Glv.roomSize/Glv.division, height);
			//p.point(0, 0, 0);
		}
		p.popMatrix();
	}
}
