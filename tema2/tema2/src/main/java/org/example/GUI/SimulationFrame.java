package org.example.GUI;

import org.example.Business_Logic.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationFrame {

    private final JFrame frame;
    private final JTextField numClientsTextField;
    private final JTextField numQueuesTextField;
    private final JTextField simulationIntervalTextField;
    private final JTextField minArrivalTimeTextField;
    private final JTextField maxArrivalTimeTextField;
    private final JTextField minServiceTimeTextField;
    private final JTextField maxServiceTimeTextField;

    public SimulationFrame() {
        frame = new JFrame("Queue Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(9, 2, 10, 10));

        JLabel titleLabel = new JLabel("Queue Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(titleLabel);
        frame.add(new JLabel());

        frame.add(new JLabel("Number of Clients (N):"));
        numClientsTextField = new JTextField(10);
        frame.add(numClientsTextField);

        frame.add(new JLabel("Number of Queues (Q):"));
        numQueuesTextField = new JTextField(10);
        frame.add(numQueuesTextField);

        frame.add(new JLabel("Simulation Interval (Max):"));
        simulationIntervalTextField = new JTextField(10);
        frame.add(simulationIntervalTextField);

        frame.add(new JLabel("Minimum Arrival Time (Min/Max):"));
        JPanel minArrivalTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        minArrivalTimeTextField = new JTextField(5);
        minArrivalTimePanel.add(minArrivalTimeTextField);
        minArrivalTimePanel.add(new JLabel("/"));
        maxArrivalTimeTextField = new JTextField(5);
        minArrivalTimePanel.add(maxArrivalTimeTextField);
        frame.add(minArrivalTimePanel);

        frame.add(new JLabel("Minimum Service Time (Min/Max):"));
        JPanel minServiceTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        minServiceTimeTextField = new JTextField(5);
        minServiceTimePanel.add(minServiceTimeTextField);
        minServiceTimePanel.add(new JLabel("/"));
        maxServiceTimeTextField = new JTextField(5);
        minServiceTimePanel.add(maxServiceTimeTextField);
        frame.add(minServiceTimePanel);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimulationManager gen = new SimulationManager();
                Thread t = new Thread(gen);
                t.start();

                int numClients = Integer.parseInt(numClientsTextField.getText());
                gen.setNumberOfClients(numClients);

                int numQueues = Integer.parseInt(numQueuesTextField.getText());
                gen.setNumberOfServers(numQueues);

                int simulationInterval = Integer.parseInt(simulationIntervalTextField.getText());
                gen.setTimeLimit(simulationInterval);

                int minArrivalTime = Integer.parseInt(minArrivalTimeTextField.getText());
                gen.setMinArrivalTime(minArrivalTime);

                int maxArrivalTime = Integer.parseInt(maxArrivalTimeTextField.getText());
                gen.setMaxArrivalTime(maxArrivalTime);

                int minServiceTime = Integer.parseInt(minServiceTimeTextField.getText());
                gen.setMinProcessingTime(minServiceTime);

                int maxServiceTime = Integer.parseInt(maxServiceTimeTextField.getText());
                gen.setMaxProcessingTime(maxServiceTime);

                JOptionPane.showMessageDialog(frame, "Input values submitted successfully!");
            }
        });
        frame.add(submitButton);
        frame.add(new JLabel());

        frame.setSize(400, 350);
        frame.setVisible(true);
    }
}
