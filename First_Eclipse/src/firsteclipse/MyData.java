package firsteclipse;

import one.util.streamex.*;

import java.util.ArrayList;
import java.util.stream.IntStream;

import com.sun.glass.ui.Size;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import controlP5.Println;
import javafx.scene.transform.Translate;
import processing.core.PApplet;
import processing.core.PVector;

public class MyData {

	PApplet p;
	ArrayList<String[]> analysis = new ArrayList<String[]>();
	ArrayList<String[]> form = new ArrayList<String[]>();

	public Float[][] _analysis;
	public Float[][] _form;

	Float[][] rAnalysis;// = new ArrayList<Float[]>();
	Float[][] rForm;// = new ArrayList<Float[]>();;

	public Integer analysisID = 0;

	public MyData(PApplet _p) {
		p = _p;
	}

	public void draw() {
		for (int i = 0; i < _form.length; i++) {
			for (int j = 0; j < _form[i].length; j++) {
				p.pushMatrix();
				{
					p.translate(p.width / 8, p.height / 4);
					p.translate(3f * p.width / 4 - (Glv.neuronSize * 1.2f * _form.length) * 0.5f,
							p.height / 2 - (Glv.neuronSize * 1.2f * _form[0].length * 0.5f));
					p.translate(Glv.neuronSize * 1.2f * i, Glv.neuronSize * 1.2f * j);

					p.fill(360, 0, 180 * (1 - _form[i][j]));
					//					
					p.ellipse(0, 0, Glv.neuronSize, Glv.neuronSize);
				}
				p.popMatrix();
			}
		}

		if (rForm != null) {
			for (int i = 0; i < rForm.length; i++) {
				for (int j = 0; j < rForm[i].length; j++) {
					p.pushMatrix();
					{
						p.translate(-p.width / 8, p.height / 4);
						p.translate(3f * p.width / 4 - (Glv.neuronSize * 1.2f * rForm.length) * 0.5f,
								p.height / 2 - (Glv.neuronSize * 1.2f * rForm[0].length * 0.5f));
						p.translate(Glv.neuronSize * 1.2f * i, Glv.neuronSize * 1.2f * j);

						if (rForm[i][j] != null) {
							p.fill(360, 0, 180 * (1 - rForm[i][j]));

							//							if (rForm[i][j] > 0.1f)
							//								p.fill(360);
							//							else
							//								p.fill(0);

							p.ellipse(0, 0, Glv.neuronSize, Glv.neuronSize);
						}
					}
					p.popMatrix();
				}
			}
		}

		//		if (Glv.neuronsStored) {
		//			if (rAnalysis != null) {
		//				for (int i = 0; i < rAnalysis.length; i++) {
		//					for (int j = 0; j < rAnalysis[i].length; j++) {
		//						p.translate(
		//								(Glv.neuronSize * 1.2f * i) + (p.width / 4
		//										- (Glv.neuronSize * 1.2f * Glv.threadNN.net.neuralnet.m_input_layer.length)
		//												* 0.5f),
		//								(Glv.neuronSize * 1.2f * j)
		//										+ (p.height / 2 - (Glv.neuronSize * 1.2f
		//												* Glv.threadNN.net.neuralnet.m_input_layer[0].length) * 0.5f)
		//										+ p.height * 0.25f);
		//
		//						p.fill(360, 0, 180 * (1 - rAnalysis[i][j]));
		//
		//						p.ellipse(0, 0, Glv.neuronSize, Glv.neuronSize);
		//					}
		//				}
		//			}
		//		}

	}

	public void convert() {
		if (analysis.size() > 0 && form.size() > 0) {
			_analysis = new Float[analysis.size()][analysis.get(0).length];
			_form = new Float[form.size()][form.get(0).length];

			int i = 0;
			for (String[] strings : analysis) {
				for (int j = 0; j < _analysis[i].length; j++) {
					if (j >= 0 && j < strings.length) {
						//if (strings[j].length() > 0 && strings[j].length()<6) {
						if (Character.isDigit(strings[j].charAt(0))) {
							float num = (float) Integer.valueOf(strings[j]);
							if (i < _analysis.length && j < _analysis[i].length)
								//_analysis[i][j] = num; // Without mapping.
								_analysis[i][j] = p.map(num, Glv.highLowForNN.y, Glv.highLowForNN.x, -1f, 1f); // Mapping the values according to the highest and lowest visibility in the set.
							//}
						}
					}
				}
				i++;
			}

			i = 0;
			for (String[] strings : form) {
				for (int j = 0; j < _form[i].length; j++) {
					if (strings[j].length() > 0 && strings[j] != null) {
						if (Character.isDigit(strings[j].charAt(0))) {
							float num = (float) Integer.valueOf(strings[j]);
							if (i < _form.length && j < _form[i].length) {
								if (num == 0)
									_form[i][j] = -1f;
								else
									_form[i][j] = num;
							}
						}
					}
				}
				i++;
			}
		}
	}

