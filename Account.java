package banking;

class Account {

    private final long NUMBER;
    private final String PIN;
    private double balance;

    public Account(String number, String pin) {
        this.NUMBER = Long.parseLong(number);
        this.PIN = pin;
        this.balance = 0;
    }

    public String getPIN() {
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