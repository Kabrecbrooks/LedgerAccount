import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
                case "D":
                    runningHomeProgram = false; // this is to get the program to stop running
                    break;

            }

        }
    }

    public static Transactions addDeposit() {

        // these will primitives after users inputs
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
    public static Transactions namePayment() {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();

        System.out.println("Please enter the payment amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        System.out.println("Please enter the vendor name: ");
        String vendor = scanner.nextLine().trim();

        System.out.println("Please provide a description: ");
        String description = scanner.nextLine().trim();

        System.out.println("Your information has been deposited! ");
        String transactionId = "P";

        return new Transactions(ld, lt, description, transactionId, vendor, amount);
    }
}

