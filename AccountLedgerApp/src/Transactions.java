import java.time.LocalDate;
import java.time.LocalTime;

public class Transactions {
    // variables for transaction class
    LocalDate date;
    LocalTime time;
    String description;
    String transactionId;
    String vendor;
    double amount;


    // adding all variables to constructor and add in all variables
    public Transactions (LocalDate date, LocalTime time, String description, String transactionId, String vendor, double amount){
        this.date = date;
        this.time = time;
        this.description = description;
        this.transactionId = transactionId;
        this.vendor = vendor;
        this.amount = amount;

    }
    // too string() to create the list

    @Override
    public String toString() {
        return date.format(AccountLedgerApp.dateFormatter) + "|" + time.format(AccountLedgerApp.timeFormatter) + "|" + description + "|" + vendor + "|" + transactionId + "|" + amount;
    }


    // creating my getters and setters


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
