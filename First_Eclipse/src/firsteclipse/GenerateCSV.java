package firsteclipse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sun.net.www.content.text.plain;

public class GenerateCSV {
	public static void save(String fileName) {
		String filePath = new File("").getAbsolutePath();

		//		generateCsvFile("C:/Users/Me/Google Drive/UCL/III_Semester/Final Thesis/DOCUMENTATION/GeneratedData/" + fileName
		//				+ ".csv");

		File file = new File(filePath + "\\" + "GeneratedData");
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}

		System.out.println(filePath + "\\" + "GeneratedData" + "\\" + fileName + ".csv");
		generateCsvFile(filePath + "\\" + "GeneratedData" + "\\" + fileName + ".csv");
	}

	private static void generateCsvFile(String sFileName) {
		File f = new File(sFileName);

		if (!f.isFile()) { // If the file doesn't exist yet

			try {
				FileWriter writer = new FileWriter(sFileName);

				for (String s : Glv.toNN) {
					writer.append(s);
				}
				// writer.append("DisplayName");

				writer.flush();
				writer.close();
				System.out.println("Done writing CSV.");

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			System.out.println("File already exists.");

	}
}