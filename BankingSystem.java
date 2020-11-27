package banking;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

class BankingSystem {

    private Operations ops = new Operations();

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
        String userPin = ops.validateInput("[0-9]{4}", true);

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

            case 0: System.out.println();
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

    private HashMap<String, Account> accounts = new HashMap<>();

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

        String cardNum = generateCardNumber();

        String pin = generateUserPin();

        if (put(cardNum, pin)) {

            return cardNum + " " + pin;
        }

        return null;
    }

    Account getAccount(String userPin) {

        return accounts.get(userPin);
    }

    private String generateUserPin() {
        StringBuilder pinNumber = new StringBuilder();
        Random random = new Random();

        while(pinNumber.length() < 4) {
            pinNumber.append(random.nextInt(10));
        }

        return pinNumber.toString();
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder("400000");
        Random random = new Random();

        while(cardNumber.length() < 15) {

            cardNumber.append(random.nextInt(10));
        }
        return createSafeCard(cardNumber.toString());
    }

    private int processNum(char val, boolean isOdd) {
        int num = Integer.parseInt(Character.toString(val));

        if (isOdd) {
            num = num * 2;
        }

        return (num > 9) ? (num - 9) : num;
    }

    private int addNumbers(String number) {
        int sum = 0;
        int index = 1;

        for (int i = 0; i < 15; i++) {

            if (index % 2 == 0) {

                sum += processNum(number.charAt(i), false);
            } else {

                sum += processNum(number.charAt(i), true);
            }
            index++;
        }


        return sum;
    }


    private String createSafeCard(String number) {
        int rem = addNumbers(number) % 10;

        int controlNum = (rem != 0) ? (10 - rem) : rem;

        return number + "" + controlNum;
    }

    private boolean checkLuhn(String cardNum) {
        int rem = (addNumbers(cardNum) + Integer.parseInt(Character.toString(cardNum.charAt(15)))) % 10;

        return rem == 0;
    }

    private boolean put(String number, String pin) {

        Account account = accounts.computeIfAbsent(pin, k -> new Account(number, pin));

        return account != null;
    }

    boolean validateUserLogin(String cardNumber, String userPin) {

        if (checkLuhn(cardNumber)) {
            long cardNum = Long.parseLong(cardNumber);

            if ((accounts.get(userPin)) != null) {
                return accounts.get(userPin).getNUMBER() == cardNum;
            }
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