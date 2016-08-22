package firsteclipse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import sun.net.www.content.text.plain;

public class GenerateCSV {
	private static PApplet p;

	public static void createDir(String myDirectory) {
		String filePath = new File("").getAbsolutePath();

		File file = new File(filePath + "\\" + myDirectory);
		if (!file.exists()) {
			if (file.mkdir()) {
				if (Glv.shP)
					System.out.println("Directory is created!");
			} else {
				if (Glv.shP)
					System.out.println("Failed to create directory!");
			}
		}

		// if(Glv.shP) System.out.println(filePath + "\\" + "GeneratedData" + "\\" + fileName + ".csv");
		//generateCsvFile(filePath + "\\" + myDirectory + "\\" + fileName + ".csv");
	}

	public static void generateCsvFile(String sFileName) {
		File f = new File(sFileName);

		if (!f.isFile()) { // If the file doesn't exist yet

			try {
				FileWriter writer = new FileWriter(sFileName);

				//				for (MyThread thread : Glv.threads) {
				//					for (String s : thread.spaceSyntax.toNN) {
				//						writer.append(s);
				//					}
				//				}

				for (int i = 0; i < Glv.threads.size(); i++) {
					MyThread thread = Glv.threads.get(i);

					for (int j = 0; j < thread.spaceSyntax.toNN.size(); j++) {
						String s = thread.spaceSyntax.toNN.get(j);
						if (i == Glv.threads.size() - 1 && j >= thread.spaceSyntax.toNN.size() - 2) {
							System.out.println("im not writing this: '" + s + "'");
						} else
							writer.append(s); // Don't write the last '_'.
					}
				}

				// writer.append("DisplayName");

				writer.flush();
				writer.close();
				if (Glv.shP)
					System.out.println("Done writing CSV.");

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (Glv.shP)
			System.out.println("File already exists.");

		//Glv.toNN=new ArrayList<String>();
	}

	public static void saveValuesToCSV(String sFileName, String counter, String avarageDistanceOfNeurons,
			Environment env, PApplet _p) {
		File f = new File(sFileName + ".csv");
		p = _p;
		final String[][] csvMatrix;
		if(Glv.errorCounter.size() > env.editorLayer.length)		csvMatrix = new String[Glv.errorCounter.size() + 2][20 + env.editorLayer[0].length];
		else csvMatrix = new String[env.editorLayer.length + 2][20 + env.editorLayer[0].length];
		
		csvMatrix[0][0] = Integer.toString(Glv.genOrA) + " | " + counter + " | " + avarageDistanceOfNeurons;
		csvMatrix[1][0] = "Size of Input";
		csvMatrix[1][1] = Integer.toString(Glv.threadNN.net.neuralnet.m_input_layer.length);
		csvMatrix[1][2] = Integer.toString(Glv.threadNN.net.neuralnet.m_input_layer[0].length);

		csvMatrix[2][0] = "Size of Hidden";
		csvMatrix[2][1] = Integer.toString(Glv.threadNN.net.neuralnet.m_hidden_layer.length);
		csvMatrix[2][2] = Integer.toString(Glv.threadNN.net.neuralnet.m_hidden_layer[0].length);

		csvMatrix[3][0] = "Size of Output";
		csvMatrix[3][1] = Integer.toString(Glv.threadNN.net.neuralnet.m_output_layer.length);
		csvMatrix[3][2] = Integer.toString(Glv.threadNN.net.neuralnet.m_output_layer[0].length);

		csvMatrix[4][0] = "Number of Chosen";
		csvMatrix[4][1] = counter;
		//		

		csvMatrix[5][0] = "Avg Distance of Neurons";
		csvMatrix[5][1] = avarageDistanceOfNeurons;

		csvMatrix[6][0] = "Learning Rate";
		csvMatrix[6][1] = Float.toString(Glv.LEARNING_RATE);

		csvMatrix[7][0] = "Cards shown/ cycle";
		csvMatrix[7][1] = Float.toString(Glv.numOfLearning);
		//		

		csvMatrix[8][0] = "<PROPERTIES OF CARDS>";
		csvMatrix[9][0] = "Size of Dataset";
		csvMatrix[9][1] = Integer.toString(Glv.threadNN.net.trainingSet.size() + Glv.threadNN.net.testingSet.size());

		csvMatrix[10][0] = "Size of Teachingset";
		csvMatrix[10][1] = Integer.toString(Glv.threadNN.net.trainingSet.size());

		csvMatrix[11][0] = "Size of Testingset";
		csvMatrix[11][1] = Integer.toString(Glv.threadNN.net.testingSet.size());

		csvMatrix[12][0] = "Size of Analysis Original";
		csvMatrix[12][1] = Integer.toString(Glv.threadNN.net.testingSet.get(0).analysis.size());
		csvMatrix[12][2] = Integer.toString(Glv.threadNN.net.testingSet.get(0).analysis.get(0).length);

		csvMatrix[13][0] = "Size of Analysis Reduced";
		csvMatrix[13][1] = Integer.toString(Glv.threadNN.net.testingSet.get(0).rAnalysis.length);
		csvMatrix[13][2] = Integer.toString(Glv.threadNN.net.testingSet.get(0).rAnalysis[0].length);

		csvMatrix[14][0] = "Size of Form Original";
		csvMatrix[14][1] = Integer.toString(Glv.threadNN.net.testingSet.get(0).form.size());
		csvMatrix[14][2] = Integer.toString(Glv.threadNN.net.testingSet.get(0).form.get(0).length);

		csvMatrix[15][0] = "Size of Form Reduced";
		csvMatrix[15][1] = Integer.toString(Glv.threadNN.net.testingSet.get(0).rForm.length);
		csvMatrix[15][2] = Integer.toString(Glv.threadNN.net.testingSet.get(0).rForm[0].length);

		csvMatrix[16][0] = "Best NN after (MSE_E): " + Glv.numOfCycles * Glv.numOfLearning;
		csvMatrix[16][1] = Float.toString(Glv.bestMSE);
		csvMatrix[16][2] = Float.toString(Glv.bestE);

		csvMatrix[17][0] = "Best VGA after (MSE_E): " + Glv.numOfCycles * Glv.numOfLearning;
		csvMatrix[17][1] = Float.toString(Glv.bestVGAMSE);
		csvMatrix[17][2] = Float.toString(Glv.bestVGAE);

		csvMatrix[18][0] = "Time to Calculate: " + Glv.numOfLearning;
		float newCounter = 0f;
		for (Float num : Glv.timeToCalc) {
			newCounter += num;
		}
		newCounter /= Glv.timeToCalc.size();
		csvMatrix[18][1] = Float.toString(newCounter);

		csvMatrix[0][5] = "Squared Mean Error %";
		csvMatrix[0][7] = "Normal Error %";
		csvMatrix[0][9] = "VGA Squared Mean Error %";
		csvMatrix[0][11] = "VGA Normal Error %";

		csvMatrix[1][5] = "x";
		csvMatrix[1][6] = "f(x)";

		csvMatrix[1][7] = "x";
		csvMatrix[1][8] = "f(x)";

		csvMatrix[1][9] = "x";
		csvMatrix[1][10] = "f(x)";

		csvMatrix[1][11] = "x";
		csvMatrix[1][12] = "f(x)";

		csvMatrix[0][14] = "<FORM OF SELECTED>";

		int i = 0;
		for (PVector myVector : Glv.errorCounter) {
			csvMatrix[2 + i][5] = Float.toString(myVector.x);
			csvMatrix[2 + i][6] = Float.toString(myVector.y);
			i++;
		}

		i = 0;
		for (PVector myVector : Glv.errorCounterNormal) {
			csvMatrix[2 + i][7] = Float.toString(myVector.x);
			csvMatrix[2 + i][8] = Float.toString(myVector.y);
			i++;
		}

		i = 0;
		for (PVector myVector : Glv.errorCounter2) {
			csvMatrix[2 + i][9] = Float.toString(myVector.x);
			csvMatrix[2 + i][10] = Float.toString(myVector.y);
			i++;
		}

		i = 0;
		for (PVector myVector : Glv.errorCounter2Normal) {
			csvMatrix[2 + i][11] = Float.toString(myVector.x);
			csvMatrix[2 + i][12] = Float.toString(myVector.y);
			i++;
		}

		for (int k = 0; k < env.editorLayer.length; k++) {
			for (int l = 0; l < env.editorLayer[k].length; l++) {
				if (env.editorLayer[k][l].iAmChosen)
					csvMatrix[1 + k][14 + l] = "1";
				else
					csvMatrix[1 + k][14 + l] = "0";
			}
		}

		if (!f.isFile()) { // If the file doesn't exist yet

			try {
				FileWriter writer = new FileWriter(sFileName + ".csv");

				for (int j = 0; j < csvMatrix.length; j++) {
					for (int j2 = 0; j2 < csvMatrix[j].length; j2++) {

						if (csvMatrix[j][j2] != null)
							writer.append(csvMatrix[j][j2]);
						else
							writer.append("");
						writer.append(",");
					}
					writer.append("\n");
				}

				writer.flush();
				writer.close();
				if (Glv.shP)
					System.out.println("Done writing CSV.");

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (Glv.shP)
			System.out.println("File already exists.");

		//Glv.toNN=new ArrayList<String>();
		Glv.programMode = 1;
		p.saveFrame(sFileName +"\\" + "neuralNet");
		Glv.programMode = 3;
		p.saveFrame(sFileName +"\\" + "editor");
	}

	public static void saveSettings(String sFileName, String counter, String avarageDistanceOfNeurons,
			Environment env) {

	}

	public static void readSettings() {

	}

	public static void saveNeuralNetwork(String sFileName) {
		File f = new File(sFileName);

		if (!f.isFile()) { // If the file doesn't exist yet

			try {
				FileWriter writer = new FileWriter(sFileName);

				//				for (MyThread thread : Glv.threads) {
				//					for (String s : thread.spaceSyntax.toNN) {
				//						writer.append(s);
				//					}
				//				}

				for (int i = 0; i < Glv.threadNN.net.neuralnet.m_hidden_layer.length; i++) {
					for (int j = 0; j < Glv.threadNN.net.neuralnet.m_hidden_layer[i].length; j++) {
						for (int k = 0; k < Glv.threadNN.net.neuralnet.m_hidden_layer[i][j].m_weights.length; k++) {
							for (int l = 0; l < Glv.threadNN.net.neuralnet.m_hidden_layer[i][j].m_weights[k].length; l++) {
								writer.append(Float
										.toString(Glv.threadNN.net.neuralnet.m_hidden_layer[i][j].m_weights[k][l]));
								writer.append(",");
							}
							writer.append(",");
						}
						writer.append("\n");
					}
					writer.append("\n");
				}

				writer.append("_");

				for (int i = 0; i < Glv.threadNN.net.neuralnet.m_output_layer.length; i++) {
					for (int j = 0; j < Glv.threadNN.net.neuralnet.m_output_layer[i].length; j++) {
						for (int k = 0; k < Glv.threadNN.net.neuralnet.m_output_layer[i][j].m_weights.length; k++) {
							for (int l = 0; l < Glv.threadNN.net.neuralnet.m_output_layer[i][j].m_weights[k].length; l++) {
								writer.append(Float
										.toString(Glv.threadNN.net.neuralnet.m_output_layer[i][j].m_weights[k][l]));
								writer.append(",");
							}
							writer.append(",");
						}
						writer.append("\n");
					}
					writer.append("\n");
				}

				// writer.append("DisplayName");

				writer.flush();
				writer.close();
				if (Glv.shP)
					System.out.println("Done writing CSV.");

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (Glv.shP)
			System.out.println("File already exists.");

	}

	public static void loadNeuralNetwork() {
		String filePath = new File("").getAbsolutePath();

		String csvFile = filePath + "/src/data/sur.csv";

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				PVector temp = new PVector();
				// use comma as separator
				String[] thisRow = line.split(cvsSplitBy);

				// p.println(buildings.size() + " temp.z: " + temp.z);

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

		if (Glv.shP)
			System.out.println("CSV Data Loaded!");
	}
}

//
//writer.append("<PROPERTIES OF NETWORK>"); // Section divider.
//writer.append("\n");
//{
//	writer.append("size of Input");
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.neuralnet.m_input_layer.length));
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.neuralnet.m_input_layer[0].length));
//	writer.append("\n");
//
//	writer.append("size of Hidden");
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.neuralnet.m_hidden_layer.length));
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.neuralnet.m_hidden_layer[0].length));
//	writer.append("\n");
//
//	writer.append("size of Output");
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.neuralnet.m_output_layer.length));
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.neuralnet.m_output_layer[0].length));
//	writer.append("\n");
//
//	writer.append("Number of Chosen");
//	writer.append(",");
//	writer.append(counter);
//	writer.append("\n");
//
//	writer.append("Avg Distance of Neurons");
//	writer.append(",");
//	writer.append(avarageDistanceOfNeurons);
//	writer.append("\n");
//	
//	writer.append("Learning Rate");
//	writer.append(",");
//	writer.append(Float.toString(Glv.LEARNING_RATE));
//	writer.append("\n");
//	
//	writer.append("Cards shown/ cycle");
//	writer.append(",");
//	writer.append(Float.toString(Glv.numOfLearning));
//	writer.append("\n");
//}
//
//writer.append("\n");
//writer.append("<PROPERTIES OF CARDS>"); // Section divider.
//writer.append("\n");
//{
//	writer.append("Size of Dataset");
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.trainingSet.size() + Glv.threadNN.net.testingSet.size()));
//	writer.append("\n");
//	
//	writer.append("Size of Teachingset");
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.trainingSet.size()));
//	writer.append("\n");
//	
//	writer.append("Size of Testingset");
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.testingSet.size()));
//	writer.append("\n");
//	
//	writer.append("Size of Analysis Original");
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.testingSet.get(0).analysis.size()));
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.testingSet.get(0).analysis.get(0).length));
//	writer.append("\n");
//	
//	writer.append("Size of Analysis Reduced");
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.testingSet.get(0).rAnalysis.length));
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.testingSet.get(0).rAnalysis[0].length));
//	writer.append("\n");
//	
//	writer.append("Size of Form Original");
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.testingSet.get(0).form.size()));
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.testingSet.get(0).form.get(0).length));
//	writer.append("\n");
//	
//	writer.append("Size of Form Reduced");
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.testingSet.get(0).rForm.length));
//	writer.append(",");
//	writer.append(Integer.toString(Glv.threadNN.net.testingSet.get(0).rForm[0].length));
//	writer.append("\n");
//}
//
//writer.append("\n");
//writer.append("<PEFORMANCE OF NETWORK>"); // Section divider.
//writer.append("\n");
//{
//	writer.append("NN x");
//	writer.append(",");
//	for (PVector myVector : Glv.errorCounter) {
//		writer.append(Float.toString(myVector.x));
//		writer.append(",");
//	}	
//	writer.append("\n");
//	
//	writer.append("NN f(x)");
//	writer.append(",");
//	for (PVector myVector : Glv.errorCounter) {
//		writer.append(Float.toString(myVector.y));
//		writer.append(",");
//	}	
//	writer.append("\n");
//	
//	writer.append("VGA x");
//	writer.append(",");
//	for (PVector myVector : Glv.errorCounter2) {
//		writer.append(Float.toString(myVector.x));
//		writer.append(",");
//	}	
//	writer.append("\n");
//	
//	writer.append("VGA f(x)");
//	writer.append(",");
//	for (PVector myVector : Glv.errorCounter2) {
//		writer.append(Float.toString(myVector.y));
//		writer.append(",");
//	}
//	writer.append("\n");
//}
//
//writer.append("\n");
//writer.append("<SELECTED NEURONS>"); // Section divider.
//writer.append("\n");
//{
//	for (int i = 0; i < env.editorLayer.length; i++) {
//		for (int j = 0; j < env.editorLayer[i].length; j++) {
//			if(env.editorLayer[i][j].iAmChosen) writer.append("1");
//			else writer.append("0");
//			
//			writer.append(",");
//		}
//		writer.append("\n");
//	}
//	writer.append("\n");
//}
