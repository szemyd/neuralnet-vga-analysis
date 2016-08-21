package firsteclipse;

import java.util.ArrayList;
import java.util.List;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import controlP5.Println;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Rotate;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Network {
	private static PApplet p;

	public Neuron[][] m_input_layer;
	public Neuron[][] m_hidden_layer;
	public Neuron[][] m_output_layer;

	public MyData lastCard;

	int networkID;

	public Network(PApplet _p, int inputsX, int inputsY, int hiddenX, int hiddenY, int outputsX, int outputsY) { // FOR GENERATING NETWORKS
		p = _p;

		m_input_layer = new Neuron[inputsX][inputsY];
		m_hidden_layer = new Neuron[hiddenX][hiddenY];
		m_output_layer = new Neuron[outputsX][outputsY];

		for (int i = 0; i < m_input_layer.length; i++) {
			for (int j = 0; j < m_input_layer[i].length; j++) {
				PVector position = new PVector(
						(Glv.neuronSize * 1.2f * i)
								+ (4f * (p.width / 10f) - (Glv.neuronSize * 1.2f * m_input_layer.length) * 0.5f),
						(Glv.neuronSize * 1.2f * j)
								+ (p.height / 2 - (Glv.neuronSize * 1.2f * m_input_layer[0].length) * 0.5f));
				m_input_layer[i][j] = new Neuron(p, position);
			}
		}

		for (int i = 0; i < m_hidden_layer.length; i++) {
			for (int j = 0; j < m_hidden_layer[i].length; j++) {
				PVector position = new PVector(
						(Glv.neuronSize * 1.2f * i)
								+ (2f * p.width / 4 - (Glv.neuronSize * 1.2f * m_hidden_layer.length) * 0.5f),
						(Glv.neuronSize * 1.2f * j
								+ (p.height / 2 - (Glv.neuronSize * 1.2f * m_hidden_layer[0].length) * 0.5f)));

				m_hidden_layer[i][j] = new Neuron(p, position, m_input_layer);
			}
		}

		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer[i].length; j++) {
				PVector position = new PVector(
						(Glv.neuronSize * 1.2f * i)
								+ (p.width / 2 - (Glv.neuronSize * 1.2f * m_output_layer.length) * 0.5f),
						(Glv.neuronSize * 1.2f * j) - (p.height / 4)
								+ (p.height / 2 - (Glv.neuronSize * 1.2f * m_output_layer[0].length) * 0.5f));
				m_output_layer[i][j] = new Neuron(p, position, m_hidden_layer);
			}
		}
	}

	public Network(PApplet _p, int inputsX, int inputsY, int hiddenX, int hiddenY, int outputsX, int outputsY, // FOR SPLIT NETWORKS
			Neuron[][] selectedNeurons, int _networkID) {
		p = _p;
		networkID = _networkID;

		m_input_layer = new Neuron[inputsX][inputsY];
		m_hidden_layer = new Neuron[hiddenX][hiddenY];
		m_output_layer = new Neuron[outputsX][outputsY];

		for (int i = 0; i < m_input_layer.length; i++) {
			for (int j = 0; j < m_input_layer[i].length; j++) {
				PVector position = new PVector(
						(Glv.neuronSize * 1.2f * i)
								+ ((p.width / 2f) - (Glv.neuronSize * 1.2f * m_input_layer.length) * 0.5f),
						(Glv.neuronSize * 1.2f * j)
								+ (p.height / 2 - (Glv.neuronSize * 1.2f * m_input_layer[0].length) * 0.5f));
				m_input_layer[i][j] = new Neuron(p, position);
			}
		}

		for (int i = 0; i < m_hidden_layer.length; i++) {
			for (int j = 0; j < m_hidden_layer[i].length; j++) {
				PVector position = new PVector(
						(Glv.neuronSize * 1.2f * i)
								+ (2f * p.width / 4 - (Glv.neuronSize * 1.2f * m_hidden_layer.length) * 0.5f),
						(Glv.neuronSize * 1.2f * j
								+ (p.height / 2 - (Glv.neuronSize * 1.2f * m_hidden_layer[0].length) * 0.5f)));

				m_hidden_layer[i][j] = new Neuron(p, position, m_input_layer);
			}
		}

		for (int i = 0; i < selectedNeurons.length; i++) {
			for (int j = 0; j < selectedNeurons[i].length; j++) {
				m_output_layer[0][i * selectedNeurons[0].length + j] = new Neuron(p,
						new PVector(selectedNeurons[i][j].position.x + 3f * p.width / 10f,
								selectedNeurons[i][j].position.y),
						m_hidden_layer, selectedNeurons[i][j].idNum);
			}
		}
		//		
		//		for (int i = 0; i < m_output_layer.length; i++) {
		//			for (int j = 0; j < m_output_layer[i].length; j++) {
		//				m_output_layer[i][j] = selectedNeurons[i][j];
		//			}
		//		}

		//		ArrayList<PVector> fromEditorLayer = new ArrayList<PVector>();
		//		ArrayList<PVector> idsFromEditorLayer = new ArrayList<PVector>();
		//
		//		for (int i = 0; i < selectedNeurons.length; i++) {
		//			for (int j = 0; j < selectedNeurons[i].length; j++) {
		//				if (selectedNeurons[i][j].iAmChosen) {
		//					fromEditorLayer.add(selectedNeurons[i][j].position);
		//					idsFromEditorLayer.add(new PVector(i, j));
		//				}
		//			}
		//		}
		//
		//		
		//		int l=0;
		//		for (int i = howManySplit; i < selectedNeurons.length; i++) {
		//			for (int j = howManySplit; j < selectedNeurons[i].length; j++) {
		//
		//				if (l>=m_output_layer[0].length) break;
		//				if (selectedNeurons[i][j].iAmChosen) {					
		//					m_output_layer[0][l] = new Neuron(p,
		//							new PVector(selectedNeurons[i][j].position.x + 3f * p.width / 10f,
		//									selectedNeurons[i][j].position.y),
		//							m_hidden_layer, selectedNeurons[i][j].idNum);
		//					
		//					l++;
		//				}
		//			}
		//			
		//		}

		/*
		int k = -1;
		int l = 0;
		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer[i].length; j++) {
		
				if (j % Math.sqrt(Glv.splitSize) == 0)
					k++;
				//				if ((k + 1) * (int) Math.sqrt(Glv.splitSize) >= selectedNeurons.length) {
				//					l++;
				//					p.println("this happened.");
				//				}
				l = p.floor(howManySplit / selectedNeurons.length);
		
				p.println("m_output_layer.length: " + m_output_layer.length + "m_output_layer[0].length: "
						+ m_output_layer[0].length);
		
				int selectThis = j - (k * (int) Math.sqrt(Glv.splitSize)) + k * (selectedNeurons.length + 1)
						+ howManySplit + (selectedNeurons.length * l * (int) Math.sqrt(Glv.splitSize));
		
				if (selectThis < fromEditorLayer.size()) {
		
					PVector position = fromEditorLayer.get(selectThis);
					PVector ids = idsFromEditorLayer.get(selectThis);
		
					//p.println(j - (k * (int) Math.sqrt(Glv.splitSize)) + k * selectedNeurons.length+ selectedNeurons.length * l * Glv.splitSize);
		
					//PVector position = fromEditorLayer.get((j+selectedNeurons[0].length)%Glv.splitSize);
					//PVector position = fromEditorLayer.get((j%selectedNeurons.length) + (j%Glv.splitSize) +howManySplit);
					//	PVector position = fromEditorLayer.get(j%Glv.splitSize+howManySplit);
		
					//				System.out.print(ids.x + " | " + ids.y + " ||| ");
					//				System.out.println("");
					//				System.out.print(position.x + " | " + position.y + " ||| ");
		
					m_output_layer[i][j] = new Neuron(p, new PVector(position.x + 3f * p.width / 10f, position.y),
							m_hidden_layer, ids);
					//}
				} else
					m_output_layer[i][j] = new Neuron(p,
							new PVector(fromEditorLayer.get(fromEditorLayer.size() - 1).x + 3f * p.width / 10f,
									fromEditorLayer.get(fromEditorLayer.size() - 1).y),
							m_hidden_layer, new PVector(idsFromEditorLayer.get(fromEditorLayer.size() - 1).x,
									idsFromEditorLayer.get(fromEditorLayer.size() - 1).y));
			}
		}
		*/
	}

	public Network(PApplet _p, int inputsX, int inputsY, int hiddenX, int hiddenY, int outputsX, int outputsY, // FOR SIMPLE ANALYSIS NETWORK
			Neuron[][] selectedNeurons) {
		p = _p;

		m_input_layer = new Neuron[inputsX][inputsY];
		m_hidden_layer = new Neuron[hiddenX][hiddenY];
		m_output_layer = new Neuron[outputsX][outputsY];

		for (int i = 0; i < m_input_layer.length; i++) {
			for (int j = 0; j < m_input_layer[i].length; j++) {
				PVector position = new PVector(
						(Glv.neuronSize * 1.2f * i)
								+ ((p.width / 2f) - (Glv.neuronSize * 1.2f * m_input_layer.length) * 0.5f),
						(Glv.neuronSize * 1.2f * j)
								+ (p.height / 2 - (Glv.neuronSize * 1.2f * m_input_layer[0].length) * 0.5f));
				m_input_layer[i][j] = new Neuron(p, position);
			}
		}

		for (int i = 0; i < m_hidden_layer.length; i++) {
			for (int j = 0; j < m_hidden_layer[i].length; j++) {
				PVector position = new PVector(
						(Glv.neuronSize * 1.2f * i)
								+ (2f * p.width / 4 - (Glv.neuronSize * 1.2f * m_hidden_layer.length) * 0.5f),
						(Glv.neuronSize * 1.2f * j
								+ (p.height / 2 - (Glv.neuronSize * 1.2f * m_hidden_layer[0].length) * 0.5f)));

				m_hidden_layer[i][j] = new Neuron(p, position, m_input_layer);
			}
		}

		ArrayList<PVector> fromEditorLayer = new ArrayList<PVector>();
		ArrayList<PVector> idsFromEditorLayer = new ArrayList<PVector>();

		for (int i = 0; i < selectedNeurons.length; i++) {
			for (int j = 0; j < selectedNeurons[i].length; j++) {
				if (selectedNeurons[i][j].iAmChosen) {
					fromEditorLayer.add(selectedNeurons[i][j].position);
					idsFromEditorLayer.add(new PVector(i, j));
				}
			}
		}

		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer[i].length; j++) {
				PVector position = fromEditorLayer.get(j);
				PVector ids = idsFromEditorLayer.get(j);

				m_output_layer[i][j] = new Neuron(p, new PVector(position.x + 3f * p.width / 10f, position.y),
						m_hidden_layer, ids);

			}
		}
	}

	public void draw(Environment env) {

		drawBoundary(m_input_layer[0][0].position, m_input_layer.length, m_input_layer[0].length, "Input Layer");

		for (int i = 0; i < m_input_layer.length; i++) {
			for (int j = 0; j < m_input_layer[i].length; j++) {
				p.pushMatrix();
				{
					m_input_layer[i][j].draw();
				}
				p.popMatrix();
			}
		}

		drawBoundary(m_output_layer[0][0].position, m_output_layer.length, m_output_layer[0].length, "Output Layer");

		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer[i].length; j++) {
				p.pushMatrix();
				{
					m_output_layer[i][j].draw();
				}
				p.popMatrix();
			}
		}
		if (lastCard != null)
			lastCard.draw(env);

		drawLineBetween(
				new PVector(
						m_input_layer[0][0].position.x - 10f - Glv.neuronSize
								+ (m_input_layer.length * Glv.neuronSize * 1.2f) + 20f
								+ Glv.neuronSize,
						m_input_layer[0][0].position.y - 10f - Glv.neuronSize
								+ ((m_input_layer[0].length * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize) * 0.5f),
				new PVector(m_output_layer[0][0].position.x - 10f - Glv.neuronSize,
						m_output_layer[0][0].position.y - 10f - Glv.neuronSize
								+ ((m_output_layer[0].length * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize) * 0.5f));
	}

	public void drawAnalysis(Environment env) {

		if (m_input_layer != null) {
			drawBoundary(m_input_layer[0][0].position, m_input_layer.length, m_input_layer[0].length, "Input Layer");
			drawInput();
		}

		if (m_output_layer != null) {
			drawBoundary(
					new PVector(env.editorLayer[0][0].position.x + 3f * p.width / 10f,
							env.editorLayer[0][0].position.y),
					env.editorLayer.length, env.editorLayer[0].length, "Output Layer");
			drawOutput();
		}

		if (lastCard != null)
			lastCard.drawOnlyAnalysis(env);

		if (m_input_layer != null && m_output_layer != null) {
			drawLineBetween(
					new PVector(
							m_input_layer[0][0].position.x - 10f - Glv.neuronSize
									+ (m_input_layer.length * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize,
							m_input_layer[0][0].position.y - 10f - Glv.neuronSize
									+ ((m_input_layer[0].length * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize)
											* 0.5f),
					new PVector((env.editorLayer[0][0].position.x + 3f * p.width / 10f) - 10f - Glv.neuronSize,
							env.editorLayer[0][0].position.y - 10f - Glv.neuronSize
									+ ((env.editorLayer[0].length * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize)
											* 0.5f));
		}

	}

	private void drawInput() {
		for (int i = 0; i < m_input_layer.length; i++) {
			for (int j = 0; j < m_input_layer[i].length; j++) {
				p.pushMatrix();
				{
					m_input_layer[i][j].draw();
				}
				p.popMatrix();
			}
		}
	}

	public void drawOutput() {
		if (m_output_layer != null) {
			for (int i = 0; i < m_output_layer.length; i++) {
				for (int j = 0; j < m_output_layer[i].length; j++) {
					p.pushMatrix();
					{
						m_output_layer[i][j].draw();
					}
					p.popMatrix();
				}
			}
		}
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

	private void drawLineBetween(PVector input, PVector output) {
		p.pushMatrix();
		{
			p.pushStyle();
			{
				p.stroke(360, 180);
				p.line(input.x, input.y, output.x, output.y);
				p.fill(360);
				p.ellipse(input.x, input.y, 7f, 7f);
				p.ellipse(output.x, output.y, 7f, 7f);
			}
			p.popStyle();
		}
		p.popMatrix();
	}

	//---> Neuron interaction
	public void respond(MyData card, Float[][] input) {

		lastCard = card;
		//p.println(card.analysisID);
		for (int i = 0; i < m_input_layer.length; i++) {
			for (int j = 0; j < m_input_layer[i].length; j++) {
				m_input_layer[i][j].m_output = input[i][j];
			}
		}

		for (int i = 0; i < m_hidden_layer.length; i++) {
			for (int j = 0; j < m_hidden_layer[i].length; j++) {
				m_hidden_layer[i][j].respond();
			}
		}

		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer[i].length; j++) {
				m_output_layer[i][j].respond();
			}
		}
	}

	public void train(Float[][] outputs) {
		//		p.println("m_output_layer.length: " + m_output_layer.length + " | m_output_layer[0].length: "
		//				+ m_output_layer[0].length);
		//		p.println(" outputs.length: " + outputs.length);
		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer[i].length; j++) {
				//		p.print(outputs[i][j] + ",");
				m_output_layer[i][j].setError(outputs[i][j]);
				m_output_layer[i][j].train();
			}
			//p.println("");
		}

		for (int i = 0; i < m_hidden_layer.length; i++) {
			for (int j = 0; j < m_hidden_layer[i].length; j++) {
				m_hidden_layer[i][j].train();
			}
		}
	}
	//<---
}
