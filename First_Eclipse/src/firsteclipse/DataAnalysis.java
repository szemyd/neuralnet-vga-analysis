package firsteclipse;

import java.awt.font.GlyphVector;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.gicentre.utils.stat.*;

import com.sun.jmx.snmp.tasks.ThreadService;
import com.sun.org.apache.bcel.internal.generic.POP;
import com.sun.swing.internal.plaf.basic.resources.basic;

import controlP5.Background;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PVector;

public class DataAnalysis {
	private PApplet p;

	BarChart myBarChart;

	XYChart lineChart, lineChart2;
	Float[][] compareAnalysis;// = new ArrayList<Float[]>();
	public static Neuron[][] compareAnalysisN;

	float difference = -1f;
	float precentage, precentageNormal;

	public DataAnalysis(PApplet _p) {
		p = _p;
	}

	public void setup() {

		//p.textFont(which, size);

		lineChart = new XYChart(p);
		lineChart2 = new XYChart(p);

		Glv.errorCounter = new ArrayList<PVector>();
		Glv.errorCounter2 = new ArrayList<PVector>();
		
		Glv.errorCounter2Normal = new ArrayList<PVector>();
		Glv.errorCounterNormal = new ArrayList<PVector>();

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
		//lineChart.setMaxY(100f);

		lineChart.setAxisColour(p.color(360, 50, 360, 360));
		//lineChart.setDecorations(true);

		//		lineChart.setYFormat("0"); // Monetary value in $US
		//	lineChart.setXFormat("0"); // Year

		// Symbol colours
		lineChart.setPointColour(p.color(180, 50, 360, 360));
		lineChart.setPointSize(5);
		lineChart.setLineWidth(2);

		//---> Chart 2.
		lineChart2.setData(Glv.errorCounter2);

		// Axis formatting and labels.
		lineChart2.showXAxis(true);
		lineChart2.showYAxis(true);

		lineChart2.setAxisValuesColour(p.color(360));

		lineChart2.setMinY(0f);
		//lineChart2.setMaxY(15f);

		lineChart2.setAxisColour(p.color(360, 50, 360, 360));
		//lineChart.setDecorations(true);

		//		lineChart.setYFormat("0"); // Monetary value in $US
		//	lineChart.setXFormat("0"); // Year

		// Symbol colours
		lineChart2.setPointColour(p.color(180, 50, 360, 360));
		lineChart2.setPointSize(5);
		lineChart2.setLineWidth(2);

	}

	public void draw() {
		//myBarChart.draw(10, 10, p.width - 20, p.height - 20);
		//myBarChart.draw
		//p.background(360);
		//p.fill(p.color(180, 50, 360, 360));
		//p.stroke(360, 360, 360);
		p.pushStyle();
		{
			p.rectMode(PConstants.CORNER);
			p.fill(360, 0, 70, 180);
			p.rect(60f, 50f, p.width - 110f, p.height * 2f * 0.33f - 50f);
			p.rect(60f, p.height * 2f * 0.33f + 50f, p.width - 110f, p.height * 0.33f - 130f);
		}
		p.popStyle();

		lineChart.draw(40f, 40f, p.width - 80f, p.height * 2f * 0.33f - 30f);
		lineChart2.draw(40f, p.height * 2f * 0.33f + 45f, p.width - 80f, p.height * 0.33f - 110f);
		//p.line(x1, y1, x2, y2);

	}

	public void drawNeuron(Environment env) {

		switch (Glv.genOrA) {
		case 0:
			drawGenerating(env);
			break;
		case 1:
			drawGenerating(env);
			break;
		case 2:
			//drawAnalysis(env);
			break;
		}

	}

