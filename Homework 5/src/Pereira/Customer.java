package Pereira;

import java.util.List;
import java.util.Random;

/**
 * Customers are simulation actors that have two fields: a name, and a list of
 * Food items that constitute the Customer's order. When running, an customer
 * attempts to enter the coffee shop (only successful if the coffee shop has a
 * free table), place its order, and then leave the coffee shop when the order
 * is complete.
 */
public class Customer implements Runnable {
	// JUST ONE SET OF IDEAS ON HOW TO SET THINGS UP...
	private final String name;
	private final List<Food> order;
	private final int orderNum;
	private final int priority;

	private static int runningCounter = 0;
	private static Random rand;

	/**
	 * You can feel free modify this constructor. It must take at least the name
	 * and order but may take other parameters if you would find adding them
	 * useful.
	 */
	public Customer(String name, List<Food> order) {
		this.name = name;
		this.order = order;
		this.orderNum = ++runningCounter;
		priority = rand.nextInt(3) + 1;
	}

	public String toString() {
		return name;
	}

	/**
	 * @return the order
	 */
	public List<Food> getOrder() {
		return order;
	}

	/**
	 * @return the orderNum
	 */
	public int getOrderNum() {
		return orderNum;
	}
	
	public int getPriority(){
		return priority;
	}

	/**
	 * This method defines what an Customer does: The customer attempts to enter
	 * the coffee shop (only successful when the coffee shop has a free table),
	 * place its order, and then leave the coffee shop when the order is
	 * complete.
	 */
	public void run() {
		// YOUR CODE GOES HERE...
		Simulation.logEvent(SimulationEvent.customerStarting(this));
		
		synchronized(Simulation.currCapacity){
			while(!(Simulation.currCapacity.size() < Simulation.events.get(0).simParams[2])){
				try {
					Simulation.currCapacity.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			Simulation.currCapacity.add(this);
			Simulation.logEvent(SimulationEvent.customerEnteredCoffeeShop(this));
			Simulation.currCapacity.notifyAll();
		}
		
		synchronized(Simulation.orderList){
			Simulation.orderList.add(this);
			Simulation.logEvent(SimulationEvent.customerPlacedOrder(this, this.order, this.orderNum));
			Simulation.orderList.notifyAll();
		}
		//initialize the persons order as not completed
		synchronized(Simulation.completedOrder){
			Simulation.completedOrder.put(this, false);
		}
		
		synchronized(Simulation.completedOrder){
			while(!(Simulation.completedOrder.get(this))){
				try {
					Simulation.completedOrder.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Simulation.logEvent(SimulationEvent.customerReceivedOrder(this, this.order, this.orderNum));
			Simulation.completedOrder.notifyAll();
		}
		synchronized(Simulation.currCapacity){
			Simulation.currCapacity.remove(this);
			Simulation.logEvent(SimulationEvent.customerLeavingCoffeeShop(this));
			Simulation.currCapacity.notifyAll();
		}
		

	}
}