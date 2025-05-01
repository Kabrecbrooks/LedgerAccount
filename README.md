# Account Ledger App

A simple Java-based command-line application that allows users to track financial transactions such as deposits and payments. Users can view, filter, and generate reports from stored transaction data in a CSV file.



## 🚀 Features

- Add deposits and payments
- Store transactions in a CSV file
- Display all transactions
- View only deposits or only payments
- Generate reports:
  - Month to Date
  - Previous Month
  - Year to Date
  - Previous Year
  - Search by Vendor


## 📂 Project Structure

 -   `AccountLedgerApp.java` – The main program where the menus, user inputs, and logic are handled.
        
-   `Transactions.java` – A class that represents each transaction (deposit or payment).
        
-   `transaction.csv` – A CSV file that stores all the recorded transactions.
        
-   **README.md** – A documentation file that explains what the project does and how to use it.


## 🛠 Requirements

- Java 17 or higher
- A terminal or IDE to run the application


## 🧪 How to Run

1. Clone or download the repository.
2. Open the project in your preferred IDE (like IntelliJ or VS Code).
3. Make sure `transaction.csv` exists in the `src/` folder. If it doesn’t, the app will create it when you record your first transaction.
4. Run `AccountLedgerApp.java`.



## 🧾 File Format (`transaction.csv`)

Each line in the CSV file follows this format:

- `transactionType`:
  - `D` for Deposit
  - `P` for Payment


## 📊 Example Usage

```bash
** WHAT WOULD YOU LIKE TO DO TODAY? **
A. Add a deposit
B. Make a payment
C. Ledger screen
D. Exit

> A
Provide deposit amount: 200
Enter vendor: PayPal
Enter item description: Refund
Complete!
