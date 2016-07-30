package firsteclipse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;

import javax.lang.model.util.ElementKindVisitor6;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import com.sun.media.jfxmedia.events.NewFrameEvent;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.webkit.ThemeClient;
import com.sun.xml.internal.org.jvnet.mimepull.CleanUpExecutorFactory;

import controlP5.Println;
import processing.core.PApplet;
import processing.core.PVector;

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
				p.println(
						"Training set size: " + trainingSet.size() + " | " + "Testing set size: " + testingSet.size());

			cleanupReadData(); // Goes through each file and eliminates ones that are not suitable.
			setHighLow(); // Calculates the range of visibility in the data.
			convertData(); // Convert the data into a -1 to 1 and maps the data to high low.
			extractValuableForm(); // Converts the outputs to the dimensionality-reducted grid size and cuts the analysis data to the right points.
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
					if (num > Glv.highLowForNN.x) {
						Glv.highLowForNN.x = num;
						Glv.cardContainingHighest = testingSet.indexOf(data);
						//p.println("Card containing lowest: " + Glv.cardContainingHighest);
					}
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

	public void extractValuableForm() {
		for (MyData data : testingSet) {
			data.extractValuableForm();
		}

		for (MyData data : trainingSet) {
			data.extractValuableForm();
		}
	}

	public int setInputNeurons(Environment env) {

		int num = 0;
		for (int i = 0; i < env.editorLayer.length; i++) {
			for (int j = 0; j < env.editorLayer[i].length; j++) {
				if (env.editorLayer[i][j].iAmChosen)
					num++;
			}
		}

		for (MyData data : trainingSet) {
			data.extractValuableAnalysis(env, num);
		}

		for (MyData data : testingSet) {
			data.extractValuableAnalysis(env, num);
		}

		return num;
	}
	//<---

	//---> NEURAL NETWORK
	public void setupNeuralNetwork() {
		if (Glv.neuronsStored) {
			Glv.netSize[0] = trainingSet.get(0).rAnalysis.length;
			Glv.netSize[1] = trainingSet.get(0).rAnalysis[0].length;

			Glv.netSize[4] = trainingSet.get(0).rForm.length;
			Glv.netSize[5] = trainingSet.get(0).rForm[2].length;

			if (Glv.netSize[0] > Glv.netSize[4] && Glv.netSize[1] > Glv.netSize[5]) { // Depends if the input or the output layer is bigger the hidden layer's size is chosen accordinglyhgt21q`	a\2R
				Glv.netSize[2] = p.floor(Glv.netSize[0] * Glv.howMuchBiggerHidden);
				Glv.netSize[3] = p.floor(Glv.netSize[1] * Glv.howMuchBiggerHidden);
			} else {
				Glv.netSize[2] = p.floor(Glv.netSize[4] * Glv.howMuchBiggerHidden);
				Glv.netSize[3] = p.floor(Glv.netSize[5] * Glv.howMuchBiggerHidden);
			}
		} else {
			Glv.netSize[0] = trainingSet.get(0)._analysis.length;
			Glv.netSize[1] = trainingSet.get(0)._analysis[2].length;
			
			Glv.netSize[2] = p.floor(trainingSet.get(0)._analysis.length * Glv.howMuchBiggerHidden);
			Glv.netSize[3] = p.floor(trainingSet.get(0)._analysis[2].length * Glv.howMuchBiggerHidden);
			
			Glv.netSize[4] = trainingSet.get(0).rForm.length;
			Glv.netSize[5] = trainingSet.get(0).rForm[2].length;
		}

		if (dataLoaded) {
			if (neuralnet == null || Glv.neuronsStored) {
				neuralnet = new Network(p, Glv.netSize[0], Glv.netSize[1], Glv.netSize[2], Glv.netSize[3],
						Glv.netSize[4], Glv.netSize[5]);

				System.out.println(
						"InputSize: " + Glv.netSize[0] + " | " + Glv.netSize[1] + " HiddenSize: " + Glv.netSize[2]
								+ " | " + Glv.netSize[3] + " OutputSize: " + Glv.netSize[4] + " | " + Glv.netSize[5]);
				if (Glv.neuronsStored)
					neuralnet.respond(trainingSet.get(0), trainingSet.get(0).rAnalysis);
				else
					neuralnet.respond(trainingSet.get(0), trainingSet.get(0)._analysis);
			} else
				System.out.println("Neural Net already created.");
		} else
			System.out.println("Please Load Data");
	}

	public void trainNN(DataAnalysis graphs) {
		float counter = 0f;
		for (int i = 0; i < Glv.numOfLearning; i++) {
			int row = (int) p.floor(p.random(0, trainingSet.size()));
			//int row=i;

			if (Glv.neuronsStored) { // If I have given which neurons to input.
				if (trainingSet.get(row).rAnalysis != null && trainingSet.get(row).rForm != null) {
					neuralnet.respond(trainingSet.get(row), trainingSet.get(row).rAnalysis);
					neuralnet.train(trainingSet.get(row).rForm);
				} else {
					p.println("Card was NULL");
					trainingSet.remove(trainingSet.get(row));
				}
			} else {
				if (trainingSet.get(row)._analysis != null && trainingSet.get(row).rForm != null) {
					neuralnet.respond(trainingSet.get(row), trainingSet.get(row)._analysis);
					neuralnet.train(trainingSet.get(row).rForm);
				} else {
					p.println("Card was NULL");
					trainingSet.remove(trainingSet.get(row));
				}
			}

			
			for (int j = 0; j < Glv.threadNN.net.neuralnet.m_output_layer.length; j++) {
				for (int k = 0; k < Glv.threadNN.net.neuralnet.m_output_layer[j].length; k++) {
					counter += p.abs(Glv.threadNN.net.neuralnet.m_output_layer[j][k].m_error);
				}
			}
			
		}
		Glv.errorCounter.add(new PVector(Glv.howManyCycles,counter/Glv.numOfLearning));
		//p.println(counter);
		graphs.lineChart.setData(Glv.errorCounter);
		
		System.out.println("Last sum of error: " + counter/Glv.numOfLearning);
		
	
		Glv.howManyCycles++;
	}

	public void testNN() {
		int row = (int) p.floor(p.random(0, testingSet.size()));
		if (Glv.neuronsStored) {
			if (testingSet.get(row).rAnalysis != null && testingSet.get(row)._form != null) {
				neuralnet.respond(testingSet.get(row), testingSet.get(row).rAnalysis);
			} else
				p.println("Card was NULL");
		} else {
			if (testingSet.get(row)._analysis != null && testingSet.get(row)._form != null) {
				neuralnet.respond(testingSet.get(row), testingSet.get(row)._analysis);
			} else
				p.println("Card was NULL");
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
