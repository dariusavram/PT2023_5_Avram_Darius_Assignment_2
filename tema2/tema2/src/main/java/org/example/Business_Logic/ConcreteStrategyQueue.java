package org.example.Business_Logic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {

    public void addTask(List<Server> servers, Task t) throws InterruptedException{
        int smallestQueueIndex = 0;
        int smallestQueueLength = Integer.MAX_VALUE;

        for (int i = 0; i < servers.size(); i++) {
            Server currentQueue = servers.get(i);
            int currentQueueLength = currentQueue.getTaskQueue().size();

            if (currentQueueLength < smallestQueueLength) {
                smallestQueueIndex = i;
                smallestQueueLength = currentQueueLength;
            }
        }

        servers.get(smallestQueueIndex).addTask(t);
    }
}
