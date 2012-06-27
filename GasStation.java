/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gasstationgui;

import java.util.ArrayDeque;
import javax.swing.JTextArea;


public class GasStation {
    private GasPump[] pumps = new GasPump[4];
    private double[] inventories = new double[4];
    private ArrayDeque<Client> clients;
    private int clientCount = 0;
    Client client = null;
    
    public GasStation(JTextArea info) {
        for (int i = 0; i < 4; i++) {
            pumps[i] = new GasPump(i+1, 500, info);
        }
        clients = new ArrayDeque<Client>();
        updateInventories();
    }
    
    private void updateInventories() {
        for (int i = 0; i < 4; i++) {
            inventories[i] = pumps[i].getGasInventory();
        }
    }
    
    public void generateClient() {
        if (clients.size() <= 4) {
            clientCount++;
            client = new Client(clientCount);
            clients.add(client);  
        } else {
            //add message update for client turned away
        } 
    }
    
    public void delegateClient() {
        Client c;
        for (int i = 0; i < pumps.length; i++) {
            if (!pumps[i].hasClient()) {
                c = clients.poll();
                pumps[i].setClient(c);
                break;
            }
        }
    }
    
    public void run() {
        for (int i = 0; i < 4; i++) {
            if (pumps[i].hasClient() && pumps[i].getHasEnoughGas()) {
                
                if (pumps[i].IsDone()) { 
                    pumps[i].acceptClientPayment(); 
                    pumps[i].removeClient();
                } 
                else
                    pumps[i].doFueling();
            }
        }
        
        if (clients.size() >= 1)
            delegateClient();
        updateInventories();
    }
    
    public int getClientCount() {
        return clientCount;
    }
    
    public double[] getInventories() {
        return inventories;
    }
    
    public ArrayDeque<Client> getClientQ() {
        return clients;
    }
    
    public GasPump[] getGasPumps() {
        return pumps;
    }
    
    public ArrayDeque<String> getTransactions() {
        ArrayDeque<String> transactions = new ArrayDeque<String>();
        for (int i = 0; i < 4; i++) {
            if (pumps[i].getTransactions() != null) {
                for(String s : pumps[i].getTransactions()) {
                    transactions.push(s);
                }
            }
        }
        
        return transactions;
    }
}
