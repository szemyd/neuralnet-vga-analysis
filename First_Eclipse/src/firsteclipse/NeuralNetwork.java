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
import com.sun.xml.internal.org.jvnet.mimepull.CleanUpExecutorFactory;

import processing.core.PApplet;

public class NeuralNetwork {

	private static PApplet p;
	public static ArrayList<MyData> trainingSet = new ArrayList<MyData>();
	public static ArrayList<MyData> testingSet = new ArrayList<MyData>();

	//private static Network neuralnet;

	public static float[] g_sigmoid = new float[200];

	public NeuralNetwork(PApplet _p) {
		p = _p;

		setupSigmoid();
	}

	public static void loadGenData() {
		String filePath = new File("").getAbsolutePath();
		File folder = new File(filePath + "\\" + "GeneratedData");
		File[] listOfFiles = folder.listFiles();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int numberOfAnalysis = 0;

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
								p.println(thisRow[j]);
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

		cleanupReadData();

		if (Glv.shP)
			p.println("Training set size: " + trainingSet.size());
		if (Glv.shP)
			p.println("Testing set size: " + testingSet.size());

//		neuralnet = new Network(p, trainingSet.get(0).analysis.size(), trainingSet.get(0).analysis.size() / 10,
//				trainingSet.get(0).form.size());
	}

	private static void cleanupReadData() {

		//---> Remove element if it has an index of 0.
		for (int k = 0; k < testingSet.size(); k++) {
			
			
			//if (k < testingSet.size() - 1) {
				MyData data = testingSet.get((k + 1)%testingSet.size());

				if (data.clean()) {
					p.println("I'm removing: " + data.analysisID);
					testingSet.remove(data);
				}
			//}
			testingSet.get(k).convert();
		}
		
		for (int k = 0; k < trainingSet.size(); k++) {
			trainingSet.get(k).convert();
		//	if (k < trainingSet.size() - 1) {
				MyData data = trainingSet.get((k + 1)%trainingSet.size());

				if (data.clean()) {
					p.println("I'm removing: " +  data.analysisID);
					trainingSet.remove(data);
				}
			//}
		}
	}

	private void setupSigmoid() {
		for (int i = 0; i < 200; i++) {
			float x = (i / 20.0f) - 5.0f;
			g_sigmoid[i] = 2.0f / (1.0f + p.exp(-2.0f * x)) - 1.0f;
		}
	}

	public static float lookupSigmoid(float x) {
		return g_sigmoid[(int) p.constrain(p.floor((x + 5f) * 20.0f), 0f, 199f)];
	}}

