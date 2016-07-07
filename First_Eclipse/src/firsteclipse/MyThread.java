package firsteclipse;

import org.omg.PortableServer.THREAD_POLICY_ID;

import processing.core.PApplet;

class MyThread extends Thread {
	public Thread t;
	//ManageBoxes manBox;

	ManageBoxes manBox;
	SpaceSyntax spaceSyntax;

	int threadID;
	boolean VGADone=false;

	private static PApplet p;

	MyThread(PApplet _p, int id) {
		p = _p;

		threadID = id;

		if(Glv.shP)System.out.println("Creating " + threadID);

		//manBox = _manBox;

		manBox = new ManageBoxes(p);
		spaceSyntax = new SpaceSyntax(p, threadID);

	}

	public void run() {
		if(Glv.shP) System.out.println("Running " + threadID);
		try {
			startThread();

			Thread.sleep(50);

		} catch (InterruptedException e) {
			if(Glv.shP) System.out.println("Thread " + threadID + " interrupted.");
		}
		if(Glv.shP) System.out.println("Thread " + threadID + " exiting.");
	}

	public void startThread() {

		int ellapsedTime = p.second() + p.minute() * 60 + p.hour() * 360;
		Glv.seed++;

		manBox.setup(); // 01. Creates the boxes in a random form.
		spaceSyntax.setup(manBox.boxes); // 02. Creates starting grid of rectangles for the spacesyntax VGA.
		
		manBox.createHeights(); // Iterates through the CA.
		//while(VGADone ==false)
		spaceSyntax();

		if(Glv.shP) p.println("Ellapsed time: " + ((p.second() + p.minute() * 60 + p.hour() * 360) - ellapsedTime));
	}

	public void spaceSyntax() {
		if (Glv.shouldSpaceSyntax)
		{
			spaceSyntax.VGA(manBox.boxes);
			VGADone=true;
		}
	}

	public void start() {
		if(Glv.shP) System.out.println("Starting " + threadID);
		if (t == null) {
			t = new Thread(this, Integer.toString(threadID));
			t.start();
		}
	}

}
