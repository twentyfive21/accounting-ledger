package com.pluralsight;
/*

- This class creates a transactions object by storing private attributes
such as date, time, description, vendor, and price.

- Keeping attributes private ensures data encapsulation
 and control over access of data remains in the class itself.

- toString() method is overridden
 to provide a readable formatted string.
 This default implementation often returns a not-so-useful output,
 typically the class name followed by the memory address of the object.

 */
public class Transaction {

    // attributes for class
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double price;

    // This constructor initializes a transaction object with the provided date, time, vendor, and price.

    public Transaction(String date, String time, String description, String vendor, double price) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("****------------------------------------****\nDate: %s & Time: %s\n" +
                        "Price: $%.2f\nVendor: %s\nDescription: %s",
                date, time, price, vendor, description);
    }
}
