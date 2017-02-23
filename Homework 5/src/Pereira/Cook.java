package Pereira;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * Cooks are simulation actors that have at least one field, a name. When
 * running, a cook attempts to retrieve outstanding orders placed by Eaters and
 * process them.
 */
public class Cook implements Runnable {
	private final String name;
	private Customer currentCustomer;
	public List<Food> finishedFood = new LinkedList<Food>();
	/**
	 * You can feel free modify this constructor. It must take at least the
	 * name, but may take other parameters if you would find adding them useful.
	 *
	 * @param: the
	 *             name of the cook
	 */
	public Cook(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	/**
	 * This method executes as follows. The cook tries to retrieve orders placed
	 * by Customers. For each order, a List<Food>, the cook submits each Food
	 * item in the List to an appropriate Machine, by calling makeFood(). Once
	 * all machines have produced the desired Food, the order is complete, and
	 * the Customer is notified. The cook can then go to process the next order.
	 * If during its execution the cook is interrupted (i.e., some other thread
	 * calls the interrupt() method on it, which could raise
	 * InterruptedException if the cook is blocking), then it terminates.
	 */
	@SuppressWarnings("unchecked")
	public void run() {

		Simulation.logEvent(SimulationEvent.cookStarting(this));
		try {
			while (true) {
				// YOUR CODE GOES HERE...

				synchronized (Simulation.orderList) {

					while (Simulation.orderList.isEmpty()) {
						Simulation.orderList.wait();
					}
					// Simulation.orderList.s

					currentCustomer = Simulation.orderList.remove();
					System.out.println("This is customer priority ---->"+currentCustomer.getPriority());
					Simulation.logEvent(SimulationEvent.cookReceivedOrder(this, currentCustomer.getOrder(),
							currentCustomer.getOrderNum()));
					Simulation.orderList.notifyAll();
				}
				
				for(int index = 0; index < currentCustomer.getOrder().size(); index++){
					Food currFood = currentCustomer.getOrder().get(index);
					if(currFood.equals(FoodType.burger)){
						synchronized(Simulation.burgerDispenser.foodList){
							while(!(Simulation.burgerDispenser.foodList.size() < Simulation.burgerDispenser.capacity)){
								Simulation.burgerDispenser.foodList.wait();
							}
							Simulation.logEvent(SimulationEvent.cookStartedFood(this, FoodType.burger , currentCustomer.getOrderNum()));
							Simulation.burgerDispenser.makeFood(this, currentCustomer.getOrderNum());
							Simulation.burgerDispenser.foodList.notifyAll();

						}
						
					}else if(currFood.equals(FoodType.fries)){
						synchronized(Simulation.deepFryer.foodList){
							while(!(Simulation.deepFryer.foodList.size() < Simulation.deepFryer.capacity)){
								Simulation.deepFryer.foodList.wait();
							}
							Simulation.logEvent(SimulationEvent.cookStartedFood(this, FoodType.fries , currentCustomer.getOrderNum()));
							Simulation.deepFryer.makeFood(this,currentCustomer.getOrderNum());
							Simulation.deepFryer.foodList.notifyAll();
							
						}
						
					}else{
						synchronized(Simulation.coffeeMachine.foodList){
							while(!(Simulation.coffeeMachine.foodList.size() < Simulation.coffeeMachine.capacity)){
								Simulation.coffeeMachine.foodList.wait();
							}
							Simulation.logEvent(SimulationEvent.cookStartedFood(this, FoodType.coffee , currentCustomer.getOrderNum()));
							Simulation.coffeeMachine.makeFood(this,currentCustomer.getOrderNum());
							Simulation.coffeeMachine.foodList.notifyAll();
							
						}
					}
				}
				synchronized(finishedFood){
					while(!(finishedFood.size() == currentCustomer.getOrder().size())){
						finishedFood.wait();
						finishedFood.notifyAll();
					}
				}
				Simulation.logEvent(SimulationEvent.cookCompletedOrder(this, currentCustomer.getOrderNum()));
				
				synchronized(Simulation.completedOrder){
					Simulation.completedOrder.put(currentCustomer, true);
					Simulation.completedOrder.notifyAll();
				}
				finishedFood = new LinkedList<Food>();
			
			}
				

			
		} catch (InterruptedException e) {
			// This code assumes the provided code in the Simulation class
			// that interrupts each cook thread when all customers are done.
			// You might need to change this if you change how things are
			// done in the Simulation class.
			Simulation.logEvent(SimulationEvent.cookEnding(this));
		}
	}
}