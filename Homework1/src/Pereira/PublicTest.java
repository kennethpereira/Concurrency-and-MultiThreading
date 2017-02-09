package Pereira;

import static org.junit.Assert.*;
import java.util.*;


import org.junit.Test;

import Pereira.ParallelMaximizer;

public class PublicTest {

	private int	threadCount = 10; // number of threads to run
	private ParallelMaximizer maximizer = new ParallelMaximizer(threadCount);
	private ParallelAverage average = new ParallelAverage(threadCount);
	
	@Test
	public void compareMax() {
		int size = 10000; // size of list
		LinkedList<Integer> list = new LinkedList<Integer>();
		Random rand = new Random();
		int serialMax = Integer.MIN_VALUE;
		int parallelMax = 0;
		// populate list with random elements
		for (int i=0; i<size; i++) {
			int next = rand.nextInt();
			list.add(next);
			serialMax = Math.max(serialMax, next); // compute serialMax
		}
		// try to find parallelMax
		try {
			parallelMax = maximizer.max(list,threadCount);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("The test failed because the max procedure was interrupted unexpectedly.");
		} catch (Exception e) {
			e.printStackTrace();
			fail("The test failed because the max procedure encountered a runtime error: " + e.getMessage());
		}
		
		assertEquals("The serial max doesn't match the parallel max", serialMax, parallelMax);
	}
	
	//for average
	
	@SuppressWarnings("deprecation")
	@Test
	public void compareAverage() {
		int size = 10000; // size of list
		LinkedList<Integer> list = new LinkedList<Integer>();
		Random rand = new Random();
		float serialAvg = 0;
		float parallelAverage = 0;
		// populate list with random elements
		
		for (int i=0; i<size; i++) {
			int next = rand.nextInt();
			list.add(next);
			serialAvg = (serialAvg+next)/2; // compute serialMax
		}
		// try to find parallelMax
		try {
			parallelAverage = average.avg(list,threadCount);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("The test failed because the max procedure was interrupted unexpectedly.");
		} catch (Exception e) {
			e.printStackTrace();
			fail("The test failed because the max procedure encountered a runtime error: " + e.getMessage());
		}
		
		assertEquals("The serial max doesn't match the parallel max", serialAvg, parallelAverage);
	}
}
