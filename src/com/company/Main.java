package com.company;

public class Main {
//    volatile static int seconds = 0;


    public static void main(String[] args) {
        CounterWrapper counterWrapper = new CounterWrapper();

        final Object locker = new Object();
        new Thread(() -> {
            while (true) {
                System.out.println(counterWrapper.getCounter());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (locker) {
                    locker.notifyAll();
                }
                counterWrapper.add();
            }
        }).start();
        Messenger messenger = new Messenger(7, locker, counterWrapper);
        Messenger messenger1 = new Messenger(5, locker, counterWrapper);

//IT IS WORK
        new Thread(messenger).start();
        new Thread(messenger1).start();
//
// IT IS NOT WORK
//        messenger.run();
//        System.out.println("After first run"); //this message is not write on console
//        messenger1.run();


    }


    static class Messenger implements Runnable {
        int secondDelay;
        final Object locker;
        CounterWrapper counterWrapper;

        public Messenger(int secondDelay, Object locker, CounterWrapper counterWrapper) {
            this.secondDelay = secondDelay;
            this.locker = locker;
            this.counterWrapper = counterWrapper;
        }

        @Override
        public synchronized void run() {
            System.out.println("seconds delay " + secondDelay);
            boolean printed = false;
            while (true) {
                if (counterWrapper.getCounter() % secondDelay != 0) {
                    synchronized (locker) {
                        try {
                            printed = false;
                            locker.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (!printed) {
                        System.out.println("Прошло " + secondDelay + " секунд");
                        printed = true;
                    }
                }
            }
        }
    }
}
