package firsteclipse;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import controlP5.Println;
import processing.core.PApplet;

public class Network {
	private static PApplet p;

	private static Neuron[][] m_input_layer;
	private static Neuron[][] m_hidden_layer;
	private static Neuron[][] m_output_layer;

	public Network(PApplet _p, int inputsX, int inputsY, int hiddenX, int hiddenY, int outputsX, int outputsY) {
		p = _p;

		m_input_layer = new Neuron[inputsX][inputsY];
		m_hidden_layer = new Neuron[hiddenX][hiddenY];
		m_output_layer = new Neuron[outputsX][outputsY];

		for (int i = 0; i < m_input_layer.length; i++) {
			for (int j = 0; j < m_input_layer[i].length; j++) {
				m_input_layer[i][j] = new Neuron(p);
			}
		}

		for (int i = 0; i < m_hidden_layer.length; i++) {
			for (int j = 0; j < m_hidden_layer[i].length; j++) {
				m_hidden_layer[i][j] = new Neuron(p, m_input_layer);
			}
		}

		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer[i].length; j++) {
				m_output_layer[i][j] = new Neuron(p, m_hidden_layer);
			}
		}
	}

	public void draw() {
		for (int i = 0; i < m_input_layer.length; i++) {
			for (int j = 0; j < m_input_layer[i].length; j++) {
				p.pushMatrix();
				{
					p.translate(p.width / 4 - (Glv.neuronSize * 1.2f * m_input_layer.length) * 0.5f,
							p.height / 2 - (Glv.neuronSize * 1.2f * m_input_layer[0].length) * 0.5f);
					p.translate(Glv.neuronSize * 1.2f * i, Glv.neuronSize * 1.2f * j);
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

		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer[i].length; j++) {
				p.pushMatrix();
				{
					p.translate(3f * p.width / 4 - (Glv.neuronSize * 1.2f * m_output_layer.length) * 0.5f,
							p.height / 2 - (Glv.neuronSize * 1.2f * m_output_layer[0].length) * 0.5f);
					p.translate(Glv.neuronSize * 1.2f * i, Glv.neuronSize * 1.2f * j);
					m_output_layer[i][j].draw();
				}
				p.popMatrix();
			}
		}

	}

	//---> Neuron interaction
	public void respond(MyData card) {

		//p.println(card.analysisID);
		for (int i = 0; i < m_input_layer.length; i++) {
			for (int j = 0; j < m_input_layer[i].length; j++) {
				//p.print(card._analysis[i][j] + ",");
				//p.print(	m_input_layer[i][j].m_output );

			//	if (i < card._analysis.length && j < card._analysis[0].length && card._analysis[i][j] != null) {

					m_input_layer[i][j].m_output = card._analysis[i][j];
				//}
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
		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer[i].length; j++) {
				m_output_layer[i][j].setError(outputs[i][j]);
				m_output_layer[i][j].train();
			}

		}
		for (int i = 0; i < m_hidden_layer.length; i++) {
			for (int j = 0; j < m_hidden_layer[i].length; j++) {
				m_hidden_layer[i][j].train();
			}
		}
	}
	//<---
}