	private void drawGenerating(Environment env) {

		if (compareAnalysis != null) {
			drawBoundary(
					new PVector((6f * p.width / 10f) - (Glv.neuronSize * 1.2f * compareAnalysis.length) * 0.5f,
							(p.height / 2f) - (Glv.neuronSize * 1.2f * compareAnalysis[0].length) * 0.5f),
					compareAnalysis.length, compareAnalysis[0].length, "Selected neurons");

			//---> THIS DRAWS THE SELECTED NEURONS OF THE SPACESYNTAX ANALYSIS OF THE OUTPUT FORM!
			for (int i = 0; i < compareAnalysis.length; i++) {
				for (int j = 0; j < compareAnalysis[i].length; j++) {
					p.pushStyle();
					{
						p.pushMatrix();
						{
							//p.fill(360, 0, 180 * (1 - compareAnalysis[i][j]));
							p.fill(360, 0, 180 * (1 - compareAnalysis[i][j]));

							p.translate(6f * p.width / 10f, p.height / 2f);
							p.translate(
									(Glv.neuronSize * 1.2f * i)
											- (Glv.neuronSize * 1.2f * compareAnalysis.length) * 0.5f,
									(Glv.neuronSize * 1.2f * j)
											- (Glv.neuronSize * 1.2f * compareAnalysis[0].length) * 0.5f);
							p.ellipse(0, 0, Glv.neuronSize, Glv.neuronSize);
						}
						p.popMatrix();
					}
					p.popStyle();
				}

			}
		}

		//---> THIS DRAWS THE SPACESYNTAX ANALYSIS OF THE OUTPUT FORM AS NEURONS ON THE RIGHT OF THE SCREEN!
		if (Glv.threadNN != null) {
			if (Glv.threadNN.net != null) {
				if (Glv.threadNN.net.threads != null) {
					if (Glv.threadNN.net.threads.get(Glv.threadNN.net.threads.size() - 1).spaceSyntax.values != null) {

						drawBoundary(
								new PVector(
										(8f * p.width / 10f)
												- (Glv.neuronSize * 1.2f
														* Glv.threadNN.net.threads.get(Glv.threadNN.net.threads.size()
																- 1).spaceSyntax.values.length)
														* 0.5f,
										(p.height / 2f) - (Glv.neuronSize * 1.2f
												* Glv.threadNN.net.threads.get(Glv.threadNN.net.threads.size()
														- 1).spaceSyntax.values[0].length)
												* 0.5f),
								Glv.threadNN.net.threads
										.get(Glv.threadNN.net.threads.size() - 1).spaceSyntax.values.length,
								Glv.threadNN.net.threads
										.get(Glv.threadNN.net.threads.size() - 1).spaceSyntax.values[0].length,
								"VGA from generated form");

						for (int i = 0; i < Glv.threadNN.net.threads
								.get(Glv.threadNN.net.threads.size() - 1).spaceSyntax.values.length; i++) {
							for (int j = 0; j < Glv.threadNN.net.threads
									.get(Glv.threadNN.net.threads.size() - 1).spaceSyntax.values[i].length; j++) {
								p.pushMatrix();
								{
									p.pushStyle();
									{
										if (env.editorLayer[i][j].iAmChosen) {
											p.strokeWeight(1.2f);
											p.stroke(360, 360, 180 * (1 - Glv.threadNN.net.threads
													.get(Glv.threadNN.net.threads.size() - 1).spaceSyntax.values[i][j]),
													360);
											p.fill(360, 0, 180 * (1 - Glv.threadNN.net.threads
													.get(Glv.threadNN.net.threads.size() - 1).spaceSyntax.values[i][j]),
													360);
										} else {
											p.strokeWeight(1.0f);
											p.stroke(360, 0, 180 * (1 - Glv.threadNN.net.threads
													.get(Glv.threadNN.net.threads.size() - 1).spaceSyntax.values[i][j]),
													360);
											p.fill(360, 0, 180 * (1 - Glv.threadNN.net.threads
													.get(Glv.threadNN.net.threads.size() - 1).spaceSyntax.values[i][j]),
													360);
										}

										p.translate(8f * p.width / 10f, p.height / 2f);
										p.translate(
												(Glv.neuronSize * 1.2f * i) - (Glv.neuronSize * 1.2f
														* Glv.threadNN.net.threads.get(Glv.threadNN.net.threads.size()
																- 1).spaceSyntax.values.length)
														* 0.5f,
												(Glv.neuronSize * 1.2f * j)
														- (Glv.neuronSize * 1.2f
																* Glv.threadNN.net.threads
																		.get(Glv.threadNN.net.threads.size()
																				- 1).spaceSyntax.values[i].length)
																* 0.5f);
										p.ellipse(0, 0, Glv.neuronSize, Glv.neuronSize);
									}
									p.popStyle();
								}
								p.popMatrix();
							}
						}
					}
				}

			}
		}
		//<---

		//---> DISPLAYS DIFFERENCE IN THE MIDDLE!!
		DecimalFormat df = new DecimalFormat("#.##"); // To chop of some of the decimal values!

		if (difference >= 0f) {
			p.pushMatrix();
			{
				p.pushStyle();
				{
					p.strokeWeight(5f);
					p.stroke(360, 220);
					p.fill(360, 180);
					p.ellipse(p.width * 0.5f, p.height * 0.5f - 70f, 120f, 120f);
					p.ellipse(p.width * 0.5f, p.height * 0.5f + 70f, 120f, 120f);
					p.textAlign(PConstants.CENTER, PConstants.CENTER);
					p.textSize(24f);
					p.fill(360);
					p.stroke(360);
					p.text(df.format(precentage) + "%", p.width * 0.5f, p.height * 0.5f - 70f);
					p.text(df.format(precentageNormal) + "%", p.width * 0.5f, p.height * 0.5f + 70f);
				}
				p.popStyle();
			}
			p.popMatrix();
		}
		//<---
	}

