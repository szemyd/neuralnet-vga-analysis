package firsteclipse;

import com.sun.javafx.geom.Vec3d;

import processing.core.PApplet;
import processing.core.PVector;

public class MyBox {

	private PApplet p;
	private float height;
	PVector position;

	public MyBox(PApplet _p, float _height, PVector _position) {
		p = _p;

		height = _height;
		position = _position;
		p.strokeWeight(5f);
	}

	void draw() {
		p.stroke(p.map(height, 0, 360, 0, 360) * 3f, 180, 360);

		p.pushMatrix();
		{
			
			p.translate(position.x, position.y, position.z);
			p.box(Glv.roomSize/Glv.division, Glv.roomSize/Glv.division, height);
			p.point(0, 0, 0);
		}
		p.popMatrix();
	}

}
