package firsteclipse;

import java.awt.event.InputMethodListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.util.ElementKindVisitor6;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import com.sun.corba.se.impl.naming.cosnaming.TransientBindingIterator;
import com.sun.media.jfxmedia.events.NewFrameEvent;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.tracing.dtrace.ProviderAttributes;
import com.sun.webkit.ThemeClient;
import com.sun.xml.internal.org.jvnet.mimepull.CleanUpExecutorFactory;

import controlP5.Println;
import processing.core.PApplet;
import processing.core.PVector;
import sun.awt.windows.ThemeReader;

public class NeuralNetworkManagment {

	private static PApplet p;
	public ArrayList<MyData> trainingSet = new ArrayList<MyData>();
	public ArrayList<MyData> testingSet = new ArrayList<MyData>();

	public Network neuralnet;
	public Network[] splitNeuralnets;
	public boolean dataLoaded = false;

	public static float[] g_sigmoid = new float[200]; // The precalculated values for the sigmoid function are contained this.

	List <MyThread> threads;
	List <Integer> shownCardIds= new ArrayList<Integer>();
	//MyThread thread;

	public NeuralNetworkManagment(PApplet _p) {
		p = _p;

		setupSigmoid();
	}

	public void drawSplitNetworks(Environment env) {
		splitNeuralnets[0].drawAnalysis(env);

		for (int i = 1; i < splitNeuralnets.length; i++) {
			splitNeuralnets[i].drawOutput();
		}
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
			if (listOfFiles.length * 10f >= Glv.numOfRead) {
				for (int i = 0; i < Glv.numOfRead; i++) {

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

							/*
							for (int j = 0; j < thisRow.length; j++) { // Go through each element of thisRow.
							
								if (!thisRow[j].isEmpty()) {
									char c = thisRow[j].charAt(0);
							
									switch (c) {
									case '_':
										if (numberOfAnalysis % Glv.divisionOfTestingTraining != 0)
											trainingSet.add(new MyData(p)); // Add new object MyData, essentially a new form + analysis.
										else
											testingSet.add(new MyData(p));
										analysisOrForm = false;
										numberOfAnalysis++;
										isItID = true;
										break;
									case ':':
										analysisOrForm = !analysisOrForm; // Is the data I am looking at the form or the analysis?
										isItID = false;
										break;
									default:
										if (isItID) {
											data.analysisID = Integer.valueOf(thisRow[j]);
											isItID = false;
										} else if (analysisOrForm) {
											data.form.add(thisRow); // If form put line in ArrayList of MyData form.
											isItID = false;
										} else {
											data.analysis.add(thisRow); // If analysis put line in ArrayList of MyData analysis.
											isItID = false;
										}
										break;
									}
								}
							}
							*/

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
											//										for (int k = 0; k < thisRow.length; k++) {
											//											p.println("thisRow: " + thisRow[k]);	
											//										}
											//										p.println("_______________");	
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
					p.println("Training set size: " + trainingSet.size() + " | " + "Testing set size: "
							+ testingSet.size());

				cleanupReadData(); // Goes through each file and eliminates ones that are not suitable.
				setHighLow(); // Calculates the range of visibility in the data.
				convertData(); // Convert the data into a -1 to 1 and maps the data to high low.
				extractValuableForm(); // Converts the outputs to the dimensionality-reducted grid size and cuts the analysis data to the right points.
				//setupNeuralNetwork(); // Initialises the network and feeds in the size of the analysis and form.

				dataLoaded = true;
			} else {
				System.out.println("There is not enough existing solutions.");
			}
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
					if (strings[i] != "\n" && strings[i] != "" && strings[i] != null) {
						try {
							int num = Integer.valueOf(strings[i]);
							if (num > Glv.highLowForNN.x) {
								Glv.highLowForNN.x = num;
								Glv.cardContainingHighest = testingSet.indexOf(data);
								//p.println("Card containing lowest: " + Glv.cardContainingHighest);
							}
							if (num < Glv.highLowForNN.y && num > 6)
								Glv.highLowForNN.y = num;
						} catch (NumberFormatException ex) { // handle your exception
							p.println("Was not a number: " + strings[i]);
						}

					}
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

	/*
	 * NEURAL NETWORK
	 */
	//---> SETUP NN.
	public void setupNeuralNetwork(Environment env) {
		//---> The decision here: 1. Have I specified certain input neurons with the editor? 2. Am I doing generating or analysis 3. I am doing analysis optimisation!

		threads = new ArrayList<MyThread>();
		
		if (dataLoaded) {
			switch (Glv.genOrA) {

			case 0:
				if (Glv.neuronsStored) {
					setupSpecifiedNeurons();
					createNetwork(trainingSet.get(0), trainingSet.get(0).rAnalysis, 0, env);
				} else
					p.println("Please specify neurons in editor");
				break;

			case 1:
				setupRawNeurons();
				createNetwork(trainingSet.get(0), trainingSet.get(0)._analysis, 0, env);
				break;

			case 2:
				if (Glv.neuronsStored) {
					if (!Glv.splitNetwork) {
						setupAnalysisNeurons();
						createNetwork(trainingSet.get(0), trainingSet.get(0).rForm, 1, env);
					} else {
						setupSplitNeurons(calcSplit(), env); // Calculates how many parts it should be split then creates the networks
					}
				} else
					p.println("Please specify neurons in editor");
				break;
			}
		} else
			System.out.println("Please Load Data");

	}

	private void setupSpecifiedNeurons() {
		Glv.netSize[0] = trainingSet.get(0).rAnalysis.length;
		Glv.netSize[1] = trainingSet.get(0).rAnalysis[0].length;

		Glv.netSize[4] = trainingSet.get(0).rForm.length;
		Glv.netSize[5] = trainingSet.get(0).rForm[2].length;

		if (Glv.netSize[0] * Glv.netSize[1] > Glv.netSize[4] * Glv.netSize[5]) { // Depends if the input or the output layer is bigger the hidden layer's size is chosen accordinglyhgt21q`	a\2R
			Glv.netSize[2] = p.floor(Glv.netSize[0] * Glv.howMuchBiggerHidden);
			Glv.netSize[3] = p.ceil(Glv.netSize[1] * Glv.howMuchBiggerHidden);
		} else {
			Glv.netSize[2] = p.floor(Glv.netSize[4] * Glv.howMuchBiggerHidden);
			Glv.netSize[3] = p.ceil(Glv.netSize[5] * Glv.howMuchBiggerHidden);
		}
	}

	//---> For spliting
	private void setupSplitNeurons(int split, Environment env) {

		Neuron[][] selectedNeurons = new Neuron[env.editorLayer.length][env.editorLayer[0].length];
		PVector[][] temporaryNeuronsPosition = new PVector[env.editorLayer.length][env.editorLayer[0].length];

		for (int k = 0; k < temporaryNeuronsPosition.length; k++) {
			for (int l = 0; l < temporaryNeuronsPosition[k].length; l++) {
				temporaryNeuronsPosition[k][l] = new PVector(env.editorLayer[k][l].position.x,
						env.editorLayer[k][l].position.y);
				selectedNeurons[k][l] = new Neuron(p, env.editorLayer[k][l].position, new Neuron[10][10],
						env.editorLayer[k][l].idNum);// env.editorLayer[k][l];
			}
		}

		//		for (int i = 0; i < env.editorLayer.length; i++) {
		//			for (int j = 0; j < env.editorLayer[i].length; j++) {
		//				p.println(env.editorLayer[i][j].idNum);
		//			}
		//		}

		Neuron[][] justSelected = takeOutSelected(env.editorLayer); // Creates an array that only has the chosen ones.
		//printThem(selectedNeurons, justSelected);

		List<Neuron[][]> subArrays = createSubArrays(justSelected, Glv.splitSize, Glv.splitSize);
		List<Neuron[][]> weededOutSubArrays = weedOutNull(subArrays);

		//		for (Neuron subArray[][] : subArrays) {
		//			//Neuron [][] subArray=weedOutNull(subArray);
		//			printOne(subArray);
		//		}

		//		for (Neuron subArray[][] : weededOutSubArrays) {
		//			printOne(subArray);
		//		}

		splitNeuralnets = new Network[subArrays.size()];

		int[] myNetSize = new int[6];

		myNetSize[0] = trainingSet.get(0).rForm.length;
		myNetSize[1] = trainingSet.get(0).rForm[2].length;

		for (int i = 0; i < weededOutSubArrays.size(); i++) {

			Neuron[][] temp = weededOutSubArrays.get(i);
			myNetSize[4] = temp.length; //trainingSet.get(0).rAnalysis.length;
			myNetSize[5] = temp[0].length;

			myNetSize[2] = p.floor(myNetSize[4] * Glv.howMuchBiggerHidden);
			myNetSize[3] = p.ceil(myNetSize[5] * Glv.howMuchBiggerHidden);

			splitNeuralnets[i] = new Network(p, myNetSize[0], myNetSize[1], myNetSize[2], myNetSize[3], myNetSize[4],
					myNetSize[5], weededOutSubArrays.get(i), i);
			//			System.out.println("InputSize: " + myNetSize[0] + " | " + myNetSize[1] + " HiddenSize: " + myNetSize[2]
			//					+ " | " + myNetSize[3] + " OutputSize: " + myNetSize[4] + " | " + myNetSize[5]);
		}

		for (int i = 0; i < splitNeuralnets.length; i++) {
			splitNeuralnets[i].respond(trainingSet.get(0), trainingSet.get(0).rForm);
		}

	}

	private void printOne(Neuron[][] one) {
		for (int i = 0; i < one.length; i++) {
			for (int j = 0; j < one[i].length; j++) {
				if (one[i][j] != null)
					if (one[i][j].iAmChosen == true)
						p.print("0");
					else
						p.print("1");
				else
					p.print("N");
			}
			p.println("");
		}

		p.println("___");
		p.println("");

	}

	private void printThem(Neuron[][] one, Neuron[][] two) {
		for (int i = 0; i < one.length; i++) {
			for (int j = 0; j < one[i].length; j++) {
				if (one[i][j].iAmChosen == true)
					p.print("0");
				else
					p.print("1");
			}
			p.println("");
		}
		p.println("___");
		p.println("");

		for (int i = 0; i < two.length; i++) {
			for (int j = 0; j < two[i].length; j++) {
				if (two[i][j].iAmChosen == true)
					p.print("0");
				else
					p.print("1");
			}
			p.println("");
		}
	}

	private List<Neuron[][]> weedOutNull(List<Neuron[][]> oneNetwork) {
		List<Neuron[][]> weededOut = new ArrayList<Neuron[][]>();

		for (Neuron[][] neurons : oneNetwork) {

			int sizeX = 0, sizeY = 0;

			for (int i = 0; i < neurons.length; i++) {
				if (neurons[i][0] != null)
					sizeX++;
			}
			for (int i = 0; i < neurons[0].length; i++) {
				if (neurons[0][i] != null)
					sizeY++;
			}

			Neuron[][] newNetwork = new Neuron[sizeX][sizeY];

			int k = 0, l = 0;
			for (int i = 0; i < neurons.length; i++) {
				for (int j = 0; j < neurons[0].length; j++) {

					if (neurons[i][j] != null) {
						if (k < newNetwork.length && l < newNetwork[0].length)
							newNetwork[k][l] = neurons[i][j];

						l++;
						if (l >= newNetwork[0].length) {
							l = 0;
							k++;
						}
					}
				}
			}
			weededOut.add(newNetwork);
		}
		return weededOut;
	}

	private Neuron[][] takeOutSelected(Neuron[][] selectedNeuron) { // This cuts out all parts that are not chosen.
		int sizeX = 0;
		int sizeY = 0;

		for (int i = 0; i < selectedNeuron.length; i++) {
			if (selectedNeuron[i][10].iAmChosen)
				sizeX++;
		}
		for (int j = 0; j < selectedNeuron[10].length; j++) {
			if (selectedNeuron[10][j].iAmChosen)
				sizeY++;
		}

		//p.println("sizeX: " + sizeX + " | Size Y: " + sizeY);

		Neuron[][] temp = new Neuron[sizeX][sizeY];
		int k = 0, l = 0;
		for (int i = 0; i < selectedNeuron.length; i++) {
			for (int j = 0; j < selectedNeuron[0].length; j++) {

				if (selectedNeuron[i][j].iAmChosen) {

					//p.println("i: " + i + " | j: " + j);
					//p.println("k: " + k + " | l: " + l);
					if (k < temp.length && l < temp[0].length)
						temp[k][l] = selectedNeuron[i][j];

					l++;
					if (l >= temp[0].length) {
						l = 0;
						k++;
					}
				}
			}
		}

		return temp;
	}

	private static List<Neuron[][]> createSubArrays(Neuron inputArray[][], int subRows, int subCols) {
		List<Neuron[][]> subArrays = new ArrayList<Neuron[][]>();
		for (int r = 0; r < inputArray.length; r += subRows) {
			for (int c = 0; c < inputArray[r].length; c += subCols) {
				Neuron subArray[][] = new Neuron[subRows][subCols];
				fillSubArray(inputArray, r, c, subArray);
				subArrays.add(subArray);
			}
		}
		return subArrays;
	}

	private static void fillSubArray(Neuron[][] inputArray, int r0, int c0, Neuron subArray[][]) {
		for (int r = 0; r < subArray.length; r++) {
			for (int c = 0; c < subArray[r].length; c++) {
				int ir = r0 + r;
				int ic = c0 + c;
				if (ir < inputArray.length && ic < inputArray[ir].length) {
					subArray[r][c] = inputArray[ir][ic];
				}
			}
		}
	}

	private int calcSplit() {
		return p.ceil((trainingSet.get(0).rAnalysis.length * trainingSet.get(0).rAnalysis[0].length) / Glv.splitSize);
	}
	//<---

	private void setupRawNeurons() {
		Glv.netSize[0] = trainingSet.get(0)._analysis.length;
		Glv.netSize[1] = trainingSet.get(0)._analysis[2].length;

		Glv.netSize[2] = p.ceil(trainingSet.get(0)._analysis.length * Glv.howMuchBiggerHidden);
		Glv.netSize[3] = p.ceil(trainingSet.get(0)._analysis[2].length * Glv.howMuchBiggerHidden);

		Glv.netSize[4] = trainingSet.get(0).rForm.length;
		Glv.netSize[5] = trainingSet.get(0).rForm[2].length;
	}

	private void setupAnalysisNeurons() {

		Glv.netSize[0] = trainingSet.get(0).rForm.length;
		Glv.netSize[1] = trainingSet.get(0).rForm[2].length;

		Glv.netSize[4] = trainingSet.get(0).rAnalysis.length;
		Glv.netSize[5] = trainingSet.get(0).rAnalysis[0].length;

		if (Glv.netSize[0] * Glv.netSize[1] > Glv.netSize[4] * Glv.netSize[5]) { // Depends if the input or the output layer is bigger the hidden layer's size is chosen accordinglyhgt21q`	a\2R
			Glv.netSize[2] = p.floor(Glv.netSize[0] * Glv.howMuchBiggerHidden);
			Glv.netSize[3] = p.ceil(Glv.netSize[1] * Glv.howMuchBiggerHidden);
		} else {
			Glv.netSize[2] = p.floor(Glv.netSize[4] * Glv.howMuchBiggerHidden);
			Glv.netSize[3] = p.ceil(Glv.netSize[5] * Glv.howMuchBiggerHidden);
		}

	}

	private void createNetwork(MyData card, Float[][] inputs, int dummyNum, Environment env) {

		if (dummyNum == 0) {
			neuralnet = new Network(p, Glv.netSize[0], Glv.netSize[1], Glv.netSize[2], Glv.netSize[3], Glv.netSize[4],
					Glv.netSize[5]);
		} else {
			neuralnet = new Network(p, Glv.netSize[0], Glv.netSize[1], Glv.netSize[2], Glv.netSize[3], Glv.netSize[4],
					Glv.netSize[5], env.editorLayer);
		}

		System.out.println("InputSize: " + Glv.netSize[0] + " | " + Glv.netSize[1] + " HiddenSize: " + Glv.netSize[2]
				+ " | " + Glv.netSize[3] + " OutputSize: " + Glv.netSize[4] + " | " + Glv.netSize[5]);

		if (Glv.genOrA != 2)
			backTo3D(env); // Generate the first random form it created.

		neuralnet.respond(card, inputs);
	}

	//<--- SETUP NN

	//---> Interact with NN.
	public void trainNN(DataAnalysis graphs, Environment env) {

		if (dataLoaded) {
			//training(10, graphs, Glv.howManyCycles); // This is so we have the number the NN original performs as.

			for (int Z = 0; Z < Glv.numOfCycles; Z++) {
				training(Glv.numOfLearning, graphs, Glv.howManyCycles);
				
				if (Glv.genOrA != 2)
					backTo3D(env); // Crates a new thread and calculates the SpaceSyntax analysis according to the generated form. 
			}

			

		}

	}

	private void training(int numOfLearning, DataAnalysis graphs, float num) {
		float counter = 0f;
		float counterNormal = 0f;
		for (int i = 0; i < Glv.numOfLearning; i++) {
			int row = (int) p.floor(p.random(0, trainingSet.size()));
			
			switch (Glv.genOrA) {
			case 0:
				if (Glv.neuronsStored)
					trainIt(trainingSet.get(row), trainingSet.get(row).rAnalysis, trainingSet.get(row).rForm);
				else
					p.println("Please specify neurons in editor");
				break;
			case 1:
				trainIt(trainingSet.get(row), trainingSet.get(row)._analysis, trainingSet.get(row).rForm);
				break;
			case 2:
				if (Glv.neuronsStored) {
					if (Glv.splitNetwork) {
						trainIt(trainingSet.get(row), trainingSet.get(row).rForm, trainingSet.get(row)._analysis);
					} else
						trainIt(trainingSet.get(row), trainingSet.get(row).rForm, trainingSet.get(row).rAnalysis);
				}
				break;
			}
			
			shownCardIds.add(row);

			if (Glv.splitNetwork) {
				for (int m = 0; m < splitNeuralnets.length; m++) {
					for (int j = 0; j < Glv.threadNN.net.splitNeuralnets[m].m_output_layer.length; j++) {
						for (int k = 0; k < Glv.threadNN.net.splitNeuralnets[m].m_output_layer[j].length; k++) {
							counter += (Math.pow(Glv.threadNN.net.splitNeuralnets[m].m_output_layer[j][k].m_error, 2f)
									/ 2f); // Counts all the mean squared error of the last learning phase.
							counterNormal += p.abs(Glv.threadNN.net.splitNeuralnets[m].m_output_layer[j][k].m_error);

						}
					}
				}
			} else {
				for (int j = 0; j < Glv.threadNN.net.neuralnet.m_output_layer.length; j++) {
					for (int k = 0; k < Glv.threadNN.net.neuralnet.m_output_layer[j].length; k++) {
						counter += (Math.pow(Glv.threadNN.net.neuralnet.m_output_layer[j][k].m_error, 2f) / 2f); // Counts all the mean squared error of the last learning phase.
						counterNormal += p.abs(Glv.threadNN.net.neuralnet.m_output_layer[j][k].m_error); // Counts all the error of the last learning phase.
					}
				}
			}
		}

		counter /= Glv.numOfLearning;
		counterNormal /= Glv.numOfLearning;
		float precentage = counter;
		float precentageNormal = counterNormal;

		if (Glv.splitNetwork) {
			double possibleErrors = 0.0f;
			float numOfAllNeurons = 0.0f;
			for (int m = 0; m < splitNeuralnets.length; m++) {
				numOfAllNeurons += (splitNeuralnets[m].m_output_layer.length
						* splitNeuralnets[m].m_output_layer[0].length);
			}
			possibleErrors = numOfAllNeurons * (Math.pow(2f, 2f)) / 2f; // Counts all the error of the last learning phase.
			precentage /= possibleErrors;
			precentageNormal /= numOfAllNeurons * 2f;
		} else {
			precentage /= (((Glv.threadNN.net.neuralnet.m_output_layer.length
					* Glv.threadNN.net.neuralnet.m_output_layer[0].length) * (Math.pow(2f, 2f)) / 2f));
			precentageNormal /= Glv.threadNN.net.neuralnet.m_output_layer.length
					* Glv.threadNN.net.neuralnet.m_output_layer[0].length * 2f;
		}
		precentage *= 100f;
		precentageNormal *= 100f;


		Glv.howManyCycles++;
		Glv.errorCounter.add(new PVector(num * Glv.numOfLearning, precentage));
		Glv.errorCounterNormal.add(new PVector(num * Glv.numOfLearning, precentageNormal));
		//Glv.errorCounter.add(new PVector(Glv.howManyCycles, precentage));
		//p.println(counter);
		graphs.lineChart.setData(Glv.errorCounter);
		System.out.println("Squared Mean Error: " + precentage + "%  |  Error: " + precentageNormal + "%");
		

		Glv.bestMSE = setSmaller(precentage, Glv.bestMSE);
		Glv.bestE = setSmaller(precentageNormal, Glv.bestE);
		
		//p.println("smallest: " + Glv.bestMSE);
	}

	private void trainIt(MyData card, Float[][] inputs, Float[][] outputs) {

		if (inputs != null && outputs != null) {
			if (Glv.splitNetwork) {

				if (trainingSet.get(0)._analysis.length == outputs.length
						&& trainingSet.get(0)._analysis[0].length <= outputs[0].length) {
					for (int m = 0; m < splitNeuralnets.length; m++) {

						Float[][] selectedOutputs = new Float[splitNeuralnets[m].m_output_layer.length][splitNeuralnets[m].m_output_layer[0].length];

						int counter = 0;

						//p.println("outputs.length: " + outputs.length + " | outputs[i].l: " + outputs[0].length);
						for (int i = 0; i < splitNeuralnets[m].m_output_layer.length; i++) {
							for (int j = 0; j < splitNeuralnets[m].m_output_layer[i].length; j++) {
								//p.println("id X: " + splitNeuralnets[m].m_output_layer[i][j].idNum.x + " | id Y: " + splitNeuralnets[m].m_output_layer[i][j].idNum.y);

								//							p.print((int) splitNeuralnets[m].m_output_layer[i][j].idNum.x
								//				                                   *splitNeuralnets[m].m_output_layer[i].length
								//						+ (int) splitNeuralnets[m].m_output_layer[i][j].idNum.y);
								PVector id = splitNeuralnets[m].m_output_layer[i][j].idNum;
								if ((int) id.x < outputs.length && (int) id.y < outputs[0].length) {
									//								p.println("outputs.length: " + outputs.length + " | " + outputs[0].length + "idX: "
									//										+ (int) splitNeuralnets[m].m_output_layer[i][j].idNum.x + "idY: "
									//										+ (int) splitNeuralnets[m].m_output_layer[i][j].idNum.y);
									selectedOutputs[i][j] = outputs[(int) id.x][(int) id.y];
									//									//									* Glv.threadNN.net.trainingSet.get(0)._analysis.length
									//									* splitNeuralnets[m].m_output_layer.length
									//									+ (int) splitNeuralnets[m].m_output_layer[i][j].idNum.y];
								}
							}
						}
						splitNeuralnets[m].respond(card, inputs);
						splitNeuralnets[m].train(selectedOutputs);
					}

				} else
					p.println("this card was faulty.");
			} else {
				neuralnet.respond(card, inputs);
				neuralnet.train(outputs);
			}
		} else {
			p.println("Card was NULL");
			trainingSet.remove(card);
		}

	}

	public void testNN(Environment env) {

		int row = (int) p.floor(p.random(0, testingSet.size()));

		switch (Glv.genOrA) {
		case 0:
			if (Glv.neuronsStored)
				testIt(testingSet.get(row), testingSet.get(row).rAnalysis);
			else
				p.println("Please specify neurons in editor");
			break;
		case 1:
			testIt(testingSet.get(row), testingSet.get(row)._analysis);
			break;
		case 2:
			if (Glv.neuronsStored) {
				testIt(testingSet.get(row), testingSet.get(row).rForm);
			}
			break;
		}

		if (Glv.genOrA != 2)
			backTo3D(env);
	}

	private void testIt(MyData card, Float[][] inputs) {
		if (inputs != null) {
			if (Glv.splitNetwork) {
				for (int i = 0; i < splitNeuralnets.length; i++) {
					splitNeuralnets[i].respond(card, inputs);
				}
			} else
				neuralnet.respond(card, inputs);
		} else {
			p.println("Card was NULL");
			testingSet.remove(card);
		}
	}
	
	public  float setSmaller(float input, float comparingTo)
	{
		//p.println("input: " + input + "comparingTo: " + comparingTo);
		if(input<comparingTo) return input;
		else return comparingTo;
	}
	//<---

	/*
	 * OTHER
	 */
	//---> For getting data back into Analysis & 3D
	public void backTo3D(Environment env) {
		threads.add(new MyThread(p, 1000, env.editorLayer));

		if (!threads.get(threads.size()-1).VGADone) // If it is a new thread then start it!
			threads.get(threads.size()-1).start();
		
//		if (thread.VGADone)
//			graphs.compare(env);
	}

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
