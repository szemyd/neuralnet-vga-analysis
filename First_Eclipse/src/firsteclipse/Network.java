package firsteclipse;

import java.util.ArrayList;

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

	public static Neuron[][] m_input_layer;
	public static Neuron[][] m_hidden_layer;
	public static Neuron[][] m_output_layer;

	public MyData lastCard;

	public Network(PApplet _p, int inputsX, int inputsY, int hiddenX, int hiddenY, int outputsX, int outputsY) {
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

	public Network(PApplet _p, int inputsX, int inputsY, int hiddenX, int hiddenY, int outputsX, int outputsY,
			Environment env) {
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

		for (int i = 0; i < env.editorLayer.length; i++) {
			for (int j = 0; j < env.editorLayer[i].length; j++) {
				if (env.editorLayer[i][j].iAmChosen) {
					fromEditorLayer.add(env.editorLayer[i][j].position);
				}
			}
		}

		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer[i].length; j++) {
				//				PVector position = new PVector(
				//						(Glv.neuronSize * 1.2f * i)
				//								+ (7f * p.width / 10f - (Glv.neuronSize * 1.2f * m_output_layer.length) * 0.5f),
				//						(Glv.neuronSize * 1.2f * j) - (p.height / 4)
				//								+ (p.height / 2 - (Glv.neuronSize * 1.2f * m_output_layer[0].length) * 0.5f));
				PVector position = fromEditorLayer.get(j);
				m_output_layer[i][j] = new Neuron(p, new PVector(position.x + 3f * p.width / 10f, position.y),
						m_hidden_layer);
			}
		}
	}

	public void draw(Environment env) {

		drawBoundary(m_input_layer[0][0].position, m_input_layer.length, m_input_layer[0].length, "Input Layer");

		for (int i = 0; i < m_input_layer.length; i++) {
			for (int j = 0; j < m_input_layer[i].length; j++) {
				p.pushMatrix();
				{
					//					p.translate(p.width / 4 - (Glv.neuronSize * 1.2f * m_input_layer.length) * 0.5f,
					//							p.height / 2 - (Glv.neuronSize * 1.2f * m_input_layer[0].length) * 0.5f);
					//					p.translate(Glv.neuronSize * 1.2f * i, Glv.neuronSize * 1.2f * j);
					//	p.translate(i * (p.width / 3f) / m_input_layer.length, j * p.width / m_input_layer[i].length);
					m_input_layer[i][j].draw();
				}
				p.popMatrix();
			}
		}

		//		for (int i = 0; i < m_hidden_layer.length; i++) {
		//			for (int j = 0; j < m_hidden_layer[i].length; j++) {
		//				p.pushMatrix();
		//				{
		//					p.translate(2f * p.width / 4 - (Glv.neuronSize * 1.2f * m_hidden_layer.length) * 0.5f,
		//							p.height / 2 - (Glv.neuronSize * 1.2f * m_hidden_layer[0].length) * 0.5f);
		//					p.translate(Glv.neuronSize * 1.2f * i, Glv.neuronSize * 1.2f * j);
		//
		//					m_hidden_layer[i][j].draw();
		//				}
		//				p.popMatrix();
		//			}
		//		}

		drawBoundary(m_output_layer[0][0].position, m_output_layer.length, m_output_layer[0].length, "Output Layer");

		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer[i].length; j++) {
				p.pushMatrix();
				{
					//					p.translate(0, -p.height / 4);
					//					p.translate(3f * p.width / 4 - (Glv.neuronSize * 1.2f * m_output_layer.length) * 0.5f,
					//							p.height / 2 - (Glv.neuronSize * 1.2f * m_output_layer[0].length) * 0.5f);
					//					p.translate(Glv.neuronSize * 1.2f * i, Glv.neuronSize * 1.2f * j);
					m_output_layer[i][j].draw();
				}
				p.popMatrix();
			}
		}
		if (lastCard != null)
			lastCard.draw(env);
		
		drawLineBetween(new PVector(
				m_input_layer[0][0].position.x - 10f - Glv.neuronSize + (m_input_layer.length * Glv.neuronSize * 1.2f)
						+ 20f + Glv.neuronSize,
				m_input_layer[0][0].position.y - 10f - Glv.neuronSize
						+ ((m_input_layer[0].length * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize) * 0.5f),
				new PVector(
						m_output_layer[0][0].position.x - 10f - Glv.neuronSize,
						m_output_layer[0][0].position.y - 10f - Glv.neuronSize
								+ ((m_output_layer[0].length * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize) * 0.5f));
	}

	public void drawAnalysis(Environment env) {

		drawBoundary(m_input_layer[0][0].position, m_input_layer.length, m_input_layer[0].length, "Input Layer");

		for (int i = 0; i < m_input_layer.length; i++) {
			for (int j = 0; j < m_input_layer[i].length; j++) {
				p.pushMatrix();
				{
					//					p.translate(p.width / 4 - (Glv.neuronSize * 1.2f * m_input_layer.length) * 0.5f,
					//							p.height / 2 - (Glv.neuronSize * 1.2f * m_input_layer[0].length) * 0.5f);
					//					p.translate(Glv.neuronSize * 1.2f * i, Glv.neuronSize * 1.2f * j);
					//	p.translate(i * (p.width / 3f) / m_input_layer.length, j * p.width / m_input_layer[i].length);
					m_input_layer[i][j].draw();
				}
				p.popMatrix();
			}
		}

		drawBoundary(
				new PVector(env.editorLayer[0][0].position.x + 3f * p.width / 10f, env.editorLayer[0][0].position.y),
				env.editorLayer.length, env.editorLayer[0].length, "Output Layer");

		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer[i].length; j++) {
				p.pushMatrix();
				{
					//					p.translate(0, -p.height / 4);
					//					p.translate(3f * p.width / 4 - (Glv.neuronSize * 1.2f * m_output_layer.length) * 0.5f,
					//							p.height / 2 - (Glv.neuronSize * 1.2f * m_output_layer[0].length) * 0.5f);
					//					p.translate(Glv.neuronSize * 1.2f * i, Glv.neuronSize * 1.2f * j);
					m_output_layer[i][j].draw();
				}
				p.popMatrix();
			}
		}
		if (lastCard != null)
			lastCard.drawOnlyAnalysis(env);

		drawLineBetween(new PVector(
				m_input_layer[0][0].position.x - 10f - Glv.neuronSize + (m_input_layer.length * Glv.neuronSize * 1.2f)
						+ 20f + Glv.neuronSize,
				m_input_layer[0][0].position.y - 10f - Glv.neuronSize
						+ ((m_input_layer[0].length * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize) * 0.5f),
				new PVector(
						(env.editorLayer[0][0].position.x + 3f * p.width / 10f) - 10f - Glv.neuronSize,
						env.editorLayer[0][0].position.y - 10f - Glv.neuronSize
								+ ((env.editorLayer[0].length * Glv.neuronSize * 1.2f) + 20f + Glv.neuronSize) * 0.5f));

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
				p.stroke(360,180);
				p.line(input.x, input.y, output.x, output.y);
				p.fill(360);
				p.ellipse(input.x, input.y,  7f, 7f);
				p.ellipse(output.x, output.y,  7f, 7f);
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
