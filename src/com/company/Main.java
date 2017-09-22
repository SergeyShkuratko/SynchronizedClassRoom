package com.company;

import java.util.concurrent.ForkJoinPool;

public class Main {
    volatile static int seconds = 0;

    public static void main(String[] args) {
        Object locker = new Object();
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

        new Messager(7, locker).start();
        new Messager(5, locker).start();

        //messager1.run();
        //messager2.run();
//        new Thread(() -> {
//            boolean printed = false;
//            while (true) {
//                if (seconds % 5 != 0) {
//                    synchronized (locker) {
//                        try {
//                            printed = false;
//                            locker.wait();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } else {
//                    if (!printed) {
//                        System.out.println("Прошло 5 секунд");
//                        printed = true;
//                    }
//                }
//            }
//        }).start();
    }

    static class Messager extends Thread {
        int secondDelay;
        Object locker;

        public Messager(int secondDelay, Object locker) {
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
