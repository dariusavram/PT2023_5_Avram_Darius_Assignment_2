package org.example.Model;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ArrayBlockingQueue;

public class Server implements Runnable {
    private final AtomicInteger waitingTime;
    private final int maxTasksPerServer;
    private BlockingQueue<Task> taskQueue;

    public Server(int maxTasksPerServer) {
        taskQueue = new ArrayBlockingQueue<>(maxTasksPerServer);
        this.waitingTime = new AtomicInteger(0);
        this.maxTasksPerServer = maxTasksPerServer;
    }

    public BlockingQueue<Task> getTaskQueue() {
        return this.taskQueue;
    }

    public void addTask(Task newTask) throws InterruptedException {
        for (int i = 0; i < newTask.getServiceTime(); i++) {
            waitingTime.getAndIncrement();
        }
        if (taskQueue.size() == 0) {
            newTask.setServiceTime(newTask.getServiceTime() + 1);
        }
        taskQueue.put(newTask);
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (taskQueue.size() > 0) {
                Task currentTask = taskQueue.peek();
                currentTask.setServiceTime(currentTask.getServiceTime() - 1);
                waitingTime.decrementAndGet();
                if (currentTask.getServiceTime() == 0) {
                    taskQueue.remove();
                }
            }
        }
    }

    public static void printTasks(BlockingQueue<Task> taskQueue) {
        for (Task task : taskQueue) {
            System.out.print("(Task ID: " + task.getId() + "\nArrival Time: "+ task.getArrivalTime() +"\nService Time: " + task.getServiceTime());
        }
    }

    public AtomicInteger getWaitingTime() {
        return waitingTime;
    }
}
