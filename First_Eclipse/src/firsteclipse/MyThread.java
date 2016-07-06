package firsteclipse;

import processing.core.PApplet;

class MyThread extends Thread {
	private Thread t;
	private String threadName;
	ManageBoxes manBox;

	private static PApplet p;

	MyThread(String name, PApplet _p, ManageBoxes _manBox) {
		p = _p;

		threadName = name;
		System.out.println("Creating " + threadName);

		manBox = _manBox;
	}

	public void run() {
		System.out.println("Running " + threadName);
		try {
			startThread();
			
//			for (int i = 4; i > 0; i--) {
//				System.out.println("Thread: " + threadName + ", " + i);
//				
//				
//				// Let the thread sleep for a while.
				Thread.sleep(50);
//			}
		} catch (InterruptedException e) {
			System.out.println("Thread " + threadName + " interrupted.");
		}
		System.out.println("Thread " + threadName + " exiting.");
	}

	public void startThread() {

		int ellapsedTime = p.second() + p.minute() * 60 + p.hour() * 360;
		Glv.seed++;
		manBox.createHeights(); // Iterates through the CA.
		SpaceSyntax.setup(manBox.boxes); // 02. Creates starting grid of rectangles for the spacesyntax VGA.
		SpaceSyntax.VGA(manBox.boxes);


		p.println("Ellapsed time: " + ((p.second() + p.minute() * 60 + p.hour() * 360) - ellapsedTime));

	}

	public void start() {
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

}
