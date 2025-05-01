import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Transactions {

    // variables to store transaction details
    LocalDate date; //stores the date of transaction
    LocalTime time; //stores the time
    String description; //description of transaction
    String transactionId; // differentiate deposit or payment
    String vendor; //vendor or company
    double amount; //the amount of money
    LocalDateTime dateTime; //combines date and time for easy comparison


    // Constructor to initialize all variables when a transaction is created
    public Transactions (LocalDate date, LocalTime time, String description, String vendor, String transactionId, double amount){
        //Combines date and time into a single object for more convenient date-based operations
        this.dateTime = LocalDateTime.of(date,time);
        this.date = date;
        this.time = time;
        this.description = description;
        this.transactionId = transactionId;
        this.vendor = vendor;
        this.amount = amount;

    }
    // toString() method formats the transaction details as a string so they can be saved or displayed

    @Override
    public String toString() {
        //Uses custom formatters from the main app to keep the format consistent
        return date.format(AccountLedgerApp.dateFormatter) + "|" + time.format(AccountLedgerApp.timeFormatter) + "|" + description + "|" + vendor + "|" + transactionId + "|" + amount;
    }


    // Getter and setter methods below are used to safely access and update each variable from other classes

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
