package firsteclipse;

import java.util.ArrayList;

import com.sun.glass.ui.Size;

import processing.core.PApplet;
import processing.core.PVector;

public class MyRect {
	private static PApplet p;

	public PVector position;
	public PVector size;
	public Integer iD;

	public ArrayList<MyRect> neighbourhood = new ArrayList<MyRect>();
	int[] shortestPath;
	Float meanShortestPath;

	public float height = 0.0f;

	public MyRect(PApplet _p, PVector _position, int _iD) {
		p = _p;

		position = _position;
		iD = _iD;
	}

	public MyRect(PApplet _p, PVector _position, PVector _size, int _iD) {
		p = _p;

		position = _position;
		size = _size;

		iD = _iD;
	}

	public void draw(PVector highLow) {

		if (Glv.VGAormeanShort) {
			if (Glv.globalHighLow)
				p.fill(p.map(neighbourhood.size(), Glv.highLow.y, Glv.highLow.x, 230, 360), 360, 360);
			else
				p.fill(p.map(neighbourhood.size(), highLow.y, highLow.x, 230, 360), 360, 360);
		} else if (meanShortestPath != null)
			p.fill(p.map(meanShortestPath, 0f, shortestPath.length * 10f, 230, 360), 360, 360);

		//		if (Glv.globalHighLow) p.fill(p.map(neighbourhood.size(), Glv.highLow.y, Glv.highLow.x, 30, 360), 360, 360);
		//				else p.fill(p.map(neighbourhood.size(), highLow.y, highLow.x, 30, 360), 360, 360);

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

	public void drawResponded(PVector highLow) {
		if (height == 0.0f)
			p.fill(110);
		else
			p.fill(p.map(height, -1f, 1f, 230, 360), 360, 360);
		p.pushMatrix();
		{
			p.translate(position.x, position.y, 0.0f);
			p.rect(0, 0, Glv.spaceCubeSize, Glv.spaceCubeSize);
		}
		p.popMatrix();
	}

	public void drawEditor() {
		if (height < 0.1f) {
			p.fill(180, 210);
		} else
			p.fill(220, 320);

		p.pushMatrix();
		{
			p.translate(position.x, position.y, 0f);
			p.rect(0, 0, size.x, size.y);
		}
		p.popMatrix();

	}

	public void calcAvarage(int number_of_vertices) {
		//shortestPath = new int[number_of_vertices];
		meanShortestPath = 0f;

		for (int i = 0; i < shortestPath.length; i++) {
			p.println(shortestPath[i]);
			meanShortestPath += shortestPath[i];
		}
		p.println("meanShortestPath: " + meanShortestPath);
		meanShortestPath /= shortestPath.length;

		p.println("meanShortestPath: " + meanShortestPath);
	}
}
