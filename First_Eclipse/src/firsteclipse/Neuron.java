package firsteclipse;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.sun.org.apache.bcel.internal.generic.POP;

import processing.core.PApplet;
import processing.core.PVector;

public class Neuron {

	private PApplet p;

	float m_output;

	Neuron[][] m_inputs;
	float[][] m_weights;

	float m_error;
	int colour=0;

	PVector position = new PVector();

	Random r = new Random();
	public boolean iAmChosen =false;

	public Neuron(PApplet _p, PVector _position) { // For INPUT neurons
		p = _p;
		position = _position;

	}

	public Neuron(PApplet _p, PVector _position, Neuron[][] inputs) { // For HIDDEN and OUTPUT neurons
		p = _p;
		position = _position;

		m_inputs = new Neuron[inputs.length][inputs[0].length];
		m_weights = new float[inputs.length][inputs[0].length];

		//		p.println("m_inputs.length: " + m_inputs.length);
		//		p.println("m_inputs[0].length: " + m_inputs[0].length);
		//		p.println("m_weigths.length: " + m_weights.length);
		//		p.println("m_weights[0].length: " + m_weights[0].length);
		//float num = p.random(1f); 

		//p.println(-1f + 2f * r.nextFloat());

		for (int i = 0; i < inputs.length; i++) {
			for (int j = 0; j < inputs[i].length; j++) {
				m_inputs[i][j] = inputs[i][j];
				//p.println(m_inputs[i][j]);
				m_weights[i][j] = -1f + 2f * r.nextFloat();
			}
		}
	}

	void draw() {
		p.pushMatrix();
		{
			p.translate(position.x, position.y);
			p.fill(360,colour,180 * (1 - m_output));
			p.ellipse(0, 0, Glv.neuronSize, Glv.neuronSize);
		}
		p.popMatrix();
	}

	void setError(float desired) {
		m_error = desired - m_output;
		//p.println("m_Error: " + m_error);
	}

	void train() {
		float delta = (1.0f - m_output) * (1.0f + m_output) * m_error * Glv.LEARNING_RATE;

		for (int i = 0; i < m_inputs.length; i++) {
			for (int j = 0; j < m_inputs[i].length; j++) {
				m_inputs[i][j].m_error += m_weights[i][j] * m_error;
				m_weights[i][j] += m_inputs[i][j].m_output * delta;
			}
		}
	}

	void respond() {
		float input = 0.0f;
		for (int i = 0; i < m_inputs.length; i++) {
			for (int j = 0; j < m_inputs[i].length; j++) {
				input += m_inputs[i][j].m_output * m_weights[i][j];
			}
		}
		m_output = NeuralNetworkManagment.lookupSigmoid(input);
		m_error = 0.0f;
	}

}
