package firsteclipse;

import processing.core.PApplet;

public class Neuron {

	PApplet p;
	
	float m_output;

	Neuron[] m_inputs;
	float[] m_weights;

	float m_error;

	public Neuron(PApplet _p) {
		p=_p;
	}

	public Neuron(PApplet _p, Neuron[] inputs) {
		p=_p;
		
		m_inputs = new Neuron[inputs.length];
		m_weights = new float[inputs.length];

		for (int i = 0; i < inputs.length; i++) {
			m_inputs[i] = inputs[i];
			m_weights[i] = p.random(-1.0f, 1.0f);
		}
	}

	void setError(float desired) {
		m_error = desired - m_output;
	}

	void train() {
		float delta = (1.0f - m_output) * (1.0f + m_output) * m_error * Glv.LEARNING_RATE;

		for (int i = 0; i < m_inputs.length; i++) {
			m_inputs[i].m_error += m_weights[i] * m_error;
			m_weights[i] += m_inputs[i].m_output * delta;
		}
	}

	void respond() {
		float input = 0.0f;
		for (int i = 0; i < m_inputs.length; i++) {
			input += m_inputs[i].m_output * m_weights[i];
		}
		m_output = NeuralNetwork.lookupSigmoid(input);
		m_error = 0.0f;
	}

	void draw() {
		p.fill(128 * (1 - m_output));
		p.ellipse(0, 0, 16, 16);
	}

	
}
