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
        System.out.println("start first");
        new Messenger(7, locker).start();
        System.out.println("start second");
        new Messenger(5, locker).start();
    }

    static class Messenger extends Thread {
        int secondDelay;
        final Object locker;

        public Messenger(int secondDelay, Object locker) {
            this.secondDelay = secondDelay;
            this.locker = locker;
        }

        @Override
        public synchronized void start() {
            System.out.println("second deley" + secondDelay);
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
