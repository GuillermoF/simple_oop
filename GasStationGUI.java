/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gasstationgui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Random;
import javax.swing.*;
/**
 *
 * @author guferrer
 */
public class GasStationGUI {
    private JFrame frame;
    private Panel panel;
    private Graphics g;
    public JTextArea info;
    private Timer timer;
    private GasStation GS;
    
    public static void main(String[] args) {
        GasStationGUI gui = new GasStationGUI();
        gui.buildGUI();
        gui.buildGasStation();
    }
    
    private void buildGasStation() {
        GS = new GasStation(info);
    }
    
    private void buildGUI() {
        frame = new JFrame("Gas Station Simulation");
        
        Font font = new Font("sanserif", Font.PLAIN, 12);
        info = new JTextArea(7, 100);
        info.setPreferredSize(new Dimension(400, 1000));
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setFont(font);
        
        JScrollPane scroller = new JScrollPane(info);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        JButton start = new JButton("Start");
        JButton stop = new JButton("Stop");
        JButton saveFile = new JButton("Save to File");
        
        start.setToolTipText("Start Simulation");
        stop.setToolTipText("Stop Simulation");
        saveFile.setToolTipText("Save transactions to a file.");

        panel = new Panel();
        panel.setSize(39,384);
        
        JPanel componentPanel = new JPanel();
        componentPanel.add(start);
        componentPanel.add(stop);
        componentPanel.add(saveFile);
        
        frame.add(BorderLayout.SOUTH, scroller);
        frame.add(BorderLayout.NORTH, componentPanel);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
        
        g = frame.getGraphics();

        start.addActionListener(new StartSimListener());
        stop.addActionListener(new StopSimListener());
        saveFile.addActionListener(new SaveToFileListener());
    }
    
    private boolean isRunning = false;
    
    public class StartSimListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isRunning) {
               isRunning = true;
               timer = new Timer(50, panel);
               timer.start();
            }
        }
    }
    
    public class StopSimListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
             if (isRunning) {
                 isRunning = false;
                 timer.stop();
             }
        }
    }
    
    public class SaveToFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isRunning) {
                isRunning = false;
                timer.stop();
            }                 
            JFileChooser dialog = new JFileChooser();
            dialog.showSaveDialog(frame);
            writeTransactions(dialog.getSelectedFile());
        }
    }
    
    private ArrayDeque<String> transactions;
    private void writeTransactions(File file) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (String s : transactions) {
                bw.write(s);
                bw.newLine();
            }
            bw.close();
        } 
        catch (Exception e) {
            System.out.println("Couldn't write to file.");
            System.out.println(e.getMessage());
        }
    }
    
    private int actions = 0;

    public class Panel extends JPanel implements ActionListener {
        private double[] inventories;
        private ArrayDeque<Client> clients;
        private GasPump[] pumps;
        private Graphics2D g2;
        private int x,y;
        @Override
        public void paintComponent(Graphics g) {
            g.clearRect(0, 63, 384, 213);
            int i;
            int height;
            for (i = 0; i < 4; i++) {
                g.setColor(Color.BLACK);
                x = 25 + 100 * i; y = 190;
                g.drawLine(x, y, x + 25, y);
                g.drawLine(x,y, x, y - 100);
                
                g2 = (Graphics2D)g;
                height = (int)inventories[i] / 5;
                g2.setColor(Color.GREEN);
                g2.fill3DRect(x + 3, y - height, 22, height, true);
                
                g.setColor(Color.BLACK);
                
                x = 96 * 2 - 80 + 30 * i; y = 230;
                g.drawOval(x, y, 20, 20);
                if (i >= clients.size())
                    g.setColor(Color.WHITE);
                g.fillOval(x, y, 20, 20);
                
                g.setColor(Color.BLACK);
                x = 32 + 100 * i; y = 200;
                g.drawOval(x, y, 10, 10);
                g.setColor(Color.WHITE);

                if (pumps[i].hasClient())
                    g.setColor(Color.BLACK);
                g.fillOval(x, y, 10, 10);
            }
            
            x = 96 * 2 - 80 + 30 * i; y = 230;
            g.setColor(Color.BLACK);
            g.drawOval(x, y, 20, 20);
            if (i >= clients.size())
                g.setColor(Color.WHITE);
            g.fillOval(x, y, 20, 20);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            actions++;
            if (actions % 4 == 0) {
                int chance = randChance();
                
                if (chance == 7) {
                    GS.generateClient();
                    GS.delegateClient();
                }
            }
            
            GS.run();

            inventories = GS.getInventories();
            clients = GS.getClientQ();
            pumps = GS.getGasPumps();
            
            transactions = GS.getTransactions();

            panel.paintComponent(g);
        }
    }
    
    private int randChance() {
        Random rand = new Random();
        int chance = rand.nextInt(5) + 3;
        return chance;
    }
}

