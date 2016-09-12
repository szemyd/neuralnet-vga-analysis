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
	Float clusteringSize = 0.0f;

	public float height = 0.0f;
	public float[] corners = new float[4];

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

		p.noStroke();
		if (Glv.neighbourHoodOrClustering) {
			if (Glv.globalHighLow)
				p.fill(p.map(neighbourhood.size(), Glv.highLow.y, Glv.highLow.x, 0, 255), 360, 360);
			else
				p.fill(p.map(neighbourhood.size(), highLow.y, highLow.x, 0, 255), 360, 360);
		} else if (clusteringSize != null)
			p.fill(p.map(clusteringSize, 0f, 1f, 0, 255), 360, 360);

		//		if (Glv.globalHighLow) p.fill(p.map(neighbourhood.size(), Glv.highLow.y, Glv.highLow.x, 30, 360), 360, 360);
		//				else p.fill(p.map(neighbourhood.size(), highLow.y, highLow.x, 30, 360), 360, 360);

		drawVertex();
	}

	public void drawResponded(PVector highLow) {
		if (height == 0.0f)
			p.fill(110);
		else
			p.fill(p.map(height, -1f, 1f, 0, 255), 360, 360);

		drawVertex();

		/*p.pushMatrix();
		{
			p.translate(position.x, position.y, 0.0f);
			p.rect(0, 0, Glv.spaceCubeSize, Glv.spaceCubeSize);
		}
		p.popMatrix();
		*/
	}

	void drawVertex() {
		if (height < 0.1f) {
			p.pushMatrix();
			{
				// p.translate((Glv.roomSizeX/Glv.divisionX)*0.5f,
				// (Glv.roomSizeY/Glv.divisionY)*0.5f, 0); // Shift because
				// boxesare in center mode.

				//	float howHigh = p.map(neighbourhood.size(), highLow.y, highLow.x, 0, -20f);
				//p.translate(position.x, position.y, howHigh);
				//p.rect(0, 0, Glv.spaceCubeSize, Glv.spaceCubeSize);

				//--> Top and bottom triangle 1
				p.beginShape();
				{
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f, corners[0]);
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f, corners[1]);
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f, corners[2]);
				}
				p.endShape();
				p.beginShape();
				{
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f,
							corners[0] - 20f);
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f,
							corners[1] - 20f);
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f,
							corners[2] - 20f);
				}
				p.endShape();
				//<---

				p.beginShape();
				{
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f, corners[0]);
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f,
							corners[0] - 20f);
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f,
							corners[1] - 20f);
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f, corners[1]);

				}
				p.endShape();

				p.beginShape();
				{
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f, corners[0]);
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f,
							corners[0] - 20f);
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f,
							corners[2] - 20f);
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f, corners[2]);

				}
				p.endShape();

				//--> Top and bottom triangle 2
				p.beginShape();
				{
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f, corners[1]);
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f, corners[2]);
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f, corners[3]);
				}
				p.endShape();
				p.beginShape();
				{
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f,
							corners[1] - 20f);
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f,
							corners[2] - 20f);
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f,
							corners[3] - 20f);
				}
				p.endShape();
				//<--

				p.beginShape();
				{
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f, corners[2]);
					p.vertex(position.x - Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f,
							corners[2] - 20f);
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f,
							corners[3] - 20f);
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f, corners[3]);

				}
				p.endShape();
				p.beginShape();
				{
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f, corners[3]);
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y + Glv.spaceCubeSize * 0.5f,
							corners[3] - 20f);
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f,
							corners[1] - 20f);
					p.vertex(position.x + Glv.spaceCubeSize * 0.5f, position.y - Glv.spaceCubeSize * 0.5f, corners[1]);

				}
				p.endShape();

				// p.box(Glv.roomSizeX/Glv.divisionX,
				// Glv.roomSizeY/Glv.divisionY, height*4.0f);
				// p.point(0, 0, 0);
			}
			p.popMatrix();
		}
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
