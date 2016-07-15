package firsteclipse;

import org.omg.PortableServer.THREAD_POLICY_ID;

import processing.core.PApplet;

class MyThreadNeuralNet extends Thread {
	public Thread t;
	//ManageBoxes manBox;

	NeuralNetworkManagment net;// = new NeuralNetworkManagment(this);
	
	int threadID;
	

	private static PApplet p;

	MyThreadNeuralNet(PApplet _p, int id) {
		p = _p;

		net = new NeuralNetworkManagment(p);
		threadID = id;

		if(Glv.shP)System.out.println("Creating " + threadID);
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
		p.println("I'm going to load the CSV cards now");
		
		net.loadGenData(); // Loads the generated data.
	
		if (Glv.shP)
			p.println("< Loaded existing data. Ellapsed time: "
					+ ((p.second() + p.minute() * 60 + p.hour() * 360) - ellapsedTime) + " >");
	}


	public void start() {
		if(Glv.shP) System.out.println("Starting " + threadID);
		if (t == null) {
			t = new Thread(this, Integer.toString(threadID));
			t.start();
		}
	}

}
