package com.pi4j.jfx.util.mvc;

import java.util.concurrent.ConcurrentLinkedQueue;

class CommandQueue extends Thread {
        private final ConcurrentLinkedQueue<Command<?>> queue = new ConcurrentLinkedQueue<>();

        private boolean running = true;

        CommandQueue(){
            setDaemon(true);
        }

        public synchronized void run() {
            setName("PUI-Command-Queue");
            while (running) {
                Command<?> command = queue.poll();
                if (command != null) {
                    command.execute();
                    synchronized (command) {
                        command.notifyAll();
                    }
                } else {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace(); //Unlikely
                    }
                }
            }
        }

        synchronized void queueEvent(Command<?> command) {
            queue.add(command);
            notifyAll();
        }

        synchronized void shutdown() {
            running = false;
            notifyAll();
        }
    }
