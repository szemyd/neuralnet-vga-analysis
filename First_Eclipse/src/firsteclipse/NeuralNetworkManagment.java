package firsteclipse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.webkit.ThemeClient;
import com.sun.xml.internal.org.jvnet.mimepull.CleanUpExecutorFactory;

import processing.core.PApplet;

public class NeuralNetworkManagment {

	private static PApplet p;
	public ArrayList<MyData> trainingSet = new ArrayList<MyData>();
	public ArrayList<MyData> testingSet = new ArrayList<MyData>();

	public Network neuralnet;
	public boolean dataLoaded = false;

	public static float[] g_sigmoid = new float[200]; // The precalculated values for the sigmoid function are contained this.

	public NeuralNetworkManagment(PApplet _p) {
		p = _p;

		setupSigmoid();
	}

	//---> Loading & Managing the data
	public void loadGenData() {
		String filePath = new File("").getAbsolutePath();
		File folder = new File(filePath + "\\" + "GeneratedData");
		File[] listOfFiles = folder.listFiles();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int numberOfAnalysis = 0;

		if (!dataLoaded) {
			for (int i = 0; i < listOfFiles.length; i++) {

				try {
					testingSet.add(new MyData(p));
					trainingSet.add(new MyData(p));
					boolean analysisOrForm = false;
					boolean isItID = true;

					br = new BufferedReader(new FileReader(listOfFiles[i]));
					while ((line = br.readLine()) != null) {

						MyData data;

						if (numberOfAnalysis % Glv.divisionOfTestingTraining != 0)
							data = trainingSet.get(trainingSet.size() - 1);
						else
							data = testingSet.get(testingSet.size() - 1);
						String[] thisRow = line.split(cvsSplitBy);

						for (int j = 0; j < thisRow.length; j++) { // Go through each element of thisRow.

							if (!thisRow[j].isEmpty()) {
								char c = thisRow[j].charAt(0);

								if (isItID) { // If this switch is on then store the file as an ID.
									data.analysisID = Integer.valueOf(thisRow[j]);
									//p.println(thisRow[j]);
									isItID = false;
								} else if (c == '_') {
									if (numberOfAnalysis % Glv.divisionOfTestingTraining != 0)
										trainingSet.add(new MyData(p)); // Add new object MyData, essentially a new form + analysis.
									else
										testingSet.add(new MyData(p));
									analysisOrForm = false;
									numberOfAnalysis++;
									isItID = true;
									break;
								} else if (c == ':') {
									analysisOrForm = !analysisOrForm; // Is the data I am looking at the form or the analysis?
									isItID = false;
									break;
								} else {
									if (analysisOrForm) {
										data.form.add(thisRow); // If form put line in ArrayList of MyData form.
										isItID = false;
										break;
									} else {
										data.analysis.add(thisRow); // If analysis put line in ArrayList of MyData analysis.
										isItID = false;
										break;
									}
								}
							}
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

			}

			if (Glv.shP)
				p.println("Training set size: " + trainingSet.size());
			if (Glv.shP)
				p.println("Testing set size: " + testingSet.size());

			cleanupReadData(); // Goes through each file and eliminates ones that are not suitable.
			setHighLow(); // Calculates the range of visibility in the data.
			convertData(); // Convert the data into a -1 to 1 and maps the data to high low.
			extractValuableData(); // Converts the outputs to the dimensionality-reducted grid size and cuts the analysis data to the right points.
			//setupNeuralNetwork(); // Initialises the network and feeds in the size of the analysis and form.

			dataLoaded = true;
		} else
			System.out.println("Data has already been loaded.");
	}

	private void cleanupReadData() {
		//---> Remove element if it has an index of 0.
		for (int k = 0; k < testingSet.size(); k++) {
			MyData data = testingSet.get((k + 1) % testingSet.size());

			if (data.clean()) { // If data is uneven (if there is one line that is smaller larger than the other.
				//p.println("I'm removing: " + data.analysisID);
				testingSet.remove(data);
			}
		}

		for (int k = 0; k < trainingSet.size(); k++) {
			MyData data = trainingSet.get((k + 1) % trainingSet.size());

			if (data.clean()) { // If data is uneven (if there is one line that is smaller larger than the other.
				//p.println("I'm removing: " + data.analysisID);
				trainingSet.remove(data);
			}
		}
	}

	private void convertData() {
		for (MyData data : trainingSet) {
			data.convert();
		}

		for (MyData data : testingSet) {
			data.convert();
		}
	}

	private void setHighLow() { // Calculates the highest and the lowest number that exist in the analysis dataset.
		for (MyData data : testingSet) {
			for (String[] strings : data.analysis) {
				for (int i = 0; i < strings.length; i++) {
					int num = Integer.valueOf(strings[i]);
					if (num > Glv.highLowForNN.x)
						Glv.highLowForNN.x = num;
					if (num < Glv.highLowForNN.y && num > 6)
						Glv.highLowForNN.y = num;
				}
			}
			//			for (int i = 0; i < data._analysis.length; i++) {
			//				for (int j = 0; j < data._analysis[i].length; j++) {
			//					if (data._analysis[i][j] > Glv.highLowForNN.x)
			//						Glv.highLowForNN.x = data._analysis[i][j];
			//					if (data._analysis[i][j] < Glv.highLowForNN.y)
			//						Glv.highLowForNN.y = data._analysis[i][j];
			//				}
			//			}
		}
		p.println("Max visibility: " + Glv.highLowForNN.x + " | Min visibility: " + Glv.highLowForNN.y);
	}

	public void extractValuableData() {
		for (MyData data : testingSet) {
			data.extractValuableData();
		}

		for (MyData data : trainingSet) {
			data.extractValuableData();
		}
	}
	//<---

	//---> NEURAL NETWORK
	public void setupNeuralNetwork() {
		if (dataLoaded) {
			if (neuralnet == null) {
				neuralnet = new Network(p, trainingSet.get(0)._analysis.length, trainingSet.get(0)._analysis[2].length,
						p.floor(trainingSet.get(0)._analysis.length * 1.2f),
						p.floor(trainingSet.get(0)._analysis.length * 1.2f), trainingSet.get(0).rForm.length,
						trainingSet.get(0).rForm[2].length);

				System.out.println("InputSize: " + trainingSet.get(0)._analysis.length + " | "
						+ trainingSet.get(0)._analysis[2].length + " HiddenSize: "
						+ p.floor(trainingSet.get(0)._analysis.length * 1.2f) + " | "
						+ p.floor(trainingSet.get(0)._analysis.length * 1.2f) + " OutputSize: "
						+ trainingSet.get(0).rForm.length + " | " + trainingSet.get(0).rForm[2].length);
				neuralnet.respond(trainingSet.get(0));
			} else
				System.out.println("Neural Net already created.");
		}
		else
			System.out.println("Please Load Data");
	}

	public void trainNN() {
		for (int i = 0; i < 10; i++) {
			int row = (int) p.floor(p.random(0, trainingSet.size()));
			//int row=i;
			if (trainingSet.get(row)._analysis != null && trainingSet.get(row).rForm != null) {
				neuralnet.respond(trainingSet.get(row));
				neuralnet.train(trainingSet.get(row).rForm);
			} else
				p.println("Card was NULL");
		}
	}

	public void testNN() {
		int row = (int) p.floor(p.random(0, testingSet.size()));
		if (testingSet.get(row)._analysis != null && testingSet.get(row)._form != null) {
			neuralnet.respond(testingSet.get(row));
		}
	}
	//<---

	//---> Calculating sigmoid
	private void setupSigmoid() { // Calculates the sigmoid function.
		for (int i = 0; i < 200; i++) {
			float x = (i / 20.0f) - 5.0f;
			g_sigmoid[i] = 2.0f / (1.0f + p.exp(-2.0f * x)) - 1.0f;
		}
	}

	public static float lookupSigmoid(float x) { // Looks up for a certain x value the y value of the precalculated sigmoid
		return g_sigmoid[(int) p.constrain(p.floor((x + 5f) * 20.0f), 0f, 199f)];
	}
	//<---
}
