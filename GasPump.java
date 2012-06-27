
package gasstationgui;

import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Date;
import javax.swing.JTextArea;

public class GasPump {
    private Client client;
    private int number;
    private double gasInventory, totalFueled, paymentAmount;
    private boolean hasClient, hasEnoughGas, isFueling, isDone;
    private ArrayDeque<String> transactions;
    private JTextArea info;

    final static private double[] gasPrices = {3.50, 3.75, 4.05};
    
    final static private double flowRate = 0.17;     //0.17 gallons / time unit
    
    public GasPump(int number, double gasInventory, JTextArea info) {
        this.gasInventory = gasInventory;
        this.number = number;
        totalFueled = 0;
        hasClient = false;
        hasEnoughGas = true;
        isFueling = false;
        isDone = false;
        transactions = new ArrayDeque<String>();
        this.info = info;
    }
    
    public void doFueling() {
        gasInventory -= flowRate;
        totalFueled += flowRate;
        isFueling = true;
    }
    
    public boolean IsDone() {
        if (totalFueled >= client.getGallonsNeeded() || gasInventory == 0) {
            isDone = true;
            isFueling = false;
        }
        
        return isDone;
    }
    
    public boolean IsFueling() {
        return isFueling;
    }
    
    private Date d;

    public void acceptClientPayment() {
        int index = client.getFuelGrade();
        paymentAmount = gasPrices[index] * totalFueled;
        d = new Date();
        DecimalFormat format = new DecimalFormat("#.##");
        
        String update = "Client " + client.getNumber() + " with license " + client.getLicense() + 
                " has finished fueling at " + d.toString() + ". Amount paid is: $" + 
                format.format(paymentAmount) + 
                " for " + client.getGallonsNeeded() + " gallons.\n";
        transactions.add(update);
        info.append(update);
    }
    
    public void removeClient() {
        this.client = null;
        isDone = false;
        hasClient = false;
    }
    
    public double getGasInventory() {
        return gasInventory;
    }
    
    public void setClient(Client client) {
        this.client = client;
        hasClient = true;
        totalFueled = 0;
        String update = "Client " + client.getNumber() + " moves to pump " + number + ".\n";
        info.append(update);
    }
    
    public boolean hasClient() {
        return hasClient;
    }
    
    public Client getClient() {
        return this.client;
    }
    
    public boolean getHasEnoughGas() {
        if (gasInventory >= flowRate)
            hasEnoughGas = true;
        else
            hasEnoughGas = false;

        return hasEnoughGas;
    }

    public ArrayDeque<String> getTransactions() {
        return transactions;
    }
}
