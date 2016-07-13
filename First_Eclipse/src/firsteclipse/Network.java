package firsteclipse;

import com.sun.corba.se.spi.orbutil.fsm.Input;

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

	public void respond(MyData card) {
		for (int i = 0; i < m_input_layer.length; i++) {
			for (int j = 0; j < m_input_layer[i].length; j++) {
				m_input_layer[i][j].m_output = card._analysis[i][j];
			}
		}

		for (int i = 0; i < m_hidden_layer.length; i++) {
			for (int j = 0; j < m_hidden_layer[i].length; j++) {
				m_hidden_layer[i][j].respond();
			}
		}

		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer.length; j++) {
				m_output_layer[i][j].respond();
			}
		}
	}

	public  void draw() {
		for (int i = 0; i < m_input_layer.length; i++) {
			for (int j = 0; j < m_input_layer[i].length; j++) {
				p.pushMatrix();
				{
					p.translate(i * (p.width / 3f) / m_input_layer.length, j * p.width / m_input_layer[i].length);
					m_input_layer[i][j].draw();
				}
				p.popMatrix();
			}
		}

		for (int i = 0; i < m_hidden_layer.length; i++) {
			for (int j = 0; j < m_hidden_layer[i].length; j++) {
				p.pushMatrix();
				{
					p.translate((i * (p.width / 3f) / m_hidden_layer.length) + (p.width / 3f),
							j * p.width / m_hidden_layer[i].length);
					m_hidden_layer[i][j].draw();
				}
				p.popMatrix();
			}
		}

		for (int i = 0; i < m_output_layer.length; i++) {
			for (int j = 0; j < m_output_layer[i].length; j++) {
				p.pushMatrix();
				{
					p.translate((i * (p.width / 3f) / m_output_layer.length) + (2f * p.width / 3f),
							j * p.width / m_output_layer[i].length);
					m_output_layer[i][j].draw();
				}
				p.popMatrix();
			}
		}
	}
}
