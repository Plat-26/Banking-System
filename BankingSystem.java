package banking;

class BankingSystem {

    final private Operations ops;

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

                if (ops.validateAccount(userData[0], userData[1])) {

                    System.out.println("\nYou have successfully logged in!");
                    loggedInMenu(userData[0], userData[1]);
                    return;
                }
            }
        }

        System.out.println("\nWrong card number or PIN!");
    }

    private void loggedInMenu(String cardNumber, String userPin) {
        ops.printLogInMenu();
        int userChoice = ops.validateInput("[012345]");

        switch (userChoice) {

            case 0: exit();
                    break;
            case 1: ops.getUserBalance(cardNumber, userPin);
                    break;
            case 2: ops.addIncome(cardNumber, userPin);
                    break;
            case 3: ops.doTransfer(cardNumber, userPin);
                    break;
            case 4: ops.closeAccount(cardNumber, userPin);
                    break;
            case 5: System.out.println("\nYou have successfully logged out!");
                    break;
        }
        loggedInMenu(cardNumber, userPin);
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


