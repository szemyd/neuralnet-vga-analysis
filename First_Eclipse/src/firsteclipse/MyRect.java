package firsteclipse;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class MyRect {
	private PApplet p;

	PVector position;
	ArrayList<MyRect> neighbourhood = new ArrayList<MyRect>();
	float height = 0.0f;

	public MyRect(PApplet _p, PVector _position) {
		p = _p;

		position = _position;	
	}

	void draw() {
		
		// if (height > 0.1f) p.fill(360);
		// else
		p.fill(p.map(neighbourhood.size(), SpaceSyntax.highLow.y, SpaceSyntax.highLow.x, 230, 360), 360, 360);
	
		if (height < 0.1f) {
			p.pushMatrix();
			{
				// p.translate((Glv.roomSizeX/Glv.divisionX)*0.5f,
				// (Glv.roomSizeY/Glv.divisionY)*0.5f, 0); // Shift because
				// boxesare in center mode.
				p.translate(position.x, position.y, 0f);
				p.rect(0, 0, Glv.spaceCubeSize, Glv.spaceCubeSize);
				// p.box(Glv.roomSizeX/Glv.divisionX,
				// Glv.roomSizeY/Glv.divisionY, height*4.0f);
				// p.point(0, 0, 0);
			}
			p.popMatrix();
		}

	}
}
