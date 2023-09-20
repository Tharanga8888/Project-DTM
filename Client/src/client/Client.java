package client;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.DefaultCaret;

public class Client extends JFrame {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private JButton connectButton;
    private JButton startButton;
    private JTextArea resultTextArea;
    private JProgressBar progressBar; // Added progress bar
    private boolean isConnected = false;
    private JLabel timerLabel;  // Add a label for the timer
    private long startTime;     // Variable to store the start time

    public Client() {
        setTitle("Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 500);
        setLayout(new BorderLayout());

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        add(scrollPane, BorderLayout.CENTER);

        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isConnected) {
                    connectToServer();
                } else {
                    disconnectFromServer();
                }
            }
        });
      
        startButton = new JButton("Start");
        startButton.setEnabled(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                resultTextArea.append("Asking for the range\n");
                out.println("Asking for the range");

                // Start a SwingWorker to perform the time-consuming operations
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        findAndSendNumbers(); // Network operations and calculations
                        return null;
                    }

                    @Override
                    protected void done() {
                        // This is executed on the EDT when doInBackground is complete
                        startButton.setEnabled(true); // Re-enable the button
                        progressBar.setValue(100); // Set progress bar to 100% when done
                    }
                }.execute();
            }
        });

        // Create a label for the timer
        timerLabel = new JLabel("Time taken to the task: 0 seconds");
        add(timerLabel, BorderLayout.NORTH);
        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true); // Show percentage text on the progress bar
        progressBar.setMaximum(100000); // Set the maximum value of the progress bar to 100

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(connectButton);
        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Create a panel to hold the timer label
        JPanel timerPanel = new JPanel();
        timerPanel.add(timerLabel);

        // Add the buttons JPanel to the PAGE_START of the main content
        add(buttonPanel, BorderLayout.PAGE_START);

        // Add the progress bar to the bottom of the window
        add(progressBar, BorderLayout.PAGE_END);

        setVisible(true);
        
        // Create a DefaultCaret for the resultTextArea
        DefaultCaret caret = (DefaultCaret) resultTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    private void connectToServer() {
        try {
            clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            connectButton.setText("Disconnect");
            startButton.setEnabled(true);
            out.println("CONNECT");
            resultTextArea.append("Connected to the server.\n");
            isConnected = true;

            // Start a separate thread to detect server disconnections
            startServerDisconnectionDetector();
        } catch (IOException e) {
            // Handle the connection error
            String errorMessage = "Error: Could not connect to the server. Make sure the server is running.";
            JOptionPane.showMessageDialog(this, errorMessage, "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void disconnectFromServer() {
        try {
            connectButton.setText("Connect");
            startButton.setEnabled(false);
            out.println("DISCONNECT");
            resultTextArea.append("Disconnected from the server.\n");
            isConnected = false;
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                    in.close();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findAndSendNumbers() {
        try {
            String range = in.readLine();
            resultTextArea.append("Received range from server: " + range + "\n");

            // Find and send prime numbers
            List<Long> primeNumbers = findPrimeNumbers(range);
            out.println("Prime Numbers: " + primeNumbers.toString());

            // Notify the server that the task is completed
            out.println("TASK_COMPLETE");
            resultTextArea.append("Task completed. Sent tasks to the server.\n");

            // Wait for confirmation from the server
            String confirmation = in.readLine();
            if (confirmation != null && confirmation.startsWith("CONFIRMATION")) {
                resultTextArea.append("CONFIRMATION: Results received successfully.\n");
            } else {
                resultTextArea.append("Confirmation not received from the server.\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startServerDisconnectionDetector() {
        Thread detectorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isConnected) {
                        // Send a ping to the server
                        out.println("PING");

                        // Wait for a response
                        String response = in.readLine();

                        if (response == null || !response.equals("PONG")) {
                            handleServerDisconnection();
                            break;
                        }

                        Thread.sleep(1000); // Adjust the sleep time as needed
                    }
                } catch (IOException | InterruptedException e) {
                    handleServerDisconnection();
                }
            }
        });
        detectorThread.setDaemon(true);
        detectorThread.start();
    }

    private void handleServerDisconnection() {
        resultTextArea.append("Disconnected from the server.\n");
        connectButton.setText("Connect");
        startButton.setEnabled(false);
        isConnected = false;
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Long> findPrimeNumbers(String rangeStr) {
        // Simulate a long-running operation for demonstration purposes
        List<Long> primeNumbers = new ArrayList<>();
        String[] rangeParts = rangeStr.split("-");
        long start = Long.parseLong(rangeParts[0]);
        long end = Long.parseLong(rangeParts[1]);

        for (long i = start; i <= end; i++) {
            if (isPrime(i)) {
                primeNumbers.add(i);
                resultTextArea.append(i+"\n");
            }
            // Update the progress bar based on the current progress
            int progress = (int) ((i - start) * 100000 / (end - start));
            progressBar.setValue(progress);
        }

        return primeNumbers;
    }
    
    private boolean isPrime(long num) {
        if (num <= 1) {
            return false;
        }
        for (long i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client();
            }
        });
    }
}
