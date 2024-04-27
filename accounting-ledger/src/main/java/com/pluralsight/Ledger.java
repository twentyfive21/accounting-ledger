package com.pluralsight;

public class Ledger {

    public static void main(String[] args) {
    // call displayHomeScreen() to start program
        displayHomeScreen();
    }

    public static void displayHomeScreen(){
        System.out.println("addDeposit");
        System.out.println("makePayment");
        System.out.println("displayLedger");
        System.out.println("exitProgram");
    }

    public static void displayLedger(){
        // display newest transactions first for all !
        System.out.println("displayAllEntries");
        System.out.println("displayDeposits");
        System.out.println("displayPayments");
        System.out.println("displayReports");
        // go back to home screen
        // displayHomeScreen();
    }

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

}

