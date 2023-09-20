package dtmclient;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.DefaultCaret;

public class DTMClient extends javax.swing.JFrame {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Timer taskTimer;
    private long taskStartTime;
    private boolean isConnected = false;
    
    public DTMClient() {
        initComponents();
        taskTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - taskStartTime;
                updateTimerLabel(elapsedTime);
            }
        });
    }
    
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DTMClient().setVisible(true);
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        closeButton = new rojerusan.RSMaterialButtonCircle();
        connectButton = new rojerusan.RSMaterialButtonCircle();
        startButton = new rojerusan.RSMaterialButtonCircle();
        progressBar = new javax.swing.JProgressBar();
        timerLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        resultTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(250, 500));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        closeButton.setBackground(new java.awt.Color(102, 0, 153));
        closeButton.setText("X");
        closeButton.setFont(new java.awt.Font("Arial Black", 1, 17)); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        jPanel1.add(closeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 40, 40));

        connectButton.setBackground(new java.awt.Color(102, 0, 153));
        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });
        jPanel1.add(connectButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 150, 150));

        startButton.setBackground(new java.awt.Color(102, 0, 153));
        startButton.setText("Start");
        startButton.setEnabled(false);
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });
        jPanel1.add(startButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 150, 150));

        progressBar.setBackground(new java.awt.Color(102, 0, 153));
        progressBar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        progressBar.setForeground(new java.awt.Color(204, 0, 204));
        progressBar.setStringPainted(true);
        jPanel1.add(progressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 230, 25));

        timerLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timerLabel.setText("00:00");
        jPanel1.add(timerLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 420, 100, 30));

        jLabel1.setFont(new java.awt.Font("Rosewood Std Regular", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Prime");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 160, 70));

        jLabel2.setFont(new java.awt.Font("Stencil", 1, 18)); // NOI18N
        jLabel2.setText("+___+Plus");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 100, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, 250, 500));

        jPanel2.setBackground(new java.awt.Color(102, 0, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setFocusTraversalPolicyProvider(true);

        resultTextArea.setEditable(false);
        resultTextArea.setColumns(20);
        resultTextArea.setRows(5);
        resultTextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        resultTextArea.setSelectionColor(new java.awt.Color(102, 0, 153));
        jScrollPane2.setViewportView(resultTextArea);
		
        DefaultCaret caret = (DefaultCaret) resultTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 530, 480));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 550, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        System.exit(1);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        if (!isConnected) {
            connectToServer();
        } else {
            disconnectFromServer();
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
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
                // Stop the timer when the progress bar reaches 100%
                if (progressBar.getValue() == 100) {
                    taskTimer.stop();
                    
                    // Calculate and format the time taken
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - taskStartTime;
                    String timeTaken = formatElapsedTime(elapsedTime);
                    
                    // Send time taken to the server
                    out.println("Time taken for task completion is " + timeTaken);
                    resultTextArea.append("Time taken for task completion is " + timeTaken + "\n");
                        }
                    }
        }.execute();
        taskStartTime = System.currentTimeMillis();
        taskTimer.start();
    }//GEN-LAST:event_startButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSMaterialButtonCircle closeButton;
    private rojerusan.RSMaterialButtonCircle connectButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextArea resultTextArea;
    private rojerusan.RSMaterialButtonCircle startButton;
    private javax.swing.JLabel timerLabel;
    // End of variables declaration//GEN-END:variables

    

    
    
    //-----------------------------Building a Task Timer-----------------------------
    
    private void updateTimerLabel(long elapsedTime) {
        // Convert elapsed time to HH:mm:ss format
        long hours = (elapsedTime / 3600000) % 24;
        long minutes = (elapsedTime / 60000) % 60;
        long seconds = (elapsedTime / 1000) % 60;
        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerLabel.setText(timeString);
    }
    private String formatElapsedTime(long elapsedTime) {
        long hours = (elapsedTime / 3600000) % 24;
        long minutes = (elapsedTime / 60000) % 60;
        long seconds = (elapsedTime / 1000) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }    

    
    
    //---------------------------------Connectivity---------------------------------
    
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
        
        // Stop the timer when the task is completed
        taskTimer.stop(); 
        // Reset the timer label to "00:00"
        timerLabel.setText("00:00");  
        
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
  
    
       
    //-----------------------------Finding Prime numbers-----------------------------
    
    private void findAndSendNumbers() {
        try {
            String range = in.readLine();
            resultTextArea.append("Received range from server: " + range + "\n");

            // Calculate the total number of iterations (for the progress bar)
            String[] rangeParts = range.split("-");
            long start = Long.parseLong(rangeParts[0]);
            long end = Long.parseLong(rangeParts[1]);
            long totalIterations = end - start + 1;

            // Find and send prime numbers
            List<Long> primeNumbers = findPrimeNumbers(range, totalIterations);
            out.println("Prime Numbers: " + primeNumbers.toString());

            // Notify the server that the task is completed
            out.println("TASK_COMPLETE");
            // Check if the stream is still open (not closed)
            if (!clientSocket.isClosed()) {
                resultTextArea.append("Task completed. Sent results to the server.\n");
            }else{
                resultTextArea.append("EEROR : Task completed. But fail to sent results to the server.\n");
            }

            String confirmation = in.readLine();
            if (confirmation != null && confirmation.startsWith("CONFIRMATION")) {
                resultTextArea.append("CONFIRMATION: Results received successfully.\n");
             }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    private List<Long> findPrimeNumbers(String rangeStr, long totalIterations) {
        // Simulate a long-running operation for demonstration purposes
        List<Long> primeNumbers = new ArrayList<>();
        String[] rangeParts = rangeStr.split("-");
        long start = Long.parseLong(rangeParts[0]);
        long end = Long.parseLong(rangeParts[1]);

        for (long i = start; i <= end; i++) {
            if (isPrime(i)) {
                primeNumbers.add(i);
                resultTextArea.append(i + "\n");
            }
            // Update the progress bar based on the current progress
            int progress = (int) ((i - start) * 100 / totalIterations);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    progressBar.setValue(progress);
                }
            });
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
    
}
