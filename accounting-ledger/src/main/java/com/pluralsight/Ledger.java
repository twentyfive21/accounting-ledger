package com.pluralsight;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

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
            Double price = scanner.nextDouble();

            // clear left over in buffer
            scanner.nextLine();
            String formattedString = "|" + vendor + "|" + price;
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv"));
            writer.write(getDateAndTime() + formattedString);
            // close writer
            writer.close();
            System.out.println("\n**** Item added successfully ****");

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