package internship_with_java;

import java.util.Scanner;

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class InvalidPinException extends Exception {
    public InvalidPinException(String message) {
        super(message);
    }
}

class Account {
    // Private fields - encapsulated
    private String accountNumber;
    private String pin;
    private double balance;
    private int failedPinAttempts;
    private boolean isLocked;

    // Constructor
    public Account(String accountNumber, String pin, double initialBalance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialBalance;
        this.failedPinAttempts = 0;
        this.isLocked = false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean validatePin(String inputPin) throws InvalidPinException {
        if (isLocked) {
            throw new InvalidPinException("Account is locked. Please contact customer service.");
        }

        if (inputPin.equals(pin)) {
            failedPinAttempts = 0; // Reset failed attempts on successful login
            return true;
        } else {
            failedPinAttempts++;

            if (failedPinAttempts >= 3) {
                isLocked = true;
                throw new InvalidPinException("Account locked due to 3 failed attempts. Please contact customer service.");
            } else {
                int attemptsLeft = 3 - failedPinAttempts;
                throw new InvalidPinException("Invalid PIN. " + attemptsLeft + " attempt(s) remaining.");
            }
        }
    }

    public void deposit(double amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
        System.out.printf("Successfully deposited $%.2f\n", amount);
    }

    public void withdraw(double amount) throws InsufficientFundsException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }

        if (amount > balance) {
            throw new InsufficientFundsException(
                    String.format("Insufficient funds. You tried to withdraw $%.2f but only have $%.2f",
                            amount, balance)
            );
        }

        balance -= amount;
        System.out.printf("Successfully withdrew $%.2f\n", amount);
    }

    public void checkBalance() {
        System.out.printf("Current balance: $%.2f\n", balance);
    }

    // Method to reset failed attempts (for demo purposes)
    public void resetFailedAttempts() {
        this.failedPinAttempts = 0;
        this.isLocked = false;
    }
}

public class ATM {
    private static Scanner scanner = new Scanner(System.in);
    private static Account currentAccount;

    public static void main(String[] args) {
        System.out.println(" WELCOME TO JAVA BANK ATM ");

        currentAccount = new Account("1234567890", "9931", 1000.0);

        if (!authenticateUser()) {
            System.out.println("ATM session terminated.");
            return;
        }

        showMainMenu();

        System.out.println("\nThank you for using Java Bank ATM. Have a nice day!");
        scanner.close();
    }

    private static boolean authenticateUser() {
        System.out.println("\n ACCOUNT LOGIN ");

        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Enter your 4-digit PIN: ");
            String inputPin = scanner.nextLine().trim();

            try {
                if (currentAccount.validatePin(inputPin)) {
                    System.out.println("Login successful!");
                    return true;
                }
            } catch (InvalidPinException e) {
                System.out.println("Error: " + e.getMessage());

                if (currentAccount.isLocked()) {
                    System.out.println("Account locked. Terminating session.");
                    return false;
                }

                attempts++;

                if (attempts == MAX_ATTEMPTS) {
                    System.out.println("Maximum login attempts reached. Terminating session.");
                    return false;
                }
            }
        }

        return false;
    }

    private static void showMainMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Exit");
            System.out.print("Select an option (1-4): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    checkBalance();
                    break;
                case "2":
                    depositMoney();
                    break;
                case "3":
                    withdrawMoney();
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please select 1-4.");
            }
        }
    }

    private static void checkBalance() {
        System.out.println("\n--- BALANCE INQUIRY ---");
        currentAccount.checkBalance();
    }

    // Deposit transaction
    private static void depositMoney() {
        System.out.println("\n--- DEPOSIT MONEY ---");

        double amount = getValidAmount("Enter deposit amount: $");

        try {
            currentAccount.deposit(amount);
            System.out.println("Transaction completed successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        }
    }

    private static void withdrawMoney() {
        System.out.println("\n--- WITHDRAW MONEY ---");

        double amount = getValidAmount("Enter withdrawal amount: $");

        try {
            currentAccount.withdraw(amount);
            System.out.println("Transaction completed successfully.");
        } catch (InsufficientFundsException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        }
    }

    private static double getValidAmount(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                double amount = Double.parseDouble(input);

                amount = Math.round(amount * 100.0) / 100.0;

                if (amount <= 0) {
                    System.out.println("Amount must be greater than 0. Please try again.");
                    continue;
                }

                return amount;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
            }
        }
    }
}