	public boolean clean() {
		if (analysisID == 0)
			return true;

		int lineLength = analysis.get(0).length;
		for (String[] strings : analysis) {
			if (lineLength != strings.length)
				return true;
		}

		int lineLength2 = form.get(0).length;
		for (String[] strings : form) {
			if (lineLength2 != strings.length)
				return true;
		}
		return false;
	}

	public void extractValuableForm() {
		if (_form != null) {

			int numToAdd = (int) (Glv.cubeSizeReduced / Glv.cubeSize);
			rForm = new Float[(int) (p.floor(_form.length / numToAdd))][(int) p.floor(_form[0].length / numToAdd)];

			int k = 0;
			for (int i = 0; i <= _form.length - numToAdd; i += numToAdd) {
				int l = 0;
				for (int j = 0; j <= _form[i].length - numToAdd; j += numToAdd) {
					rForm[k][l] = _form[i][j];
					l++;
				}
				k++;
			}
		}
	}

	public void extractValuableAnalysis(Environment env, int length) {
		if (rAnalysis == null && _analysis != null) {

			if (env.editorLayer[0].length == _analysis[0].length && env.editorLayer.length == _analysis.length) {
				rAnalysis = new Float[1][length];
				//Float [][] temp;
				int counter = 0;
				for (int i = 0; i < _analysis.length; i++) {
					for (int j = 0; j < _analysis[i].length; j++) {
						if (env.editorLayer[i][j].iAmChosen)
							rAnalysis[0][counter++] = _analysis[i][j];
					}
				}
			}
		}
	}
}
//
//			ArrayList<ArrayList<Float>> rows = new ArrayList<ArrayList<Float>>();
//
//			for (int i = 0; i < _analysis.length; i++) {
//				rows.add(new ArrayList<Float>());
//				for (int j = 0; j < _analysis[i].length; j++) {
//					if (env.editorLayer[i][j].iAmChosen)
//						rows.get(i).add(_analysis[i][j]);
//					else
//						rows.get(i).add(null);
//				}
//			}

//			for (int i = 0; i < rows.size(); i++) {
//				for (int j = 0; j < rows.get(i).size(); j++) {
//
//					ArrayList<Float> localArrayList = rows.get((i + 1) % rows.size());
//
//					if (rows.size() > 0) {
//						Float myFloat = rows.get(i).get((j + 1) % rows.get(i).size());
//
//						//p.println("myFloat: " + myFloat);
//						if (myFloat == null) { // If data is uneven (if there is one line that is smaller larger than the other.
//							//p.println("I'm removing: " + data.analysisID);
//							rows.get(i).remove(myFloat);
//							//p.println("I am removing myFloat");
//						}
//						if (localArrayList.size() <= 0)
//							rows.remove(localArrayList);
//					}
//				}
//			}
//
//			rAnalysis = new Float[rows.size()][rows.get(0).size()];
//
//			for (int i = 0; i < rAnalysis.length; i++) {
//				for (int j = 0; j < rAnalysis[0].length; j++) {
//					rAnalysis[i][j] = rows.get(i).get(j);
//					p.println(rAnalysis[i][j]);
//				}
//			}

/* MARKKAL!
 * 

Float[][] thisIsIt = new Float[10][5];

for (int i = 0; i < thisIsIt.length; i++) {
	for (int j = 0; j < thisIsIt[i].length; j++) {
		thisIsIt[i][j] = i * j * 30f;
		p.println(thisIsIt[i]);
	}
}


String[][] myArray = IntStream.range(0, 3).mapToObj(x -> IntStream.range(0, 3)
				.mapToObj(y -> String.format("%c%c", letter(x), letter(y))).toArray(String[]::new))
		.toArray(String[][]::new);

for (int i = 0; i < thisIsIt.length; i++) {
	Float[] myArrayList = EntryStream.of(thisIsIt).filterKeyValue((index, item) -> index % 4 == 0).values()
			.toArray(size -> new Float[size]);
}
*/

//form.stream().map();

//p.println(myArrayList[0]);
/*
			for (int i = 0; i < _form.length; i += numToAdd) {
				for (int j = 0; j < _form[i].length; j += numToAdd) {
					if (i < rForm.length && j < rForm[i].length) {
						rForm[(int)i/numToAdd][(int)j/numToAdd] = _form[i][j];
						p.print(rForm[i][j]+ ",");
					}
				}
				p.println();
			}

			for (int i = 0; i < rForm.length; i++) {
				for (int j = 0; j < rForm[i].length; j++) {
					if (rForm[i][j] == null)
						p.print("it's null :-(");
					else
						p.print(rForm[i][j] + ",");
				}
				p.println("");
			}
*/

//		
//		for (int i = 0; i < _analysis.length; i++) {
//			for (int j = 0; j < _analysis[i].length; j++) {
//				
//			}
//		}
