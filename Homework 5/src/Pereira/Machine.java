package Pereira;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A Machine is used to make a particular Food.  Each Machine makes
 * just one kind of Food.  Each machine has a capacity: it can make
 * that many food items in parallel; if the machine is asked to
 * produce a food item beyond its capacity, the requester blocks.
 * Each food item takes at least item.cookTimeMS milliseconds to
 * produce.
 */
public class Machine {
	public final String machineName;
	public final Food machineFoodType;
	int capacity;
	Queue<Food> foodList;
	//YOUR CODE GOES HERE...


	/**
	 * The constructor takes at least the name of the machine,
	 * the Food item it makes, and its capacity.  You may extend
	 * it with other arguments, if you wish.  Notice that the
	 * constructor currently does nothing with the capacity; you
	 * must add code to make use of this field (and do whatever
	 * initialization etc. you need).
	 */
	public Machine(String nameIn, Food foodIn, int capacityIn) {
		this.machineName = nameIn;
		this.machineFoodType = foodIn;
				
		//YOUR CODE GOES HERE...
		this.capacity = capacityIn;
		foodList = new LinkedList<Food>();	

	}
	

	

	/**
	 * This method is called by a Cook in order to make the Machine's
	 * food item.  You can extend this method however you like, e.g.,
	 * you can have it take extra parameters or return something other
	 * than Object.  It should block if the machine is currently at full
	 * capacity.  If not, the method should return, so the Cook making
	 * the call can proceed.  You will need to implement some means to
	 * notify the calling Cook when the food item is finished.
	 */
	public void makeFood(Cook name, int orderNum) throws InterruptedException {
		//YOUR CODE GOES HERE...
		foodList.add(machineFoodType);
		
		Thread currentThread = new Thread(new CookAnItem(name,orderNum));
		currentThread.start();
		
	}

	//THIS MIGHT BE A USEFUL METHOD TO HAVE AND USE BUT IS JUST ONE IDEA
	private class CookAnItem implements Runnable {
		
		Cook cook;
		int orderNum;
		
		public CookAnItem(Cook cook, int orderNum) {
			// TODO Auto-generated constructor stub
			this.cook = cook;
			this.orderNum = orderNum;
		}

		public void run() {
			try {
				//YOUR CODE GOES HERE...
				//Simulation.logEvent(SimulationEvent.machineCookingFood(Machine.this, machineFoodType));
				synchronized(Simulation.machCapacity){
					while(!(Simulation.machCapacity.size() < Simulation.events.get(0).simParams[3])){
						try {
							Simulation.machCapacity.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					
					//Simulation.machCapacity.add(this);
					
					Simulation.machCapacity.notifyAll();
				}
				Simulation.logEvent(SimulationEvent.machineCookingFood(Machine.this, machineFoodType));
				Thread.sleep(machineFoodType.cookTimeMS);
				Simulation.logEvent(SimulationEvent.machineDoneFood(Machine.this, machineFoodType));
				Simulation.logEvent(SimulationEvent.cookFinishedFood(cook, machineFoodType,orderNum));
				synchronized(foodList){
					foodList.remove();
					foodList.notifyAll();	
				}
				synchronized(name.finishedFood){
					cook.finishedFood.add(machineFoodType);
					cook.finishedFood.notifyAll();	
				}
				
			} catch(InterruptedException e) { }
		} 
	}
 

	public String toString() {
		return machineName;
	}
}