package firsteclipse;

import java.io.FileWriter;
import java.io.IOException;

import sun.net.www.content.text.plain;

public class GenerateCSV {
	public static void save(String fileName) {
		generateCsvFile("C:/Users/Me/Google Drive/UCL/III_Semester/Final Thesis/DOCUMENTATION/GeneratedData/" + fileName + ".csv");
	}

	private static void generateCsvFile(String sFileName) {
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
	}
}