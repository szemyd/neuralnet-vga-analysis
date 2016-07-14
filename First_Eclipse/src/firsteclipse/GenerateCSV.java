package firsteclipse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import sun.net.www.content.text.plain;

public class GenerateCSV {
	public static void save(String fileName) {
		String filePath = new File("").getAbsolutePath();

		File file = new File(filePath + "\\" + "GeneratedData");
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
		generateCsvFile(filePath + "\\" + "GeneratedData" + "\\" + fileName + ".csv");
	}

	private static void generateCsvFile(String sFileName) {
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
						if (i== Glv.threads.size()-1 && j == thread.spaceSyntax.toNN.size()-2)   {System.out.println("im not writing this: '" + s + "'");}
						else writer.append(s); // Don't write the last '_'.
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
}