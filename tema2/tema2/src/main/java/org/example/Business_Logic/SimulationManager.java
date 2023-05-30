package org.example.Business_Logic;

import org.apache.commons.io.output.TeeOutputStream;
import org.example.Model.Server;
import org.example.Model.Task;
import org.example.GUI.SimulationFrame;
import java.io.*;
import java.util.*;


public class SimulationManager implements Runnable{
    private PrintStream print;
    private TeeOutputStream tee;
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServers;
    public int numberOfClients;
    public int minArrivalTime;
    public int maxArrivalTime;


    public synchronized void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public synchronized void setNumberOfServers(int numberOfServers) {
        this.numberOfServers = numberOfServers;
    }

    public synchronized void setMinProcessingTime(int minProcessingTime) {
        this.minProcessingTime = minProcessingTime;
    }

    public synchronized void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public synchronized void setMaxProcessingTime(int maxProcessingTime) {
        this.maxProcessingTime = maxProcessingTime;
    }

    public synchronized void setMinArrivalTime(int minArrivalTime) {
        this.minArrivalTime = minArrivalTime;
    }

    public synchronized void setMaxArrivalTime(int maxArrivalTime) {
        this.maxArrivalTime = maxArrivalTime;
    }

    public ConcreteStrategyTime.SelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }

    public ConcreteStrategyTime.SelectionPolicy selectionPolicy = ConcreteStrategyTime.SelectionPolicy.SHORTEST_TIME;
    private Scheduler sch;

    public List<Task> generateClients() {
        List<Task> clientsList = Collections.synchronizedList(new ArrayList<>(numberOfClients));

        Random random = new Random();
        for (int i = 0; i < numberOfClients; i++) {
            Task t = new Task(i, random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime, random.nextInt(maxProcessingTime - minProcessingTime + 1) + minProcessingTime);
            clientsList.add(i, t);
            clientsList.sort((Comparator.comparing(Task::getArrivalTime)));
        }
        return clientsList;
    }

    @Override
    public void run() {
        PrintStream console = System.out;
        try {
            print = new PrintStream(new FileOutputStream("print.txt"));
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        tee = new TeeOutputStream(console, print);
        System.setOut(new PrintStream(tee));

        sch = new Scheduler(numberOfServers, numberOfClients);
        ArrayList<Task> waitingClients = new ArrayList<>(generateClients());
        float averageServiceTime = 0;
        int averageServiceTimeCounter = 0;
        float averageWaitingTime = 0;
        int averageWaitingTimeCounter = 0;
        int nClientsInQueues = 1;
        int peakWaitingTime = 0;
        int peakHour = 0;

        int time = 0;
        while (time < timeLimit && nClientsInQueues != 0) {
            System.out.println("Waiting clients: ");
            for (Task task : waitingClients) {
                System.out.print("(" + task.getId() + "," + task.getArrivalTime() + "," + task.getServiceTime() + ")");
            }
            System.out.println();
            int i = 0;
            while (i == 0) {

                if (!waitingClients.isEmpty()) {
                    Task client_x = waitingClients.get(0);
                    if (client_x.getArrivalTime()==time) {
                        try {
                            sch.dispatchClient(sch.getServers(), client_x);
                            waitingClients.remove(client_x);
                            averageServiceTime = averageServiceTime + client_x.getServiceTime();
                            averageServiceTimeCounter++;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    // check if client arrives in the future, then wait until arrival time == current time
                    else if (client_x.getArrivalTime() > time) i = -1;
                }
                // if no more clients, exit the loop
                if (waitingClients.isEmpty()) i = -1;
            }
            System.out.println("Time is " + time);
            time++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < sch.getServers().size(); j++) {
                System.out.println("\nQueue " + j + " :");
                Server.printTasks(sch.getServers().get(j).getTaskQueue());
            }
            System.out.println();
        }
        print.close();
    }
    public static void main(String[] args){
        SimulationFrame SimFrame = new SimulationFrame();
        SimulationManager gen = new SimulationManager();
        Thread t = new Thread(gen);
        t.start();

    }

}