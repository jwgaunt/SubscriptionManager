/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subscriptionmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author jwgau
 */
public class Subscription implements Serializable {
    
    //Get date subscription activated
    private String subscriptionDate;
    
    //Subscription package information 'B' = Bronze, 'S' = Silver, 'G' = Gold
    private int subscriptionDuration;
    
    private char subscriptionTier;
    
    //Variable to assign appropriate discount to subscription.
    private String subscriptionDiscount;
    
    //Variable to assign either monthly or one off subscription payment 'O' = one off payment, 'M' = pay monthly
    private char subscriptionPaymentSchedule;
    
    //Variable to display total cost after discount
    private int subscriptionCostInPence;
    
    //Customer name variable
    private String subscriptionHolderName;

    public Subscription(String subscriptionDate, char subscriptionTier, int subscriptionDuration, String subscriptionDiscount, char subscriptionPaymentSchedule, int subscriptionCostInPence, String subscriptionHolderName) {
        this.subscriptionDate = subscriptionDate;
        this.subscriptionDuration = subscriptionDuration;
        this.subscriptionTier = subscriptionTier;
        this.subscriptionDiscount = subscriptionDiscount;
        this.subscriptionPaymentSchedule = subscriptionPaymentSchedule;
        this.subscriptionCostInPence = subscriptionCostInPence;
        this.subscriptionHolderName = subscriptionHolderName;
        
    }
    
    
    //Getter & setter methods
    
    public String getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(String subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public int getSubscriptionDuration() {
        return subscriptionDuration;
    }

    public void setSubscriptionDuration(int subscriptionDuration) {
        this.subscriptionDuration = subscriptionDuration;
    }

    public char getSubscriptionTier() {
        return subscriptionTier;
    }

    public void setSubscriptionTier(char subscriptionTier) {
        this.subscriptionTier = subscriptionTier;
    }

    public String getSubscriptionDiscount() {
        return subscriptionDiscount;
    }

    public void setSubscriptionDiscount(String subscriptionDiscount) {
        this.subscriptionDiscount = subscriptionDiscount;
    }

    public char getSubscriptionPaymentType() {
        return subscriptionPaymentSchedule;
    }

    public void setSubscriptionPaymentType(char subscriptionPaymentType) {
        this.subscriptionPaymentSchedule = subscriptionPaymentType;
    }

    public int subscriptionCostInPence() {
        return subscriptionCostInPence;
    }

    public void setSubscriptionPrice(int subscriptionCostInPence) {
        this.subscriptionCostInPence = subscriptionCostInPence;
    }

    public String getSubscriptionHolderName() {
        return subscriptionHolderName;
    }

    public void setSubscriptionHolderName(String subscriptionHolderName) {
        this.subscriptionHolderName = subscriptionHolderName;
    }

    @Override
    public String toString() {
        return subscriptionDate + "\t" + subscriptionDuration + "\t"
                + subscriptionTier + "\t" + subscriptionDiscount + "\t"
                + subscriptionPaymentSchedule + "\t" + subscriptionCostInPence
                + "\t" + subscriptionHolderName;
    }
    
    public void writeToFile(Subscription sub1) throws IOException{
        try{
            File subscriptionFile = new File("subscriptions.txt");
            if(!subscriptionFile.exists()){
                subscriptionFile.createNewFile();
            }
            try (FileWriter writeFile = new FileWriter(subscriptionFile, true)) {
                writeFile.append(sub1.toString() + "\n");
            }
        }catch(FileNotFoundException e){
            System.out.println("File not found");
        }catch(IOException e){
            System.out.println("Error writing to file");
        }
            
        }
    
    
    
    
}
