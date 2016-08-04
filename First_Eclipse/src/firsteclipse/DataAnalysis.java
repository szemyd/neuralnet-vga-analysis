package firsteclipse;

import java.util.ArrayList;

import org.gicentre.utils.stat.*;

import com.sun.swing.internal.plaf.basic.resources.basic;

import controlP5.Background;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class DataAnalysis {
	private PApplet p;

	BarChart myBarChart;
	PFont titleFont, smallFont;

	XYChart lineChart;

	public DataAnalysis(PApplet _p) {
		p = _p;

		//myBarChart = new BarChart(p);

	}

	public void setup() {
		//		titleFont = p.loadFont("Helvetica-22.vlw");
		//		smallFont = p.loadFont("Helvetica-12.vlw");
		//		p.textFont(smallFont);
		//
		//		myBarChart.setData(new float[] { 2462, 2801, 3280, 3983, 4490, 4894, 5642, 6322, 6489, 6401, 7657, 9649, 9767,
		//				12167, 15154, 18200, 23124, 28645 });
		//
		//		myBarChart.setBarLabels(new String[] { "1830", "1840", "1850", "1860", "1870", "1880", "1890", "1900", "1910",
		//				"1920", "1930", "1940", "1950", "1960", "1970", "1980", "1990", "2000" });
		//		myBarChart.setBarColour(p.color(200, 80, 80, 100));
		//		myBarChart.setBarGap(2);
		//		myBarChart.setValueFormat("$###,###");
		//		myBarChart.showValueAxis(true);
		//		myBarChart.showCategoryAxis(true);

		lineChart = new XYChart(p);

		Glv.errorCounter = new ArrayList<PVector>();
		Glv.howManyCycles = 0f;

		//		lineChart.setData(new float[] { 1900, 1910, 1920, 1930, 1940, 1950, 1960, 1970, 1980, 1990, 2000 },
		//				new float[] { 6322, 6489, 6401, 7657, 9649, 9767, 12167, 15154, 18200, 23124, 28645 });
		//
		//		lineChart.setData(new float[] { 0,1,2,3,4,5,6},
		//				new float[] { Glv.errorCounter.get(0),Glv.errorCounter.get(1),Glv.errorCounter.get(2),Glv.errorCounter.get(3),Glv.errorCounter.get(4),Glv.errorCounter.get(5), Glv.errorCounter.get(6)});

		lineChart.setData(Glv.errorCounter);

		// Axis formatting and labels.
		lineChart.showXAxis(true);
		lineChart.showYAxis(true);
		
		
		lineChart.setAxisValuesColour(p.color(360));
		
		lineChart.setMinY(0f);
		lineChart.setMaxY(100f);

		lineChart.setAxisColour(p.color(360, 50, 360, 360));
		//lineChart.setDecorations(true);


//		lineChart.setYFormat("0"); // Monetary value in $US
	//	lineChart.setXFormat("0"); // Year

		// Symbol colours
		lineChart.setPointColour(p.color(180, 50, 360, 360));
		lineChart.setPointSize(5);
		lineChart.setLineWidth(2);
		
		
	}

	public void draw() {
		//myBarChart.draw(10, 10, p.width - 20, p.height - 20);
		//myBarChart.draw
		//p.background(360);
		//p.fill(p.color(180, 50, 360, 360));
		//p.stroke(360, 360, 360);
		lineChart.draw(15f, 15f, p.width - 30f, p.height*2f*0.33f - 30f);
		//p.line(x1, y1, x2, y2);
	}
}
