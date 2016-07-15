package firsteclipse;

import org.gicentre.utils.stat.*;
import processing.core.PApplet;
import processing.core.PFont;

public class DataAnalysis {
	private PApplet p;

	BarChart barChart;
	PFont titleFont, smallFont;

	public DataAnalysis(PApplet _p) {
		p = _p;

		barChart = new BarChart(p);
	}

	public void setup() {
		titleFont = p.loadFont("Helvetica-22.vlw");
		smallFont = p.loadFont("Helvetica-12.vlw");
		p.textFont(smallFont);

		barChart.setData(new float[] { 2462, 2801, 3280, 3983, 4490, 4894, 5642, 6322, 6489, 6401, 7657, 9649, 9767,
				12167, 15154, 18200, 23124, 28645 });

		barChart.setBarLabels(new String[] { "1830", "1840", "1850", "1860", "1870", "1880", "1890", "1900", "1910",
				"1920", "1930", "1940", "1950", "1960", "1970", "1980", "1990", "2000" });
		barChart.setBarColour(p.color(200, 80, 80, 100));
		barChart.setBarGap(2);
		barChart.setValueFormat("$###,###");
		barChart.showValueAxis(true);
		barChart.showCategoryAxis(true);
	}

	public void draw() {
		barChart.draw(10, 10, p.width - 20, p.height - 20);
		//barChart.draw

		//p.line(x1, y1, x2, y2);
	}
}
