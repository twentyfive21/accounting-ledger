package com.pluralsight;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Ledger {

// Declaring the scanner globally to enhance code cleanliness and eliminate the need for passing
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>();

// ~~~~~~~~~~~~~~~~~~~~ MAIN ~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void main(String[] args) {
        loadInventory();
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
        System.out.println("Please select what you would like to do today.");
        System.out.println("(D) Add Deposit");
        System.out.println("(P) Make Payment");
        System.out.println("(L) Ledger");
        System.out.println("(X) Exit");
        System.out.print("Selection: ");
        // turn choice to lower case for checking and error handles both cases
        String choice = scanner.nextLine().toLowerCase();

        switch (choice){
            case "d": getUserInput("deposit from");
                break;
            case "p": getUserInput("spent on");
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

// ~~~~~~~~~~~~~~~~~~~ FORMAT INPUT ~~~~~~~~~~~~~~~~~~~~~~~~
    public static void getUserInput(String type){
        try{
            System.out.printf("\n~~~~ You have chosen to add a %s ~~~~\n", type);
            System.out.print("Please provide the description: ");
            String description = scanner.nextLine().trim();
            System.out.print("Please provide the vendor: ");
            String vendor = scanner.nextLine().trim();
            System.out.print("Please provide the price: ");
            double price = 0.00;
            if(type.equals("deposit from")){
                price = scanner.nextDouble();
            } else {
                price = scanner.nextDouble();
                // turn double negative
                price = -price;
            }
            System.out.printf("\n$%,.2f %s %s added on %s",price,type,vendor,getDateAndTime());
            // clear left over in buffer if try is successful
            scanner.nextLine();
            // get date and time for object creation
            String unformattedForObject = getDateAndTime();
            String[] dateAndTime = unformattedForObject.split(Pattern.quote("|"));
            Transaction item = new Transaction(dateAndTime[0],dateAndTime[1],description,vendor,price);
            // add transaction object to array list
            transactions.add(item);
            writeToFile(description,vendor,price);
        } catch (Exception e){
            System.out.println("\n**** Error adding deposit please try again. ****");
            // next line handles the error and clears buffer since try failed
            scanner.nextLine();
            getUserInput(type);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~ FORMAT TRANSACTION  ~~~~~~~~~~~~~~~~~~~~~~~~
    public static void writeToFile(String description, String vendor, double price){
        try{
            String fmtString = String.format("%s|%s|%s|%.2f",getDateAndTime(),description, vendor, price);
            // create file and write using buffer reader set append to true so file is not overwritten
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));
            // file.exists(): This checks if the file does not exist.
            // file.length() == 0: This checks if the file exists but its length is 0, meaning it's empty.
            File file = new File("transactions.csv");
            if(!file.exists() || file.length() == 0){
                writer.write("date|time|description|vendor|amount");
                writer.newLine();
            }
            writer.write(fmtString);
            // Add a new line in the file to separate different entries or sections
            writer.newLine();
            // writes to file immediately without having to close out file to view data & close after
            writer.flush();
            writer.close();
            // The comma in the format string is used to include a thousand separator in the price for readability.
            System.out.println("\n**** Transaction added successfully ****");
        }catch(Exception e){
            System.out.println("\n**** Error writing to file ****");
        }
        // take user back home
        displayHomeScreen();
    }

// ~~~~~~~~~~~~~~~~~~~ DISPLAY LEDGER  ~~~~~~~~~~~~~~~~~~~~~~~~

    public static void displayLedger(){
        System.out.println("\n~~~~~~ Welcome to the ledger screen ~~~~~~");
        System.out.println("(A) Display all entries");
        System.out.println("(D) Display deposits");
        System.out.println("(P) Display payments");
        System.out.println("(R) Display Reports");
        System.out.println("(H) Go back home");
        System.out.print("Selection: ");
        String choice = scanner.nextLine().trim().toLowerCase();
        switch(choice){
            case "a": displayAllEntries();
                break;
            case "d": System.out.println("hello");
                //displayDeposits();
                break;
            case "p": System.out.println("hello2");
                //displayPayments();
                break;
            case "r": displayReports();
                break;
            case "h": displayHomeScreen();
                break;
            default: System.out.println("\n**** Error please choose a valid option ****");
                     displayLedger();
                break;
        }

    }
// ~~~~~~~~~~~~~~~~~~~ DISPLAY ALL ENTRIES  ~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static void displayAllEntries(){
        // display newest transactions first for all !
        transactions.sort(Comparator.comparing(Transaction::getDate));
        System.out.println("\n~~~~~~ Start of all the current transactions ~~~~~~");
            for(Transaction item : transactions){
                System.out.println(item);
            }
        System.out.println("\n~~~~~~ End of all the current transactions ~~~~~~");
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

// ~~~~~~~~~~~~~~~~~~~ LOAD INVENTORY  ~~~~~~~~~~~~~~~~~~~~~~~~

    public static void loadInventory(){
        try{
            BufferedReader bufReader = new BufferedReader(new FileReader("transactions.csv"));
            // skips header
            bufReader.readLine();
            String item = "";
            while ((item = bufReader.readLine()) != null){

                String[] entry = item.split(Pattern.quote("|"));

                Transaction currentItem = new Transaction(entry[0], entry[1],
                        entry[2],entry[3], Double.parseDouble(entry[4]));

                transactions.add(currentItem);

            }
        } catch (Exception e){
            System.out.println("Error loading inventory");
        }
    }

// ~~~~~~~~~~~~~~~~~~~ EXIT PROGRAM  ~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void exitProgram() {
        System.out.println("\nThank you for visiting! Goodbye, come again! :)");
    }

}