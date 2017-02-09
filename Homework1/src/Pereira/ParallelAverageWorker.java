package Pereira;

import java.util.LinkedList;
import java.util.Random;

public class ParallelAverageWorker extends Thread{
	
	protected LinkedList<Integer> list;
	protected float walkingAverage = 0; // initialize to 0
	
	public ParallelAverageWorker(LinkedList<Integer> list) {
		this.list = list;
	}
	
	
	public void run() {
		while (true) {
			int number;
			// check if list is not empty and removes the head
			// synchronization needed to avoid atomicity violation
			synchronized(list) {
				if (list.isEmpty())
					return; // list is empty
				
				//removing number randomly
				/*Random rand = new Random();
				int next = rand.nextInt(list.size());*/
				number = list.remove();
			}
			
			// update walkingAverage according to new value
			// TODO: IMPLEMENT CODE HERE
			walkingAverage = (getwalkingAverage()+number)/2;
			
			
		}
	}
	
	public float getwalkingAverage() {
		return walkingAverage;
	}


}
