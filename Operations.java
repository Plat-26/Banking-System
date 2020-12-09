package banking;

import java.util.Random;
import java.util.Scanner;

class Operations {

    BankData data;

    Operations(BankData data) {
        this.data = data;
    }

    void printLogInMenu() {
        System.out.println( "1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
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

    boolean validateAccount(String cardNumber, String userPin) {

        return data.isValid(cardNumber, userPin);
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

        return data.addNewAccount(number, pin);
    }

    void getUserBalance(String cardNumber, String userPin) {

        System.out.println("\nBalance: " + data.getBalance(cardNumber, userPin));
    }

    void addIncome(String cardNumber, String userPin) {
        System.out.println("\nEnter income:");
        int income = validateInput("\\d+");

        if (data.updateBalance(cardNumber, userPin, income)) {
            System.out.println("\nIncome was added!");
            return;
        }

        System.out.println("Income was not added");
    }

    void doTransfer (String cardNumber, String userPin) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nTransfer" + "\nEnter card number:");
        String recipientNumber = scanner.nextLine();

        if (!validateUserLogin(recipientNumber)) {
            System.out.println("\nProbably you made mistake in the card number. Please try again!");
            return;
        }

        if (!data.isValid(recipientNumber)){
            System.out.println("\nSuch a card does not exist.");
            return;
        }

        System.out.println("\nEnter how much money you want to transfer:");
        int amount = validateInput("\\d+");
        data.userTransfer(cardNumber, userPin, recipientNumber, amount);
    }

    void closeAccount(String cardNumber, String userPin) {

        if (data.closeAccount(cardNumber, userPin)) {
            System.out.println("The account has been closed!");
        }
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