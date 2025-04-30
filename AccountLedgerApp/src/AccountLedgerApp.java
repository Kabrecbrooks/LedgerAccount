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
    static Scanner scanner = new Scanner(System.in);
    static String transactionFileName = "src/transaction.csv"; // inputting here so it can be accessible
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {

        //Home screen
        homeMenu();


    }

    // creating my menu method with prompts
    public static void homeMenu() {
        System.out.println("\n ** WELCOME TO YOUR ACCOUNT LEDGER ** \n");

        // until the user exits the app will continue to run with this going
        boolean runningHomeProgram = true;

        //next, creating a loop to go until the condition is met
        while (runningHomeProgram) {
            //Give user options
            System.out.println("""
                    \n ** WHAT WOULD YOU LIKE TO DO TODAY? ** \n
                    A. Add a deposit 
                    B. Make a payment
                    C. Ledger screen
                    D. Exit """);
            // user makes their selection
            String userInput = scanner.nextLine().trim().toUpperCase();
            // creating a switch statement for user to choose which option they will want
            switch (userInput) {
                case "A":
                    Transactions newDeposit = addDeposit();
                    writeToFile(transactionFileName, newDeposit);
                    break;
                case "B":
                    Transactions newPayment = makePayment();
                    writeToFile(transactionFileName, newPayment);
                    break;
                case "C":
                    ledgerMenu();
                    break;
                case "D":
                    runningHomeProgram = false; // this is to get the program to stop running
                    break;

            }

        }
    }

    public static Transactions addDeposit() {

        // primitives after users inputs
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        System.out.println(" Provide deposit amount ");
        double amount = Double.parseDouble(scanner.nextLine());// main scanner?

        System.out.println(" Enter vendor : ");
        String vendor = scanner.nextLine().trim();

        System.out.println(" Enter item description: ");
        String description = scanner.nextLine().trim();

        System.out.println(" Complete! ");
        String transactionId = "D";

        //call the constructor out
        // Takes user input and create single deposit transaction
        return new Transactions(ld, lt, description, vendor, transactionId, amount);

    }

    // file method to write
    // add the string variable, class and object
    public static void writeToFile(String fileName, Transactions transaction) {
        // allows to add more to the file w.o losing what was inputted before
        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.write(transaction.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Something went wrong " + e.getMessage()); // this is for if something goes wrong

        }
    }

    // My payment screen
    public static Transactions makePayment() {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();

        System.out.println("Please enter the payment amount: ");
        double amount = Double.parseDouble((scanner.nextLine())) * -1;

        System.out.println("Please enter the vendor name: ");
        String vendor = scanner.nextLine().trim();

        System.out.println("Please provide a description: ");
        String description = scanner.nextLine().trim();

        System.out.println("Deposit is complete! ");
        String transactionId = "P";

        return new Transactions(ld, lt, description, vendor, transactionId, amount);
    }

    // Creating my ledger menu with methods
    public static void ledgerMenu() {

        System.out.println("\n ** Ledger main menu ** \n");
        // ledger screen will continue to run until the user decides to exit
        boolean ledgerRunning = true;

        // loop to repeat code until condition is met
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
            // this is how the user will make their selection
            String userInputLedger = scanner.nextLine().trim().toUpperCase();


            // allow usr to make a choice by creating switch statement
            switch (userInputLedger) {
                case "A":
                    List<Transactions> transactions = getTransactionFromFile(transactionFileName);
                    transactions.sort(Comparator.comparing(Transactions::getDateTime).reversed());
                    displayTransaction(transactions);
                    break;
                case "D":
                    List<Transactions> depositTransaction = searchTransById("D", "Deposit");
                    depositTransaction.sort(Comparator.comparing(Transactions::getDateTime).reversed());
                    displayTransaction(depositTransaction);
                    break;
                case "P":
                    List<Transactions> paymentTransaction = searchTransById("P", "Payments");
                    paymentTransaction.sort(Comparator.comparing(Transactions::getDateTime).reversed());
                    displayTransaction(paymentTransaction);
                    break;
                case "R":
                    reportMenu();
                    break;
                case "H":
                    ledgerRunning = false;
                    break;


            }
        }

    }

    // Displaying all entries
    public static List<Transactions> getTransactionFromFile(String fileName) {
        List<Transactions> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arrTransaction = line.split("\\|");
                Transactions transaction = new Transactions(LocalDate.parse(arrTransaction[0], dateFormatter), LocalTime.parse(arrTransaction[1], timeFormatter), arrTransaction[2], arrTransaction[3], arrTransaction[4], Double.parseDouble(arrTransaction[5]));
                transactions.add(transaction);

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return transactions;
    }

    // displaying all entries
    public static void displayTransaction(List<Transactions> transactions) {
        for (Transactions transaction : transactions) {
            System.out.printf("%s | %s | %s | %s | %.2f%n", transaction.getDate().format(dateFormatter), transaction.getTime().format(timeFormatter), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
        }
    }

    //showing all deposits
    public static List<Transactions> searchTransById(String id, String TransTypeName) {
        System.out.println("\n ** Showing Deposits & Payments " + TransTypeName + ": **\n");

        //list containing all transactions
        List<Transactions> transactions = getTransactionFromFile(transactionFileName);

        // Empty list where I will store all deposit transactions
        List<Transactions> matchingTransById = new ArrayList<>();

        // for each loop where it will enter into csv file and sort out 'D' transactions into new list
        for (Transactions transaction : transactions) {
            if (transaction.getTransactionId().equals(id)) {
                matchingTransById.add(transaction);
            }
        }
        return matchingTransById;

    }

    public static void reportMenu() {
        boolean reportMenuRunning = true;
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
            int userChoice = Integer.parseInt(scanner.nextLine());

            switch (userChoice) {
                case 1:
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
                    reportMenuRunning = false;
                    break;
            }
        }
    }

    public static List<Transactions> searchByVendor(String fileName) {
        List<Transactions> transactions = getTransactionFromFile(fileName);
        List<Transactions> matchingVendors = new ArrayList<>();
        System.out.println("Enter vendors name: ");

        String userVendorsName = scanner.nextLine();

        for (Transactions transaction : transactions) {
            if (transaction.getVendor().equals(userVendorsName)) {
                matchingVendors.add(transaction);
            }
        }
        return matchingVendors;
    }

    public static List<Transactions> yearToDate(String fileName) {

        List<Transactions> transactions = getTransactionFromFile(fileName);
        List<Transactions> yearToDate = new ArrayList<>();

        LocalDateTime todayDate = LocalDateTime.now();
        LocalDateTime firstDayOfYear = todayDate.withDayOfYear(1);

        for (Transactions transaction : transactions) {
            LocalDateTime dateTime = transaction.getDateTime();

            if ((dateTime.isEqual(firstDayOfYear) || dateTime.isAfter(firstDayOfYear)) && ((dateTime.isEqual(todayDate) || dateTime.isBefore(todayDate)))) {
                yearToDate.add(transaction);
            }
        }
        return yearToDate;
    }

    public static List<Transactions> monthToDate(String fileName) {
        List<Transactions> transactions = getTransactionFromFile(fileName);
        List<Transactions> monthToDate = new ArrayList<>();

        LocalDateTime todayDate = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = todayDate.withDayOfMonth(1);

        for (Transactions transaction : transactions) {
            LocalDateTime dateTransaction = transaction.getDateTime();
            if ((dateTransaction.isEqual(firstDayOfMonth) || dateTransaction.isAfter(firstDayOfMonth)) &&
                    ((dateTransaction.isEqual(todayDate)) || dateTransaction.isBefore(todayDate))) {

                monthToDate.add(transaction);
            }

        }
        return monthToDate;


    }
    public static List<Transactions> prevMonth(String fileName){
        List<Transactions> transactions = getTransactionFromFile(fileName);
        List<Transactions> prevMonth = new ArrayList<>();

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = currentDate.withDayOfMonth(1).minusMonths(1);
        LocalDateTime lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.getMonth().length(LocalDate.of(firstDayOfMonth.getYear(),1,1).isLeapYear()));

        for (Transactions transaction : transactions){
            LocalDateTime prevMonthDT = transaction.getDateTime();
            if((prevMonthDT.isEqual(firstDayOfMonth) || prevMonthDT.isAfter(firstDayOfMonth)) &&
            (prevMonthDT.isEqual(lastDayOfMonth) || prevMonthDT.isBefore(lastDayOfMonth))){

                prevMonth.add(transaction);
            }
        }

        return prevMonth;


    }
    public static List<Transactions> prevYear (String fileName){
        List<Transactions> transactions = getTransactionFromFile(fileName);
        List<Transactions> prevYear = new ArrayList<>();

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime firstDayOfYear = currentDate.minusYears(1).withDayOfYear(1);
        LocalDateTime lastDayOfYear = LocalDateTime.of(firstDayOfYear.getYear(),12, 31, 23, 59, 59);

                for (Transactions transaction : transactions){
                    LocalDateTime dateTime = transaction.getDateTime();
                    if((dateTime.isEqual(firstDayOfYear) || dateTime.isAfter(firstDayOfYear)) &&
                    (dateTime.isEqual(lastDayOfYear)) || dateTime.isBefore(lastDayOfYear)){

                        prevYear.add(transaction);
                    }
                }

                return prevYear;




    }

}




