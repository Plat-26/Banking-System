package banking;

import java.util.Random;
import java.util.Scanner;

class BankingSystem {

    private Operations ops;

    BankingSystem(BankData data) {
        this.ops = new Operations(data);
    }

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
        String userInput = ops.getLoginDetails();

        if (userInput != null) {

            String[] userData = userInput.split(" ");

            if (ops.validateUserLogin(userData[0])) {

                int balance = ops.getAccount(userData[0], userData[1]);

                if (balance >= 0) {
                    System.out.println("\nYou have successfully logged in!");
                    loggedInMenu(balance);
                    return;
                }
            }
        }

        System.out.println("\nWrong card number or PIN!");
    }

    private void loggedInMenu(int balance) {
        ops.printLogInMenu();
        int userChoice = ops.validateInput("[012]");

        switch (userChoice) {

            case 0: exit();
                break;
            case 1: System.out.println("\nBalance: " + balance);
                loggedInMenu(balance);
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
        System.exit(0);
    }

}


class Operations {

    BankData data;

    Operations(BankData data) {
        this.data = data;
    }

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

    int getAccount(String cardNumber, String userPin) {

        return data.getData(cardNumber, userPin);
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

        return data.updateTable(number, pin);
    }

    boolean validateUserLogin(String cardNumber) {

        return checkLuhn(cardNumber);
    }

    String getLoginDetails() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nEnter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String userPin = scanner.nextLine();

        if (validateInput(cardNumber,"[0-9]{16}") && validateInput(userPin,"[0-9]{4}")){

            return cardNumber + " " + userPin;
        }

        return null;
    }

    private boolean validateInput(String userInput, String txtForRegex) {

        return userInput.matches(txtForRegex);
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