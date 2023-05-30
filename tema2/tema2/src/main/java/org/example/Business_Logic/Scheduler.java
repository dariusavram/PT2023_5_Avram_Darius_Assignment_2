package org.example.Business_Logic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers = new ArrayList<>();
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy = new ConcreteStrategyQueue();

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        for (int i = 0; i < maxNoServers; i++) {

            servers.add(new Server(maxTasksPerServer));
            Thread t = new Thread(servers.get(i));
            t.start();
        }
    }
    public int getMaxTasksPerServer() {
        return maxTasksPerServer;
    }
    public void changeStrategy(ConcreteStrategyTime.SelectionPolicy policy) {
        if (policy == ConcreteStrategyTime.SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
        if (policy == ConcreteStrategyTime.SelectionPolicy.SHORTEST_TIME){
                strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchClient(List<Server> servers, Task t) throws InterruptedException {
        strategy.addTask(servers, t);
    }

    public List<Server> getServers() {
        return this.servers;
    }
}
