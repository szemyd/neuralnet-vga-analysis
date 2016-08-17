package firsteclipse;

import java.util.ArrayList;

import com.sun.org.glassfish.external.statistics.Statistic;

import processing.core.PVector;

public class Glv {
	/* 
	 * SETTINGS
	 */
	public static float cubeSizeReduced = 20.0f; // How big should the reduced cubes be.
	public static boolean shouldDimReduction = true; // Should the generated cubes be reduced.
	public static boolean shouldSpaceSyntax = true; // Should the SpaceSyntax Analysis work
	public static boolean globalHighLow = true; // Should the mapping of the SpaceSyntax colouring be local relative or global relative.
	public static boolean shP = true; // Should it print to the console?
	public static boolean VGAormeanShort = true;

	public static float chance = 99.5f; // How big are the chances that a cube is a seed

	//---> Solution settings and optimal calculation
	public static int numOfThreads = 1; // How many parallel threads should there be
	public static int numOfSolutions = 1; // How many solutions do I want to generate

	//---> Form generating settings
	public static float cubeSize = 5f; // How big is the cube that we are using.
	public static float spaceCubeSize = 5f; // How big is the one grid rectangle.

	public static float roomSizeX = 129.4f; // How big is the park
	public static float roomSizeY = 160.42f;

	public static float spaceRoomSizeX = 129.4f * 1.5f; // How big is the SpaceSyntax analysis.
	public static float spaceRoomSizeY = 160.42f * 1.5f;

	//---> NEURAL NETWORK
	public static float LEARNING_RATE = 0.001f; // This damms the speed at which the backpropagation happens: slower learning rate.
	public static int divisionOfTestingTraining = 5; // How should the testing and training test be divided.
	public static ArrayList<ArrayList<Integer>> whichInputs;// = new ArrayList<ArrayList<Integer>>();
	public static boolean neuronsStored = false; // Have I received the specified neurons?
	public static Integer[] netSize = new Integer[6]; // How many neurons should the layer contain. INPUT || HIDDEN || OUTPUT
	public static int numOfLearning = 500; // How many cards should I show with one click?
	public static int numOfCycles = 10;

	public static ArrayList<PVector> errorCounter = new ArrayList<PVector>();
	public static ArrayList<PVector> errorCounter2 = new ArrayList<PVector>();
	public static float howManyCycles = 0f; // How often have I pressed "train NN". Shown a predefined number of cards.
	public static float howMuchBiggerHidden = 2.5f;

	public static int genOrA = 0; // Am I feeding in to create the public space or to get a certain analysis? 

	//---> For the ENVIRONMENT
	public static int programMode = 0; // Should it generate data or load Neural Network?
	public static boolean newOrNet = false; // Should I display in 3D the data generating or the current network?
	public static boolean editorForAnalysisOn = false;

	/* 
	 * OTHER GLOBAL VARIABLES
	 */
	public static int howManyUntilNow = 0; // How many solutions have been already done.

	public static int divisionX = ((int) roomSizeX / (int) cubeSize);
	public static int divisionY = ((int) roomSizeY / (int) cubeSize);

	public static int spaceDivisionX = ((int) spaceRoomSizeX / (int) spaceCubeSize);
	public static int spaceDivisionY = ((int) spaceRoomSizeY / (int) spaceCubeSize);

	public static int seed = 2020;
	public static int initialSeed = seed;
	public static int whichToDisplay = 0;
	public static int isDone = 0;
	public static boolean isDoneBool = false;

	/*
	 * For SpaceSytntax
	 */
	public static PVector highLow = new PVector(0f, 1000f);
	//public static ArrayList<String> toNN = new ArrayList<String>();
	public static PVector highLowForNN = new PVector(0f, 1000f);
	public static int cardContainingHighest = 0;

	public static ArrayList<MyThread> threads = new ArrayList<MyThread>();
	public static MyThreadNeuralNet threadNN;
	public static boolean threadSuspended = false;

	public static int neuronSize = 16;
}
