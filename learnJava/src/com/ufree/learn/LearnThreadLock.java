package com.ufree.learn;

import java.util.concurrent.locks.ReentrantLock;

public class LearnThreadLock implements Runnable {
	// private TestFoo tfo = new TestFoo();
	//private TestBar tb = new TestBar();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LearnThreadLock lock1 = new LearnThreadLock();
		LearnThreadLock lock2 = new LearnThreadLock();
		LearnThreadLock lock3 = new LearnThreadLock();
		LearnThreadLock lock4 = new LearnThreadLock();
		LearnThreadLock lock5 = new LearnThreadLock();
		LearnThreadLock lock6 = new LearnThreadLock();
		Thread td1 = new Thread(lock1, "Thread[1]");
		//Thread td2 = new Thread(lock1, "Thread[2]");
		Thread td2 = new Thread(lock2, "Thread[2]");
		//Thread td3 = new Thread(lock1, "Thread[3]");
		Thread td3 = new Thread(lock3, "Thread[3]");
		Thread td4 = new Thread(lock4, "Thread[4]");
		Thread td5 = new Thread(lock5, "Thread[5]");
		Thread td6 = new Thread(lock6, "Thread[6]");
		td1.start();
		td2.start();
		td3.start();
		td4.start();
		td5.start();
		td6.start();
	}

	// test for class instance
	public void run() {
		for (int k = 0; k < 3; k++) {
			// TestFoo.updateX(25, k);
			// tfo.updateX(25, k);
			//tb.updateY(25, k);
			TestBar.updateY(25, k);
		}
	}
}

class TestFoo {
	private static int x = 100;

	// public synchronized int updateX(int y, int k) {
	public static synchronized int updateX(int y, int k) {
		x = x - y;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("k==" + k + ", | "
				+ Thread.currentThread().getName() + ".x==" + x);
		return x;
	}
}

class TestBar {
	private static int x = 100;
	// lock class instance
	private static final ReentrantLock lock = new ReentrantLock();

	public static int updateY(int y, int k) {
		lock.lock();

		try {
			x = x - y;
			Thread.sleep(2000);
			System.out.println("k==" + k + ", | "
					+ Thread.currentThread().getName() + ".x==" + x);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

		return x;
	}
}
