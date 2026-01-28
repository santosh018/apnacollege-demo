package internship_with_java;

class BankAccount {

    int accountNumber;
    String holderName;
    double balance;

    //default constructor
    BankAccount() {
        this.accountNumber = 0;
        this.holderName = "";
        this.balance = 0.0;
    }

    //parameterized constructor
    BankAccount(int accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }


    void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("Deposited: " + amount);
            System.out.println("Avl Balance: " + this.balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    void withdraw(double amount) {
        if (amount <= balance) {
            this.balance -= amount;
            System.out.println("Withdrawn: " + amount);
            System.out.println("Avl Balance: " + this.balance);
        } else {
            System.out.println("Insufficient balance" + this.balance);
        }
    }

    void display() {
        System.out.println("\n--- Account Details ---");
        System.out.println("Account Number: " + this.accountNumber);
        System.out.println("Holder Name:    " + this.holderName);
        System.out.println("Current Balance:" + this.balance);
        System.out.println("-----------------------\n");
    }
}

public class Bank {
    public static void main(String[] args) {

        //default
        BankAccount acc1 = new BankAccount();
        acc1.display();

        //parameterized
        BankAccount acc2 = new BankAccount(05, "santosh", 600000000);
        acc2.deposit(4000000);
        acc2.withdraw(17000000);
        acc2.display();


    }
}