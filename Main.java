package banking;

import org.sqlite.SQLiteDataSource;

public class Main {
    public static void main(String[] args) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        String url = "jdbc:sqlite:" + args[1];
        dataSource.setUrl(url);

        BankData data = new BankData(dataSource);
        data.createTable();

        BankingSystem bank = new BankingSystem(data);
        bank.startMenu();
    }
}


