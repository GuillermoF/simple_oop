/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gasstationgui;

import java.util.Random;

/**
 *
 * @author OhBoy
 */
public class Client {
    private String license;
    private double gallonsNeeded;
    private int number;
    private int fuelGrade;
    
    public Client(int number) {
        this.number = number;
        
        Random rand = new Random();
        license = "";
        for (int i = 1; i <= 6; i++)
        {
            int code = rand.nextInt(43) + 48;
            if ((code <= 64) && (code >= 58)) {
                i--;
                continue;
            }

            license += ((char)(code));
        }
        
        gallonsNeeded = rand.nextInt(30) + 1;
        fuelGrade = rand.nextInt(3);
    }
    
    public double getGallonsNeeded() {
        return gallonsNeeded;
    }
    
    public String getLicense() {
        return license;
    }
    
    public int getNumber() {
        return number;
    }
    
    public int getFuelGrade() {
        return fuelGrade;
    }
}
