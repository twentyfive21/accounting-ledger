package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
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
        // Display welcome message
        System.out.println("\n~~~~~~~~~ Welcome to the Account Ledger Application ~~~~~~~~~");
        System.out.println("~ We are pleased you have chosen us for your business needs. ~");
        System.out.println("Please select what you would like to do today.");
        // Display menu options
        System.out.println("(D) Add Deposit");
        System.out.println("(P) Make Payment");
        System.out.println("(L) Ledger");
        System.out.println("(X) Exit");
        System.out.print("Selection: ");
        // Get user input and convert it to lowercase for case-insensitive comparison
        String choice = scanner.nextLine().toLowerCase();
        // Handle user choice
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
        try{ // get user info for Transaction object
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
            writer.newLine();
            writer.write(fmtString);
            // Add a new line in the file to separate different entries or sections
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
        // Display menu options
        System.out.println("\n~~~~~~ Welcome to the ledger screen ~~~~~~");
        System.out.println("(A) Display all entries");
        System.out.println("(D) Display deposits");
        System.out.println("(P) Display payments");
        System.out.println("(R) Display Reports");
        System.out.println("(H) Go back home");
        System.out.print("Selection: ");
        String choice = scanner.nextLine().trim().toLowerCase();
        // Handle user choice
        switch(choice){
            case "a": displayAllEntries();
                break;
            case "d": displayChosenEntries("deposit");
                break;
            case "p": displayChosenEntries("negative");
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

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~ DISPLAY ALL ENTRIES  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void displayAllEntries(){
    /* transactions.sort(...): Sorts the transactions list in desc order based on the transaction date.
    *
    * Comparator.comparing(...): Creates a comparator that extracts the date from each Transaction object
    * and compares them using their natural ordering or a specified comparator.
    *
    * Transaction::getDate: Method reference to the getDate() method of the Transaction class,
    * used to extract the date from each Transaction object.
    *
    * .reversed(): Reverses the natural ordering of the comparator, ensuring transactions are sorted
    * in descending order of dates, from newest to oldest. */
        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
        System.out.println("\n~~~~~~ Start of all the current transactions ~~~~~~");
            for(Transaction item : transactions){
                System.out.println(item);
            }
        System.out.println("\n~~~~~~ End of all the current transactions ~~~~~~");
        // take user back to ledger
        displayLedger();
    }

// ~~~~~~~~~~~~~~~~~~~~~ DISPLAY DEPOSITS OR NEGATIVE TRANSACTIONS ~~~~~~~~~~~~~~~~~~~~~~~

    public static void displayChosenEntries(String choice){
        // Sort transactions in descending order based on date
        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
        System.out.printf("\n~~~~~~ Start of all the %s transactions ~~~~~~\n", choice);
        // Iterate over transactions to display chosen entries based on the choice
        if(choice.equals("deposit")){
            // Display transactions with positive prices (deposits)
            for(Transaction item : transactions){
                if(item.getPrice() > 0){
                    System.out.println(item);
                }
            }
        } else {
            // Display transactions with negative prices (payments)
            for(Transaction item : transactions){
                if(item.getPrice() < 0){
                    System.out.println(item);
                }
            }
        }

        System.out.printf("\n~~~~~~ End of all the %s transactions ~~~~~~\n", choice);
        // take user back to ledger
        displayLedger();
    }


// ~~~~~~~~~~~~~~~~~~~ DISPLAY REPORTS  ~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void displayReports(){
        /* the workshop asked to have a method to re-route you back
         to reports page while on the reports screen
         if that method is called on the reports screen logic is not valid
         option 0 is changed to take you back to ledger instead
         else user would be stuck in reports screen with no way out */
        System.out.println("\n~~~~~ Welcome to the reports page ~~~~\n");
        System.out.println("(1) Month to Date");
        System.out.println("(2) Previous Month");
        System.out.println("(3) Year To Date");
        System.out.println("(4) Previous Year");
        System.out.println("(5) Search by Vendor");
        System.out.println("(0) Go back to Ledger");
        System.out.print("Selection: ");
        String choice = scanner.nextLine();
        // Handle user choice
        switch (choice){
            case "1": displayMonthReport("current");
                break;
            case "2": displayMonthReport("previous");
                break;
            case "3": displayYearReport("current");
                break;
            case "4": displayYearReport("previous");
                break;
            case "5": searchByVendor();
                break;
            case "0": displayLedger(); // Go back to ledger
                break;
            default: System.out.println("\n**** Error invalid choice ****\n");
                displayReports();
                break;
        }

    }

// ~~~~~~~~~~~~~~~~~~~ ALL REPORT METHODS START  ~~~~~~~~~~~~~~~~~~~~~~~~

    // ************* MONTH TO DATE && PREVIOUS MONTH REPORT ****************
    public static void displayMonthReport(String type){
        // get current day
        LocalDate today;
        if(type.equals("current")){
            // current month choice
             today = LocalDate.now();
        } else {
            // previous month choice
            today = LocalDate.now().minusMonths(1);
        }
        // get beginning of month
        LocalDate monthFromNow = today.withDayOfMonth(1);
        System.out.printf("\n~~~~ All transactions from %s to %s ~~~~\n",monthFromNow, today);

        // check for dates matching the range
        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
        for(Transaction currentDate : transactions){
            // parse the date to localDate since it is saved as a string in the object
            LocalDate date = LocalDate.parse(currentDate.getDate());
            // Adjusting the date range to include all days within the range (inclusive)
            boolean startDate = date.isAfter(monthFromNow.minusDays(1));
            boolean endDate = date.isBefore(today.plusDays(1));
            if(startDate && endDate){
                System.out.println(currentDate);
            }
        }
        System.out.printf("\n~~~~ End of transactions from %s to %s ~~~~\n",monthFromNow, today);
        System.out.println();
        displayReports();
    }

    // ************* YEAR TO DATE && PREVIOUS YEAR REPORT ****************

    public static void displayYearReport(String type){
        LocalDate endOfYear;
        LocalDate startOfYear;
        // show current year to date
        if(type.equals("current")){
            endOfYear = LocalDate.now();
            startOfYear = LocalDate.now().withDayOfMonth(1).withMonth(1);
        } else {
            // 01-01-2023 to 12-31-2023
            startOfYear = LocalDate.now().minusYears(1).withMonth(1).withDayOfMonth(1);
            endOfYear = LocalDate.now().minusYears(1).withMonth(12).withDayOfMonth(31);
        }
        // display the newest dates first
        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
        System.out.printf("\n~~~~ All transactions from %s to %s ~~~~\n",startOfYear, endOfYear);

        for(Transaction currentDate : transactions){
            // parse the date to localDate since it is saved as a string in the object
            LocalDate date = LocalDate.parse(currentDate.getDate());
            // Adjusting the date range to include all days within the range (inclusive)
            boolean startYear = date.isAfter(startOfYear.minusDays(1));
            boolean endYear = date.isBefore(endOfYear.plusDays(1));
            if(startYear && endYear){
                System.out.println(currentDate);
            }
        }
        System.out.printf("\n~~~~ End of transactions from %s to %s ~~~~\n",startOfYear, endOfYear);
        // re-run program
        displayReports();
    }

    // ********************* SEARCH BY VENDOR REPORT *********************
    // (TODO)
    public static void searchByVendor(){
        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
    }

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~ ALL REPORT METHODS END  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~


// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ LOAD INVENTORY  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void loadInventory(){
        try{ // Open the file "transactions.csv" for reading
            BufferedReader bufReader = new BufferedReader(new FileReader("transactions.csv"));
            // Skip the header line
            bufReader.readLine();
            String item = "";
            // Read each line of the file until the end
            while ((item = bufReader.readLine()) != null){
                // Split the line into individual elements using the "|"
                String[] entry = item.split(Pattern.quote("|"));
                // Create a new Transaction object using the elements from the line
                Transaction currentItem = new Transaction(entry[0], entry[1],
                        entry[2],entry[3], Double.parseDouble(entry[4]));
                // Add the Transaction object to the transactions list
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