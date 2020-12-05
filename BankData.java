package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class BankData {

    SQLiteDataSource dataSource;

    BankData(SQLiteDataSource dataSource) {
        this.dataSource = dataSource;
    }

    void createTable() {

        String createTable = "CREATE TABLE IF NOT EXISTS card(" +
                "    id INTEGER," +
                "    number TEXT," +
                "    pin TEXT," +
                "    balance INTEGER DEFAULT 0" +
                ");";

        try (Connection connection = dataSource.getConnection()) {

            try (Statement statement = connection.createStatement()) {


                statement.executeUpdate(createTable);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    boolean updateTable(String cardNumber, String userPin) {

        int i = -1;

        String updateTable = "INSERT INTO card (number, pin) VALUES('"+ cardNumber + "','" + userPin +"');";
        try (Connection connection = dataSource.getConnection()) {

            try (Statement statement = connection.createStatement()) {

                i = statement.executeUpdate(updateTable);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return i == 1;
    }


    int getData(String cardNumber, String userPin) {

        int balance = -1;

        String selectData = "SELECT * FROM card WHERE number ='" + cardNumber + "'AND pin ='" + userPin + "';" ;

        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                try (ResultSet userInfo = statement.executeQuery(selectData)) {

                    while (userInfo.next()) {

                        balance = userInfo.getInt("balance");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return balance;

    }

}