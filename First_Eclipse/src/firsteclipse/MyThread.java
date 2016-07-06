package firsteclipse;

import org.omg.PortableServer.THREAD_POLICY_ID;

import processing.core.PApplet;

class MyThread extends Thread {
	private Thread t;
	//ManageBoxes manBox;
	

	ManageBoxes manBox;
	SpaceSyntax spaceSyntax;
	
	int threadID;

	private static PApplet p;

	MyThread(PApplet _p, int id) {
		p = _p;

		threadID=id;
		
		System.out.println("Creating " + threadID);

		//manBox = _manBox;
		
		manBox = new ManageBoxes(p);
		spaceSyntax = new SpaceSyntax(p, threadID);
		
	
	}

	public void run() {
		System.out.println("Running " + threadID);
		try {
			startThread();
			
//			for (int i = 4; i > 0; i--) {
//				System.out.println("Thread: " + threadID + ", " + i);
//				
//				
//				// Let the thread sleep for a while.
				Thread.sleep(50);
//			}
		} catch (InterruptedException e) {
			System.out.println("Thread " + threadID + " interrupted.");
		}
		System.out.println("Thread " + threadID + " exiting.");
	}

	public void startThread() {

		int ellapsedTime = p.second() + p.minute() * 60 + p.hour() * 360;
		Glv.seed++;
		
		manBox.setup(); // 01. Creates the boxes in a random form.
		spaceSyntax.setup(manBox.boxes); // 02. Creates starting grid of rectangles for the spacesyntax VGA.
		
		manBox.createHeights(); // Iterates through the CA.
		spaceSyntax.VGA(manBox.boxes);

		p.println("Ellapsed time: " + ((p.second() + p.minute() * 60 + p.hour() * 360) - ellapsedTime));

	}

	public void start() {
		System.out.println("Starting " + threadID);
		if (t == null) {
			t = new Thread(this, Integer.toString(threadID));
			t.start();
		}
	}

}
