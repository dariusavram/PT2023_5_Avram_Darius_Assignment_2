package org.example.Business_Logic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    public void addTask(List<Server> servers, Task t) throws InterruptedException {
        int shortestQueueIndex = -1;
        int shortestQueueServiceTime = Integer.MAX_VALUE;

        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);
            int currentServiceTime = server.getWaitingTime().intValue();

            if (currentServiceTime < shortestQueueServiceTime) {
                shortestQueueIndex = i;
                shortestQueueServiceTime = currentServiceTime;
            }
        }
        if (shortestQueueIndex != -1) {
            servers.get(shortestQueueIndex).addTask(t);
        }

    }

    public enum SelectionPolicy {
        SHORTEST_QUEUE, SHORTEST_TIME
    }
}