	//	private void drawAnalysis(Environment env)
	//	{
	//		
	//	}

	private void drawBoundary(PVector position, int sizeX, int sizeY, String myText) {
		p.pushStyle();
		{
			p.noFill();
			//p.fill(360);
			p.stroke(360);
			p.pushMatrix();
			{
				p.rectMode(PConstants.CORNER);
				p.rect(position.x - 10f - Glv.neuronSize, position.y - 10f - Glv.neuronSize,
						(sizeX * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize,
						(sizeY * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize, 20f);
			}
			p.popMatrix();
		}
		p.popStyle();

		drawText(myText, new PVector(position.x - Glv.neuronSize,
				position.y - 10f - Glv.neuronSize + (sizeY * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize + 20f));
	}

	private void drawText(String myText, PVector position) {
		p.pushMatrix();
		{
			p.pushStyle();
			{
				p.textAlign(PConstants.LEFT, PConstants.CENTER);
				p.textSize(18);
				p.fill(360);
				p.text(myText, position.x + 15f, position.y);
				p.ellipse(position.x, position.y, 8f, 8f);
			}
			p.popStyle();

		}
		p.popMatrix();
	}

	public void compare(Environment env) {

		//Glv.threadNN.net.thread.spaceSyntax.values;

		int num = 0;
		for (int i = 0; i < env.editorLayer.length; i++) {
			for (int j = 0; j < env.editorLayer[i].length; j++) {
				if (env.editorLayer[i][j].iAmChosen)
					num++;
			}
		}

		boolean areThreadsStillRunning = false;

		for (MyThread thread : Glv.threadNN.net.threads) {
			if (thread.isAlive())
				areThreadsStillRunning = true;
		}

		if (!areThreadsStillRunning) {
			int i = 0;
			for (MyThread thread : Glv.threadNN.net.threads) {

				extractValuableAnalysis(env, num, thread);
				calcDifference(i++, thread, env); // Calculate difference between the selected points of the original loaded VGA and the newly generated one!
			}
		}
	}

	public void extractValuableAnalysis(Environment env, int length, MyThread thread) {

		if (Glv.threadNN.net.threads.get(Glv.threadNN.net.threads.size() - 1).spaceSyntax.values != null) {

			float[][] spaceSyntaxValuesTemp = new float[Glv.threadNN.net.threads
					.get(Glv.threadNN.net.threads.size() - 1).spaceSyntax.values.length][Glv.threadNN.net.threads
							.get(Glv.threadNN.net.threads.size() - 1).spaceSyntax.values[0].length];

			//---> MAP the values!
			for (int i = 0; i < thread.spaceSyntax.values.length; i++) {
				for (int j = 0; j < thread.spaceSyntax.values[i].length; j++) {
					spaceSyntaxValuesTemp[i][j] = p.map(thread.spaceSyntax.values[i][j], Glv.highLowForNN.y, 1429f, -1f,
							1f);
					thread.spaceSyntax.values[i][j] = spaceSyntaxValuesTemp[i][j];
				}
			}

			//if (env.editorLayer[0].length == _analysis[0].length && env.editorLayer.length == _analysis.length) {
			compareAnalysis = new Float[1][length];
			//compareAnalysisN = new Neuron[1][length];
			//Float [][] temp;
			int counter = 0;
			for (int i = 0; i < spaceSyntaxValuesTemp.length; i++) {
				for (int j = 0; j < spaceSyntaxValuesTemp[i].length; j++) {
					if (env.editorLayer[i][j].iAmChosen) {
						compareAnalysis[0][counter] = spaceSyntaxValuesTemp[i][j];
						//						compareAnalysisN[0][counter] = new Neuron(p,
						//								new PVector(env.editorLayer[i][j].position.x + p.width / 10f * 3f,
						//										env.editorLayer[i][j].position.y));
						//						compareAnalysisN[0][counter].m_output = spaceSyntaxValuesTemp[i][j];
						counter++;
					}
				}
			}
			//}
		}

	}

	private void test() {
		for (int i = 0; i < compareAnalysis.length; i++) {
			for (int j = 0; j < compareAnalysis[i].length; j++) {
				compareAnalysis[i][j] = -1.0f;
			}
		}

		for (int i = 0; i < Glv.threadNN.net.neuralnet.lastCard.rAnalysis.length; i++) {
			for (int j = 0; j < Glv.threadNN.net.neuralnet.lastCard.rAnalysis[i].length; j++) {

				Glv.threadNN.net.neuralnet.lastCard.rAnalysis[i][j] = 1.0f;
			}
		}
	}

	public void calcDifference(int m, MyThread thread, Environment env) {
		if (compareAnalysis != null) {
			difference = 0f;
			precentageNormal = 0f;
			//test(); // This checks if how I calculate the difference is accurate or not.

			int counter = 0;
			for (int i = 0; i < thread.spaceSyntax.values.length; i++) {
				for (int j = 0; j < thread.spaceSyntax.values[i].length; j++) {
					if (env.editorLayer[i][j].iAmChosen) {
						difference += Math.pow(thread.spaceSyntax.values[i][j] - Glv.threadNN.net.trainingSet
								.get(Glv.threadNN.net.shownCardIds.get(m)).rAnalysis[0][counter], 2f) / 2f;
						precentageNormal += Math.abs(thread.spaceSyntax.values[i][j] - Glv.threadNN.net.trainingSet
								.get(Glv.threadNN.net.shownCardIds.get(m)).rAnalysis[0][counter]);

						counter++;
					}
				}
			}

			/*
			for (int i = 0; i < compareAnalysis.length; i++) {
				for (int j = 0; j < compareAnalysis[i].length; j++) {
					//										difference += (Math.pow(compareAnalysis[i][j] - Glv.threadNN.net.neuralnet.lastCard.rAnalysis[i][j],
					//												2));
					//					p.print("compAna: " + compareAnalysis[i][j] + " - rAnalysis[i][j]: "
					//							+ Glv.threadNN.net.neuralnet.lastCard.rAnalysis[i][j] + " | ");
					difference += Math.pow(compareAnalysis[i][j] - Glv.threadNN.net.trainingSet.get(Glv.threadNN.net.shownCardIds.get(m)).rAnalysis[i][j],
							2f) / 2f;
					precentageNormal += Math
							.abs(compareAnalysis[i][j] - Glv.threadNN.net.trainingSet.get(Glv.threadNN.net.shownCardIds.get(m)).rAnalysis[i][j]);
				}
			}
			*/

			precentage = 0;
			precentage = difference;

			precentageNormal /= (compareAnalysis.length * compareAnalysis[0].length) * 2f;
			precentage /= (((compareAnalysis.length * compareAnalysis[0].length) * 2f)); // IS THE SAME AS == (Math.pow(2f, 2f)) /2f

			precentage *= 100f;
			precentageNormal *= 100f;

			Glv.bestVGAMSE = setSmaller(precentage, Glv.bestVGAMSE);
			Glv.bestVGAE = setSmaller(precentageNormal, Glv.bestVGAE);

			Glv.errorCounter2.add(new PVector(m * Glv.numOfLearning, precentage));
			Glv.errorCounter2Normal.add(new PVector(m * Glv.numOfLearning, precentageNormal));
			//Glv.errorCounter.add(new PVector(Glv.howManyCycles, precentage));
			//p.println(counter);
			lineChart2.setData(Glv.errorCounter2);
			//			System.out.println("Last sum of VGA difference: " + precentage + "%" + "Difference: " + difference
			//					+ "All possible differences: "
			//					+ (((compareAnalysis.length * compareAnalysis[0].length) * (Math.pow(2f, 2f)) / 2f)));
		}
	}

	public float setSmaller(float input, float comparingTo) {
		if (input < comparingTo)
			return input;
		else
			return comparingTo;
	}
}