package com.pi4j.mvc.util.mvcbase;

import java.time.Duration;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A device where tasks can be submitted for execution.
 *
 * Execution is asynchronous - possibly in a different thread - but the sequence is kept stable, such that
 * for all tasks A and B: if B is submitted after A, B will only be executed after A is finished.
 *
 * New tasks can be submitted while tasks are running.
 *
 * Task submission itself is supposed to be thread-confined,
 * i.e. creation of the ConcurrentTaskQueue and task submission is expected to run in the same thread,
 * most likely the JavaFX UI Application Thread.
 *
 * @author Dierk Koenig
 */

public final class ConcurrentTaskQueue<R> {
    private final ExecutorService executor;
    private final ConcurrentLinkedQueue<Task<R>> buffer;
    private final Duration maxToDoTime;

    private boolean running = false; // for non-thread-confined submissions, we might need an AtomicBoolean

    public ConcurrentTaskQueue(){
        this(Duration.ofSeconds(5));
    }

    public ConcurrentTaskQueue(Duration maxToDoTime) {
        this.maxToDoTime = maxToDoTime;
        this.executor    = Executors.newFixedThreadPool(1);  // use 2 for overlapping onDone with next to-do
        this.buffer      = new ConcurrentLinkedQueue<>();
    }

    public void shutdown(){
        executor.shutdown();
    }

    public void submit(Supplier<R> todo){
        submit(todo, r -> {});
    }

    public void submit(Supplier<R> todo, Consumer<R> onDone){
        buffer.add(new Task<>(todo, onDone));
        execute();
    }

    private void execute() {
        if (running) {
            return;
        }

        final Task<R> task = buffer.poll();

        if(task == null){
            return;
        }

        running = true;

        final Future<R> todoFuture = executor.submit(task.todo::get);

        Runnable onDoneRunnable = () -> {
            try {
                final R r = todoFuture.get(maxToDoTime.getSeconds(), TimeUnit.SECONDS);
                task.onDone.accept(r);
            } catch (Exception e) {
                e.printStackTrace(); // todo: think about better exception handling
            } finally {
                running = false;
                execute();
            }
        };
        executor.submit(onDoneRunnable);
    }

    private static class Task<T> {
        private final Supplier<T> todo;    // the return type of to-do ..
        private final Consumer<T> onDone;  // .. must match the input type of onDone

        public Task(Supplier<T> todo, Consumer<T> onDone) {
            this.todo = todo;
            this.onDone = onDone;
        }
    }
}

