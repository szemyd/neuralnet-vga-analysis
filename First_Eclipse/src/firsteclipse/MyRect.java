package firsteclipse;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class MyRect {
	private  PApplet p;

	PVector position;
	ArrayList<MyRect> neighbourhood = new ArrayList<MyRect>();
	float height=0.0f;

	public MyRect(PApplet _p, PVector _position) {
		p = _p;

		//height = _height;
		position = _position;
		//p.noStroke();
		//p.strokeWeight(5f);
	}

	void draw() {
		//p.fill(p.map(height, 0, 360, 0, 360) * 3f, 180, 360);
		//float myFill = 0;
		//p.println(p.map(neighbourhood.size(),SpaceSyntax.highLow.y,SpaceSyntax.highLow.x,0,230));
		//if (height>0.1f) p.fill(360, 0, 110);// myFill = p.map(neighbourhood.size(),SpaceSyntax.highLow.y,SpaceSyntax.highLow.x,0,360);
		//else 
		p.println(neighbourhood.size());
			p.fill(p.map(neighbourhood.size(),SpaceSyntax.highLow.y,SpaceSyntax.highLow.x,230,360), 360, 360); 
		//myFill = p.map(neighbourhood.size(),SpaceSyntax.highLow.y,SpaceSyntax.highLow.x,110,360);
		//float myFill = p.map(neighbourhood.size(),SpaceSyntax.highLow.y,SpaceSyntax.highLow.x,height,360);
		//p.println(myFill);
		//p.fill(myFill, 360, 360);// + 250);
//		p.fill(360, height*110, p.map(neighbourhood.size(),0,360,360,800));// + 250);

		p.pushMatrix();
		{
			//p.translate((Glv.roomSizeX/Glv.divisionX)*0.5f, (Glv.roomSizeY/Glv.divisionY)*0.5f, 0); // Shift because boxesare in center mode.
			p.translate(position.x, position.y, height*4.0f);
			p.rect(0,0,Glv.roomSizeX/Glv.divisionX, Glv.roomSizeY/Glv.divisionY);
			//p.box(Glv.roomSizeX/Glv.divisionX, Glv.roomSizeY/Glv.divisionY, height*4.0f);
			//p.point(0, 0, 0);
		}
		p.popMatrix();
	}
}
