package firsteclipse;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class MyBox {

	private PApplet p;
	public float height=0.0f;
	PVector position;
	float neighbourSum;
	ArrayList<MyBox> neighbourhood = new ArrayList<MyBox>();
	
	boolean amIEdge = true; // Check if after dimensionality reduction if the box is on the edge or not.

	public MyBox(PApplet _p, PVector _position) {
		p = _p;

		//height = _height;
		position = _position;
		p.noStroke();
		//p.strokeWeight(5f);
	}

	void draw() {
		
		if (height>0.1f) p.fill(360, 0, 360); // myFill = p.map(neighbourhood.size(),SpaceSyntax.highLow.y,SpaceSyntax.highLow.x,0,360);
		else p.fill (0,0,0); 

		p.pushMatrix();
		{
			//p.translate((Glv.roomSizeX/Glv.divisionX)*0.5f, (Glv.roomSizeY/Glv.divisionY)*0.5f, 0); // Shift because boxesare in center mode.
			p.translate(position.x, position.y, height*4.0f*0.5f);
			p.box(Glv.cubeSize, Glv.cubeSize, height*4.0f);
			//p.point(0, 0, 0);
		}
		p.popMatrix();
	}
}
