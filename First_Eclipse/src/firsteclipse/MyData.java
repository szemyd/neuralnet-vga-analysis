package firsteclipse;

import java.util.ArrayList;

import processing.core.PApplet;

public class MyData {

	PApplet p;
	ArrayList<String[]> analysis = new ArrayList<String[]>();
	ArrayList<String[]> form = new ArrayList<String[]>();

	public Integer[][] _analysis;
	public Integer[][] _form;

	public Integer analysisID = 0;

	public MyData(PApplet _p) {
		// TODO Auto-generated constructor stub
		p = _p;
	}

	public void convert() {
		if (analysis.size() > 0 && form.size() > 0) {
			 _analysis = new Integer[analysis.size()][analysis.get(0).length];
			_form = new Integer[form.size()][form.get(0).length];
			//

		//	p.println("Id: " + analysisID);

			int i = 0;
			for (String[] strings : analysis) {
				for (int j = 0; j < _analysis[i].length; j++) {
					if (strings[j].length() > 0 && strings[j] != null) {
						if (Character.isDigit(strings[j].charAt(0))) {
							int num = Integer.valueOf(strings[j]);
							if (i < _analysis.length && j < _analysis[i].length)
								_analysis[i][j] = num;
						}
					}
//					if (_analysis[i][j] != null)
//						p.print(_analysis[i][j] + ",");
				}
//				p.println("");
				i++;
			}

			//			for (String[] strings : form) {
			//				p.println(strings);
			//			}
			//		
			//			for (String[] strings : analysis) {
			//				p.println(strings);
			//			}
			//			
			i = 0;
			for (String[] strings : form) {
				for (int j = 0; j < _form[i].length; j++) {
					if (strings[j].length() > 0 && strings[j] != null) {
						if (Character.isDigit(strings[j].charAt(0))) {
							int num = Integer.valueOf(strings[j]);
							if (i < _form.length && j < _form[i].length) {
								if (num == 0)
									_form[i][j] = -1;
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
		if (analysisID == 0) return true;
		
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
