package firsteclipse;

import processing.core.PApplet;

public class Network {
	private PApplet p;

	Neuron[] m_input_layer;
	Neuron[] m_hidden_layer;
	Neuron[] m_output_layer;

	public Network(PApplet _p, int inputs, int hidden, int outputs) {
		p = _p;

		for (int i = 0; i < m_input_layer.length; i++) {
			m_input_layer[i] = new Neuron(p);
		}

		for (int i = 0; i < m_hidden_layer.length; i++) {
			m_hidden_layer[i] = new Neuron(p, m_input_layer);
		}

		for (int i = 0; i < m_output_layer.length; i++) {
			m_output_layer[i] = new Neuron(p, m_hidden_layer);
		}
	}

}
