package firsteclipse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import processing.core.PVector;
import sun.net.www.content.text.plain;

public class GenerateCSV {

	public static void save(String fileName, String myDirectory) {
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
						if (i == Glv.threads.size() - 1 && j == thread.spaceSyntax.toNN.size() - 2) {
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
			Environment env) {
		File f = new File(sFileName);

		final String[][] csvMatrix = new String[Glv.errorCounter.size() + 2][10+env.editorLayer[0].length];

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
		csvMatrix[10][1] =Integer.toString(Glv.threadNN.net.trainingSet.size());
		
		csvMatrix[11][0] = "Size of Testingset";
		csvMatrix[11][1] =Integer.toString(Glv.threadNN.net.testingSet.size());
		
		csvMatrix[12][0] = "Size of Analysis Original";
		csvMatrix[12][1] =Integer.toString(Glv.threadNN.net.testingSet.get(0).analysis.size());
		csvMatrix[12][2] =Integer.toString(Glv.threadNN.net.testingSet.get(0).analysis.get(0).length);


		csvMatrix[13][0] = "Size of Analysis Reduced";
		csvMatrix[13][1] = Integer.toString(Glv.threadNN.net.testingSet.get(0).rAnalysis.length);
		csvMatrix[13][2] = Integer.toString(Glv.threadNN.net.testingSet.get(0).rAnalysis[0].length);
		
		csvMatrix[14][0] = "Size of Form Original";
		csvMatrix[14][1] = Integer.toString(Glv.threadNN.net.testingSet.get(0).form.size());
		csvMatrix[14][2] = Integer.toString(Glv.threadNN.net.testingSet.get(0).form.get(0).length);
		
		csvMatrix[15][0] = "Size of Form Reduced";
		csvMatrix[15][1] = Integer.toString(Glv.threadNN.net.testingSet.get(0).rForm.length);
		csvMatrix[15][2] = Integer.toString(Glv.threadNN.net.testingSet.get(0).rForm[0].length);
		
		csvMatrix[0][5] = "<PEFORMANCE OF NETWORK>";
		csvMatrix[1][5] = "NN x";
		csvMatrix[1][6] = "NN f(x)";

		csvMatrix[1][7] = "VGA x";
		csvMatrix[1][8] = "VGA f(x)";
		
		csvMatrix[0][10] = "<FORM OF SELECTED>";

		int i = 0;
		for (PVector myVector : Glv.errorCounter) {
			csvMatrix[2 + i][5] = Float.toString(myVector.x);
			i++;
		}

		i = 0;
		for (PVector myVector : Glv.errorCounter) {
			csvMatrix[2 + i][6] = Float.toString(myVector.y);
			i++;
		}

		i = 0;
		for (PVector myVector : Glv.errorCounter2) {
			csvMatrix[2 + i][7] = Float.toString(myVector.x);
			i++;
		}

		i = 0;
		for (PVector myVector : Glv.errorCounter2) {
			csvMatrix[2 + i][8] = Float.toString(myVector.y);
			i++;
		}
		
		for (int k = 0; k < env.editorLayer.length; k++) {
			for (int l = 0; l < env.editorLayer[k].length; l++) {
				if (env.editorLayer[k][l].iAmChosen)
					csvMatrix[1 + k][10+l]="1";
				else
					csvMatrix[1 + k][10+l]="0";
			}
		}

		if (!f.isFile()) { // If the file doesn't exist yet

			try {
				FileWriter writer = new FileWriter(sFileName);

				for (int j = 0; j < csvMatrix.length; j++) {
					for (int j2 = 0; j2 < csvMatrix[j].length; j2++) {
						
						if(csvMatrix[j][j2] != null) writer.append(csvMatrix[j][j2]);
						else writer.append("");
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
