package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
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
            case "d": getUserInput("deposit");
                break;
            case "p": getUserInput("payment");
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
            // get user info for Transaction object
            System.out.printf("\n~~~~ You have chosen to add a %s ~~~~\n", type);
            System.out.print("Please provide the description: ");
            String description = scanner.nextLine().trim();
            System.out.print("Please provide the vendor: ");
            String vendor = scanner.nextLine().trim();
            System.out.print("Please provide the price: ");

            double price = 0.00;
            if(type.equals("deposit")){
                price = scanner.nextDouble();
            } else {
                price = scanner.nextDouble();
                // turn double negative
                price = -price;
            }
            System.out.printf("\n$%,.2f %s from %s added on %s",price,type,vendor,getDateAndTime());
            scanner.nextLine();
            // get date and time for object creation
            String unformattedForObject = getDateAndTime();
            String[] dateAndTime = unformattedForObject.split(Pattern.quote("|"));
            Transaction item = new Transaction(dateAndTime[0],dateAndTime[1],description,vendor,price);
            // add transaction object to array list
            transactions.add(item);
            writeToFile(description,vendor,price);
        } catch (Exception e){
            if(type.equals("deposit")){
                System.out.println("\n**** Error adding deposit please try again. ****");
            } else {
                System.out.println("\n**** Error adding payment please try again. ****");
            }
//            System.out.println("\n**** Error adding please try again. ****");
            /* next line handles the error and clears buffer since try failed
            if not cleared will skip home screen and go back to deposit */
            scanner.nextLine();
            displayHomeScreen();
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
        System.out.println("(6) Custom Search");
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
            case "6": customSearch();
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
            /* previous month choice
             .with(TemporalAdjusters.lastDayOfMonth()): This adjusts the date to the last day of that month.
             TemporalAdjusters is a utility method in Java's java.time package  */
            today = LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
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

    public static void searchByVendor(){
        // Prompt the user for input
        System.out.println("~~~~ You have chosen to search by vendor ~~~~");
        System.out.print("Please provide vendor: ");
        // Get user input and convert it to lowercase for case-insensitive comparison
        String input = scanner.nextLine().trim().toLowerCase();
        // Sort transactions in descending order based on date
        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
        // Initialize a boolean flag to track if any matching vendors are found
        boolean match = false;
        System.out.printf("\n~~~~ Search results for %s ~~~~\n", input);
        // Iterate over transactions to search for the provided vendor
        for(Transaction vendor : transactions){
            String item = vendor.getVendor().toLowerCase();
            // Check if the vendor name contains the user-provided input
            if(item.contains(input)){
                // Update flag to indicate a match is found
                match = true;
                System.out.println(vendor);
            }
        }
        // Display message if no matching vendors are found
        if(!match){
            System.out.printf("\nNo matching vendors for %s :( \n", input);
        }
        System.out.printf("\n~~~~ End of search results for %s ~~~~\n", input);
        // re-run program
        displayReports();
    }

    // ********************* CUSTOM SEARCH *********************

    public static void customSearch() {
        try {
            System.out.println("\n~~~~ Welcome to the custom search ~~~~");
            System.out.print("Start Date yyyy-mm-dd : ");
            String startDate = scanner.nextLine().trim();
            System.out.print("End Date yyyy-mm-dd: ");
            String endDate = scanner.nextLine().trim();
            System.out.print("Description: ");
            String details = scanner.nextLine().trim().toLowerCase();
            System.out.print("Vendor: ");
            String vendor = scanner.nextLine().trim().toLowerCase();
            System.out.print("Exact Price: ");
            String price = scanner.nextLine();

            // Sort transactions in descending order based on date
            transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
            boolean found = false; // Flag to track if any transactions are found

            for (Transaction item : transactions) {
            //Make a variable to hold the date of each transaction and parse it to a LocalDate datatype
                boolean dateInRange = isDateInRange(item, startDate, endDate);
                boolean matchesDetails = details.isEmpty() || item.getDescription().toLowerCase().contains(details);
                boolean matchesVendor = vendor.isEmpty() || item.getVendor().toLowerCase().contains(vendor);
                boolean matchesPrice = price.isEmpty() || Double.parseDouble(price) == item.getPrice();
            /* print out matching transaction. If a field is empty(null), the condition will be true,
                effectively ignoring that criteria in the search. This allows the user to perform a search
                based on any combination of criteria without being required to provide values for all fields. */
                if (dateInRange && matchesDetails && matchesVendor && matchesPrice) {
                    System.out.println(item);
                    found = true; // Set found flag to true if at least one transaction is found
                }
            }
            if (!found) {
                System.out.println("No matching transactions found.");
            }
        } catch (Exception e) {
            System.out.println("\nNo matching transactions found within this criteria.");
        }
        displayReports();
    }
    /* isDateInRange method private is a good practice when the method is intended for internal
     use within the class(customSearch can only access it) and does not need to be accessed from outside the class.
     It encapsulates the functionality within the class, reducing the scope of access and preventing unintended use or
     modification from other parts of the program.

    Making the method static allows it to be called without creating an instance of the class.
    This is useful for utility methods like isDateInRange, which operate on parameters passed to them
    and do not rely on the state of any particular object instance. */
    // ********************* IS DATE IN RANGE Referencing CUSTOM SEARCH *********************
    private static boolean isDateInRange(Transaction item, String startDate, String endDate) {
        LocalDate transactionDate = LocalDate.parse(item.getDate());
        // Check if both start and end dates are empty return true and do not filter
        if (startDate.isEmpty() && endDate.isEmpty()) {
            return true;
       }
        // return start date since no end date was provided
        if (!startDate.isEmpty() && endDate.isEmpty() && transactionDate.isEqual(LocalDate.parse(startDate))){
            return true;
        }
        // return end date since no start date was provided
        if (startDate.isEmpty() && !endDate.isEmpty() && transactionDate.isEqual(LocalDate.parse(endDate))){
            return true;
        }
        // return inclusive dates from start to end date
        if (!startDate.isEmpty() && !endDate.isEmpty() &&
                transactionDate.isAfter(LocalDate.parse(startDate).minusDays(1)) &&
                transactionDate.isBefore(LocalDate.parse(endDate).plusDays(1))){
            return true;
        }
        // return false if nothing matches
        return false;
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