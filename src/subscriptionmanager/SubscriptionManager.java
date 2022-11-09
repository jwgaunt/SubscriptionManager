/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subscriptionmanager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Calendar; 
/**
 *
 * @author JOE GAUNT
 */
public class SubscriptionManager {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        String currentDate = DateHelper.getDate();
        System.out.printf("The current date is %s \n", currentDate);
        menuSystem();    
        }

    //below is the method to display main menu and take user input,
    //validate and call requested destination logic
    
    public static void menuSystem() throws IOException{
        Scanner scan = new Scanner(System.in);
        int choice;
        do{
            System.out.println("\nPLEASE ENSURE YOU ARE ENTERING AN"
                    + " APPROPRIATE INTEGER (0-4) TO PROCEED."
                + "\n\n1. Enter new Subscription: "
                + "\n2. Display Summary of subscriptions: "
                + "\n3. Display summary of subscriptions from a"
                    + " specific month: "
                + "\n4. Find and display subscriptions: "
                + "\n0. Exit");
            
            while(!scan.hasNextInt()){
                System.out.println("\nNON INTEGER INPUT DETECTED - TRY AGAIN: "
                + "\n\nPLEASE ENSURE YOU ARE ENTERING AN APPROPRIATE INTEGER"
                        + " (0-4) TO PROCEED."
                + "\n\n1. Enter new Subscription: "
                + "\n2. Display Summary of subscriptions: "
                + "\n3. Display summary of subscriptions from a"
                        + " specific month: "
                + "\n4. Find and display subscriptions: "
                + "\n0. Exit");
                scan.next();
            }
          choice = scan.nextInt();
        } while(choice <0 || choice >=5);
        switch (choice) {
            case 1:
                
                System.out.println(" ");
                String date = getDate();
                System.out.println(" ");
                
                int monthDuration = getMonthDuration();
                System.out.println(" ");  
                
                char subTier = getTier();
                System.out.println(" ");
                
                String discountCode = getDiscountCode();
                System.out.println(" ");
                
                int discountPercentage = getDiscountAmount(discountCode);   
                System.out.println(" ");
                
                char paymentSchedule = getPaymentSchedule();
                System.out.println(" ");
                
                //calculatedSubscription cost returns a float value,
                //this is the only way I can get the currrency
                //formatting to work.
                float calculatedSubscriptionCost = 
                        calculateSubscriptionCost(monthDuration, subTier,
                                discountPercentage);
                System.out.println(" ");
                
                String custName = getCustName();
                System.out.println(" ");   
                //below calling method to convert calculated float value
                //into int for use in class object
                
                String formattedName = manipulateName(custName);
                int subscriptionCostInPence = 
                        getSubscriptionCostInPence(calculatedSubscriptionCost);
                System.out.println(" ");
                
                Subscription sub1 = new Subscription(date, subTier,
                        monthDuration, discountCode, paymentSchedule,
                        subscriptionCostInPence, formattedName);
                
                printDetails(date, subTier, monthDuration, discountCode,
                        paymentSchedule, calculatedSubscriptionCost,
                        formattedName);
                
                sub1.writeToFile(sub1);
                menuSystem();
                break;
 
            case 2:
                readFromFile();
                menuSystem();
                break;
                
            case 3:
                informationByMonth();
                menuSystem();
                break;
 
            case 4:
                findAndDisplay();
                menuSystem();
                break;
                
            case 0:
                System.out.println("PROGRAM TERMINATING... GOODBYE");
                break;
        }
    
    }
    
    //Below method will grab current date and return value
    
    public static String getDate()
    {
        String date;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        date = sdf.format(cal.getTime());
        return date;
    }

    //Below method will ask for new subscription's tier, validating the input
    //before returning the value
    
     public static char getTier(){
         Scanner scan = new Scanner(System.in);
         char subTier;
         do
         {
             System.out.println("Enter subscription tier, 'B, S, G");
             System.out.println("Please ensure you are only entering a single"
                     + " character, 'B', 'S', 'G'");
             subTier = scan.next().charAt(0);
         }while(!Character.toString(subTier).matches("^[G,S,B,g,s,b]*$"));
         subTier = Character.toUpperCase(subTier);  
         System.out.println("Your chosen tier was: "+subTier);         
        return subTier;    
         }
     
     //Below method will ask for customer name, validating input to ensure
     //a string was inputted
     
     public static String getCustName(){
         int doing = 0;
         String choice;
         Scanner scan = new Scanner(System.in);
         String custName;
         do
         {
             System.out.println("Enter full customer name, please ensure only "
                     + "alphabetical characters are entered: ");
             custName = scan.nextLine();
         }while(custName.matches(".*[0-9,`~!@#$%^&*()\\\\-_=+\\\\\\\\|\\\\[{\\"
                 + "\\]};:'\\\",<.>/?].*"));
         
         //Below I am checking the user has entered the correct name, giving an
         //oppurtunity to restart the loop if needed.
         
         while(doing == 0){
             System.out.println("Your chosen name was: "+custName+", was this"
                     + " correct? 1 to confirm or any other key to try again.");
             choice = scan.nextLine();
             if(choice.equals("1")){
                 System.out.println("Name confirmed.");
                 doing = 1;
             }
             else{
                 System.out.println("User rejected name.");
                 getCustName();
             }
             
         }
        return custName;    
         
         }
     //Below method will ask for duration of subscription in months,
     //and validate the input
     
     public static int getMonthDuration(){
         Scanner scan = new Scanner(System.in);
         int monthDuration;
         do
         {
             System.out.println("Enter length of subscription in months,"
                     + " valid inputs are '1', '3', '6', '12'");
             while(!scan.hasNextInt()){
                System.out.println("\nNON INTEGER INPUT DETECTED - TRY AGAIN:"
                        + " \nEnter length of subscription in months,"
                        + " 1, 3, 6, 12");
                scan.next();}
             monthDuration = scan.nextInt();
         }while((monthDuration != 1) && (monthDuration != 3) && 
                 (monthDuration != 6) && (monthDuration != 12 ));
         System.out.println("Your chosen subscription length in months was: "
                 +monthDuration);
        return monthDuration;    
         }     
     
     //Below method will ask for discount code, validating input
     //to ensure a string was inputted
     
     public static String getDiscountCode(){
          Scanner scan = new Scanner(System.in);
          String discountCode;
          char ch1;
          char ch2;
          char ch3;
          char ch4;
          char ch5;
          char ch6;

          do
          {
              System.out.println("Enter discount code of "
                      + "format XX11X1 (x representing letters"
                      + " & 1 representing numbers) or enter - if n/a: ");
              discountCode = scan.nextLine();
              if(discountCode.equals("-")){
                  System.out.println("Your chosen discount code was: - ");
                  return "-";
          }
             while(discountCode.length() != 6){
                  System.out.println("Invalid length, discount code should "
                          + "be in the format XX11X1 (x representing letters"
                          + " & 1 representing numbers) or - for no discount");
                  discountCode = scan.nextLine();
                  if(discountCode.equals("-")){
                      System.out.println("Your chosen discount code was: - ");
                      return "-";
                  }
              }

              ch1 = discountCode.charAt(0);
              ch2 = discountCode.charAt(1);
              ch3 = discountCode.charAt(2);
              ch4 = discountCode.charAt(3);
              ch5 = discountCode.charAt(4);
              ch6 = discountCode.charAt(5);

          }while(!Character.isAlphabetic(ch1)
              || !Character.isAlphabetic(ch2)
              || !Character.isDigit(ch3)     
              || !Character.isDigit(ch4)    
              || !Character.isAlphabetic(ch5)     
              || !Character.isDigit(ch6));
     
          System.out.println("Your chosen discount code was: "+discountCode);
       
         return discountCode;
     } 
     
     public static int getDiscountAmount(String discountCode){
         if(discountCode.equals("-")){
             return 0;
         }
         char ch6;
         int discountPercentage;
         ch6 = discountCode.charAt(5);
         discountPercentage = Integer.parseInt(String.valueOf(ch6));
         
        return discountPercentage;
         
     }
     
     //Below method will ask for payment method, which can be M for monthly,
     //or O for one off
     
      public static char getPaymentSchedule(){
         Scanner scan = new Scanner(System.in);
         char paymentSchedule;
         do
         {
             System.out.println("Enter payment schedule 'O' for one off,"
                     + " or 'M for monthly: ");
             paymentSchedule = scan.next().charAt(0);
         }while(!Character.toString(paymentSchedule).matches("^[O,M,o,m]*$"));
         paymentSchedule = Character.toUpperCase(paymentSchedule);
         System.out.println("Your chosen payment schedule is: "
                 +paymentSchedule);
         paymentSchedule = Character.toUpperCase(paymentSchedule);
        return paymentSchedule;    
         }
      
      //Below method will take subscription tier and length, calculating total 
      //cost - note to self also do discount logic here once validation working
       
     public static float calculateSubscriptionCost(int monthDuration,
             char subTier, int discountPercentage){
         
         float calculatedSubscriptionCost = 0;
         int subscriptionCostPreDiscount = 0;
         float finalDiscount;
         int monthlySubCost = 0;

         //below switch statemtents apply required monthly subscription cost
         //based on subscription tier and duration
         
         if(monthDuration == 1){
             switch (subTier) {
                 case 'B':
                     monthlySubCost = 600;
                     break;             
                 case 'S':
                     monthlySubCost = 800;
                     break;                  
                 case 'G':
                     monthlySubCost = 999;
                     break;                 
                 default:
                     break;
             }
         }
         
         else if(monthDuration == 3){
             switch (subTier) {
                 case 'B':
                     monthlySubCost = 500;
                     break;                     
                 case 'S':
                     monthlySubCost = 700;
                     break;                 
                 case 'G':
                     monthlySubCost = 899;
                     break;                
                 default:
                     break;
             }
         }     
         
         else if(monthDuration == 6){
             switch (subTier) {
                 case 'B':
                     monthlySubCost = 400;
                     break;        
                 case 'S':
                     monthlySubCost = 600;
                     break;     
                 case 'G':
                     monthlySubCost = 799;
                     break;          
                 default:
                     break;
             }
         } 
         
         else if(monthDuration == 12){
             switch (subTier) {
                 case 'B':
                     monthlySubCost = 300;
                     break;          
                 case 'S':
                     monthlySubCost = 500;
                     break;                  
                 case 'G':
                     monthlySubCost = 699;
                     break;                  
                 default:
                     break;
             }
         }
         //The first equation will take discountPercentage value, manipulating 
         //it to take the required discount from calculatedSubscriptionCost.
         //For example - discountPercentage = 7... (100-7 = 93) / 100
         //so finalDiscount = 0.93, a discount of 7% when 
         //calculatedSubscriptionCost *discountPercentage
         
         subscriptionCostPreDiscount = (monthlySubCost * monthDuration);
         
         finalDiscount = (100f - discountPercentage) / 100f;
            
         calculatedSubscriptionCost = (monthlySubCost * monthDuration) * 
                 finalDiscount;
        
         System.out.printf("Cost for subscription pre discount is: "+"£%.2f ", 
                 (float)subscriptionCostPreDiscount / 100);
         System.out.println("");
         System.out.printf("Cost for subscription after discount is: "+"£%.2f ",
                 (calculatedSubscriptionCost / 100));
        return calculatedSubscriptionCost;    
         }
     
     //Below method converts float value calculatedSubscriptionCost to integer
     //ready for addition to class object.
     
     public static int getSubscriptionCostInPence
        (float calculatedSubscriptionCost){
        int subscriptionCostInPence;
        subscriptionCostInPence = (int)calculatedSubscriptionCost;
        return subscriptionCostInPence;          
     }
     
     //Below method prints new subscription details after creating 
     //subscription object
     
     public static void printDetails(String date, char subTier,
             int monthDuration, String discountCode, char paymentSchedule,
             float calculatedSubscriptionCost, String formattedName){
         String tierToString = null;
         String scheduleToString = null;
         String durationToString = null;
         String subscriptionString = null;
         String noDiscount = "-";
         String localDiscountCode = discountCode;
         if(discountCode == null){
            localDiscountCode = noDiscount;
         }

        //Below I am using switch statements to take the raw values and convert 
        //them to appropriate values for output
        
         
         switch (paymentSchedule){
             case 'M':
                 scheduleToString = "Monthly";
                 subscriptionString = "Monthly Subscription: ";
                 break;
             case 'O':
                 scheduleToString = "One off"; 
                 subscriptionString = "One-off Subscription: ";
                 break;

         }
         switch (subTier){
             case 'B':
                 tierToString = "Bronze";
                 break;
             case 'S':
                 tierToString = "Silver";
                 break;
             case 'G':
                 tierToString = "Gold"; 
                 break;
         }
         
         switch (monthDuration){
             case 1:
                 durationToString = "One";
                 break;
             case 3:
                 durationToString = "Three";
                 break;
             case 6:
                 durationToString = "Six"; 
                 break;
             case 12:
                 durationToString = "Twelve"; 
                 break;                 
         }         
         
         System.out.println("+================================================="
                 + "=============+\n" +
                            "                                                  "
                 + "              \n" +
                            "                    Customer: "+formattedName+"  "
                                    + "                                     \n"
                                    +
                            "                                                  "
                                    + "              \n" +
                            "   Date: "+date+"             Discount Code: "
                 +localDiscountCode+"             \n" +
                            "   Package: "+tierToString+"              "
                                    + "Duration: "+durationToString+"          "
                                            + "     \n" +
                            "   Terms: "+scheduleToString+"                    "
                                    + "                                 \n" +
                            "                                                  "
                                    + "              \n" +
                            "           ");System.out.printf("                 "
                                    + ""+subscriptionString+"£%.2f ", 
                                    (calculatedSubscriptionCost / 100));
                            System.out.println(" \n" +
                            "                                                  "
                                    + "              \n" +
                            "+================================================="
                                    + "=============+");
                            
     }
     
     //Below method will take custName variable and manipulate to the
     //correct format, for example jOE gAUnT - J Gaunt
     
     public static String manipulateName(String custName){
         
         String lastName = custName.substring(custName.indexOf(' ')+1);
         char lastNameInitial;
         char firstInitial;
         String restOfLastName = lastName.substring(1);
         restOfLastName = (restOfLastName.toLowerCase());
         firstInitial = custName.charAt(0);
         lastNameInitial = lastName.charAt(0);
         firstInitial = Character.toUpperCase(firstInitial);
         lastNameInitial = Character.toUpperCase(lastNameInitial);
         lastName = lastNameInitial + restOfLastName;
         String formattedName = firstInitial + " " + lastName;
         
        return formattedName;
     }
     
     public static void readFromFile(){
         Scanner scan = new Scanner(System.in);
         String choice = null;
         double numberOfSubs = 0;
         int accumulatedSubCost = 0;
         int averageSubCost = 0;
         char userChoice;
        //Below I am setting up count variables to count the instances
        //required to get the data needed
         double bCount = 0;
         double sCount = 0;
         double gCount = 0;
         
         int janCount = 0;
         int febCount = 0;
         int marCount = 0;
         int aprCount = 0;
         int mayCount = 0;
         int junCount = 0;
         int julCount = 0;
         int augCount = 0;
         int sepCount = 0;
         int octCount = 0;
         int novCount = 0;
         int decCount = 0;
         int averageSub = 0;
         
        //validating user choice for which file to open
         
         do
         {
             System.out.println("Enter requested file to open, C for current "
                     + "or S for sample: ");
             userChoice = scan.next().charAt(0);
         }while(!Character.toString(userChoice).matches("^[S,C,s,c]*$"));
         System.out.println("Input accepted.");
         userChoice = Character.toUpperCase(userChoice);

         if(userChoice == 'S'){
             choice = "sample.txt";
         }
         else{
             choice = "current.txt";
         }
        
        //below you will see several try catch blocks, this is so I can
        //open the required file, with the while sections containing the
        //required logic
         
        Scanner input = null;  	// this is to keep the compiler happy
        // as the object initialisation is in a separate block					   						   
        try {
            
            input = new Scanner(new File(choice));
            
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            System.exit(1);
        }

        while(input.hasNextLine()) {
            input.nextLine();
            numberOfSubs++;        
        }
        
        				   						   
        try {
            
            input = new Scanner(new File(choice));
            
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            System.exit(1);
        }
        
        //using string lists as seen below allows me to extract specific
        //pieces of data from the text file

        while(input.hasNextLine()) {
            String currentLine = input.nextLine();
            String[] custPrices = currentLine.split("\t");
            String cost = custPrices[5];
            int costInt = Integer.parseInt(cost); 
            accumulatedSubCost = accumulatedSubCost + costInt;
            
        }        
        
        averageSubCost = accumulatedSubCost / (int)numberOfSubs;   
        
        try {
            
            input = new Scanner(new File(choice));
            
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            System.exit(1);
        }
        //below I use a switch statement to count the number of each
        //subscription tier
        while(input.hasNextLine()) {
            String currentLine = input.nextLine();
            String[] subTier = currentLine.split("\t");
            String tier = subTier[1];
            
            switch(tier){
                case "B":
                    bCount++;
                    break;
                    
                case "S":
                    sCount++;
                    break;
                    
                case "G":
                    gCount++;
                    break;
            }
        }
        
        try {
            
            input = new Scanner(new File(choice));
            
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            System.exit(1);
        }

        while(input.hasNextLine()) {
            String currentLine = input.nextLine();
            String[] months = currentLine.split("-");
            String month = months[1];
            
            switch(month){
                case "Jan":
                    janCount++;
                    break;
                    
                case "Feb":
                    febCount++;
                    break;
                    
                case "Mar":
                    marCount++;
                    break;
                    
                case "Apr":
                    aprCount++;
                    break;
                    
                case "May":
                    mayCount++;
                    break;
                    
                case "Jun":
                    junCount++;
                    break;     
                    
                case "Jul":
                    julCount++;
                    break;
                    
                case "Aug":
                    augCount++;
                    break;
                    
                case "Sep":
                    sepCount++;
                    break;

                case "Oct":
                    octCount++;
                    break;
                    
                case "Nov":
                    novCount++;
                    break;
                    
                case "Dec":
                    decCount++;
                    break;                    
            }
        }        
        //below is the maths required to process the raw data and get the
        //required values.
        averageSub = (janCount + febCount + marCount + aprCount + mayCount + 
                      junCount + julCount + augCount + sepCount + octCount +
                      novCount + decCount) / 12;
        
        bCount = (bCount * 100) / numberOfSubs;
        sCount = (sCount * 100) / numberOfSubs;
        gCount = (gCount * 100) / numberOfSubs;        
        
        //below I am outputting the required data to the user
        
        System.out.println("\n");
        System.out.println("Total number of subscriptions: "+numberOfSubs);
        System.out.println("Average monthly subscriptions: "+averageSub);
        System.out.printf("Average monthly subscription fee: "+"£%.2f ",
                (float)averageSubCost / 100);
        System.out.println("\n");
        
        System.out.println("Percentage of subscriptions: ");
        System.out.printf("Bronze: %.1f %n", bCount);
        System.out.printf("Silver: %.1f %n", sCount);
        System.out.printf("Gold: %.1f %n", gCount); 
        System.out.println(" ");        
        
        System.out.println("Amount of subscriptions per month:");
        System.out.println("Jan  Feb  Mar  Apr  May  Jun  Jul  Aug  Sep  Oct"
                + "  Nov  Dec");
        System.out.println(janCount + "   " + febCount + "   "+marCount +
                 "   " + aprCount + "   "+mayCount + "   " + junCount +
                 "   "+julCount + "   " + augCount + "   "+sepCount +
                 "   " + octCount + "   "+novCount + "   " + decCount);
        System.out.println(" ");
        
        //closing the file
        
        input.close();       
}
     //the below method is similar to the one above, however contains
     //additional logic to extract data from a specific month
     
     public static void informationByMonth(){
         Scanner scan = new Scanner(System.in);
         int accumulatedSubCost = 0;
         int averageSubCost = 0;
         char userChoice;
         String choice = null;
         String monthChoice;
         double subscriptionCount = 0;
         double bCount = 0;
         double sCount = 0;
         double gCount = 0;

         do
         {
             System.out.println("Enter requested file to open, C for current "
                     + "or S for sample: ");
             userChoice = scan.next().charAt(0);
         }while(!Character.toString(userChoice).matches("^[S,C,s,c]*$"));
         System.out.println("Input accepted.");
         userChoice = Character.toUpperCase(userChoice);

         if(userChoice == 'S'){
             choice = "sample.txt";
         }
         else{
             choice = "current.txt";
         }  
        
        //below I am taking user choice on month required and validating it 
         
         do
         {
             System.out.println("Enter the first three letters of a month"
             + " to see the summary, example: 'Jan' 'Mar': ");
             System.out.println(" ");
             System.out.println("Please note you must capitalise month"
             + " correctly in order to proceed");
             monthChoice = scan.next();

         }while(!monthChoice.matches("^[Jan,Feb,Mar,Apr,May,Jun,Jul"
                 + ",Aug,Sep,Oct,Nov,Dec]*$"));
         System.out.println("Input accepted.");                
        Scanner input = null;
        
        try {
            
            input = new Scanner(new File(choice));
            
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            System.exit(1);
        }

        //the below while loop will check each line of the file in which
        //the selected month appears in the file, adding to a count variable
        
        while(input.hasNextLine()) {
            String currentLine = input.nextLine();
            String[] months = currentLine.split("-");
            String month = months[1];
            while(monthChoice.equals(month)){
                subscriptionCount++;
                break;
            }
                 
     }
        
        try {
            
            input = new Scanner(new File(choice));
            
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            System.exit(1);
        }

        //the same logic as above can be seen below, extracting only data
        //which matches the users month choice
        
        while(input.hasNextLine()) {
            String currentLine = input.nextLine();
            String[] months = currentLine.split("-");
            String month = months[1];
            while(monthChoice.equals(month)){
                String[] subTier = currentLine.split("\t");
                String tier = subTier[1];
            
                switch(tier){
                    case "B":
                        bCount++;
                        break;
                    
                    case "S":
                        sCount++;
                        break;
                    
                    case "G":
                        gCount++;
                        break;
                }            
              break;  
            }
            
                 
     }
        
        
        try {
            
            input = new Scanner(new File(choice));
            
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            System.exit(1);
        }

        while(input.hasNextLine()) {
            String currentLine = input.nextLine();
            String[] months = currentLine.split("-");
            String month = months[1];
            while(monthChoice.equals(month)){
                String[] custPrices = currentLine.split("\t");
                String cost = custPrices[5];
                int costInt = Integer.parseInt(cost); 
                accumulatedSubCost = accumulatedSubCost + costInt;
                break;
            }         
     }

        //below is the requrired math logic to process the data from file
        
        averageSubCost = accumulatedSubCost / (int)subscriptionCount;        
        
        bCount = (bCount * 100) / subscriptionCount;
        sCount = (sCount * 100) / subscriptionCount;
        gCount = (gCount * 100) / subscriptionCount;
        
        //outputting the data to the user as required
        
        System.out.println("\n");
        System.out.println("Total subscriptions for "+monthChoice+": "
        +(int)subscriptionCount);
        System.out.printf("Average monthly subscription fee: "+"£%.2f ", 
                (float)averageSubCost / 100);
        System.out.println("\n");
        System.out.println("Percentage of subscriptions: ");
        System.out.printf("Bronze: %.1f %n", bCount);
        System.out.printf("Silver: %.1f %n", sCount);
        System.out.printf("Gold: %.1f %n", gCount);

        input.close();
        
        
         }
         
     
     //the final method findAndDisplay allows the user to search for a
     //partial/full name and outputs each subscription to the console
     
     public static void findAndDisplay(){
         String choice = null;
         String subscriptionString = null;
         char userChoice;
         String nameSearch;
         Scanner scan = new Scanner(System.in);
         
         do
         {
             System.out.println("Enter requested file to open, C for current "
                     + "or S for sample: ");
             userChoice = scan.next().charAt(0);
         }while(!Character.toString(userChoice).matches("^[S,C,s,c]*$"));
         System.out.println("Input accepted.");
         userChoice = Character.toUpperCase(userChoice);

         if(userChoice == 'S'){
             choice = "sample.txt";
         }
         else{
             choice = "current.txt";
         }   
         
        //below I am validating the user input to ensure only alphabetical
        //characters are entered
         
         do
         {
             System.out.println("Enter search constraints, please ensure only "
                     + "alphabetical characters are entered: ");
             System.out.println("If the name is not found in the database "
                     + "you will be returned to the main menu");
             nameSearch = scan.next();
         }while(nameSearch.matches(".*[0-9,`~!@#$%^&*()\\\\-_=+\\\\\\\\|\\\\["
                 + "{\\\\]};:'\\\",<.>/?].*"));         

        Scanner input = null;
         
        try {
            
            input = new Scanner(new File(choice));
            
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            System.exit(1);
        }
        
        //below I use similar logic to earlier in that I am splitting each line
        //of the file into string lists to extract the required data

        while(input.hasNextLine()) {
            String currentLine = input.nextLine();
            String[] custNames = currentLine.split("\t");
            String name = custNames[6];
            
            
            while(name.toLowerCase().contains(nameSearch.toLowerCase())){
                String[] custInfo = currentLine.split("\t");
                String cost = custInfo[5];
                String subscriptionType = custInfo[4];
                String discountCode = custInfo[3];
                String duration = custInfo[2];
                String subTier = custInfo[1];
                String date = custInfo[0];
                int costInt = Integer.parseInt(cost);
                
                //the below switch statements are for processing raw data
                //into the correct format for printing as seen earlier
                
                switch(subscriptionType){
                    case "O":
                        subscriptionType = "One-off";
                        subscriptionString = "One-off Subscription: ";
                        break;
                    case "M":
                        subscriptionType = "Monthly";
                        subscriptionString = "Monthly Subscription: ";
                        break;
                }
                
                switch(subTier){
                    case "B":
                        subTier = "Bronze";
                        break;
                    case "S":
                        subTier = "Silver";
                        break;
                    case "G":
                        subTier = "Gold";
                }    
                
                switch(duration){
                    case "1":
                        duration = "One";
                        break;
                    case "3":
                        duration = "Three";
                        break;
                    case "6":
                        duration = "Six";
                        break;
                    case "12":
                        duration = "Twelve";
                        break;                        
                }
                
             
                
                //for each line of the file that matches, the below print
                //statement will iterate once, giving the desired output
                
                System.out.println("\n");
                
                System.out.println("+=========================================="
                        + "====================+\n" +
                            "                                                  "
                        + "              \n" +
                            "                    Customer: "+name+"            "
                                    + "                           \n" +
                            "                                                  "
                                    + "              \n" +
                            "   Date: "+date+"             Discount Code: "
                        +discountCode+"             \n" +
                            "   Package: "+subTier+"                 Duration: "
                        +duration+"               \n" +
                            "   Terms: "+subscriptionType+"                    "
                                    + "                                 \n" +
                                    
                            "                                                  "
                                    + "              \n" +
                            "           ");System.out.printf("                 "
                                    + ""+subscriptionString+"£%.2f ", 
                                    (float)(costInt / 100));
                            System.out.println(" \n" +
                            "                                                  "
                                    + "              \n" +
                            "+================================================="
                                    + "=============+");
                            
                System.out.println("\n");
                            
                break;
            }         
     }
         
     }
}
