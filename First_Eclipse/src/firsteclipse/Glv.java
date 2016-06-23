package firsteclipse;

import processing.core.PShape;

public class Glv {
	public static float cubeSize = 5f;

	public static float roomSizeX = 129.4f;
	public static float roomSizeY = 160.42f;
	public static int divisionX = ((int) roomSizeX / (int) cubeSize);
	public static int divisionY = ((int) roomSizeY / (int) cubeSize);
	public static int seed = 0;

	public static float cubeSizeReduced = 20.0f;
	
	public static boolean shouldDimReduction = false;
	
	public static float chance = 98.0f;
}
