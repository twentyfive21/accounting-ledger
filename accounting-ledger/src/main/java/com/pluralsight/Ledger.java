package com.pluralsight;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Ledger {

// Declaring the scanner globally to enhance code cleanliness and eliminate the need for passing
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>();

// ~~~~~~~~~~~~~~~~~~~~ MAIN ~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void main(String[] args) {
    // call displayHomeScreen() to start program
        displayHomeScreen();
    }

// ~~~~~~~~~~~~~~~~~~~ DATE AND TIME ~~~~~~~~~~~~~~~~~~~~~~
    // get date and time for each transaction
    public static String getDateAndTime(){
        // get the current date and time
        LocalDateTime date = LocalDateTime.now();
        // use date formatter to select desired format
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss");
        // format the date with .format
        String formattedTime = date.format(fmt);
        // return string to allow for use when called
        return formattedTime;
    }

// ~~~~~~~~~~~~~~~~~~ DISPLAY HOME SCREEN ~~~~~~~~~~~~~~~~~

    public static void displayHomeScreen(){

        System.out.println("\n~~~~~~~~~ Welcome to the Account Ledger Application ~~~~~~~~~");
        System.out.println("~ We are pleased you have chosen us for your business needs. ~");
        System.out.println("Please select what you would like to do today");
        System.out.println("(D) Add Deposit");
        System.out.println("(P) Make Payment");
        System.out.println("(L) Ledger");
        System.out.println("(X) Exit");
        System.out.print("Selection: ");
        // turn choice to lower case for checking and error handles both cases
        String choice = scanner.nextLine().toLowerCase();

        switch (choice){
            case "d": addDeposit();
                break;
            case "p": makePayment();
                break;
            case "l": displayLedger();
                break;
            case "x": exitProgram();
                break;
            default: System.out.println("\n**** Error invalid input please choose again. ****");
                     displayHomeScreen();
                break;
        }
    }
// ~~~~~~~~~~~~~~~~~~~ ADD DEPOSIT  ~~~~~~~~~~~~~~~~~~~~~~~~

    public static void addDeposit(){
        try{
            System.out.println("\n~~~~ You have chosen to add a deposit ~~~~\n");
            System.out.print("Please provide the vendor: ");
            String vendor = scanner.nextLine().trim();
            System.out.print("Please provide the price: ");
            double price = scanner.nextDouble();
            // clear left over in buffer
            scanner.nextLine();
            String formattedString = getDateAndTime() + "|" + vendor + "|" + price;
            // create file and write using buffer reader set append to true so file is not overwritten
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));
            // file.exists(): This checks if the file does not exist.
            // file.length() == 0: This checks if the file exists but its length is 0, meaning it's empty.
            File file = new File("transactions.csv");
            if(!file.exists() || file.length() == 0){
                writer.write("date|time|description|vendor|amount");
                writer.newLine();
            }
            writer.write(formattedString);
            // Add a new line in the file to separate different entries or sections
            writer.newLine();
            // get date and time for object creation
            String unformattedForObject = getDateAndTime();
            String[] dateAndTime = unformattedForObject.split(Pattern.quote("|"));
            Transaction itemToAdd = new Transaction(dateAndTime[0],dateAndTime[1],vendor,price);
            // add transaction object to array list
            transactions.add(itemToAdd);
            // writes to file immediately without having to close out file
            writer.flush();
            // close writer
            writer.close();
            // The comma in the format string is used to include a thousand separator in the price for readability.
            System.out.printf("\n$%,.2f spent on %s added on %s",price,vendor,getDateAndTime());
            System.out.println("\n**** Transaction added successfully ****");
        }catch(Exception e){
            scanner.nextLine();
            System.out.println("\n**** Error adding deposit please try again. ****");
        }
        // take user back home
        displayHomeScreen();
    }

// ~~~~~~~~~~~~~~~~~~~ MAKE PAYMENT  ~~~~~~~~~~~~~~~~~~~~~~~~

    public static void makePayment(){

    }

// ~~~~~~~~~~~~~~~~~~~ DISPLAY LEDGER  ~~~~~~~~~~~~~~~~~~~~~~~~

    public static void displayLedger(){
        // display newest transactions first for all !
//        for(Transaction item : transactions){
//            System.out.println(item);
//        }
        System.out.println("displayAllEntries");
        System.out.println("displayDeposits");
        System.out.println("displayPayments");
        System.out.println("displayReports");
        // go back to home screen
        // displayHomeScreen();
    }

// ~~~~~~~~~~~~~~~~~~~ DISPLAY REPORTS  ~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static void displayReports(){
        System.out.println("displayMonthToDate");
        System.out.println("displayPreviousMonth");
        System.out.println("displayYearToDate");
        System.out.println("displayPreviousYear");
        System.out.println("searchByVendor");
        System.out.println("searchByInput");
        // go back to report screen
        System.out.println("displayReports");
    }

// ~~~~~~~~~~~~~~~~~~~ EXIT PROGRAM  ~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void exitProgram() {
        System.out.println("\nThank you for visiting! Goodbye, come again! :)");
    }

}