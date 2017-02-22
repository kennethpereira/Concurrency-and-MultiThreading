package Pereira;

import java.util.Arrays;
import java.util.Comparator;
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

		Customer[] cust = (Customer[]) Simulation.orderList.toArray();
		Arrays.sort(cust, new Comparator<Customer>() {
			public int compare(Customer o1, Customer o2) {
				if (o1.getPriority() > o2.getPriority()) {
					return o1.getPriority();
				} else {
					return o2.getPriority();
				}
			}
		});

		Simulation.orderList = (Queue<Customer>) Arrays.asList(cust);
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
					Simulation.logEvent(SimulationEvent.cookReceivedOrder(this, currentCustomer.getOrder(),
							currentCustomer.getOrderNum()));
					Simulation.orderList.notifyAll();
				}
				
				

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