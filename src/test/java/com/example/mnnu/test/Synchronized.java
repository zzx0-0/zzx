package com.example.mnnu.test;

class Synchronized implements Runnable {

    private static int ticket = 100;

    private static int f = 0;

    @Override
    public void run() {
        synchronized (this) {
            if (ticket >= 0) {
                System.out.println("还有票数-->" + (ticket));
                ticket--;
            }
        }
    }

}

class SynchronizedTest {

    public static void main(String[] args) {
        Synchronized thread = new Synchronized();
        Thread t1 = new Thread(thread);
        Thread t2 = new Thread(thread);
        Thread t3 = new Thread(thread);
        for (int i = 0; i < 1000; i++) {
            t1.run();
            t2.run();
            t3.run();
        }
    }

}