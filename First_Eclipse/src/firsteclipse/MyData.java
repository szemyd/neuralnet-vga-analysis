package firsteclipse;

import one.util.streamex.*;

import java.util.ArrayList;
import java.util.stream.IntStream;

import com.sun.glass.ui.Size;
import com.sun.java_cup.internal.runtime.virtual_parse_stack;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import controlP5.Println;
import javafx.scene.transform.Translate;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import sun.util.locale.provider.FallbackLocaleProviderAdapter;

public class MyData {

	PApplet p;
	ArrayList<String[]> analysis = new ArrayList<String[]>();
	ArrayList<String[]> form = new ArrayList<String[]>();

	public Float[][] _analysis;
	public Float[][] _form;

	public static Neuron[][] rAnalysisN;
	Float[][] rAnalysis;// = new ArrayList<Float[]>();
	Float[][] rForm;// = new ArrayList<Float[]>();;
	Float[][] rAnalysisSplit;// = new ArrayList<Float[]>();

	public Integer analysisID = 0;

	public MyData(PApplet _p) {
		p = _p;
	}

	public void draw(Environment env) {

		/*
		for (int i = 0; i < _form.length; i++) {
			for (int j = 0; j < _form[i].length; j++) {
				p.pushMatrix();
				{
					p.translate(p.width / 8, p.height / 4);
					p.translate(3f * p.width / 4 - (Glv.neuronSize * 1.2f * _form.length) * 0.5f,
							p.height / 2 - (Glv.neuronSize * 1.2f * _form[0].length * 0.5f));
					p.translate(Glv.neuronSize * 1.2f * i, Glv.neuronSize * 1.2f * j);
		
					//if(env.editorLayer[i][j].iAmChosen) p.fill(360, 360, 180 * (1 - _form[i][j]));
					//else 
						p.fill(360, 0, 180 * (1 - _form[i][j]));
					//					
					p.ellipse(0, 0, Glv.neuronSize, Glv.neuronSize);
				}
				p.popMatrix();
			}
		}
		*/

		drawBoundary(
				new PVector(p.width / 2 - (Glv.neuronSize * 1.2f * rForm.length) * 0.5f,
						(p.height / 4) + p.height / 2 - (Glv.neuronSize * 1.2f * rForm[0].length * 0.5f)),
				rForm.length, rForm[0].length, "Loaded Form");

		//---> THIS DRAWS WHAT THE LOADED FORM SHOULD BE IN THE MIDDLE, BUT BELOW!
		if (rForm != null) {
			for (int i = 0; i < rForm.length; i++) {
				for (int j = 0; j < rForm[i].length; j++) {
					p.pushMatrix();
					{
						p.translate(0, p.height / 4);
						p.translate(p.width / 2 - (Glv.neuronSize * 1.2f * rForm.length) * 0.5f,
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
		//<---

		drawBoundary(
				new PVector(p.width / 5 - (Glv.neuronSize * 1.2f * _analysis.length) * 0.5f,
						p.height * 0.5f - (Glv.neuronSize * 1.2f * _analysis[0].length) * 0.5f),
				_analysis.length, _analysis[0].length, "Loaded SpaceSyntax");

		//---> THIS DRAWS THE LOADED SPACESYNTAX ANALYSIS ON THE LEFT!
		if (Glv.neuronsStored) {
			if (_analysis != null) {
				for (int i = 0; i < _analysis.length; i++) {
					for (int j = 0; j < _analysis[i].length; j++) {
						p.pushMatrix();
						{
							p.pushStyle();
							{

								p.translate(p.width / 5, p.height * 0.5f);
								p.translate(
										(Glv.neuronSize * 1.2f * i) - (Glv.neuronSize * 1.2f * _analysis.length) * 0.5f,
										(Glv.neuronSize * 1.2f * j)
												- (Glv.neuronSize * 1.2f * _analysis[i].length) * 0.5f);

								if (env.editorLayer[i][j].iAmChosen) {
									p.strokeWeight(1.2f);
									p.stroke(360, 360, 180 * (1 - _analysis[i][j]), 360);
									p.fill(360, 0, 180 * (1 - _analysis[i][j]), 360);
								} else {
									p.strokeWeight(1.0f);
									p.stroke(360, 0, 180 * (1 - _analysis[i][j]), 360);
									p.fill(360, 0, 180 * (1 - _analysis[i][j]), 360);
								}

								//							if (env.editorLayer[i][j].iAmChosen)
								//								p.fill(360, 360, 180 * (1 - _analysis[i][j]), 180);
								//
								//							else
								//								p.fill(360, 0, 180 * (1 - _analysis[i][j]), 180);
								//p.fill(360, 0, 180 * (1 - rAnalysis[i][j]));

								p.ellipse(0, 0, Glv.neuronSize, Glv.neuronSize);
							}
							p.popStyle();
						}
						p.popMatrix();
					}
				}
			}
		}

		/*
		drawBoundary(new PVector(
				+(p.width / 4 - (Glv.neuronSize * 1.2f * Glv.threadNN.net.neuralnet.m_input_layer.length) * 0.5f),
		
				+(p.height / 2 - (Glv.neuronSize * 1.2f * Glv.threadNN.net.neuralnet.m_input_layer[0].length) * 0.5f)
						+ p.height * 0.25f),
				rAnalysis.length, rAnalysis[0].length);
		
		if (Glv.neuronsStored) {
			if (rAnalysis != null) {
				for (int i = 0; i < rAnalysis.length; i++) {
					for (int j = 0; j < rAnalysis[i].length; j++) {
						p.translate(
								(Glv.neuronSize * 1.2f * i) + (p.width / 4
										- (Glv.neuronSize * 1.2f * Glv.threadNN.net.neuralnet.m_input_layer.length)
												* 0.5f),
								(Glv.neuronSize * 1.2f * j)
										+ (p.height / 2 - (Glv.neuronSize * 1.2f
												* Glv.threadNN.net.neuralnet.m_input_layer[0].length) * 0.5f)
										+ p.height * 0.25f);
		
						if(env.editorLayer[i][j].iAmChosen) p.fill(360, 360, 180 * (1 - rAnalysis[i][j]));
						else p.fill(360, 0, 180 * (1 - rAnalysis[i][j]));
						//p.fill(360, 0, 180 * (1 - rAnalysis[i][j]));
		
						p.ellipse(0, 0, Glv.neuronSize, Glv.neuronSize);
					}
				}
			}
		}
		*/
	}

	public void drawOnlyAnalysis(Environment env) {
		drawBoundary(
				new PVector(p.width / 5 - (Glv.neuronSize * 1.2f * _analysis.length) * 0.5f,
						((p.height-150f) * 0.5f+150f) - (Glv.neuronSize * 1.2f * _analysis[0].length) * 0.5f),
				_analysis.length, _analysis[0].length, "Loaded SpaceSyntax");

		//---> THIS DRAWS THE LOADED SPACESYNTAX ANALYSIS ON THE LEFT!
		if (Glv.neuronsStored) {
			if (_analysis != null) {
				for (int i = 0; i < _analysis.length; i++) {
					for (int j = 0; j < _analysis[i].length; j++) {
						p.pushMatrix();
						{
							p.pushStyle();
							{

								p.translate(p.width / 5, ((p.height-150f) * 0.5f)+150f);
								p.translate(
										(Glv.neuronSize * 1.2f * i) - (Glv.neuronSize * 1.2f * _analysis.length) * 0.5f,
										(Glv.neuronSize * 1.2f * j)
												- (Glv.neuronSize * 1.2f * _analysis[i].length) * 0.5f);

								if (env.editorLayer[i][j].iAmChosen) {
									p.strokeWeight(1.2f);
									p.stroke(360, 360, 180 * (1 - _analysis[i][j]));
									p.fill(360, 0, 180 * (1 - _analysis[i][j]));
								} else {
									p.strokeWeight(1.0f);
									p.stroke(360, 0, 180 * (1 - _analysis[i][j]));
									p.fill(360, 0, 180 * (1 - _analysis[i][j]));
								}

								p.ellipse(0, 0, Glv.neuronSize, Glv.neuronSize);
							}
							p.popStyle();
						}
						p.popMatrix();
					}
				}
				//p.fill(360);
			}
		}
	}

	public void drawLastDataForSplit(Environment env) {
		drawBoundary(
				new PVector(p.width / 5 - (Glv.neuronSize * 1.2f * _analysis.length) * 0.5f,
						p.height * 0.5f - (Glv.neuronSize * 1.2f * _analysis[0].length) * 0.5f),
				_analysis.length, _analysis[0].length, "Loaded SpaceSyntax");

		//---> THIS DRAWS THE LOADED SPACESYNTAX ANALYSIS ON THE LEFT!
		if (Glv.neuronsStored) {
			if (_analysis != null) {
				for (int i = 0; i < _analysis.length; i++) {
					for (int j = 0; j < _analysis[i].length; j++) {
						p.pushMatrix();
						{
							p.pushStyle();
							{

								p.translate(p.width / 5, p.height * 0.5f);
								p.translate(
										(Glv.neuronSize * 1.2f * i) - (Glv.neuronSize * 1.2f * _analysis.length) * 0.5f,
										(Glv.neuronSize * 1.2f * j)
												- (Glv.neuronSize * 1.2f * _analysis[i].length) * 0.5f);

								if (env.editorLayer[i][j].iAmChosen) {
									p.strokeWeight(1.2f);
									p.stroke(360, 360, 180 * (1 - _analysis[i][j]));
									p.fill(360, 0, 180 * (1 - _analysis[i][j]));
								} else {
									p.strokeWeight(1.0f);
									p.stroke(360, 0, 180 * (1 - _analysis[i][j]));
									p.fill(360, 0, 180 * (1 - _analysis[i][j]));
								}

								p.ellipse(0, 0, Glv.neuronSize, Glv.neuronSize);
							}
							p.popStyle();
						}
						p.popMatrix();
					}
				}
				//p.fill(360);
			}
		}
	}

	private void drawBoundary(PVector position, int sizeX, int sizeY, String myText) {
		p.pushStyle();
		{
			p.noFill();
			//p.fill(360);
			p.stroke(360);
			p.pushMatrix();
			{
				p.rectMode(PConstants.CORNER);
				p.rect(position.x - 10f - Glv.neuronSize, position.y - 10f - Glv.neuronSize,
						(sizeX * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize,
						(sizeY * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize, 20f);
			}
			p.popMatrix();
		}
		p.popStyle();

		drawText(myText, new PVector(position.x - Glv.neuronSize,
				position.y - 10f - Glv.neuronSize + (sizeY * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize + 20f));
	}

	private void drawText(String myText, PVector position) {
		p.pushMatrix();
		{
			p.pushStyle();
			{
				p.textAlign(PConstants.LEFT, PConstants.CENTER);
				p.textSize(18);
				p.fill(360);
				p.text(myText, position.x + 15f, position.y);
				p.ellipse(position.x, position.y, 8f, 8f);
			}
			p.popStyle();

		}
		p.popMatrix();
	}

	//---> Data cleaning
	public void convert() {

		if (analysis.size() > 0 && form.size() > 0) {
			//	p.println("1) Analysis length: " + analysis.size() + " | analysis.get(0).length: " + analysis.get(0).length);
			_analysis = new Float[analysis.get(0).length][analysis.size()];
			_form = new Float[form.get(0).length][form.size()];

			int i = 0;
			for (String[] strings : analysis) {
				for (int j = 0; j < strings.length; j++) {
					if (j >= 0 && j < strings.length) {
						//if (strings[j].length() > 0 && strings[j].length()<6) {
						if (Character.isDigit(strings[j].charAt(0))) {
							float num = Float.valueOf(strings[j]);
							if (j < _analysis.length && i < _analysis[j].length)
								//_analysis[i][j] = num; // Without mapping.
								if (Glv.neighbourHoodOrClustering) {
								if (num > 1429f)
								_analysis[j][i] = 1f;
								else if (num < 10)
								_analysis[j][i] = -1f;
								else
								_analysis[j][i] = p.map(num, 10, 1429f, -1f, 1f); //1200f,-1f, 1f); // Mapping the values according to the highest and lowest visibility in the set.
								//}
								} else {
								if (num > 1.0f)
								_analysis[j][i] = 1f;
								else if (num < 0f)
								_analysis[j][i] = -1f;
								else
								_analysis[j][i] = p.map(num, 0, 1f, -1f, 1f); //1200f,-1f, 1f); // Mapping the values according to the highest and lowest visibility in the set.

								}
						}
					}
				}
				i++;
			}

			//p.println("2) Analysis length: " + _analysis.length + " | analysis[i].length: " + _analysis[0].length);
			//p.println("form length: " + form.size() + " | form[i].length: " + form.get(0).length);
			i = 0;
			for (String[] strings : form) {
				for (int j = 0; j < strings.length; j++) {
					if (strings[j].length() > 0 && strings[j] != null) {
						if (Character.isDigit(strings[j].charAt(0))) {
							float num = (float) Integer.valueOf(strings[j]);
							if (j < _form.length && i < _form[j].length) {
								if (num == 0)
									_form[j][i] = -1f;
								else
									_form[j][i] = num;
							}
						}
					}
				}
				i++;
			}
			//p.println("_form length: " + _form.length + " | _form[i].length: " + _form[0].length);
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

		/*int firstValue = 1;
		if (length * Glv.neuronSize * 1.2f > p.height - 40f)
			firstValue = 2;
		if (length * Glv.neuronSize * 1.2f > (p.height - 40f) * 2f)
			firstValue = 3;
		*/

		if (_analysis != null) {

			if (env.editorLayer[0].length == _analysis[0].length && env.editorLayer.length == _analysis.length) {
				rAnalysis = new Float[1][length];
				rAnalysisN = new Neuron[1][length];

				rAnalysisSplit = takeOutSelected(env.editorLayer);

				int counter = 0;
				for (int i = 0; i < _analysis.length; i++) {
					for (int j = 0; j < _analysis[i].length; j++) {
						if (env.editorLayer[i][j].iAmChosen) {
							rAnalysis[0][counter] = _analysis[i][j];

							rAnalysisN[0][counter] = new Neuron(p,
									new PVector(env.editorLayer[i][j].position.x - p.width / 10f * 3f,
											env.editorLayer[i][j].position.y));
							rAnalysisN[0][counter].m_output = _analysis[i][j];
							counter++;
						}
					}
				}
			}
		}
	}

	private Neuron[][] weedOutNull(Neuron[][] oneNetwork) {

		int sizeX = 0, sizeY = 0;

		for (int i = 0; i < oneNetwork.length; i++) {
			if (oneNetwork[i][0] != null)
				sizeX++;
		}
		for (int i = 0; i < oneNetwork[0].length; i++) {
			if (oneNetwork[0][i] != null)
				sizeY++;
		}

		Neuron[][] newNetwork = new Neuron[sizeX][sizeY];

		int k = 0, l = 0;
		for (int i = 0; i < oneNetwork.length; i++) {
			for (int j = 0; j < oneNetwork[0].length; j++) {

				if (oneNetwork[i][j] != null) {
					if (k < newNetwork.length && l < newNetwork[0].length)
						newNetwork[k][l] = oneNetwork[i][j];

					l++;
					if (l >= newNetwork[0].length) {
						l = 0;
						k++;
					}
				}
			}
		}

		return newNetwork;
	}

	private Float[][] takeOutSelected(Neuron[][] selectedNeuron) { // This cuts out all parts that are not chosen.
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

		Float[][] temp = new Float[sizeX][sizeY];
		if (sizeX > 0 && sizeY > 0) {
			int k = 0, l = 0;
			for (int i = 0; i < selectedNeuron.length; i++) {
				for (int j = 0; j < selectedNeuron[0].length; j++) {

					if (selectedNeuron[i][j].iAmChosen) {

						//p.println("i: " + i + " | j: " + j);
						//p.println("k: " + k + " | l: " + l);
						if (k < temp.length && l < temp[0].length)
							temp[k][l] = _analysis[i][j];

						l++;
						if (l >= temp[0].length) {
							l = 0;
							k++;
						}
					}
				}
			}
		}
		return temp;

	}

	//<--- Data Cleaning.

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
