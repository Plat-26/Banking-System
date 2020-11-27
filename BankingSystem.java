package banking;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;


public class BankingSystem {
    public static void main(String[] args) {
		
        Bank bank = new Bank();
        bank.startMenu();
    }
}

class Account {

    private final long NUMBER;
    private final int PIN;
    private double balance;

    public Account(String number, int pin) {
        this.NUMBER = Long.parseLong(number);
        this.PIN = pin;
        this.balance = 0;
    }

    public int getPIN() {
        return PIN;
    }

    public long getNUMBER() {
        return NUMBER;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance += balance;
    }
}

class Bank {

    Operations ops = new Operations();

    void startMenu() {
        ops.printStartMenu();
        int userChoice = ops.validateInput("[012]");

        switch (userChoice) {

            case 0: exit();
                    break;
            case 1: createAccount();
                    startMenu();
                    break;
            case 2: logIn();
                    startMenu();
                    break;
        }
    }

    private void logIn() {
        System.out.println("\nEnter your card number:");
        String cardNumber = ops.validateInput("[0-9]{16}", true);
        System.out.println("Enter your PIN:");
        int userPin = ops.validateInput("[0-9]{4}");

        if (ops.validateUserLogin(cardNumber, userPin)) {
            Account acct = ops.getAccount(userPin);
            System.out.println("\nYou have successfully logged in!");
            loggedInMenu(acct);

        } else {
            System.out.println("\nWrong card number or PIN!");
        }
    }

    private void loggedInMenu(Account acct) {
        ops.printLogInMenu();
        int userChoice = ops.validateInput("[012]");

        switch (userChoice) {

            case 0: exit();
                    break;
            case 1: System.out.println("\nBalance: " + acct.getBalance());
                    loggedInMenu(acct);
                    break;
            case 2: System.out.println("\nYou have successfully logged out!");
                    break;
        }
    }


    private void createAccount() {
        String[] accountDetails = (ops.createNewAccount()).split(" ");
        if (accountDetails[0] != null) {
            System.out.println("\nYour card has been created");
            System.out.printf("\nYour card number:\n%s", accountDetails[0]);
            System.out.printf("\nYour card PIN:\n%s\n", accountDetails[1]);
        }
    }

    private void exit() {

        System.out.println("\nBye!");
    }

}


class Operations {

    private HashMap<Integer, Account> accounts = new HashMap<>();

    void printLogInMenu() {
        System.out.println(
                "\n1. Balance\n" +
                        "2. Log out\n" +
                        "0. Exit"
        );
    }

    void printStartMenu() {
        System.out.println("\n1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit");
    }

    String createNewAccount() {

        String number = generateCardNumber();

        int pin = generateUserPin();

        if (put(number, pin)) {

            return number + " " + pin;
        }

        return null;
    }

    Account getAccount(int userPin) {

        return accounts.get(userPin);
    }

    private int generateUserPin() {
        StringBuilder pinNumber = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            pinNumber.append(random.nextInt(10));
        }

        return Integer.parseInt(pinNumber.toString());
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder("400000");
        Random random = new Random();

        for (int i = 0; i < 9; i++) {
            cardNumber.append(random.nextInt(10));
        }
        cardNumber.append(5);
        return cardNumber.toString();
    }

    private boolean put(String number, int pin){

        Account account = accounts.computeIfAbsent(pin, k -> new Account(number, pin));

        return account != null;
    }

    boolean validateUserLogin(String cardNumber, int userPin) {

        long cardNum = Long.parseLong(cardNumber);

        if ((accounts.get(userPin)) != null) {
            return accounts.get(userPin).getNUMBER() == cardNum;
        }

        return false;
    }

    String validateInput(String txtForRegex, boolean useString) {
        Scanner scanner = new Scanner(System.in);

        if (useString) {

            String userInput = scanner.nextLine();
            while(!userInput.matches(txtForRegex)) {
                System.out.println("Invalid format");
                userInput = scanner.nextLine();
            }
            return userInput;
        }
        return null;
    }

    int validateInput(String txtForRegex) {
        Scanner scanner = new Scanner(System.in);

        String userInput = scanner.nextLine();
        while(!userInput.matches(txtForRegex)) {
            System.out.println("Invalid format");
            userInput = scanner.nextLine();
        }
        return Integer.parseInt(userInput);
    }
}