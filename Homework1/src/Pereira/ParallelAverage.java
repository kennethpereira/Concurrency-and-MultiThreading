package Pereira;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class ParallelAverage {

	int numThreads;
	ArrayList<ParallelAverageWorker> workers; // 

	public ParallelAverage(int numThreads) {
		workers = new ArrayList<ParallelAverageWorker>(numThreads);
	}


	
	public static void main(String[] args) {
		int numThreads = 4; // number of threads for the maximizer
		int numElements = 10000000; // number of integers in the list
		
		ParallelAverage avg = new ParallelAverage(numThreads);
		LinkedList<Integer> list = new LinkedList<Integer>();
		
		// populate the list
		// TODO: change this implementation to test accordingly
		
		Random rand = new Random();
		for (int i=0; i<numElements; i++) {
			int next = rand.nextInt(10);
			//System.out.println(next);
			list.add(next);
		}


		// run the maximizer
		try {
			System.out.println("The walking Average is:"+avg.avg(list, numThreads));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Finds the maximum by using <code>numThreads</code> instances of
	 * <code>ParallelMaximizerWorker</code> to find partial maximums and then
	 * combining the results.
	 * @param list <code>LinkedList</code> containing <code>Integers</code>
	 * @return Maximum element in the <code>LinkedList</code>
	 * @throws InterruptedException
	 */
	public float avg(LinkedList<Integer> list, int numThreads) throws InterruptedException {
		//int max = Integer.MIN_VALUE; // initialize max as lowest value
		float walkingAverage = 0;
		System.out.println("initial worker size--->"+workers.size());
		// run numThreads instances of ParallelMaximizerWorker
		for (int i=0; i < numThreads; i++) {
			workers.add(i, new ParallelAverageWorker(list));
			workers.get(i).start();
			
		}
		
		// wait for threads to finish
		for (int i=0; i<workers.size(); i++)
			workers.get(i).join();
		//
		/*for (int i=0; i<workers.size(); i++)
			System.out.println("Thread"+i+"---->"+workers.get(i).getwalkingAverage());
		*/
		// TODO: IMPLEMENT CODE HERE
		//ParallelMaximizerWorker maximizer=new ParallelMaximizerWorker(list);
		for(ParallelAverageWorker paralleWorker:workers){
			walkingAverage = (walkingAverage+paralleWorker.getwalkingAverage())/2;
			//System.out.println("--->"+walkingAverage);
		}
		//System.out.println(walkingAverage);
		return walkingAverage;
	}

}
