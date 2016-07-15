package firsteclipse;

import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import processing.core.PApplet;

public class MyData {

	PApplet p;
	ArrayList<String[]> analysis = new ArrayList<String[]>();
	ArrayList<String[]> form = new ArrayList<String[]>();

	public Float[][] _analysis;
	public Float[][] _form;

	public Integer analysisID = 0;

	public MyData(PApplet _p) {
		p = _p;
	}

	public void draw() {
		for (int i = 0; i < _form.length; i++) {
			for (int j = 0; j < _form[i].length; j++) {
				p.pushMatrix();
				{
					p.translate(0, p.height/4);
					p.translate(3f * p.width / 4 - (Glv.neuronSize * 1.2f * _form.length) * 0.5f,
							p.height / 2 - (Glv.neuronSize * 1.2f * _form[0].length * 0.5f));
					p.translate(Glv.neuronSize * 1.2f * i, Glv.neuronSize * 1.2f * j);
					
					if (_form[i][j] > 0.1f) p.fill(360);
					else p.fill(0);
					
					p.ellipse(0, 0, Glv.neuronSize, Glv.neuronSize);
				}
				p.popMatrix();
			}
		}
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
								_analysis[i][j] = p.map(num, Glv.highLowForNN.y, 1000, -1f, 1f); // Mapping the values according to the highest and lowest visibility in the set.
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

}
