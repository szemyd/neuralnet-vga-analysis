package firsteclipse;

import java.util.ArrayList;

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

	public static float chance = 99.5f; // How big are the chances that a cube is a seed

	//---> Solution settings and optimal calculation
	public static int numOfThreads = 15; // How many parallel threads should there be
	public static int numOfSolutions =29; // How many solutions do I want to generate

	//---> Form generating settings
	public static float cubeSize = 5f; // How big is the cube that we are using.
	public static float spaceCubeSize = 5f; // How big is the one grid rectangle.

	public static float roomSizeX = 129.4f; // How big is the park
	public static float roomSizeY = 160.42f; 

	public static float spaceRoomSizeX = 129.4f * 1.50f; // How big is the SpaceSyntax analysis.
	public static float spaceRoomSizeY = 160.42f * 1.50f;

	/* 
	 * OTHER GLOBAL VARIABLES
	 */
	public static int howManyUntilNow = 0; // How many solutions have been already done.

	public static int divisionX = ((int) roomSizeX / (int) cubeSize);
	public static int divisionY = ((int) roomSizeY / (int) cubeSize);

	public static int spaceDivisionX = ((int) spaceRoomSizeX / (int) spaceCubeSize);
	public static int spaceDivisionY = ((int) spaceRoomSizeY / (int) spaceCubeSize);

	public static int seed = 1000;
	public static int initialSeed = seed;
	public static int whichToDisplay = 0;
	public static int isDone = 0;
	public static boolean isDoneBool = false;

	public static PVector highLow = new PVector(0f, 1000f);
	public static ArrayList<String> toNN = new ArrayList<String>();

}
