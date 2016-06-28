package firsteclipse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import sun.net.www.content.text.plain;

public class Environment {

	private static PApplet p;
	public static ArrayList<Building> buildings = new ArrayList<Building>();

	public Environment(PApplet _p) {
		p = _p;
	}

	public void draw(PShape s) {
		p.pushStyle();
		{
			p.strokeWeight(5.0f);
			p.stroke(360, 0, 360);
			for (Building building : buildings) {
				building.draw();
			}
		}
		p.popStyle();
		
		

		p.pushMatrix();
		{
			p.pushStyle();
			{
				p.fill(360);
				p.translate(0, 0, -.01f);
				p.shape(s, 0, 0);
				// s.disableStyle();
			}
			p.popStyle();
		}
		p.popMatrix();
	}



	public void loadData() {

		String csvFile = "C:/Users/Me/Google Drive/UCL/III_Semester/Final Thesis/CODE/PHASE 1_Generating Data/First_Eclipse/src/data/sur.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				PVector temp = new PVector();
				// use comma as separator
				String[] thisRow = line.split(cvsSplitBy);

				temp.x = Float.parseFloat(thisRow[1]) / 100;
				temp.y = Float.parseFloat(thisRow[2]) / 100;
				temp.z = Float.parseFloat(thisRow[4]);

				// p.println(buildings.size() + " temp.z: " + temp.z);
				if (buildings.size() < (int) temp.z)
					buildings.add(new Building(p, temp));
				else {
					Building tempBuilding = buildings.get((int) temp.z - 1);
					tempBuilding.bLines.add(temp);
					tempBuilding.myPolygon.addPoint((int)temp.x,(int) temp.y);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// System.out.println("Country [code= " + country[4]
		// + " , name=" + country[5] + "]");
		//
		// String[] sData = p.loadStrings("sur.csv"); // Loading the file
		// // to an array.
		//
		// for (int i = 1; i < sData.length; i++) {
		// String[] thisRow = p.split(sData[i], ",");
		// }
		//
		// for (int i = 0; i < sData.length; i++) {
		// PVector temp = new PVector();
		// String[] thisRow = p.split(sData[i], ",");
		//
		// temp.x = Float.parseFloat(thisRow[1]);
		// temp.y = Float.parseFloat(thisRow[2]);
		// temp.z = Float.parseFloat(thisRow[4]);
		// p.println(temp);
		// // for (int j=0; j<thisRow.length; j++)
		// // {
		// //
		//
		// }

	}
}

//// --->Checking the biggest column size (if we have data that is not
// homogenous).
// for (int i=1; i<sData.length; i++)
// {
// String [] thisRow= split(sData[i], ",");
//
// cellCount=thisRow.length;
// if (maxNum<cellCount)
// {
// maxNum=cellCount;
// }
// }