package com.company;

public class Main {
    volatile static int seconds = 0;

    public static void main(String[] args) {
        final Object locker = new Object();
        new Thread(() -> {
            while (true) {
                System.out.println(seconds);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (locker) {
                    locker.notifyAll();
                }
                seconds++;
            }
        }).start();
        Messenger messenger = new Messenger(7, locker);
        Messenger messenger1 = new Messenger(5, locker);

//IT IS WORK
//        new Thread(messenger).start();
//        new Thread(messenger1).start();
//
// IT IS NOT WORK
//        messenger.run();
//        System.out.println("After first run"); //this message is not write on console
//        messenger1.run();


    }


    static class Messenger implements Runnable {
        int secondDelay;
        final Object locker;

        public Messenger(int secondDelay, Object locker) {
            this.secondDelay = secondDelay;
            this.locker = locker;
        }

        @Override
        public synchronized void run() {
            System.out.println("seconds delay " + secondDelay);
            boolean printed = false;
            while (true) {
                if (seconds % secondDelay != 0) {
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
