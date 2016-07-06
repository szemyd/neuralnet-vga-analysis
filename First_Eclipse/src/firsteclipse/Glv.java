package firsteclipse;

import java.util.ArrayList;

public class Glv {
	public static float cubeSize = 5f;
	public static float spaceCubeSize = 5f;

	public static float roomSizeX = 129.4f;
	public static float roomSizeY = 160.42f;
	public static int divisionX = ((int) roomSizeX / (int) cubeSize);
	public static int divisionY = ((int) roomSizeY / (int) cubeSize);
	
	public static float spaceRoomSizeX = 129.4f*1.50f;
	public static float spaceRoomSizeY = 160.42f*1.50f;
	public static int spaceDivisionX = ((int) spaceRoomSizeX / (int) spaceCubeSize);
	public static int spaceDivisionY = ((int) spaceRoomSizeY / (int) spaceCubeSize);
	
	
	public static int seed = 10;
	public static int whichToDisplay=0;

	public static float cubeSizeReduced = 20.0f;
	public static boolean shouldDimReduction = true;
	public static boolean shouldSpaceSyntax = true;
	
	public static float chance = 99.5f;
	
	public static ArrayList<String> toNN = new ArrayList<String>();
	
	public static int isDone=0;
	public static boolean isDoneBool=false;
	
	public static int numOfThreads = 15;
	public static int numOfSolutions = 100;
}
