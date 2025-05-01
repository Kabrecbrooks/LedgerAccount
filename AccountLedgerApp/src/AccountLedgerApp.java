import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class AccountLedgerApp {
    // reads input from console
    static Scanner scanner = new Scanner(System.in);
    //path to CSV file
    static String transactionFileName = "src/transaction.csv";
    //control how date/time are displayed
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {

        // shows the main screen with options for user
        homeMenu();

    }

    public static void homeMenu() {
        System.out.println("\n ** WELCOME TO YOUR ACCOUNT LEDGER ** \n");

        // until the user exits the menu will continue to run
        boolean runningHomeProgram = true;

        //next, creating a while loop to go until the condition is met
        while (runningHomeProgram) {
            System.out.println("""
                    \n ** WHAT WOULD YOU LIKE TO DO TODAY? ** \n
                    A. Add a deposit
                    B. Make a payment
                    C. Ledger screen
                    D. Exit """);
            // user makes their selection and converts to uppercase to make it easier to check
            String userInput = scanner.nextLine().trim().toUpperCase();
            // creating a switch statement for user to choose which option they will want
            switch (userInput) {
                case "A":
                    // If user picks A, it calls the method to add a deposit
                    Transactions newDeposit = addDeposit();
                    //saves the new deposit to file
                    writeToFile(transactionFileName, newDeposit);
                    break;
                case "B":
                    Transactions newPayment = makePayment();
                    writeToFile(transactionFileName, newPayment);
                    break;
                case "C":
                    // takes user back to user menu
                    ledgerMenu();
                    break;
                case "D":
                    //this is to get the program to stop running
                    runningHomeProgram = false;
                    break;

            }

        }
    }

    public static Transactions addDeposit() {

        // getting the current date and time for the transaction
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        // getting the deposit amount and convert user input into a double
        System.out.println(" Provide deposit amount ");
        double amount = Double.parseDouble(scanner.nextLine());

        System.out.println(" Enter vendor : ");
        String vendor = scanner.nextLine().trim();

        System.out.println(" Enter item description: ");
        String description = scanner.nextLine().trim();

        System.out.println(" Complete! ");
        //setting transaction ID to "D" for deposit
        String transactionId = "D";

        //call the constructor out, taking the values and sending them to the constructor
        // Takes user input and create single deposit transaction with the info
        return new Transactions(ld, lt, description, vendor, transactionId, amount);

    }

    // It takes in the file name (where to save) and the transaction object (what to save)
    public static void writeToFile(String fileName, Transactions transaction) {
        // 'FileWriter' opens the file and gets it ready for writing
        // 'true' means we're appending to the file — not erasing old data, just adding to the end
        try (FileWriter fw = new FileWriter(fileName, true)) {
            // Write the transaction as a string followed by a new line
            // The transaction object uses its toString() method to format the data correctly
            fw.write(transaction.toString() + "\n");
        } catch (IOException e) {
            // If there's an error (like the file doesn't exist), this will show a message instead of crashing the program
            System.out.println("Something went wrong " + e.getMessage());

        }
    }

    // My payment screen
    public static Transactions makePayment() {
        //used to timestamp the transaction by getting current date and time
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();

        System.out.println("Please enter the payment amount: ");
        //convert amount to a negative number to represent money being spent
        double amount = Double.parseDouble((scanner.nextLine())) * -1;

        System.out.println("Please enter the vendor name: ");
        String vendor = scanner.nextLine().trim();

        System.out.println("Please provide a description: ");
        String description = scanner.nextLine().trim();

        System.out.println("Deposit is complete! ");
        //Use "P" as a simple way to mark this transaction as a Payment
        String transactionId = "P";
        // Create and return a new Transaction object with the user's inputs
        return new Transactions(ld, lt, description, vendor, transactionId, amount);
    }

    // Creating my ledger menu with methods
    public static void ledgerMenu() {

        System.out.println("\n ** Ledger main menu ** \n");
        // This boolean controls the while loop so the menu keeps running until the user exits
        boolean ledgerRunning = true;

        // Loop keeps showing the menu until the user chooses to go back to the home screen
        while (ledgerRunning) {
            // give the user options to pick for show deposits and show all payments
            System.out.println(""" 
                     \n ** select from following options:  **\n
                    A. Display all entries
                    D. Deposits
                    P. Payments
                    R. Reports
                    H. Home
                    """);
            // Take user input, remove any extra spaces, and convert to uppercase for easy matching
            String userInputLedger = scanner.nextLine().trim().toUpperCase();


            // Use a switch statement to handle the user's selection
            switch (userInputLedger) {
                case "A":
                    // Get ALL transactions from the file
                    List<Transactions> transactions = getTransactionFromFile(transactionFileName);
                    // Sort them by date & time, with the most recent on top//Stan helped me figure this piece out
                    transactions.sort(Comparator.comparing(Transactions::getDateTime).reversed());
                    // Show the sorted transactions on the screen
                    displayTransaction(transactions);
                    break;
                case "D":
                    // Get only DEPOSIT transactions using ID "D"
                    List<Transactions> depositTransaction = searchTransById("D", "Deposit");
                    depositTransaction.sort(Comparator.comparing(Transactions::getDateTime).reversed());
                    displayTransaction(depositTransaction);
                    break;
                case "P":
                    // Get only PAYMENT transactions using ID "P"
                    List<Transactions> paymentTransaction = searchTransById("P", "Payments");
                    paymentTransaction.sort(Comparator.comparing(Transactions::getDateTime).reversed());
                    displayTransaction(paymentTransaction);
                    break;
                case "R":
                    reportMenu();
                    break;
                case "H":
                    // Set the boolean to false and exit the loop and go back to the home screen
                    ledgerRunning = false;
                    break;


            }
        }

    }

    // Displaying all entries
    public static List<Transactions> getTransactionFromFile(String fileName) {
        // Create a new list to store all transactions from the file
        List<Transactions> transactions = new ArrayList<>();
        // Try-with-resources ensures the file is automatically closed after reading
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Read the file line by line
            while ((line = br.readLine()) != null) {
                // Split each line by the "|" character — each part is a different field of the transaction
                String[] arrTransaction = line.split("\\|");
                // Create a new Transaction object using the data from the line
                //Parse the date string[0],Parse the time string[1],Description[2],Vendor[3],Transaction ID (Deposit/Payment)[4],Amount[5]
                Transactions transaction = new Transactions(LocalDate.parse(arrTransaction[0], dateFormatter), LocalTime.parse(arrTransaction[1], timeFormatter), arrTransaction[2], arrTransaction[3], arrTransaction[4], Double.parseDouble(arrTransaction[5]));
                //Add the transaction to list
                transactions.add(transaction);

            }
        } catch (IOException e) {
            // If something goes wrong while reading the file, print the error message
            System.out.println(e.getMessage());
        }
        // Return the list of transactions so other methods can use it
        return transactions;
    }

    // displaying all entries
    public static void displayTransaction(List<Transactions> transactions) {
        // Loop through each transaction in the list passed into this method
        for (Transactions transaction : transactions) {
            // Print each transaction's details in a clean and formatted way
            // Format and print the date, Format and print the time, Print the description, Print the vendor, Print the amount (to 2 decimal places)
            System.out.printf("%s | %s | %s | %s | %.2f%n", transaction.getDate().format(dateFormatter), transaction.getTime().format(timeFormatter), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
        }
    }

    //showing all deposits
    public static List<Transactions> searchTransById(String id, String TransTypeName) {
        System.out.println("\n ** Showing Deposits & Payments " + TransTypeName + ": **\n");

        //list containing all transactions from CSV file
        List<Transactions> transactions = getTransactionFromFile(transactionFileName);

        // Create a new list to store only the matching transactions (either Deposits or Payments)
        List<Transactions> matchingTransById = new ArrayList<>();

        // for each loop where it will enter into csv file and sort out 'D' transactions into new list
        for (Transactions transaction : transactions) {
            // If the transaction ID matches the one passed in (like "D" or "P"), add it to the filtered list
            if (transaction.getTransactionId().equals(id)) {
                matchingTransById.add(transaction);
            }
        }
        // Return the list of matching transactions
        return matchingTransById;

    }

    public static void reportMenu() {
        // This boolean keeps the report menu running until the user chooses to exit
        boolean reportMenuRunning = true;
        //while loop
        while(reportMenuRunning){
            System.out.println("""
                \n *** Select from following options: ***
                1. Month to date
                2. Previous month
                3. Year to date
                4. Previous year
                5. Search by vendor
                0. Back
                """);
            // Get the user’s menu choice and convert it from String to an integer
            int userChoice = Integer.parseInt(scanner.nextLine());
            // Based on what the user selects, run the appropriate method
            switch (userChoice) {
                case 1:
                    // Call method to get all transactions from this month up to today
                    List<Transactions> monthToDateTransactions = monthToDate(transactionFileName);
                    monthToDateTransactions.sort(Comparator.comparing(Transactions::getDateTime).reversed());
                    displayTransaction(monthToDateTransactions);
                    break;
                case 2:
                    List<Transactions> prevMonth = prevMonth(transactionFileName);
                    prevMonth.sort(Comparator.comparing(Transactions::getDateTime).reversed());
                    displayTransaction(prevMonth);
                    break;
                case 3:
                    List<Transactions> yearToDateTransactions = yearToDate(transactionFileName);
                    yearToDateTransactions.sort(Comparator.comparing(Transactions::getDateTime).reversed());
                    displayTransaction(yearToDateTransactions);
                    break;
                case 4:
                    List<Transactions> prevYear = prevYear(transactionFileName);
                    prevYear.sort(Comparator.comparing(Transactions::getDateTime).reversed());
                    displayTransaction(prevYear);
                    break;
                case 5:
                    List<Transactions> searchByVendorTransactions = searchByVendor(transactionFileName);
                    searchByVendorTransactions.sort(Comparator.comparing(Transactions::getDateTime).reversed());
                    displayTransaction(searchByVendorTransactions);
                    break;
                case 0:
                    //Exit the report menu loop
                    reportMenuRunning = false;
                    break;
            }
        }
    }

    public static List<Transactions> searchByVendor(String fileName) {
        //Get all transactions from csv file
        List<Transactions> transactions = getTransactionFromFile(fileName);
        //create new list to store matches found by vendor name
        List<Transactions> matchingVendors = new ArrayList<>();
        System.out.println("Enter vendors name: ");

        String userVendorsName = scanner.nextLine();
        //Loop through every transaction
        for (Transactions transaction : transactions) {
            // If the vendor name matches what the user typed (case-insensitive), add it to the list
            if (transaction.getVendor().equalsIgnoreCase(userVendorsName)) {
                matchingVendors.add(transaction);
            }
        }
        //return list of matching transactions
        return matchingVendors;
    }

    public static List<Transactions> yearToDate(String fileName) {
        //read all transactions from CSV file
        List<Transactions> transactions = getTransactionFromFile(fileName);
        //Create a list to hold only the "year to date" transactions
        List<Transactions> yearToDate = new ArrayList<>();

        LocalDateTime todayDate = LocalDateTime.now();
        //Set the date to the first day of the current year (January 1st)
        LocalDateTime firstDayOfYear = todayDate.withDayOfYear(1);
        //Loop through each transaction
        for (Transactions transaction : transactions) {
            LocalDateTime dateTime = transaction.getDateTime();
            //Check if the transaction is between Jan 1st and today//Stan helped with this
            if ((dateTime.isEqual(firstDayOfYear) || dateTime.isAfter(firstDayOfYear)) && ((dateTime.isEqual(todayDate) || dateTime.isBefore(todayDate)))) {
                //If yes, add it to the yearToDate list
                yearToDate.add(transaction);
            }
        }
        return yearToDate;
    }

    public static List<Transactions> monthToDate(String fileName) {
        List<Transactions> transactions = getTransactionFromFile(fileName);
        //Create a list to hold only the "month to date" transactions
        List<Transactions> monthToDate = new ArrayList<>();

        LocalDateTime todayDate = LocalDateTime.now();
        //Set the date to the first day of the current month-with hour 0 rep. midnight
        LocalDateTime firstDayOfMonth = todayDate.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        for (Transactions transaction : transactions) {
            LocalDateTime dateTransaction = transaction.getDateTime();
            //Check if the transaction is between the first day of the month and today
            if ((dateTransaction.isEqual(firstDayOfMonth) || dateTransaction.isAfter(firstDayOfMonth)) &&
                    ((dateTransaction.isEqual(todayDate)) || dateTransaction.isBefore(todayDate))) {
                //If yes, add it to the monthToDate list
                monthToDate.add(transaction);
            }

        }
        return monthToDate;
    }

    public static List<Transactions> prevMonth(String fileName){
        List<Transactions> transactions = getTransactionFromFile(fileName);
        List<Transactions> prevMonth = new ArrayList<>();

        LocalDateTime currentDate = LocalDateTime.now();
        //Calculate the first day of the previous month
        LocalDateTime firstDayOfMonth = currentDate.withDayOfMonth(1).minusMonths(1);
        //Calculate the last day of the previous month
        //This checks if the previous year was a leap year and gets the correct number of days for the month// Stan helped
        LocalDateTime lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.getMonth().length(LocalDate.of(firstDayOfMonth.getYear(),1,1).isLeapYear()));

        for (Transactions transaction : transactions){
            LocalDateTime prevMonthDT = transaction.getDateTime();
            if((prevMonthDT.isEqual(firstDayOfMonth) || prevMonthDT.isAfter(firstDayOfMonth)) &&
            (prevMonthDT.isEqual(lastDayOfMonth) || prevMonthDT.isBefore(lastDayOfMonth))){

                prevMonth.add(transaction);
            }
        }
        //Return the list of filtered transactions
        return prevMonth;


    }


    public static List<Transactions> prevYear (String fileName){
        List<Transactions> transactions = getTransactionFromFile(fileName);
        List<Transactions> prevYear = new ArrayList<>();

        LocalDateTime currentDate = LocalDateTime.now();
        //Find the first day of the previous year
        LocalDateTime firstDayOfYear = currentDate.minusYears(1).withDayOfYear(1);
        //Define the last day of that previous year (set to December 31st, 23:59:59)
        LocalDateTime lastDayOfYear = LocalDateTime.of(firstDayOfYear.getYear(),12, 31, 23, 59, 59);

                for (Transactions transaction : transactions){
                    LocalDateTime dateTime = transaction.getDateTime();
                    //Check if the transaction happened within the previous year
                    if((dateTime.isEqual(firstDayOfYear) || dateTime.isAfter(firstDayOfYear)) &&
                    (dateTime.isEqual(lastDayOfYear)) || dateTime.isBefore(lastDayOfYear)){
                        //Add the matching transaction to the list
                        prevYear.add(transaction);
                    }
                }
                //return filtered list
                return prevYear;




    }

}




