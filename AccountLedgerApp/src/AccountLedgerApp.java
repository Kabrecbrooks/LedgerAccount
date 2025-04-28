import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        ledgerMenu();


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
                case "B":
                    Transactions newPayment = makePayment();
                    writeToFile(transactionFileName,newPayment);
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
        String transactionId = "A";

        //call the constructor out
        // Takes user input and create single deposit transaction
        return new Transactions(ld, lt, description, transactionId, vendor, amount);

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
        double amount = Double.parseDouble((scanner.nextLine()))* -1;

        System.out.println("Please enter the vendor name: ");
        String vendor = scanner.nextLine().trim();

        System.out.println("Please provide a description: ");
        String description = scanner.nextLine().trim();

        System.out.println("Deposit is complete! ");
        String transactionId = "B";

        return new Transactions(ld, lt, description, transactionId, vendor, amount);
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
                    B. Deposits
                    C. Payments
                    D. Reports
                    E. Home
                    """);
        // this is how the user will make their selection
        String userInputLedger = scanner.nextLine().trim().toUpperCase();

        // allow usr to make a choice by creating switch statement
        switch (userInputLedger) {
            case "A":
                List<Transactions> transactions = getTransactionFromFile(transactionFileName);
                displayTransaction(transactions);
                break;
            case "B":
                ledgerRunning = false;
                break;



        }
        }

    }
    public static List<Transactions> getTransactionFromFile(String fileName){
        List<Transactions> transactions = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line;
            while((line = br.readLine())!= null){
                String[] arrTransaction = line.split("\\|");
                Transactions transaction = new Transactions(LocalDate.parse(arrTransaction[0]),LocalTime.parse(arrTransaction[1]),arrTransaction[2],arrTransaction[3],arrTransaction[4],Double.parseDouble(arrTransaction[5]));
                transactions.add(transaction);

            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        return transactions;
    }
    public static void displayTransaction(List<Transactions>transactions){
        for(Transactions transaction: transactions){
            System.out.printf("%s | %s | %s | %s | %s | %.2f%n", transaction.getDate().format(dateFormatter), transaction.getTime().format(timeFormatter), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
        }
    }
}




